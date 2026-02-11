package Swing.utils;

import javax.swing.JLabel;
import javax.swing.Timer;

public class GameTimer {

    private Timer timer;
    private int elapsedSeconds;
    private final JLabel tempoLabel;

    public GameTimer(JLabel tempoLabel) {
        this.tempoLabel = tempoLabel;
        this.elapsedSeconds = 0;
    }

    public void iniciar() {
        elapsedSeconds = 0;
        atualizarLabel();
        timer = new Timer(1000, e -> {
            elapsedSeconds++;
            atualizarLabel();
        });
        timer.start();
    }

    public void parar() {
        if (timer != null) {
            timer.stop();
        }
    }

    private void atualizarLabel() {
        int minutos = elapsedSeconds / 60;
        int segundos = elapsedSeconds % 60;
        tempoLabel.setText(String.format("Tempo: %02d:%02d", minutos, segundos));
    }

    public int getElapsedSeconds() {
        return elapsedSeconds;
    }
}
