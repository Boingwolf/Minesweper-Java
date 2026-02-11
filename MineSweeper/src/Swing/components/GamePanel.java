package Swing.components;

import Funcoes.CelulaPack.Celula;
import Funcoes.TabuleiroPack.Tabuleiro;
import Swing.icons.IconManager;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GamePanel extends JPanel {

    private static final int CELL_ICON_SIZE = 24;

    private final JButton[][] buttons;
    private final int linhas;
    private final int colunas;
    private final Tabuleiro tabuleiro;
    private final IconManager iconManager;
    private final StatusPanel statusPanel;
    private final GameCallback gameCallback;
    private boolean primeiroClique;
    private static boolean primeiroCliqueSeguroAtivado = true;

    public interface GameCallback {
        void onGameLost();

        void onGameWon();
    }

    public GamePanel(Tabuleiro tabuleiro, int linhas, int colunas, StatusPanel statusPanel, GameCallback gameCallback) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.tabuleiro = tabuleiro;
        this.statusPanel = statusPanel;
        this.gameCallback = gameCallback;
        this.buttons = new JButton[linhas][colunas];
        this.primeiroClique = true;
        this.iconManager = new IconManager();

        criarGrid();
    }

    private void criarGrid() {
        setLayout(new BorderLayout());

        PatternPanel gridPanel = new PatternPanel(new GridLayout(linhas, colunas));

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                buttons[i][j] = new JButton();
                int linha = i;
                int coluna = j;

                buttons[i][j].addActionListener(e -> handleCellClick(linha, coluna));
                buttons[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            handleRightClick(linha, coluna);
                        }
                    }
                });

                configurarBotao(buttons[i][j]);
                gridPanel.add(buttons[i][j]);
            }
        }

        add(gridPanel, java.awt.BorderLayout.CENTER);
        atualizarTela();
    }

    private void configurarBotao(JButton button) {
        button.setBorderPainted(true);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setPreferredSize(new Dimension(CELL_ICON_SIZE, CELL_ICON_SIZE));
        button.setIcon(iconManager.getCelulaFechadaIcon());
    }

    private void handleCellClick(int linha, int coluna) {
        if (primeiroClique && primeiroCliqueSeguroAtivado) {
            tabuleiro.revelarCasa(linha, coluna);
            if (tabuleiro.getTabuleiro()[linha][coluna].getTemMina()) {
                tabuleiro.resetarTabuleiro(linha, coluna);
                tabuleiro.getTabuleiro()[linha][coluna].setEstaRevelada(false);
            }
            primeiroClique = false;
        }

        tabuleiro.revelarCasa(linha, coluna);
        atualizarTela();

        if (tabuleiro.getTabuleiro()[linha][coluna].getTemMina()) {
            JOptionPane.showMessageDialog(this, "Você perdeu!");
            gameCallback.onGameLost();
        } else if (tabuleiro.verificarVitoria()) {
            JOptionPane.showMessageDialog(this, "Parabéns, você venceu!");
            gameCallback.onGameWon();
        }
    }

    private void handleRightClick(int linha, int coluna) {
        tabuleiro.toggleFlag(linha, coluna);
        Celula cell = tabuleiro.getTabuleiro()[linha][coluna];

        if (cell.getFlagged()) {
            buttons[linha][coluna].setIcon(iconManager.getBandeiraIcon());
        } else if (!cell.getEstaRevelada()) {
            buttons[linha][coluna].setIcon(iconManager.getCelulaFechadaIcon());
        }

        atualizarMinasRestantes();
    }

    private void atualizarTela() {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                Celula cell = tabuleiro.getTabuleiro()[i][j];

                if (cell.getEstaRevelada()) {
                    if (cell.getTemMina()) {
                        buttons[i][j].setIcon(iconManager.getExplosaoIcon() != null ? iconManager.getExplosaoIcon()
                                : iconManager.getBombaIcon());
                    } else if (cell.getVizinhas() > 0) {
                        buttons[i][j].setIcon(iconManager.getNumeroIcon(cell.getVizinhas()));
                    } else {
                        buttons[i][j].setIcon(iconManager.getCelulaAbertaIcon());
                    }

                    buttons[i][j].setDisabledIcon(buttons[i][j].getIcon());
                    buttons[i][j].setText("");
                    buttons[i][j].setEnabled(false);
                } else if (!cell.getFlagged()) {
                    buttons[i][j].setIcon(iconManager.getCelulaFechadaIcon());
                }
            }
        }
    }

    private void atualizarMinasRestantes() {
        int bandeiras = contarBandeiras();
        statusPanel.atualizarMinasRestantes(bandeiras);
    }

    private int contarBandeiras() {
        int count = 0;
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                if (tabuleiro.getTabuleiro()[i][j].getFlagged()) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setPaint(
                new GradientPaint(0, 0, new Color(245, 245, 245), 0, getHeight(), new Color(232, 232, 232)));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }

    public static boolean isPrimeiroCliqueSeguroAtivado() {
        return primeiroCliqueSeguroAtivado;
    }

    public static void setPrimeiroCliqueSeguroAtivado(boolean ativado) {
        primeiroCliqueSeguroAtivado = ativado;
    }

    private class PatternPanel extends JPanel {
        PatternPanel(LayoutManager layout) {
            super(layout);
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(235, 235, 235));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(new Color(228, 228, 228));
            int step = CELL_ICON_SIZE;
            for (int y = 0; y < getHeight(); y += step) {
                for (int x = 0; x < getWidth(); x += step) {
                    if (((x + y) / step) % 2 == 0) {
                        g2.fillRect(x, y, step, step);
                    }
                }
            }
            g2.dispose();
        }
    }
}
