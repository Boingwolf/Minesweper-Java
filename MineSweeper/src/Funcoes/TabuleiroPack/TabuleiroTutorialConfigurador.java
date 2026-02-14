package Funcoes.TabuleiroPack;

import Funcoes.CelulaPack.Celula;

public final class TabuleiroTutorialConfigurador {

    private TabuleiroTutorialConfigurador() {
    }

    public static int contarMinas(boolean[][] mapaMinas) {
        int totalMinas = 0;
        for (boolean[] linha : mapaMinas) {
            for (boolean temMina : linha) {
                if (temMina) {
                    totalMinas++;
                }
            }
        }
        return totalMinas;
    }

    public static Celula[][] configurarTabuleiroTutorial(boolean[][] mapaMinas) {
        if (mapaMinas == null || mapaMinas.length == 0 || mapaMinas[0].length == 0) {
            return null;
        }

        int linhas = mapaMinas.length;
        int colunas = mapaMinas[0].length;
        Celula[][] tabuleiro = TabuleiroGerador.iniciarTabuleiro(linhas, colunas);

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                if (mapaMinas[i][j]) {
                    tabuleiro[i][j] = new Celula(true, false, false, 0);
                }
            }
        }

        TabuleiroGerador.calcularVizinhas(tabuleiro, linhas, colunas);
        return tabuleiro;
    }
}
