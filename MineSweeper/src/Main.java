import Funcs.TabuleiroPack.Tabuleiro;

public class Main {
    public static void main(String[] args) throws Exception {
        Tabuleiro tabuleiro = new Tabuleiro();
        tabuleiro.iniciarTabuleiro();
        tabuleiro.gerarMinas();
        tabuleiro.calcularVizinhas();
        /*do { 
            tabuleiro.imprimirTabuleiro();
            
            tabuleiro.imprimirTabuleiro();
            tabuleiro.verificarDerrota();
            tabuleiro.verificarVitoria();
            } while (true);
            */
            tabuleiro.imprimirTabuleiroFirst();
            tabuleiro.revelarCasa(0, 0);
            //tabuleiro.revelarCasa(3, 3);
            tabuleiro.imprimirTabuleiro();

    }
}
