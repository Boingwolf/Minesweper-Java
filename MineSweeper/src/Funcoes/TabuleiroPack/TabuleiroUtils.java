package Funcoes.TabuleiroPack;

import Funcoes.CelulaPack.Celula;

public final class TabuleiroUtils {

    private TabuleiroUtils() {
    }

    public static boolean estaDentroDoTabuleiro(int linha, int coluna, int linhas, int colunas) {
        return linha >= 0 && linha < linhas && coluna >= 0 && coluna < colunas;
    }

    public static int contarMinasVizinhas(Celula[][] tabuleiro, int linha, int coluna, int linhas, int colunas) {
        int count = 0;
        for (int di = -1; di <= 1; di++) {
            for (int dj = -1; dj <= 1; dj++) {
                if (di == 0 && dj == 0) {
                    continue;
                }

                int ni = linha + di;
                int nj = coluna + dj;

                if (estaDentroDoTabuleiro(ni, nj, linhas, colunas) && tabuleiro[ni][nj].getTemMina()) {
                    count++;
                }
            }
        }
        return count;
    }
}
