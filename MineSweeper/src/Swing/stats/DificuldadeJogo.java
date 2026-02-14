package Swing.stats;

/**
 * Representa as dificuldades reconhecidas para estatísticas.
 */
public enum DificuldadeJogo {
    FACIL("Fácil"),
    MEDIO("Médio"),
    DIFICIL("Difícil"),
    PERSONALIZADO("Personalizado");

    private final String nomeExibicao;

    DificuldadeJogo(String nomeExibicao) {
        this.nomeExibicao = nomeExibicao;
    }

    /**
     * Retorna o nome de exibição da dificuldade.
     *
     * @return nome legível para UI
     */
    public String getNomeExibicao() {
        return nomeExibicao;
    }

    /**
     * Identifica a dificuldade por configuração de tabuleiro.
     *
     * @param linhas  linhas do tabuleiro
     * @param colunas colunas do tabuleiro
     * @param minas   minas do tabuleiro
     * @return dificuldade correspondente
     */
    public static DificuldadeJogo deConfiguracao(int linhas, int colunas, int minas) {
        if (linhas == 9 && colunas == 9 && minas == 10) {
            return FACIL;
        }
        if (linhas == 12 && colunas == 12 && minas == 20) {
            return MEDIO;
        }
        if (linhas == 16 && colunas == 16 && minas == 40) {
            return DIFICIL;
        }
        return PERSONALIZADO;
    }
}