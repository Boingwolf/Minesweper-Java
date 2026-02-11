package Funcoes.TabuleiroPack;

import Funcoes.CelulaPack.Celula;
import Funcoes.OutrasFuncoes.LimparTela;
import java.util.Random;

/**
 * Controla o estado e as regras do tabuleiro do Minesweeper.
 */
public class Tabuleiro {
    private Integer linhas;
    private Integer colunas;
    private Integer minas;
    private Celula[][] tabuleiro;

    /**
     * Cria um tabuleiro padrao 9x9 com 10 minas e inicializa o jogo.
     */
    public Tabuleiro() {
        this.linhas = 9;
        this.colunas = 9;
        this.minas = 10;
        iniciarTabuleiro();
        gerarMinas();
        calcularVizinhas();
    }

    /**
     * Cria um tabuleiro com dimensoes e quantidade de minas informadas.
     *
     * @param linhas numero de linhas
     * @param colunas numero de colunas
     * @param minas quantidade de minas
     */
    public Tabuleiro(Integer linhas, Integer colunas, Integer minas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;
    }

    /**
     * Inicializa a matriz de celulas com estado padrao.
     */
    public void iniciarTabuleiro() {
        this.tabuleiro = new Celula[this.linhas][this.colunas];
        for (int i = 0; i < this.linhas; i++) {
            for (int j = 0; j < this.colunas; j++) {
                this.tabuleiro[i][j] = new Celula();
            }
        }
    }

    /**
     * Verifica se as coordenadas estao dentro dos limites do tabuleiro.
     *
     * @param linha indice da linha
     * @param coluna indice da coluna
     * @return true se estiver dentro; caso contrario false
     */
    private boolean isInside(int linha, int coluna) {
        return linha >= 0 && linha < this.linhas && coluna >= 0 && coluna < this.colunas;
    }

    /**
     * Distribui minas aleatoriamente em todo o tabuleiro.
     */
    public void gerarMinas() {
        gerarMinasExceto(-1, -1);
    }

    /**
     * Distribui minas aleatoriamente, evitando uma coordenada especifica.
     *
     * @param linhaExcluida linha que nao pode receber mina
     * @param colunaExcluida coluna que nao pode receber mina
     */
    public void gerarMinasExceto(int linhaExcluida, int colunaExcluida) {
        if (this.tabuleiro == null)
            iniciarTabuleiro();
        Random rnd = new Random();
        int placed = 0;
        while (placed < this.minas) {
            int r = rnd.nextInt(this.linhas);
            int c = rnd.nextInt(this.colunas);

            // Não coloca mina na posição excluída
            if (r == linhaExcluida && c == colunaExcluida) {
                continue;
            }

            Celula m = this.tabuleiro[r][c];
            if (!m.getTemMina()) {
                // replace with a Mina that has a mine
                this.tabuleiro[r][c] = new Celula(true, false, false, m.getVizinhas());
                placed++;
            }
        }
    }

    /**
     * Calcula a quantidade de minas vizinhas para cada celula.
     */
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

    /**
     * Revela a celula e, se necessario, expande recursivamente casas vazias.
     *
     * @param linha linha da celula
     * @param coluna coluna da celula
     */
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

    /**
     * Verifica se todas as celulas sem mina foram reveladas.
     *
     * @return true se o jogador venceu; caso contrario false
     */
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

    /**
     * Verifica se alguma mina foi revelada.
     *
     * @return true se houve derrota; caso contrario false
     */
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

    /**
     * Imprime o tabuleiro no console, mostrando estado atual das celulas.
     */
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

    /**
     * Alterna a bandeira em uma celula nao revelada.
     *
     * @param linha linha da celula
     * @param coluna coluna da celula
     */
    public void toggleFlag(int linha, int coluna) {
        Celula cell = tabuleiro[linha][coluna];

        if (!cell.getEstaRevelada()) {
            cell.setFlagged(!cell.getFlagged());
        }
    }

    /**
     * Retorna a quantidade de minas do tabuleiro.
     *
     * @return numero de minas
     */
    public Integer getMinas() {
        return this.minas;
    }

    /**
     * Retorna a matriz de celulas do tabuleiro.
     *
     * @return matriz de celulas
     */
    public Celula[][] getTabuleiro() {
        return this.tabuleiro;
    }

    /**
     * Recria o tabuleiro e redistribui minas evitando uma coordenada.
     *
     * @param linhaExcluida linha a ser excluida da geracao de minas
     * @param colunaExcluida coluna a ser excluida da geracao de minas
     */
    public void resetarTabuleiro(int linhaExcluida, int colunaExcluida) {
        iniciarTabuleiro();
        gerarMinasExceto(linhaExcluida, colunaExcluida);
        calcularVizinhas();
    }

}
