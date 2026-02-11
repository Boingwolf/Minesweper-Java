package Swing;

import Funcoes.TabuleiroPack.Tabuleiro;

/**
 * Ponto de entrada da versao Swing do Minesweeper.
 */
public class GUIMain {
    /**
     * Inicializa o tabuleiro e exibe o menu inicial.
     *
     * @param args argumentos de linha de comando
     */
    public static void main(String[] args) {
        Tabuleiro tabuleiro = new Tabuleiro();
        GUI gui = new GUI(tabuleiro, 9, 9);
        gui.mostrarMenuInicial();
    }
}
