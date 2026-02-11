package Swing.components;

import Funcoes.CelulaPack.Celula;
import Funcoes.TabuleiroPack.Tabuleiro;
import Swing.icons.IconManager;
import Swing.utils.Tema;
import Swing.utils.TemaManager;
import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Painel principal do jogo, responsavel por renderizar e interagir com o
 * tabuleiro.
 */
public class GamePanel extends JPanel {

    private static final int TAMANHO_ICONE_CELULA = 24;

    private final JButton[][] botoes;
    private final int linhas;
    private final int colunas;
    private final Tabuleiro tabuleiro;
    private final IconManager gestorIcones;
    private final StatusPanel painelEstado;
    private final GameCallback callbackJogo;
    private boolean primeiroClique;
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
        this.linhas = linhas;
        this.colunas = colunas;
        this.tabuleiro = tabuleiro;
        this.painelEstado = painelEstado;
        this.callbackJogo = callbackJogo;
        this.botoes = new JButton[linhas][colunas];
        this.primeiroClique = true;
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

                botoes[i][j].addActionListener(e -> handleCellClick(linha, coluna));
                botoes[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            handleRightClick(linha, coluna);
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
    private void handleCellClick(int linha, int coluna) {
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

        if (tabuleiro.getTabuleiro()[linha][coluna].getTemMina()) {
            JOptionPane.showMessageDialog(this, "Você perdeu!");
            callbackJogo.onGameLost();
        } else if (tabuleiro.verificarVitoria()) {
            JOptionPane.showMessageDialog(this, "Parabéns, você venceu!");
            callbackJogo.onGameWon();
        }
    }

    /**
     * Trata o clique direito para alternar bandeiras.
     *
     * @param linha  linha clicada
     * @param coluna coluna clicada
     */
    private void handleRightClick(int linha, int coluna) {
        tabuleiro.toggleFlag(linha, coluna);
        Celula celula = tabuleiro.getTabuleiro()[linha][coluna];

        if (celula.getFlagged()) {
            botoes[linha][coluna].setIcon(gestorIcones.getBandeiraIcon());
        } else if (!celula.getEstaRevelada()) {
            botoes[linha][coluna].setIcon(gestorIcones.getCelulaFechadaIcon());
        }

        atualizarMinasRestantes();
    }

    /**
     * Atualiza os icones e estados visuais das celulas.
     */
    private void atualizarTela() {
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
                } else if (!celula.getFlagged()) {
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
                if (tabuleiro.getTabuleiro()[i][j].getFlagged()) {
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
        setOpaque(true);
        setBackground(tema.getPainelFundo());
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                botoes[i][j].setBackground(tema.getPainelFundo());
                botoes[i][j].setForeground(tema.getTextoPadrao());
            }
        }
        repaint();
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
