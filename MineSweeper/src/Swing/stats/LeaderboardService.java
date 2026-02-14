package Swing.stats;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * Gerencia o leaderboard do jogo, mantendo top 10 por dificuldade.
 */
public class LeaderboardService {

    private static final String ARQUIVO_NOME = "minesweeper-leaderboard.properties";
    private static final String PASTA_DADOS = "MineSweeper";
    private static final String SUBPASTA_DADOS = "data";
    private static final int LIMITE_ENTRADAS = 10;
    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final Path caminhoArquivo;
    private final EnumMap<DificuldadeJogo, List<LeaderboardEntry>> rankings;

    /**
     * Cria o serviço de leaderboard com carga inicial em disco.
     */
    public LeaderboardService() {
        String diretorioExecucao = System.getProperty("user.dir");
        Path caminhoBaseProjeto = Paths.get(diretorioExecucao);

        if (Files.exists(caminhoBaseProjeto.resolve(PASTA_DADOS))) {
            caminhoBaseProjeto = caminhoBaseProjeto.resolve(PASTA_DADOS);
        }

        this.caminhoArquivo = caminhoBaseProjeto.resolve(SUBPASTA_DADOS).resolve(ARQUIVO_NOME);
        this.rankings = carregarDoDisco();
    }

    /**
     * Adiciona um novo resultado ao leaderboard da dificuldade.
     *
     * @param dificuldade   dificuldade da partida
     * @param nomeJogador   nome do jogador
     * @param tempoSegundos tempo da partida em segundos
     */
    public void adicionarResultado(DificuldadeJogo dificuldade, String nomeJogador, int tempoSegundos) {
        if (dificuldade == null) {
            return;
        }

        List<LeaderboardEntry> lista = rankings.computeIfAbsent(dificuldade, chave -> new ArrayList<>());
        lista.add(new LeaderboardEntry(nomeJogador, tempoSegundos, LocalDateTime.now()));
        ordenarETruncar(lista);
        salvarNoDisco();
    }

    /**
     * Obtém o ranking de uma dificuldade.
     *
     * @param dificuldade dificuldade consultada
     * @return cópia da lista ordenada
     */
    public List<LeaderboardEntry> getRanking(DificuldadeJogo dificuldade) {
        List<LeaderboardEntry> lista = rankings.getOrDefault(dificuldade, new ArrayList<>());
        return new ArrayList<>(lista);
    }

    /**
     * Gera texto formatado para visualização do leaderboard.
     *
     * @return ranking formatado por dificuldade
     */
    public String gerarResumoRanking() {
        StringBuilder builder = new StringBuilder();
        builder.append("Leaderboard - Top 10 por dificuldade\n\n");

        appendRankingDificuldade(builder, DificuldadeJogo.FACIL);
        builder.append('\n');
        appendRankingDificuldade(builder, DificuldadeJogo.MEDIO);
        builder.append('\n');
        appendRankingDificuldade(builder, DificuldadeJogo.DIFICIL);
        builder.append('\n');
        appendRankingDificuldade(builder, DificuldadeJogo.PERSONALIZADO);

        return builder.toString();
    }

    private void appendRankingDificuldade(StringBuilder builder, DificuldadeJogo dificuldade) {
        builder.append(dificuldade.getNomeExibicao()).append(':').append('\n');
        List<LeaderboardEntry> lista = rankings.getOrDefault(dificuldade, new ArrayList<>());

        if (lista.isEmpty()) {
            builder.append("  -- sem registos --\n");
            return;
        }

        for (int i = 0; i < lista.size(); i++) {
            LeaderboardEntry entry = lista.get(i);
            builder.append(String.format(Locale.US, "  %d) %s - %s - %s\n",
                    i + 1,
                    entry.getNomeJogador(),
                    formatarTempo(entry.getTempoSegundos()),
                    entry.getDataRegistro().format(FORMATADOR_DATA)));
        }
    }

    private EnumMap<DificuldadeJogo, List<LeaderboardEntry>> carregarDoDisco() {
        EnumMap<DificuldadeJogo, List<LeaderboardEntry>> dados = new EnumMap<>(DificuldadeJogo.class);
        for (DificuldadeJogo dificuldade : DificuldadeJogo.values()) {
            dados.put(dificuldade, new ArrayList<>());
        }

        if (!Files.exists(caminhoArquivo)) {
            return dados;
        }

        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(caminhoArquivo)) {
            properties.load(input);
        } catch (IOException ignored) {
            return dados;
        }

        for (DificuldadeJogo dificuldade : DificuldadeJogo.values()) {
            List<LeaderboardEntry> lista = dados.get(dificuldade);
            for (int i = 0; i < LIMITE_ENTRADAS; i++) {
                String prefixo = "leaderboard." + dificuldade.name() + "." + i;
                String nome = properties.getProperty(prefixo + ".nome");
                int tempo = parseInt(properties.getProperty(prefixo + ".tempo"), -1);
                String dataRaw = properties.getProperty(prefixo + ".data");

                if (nome == null || tempo < 0 || dataRaw == null) {
                    continue;
                }

                LocalDateTime dataRegistro;
                try {
                    dataRegistro = LocalDateTime.parse(dataRaw);
                } catch (Exception e) {
                    dataRegistro = LocalDateTime.now();
                }

                lista.add(new LeaderboardEntry(nome, tempo, dataRegistro));
            }
            ordenarETruncar(lista);
        }

        return dados;
    }

    private void salvarNoDisco() {
        Properties properties = new Properties();

        for (DificuldadeJogo dificuldade : DificuldadeJogo.values()) {
            List<LeaderboardEntry> lista = rankings.getOrDefault(dificuldade, new ArrayList<>());
            for (int i = 0; i < lista.size() && i < LIMITE_ENTRADAS; i++) {
                LeaderboardEntry entry = lista.get(i);
                String prefixo = "leaderboard." + dificuldade.name() + "." + i;
                properties.setProperty(prefixo + ".nome", entry.getNomeJogador());
                properties.setProperty(prefixo + ".tempo", String.valueOf(entry.getTempoSegundos()));
                properties.setProperty(prefixo + ".data", entry.getDataRegistro().toString());
            }
        }

        try {
            Path diretorio = caminhoArquivo.getParent();
            if (diretorio != null) {
                Files.createDirectories(diretorio);
            }

            try (OutputStream output = Files.newOutputStream(caminhoArquivo)) {
                properties.store(output, "Leaderboard do jogador - Minesweeper");
            }
        } catch (IOException ignored) {
        }
    }

    private void ordenarETruncar(List<LeaderboardEntry> lista) {
        lista.sort(Comparator
                .comparingInt(LeaderboardEntry::getTempoSegundos)
                .thenComparing(LeaderboardEntry::getDataRegistro));

        if (lista.size() > LIMITE_ENTRADAS) {
            lista.subList(LIMITE_ENTRADAS, lista.size()).clear();
        }
    }

    private String formatarTempo(int segundosTotais) {
        int minutos = Math.max(0, segundosTotais) / 60;
        int segundos = Math.max(0, segundosTotais) % 60;
        return String.format(Locale.US, "%02d:%02d", minutos, segundos);
    }

    private int parseInt(String valor, int padrao) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return padrao;
        }
    }
}