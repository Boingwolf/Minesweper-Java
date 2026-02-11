package Swing;

import Funcs.CelulaPack.Celula;
import Funcs.TabuleiroPack.Tabuleiro;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import javax.swing.*;



public class GUI extends JPanel {

    private JButton[][] buttons;
    private int linhas = 9;
    private int colunas = 9;
    private Tabuleiro tabuleiro;

    private ImageIcon bombaIcon;
    private ImageIcon bandeiraIcon;

    public GUI(Tabuleiro tabuleiro) {
        this.buttons = new JButton[linhas][colunas];
        this.tabuleiro = tabuleiro;

        URL bombUrl = getClass().getResource("/images/Bomba.png");
        
        if (bombUrl == null)
            bombUrl = getClass().getResource("/images/Bomba.png");

        if (bombUrl != null) 
            bombaIcon = new ImageIcon(bombUrl);
        else 
            bombaIcon = new ImageIcon();
        

        URL flagUrl = getClass().getResource("/images/Bandeira.png");
        
        if (flagUrl == null)
            flagUrl = getClass().getResource("/images/Bandeira.png");

        if (flagUrl != null) 
            bandeiraIcon = new ImageIcon(flagUrl);
        else 
            bandeiraIcon = new ImageIcon();
        

        setLayout(new GridLayout(linhas, colunas));
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                buttons[i][j] = new JButton();
                int linha = i;
                int coluna = j;
                buttons[i][j].addActionListener(e -> {
                    tabuleiro.revelarCasa(linha, coluna);
                    atualizarTela();
                });
                buttons[i][j].addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {

                    if (SwingUtilities.isRightMouseButton(e)) {

                        tabuleiro.toggleFlag(linha, coluna);

                        Celula cell = tabuleiro.getTabuleiro()[linha][coluna];

                        if (cell.getFlagged()) {
                            buttons[linha][coluna].setIcon(bandeiraIcon);
                        } else {
                            buttons[linha][coluna].setIcon(null);
                        }
                    }
                }
            });
                add(buttons[i][j]);
            }
        }
    }
    
    private void atualizarTela() {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {

                Celula cell = tabuleiro.getTabuleiro()[i][j];

                if (cell.getEstaRevelada()) {

                    if (cell.getTemMina()) {
                        buttons[i][j].setIcon(bombaIcon);;
                    } else if (cell.getVizinhas() > 0) {
                        buttons[i][j].setText(
                            String.valueOf(cell.getVizinhas())
                        );
                    } else {
                        buttons[i][j].setText("");
                    }

                    buttons[i][j].setEnabled(false);
                }
            }
        }
    }

    
    public void iniciarJanela() {
        JFrame frame = new JFrame("MineSweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
