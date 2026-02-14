package Funcoes.CelulaPack;

public final class CelulaEstado {

    private final Boolean temMina;
    private final Boolean estaRevelada;
    private final Boolean temBandeira;
    private final Integer vizinhas;

    public CelulaEstado(Boolean temMina, Boolean estaRevelada, Boolean temBandeira, Integer vizinhas) {
        this.temMina = temMina;
        this.estaRevelada = estaRevelada;
        this.temBandeira = temBandeira;
        this.vizinhas = vizinhas;
    }

    public Boolean getTemMina() {
        return temMina;
    }

    public Boolean getEstaRevelada() {
        return estaRevelada;
    }

    public Boolean getTemBandeira() {
        return temBandeira;
    }

    public Integer getVizinhas() {
        return vizinhas;
    }
}
