package Swing.stats;

import java.time.LocalDateTime;

/**
 * Representa um registo individual no leaderboard.
 */
public class LeaderboardEntry {

    private final String nomeJogador;
    private final int tempoSegundos;
    private final LocalDateTime dataRegistro;

    /**
     * Cria um novo registo de leaderboard.
     *
     * @param nomeJogador   nome do jogador
     * @param tempoSegundos tempo final em segundos
     * @param dataRegistro  data/hora do registo
     */
    public LeaderboardEntry(String nomeJogador, int tempoSegundos, LocalDateTime dataRegistro) {
        this.nomeJogador = (nomeJogador == null || nomeJogador.isBlank()) ? "Jogador" : nomeJogador.trim();
        this.tempoSegundos = Math.max(0, tempoSegundos);
        this.dataRegistro = dataRegistro == null ? LocalDateTime.now() : dataRegistro;
    }

    public String getNomeJogador() {
        return nomeJogador;
    }

    public int getTempoSegundos() {
        return tempoSegundos;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }
}