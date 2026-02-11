import Funcoes.TabuleiroPack.Tabuleiro;
import java.util.Scanner;
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
        do { 
            System.out.println("Digite a linha e a coluna para revelar (ex: 1 2): ");
            Scanner sc = new java.util.Scanner(System.in);
            int linha = sc.nextInt();
            int coluna = sc.nextInt();
            tabuleiro.revelarCasa(linha, coluna);
            tabuleiro.imprimirTabuleiro();
            if (tabuleiro.verificarDerrota()) {
                System.out.println("Você perdeu!");
                break;
            }
            if (tabuleiro.verificarVitoria()) {
                System.out.println("Você venceu!");
                break;
            }
        } while (!tabuleiro.verificarDerrota() && !tabuleiro.verificarVitoria());
    }
}
