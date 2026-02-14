package Funcoes.TabuleiroPack;

import Funcoes.CelulaPack.Celula;

public final class TabuleiroResetador {

    private TabuleiroResetador() {
    }

    public static Celula[][] resetarTabuleiro(Integer linhas, Integer colunas, Integer minas, int linhaExcluida,
            int colunaExcluida) {
        if (linhas == null || colunas == null || minas == null) {
            return null;
        }

        Celula[][] tabuleiro = TabuleiroGerador.iniciarTabuleiro(linhas, colunas);
        tabuleiro = TabuleiroGerador.gerarMinasExceto(tabuleiro, linhas, colunas, minas, linhaExcluida, colunaExcluida);
        TabuleiroGerador.calcularVizinhas(tabuleiro, linhas, colunas);
        return tabuleiro;
    }
}
