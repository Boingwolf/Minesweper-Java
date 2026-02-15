package Funcoes.TabuleiroPack;

import Funcoes.CelulaPack.Celula;

public final class TabuleiroChordador {

    private TabuleiroChordador() {
    }

    public static boolean chordarCasa(Celula[][] tabuleiro, Integer linhas, Integer colunas, Integer linha,
            Integer coluna) {
        if (tabuleiro == null || linhas == null || colunas == null || linha == null || coluna == null) {
            return false;
        }

        if (!TabuleiroUtils.estaDentroDoTabuleiro(linha, coluna, linhas, colunas)) {
            return false;
        }

        Celula celula = tabuleiro[linha][coluna];
        if (!celula.getEstaRevelada() || celula.getTemMina() || celula.getVizinhas() <= 0) {
            return false;
        }

        int bandeirasVizinhas = contarBandeirasVizinhas(tabuleiro, linhas, colunas, linha, coluna);
        if (bandeirasVizinhas != celula.getVizinhas()) {
            return false;
        }

        boolean revelouAlguma = false;

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) {
                    continue;
                }

                int linhaVizinha = linha + dr;
                int colunaVizinha = coluna + dc;

                if (!TabuleiroUtils.estaDentroDoTabuleiro(linhaVizinha, colunaVizinha, linhas, colunas)) {
                    continue;
                }

                Celula vizinha = tabuleiro[linhaVizinha][colunaVizinha];
                if (vizinha.getEstaRevelada() || vizinha.isTemBandeira()) {
                    continue;
                }

                TabuleiroRevelador.revelarCasa(tabuleiro, linhas, colunas, linhaVizinha, colunaVizinha);
                revelouAlguma = true;
            }
        }

        return revelouAlguma;
    }

    private static int contarBandeirasVizinhas(Celula[][] tabuleiro, int linhas, int colunas, int linha,
            int coluna) {
        int contagem = 0;

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) {
                    continue;
                }

                int linhaVizinha = linha + dr;
                int colunaVizinha = coluna + dc;

                if (!TabuleiroUtils.estaDentroDoTabuleiro(linhaVizinha, colunaVizinha, linhas, colunas)) {
                    continue;
                }

                if (tabuleiro[linhaVizinha][colunaVizinha].isTemBandeira()) {
                    contagem++;
                }
            }
        }

        return contagem;
    }
}
