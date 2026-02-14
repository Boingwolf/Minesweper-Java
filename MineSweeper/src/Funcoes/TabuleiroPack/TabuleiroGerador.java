package Funcoes.TabuleiroPack;

import Funcoes.CelulaPack.Celula;
import java.util.Random;

public final class TabuleiroGerador {

    private TabuleiroGerador() {
    }

    public static Celula[][] iniciarTabuleiro(Integer linhas, Integer colunas) {
        if (linhas == null || colunas == null || linhas <= 0 || colunas <= 0) {
            return null;
        }

        Celula[][] tabuleiro = new Celula[linhas][colunas];
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                tabuleiro[i][j] = new Celula();
            }
        }
        return tabuleiro;
    }

    public static Celula[][] gerarMinasExceto(Celula[][] tabuleiro, Integer linhas, Integer colunas, Integer minas,
            int linhaExcluida, int colunaExcluida) {
        if (linhas == null || colunas == null || minas == null || linhas <= 0 || colunas <= 0 || minas <= 0) {
            return tabuleiro;
        }

        if (tabuleiro == null) {
            tabuleiro = iniciarTabuleiro(linhas, colunas);
        }

        if (tabuleiro == null) {
            return null;
        }

        Random rnd = new Random();
        int placed = 0;

        while (placed < minas) {
            int r = rnd.nextInt(linhas);
            int c = rnd.nextInt(colunas);

            if (r == linhaExcluida && c == colunaExcluida) {
                continue;
            }

            Celula celula = tabuleiro[r][c];
            if (!celula.getTemMina()) {
                tabuleiro[r][c] = new Celula(true, false, false, celula.getVizinhas());
                placed++;
            }
        }

        return tabuleiro;
    }

    public static void calcularVizinhas(Celula[][] tabuleiro, Integer linhas, Integer colunas) {
        if (tabuleiro == null || linhas == null || colunas == null || linhas <= 0 || colunas <= 0) {
            return;
        }

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                Celula atual = tabuleiro[i][j];
                int vizinhas = TabuleiroUtils.contarMinasVizinhas(tabuleiro, i, j, linhas, colunas);

                tabuleiro[i][j] = new Celula(atual.getTemMina(), atual.getEstaRevelada(), atual.isTemBandeira(),
                        vizinhas);
            }
        }
    }
}
