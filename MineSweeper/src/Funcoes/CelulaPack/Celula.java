package Funcoes.CelulaPack;

/**
 * Representa uma celula do tabuleiro, contendo estado de mina, revelacao,
 * bandeira e quantidade de minas vizinhas.
 */
public class Celula {
    private Boolean temMina;
    private Boolean estaRevelada;
    private Boolean flagged;
    private Integer vizinhas;

    /**
     * Cria uma celula vazia, nao revelada, sem bandeira e com zero vizinhas.
     */
    public Celula() {
        this.temMina = false;
        this.estaRevelada = false;
        this.flagged = false;
        this.vizinhas = 0;
    }

    /**
     * Cria uma celula com estado inicial definido.
     *
     * @param temMina indica se a celula possui mina
     * @param estaRevelada indica se a celula esta revelada
     * @param flagged indica se a celula possui bandeira
     * @param vizinhas numero de minas vizinhas
     */
    public Celula(Boolean temMina, Boolean estaRevelada, Boolean flagged, Integer vizinhas) {
        this.temMina = temMina;
        this.estaRevelada = estaRevelada;
        this.flagged = flagged;
        this.vizinhas = vizinhas;
    }

    /**
     * Retorna se a celula contem uma mina.
     *
     * @return true se contem mina; caso contrario false
     */
    public Boolean getTemMina() {
        return temMina;
    }

    /**
     * Retorna se a celula esta revelada.
     *
     * @return true se revelada; caso contrario false
     */
    public Boolean getEstaRevelada() {
        return estaRevelada;
    }

    /**
     * Retorna se a celula possui bandeira.
     *
     * @return true se estiver marcada; caso contrario false
     */
    public Boolean getFlagged() {
        return flagged;
    }

    /**
     * Retorna a quantidade de minas vizinhas.
     *
     * @return numero de minas adjacentes
     */
    public Integer getVizinhas() {
        return vizinhas;
    }

    /**
     * Define se a celula esta revelada.
     *
     * @param estaRevelada novo estado de revelacao
     */
    public void setEstaRevelada(Boolean estaRevelada) {
        this.estaRevelada = estaRevelada;
    }

    /**
     * Define se a celula possui bandeira.
     *
     * @param flagged novo estado de bandeira
     */
    public void setFlagged(Boolean flagged) {
        this.flagged = flagged;
    }
}
