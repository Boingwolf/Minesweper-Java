package Funcoes.TabuleiroPack;

import Funcoes.CelulaPack.Celula;

public final class TabuleiroRestaurador {

    private TabuleiroRestaurador() {
    }

    public static Celula[][] restaurarEstado(boolean[][] minasMapa, boolean[][] reveladasMapa,
            boolean[][] bandeirasMapa, int[][] vizinhasMapa) {
        if (!mapasValidos(minasMapa, reveladasMapa, bandeirasMapa, vizinhasMapa)) {
            return null;
        }

        int linhas = minasMapa.length;
        int colunas = minasMapa[0].length;

        Celula[][] tabuleiro = new Celula[linhas][colunas];
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                tabuleiro[i][j] = new Celula(
                        minasMapa[i][j],
                        reveladasMapa[i][j],
                        bandeirasMapa[i][j],
                        vizinhasMapa[i][j]);
            }
        }

        return tabuleiro;
    }

    private static boolean mapasValidos(boolean[][] minasMapa, boolean[][] reveladasMapa,
            boolean[][] bandeirasMapa, int[][] vizinhasMapa) {
        if (minasMapa == null || reveladasMapa == null || bandeirasMapa == null || vizinhasMapa == null) {
            return false;
        }

        if (minasMapa.length == 0 || minasMapa[0].length == 0) {
            return false;
        }

        int linhas = minasMapa.length;
        int colunas = minasMapa[0].length;

        if (reveladasMapa.length != linhas || bandeirasMapa.length != linhas || vizinhasMapa.length != linhas) {
            return false;
        }

        for (int i = 0; i < linhas; i++) {
            if (minasMapa[i].length != colunas
                    || reveladasMapa[i].length != colunas
                    || bandeirasMapa[i].length != colunas
                    || vizinhasMapa[i].length != colunas) {
                return false;
            }
        }

        return true;
    }
}
