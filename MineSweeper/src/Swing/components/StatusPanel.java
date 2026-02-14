package Swing.components;

import Funcoes.TabuleiroPack.Tabuleiro;
import Swing.utils.Tema;
import Swing.utils.TemaManager;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Painel de status que exibe tempo e minas restantes.
 */
public final class StatusPanel extends JPanel {

    private final JLabel labelTempo;
    private final JLabel labelMinas;
    private final JLabel labelDicaTutorial;
    private final Tabuleiro tabuleiro;

    /**
     * Cria o painel de status vinculado ao tabuleiro.
     *
     * @param tabuleiro tabuleiro do jogo
     */
    public StatusPanel(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 8));

        labelTempo = new JLabel("Tempo: 00:00");
        labelMinas = new JLabel("Minas: 0");
        labelDicaTutorial = new JLabel("");

        add(labelTempo);
        add(labelMinas);
        add(labelDicaTutorial);

        aplicarTema(TemaManager.getTemaAtual());
    }

    /**
     * Retorna o label de tempo.
     *
     * @return label de tempo
     */
    public JLabel getTempoLabel() {
        return labelTempo;
    }

    /**
     * Retorna o label de minas restantes.
     *
     * @return label de minas
     */
    public JLabel getMinasLabel() {
        return labelMinas;
    }

    /**
     * Atualiza a dica exibida durante o tutorial.
     *
     * @param dica texto da dica contextual
     */
    public void atualizarDicaTutorial(String dica) {
        labelDicaTutorial.setText(dica == null ? "" : "Dica: " + dica);
    }

    /**
     * Limpa a dica de tutorial exibida no painel.
     */
    public void limparDicaTutorial() {
        labelDicaTutorial.setText("");
    }

    /**
     * Atualiza o total de minas restantes com base nas bandeiras.
     *
     * @param bandeiras quantidade de bandeiras posicionadas
     */
    public void atualizarMinasRestantes(int bandeiras) {
        int minasRestantes = tabuleiro.getMinas() - bandeiras;
        if (minasRestantes < 0) {
            minasRestantes = 0;
        }
        labelMinas.setText("Minas: " + minasRestantes);
    }

    /**
     * Aplica o tema ao painel de status.
     *
     * @param tema tema escolhido
     */
    public void aplicarTema(Tema tema) {
        setOpaque(true);
        setBackground(tema.getPainelFundo());
        labelTempo.setForeground(tema.getTextoPadrao());
        labelMinas.setForeground(tema.getTextoPadrao());
        labelDicaTutorial.setForeground(tema.getTextoPadrao());
    }
}
