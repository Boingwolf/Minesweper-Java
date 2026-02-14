package Swing;

import Funcoes.TabuleiroPack.Tabuleiro;
import Swing.components.GamePanel;
import Swing.components.StatusPanel;
import Swing.icons.IconManager;
import Swing.menu.MenuInicial;
import Swing.stats.DificuldadeJogo;
import Swing.stats.EstatisticasService;
import Swing.stats.LeaderboardService;
import Swing.utils.GameSaveData;
import Swing.utils.GameSaveManager;
import Swing.utils.GameTimer;
import Swing.utils.Tema;
import Swing.utils.TemaManager;
import Swing.utils.ThemedDialog;
import Swing.utils.TutorialInterativo;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Controla a janela principal e o fluxo de telas do jogo.
 */
public class GUIConfig {

    private JFrame janela;
    private GamePanel painelJogo;
    private StatusPanel painelEstado;
    private GameTimer cronometroJogo;
    private final Tabuleiro tabuleiro;
    private final EstatisticasService estatisticasService;
    private final LeaderboardService leaderboardService;
    private final GameSaveManager gameSaveManager;
    private final int linhasGame;
    private final int colunasGame;
    private final int minasGame;
    private final boolean modoTutorial;
    private DificuldadeJogo dificuldadeAtual;
    private int segundosIniciais;
    private boolean primeiroCliquePendente;

    /**
     * Cria a GUI vinculada ao tabuleiro.
     *
     * @param tabuleiro tabuleiro do jogo
     * @param linhas    numero de linhas (usado para configuracao inicial)
     * @param colunas   numero de colunas (usado para configuracao inicial)
     */
    public GUIConfig(Tabuleiro tabuleiro, int linhas, int colunas) {
        this(tabuleiro, linhas, colunas, false);
    }

    /**
     * Cria a GUI vinculada ao tabuleiro, com suporte opcional a tutorial.
     *
     * @param tabuleiro    tabuleiro do jogo
     * @param linhas       numero de linhas
     * @param colunas      numero de colunas
     * @param modoTutorial indica se a sessão é tutorial
     */
    public GUIConfig(Tabuleiro tabuleiro, int linhas, int colunas, boolean modoTutorial) {
        this.tabuleiro = tabuleiro;
        this.estatisticasService = new EstatisticasService();
        this.leaderboardService = new LeaderboardService();
        this.gameSaveManager = new GameSaveManager();
        this.linhasGame = linhas;
        this.colunasGame = colunas;
        this.minasGame = tabuleiro.getMinas();
        this.modoTutorial = modoTutorial;
        this.dificuldadeAtual = DificuldadeJogo.deConfiguracao(linhas, colunas, minasGame);
        this.segundosIniciais = 0;
        this.primeiroCliquePendente = !modoTutorial;
    }

    /**
     * Exibe a tela de menu inicial.
     */
    public void mostrarMenuInicial() {
        MenuInicial menu = new MenuInicial(gameSaveManager.existeSave(), new MenuInicial.MenuCallback() {
            @Override
            public void onDificuldadeSelecionada(int linhas, int colunas, int minas) {
                iniciarJogoComDificuldade(linhas, colunas, minas);
            }

            @Override
            public void onTutorialSelecionado() {
                iniciarTutorialInterativo();
            }

            @Override
            public void onContinuarJogoSalvo() {
                carregarJogoSalvo();
            }
        });
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
        gameSaveManager.limparSave();
        GamePanel.setPrimeiroCliqueSeguroAtivado(MenuInicial.isPrimeiroCliqueSeguroAtivado());

        Tabuleiro novoTabuleiro = new Tabuleiro(linhas, colunas, minas);
        novoTabuleiro.iniciarTabuleiro();
        novoTabuleiro.gerarMinas();
        novoTabuleiro.calcularVizinhas();

        GUIConfig novaGui = new GUIConfig(novoTabuleiro, linhas, colunas, false);
        novaGui.dificuldadeAtual = DificuldadeJogo.deConfiguracao(linhas, colunas, minas);
        novaGui.iniciarJanela();
    }

    /**
     * Inicia uma sessão de tutorial interativo para novos jogadores.
     */
    private void iniciarTutorialInterativo() {
        gameSaveManager.limparSave();
        boolean[][] mapaTutorial = new boolean[][] {
                { false, false, false, false, false },
                { false, true, false, false, false },
                { false, false, false, false, false },
                { false, false, false, true, false },
                { true, false, false, false, false }
        };

        Tabuleiro tabuleiroTutorial = new Tabuleiro(5, 5, 3);
        tabuleiroTutorial.configurarTabuleiroTutorial(mapaTutorial);

        GUIConfig guiTutorial = new GUIConfig(tabuleiroTutorial, 5, 5, true);
        guiTutorial.primeiroCliquePendente = false;
        guiTutorial.iniciarJanela();
    }

