package Swing.stats;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Properties;

/**
 * Gerencia leitura, atualização e persistência das estatísticas do jogador.
 */
public class EstatisticasService {

    private static final String ARQUIVO_NOME = "minesweeper-estatisticas.properties";
    private static final String PASTA_DADOS = "MineSweeper";
    private static final String SUBPASTA_DADOS = "data";

    private final Path caminhoArquivo;
    private final EstatisticasJogador estatisticas;

    /**
     * Cria o serviço apontando para a pasta de dados dentro do projeto.
     */
    public EstatisticasService() {
        String diretorioExecucao = System.getProperty("user.dir");
        Path caminhoBaseProjeto = Paths.get(diretorioExecucao);

        if (Files.exists(caminhoBaseProjeto.resolve(PASTA_DADOS))) {
            caminhoBaseProjeto = caminhoBaseProjeto.resolve(PASTA_DADOS);
        }

        this.caminhoArquivo = caminhoBaseProjeto.resolve(SUBPASTA_DADOS).resolve(ARQUIVO_NOME);
        this.estatisticas = carregarDoDisco();
    }

    /**
     * Retorna as estatísticas atuais em memória.
     *
     * @return estatísticas carregadas
     */
    public EstatisticasJogador getEstatisticas() {
        return estatisticas;
    }

    /**
     * Registra um resultado e persiste em disco.
     *
     * @param dificuldade dificuldade da partida
     * @param venceu      resultado da partida
     * @param tempoSeg    duração da partida em segundos
     */
    public void registrarResultado(DificuldadeJogo dificuldade, boolean venceu, int tempoSeg) {
        estatisticas.registrarResultado(dificuldade, venceu, tempoSeg);
        salvarNoDisco();
    }

    /**
     * Gera texto resumido para exibição na interface.
     *
     * @return texto das estatísticas
     */
    public String gerarResumo() {
        StringBuilder builder = new StringBuilder();
        builder.append("Total de jogos: ").append(estatisticas.getTotalJogos()).append('\n');
        builder.append("Vitórias: ").append(estatisticas.getTotalVitorias()).append('\n');
        builder.append("Derrotas: ").append(estatisticas.getTotalDerrotas()).append('\n');
        builder.append("Taxa de vitória: ")
                .append(String.format(Locale.US, "%.2f%%", estatisticas.getTaxaVitoria()))
                .append('\n');
        builder.append("Média de tempo: ")
                .append(formatarTempo((int) Math.round(estatisticas.getMediaTempoSegundos())))
                .append('\n');
        builder.append("Sequência atual: ")
                .append(formatarSequencia())
                .append('\n');
        builder.append("\nMelhor tempo por dificuldade:\n");
        builder.append("- Fácil: ").append(formatarMelhorTempo(DificuldadeJogo.FACIL)).append('\n');
        builder.append("- Médio: ").append(formatarMelhorTempo(DificuldadeJogo.MEDIO)).append('\n');
        builder.append("- Difícil: ").append(formatarMelhorTempo(DificuldadeJogo.DIFICIL)).append('\n');
        builder.append("- Personalizado: ").append(formatarMelhorTempo(DificuldadeJogo.PERSONALIZADO));
        return builder.toString();
    }

    private String formatarSequencia() {
        if (estatisticas.getSequenciaAtual() <= 0 || "NENHUMA".equals(estatisticas.getTipoSequenciaAtual())) {
            return "Sem sequência";
        }

        String tipo = "VITORIA".equals(estatisticas.getTipoSequenciaAtual()) ? "vitórias" : "derrotas";
        return estatisticas.getSequenciaAtual() + " " + tipo;
    }

    private String formatarMelhorTempo(DificuldadeJogo dificuldade) {
        Integer valor = estatisticas.getMelhorTempo(dificuldade);
        if (valor == null) {
            return "--";
        }
        return formatarTempo(valor);
    }

    private String formatarTempo(int segundosTotais) {
        int minutos = Math.max(0, segundosTotais) / 60;
        int segundos = Math.max(0, segundosTotais) % 60;
        return String.format(Locale.US, "%02d:%02d", minutos, segundos);
    }

    private EstatisticasJogador carregarDoDisco() {
        EstatisticasJogador dados = new EstatisticasJogador();

        if (!Files.exists(caminhoArquivo)) {
            return dados;
        }

        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(caminhoArquivo)) {
            properties.load(input);
        } catch (IOException ignored) {
            return dados;
        }

        dados.setTotalJogos(parseInt(properties.getProperty("totalJogos"), 0));
        dados.setTotalVitorias(parseInt(properties.getProperty("totalVitorias"), 0));
        dados.setTotalDerrotas(parseInt(properties.getProperty("totalDerrotas"), 0));
        dados.setTempoTotalSegundos(parseLong(properties.getProperty("tempoTotalSegundos"), 0L));
        dados.setSequenciaAtual(parseInt(properties.getProperty("sequenciaAtual"), 0));
        dados.setTipoSequenciaAtual(properties.getProperty("tipoSequenciaAtual", "NENHUMA"));

        for (DificuldadeJogo dificuldade : DificuldadeJogo.values()) {
            String chave = "melhorTempo." + dificuldade.name();
            int valor = parseInt(properties.getProperty(chave), -1);
            if (valor >= 0) {
                dados.setMelhorTempo(dificuldade, valor);
            }
        }

        return dados;
    }

    private void salvarNoDisco() {
        Properties properties = new Properties();
        properties.setProperty("totalJogos", String.valueOf(estatisticas.getTotalJogos()));
        properties.setProperty("totalVitorias", String.valueOf(estatisticas.getTotalVitorias()));
        properties.setProperty("totalDerrotas", String.valueOf(estatisticas.getTotalDerrotas()));
        properties.setProperty("tempoTotalSegundos", String.valueOf(estatisticas.getTempoTotalSegundos()));
        properties.setProperty("sequenciaAtual", String.valueOf(estatisticas.getSequenciaAtual()));
        properties.setProperty("tipoSequenciaAtual", estatisticas.getTipoSequenciaAtual());

        for (DificuldadeJogo dificuldade : DificuldadeJogo.values()) {
            Integer melhorTempo = estatisticas.getMelhorTempo(dificuldade);
            if (melhorTempo != null) {
                properties.setProperty("melhorTempo." + dificuldade.name(), String.valueOf(melhorTempo));
            }
        }

        try {
            Path diretorio = caminhoArquivo.getParent();
            if (diretorio != null) {
                Files.createDirectories(diretorio);
            }

            try (OutputStream output = Files.newOutputStream(caminhoArquivo)) {
                properties.store(output, "Estatísticas do jogador - Minesweeper");
            }
        } catch (IOException ignored) {
        }
    }

    private int parseInt(String valor, int padrao) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return padrao;
        }
    }

    private long parseLong(String valor, long padrao) {
        try {
            return Long.parseLong(valor);
        } catch (NumberFormatException e) {
            return padrao;
        }
    }
}