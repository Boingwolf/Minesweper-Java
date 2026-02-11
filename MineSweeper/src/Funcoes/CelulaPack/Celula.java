package Funcoes.CelulaPack;

public class Celula {
    private Boolean temMina;
    private Boolean estaRevelada;
    private Boolean flagged;
    private Integer vizinhas;

    public Celula() {
        this.temMina = false;
        this.estaRevelada = false;
        this.flagged = false;
        this.vizinhas = 0;
    }

    public Celula(Boolean temMina, Boolean estaRevelada, Boolean flagged, Integer vizinhas) {
        this.temMina = temMina;
        this.estaRevelada = estaRevelada;
        this.flagged = flagged;
        this.vizinhas = vizinhas;
    }

    public Boolean getTemMina() {
        return temMina;
    }

    public Boolean getEstaRevelada() {
        return estaRevelada;
    }

    public Boolean getFlagged() {
        return flagged;
    }

    public Integer getVizinhas() {
        return vizinhas;
    }

    public void setEstaRevelada(Boolean estaRevelada) {
        this.estaRevelada = estaRevelada;
    }

    public void setFlagged(Boolean flagged) {
        this.flagged = flagged;
    }
}
