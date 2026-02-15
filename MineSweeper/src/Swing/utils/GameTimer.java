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
    private boolean rodando;

    /**
     * Cria o timer vinculado a um label de tempo.
     *
     * @param labelTempo label a ser atualizado
     */
    public GameTimer(JLabel labelTempo) {
        this.labelTempo = labelTempo;
        this.segundosDecorridos = 0;
        this.rodando = false;
    }

    /**
     * Inicia o timer e zera o contador.
     */
    public void iniciar() {
        iniciarComSegundos(0);
    }

    /**
     * Inicia o timer a partir de um valor pré-existente.
     *
     * @param segundosIniciais segundos já decorridos
     */
    public void iniciarComSegundos(int segundosIniciais) {
        parar();
        segundosDecorridos = Math.max(0, segundosIniciais);
        atualizarLabel();
        temporizador = new Timer(1000, e -> {
            segundosDecorridos++;
            atualizarLabel();
        });
        temporizador.start();
        rodando = true;
    }

    /**
     * Para o timer se estiver ativo.
     */
    public void parar() {
        if (temporizador != null) {
            temporizador.stop();
        }
        rodando = false;
    }

    /**
     * Pausa o timer sem perder o valor acumulado.
     */
    public void pausar() {
        if (temporizador != null && rodando) {
            temporizador.stop();
            rodando = false;
        }
    }

    /**
     * Retoma o timer a partir do valor atual.
     */
    public void retomar() {
        if (temporizador != null && !rodando) {
            temporizador.start();
            rodando = true;
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
    public int getSegundosDecorridos() {
        return segundosDecorridos;
    }

    /**
     * Adiciona penalidade em segundos ao tempo atual.
     *
     * @param segundos quantidade de segundos a adicionar
     */
    public void adicionarPenalidadeSegundos(int segundos) {
        if (segundos <= 0) {
            return;
        }

        segundosDecorridos += segundos;
        atualizarLabel();
    }

    /**
     * Retorna se o cronômetro está rodando.
     *
     * @return true quando ativo
     */
    public boolean isRodando() {
        return rodando;
    }
}
