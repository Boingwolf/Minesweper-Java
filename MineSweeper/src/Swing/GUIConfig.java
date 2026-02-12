package Swing;

import Funcoes.TabuleiroPack.Tabuleiro;
import Swing.components.GamePanel;
import Swing.components.StatusPanel;
import Swing.icons.IconManager;
import Swing.menu.MenuInicial;
import Swing.utils.GameTimer;
import Swing.utils.Tema;
import Swing.utils.TemaManager;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Controla a janela principal e o fluxo de telas do jogo.
 */
public class GUIConfig {

    private JFrame janela;
    private GamePanel painelJogo;
    private StatusPanel painelEstado;
    private GameTimer cronometroJogo;
    private final Tabuleiro tabuleiro;
    private int linhasGame;
    private int colunasGame;

    /**
     * Cria a GUI vinculada ao tabuleiro.
     *
     * @param tabuleiro tabuleiro do jogo
     * @param linhas    numero de linhas (usado para configuracao inicial)
     * @param colunas   numero de colunas (usado para configuracao inicial)
     */
    public GUIConfig(Tabuleiro tabuleiro, int linhas, int colunas) {
        this.tabuleiro = tabuleiro;
        this.linhasGame = linhas;
        this.colunasGame = colunas;
    }

    /**
     * Exibe a tela de menu inicial.
     */
    public void mostrarMenuInicial() {
        MenuInicial menu = new MenuInicial(this::iniciarJogoComDificuldade);
        menu.mostrar();
    }

    /**
     * Inicia um novo jogo com a dificuldade escolhida.
     *
     * @param linhas  numero de linhas
     * @param colunas numero de colunas
     * @param minas   quantidade de minas
     */
    private void iniciarJogoComDificuldade(int linhas, int colunas, int minas) {
        Tabuleiro novoTabuleiro = new Tabuleiro(linhas, colunas, minas);
        novoTabuleiro.iniciarTabuleiro();
        novoTabuleiro.gerarMinas();
        novoTabuleiro.calcularVizinhas();

        GUIConfig novaGui = new GUIConfig(novoTabuleiro, linhas, colunas);
        novaGui.iniciarJanela();
    }

    /**
     * Monta e exibe a janela principal do jogo.
     */
    public void iniciarJanela() {
        janela = new JFrame("MineSweeper");
        IconManager gestorIcones = new IconManager();
        janela.setIconImage(gestorIcones.carregarIconeJanela("/images/favicon.png"));
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setSize(700, 800);
        janela.setLocationRelativeTo(null);

        painelEstado = new StatusPanel(tabuleiro);

        GamePanel.GameCallback callbackJogo = new GamePanel.GameCallback() {
            @Override
            public void onGameLost() {
                cronometroJogo.parar();
                janela.dispose();
                mostrarMenuInicial();
            }

            @Override
            public void onGameWon() {
                cronometroJogo.parar();
                janela.dispose();
                mostrarMenuInicial();
            }
        };

        painelJogo = new GamePanel(tabuleiro, linhasGame, colunasGame, painelEstado, callbackJogo);

        janela.setLayout(new BorderLayout());
        janela.add(painelEstado, BorderLayout.NORTH);
        janela.add(painelJogo, BorderLayout.CENTER);

        cronometroJogo = new GameTimer(painelEstado.getTempoLabel());
        cronometroJogo.iniciar();

        painelEstado.atualizarMinasRestantes(0);

        final JMenuBar barraMenu = new JMenuBar();
        JMenu menu = new JMenu("Opções");
        JMenuItem itemReiniciar = new JMenuItem("Reiniciar");
        itemReiniciar.addActionListener(e -> reiniciarJogo());
        JMenuItem itemMenuInicial = new JMenuItem("Menu Inicial");
        itemMenuInicial.addActionListener(e -> {
            cronometroJogo.parar();
            janela.dispose();
            mostrarMenuInicial();
        });

        JMenu menuTema = new JMenu("Tema");
        JMenuItem itemTemaClaro = new JMenuItem("Claro");
        itemTemaClaro.addActionListener(e -> {
            TemaManager.setTemaAtual(Tema.CLARO);
            aplicarTema(barraMenu, menu, itemReiniciar, itemMenuInicial, menuTema);
        });
        JMenuItem itemTemaEscuro = new JMenuItem("Escuro");
        itemTemaEscuro.addActionListener(e -> {
            TemaManager.setTemaAtual(Tema.ESCURO);
            aplicarTema(barraMenu, menu, itemReiniciar, itemMenuInicial, menuTema);
        });
        menuTema.add(itemTemaClaro);
        menuTema.add(itemTemaEscuro);

        menu.add(itemReiniciar);
        menu.add(itemMenuInicial);
        menu.addSeparator();
        menu.add(menuTema);
        barraMenu.add(menu);
        janela.setJMenuBar(barraMenu);

        aplicarTema(barraMenu, menu, itemReiniciar, itemMenuInicial, menuTema);

        janela.setVisible(true);
    }

    /**
     * Aplica o tema atual aos componentes da janela.
     *
     * @param barraMenu       barra de menu
     * @param menu            menu principal
     * @param itemReiniciar   item de reinicio
     * @param itemMenuInicial item para voltar ao menu inicial
     * @param menuTema        menu de seleção de tema
     */
    private void aplicarTema(JMenuBar barraMenu, JMenu menu, JMenuItem itemReiniciar, JMenuItem itemMenuInicial,
            JMenu menuTema) {
        Tema tema = TemaManager.getTemaAtual();
        janela.getContentPane().setBackground(tema.getPainelFundo());

        painelEstado.aplicarTema(tema);
        painelJogo.aplicarTema(tema);

        barraMenu.setOpaque(true);
        barraMenu.setBackground(tema.getMenuFundo());

        menu.setOpaque(true);
        menu.setBackground(tema.getMenuFundo());
        menu.setForeground(tema.getMenuTexto());

        menuTema.setOpaque(true);
        menuTema.setBackground(tema.getMenuFundo());
        menuTema.setForeground(tema.getMenuTexto());

        itemReiniciar.setOpaque(true);
        itemReiniciar.setBackground(tema.getMenuFundo());
        itemReiniciar.setForeground(tema.getMenuTexto());

        itemMenuInicial.setOpaque(true);
        itemMenuInicial.setBackground(tema.getMenuFundo());
        itemMenuInicial.setForeground(tema.getMenuTexto());

        for (int i = 0; i < menuTema.getItemCount(); i++) {
            JMenuItem item = menuTema.getItem(i);
            if (item != null) {
                item.setOpaque(true);
                item.setBackground(tema.getMenuFundo());
                item.setForeground(tema.getMenuTexto());
            }
        }
    }

    /**
     * Reinicia o jogo com a dificuldade atual.
     */
    private void reiniciarJogo() {
        cronometroJogo.parar();
        janela.dispose();
        iniciarJogoComDificuldade(linhasGame, colunasGame, tabuleiro.getMinas());
    }
}