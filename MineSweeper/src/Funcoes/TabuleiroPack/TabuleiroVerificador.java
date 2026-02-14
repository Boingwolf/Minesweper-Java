package Funcoes.TabuleiroPack;

import Funcoes.CelulaPack.Celula;

public final class TabuleiroVerificador {

    private TabuleiroVerificador() {
    }

    public static boolean verificarVitoria(Celula[][] tabuleiro, Integer linhas, Integer colunas) {
        if (tabuleiro == null || linhas == null || colunas == null) {
            return false;
        }

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                Celula celula = tabuleiro[i][j];
                if (!celula.getTemMina() && !celula.getEstaRevelada()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean verificarDerrota(Celula[][] tabuleiro, Integer linhas, Integer colunas) {
        if (tabuleiro == null || linhas == null || colunas == null) {
            return false;
        }

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                Celula celula = tabuleiro[i][j];
                if (celula.getTemMina() && celula.getEstaRevelada()) {
                    return true;
                }
            }
        }
        return false;
    }
}
