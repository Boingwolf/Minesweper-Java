package Swing.components;

import Funcoes.CelulaPack.Celula;
import Funcoes.TabuleiroPack.Tabuleiro;
import Swing.icons.IconManager;
import Swing.utils.Tema;
import Swing.utils.TemaManager;
import Swing.utils.ThemedDialog;
import Swing.utils.TutorialInterativo;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Painel principal do jogo, responsavel por renderizar e interagir com o
 * tabuleiro.
 */
public final class GamePanel extends JPanel {

    private static final int TAMANHO_ICONE_CELULA = 24;

    private final JButton[][] botoes;
    private final int linhas;
    private final int colunas;
    private final Tabuleiro tabuleiro;
    private final IconManager gestorIcones;
    private final StatusPanel painelEstado;
    private final GameCallback callbackJogo;
    private final TutorialInterativo tutorialInterativo;
    private boolean primeiroClique;
    private boolean pausado;
    private static boolean primeiroCliqueSeguroAtivado = true;

    /**
     * Callback para eventos de vitoria e derrota do jogo.
     */
    public interface GameCallback {
        /**
         * Disparado quando o jogador perde.
         */
        void onGameLost();

        /**
         * Disparado quando o jogador vence.
         */
        void onGameWon();
    }

    /**
     * Cria o painel do jogo com tabuleiro e status vinculados.
     *
     * @param tabuleiro    instancia do tabuleiro
     * @param linhas       quantidade de linhas
     * @param colunas      quantidade de colunas
     * @param painelEstado painel de status
     * @param callbackJogo callbacks de vitoria/derrota
     */
    public GamePanel(Tabuleiro tabuleiro, int linhas, int colunas, StatusPanel painelEstado,
            GameCallback callbackJogo) {
        this(tabuleiro, linhas, colunas, painelEstado, callbackJogo, null, true);
    }

    /**
     * Cria o painel do jogo com suporte opcional a tutorial guiado.
     *
     * @param tabuleiro          instancia do tabuleiro
     * @param linhas             quantidade de linhas
     * @param colunas            quantidade de colunas
     * @param painelEstado       painel de status
     * @param callbackJogo       callbacks de vitoria/derrota
     * @param tutorialInterativo tutorial ativo, quando existir
     */
    public GamePanel(Tabuleiro tabuleiro, int linhas, int colunas, StatusPanel painelEstado,
            GameCallback callbackJogo, TutorialInterativo tutorialInterativo) {
        this(tabuleiro, linhas, colunas, painelEstado, callbackJogo, tutorialInterativo, true);
    }

    /**
     * Cria o painel com controle explícito do estado de primeiro clique.
     *
     * @param tabuleiro              instancia do tabuleiro
     * @param linhas                 quantidade de linhas
     * @param colunas                quantidade de colunas
     * @param painelEstado           painel de status
     * @param callbackJogo           callbacks de vitoria/derrota
     * @param tutorialInterativo     tutorial ativo, quando existir
     * @param primeiroCliquePendente true quando o primeiro clique ainda não ocorreu
     */
    public GamePanel(Tabuleiro tabuleiro, int linhas, int colunas, StatusPanel painelEstado,
            GameCallback callbackJogo, TutorialInterativo tutorialInterativo, boolean primeiroCliquePendente) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.tabuleiro = tabuleiro;
        this.painelEstado = painelEstado;
        this.callbackJogo = callbackJogo;
        this.tutorialInterativo = tutorialInterativo;
        this.botoes = new JButton[linhas][colunas];
        this.primeiroClique = tutorialInterativo == null && primeiroCliquePendente;
        this.pausado = false;
        this.gestorIcones = new IconManager();

