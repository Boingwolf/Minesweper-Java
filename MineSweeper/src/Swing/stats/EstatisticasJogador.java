package Swing.stats;

import java.util.EnumMap;
import java.util.Map;

/**
 * Armazena os dados de estatísticas acumuladas do jogador.
 */
public class EstatisticasJogador {

    private int totalJogos;
    private int totalVitorias;
    private int totalDerrotas;
    private long tempoTotalSegundos;
    private int sequenciaAtual;
    private String tipoSequenciaAtual;
    private final EnumMap<DificuldadeJogo, Integer> melhorTempoPorDificuldade;

    /**
     * Cria estatísticas com estado inicial.
     */
    public EstatisticasJogador() {
        this.totalJogos = 0;
        this.totalVitorias = 0;
        this.totalDerrotas = 0;
        this.tempoTotalSegundos = 0;
        this.sequenciaAtual = 0;
        this.tipoSequenciaAtual = "NENHUMA";
        this.melhorTempoPorDificuldade = new EnumMap<>(DificuldadeJogo.class);
    }

    /**
     * Registra o resultado de uma partida concluída.
     *
     * @param dificuldade     dificuldade da partida
     * @param venceu          true quando vitória
     * @param tempoEmSegundos duração da partida em segundos
     */
    public void registrarResultado(DificuldadeJogo dificuldade, boolean venceu, int tempoEmSegundos) {
        totalJogos++;
        tempoTotalSegundos += Math.max(0, tempoEmSegundos);

        if (venceu) {
            totalVitorias++;
            atualizarSequencia("VITORIA");
            atualizarMelhorTempo(dificuldade, tempoEmSegundos);
        } else {
            totalDerrotas++;
            atualizarSequencia("DERROTA");
        }
    }

    private void atualizarMelhorTempo(DificuldadeJogo dificuldade, int tempoEmSegundos) {
        if (dificuldade == null) {
            return;
        }

        Integer melhorAtual = melhorTempoPorDificuldade.get(dificuldade);
        if (melhorAtual == null || tempoEmSegundos < melhorAtual) {
            melhorTempoPorDificuldade.put(dificuldade, tempoEmSegundos);
        }
    }

    private void atualizarSequencia(String novoTipo) {
        if (novoTipo.equals(tipoSequenciaAtual)) {
            sequenciaAtual++;
        } else {
            tipoSequenciaAtual = novoTipo;
            sequenciaAtual = 1;
        }
    }

    /**
     * Taxa de vitória em percentual.
     *
     * @return taxa em porcentagem
     */
    public double getTaxaVitoria() {
        if (totalJogos == 0) {
            return 0.0;
        }
        return (totalVitorias * 100.0) / totalJogos;
    }

    /**
     * Tempo médio de jogo em segundos.
     *
     * @return média de tempo por partida
     */
    public double getMediaTempoSegundos() {
        if (totalJogos == 0) {
            return 0.0;
        }
        return tempoTotalSegundos / (double) totalJogos;
    }

    /**
     * Melhor tempo salvo para uma dificuldade.
     *
     * @param dificuldade dificuldade consultada
     * @return melhor tempo em segundos, ou null se não houver
     */
    public Integer getMelhorTempo(DificuldadeJogo dificuldade) {
        return melhorTempoPorDificuldade.get(dificuldade);
    }

    /**
     * Mapa de melhores tempos por dificuldade.
     *
     * @return mapa imutável via referência de leitura
     */
    public Map<DificuldadeJogo, Integer> getMelhoresTempos() {
        return melhorTempoPorDificuldade;
    }

    public int getTotalJogos() {
        return totalJogos;
    }

    public void setTotalJogos(int totalJogos) {
        this.totalJogos = Math.max(0, totalJogos);
    }

    public int getTotalVitorias() {
        return totalVitorias;
    }

    public void setTotalVitorias(int totalVitorias) {
        this.totalVitorias = Math.max(0, totalVitorias);
    }

    public int getTotalDerrotas() {
        return totalDerrotas;
    }

    public void setTotalDerrotas(int totalDerrotas) {
        this.totalDerrotas = Math.max(0, totalDerrotas);
    }

    public long getTempoTotalSegundos() {
        return tempoTotalSegundos;
    }

    public void setTempoTotalSegundos(long tempoTotalSegundos) {
        this.tempoTotalSegundos = Math.max(0, tempoTotalSegundos);
    }

    public int getSequenciaAtual() {
        return sequenciaAtual;
    }

    public void setSequenciaAtual(int sequenciaAtual) {
        this.sequenciaAtual = Math.max(0, sequenciaAtual);
    }

    public String getTipoSequenciaAtual() {
        return tipoSequenciaAtual;
    }

    public void setTipoSequenciaAtual(String tipoSequenciaAtual) {
        if (tipoSequenciaAtual == null || tipoSequenciaAtual.isBlank()) {
            this.tipoSequenciaAtual = "NENHUMA";
            return;
        }
        this.tipoSequenciaAtual = tipoSequenciaAtual;
    }

    public void setMelhorTempo(DificuldadeJogo dificuldade, Integer valor) {
        if (dificuldade == null || valor == null || valor < 0) {
            return;
        }
        melhorTempoPorDificuldade.put(dificuldade, valor);
    }
}