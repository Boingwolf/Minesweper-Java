package Swing;

import Funcoes.TabuleiroPack.Tabuleiro;

public class GUIMain {
    public static void main(String[] args) {
        Tabuleiro tabuleiro = new Tabuleiro();
        GUI janela = new GUI(tabuleiro, 9, 9);
        janela.mostrarMenuInicial();
    }
}
