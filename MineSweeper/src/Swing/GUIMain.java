package Swing;

import Funcs.TabuleiroPack.Tabuleiro;

public class GUIMain {

    public static void main(String[] args) {
        Tabuleiro tabuleiro = new Tabuleiro();
        GUI janela = new GUI(tabuleiro);
        janela.iniciarJanela();
        
    }
}