    /**
     * Carrega um jogo salvo em disco e inicia uma nova sessão com o estado
     * restaurado.
     */
    private void carregarJogoSalvo() {
        GameSaveData saveData = gameSaveManager.carregar();
        if (saveData == null) {
            ThemedDialog.mostrar(null, "Continuar Jogo", "Nenhum jogo salvo válido foi encontrado.", "Fechar", 300);
            mostrarMenuInicial();
            return;
        }

        Tabuleiro tabuleiroSalvo = new Tabuleiro(saveData.getLinhas(), saveData.getColunas(), saveData.getMinas());
        tabuleiroSalvo.restaurarEstado(
                saveData.getMinasMapa(),
                saveData.getReveladasMapa(),
                saveData.getBandeirasMapa(),
                saveData.getVizinhasMapa());

        GamePanel.setPrimeiroCliqueSeguroAtivado(saveData.isPrimeiroCliqueSeguroAtivado());

        GUIConfig guiCarregada = new GUIConfig(tabuleiroSalvo, saveData.getLinhas(), saveData.getColunas(), false);
        guiCarregada.dificuldadeAtual = DificuldadeJogo.deConfiguracao(
                saveData.getLinhas(), saveData.getColunas(), saveData.getMinas());
        guiCarregada.segundosIniciais = saveData.getSegundosDecorridos();
        guiCarregada.primeiroCliquePendente = saveData.isPrimeiroCliquePendente();
        guiCarregada.iniciarJanela();
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
                gameSaveManager.limparSave();
                registrarResultadoPartida(false);
                janela.dispose();
                mostrarMenuInicial();
            }

