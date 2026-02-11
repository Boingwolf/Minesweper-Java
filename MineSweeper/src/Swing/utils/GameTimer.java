package Swing.utils;

import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * Controla o tempo de jogo e atualiza o label exibido.
 */
public class GameTimer {

    private Timer temporizador;
    private int segundosDecorridos;
    private final JLabel labelTempo;

    /**
     * Cria o timer vinculado a um label de tempo.
     *
     * @param labelTempo label a ser atualizado
     */
    public GameTimer(JLabel labelTempo) {
        this.labelTempo = labelTempo;
        this.segundosDecorridos = 0;
    }

    /**
     * Inicia o timer e zera o contador.
     */
    public void iniciar() {
        segundosDecorridos = 0;
        atualizarLabel();
        temporizador = new Timer(1000, e -> {
            segundosDecorridos++;
            atualizarLabel();
        });
        temporizador.start();
    }

    /**
     * Para o timer se estiver ativo.
     */
    public void parar() {
        if (temporizador != null) {
            temporizador.stop();
        }
    }

    /**
     * Atualiza o label com o tempo atual formatado.
     */
    private void atualizarLabel() {
        int minutos = segundosDecorridos / 60;
        int segundos = segundosDecorridos % 60;
        labelTempo.setText(String.format("Tempo: %02d:%02d", minutos, segundos));
    }

    /**
     * Retorna o total de segundos decorridos.
     *
     * @return segundos decorridos
     */
    public int getElapsedSeconds() {
        return segundosDecorridos;
    }
}
