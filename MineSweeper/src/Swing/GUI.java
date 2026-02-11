package Swing;

import Funcs.TabuleiroPack.Tabuleiro;
import java.awt.GridLayout;
import javax.swing.*;

public class GUI extends JPanel {

    private JButton[][] buttons;
    private int linhas = 9;
    private int colunas = 9;
    private Tabuleiro tabuleiro;

    public GUI(Tabuleiro tabuleiro) {
        this.buttons = new JButton[linhas][colunas];
        this.tabuleiro = tabuleiro;

        setLayout(new GridLayout(linhas, colunas));
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                buttons[i][j] = new JButton();
                int linha = i;
                int coluna = j;
                buttons[i][j].addActionListener(e -> {
                    tabuleiro.revelarCasa(linha, coluna);
                });
                add(buttons[i][j]);
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