            @Override
            public void onGameWon() {
                cronometroJogo.parar();
                gameSaveManager.limparSave();
                registrarResultadoPartida(true);
                janela.dispose();
                mostrarMenuInicial();
            }
        };

        TutorialInterativo tutorialInterativo = null;
        if (modoTutorial) {
            tutorialInterativo = new TutorialInterativo();
            painelEstado.atualizarDicaTutorial(tutorialInterativo.getDicaAtual());
        } else {
            painelEstado.limparDicaTutorial();
        }

        painelJogo = new GamePanel(tabuleiro, linhasGame, colunasGame, painelEstado, callbackJogo, tutorialInterativo,
                primeiroCliquePendente);

        janela.setLayout(new BorderLayout());
        janela.add(painelEstado, BorderLayout.NORTH);
        janela.add(painelJogo, BorderLayout.CENTER);

        cronometroJogo = new GameTimer(painelEstado.getTempoLabel());
        cronometroJogo.iniciarComSegundos(segundosIniciais);

        painelEstado.atualizarMinasRestantes(painelJogo.getQuantidadeBandeiras());

        final JMenuBar barraMenu = new JMenuBar();
        JMenu menu = new JMenu("Opções");
        JMenuItem itemReiniciar = new JMenuItem("Reiniciar");
        itemReiniciar.addActionListener(e -> reiniciarJogo());
        JMenuItem itemPausar = new JMenuItem("Pausar");
        itemPausar.addActionListener(e -> alternarPausa(itemPausar));
        JMenuItem itemEstatisticas = new JMenuItem("Estatísticas");
        itemEstatisticas.addActionListener(e -> mostrarEstatisticas());
        JMenuItem itemLeaderboard = new JMenuItem("Leaderboard");
        itemLeaderboard.addActionListener(e -> mostrarLeaderboard());
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
            aplicarTema(barraMenu, menu, itemReiniciar, itemPausar, itemEstatisticas, itemLeaderboard,
                    itemMenuInicial, menuTema);
        });
        JMenuItem itemTemaEscuro = new JMenuItem("Escuro");
        itemTemaEscuro.addActionListener(e -> {
            TemaManager.setTemaAtual(Tema.ESCURO);
            aplicarTema(barraMenu, menu, itemReiniciar, itemPausar, itemEstatisticas, itemLeaderboard,
                    itemMenuInicial, menuTema);
        });
        menuTema.add(itemTemaClaro);
        menuTema.add(itemTemaEscuro);

        menu.add(itemReiniciar);
        menu.add(itemPausar);
        menu.add(itemEstatisticas);
        menu.add(itemLeaderboard);
        menu.add(itemMenuInicial);
        menu.addSeparator();
        menu.add(menuTema);
        barraMenu.add(menu);
        janela.setJMenuBar(barraMenu);

        janela.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                if (!modoTutorial && painelJogo != null) {
                    salvarJogoAtual();
                } else {
                    gameSaveManager.limparSave();
                }

                if (cronometroJogo != null) {
                    cronometroJogo.parar();
                }
            }
        });

        aplicarTema(barraMenu, menu, itemReiniciar, itemPausar, itemEstatisticas, itemLeaderboard, itemMenuInicial,
                menuTema);

        janela.setVisible(true);

        if (modoTutorial && tutorialInterativo != null) {
            ThemedDialog.mostrar(janela, "Tutorial Interativo", tutorialInterativo.getExplicacaoInicial(), "Continuar",
                    340);
        }
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
    private void aplicarTema(JMenuBar barraMenu, JMenu menu, JMenuItem itemReiniciar, JMenuItem itemPausar,
            JMenuItem itemEstatisticas, JMenuItem itemLeaderboard, JMenuItem itemMenuInicial, JMenu menuTema) {
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

        itemPausar.setOpaque(true);
        itemPausar.setBackground(tema.getMenuFundo());
        itemPausar.setForeground(tema.getMenuTexto());

        itemEstatisticas.setOpaque(true);
        itemEstatisticas.setBackground(tema.getMenuFundo());
        itemEstatisticas.setForeground(tema.getMenuTexto());

        itemLeaderboard.setOpaque(true);
        itemLeaderboard.setBackground(tema.getMenuFundo());
        itemLeaderboard.setForeground(tema.getMenuTexto());

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
        gameSaveManager.limparSave();
        janela.dispose();
        if (modoTutorial) {
            iniciarTutorialInterativo();
            return;
        }

        iniciarJogoComDificuldade(linhasGame, colunasGame, minasGame);
    }

    /**
     * Registra o resultado da partida atual para estatísticas.
     *
     * @param venceu true quando vitória
     */
    private void registrarResultadoPartida(boolean venceu) {
        int segundos = cronometroJogo != null ? cronometroJogo.getSegundosDecorridos() : 0;
        estatisticasService.registrarResultado(dificuldadeAtual, venceu, segundos);

        if (venceu && !modoTutorial) {
            String nomeJogador = solicitarNomeJogador();
            leaderboardService.adicionarResultado(dificuldadeAtual, nomeJogador, segundos);
        }
    }

    /**
     * Exibe o resumo de estatísticas ao jogador.
     */
    private void mostrarEstatisticas() {
        ThemedDialog.mostrar(janela, "Estatísticas do Jogador", estatisticasService.gerarResumo(), "Fechar", 400);
    }

    /**
     * Exibe o leaderboard consolidado por dificuldade.
     */
    private void mostrarLeaderboard() {
        ThemedDialog.mostrar(janela, "Leaderboard", leaderboardService.gerarResumoRanking(), "Fechar", 500);
    }

    /**
     * Solicita o nome do jogador para registrar no leaderboard.
     *
     * @return nome informado, ou nome padrão em caso de vazio
     */
    private String solicitarNomeJogador() {
        Tema tema = TemaManager.getTemaAtual();
        JPanel painel = new JPanel(new GridLayout(2, 1, 0, 8));
        painel.setBackground(tema.getPainelFundo());

        JLabel label = new JLabel("Vitória! Informe seu nome para o leaderboard:");
        label.setForeground(tema.getTextoPadrao());

        JTextField campoNome = new JTextField(System.getProperty("user.name", "Jogador"), 20);
        campoNome.setBackground(tema.getBotaoFundo());
        campoNome.setForeground(tema.getBotaoTexto());

        painel.add(label);
        painel.add(campoNome);

        int resposta = JOptionPane.showConfirmDialog(
                janela,
                painel,
                "Leaderboard",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (resposta != JOptionPane.OK_OPTION) {
            return "Jogador";
        }

        String nome = campoNome.getText();
        if (nome == null || nome.isBlank()) {
            return "Jogador";
        }

        return nome.trim();
    }

    /**
     * Alterna entre os estados de pausa e continuação da partida.
     *
     * @param itemPausar item de menu responsável pela ação
     */
    private void alternarPausa(JMenuItem itemPausar) {
        if (modoTutorial || painelJogo == null || cronometroJogo == null) {
            return;
        }

        if (!painelJogo.isPausado()) {
            painelJogo.setPausado(true);
            cronometroJogo.pausar();
            salvarJogoAtual();
            itemPausar.setText("Retomar");
        } else {
            painelJogo.setPausado(false);
            cronometroJogo.retomar();
            itemPausar.setText("Pausar");
        }
    }

    /**
     * Serializa e persiste o estado atual da partida.
     */
    private void salvarJogoAtual() {
        if (modoTutorial || tabuleiro.getTabuleiro() == null) {
            return;
        }

        int linhas = tabuleiro.getLinhas();
        int colunas = tabuleiro.getColunas();

        boolean[][] minasMapa = new boolean[linhas][colunas];
        boolean[][] reveladasMapa = new boolean[linhas][colunas];
        boolean[][] bandeirasMapa = new boolean[linhas][colunas];
        int[][] vizinhasMapa = new int[linhas][colunas];

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                minasMapa[i][j] = tabuleiro.getTabuleiro()[i][j].getTemMina();
                reveladasMapa[i][j] = tabuleiro.getTabuleiro()[i][j].getEstaRevelada();
                bandeirasMapa[i][j] = tabuleiro.getTabuleiro()[i][j].isTemBandeira();
                vizinhasMapa[i][j] = tabuleiro.getTabuleiro()[i][j].getVizinhas();
            }
        }

        GameSaveData estado = new GameSaveData(
                linhas,
                colunas,
                tabuleiro.getMinas(),
                cronometroJogo.getSegundosDecorridos(),
                painelJogo.isPrimeiroCliquePendente(),
                GamePanel.isPrimeiroCliqueSeguroAtivado(),
                minasMapa,
                reveladasMapa,
                bandeirasMapa,
                vizinhasMapa);

        gameSaveManager.salvar(estado);
    }
}