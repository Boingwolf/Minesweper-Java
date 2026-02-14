package Swing.utils;

import java.io.Serializable;

/**
 * Estrutura serializável com o estado necessário para continuar uma partida.
 */
public class GameSaveData implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int linhas;
    private final int colunas;
    private final int minas;
    private final int segundosDecorridos;
    private final boolean primeiroCliquePendente;
    private final boolean primeiroCliqueSeguroAtivado;

    private final boolean[][] minasMapa;
    private final boolean[][] reveladasMapa;
    private final boolean[][] bandeirasMapa;
    private final int[][] vizinhasMapa;

    /**
     * Cria um snapshot completo do jogo salvo.
     */
    public GameSaveData(int linhas, int colunas, int minas, int segundosDecorridos,
            boolean primeiroCliquePendente, boolean primeiroCliqueSeguroAtivado,
            boolean[][] minasMapa, boolean[][] reveladasMapa, boolean[][] bandeirasMapa, int[][] vizinhasMapa) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;
        this.segundosDecorridos = segundosDecorridos;
        this.primeiroCliquePendente = primeiroCliquePendente;
        this.primeiroCliqueSeguroAtivado = primeiroCliqueSeguroAtivado;
        this.minasMapa = minasMapa;
        this.reveladasMapa = reveladasMapa;
        this.bandeirasMapa = bandeirasMapa;
        this.vizinhasMapa = vizinhasMapa;
    }

    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }

    public int getMinas() {
        return minas;
    }

    public int getSegundosDecorridos() {
        return segundosDecorridos;
    }

    public boolean isPrimeiroCliquePendente() {
        return primeiroCliquePendente;
    }

    public boolean isPrimeiroCliqueSeguroAtivado() {
        return primeiroCliqueSeguroAtivado;
    }

    public boolean[][] getMinasMapa() {
        return minasMapa;
    }

    public boolean[][] getReveladasMapa() {
        return reveladasMapa;
    }

    public boolean[][] getBandeirasMapa() {
        return bandeirasMapa;
    }

    public int[][] getVizinhasMapa() {
        return vizinhasMapa;
    }
}
