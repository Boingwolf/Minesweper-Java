package Funcoes.TabuleiroPack;

import Funcoes.CelulaPack.Celula;

public final class TabuleiroBandeiraAlternador {

    private TabuleiroBandeiraAlternador() {
    }

    public static void alternarBandeira(Celula[][] tabuleiro, Integer linhas, Integer colunas, int linha, int coluna) {
        if (tabuleiro == null || linhas == null || colunas == null) {
            return;
        }

        if (!TabuleiroUtils.estaDentroDoTabuleiro(linha, coluna, linhas, colunas)) {
            return;
        }

        Celula celula = tabuleiro[linha][coluna];
        if (!celula.getEstaRevelada()) {
            celula.setTemBandeira(!celula.isTemBandeira());
        }
    }
}
