package Swing.components;

import Funcoes.TabuleiroPack.Tabuleiro;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatusPanel extends JPanel {

    private final JLabel tempoLabel;
    private final JLabel minasLabel;
    private final Tabuleiro tabuleiro;

    public StatusPanel(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 8));

        tempoLabel = new JLabel("Tempo: 00:00");
        minasLabel = new JLabel("Minas: 0");

        add(tempoLabel);
        add(minasLabel);
    }

    public JLabel getTempoLabel() {
        return tempoLabel;
    }

    public JLabel getMinasLabel() {
        return minasLabel;
    }

    public void atualizarMinasRestantes(int bandeiras) {
        int minasRestantes = tabuleiro.getMinas() - bandeiras;
        if (minasRestantes < 0) {
            minasRestantes = 0;
        }
        minasLabel.setText("Minas: " + minasRestantes);
    }
}
