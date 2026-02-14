package Funcoes.TabuleiroPack;

import Funcoes.CelulaPack.Celula;

/**
 * Controla o estado e as regras do tabuleiro do Minesweeper.
 */
public class Tabuleiro {
    private Integer linhas;
    private Integer colunas;
    private Integer minas;
    private Celula[][] tabuleiro;

    /**
     * Cria um tabuleiro padrao 9x9 com 10 minas e inicializa o jogo.
     */
    public Tabuleiro() {
        this.linhas = 9;
        this.colunas = 9;
        this.minas = 10;
        this.tabuleiro = TabuleiroGerador.iniciarTabuleiro(this.linhas, this.colunas);
        TabuleiroGerador.gerarMinasExceto(this.tabuleiro, this.linhas, this.colunas, this.minas, -1, -1);
        TabuleiroGerador.calcularVizinhas(this.tabuleiro, this.linhas, this.colunas);
    }

    /**
     * Cria um tabuleiro com dimensoes e quantidade de minas informadas.
     *
     * @param linhas  numero de linhas
     * @param colunas numero de colunas
     * @param minas   quantidade de minas
     */
    public Tabuleiro(Integer linhas, Integer colunas, Integer minas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;
    }

    /**
     * Inicializa a matriz de celulas com estado padrao.
     */
    public void iniciarTabuleiro() {
        this.tabuleiro = TabuleiroGerador.iniciarTabuleiro(this.linhas, this.colunas);
    }

    /**
     * Distribui minas aleatoriamente em todo o tabuleiro.
     */
    public void gerarMinas() {
        gerarMinasExceto(-1, -1);
    }

    /**
     * Distribui minas aleatoriamente, evitando uma coordenada especifica.
     *
     * @param linhaExcluida  linha que nao pode receber mina
     * @param colunaExcluida coluna que nao pode receber mina
     */
    public void gerarMinasExceto(int linhaExcluida, int colunaExcluida) {
        this.tabuleiro = TabuleiroGerador.gerarMinasExceto(this.tabuleiro, this.linhas, this.colunas, this.minas,
                linhaExcluida, colunaExcluida);
    }

    /**
     * Calcula a quantidade de minas vizinhas para cada celula.
     */
    public void calcularVizinhas() {
        TabuleiroGerador.calcularVizinhas(this.tabuleiro, this.linhas, this.colunas);
    }

    /**
     * Revela a celula e, se necessario, expande recursivamente casas vazias.
     *
     * @param linha  linha da celula
     * @param coluna coluna da celula
     */
    public void revelarCasa(Integer linha, Integer coluna) {
        TabuleiroRevelador.revelarCasa(this.tabuleiro, this.linhas, this.colunas, linha, coluna);
    }

    /**
     * Verifica se todas as celulas sem mina foram reveladas.
     *
     * @return true se o jogador venceu; caso contrario false
     */
    public Boolean verificarVitoria() {
        return TabuleiroVerificador.verificarVitoria(this.tabuleiro, this.linhas, this.colunas);
    }

    /**
     * Verifica se alguma mina foi revelada.
     *
     * @return true se houve derrota; caso contrario false
     */
    public Boolean verificarDerrota() {
        return TabuleiroVerificador.verificarDerrota(this.tabuleiro, this.linhas, this.colunas);
    }

    /**
     * Imprime o tabuleiro no console, mostrando estado atual das celulas.
     */
    public void imprimirTabuleiro() {
        TabuleiroImpressao.imprimir(this.tabuleiro, this.linhas, this.colunas);
    }

    /**
     * Alterna a bandeira em uma celula nao revelada.
     *
     * @param linha  linha da celula
     * @param coluna coluna da celula
     */
    public void alternarBandeira(int linha, int coluna) {
        TabuleiroBandeiraAlternador.alternarBandeira(this.tabuleiro, this.linhas, this.colunas, linha, coluna);
    }

    /**
     * Retorna a quantidade de minas do tabuleiro.
     *
     * @return numero de minas
     */
    public Integer getMinas() {
        return this.minas;
    }

    /**
     * Retorna a matriz de celulas do tabuleiro.
     *
     * @return matriz de celulas
     */
    public Celula[][] getTabuleiro() {
        return this.tabuleiro;
    }

    /**
     * Retorna o número de linhas do tabuleiro.
     *
     * @return quantidade de linhas
     */
    public Integer getLinhas() {
        return this.linhas;
    }

    /**
     * Retorna o número de colunas do tabuleiro.
     *
     * @return quantidade de colunas
     */
    public Integer getColunas() {
        return this.colunas;
    }

    /**
     * Restaura o estado completo do tabuleiro a partir de mapas serializados.
     *
     * @param minasMapa     mapa de minas
     * @param reveladasMapa mapa de células reveladas
     * @param bandeirasMapa mapa de bandeiras
     * @param vizinhasMapa  mapa de vizinhas
     */
    public void restaurarEstado(boolean[][] minasMapa, boolean[][] reveladasMapa,
            boolean[][] bandeirasMapa, int[][] vizinhasMapa) {
        Celula[][] tabuleiroRestaurado = TabuleiroRestaurador.restaurarEstado(
                minasMapa,
                reveladasMapa,
                bandeirasMapa,
                vizinhasMapa);

        if (tabuleiroRestaurado == null) {
            return;
        }

        this.linhas = tabuleiroRestaurado.length;
        this.colunas = tabuleiroRestaurado[0].length;
        this.tabuleiro = tabuleiroRestaurado;
    }

    /**
     * Recria o tabuleiro e redistribui minas evitando uma coordenada.
     *
     * @param linhaExcluida  linha a ser excluida da geracao de minas
     * @param colunaExcluida coluna a ser excluida da geracao de minas
     */
    public void resetarTabuleiro(int linhaExcluida, int colunaExcluida) {
        this.tabuleiro = TabuleiroResetador.resetarTabuleiro(
                this.linhas,
                this.colunas,
                this.minas,
                linhaExcluida,
                colunaExcluida);
    }

    /**
     * Configura um tabuleiro guiado com posições de mina pré-definidas.
     *
     * @param mapaMinas matriz booleana indicando onde existem minas
     */
    public void configurarTabuleiroTutorial(boolean[][] mapaMinas) {
        Celula[][] tabuleiroTutorial = TabuleiroTutorialConfigurador.configurarTabuleiroTutorial(mapaMinas);
        if (tabuleiroTutorial == null) {
            return;
        }

        this.linhas = mapaMinas.length;
        this.colunas = mapaMinas[0].length;
        this.minas = TabuleiroTutorialConfigurador.contarMinas(mapaMinas);
        this.tabuleiro = tabuleiroTutorial;
    }

}
