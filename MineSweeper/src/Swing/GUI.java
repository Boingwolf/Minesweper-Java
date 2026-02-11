package Swing;

import Funcoes.TabuleiroPack.Tabuleiro;
import Swing.components.GamePanel;
import Swing.components.StatusPanel;
import Swing.icons.IconManager;
import Swing.menu.MenuInicial;
import Swing.utils.GameTimer;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class GUI {

    private JFrame frame;
    private GamePanel gamePanel;
    private StatusPanel statusPanel;
    private GameTimer gameTimer;
    private Tabuleiro tabuleiro;

    public GUI(Tabuleiro tabuleiro, int linhas, int colunas) {
        this.tabuleiro = tabuleiro;
    }

    public void mostrarMenuInicial() {
        MenuInicial menu = new MenuInicial(this::iniciarJogoComDificuldade);
        menu.mostrar();
    }

    private void iniciarJogoComDificuldade(int linhas, int colunas, int minas) {
        Tabuleiro novoTabuleiro = new Tabuleiro(linhas, colunas, minas);
        novoTabuleiro.iniciarTabuleiro();
        novoTabuleiro.gerarMinas();
        novoTabuleiro.calcularVizinhas();

        GUI novaGUI = new GUI(novoTabuleiro, linhas, colunas);
        novaGUI.iniciarJanela();
    }

    public void iniciarJanela() {
        frame = new JFrame("MineSweeper");
        IconManager iconManager = new IconManager();
        frame.setIconImage(iconManager.carregarIconeJanela("/images/favicon.png"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 800);
        frame.setLocationRelativeTo(null);

        statusPanel = new StatusPanel(tabuleiro);

        GamePanel.GameCallback gameCallback = new GamePanel.GameCallback() {
            @Override
            public void onGameLost() {
                gameTimer.parar();
                frame.dispose();
                mostrarMenuInicial();
            }

            @Override
            public void onGameWon() {
                gameTimer.parar();
                frame.dispose();
                mostrarMenuInicial();
            }
        };

        gamePanel = new GamePanel(tabuleiro, 9, 9, statusPanel, gameCallback);

        frame.setLayout(new BorderLayout());
        frame.add(statusPanel, BorderLayout.NORTH);
        frame.add(gamePanel, BorderLayout.CENTER);

        gameTimer = new GameTimer(statusPanel.getTempoLabel());
        gameTimer.iniciar();

        statusPanel.atualizarMinasRestantes(0);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Opções");
        JMenuItem reiniciarItem = new JMenuItem("Reiniciar");
        reiniciarItem.addActionListener(e -> reiniciarJogo());
        JMenuItem menuInicialItem = new JMenuItem("Menu Inicial");
        menuInicialItem.addActionListener(e -> {
            gameTimer.parar();
            frame.dispose();
            mostrarMenuInicial();
        });
        menu.add(reiniciarItem);
        menu.add(menuInicialItem);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        frame.setVisible(true);
    }

    private void reiniciarJogo() {
        gameTimer.parar();
        frame.dispose();
        iniciarJogoComDificuldade(9, 9, 10);
    }
}