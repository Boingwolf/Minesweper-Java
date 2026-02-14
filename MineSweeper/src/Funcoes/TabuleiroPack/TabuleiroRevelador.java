package Funcoes.TabuleiroPack;

import Funcoes.CelulaPack.Celula;

public final class TabuleiroRevelador {

    private TabuleiroRevelador() {
    }

    public static void revelarCasa(Celula[][] tabuleiro, Integer linhas, Integer colunas, Integer linha,
            Integer coluna) {
        if (tabuleiro == null || linhas == null || colunas == null || linha == null || coluna == null) {
            return;
        }

        if (!TabuleiroUtils.estaDentroDoTabuleiro(linha, coluna, linhas, colunas)) {
            return;
        }

        Celula celula = tabuleiro[linha][coluna];
        if (celula.getEstaRevelada() || celula.isTemBandeira()) {
            return;
        }

        celula.setEstaRevelada(true);

        if (celula.getTemMina() || celula.getVizinhas() > 0) {
            return;
        }

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) {
                    continue;
                }
                revelarCasa(tabuleiro, linhas, colunas, linha + dr, coluna + dc);
            }
        }
    }
}