        criarGrid();
        aplicarTema(TemaManager.getTemaAtual());
    }

    /**
     * Monta a grade de botoes e associa os listeners de clique.
     */
    private void criarGrid() {
        setLayout(new BorderLayout());

        PatternPanel painelGrelha = new PatternPanel(new GridLayout(linhas, colunas));

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                botoes[i][j] = new JButton();
                int linha = i;
                int coluna = j;

                botoes[i][j].addActionListener(e -> processarCliqueCelula(linha, coluna));
                botoes[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            processarCliqueDireito(linha, coluna);
                        }
                    }
                });

                configurarBotao(botoes[i][j]);
                painelGrelha.add(botoes[i][j]);
            }
        }

        add(painelGrelha, java.awt.BorderLayout.CENTER);
        atualizarTela();
    }

    /**
     * Configura o estilo e o icon padrao de um botao de celula.
     *
     * @param botao botao a configurar
     */
    private void configurarBotao(JButton botao) {
        Tema tema = TemaManager.getTemaAtual();
        botao.setBorderPainted(true);
        botao.setFocusPainted(false);
        botao.setContentAreaFilled(false);
        botao.setOpaque(true);
        botao.setBackground(tema.getPainelFundo());
        botao.setForeground(tema.getTextoPadrao());
        botao.setMargin(new Insets(0, 0, 0, 0));
        botao.setPreferredSize(new Dimension(TAMANHO_ICONE_CELULA, TAMANHO_ICONE_CELULA));
        botao.setIcon(gestorIcones.getCelulaFechadaIcon());
    }

    /**
     * Trata o clique esquerdo em uma celula.
     *
     * @param linha  linha clicada
     * @param coluna coluna clicada
     */
    private void processarCliqueCelula(int linha, int coluna) {
        if (pausado) {
            return;
        }

        if (tutorialInterativo != null
                && !tutorialInterativo.validarAcaoEsperada(linha, coluna, false, painelEstado)) {
            return;
        }

        if (primeiroClique && primeiroCliqueSeguroAtivado) {
            tabuleiro.revelarCasa(linha, coluna);
            if (tabuleiro.getTabuleiro()[linha][coluna].getTemMina()) {
                tabuleiro.resetarTabuleiro(linha, coluna);
                tabuleiro.getTabuleiro()[linha][coluna].setEstaRevelada(false);
            }
            primeiroClique = false;
        }

        tabuleiro.revelarCasa(linha, coluna);
        atualizarTela();

        if (tutorialInterativo != null) {
            tutorialInterativo.concluirPasso(painelEstado);
            if (!tutorialInterativo.isAtivo()) {
                ThemedDialog.mostrar(this, "Tutorial Interativo",
                        "Tutorial concluído! Agora você já pode jogar normalmente.", "Continuar", 260);
                callbackJogo.onGameWon();
            }
            return;
        }

        if (tabuleiro.getTabuleiro()[linha][coluna].getTemMina()) {
            ThemedDialog.mostrar(this, "Fim de Jogo", "Você perdeu!", "Continuar", 240);
            callbackJogo.onGameLost();
        } else if (tabuleiro.verificarVitoria()) {
            ThemedDialog.mostrar(this, "Fim de Jogo", "Parabéns, você venceu!", "Continuar", 240);
            callbackJogo.onGameWon();
        }
    }

    /**
     * Trata o clique direito para alternar bandeiras.
     *
     * @param linha  linha clicada
     * @param coluna coluna clicada
     */
    private void processarCliqueDireito(int linha, int coluna) {
        if (pausado) {
            return;
        }

        if (tutorialInterativo != null
                && !tutorialInterativo.validarAcaoEsperada(linha, coluna, true, painelEstado)) {
            return;
        }

        tabuleiro.alternarBandeira(linha, coluna);
        Celula celula = tabuleiro.getTabuleiro()[linha][coluna];

        if (celula.isTemBandeira()) {
            botoes[linha][coluna].setIcon(gestorIcones.getBandeiraIcon());
        } else if (!celula.getEstaRevelada()) {
            botoes[linha][coluna].setIcon(gestorIcones.getCelulaFechadaIcon());
        }

        atualizarMinasRestantes();

        if (tutorialInterativo != null) {
            tutorialInterativo.concluirPasso(painelEstado);
            if (!tutorialInterativo.isAtivo()) {
                ThemedDialog.mostrar(this, "Tutorial Interativo",
                        "Tutorial concluído! Agora você já pode jogar normalmente.", "Continuar", 260);
                callbackJogo.onGameWon();
            }
        }
    }

    /**
     * Atualiza os icones e estados visuais das celulas.
     */
    private void atualizarTela() {
        if (pausado) {
            for (int i = 0; i < linhas; i++) {
                for (int j = 0; j < colunas; j++) {
                    botoes[i][j].setIcon(gestorIcones.getCelulaFechadaIcon());
                    botoes[i][j].setDisabledIcon(gestorIcones.getCelulaFechadaIcon());
                    botoes[i][j].setText("");
                    botoes[i][j].setEnabled(false);
                }
            }
            return;
        }

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                Celula celula = tabuleiro.getTabuleiro()[i][j];

                if (celula.getEstaRevelada()) {
                    if (celula.getTemMina()) {
                        botoes[i][j].setIcon(gestorIcones.getExplosaoIcon() != null ? gestorIcones.getExplosaoIcon()
                                : gestorIcones.getBombaIcon());
                    } else if (celula.getVizinhas() > 0) {
                        botoes[i][j].setIcon(gestorIcones.getNumeroIcon(celula.getVizinhas()));
                    } else {
                        botoes[i][j].setIcon(gestorIcones.getCelulaAbertaIcon());
                    }

                    botoes[i][j].setDisabledIcon(botoes[i][j].getIcon());
                    botoes[i][j].setText("");
                    botoes[i][j].setEnabled(false);
                } else if (!celula.isTemBandeira()) {
                    botoes[i][j].setIcon(gestorIcones.getCelulaFechadaIcon());
                }
            }
        }
    }

    /**
     * Atualiza o indicador de minas restantes no painel de status.
     */
    private void atualizarMinasRestantes() {
        int bandeiras = contarBandeiras();
        painelEstado.atualizarMinasRestantes(bandeiras);
    }

    /**
     * Conta quantas celulas estao marcadas com bandeira.
     *
     * @return quantidade de bandeiras
     */
    private int contarBandeiras() {
        int contagem = 0;
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                if (tabuleiro.getTabuleiro()[i][j].isTemBandeira()) {
                    contagem++;
                }
            }
        }
        return contagem;
    }

    /**
     * Pinta o fundo com um gradiente suave.
     *
     * @param grafico contexto grafico
     */
    @Override
    protected void paintComponent(Graphics grafico) {
        super.paintComponent(grafico);
        Tema tema = TemaManager.getTemaAtual();
        Graphics2D grafico2d = (Graphics2D) grafico.create();
        grafico2d.setPaint(
                new GradientPaint(0, 0, tema.getFundoGradienteTopo(), 0, getHeight(),
                        tema.getFundoGradienteBase()));
        grafico2d.fillRect(0, 0, getWidth(), getHeight());
        grafico2d.dispose();
    }

    /**
     * Aplica o tema ao painel e aos botoes.
     *
     * @param tema tema escolhido
     */
    public void aplicarTema(Tema tema) {
        // Recarrega os ícones para o novo tema
        gestorIcones.recarregarParaTema(tema);

        setOpaque(true);
        setBackground(tema.getPainelFundo());
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                botoes[i][j].setBackground(tema.getPainelFundo());
                botoes[i][j].setForeground(tema.getTextoPadrao());
            }
        }

        // Atualiza a tela para refletir os novos ícones
        atualizarTela();
        repaint();
    }

    /**
     * Pausa ou retoma a interação e renderização do tabuleiro.
     *
     * @param pausado true para pausar; false para retomar
     */
    public void setPausado(boolean pausado) {
        this.pausado = pausado;
        atualizarTela();
        repaint();
    }

    /**
     * Retorna se o painel está pausado.
     *
     * @return true quando pausado
     */
    public boolean isPausado() {
        return pausado;
    }

    /**
     * Retorna se o primeiro clique ainda está pendente.
     *
     * @return true quando ainda não houve clique inicial
     */
    public boolean isPrimeiroCliquePendente() {
        return primeiroClique;
    }

    /**
     * Retorna a quantidade atual de bandeiras marcadas no tabuleiro.
     *
     * @return total de bandeiras
     */
    public int getQuantidadeBandeiras() {
        return contarBandeiras();
    }

    /**
     * Retorna se o modo de primeiro clique seguro esta ativado.
     *
     * @return true se ativado; caso contrario false
     */
    public static boolean isPrimeiroCliqueSeguroAtivado() {
        return primeiroCliqueSeguroAtivado;
    }

    /**
     * Define o modo de primeiro clique seguro.
     *
     * @param ativado novo estado do modo seguro
     */
    public static void setPrimeiroCliqueSeguroAtivado(boolean ativado) {
        primeiroCliqueSeguroAtivado = ativado;
    }

    /**
     * Painel interno que desenha o padrao de fundo do grid.
     */
    private class PatternPanel extends JPanel {
        /**
         * Cria o painel com o layout fornecido.
         *
         * @param layout layout do painel
         */
        PatternPanel(LayoutManager layout) {
            super(layout);
            setOpaque(true);
        }

        /**
         * Desenha o padrao quadriculado no fundo.
         *
         * @param grafico contexto grafico
         */
        @Override
        protected void paintComponent(Graphics grafico) {
            super.paintComponent(grafico);
            Graphics2D grafico2d = (Graphics2D) grafico.create();
            Tema tema = TemaManager.getTemaAtual();
            grafico2d.setColor(tema.getGridBase());
            grafico2d.fillRect(0, 0, getWidth(), getHeight());
            grafico2d.setColor(tema.getGridAlternado());
            int passo = TAMANHO_ICONE_CELULA;
            for (int y = 0; y < getHeight(); y += passo) {
                for (int x = 0; x < getWidth(); x += passo) {
                    if (((x + y) / passo) % 2 == 0) {
                        grafico2d.fillRect(x, y, passo, passo);
                    }
                }
            }
            grafico2d.dispose();
        }
    }
}
