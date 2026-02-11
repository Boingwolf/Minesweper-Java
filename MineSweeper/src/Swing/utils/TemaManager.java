package Swing.utils;

/**
 * Mantem o tema atual da interface.
 */
public final class TemaManager {

    private static Tema temaAtual = Tema.CLARO;

    private TemaManager() {
    }

    public static Tema getTemaAtual() {
        return temaAtual;
    }

    public static void setTemaAtual(Tema tema) {
        if (tema != null) {
            temaAtual = tema;
        }
    }
}
