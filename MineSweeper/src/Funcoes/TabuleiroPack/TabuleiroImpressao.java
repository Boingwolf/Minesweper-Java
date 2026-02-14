package Funcoes.TabuleiroPack;

import Funcoes.CelulaPack.Celula;
import Funcoes.OutrasFuncoes.LimparTela;

public final class TabuleiroImpressao {

    private TabuleiroImpressao() {
    }

    public static void imprimir(Celula[][] tabuleiro, Integer linhas, Integer colunas) {
        if (tabuleiro == null || linhas == null || colunas == null) {
            return;
        }

        LimparTela.limparTela();
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                Celula celula = tabuleiro[i][j];
                if (!celula.getEstaRevelada()) {
                    if (celula.isTemBandeira()) {
                        System.out.print("F ");
                    } else {
                        System.out.print("# ");
                    }
                } else {
                    if (celula.getTemMina()) {
                        System.out.print("* ");
                    } else {
                        System.out.print(celula.getVizinhas() + " ");
                    }
                }
            }
            System.out.println();
        }
    }
}
