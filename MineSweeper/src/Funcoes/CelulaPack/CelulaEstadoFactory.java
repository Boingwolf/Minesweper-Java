package Funcoes.CelulaPack;

public final class CelulaEstadoFactory {

    private CelulaEstadoFactory() {
    }

    public static CelulaEstado criarPadrao() {
        return new CelulaEstado(false, false, false, 0);
    }

    public static CelulaEstado criarNormalizado(Boolean temMina, Boolean estaRevelada,
            Boolean temBandeira, Integer vizinhas) {
        return new CelulaEstado(
                normalizarBoolean(temMina),
                normalizarBoolean(estaRevelada),
                normalizarBoolean(temBandeira),
                normalizarVizinhas(vizinhas));
    }

    public static Boolean normalizarBoolean(Boolean valor) {
        return valor != null && valor;
    }

    public static Integer normalizarVizinhas(Integer vizinhas) {
        if (vizinhas == null || vizinhas < 0) {
            return 0;
        }
        return vizinhas;
    }
}
