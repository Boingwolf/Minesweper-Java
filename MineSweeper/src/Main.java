import Funcoes.TabuleiroPack.Tabuleiro;
import Swing.GUIConfig;

/**
 * Ponto de entrada da versao Swing do Minesweeper.
 */
public class Main {
    /**
     * Inicializa o tabuleiro e exibe o menu inicial.
     *
     * @param args argumentos de linha de comando
     */
    public static void main(String[] args) {
        Tabuleiro tabuleiro = new Tabuleiro();
        GUIConfig gui = new GUIConfig(tabuleiro, 9, 9);
        gui.mostrarMenuInicial();
    }
}