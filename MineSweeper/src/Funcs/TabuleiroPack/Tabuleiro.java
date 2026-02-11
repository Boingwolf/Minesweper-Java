package Funcs.TabuleiroPack;

import Funcs.CelulaPack.Celula;
import Funcs.OutrasFuncoes.LimparTela;
import java.util.Random;

public class Tabuleiro {
    private Integer linhas;
    private Integer colunas;
    private Integer minas;
    private Celula[][] tabuleiro;

    public Tabuleiro() {
        this.linhas = 9;
        this.colunas = 9;
        this.minas = 10;
    }

    public Tabuleiro(Integer linhas, Integer colunas, Integer minas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;
    }

    public void iniciarTabuleiro() {
        this.tabuleiro = new Celula[this.linhas][this.colunas];
        for (int i = 0; i < this.linhas; i++) {
            for (int j = 0; j < this.colunas; j++) {
                this.tabuleiro[i][j] = new Celula();
            }
        }

    }

    private boolean isInside(int linha, int coluna) {
        return linha >= 0 && linha < this.linhas && coluna >= 0 && coluna < this.colunas;
    }

    public void gerarMinas() {
        if (this.tabuleiro == null)
            iniciarTabuleiro();
        Random rnd = new Random();
        int placed = 0;
        while (placed < this.minas) {
            int r = rnd.nextInt(this.linhas);
            int c = rnd.nextInt(this.colunas);
            Celula m = this.tabuleiro[r][c];
            if (!m.getTemMina()) {
                // replace with a Mina that has a mine
                this.tabuleiro[r][c] = new Celula(true, false, false, m.getVizinhas());
                placed++;
            }
        }
    }

    public void calcularVizinhas() {
        if (this.tabuleiro == null)
            return;
        for (int i = 0; i < this.linhas; i++) {
            for (int j = 0; j < this.colunas; j++) {
                Celula current = this.tabuleiro[i][j];
                int count = 0;
                for (int di = -1; di <= 1; di++) {
                    for (int dj = -1; dj <= 1; dj++) {
                        if (di == 0 && dj == 0)
                            continue;
                        int ni = i + di, nj = j + dj;
                        if (isInside(ni, nj) && this.tabuleiro[ni][nj].getTemMina()) {
                            count++;
                        }
                    }
                }
                // replace with updated vizinhas
                this.tabuleiro[i][j] = new Celula(current.getTemMina(), current.getEstaRevelada(), current.getFlagged(),
                        count);
            }
        }
    }

    public void revelarCasa(Integer linha, Integer coluna) {
        if (linha == null || coluna == null)
        return;

        if (this.tabuleiro == null)
            return;

        if (!isInside(linha, coluna))
            return;

        Celula cell = tabuleiro[linha][coluna];

        if (cell.getEstaRevelada() || cell.getFlagged())
            return;

        // revela a célula
        cell.setEstaRevelada(true);

        // se for mina, para aqui (game over tratado fora)
        if (cell.getTemMina())
            return;

        // se tiver número, para aqui
        if (cell.getVizinhas() > 0)
            return;

        // se for zero, abrir vizinhos
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {

                if (dr == 0 && dc == 0)
                    continue;

                revelarCasa(linha + dr, coluna + dc);
            }
        }
    }

    public Boolean verificarVitoria() {
        if (this.tabuleiro == null)
            return false;
        for (int i = 0; i < this.linhas; i++) {
            for (int j = 0; j < this.colunas; j++) {
                Celula m = this.tabuleiro[i][j];
                if (!m.getTemMina() && !m.getEstaRevelada())
                    return false;
            }
        }
        return true;
    }

    public Boolean verificarDerrota() {
        if (this.tabuleiro == null)
            return false;
        for (int i = 0; i < this.linhas; i++) {
            for (int j = 0; j < this.colunas; j++) {
                Celula m = this.tabuleiro[i][j];
                if (m.getTemMina() && m.getEstaRevelada())
                    return true;
            }
        }
        return false;
    }

    public void imprimirTabuleiro() {
        if (this.linhas == null || this.colunas == null || this.tabuleiro == null) {
            return;
        }
        LimparTela.limparTela();
        for (int i = 0; i < this.linhas; i++) {
            for (int j = 0; j < this.colunas; j++) {
                Celula m = this.tabuleiro[i][j];
                if (!m.getEstaRevelada()) {
                    if (m.getFlagged())
                        System.out.print("F ");
                    else
                        System.out.print("# ");
                } else {
                    if (m.getTemMina())
                        System.out.print("* ");
                    else
                        System.out.print(m.getVizinhas() + " ");
                }
            }
            System.out.println();
        }
    }

    public void imprimirTabuleiroFirst() {
        if (this.linhas == null || this.colunas == null || this.tabuleiro == null) {
            return;
        }
        LimparTela.limparTela();
        for (int i = 0; i < this.linhas; i++) {
            for (int j = 0; j < this.colunas; j++) {
                Celula m = this.tabuleiro[i][j];
                System.out.print("# ");
            }
            System.out.println();
        }
    }
}
