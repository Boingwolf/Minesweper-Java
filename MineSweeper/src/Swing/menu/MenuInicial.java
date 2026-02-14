package Swing.menu;

import Swing.icons.IconManager;
import Swing.stats.EstatisticasService;
import Swing.utils.Tema;
import Swing.utils.TemaManager;
import Swing.utils.ThemedDialog;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * Tela de menu inicial com opcoes de dificuldade e configuracao.
 */
public class MenuInicial {

    private static boolean primeiroCliqueSeguroAtivado = true;
    private final boolean possuiJogoSalvo;
    private final MenuCallback callback;
    private final EstatisticasService estatisticasService;

    /**
     * Callback para selecao de dificuldade.
     */
    public interface MenuCallback {
        /**
         * Disparado quando o usuario escolhe a dificuldade.
         *
         * @param linhas  numero de linhas
         * @param colunas numero de colunas
         * @param minas   quantidade de minas
         */
        void onDificuldadeSelecionada(int linhas, int colunas, int minas);

        /**
         * Disparado quando o usuário escolhe iniciar o tutorial interativo.
         */
        void onTutorialSelecionado();

        /**
         * Disparado quando o usuário escolhe continuar um jogo salvo.
         */
        void onContinuarJogoSalvo();
    }

    /**
     * Cria o menu inicial com um callback de selecao.
     *
     * @param possuiJogoSalvo indica se há um jogo pausado disponível
     * @param callback        callback para iniciar o jogo
     */
    public MenuInicial(boolean possuiJogoSalvo, MenuCallback callback) {
        this.possuiJogoSalvo = possuiJogoSalvo;
        this.callback = callback;
        this.estatisticasService = new EstatisticasService();
    }

    /**
     * Exibe a janela do menu inicial.
     */
    public void mostrar() {
        JFrame janela = new JFrame("MineSweeper - Menu");
        IconManager gestorIcones = new IconManager();
        janela.setIconImage(gestorIcones.carregarIconeJanela("/images/favicon.png"));
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setSize(550, 750);
        janela.setLocationRelativeTo(null);
        janela.setLayout(new BorderLayout());

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel titulo = new JLabel("MINESWEEPER");
        titulo.setFont(new Font("Arial", Font.BOLD, 32));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelPrincipal.add(titulo);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel painelOpcao = new JPanel();
        painelOpcao.setLayout(new BoxLayout(painelOpcao, BoxLayout.Y_AXIS));
        painelOpcao.setBorder(BorderFactory.createTitledBorder("Opções"));

        JLabel labelPrimeiroClique = new JLabel("Primeiro Clique Seguro:");
        labelPrimeiroClique.setFont(new Font("Arial", Font.PLAIN, 12));
        painelOpcao.add(labelPrimeiroClique);

        ButtonGroup grupoPrimeiroClique = new ButtonGroup();
        JRadioButton botaoSim = new JRadioButton("Ativado", primeiroCliqueSeguroAtivado);
        JRadioButton botaoNao = new JRadioButton("Desativado", !primeiroCliqueSeguroAtivado);

        botaoSim.addActionListener(e -> primeiroCliqueSeguroAtivado = true);
        botaoNao.addActionListener(e -> primeiroCliqueSeguroAtivado = false);

        grupoPrimeiroClique.add(botaoSim);
        grupoPrimeiroClique.add(botaoNao);

        painelOpcao.add(botaoSim);
        painelOpcao.add(botaoNao);
        painelOpcao.setMaximumSize(new Dimension(250, 100));
        painelOpcao.setAlignmentX(Component.CENTER_ALIGNMENT);

        painelPrincipal.add(painelOpcao);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel painelTema = new JPanel();
        painelTema.setLayout(new BoxLayout(painelTema, BoxLayout.Y_AXIS));
        painelTema.setBorder(BorderFactory.createTitledBorder("Tema"));

        JLabel labelTema = new JLabel("Escolha o tema:");
        labelTema.setFont(new Font("Arial", Font.PLAIN, 12));
        painelTema.add(labelTema);

        ButtonGroup grupoTema = new ButtonGroup();
        JRadioButton botaoClaro = new JRadioButton("Claro", TemaManager.getTemaAtual() == Tema.CLARO);
        JRadioButton botaoEscuro = new JRadioButton("Escuro", TemaManager.getTemaAtual() == Tema.ESCURO);

        grupoTema.add(botaoClaro);
        grupoTema.add(botaoEscuro);

        painelTema.add(botaoClaro);
        painelTema.add(botaoEscuro);
        painelTema.setMaximumSize(new Dimension(250, 100));
        painelTema.setAlignmentX(Component.CENTER_ALIGNMENT);

        painelPrincipal.add(painelTema);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton botaoContinuar = criarBotaoMenu("Continuar Jogo Salvo");
        botaoContinuar.setEnabled(possuiJogoSalvo);
        botaoContinuar.addActionListener(e -> {
            janela.dispose();
            callback.onContinuarJogoSalvo();
        });
        painelPrincipal.add(botaoContinuar);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton botaoFacil = criarBotaoMenu("Fácil (9x9)");
        botaoFacil.addActionListener(e -> {
            janela.dispose();
            callback.onDificuldadeSelecionada(9, 9, 10);
        });
        painelPrincipal.add(botaoFacil);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton botaoMedio = criarBotaoMenu("Médio (12x12)");
        botaoMedio.addActionListener(e -> {
            janela.dispose();
            callback.onDificuldadeSelecionada(12, 12, 20);
        });
        painelPrincipal.add(botaoMedio);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton botaoDificil = criarBotaoMenu("Difícil (16x16)");
        botaoDificil.addActionListener(e -> {
            janela.dispose();
            callback.onDificuldadeSelecionada(16, 16, 40);
        });
        painelPrincipal.add(botaoDificil);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton botaoTutorial = criarBotaoMenu("Tutorial Interativo");
        botaoTutorial.addActionListener(e -> {
            janela.dispose();
            callback.onTutorialSelecionado();
        });
        painelPrincipal.add(botaoTutorial);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton botaoSair = criarBotaoMenu("Sair");
        botaoSair.addActionListener(e -> System.exit(0));
        JButton botaoEstatisticas = criarBotaoMenu("Estatísticas");
        botaoEstatisticas.addActionListener(e -> ThemedDialog.mostrar(janela, "Estatísticas do Jogador",
                estatisticasService.gerarResumo(), "Fechar", 400));
        painelPrincipal.add(botaoEstatisticas);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));
        painelPrincipal.add(botaoSair);

        JButton[] botoesMenu = new JButton[] { botaoContinuar, botaoFacil, botaoMedio, botaoDificil, botaoTutorial,
                botaoEstatisticas, botaoSair };
        JRadioButton[] radiosMenu = new JRadioButton[] { botaoSim, botaoNao, botaoClaro, botaoEscuro };

        botaoClaro.addActionListener(e -> {
            TemaManager.setTemaAtual(Tema.CLARO);
            aplicarTema(TemaManager.getTemaAtual(), janela, painelPrincipal, painelOpcao, painelTema, titulo,
                    labelPrimeiroClique, labelTema, botoesMenu, radiosMenu);
        });
        botaoEscuro.addActionListener(e -> {
            TemaManager.setTemaAtual(Tema.ESCURO);
            aplicarTema(TemaManager.getTemaAtual(), janela, painelPrincipal, painelOpcao, painelTema, titulo,
                    labelPrimeiroClique, labelTema, botoesMenu, radiosMenu);
        });

        janela.add(painelPrincipal, BorderLayout.CENTER);
        aplicarTema(TemaManager.getTemaAtual(), janela, painelPrincipal, painelOpcao, painelTema, titulo,
                labelPrimeiroClique, labelTema, botoesMenu, radiosMenu);
        janela.setVisible(true);
    }

    /**
     * Cria um botao padrao para o menu.
     *
     * @param texto texto exibido
     * @return botao configurado
     */
    private JButton criarBotaoMenu(String texto) {
        JButton botao = new JButton(texto);
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setFont(new Font("Arial", Font.PLAIN, 18));
        botao.setMaximumSize(new Dimension(250, 40));
        botao.setFocusPainted(false);
        botao.setOpaque(true);
        return botao;
    }

    /**
     * Aplica o tema atual aos componentes do menu.
     *
     * @param tema            tema escolhido
     * @param janela          janela do menu
     * @param painelPrincipal painel principal
     * @param painelOpcao     painel de opcoes
     * @param painelTema      painel de tema
     * @param titulo          label do titulo
     * @param labelPrimeiro   label do primeiro clique
     * @param labelTema       label do tema
     * @param botoes          botoes do menu
     * @param radios          botoes de radio
     */
    private void aplicarTema(Tema tema, JFrame janela, JPanel painelPrincipal, JPanel painelOpcao, JPanel painelTema,
            JLabel titulo, JLabel labelPrimeiro, JLabel labelTema, JButton[] botoes, JRadioButton[] radios) {
        janela.getContentPane().setBackground(tema.getPainelFundo());

        painelPrincipal.setOpaque(true);
        painelPrincipal.setBackground(tema.getPainelFundo());

        painelOpcao.setOpaque(true);
        painelOpcao.setBackground(tema.getPainelFundo());

        painelTema.setOpaque(true);
        painelTema.setBackground(tema.getPainelFundo());

        titulo.setForeground(tema.getTextoPadrao());
        labelPrimeiro.setForeground(tema.getTextoPadrao());
        labelTema.setForeground(tema.getTextoPadrao());

        for (JButton botao : botoes) {
            botao.setBackground(tema.getBotaoFundo());
            botao.setForeground(tema.getBotaoTexto());
        }

        for (JRadioButton radio : radios) {
            radio.setOpaque(true);
            radio.setBackground(tema.getPainelFundo());
            radio.setForeground(tema.getTextoPadrao());
        }
    }

    /**
     * Retorna se o modo de primeiro clique seguro esta ativado.
     *
     * @return true se ativado; caso contrario false
     */
    public static boolean isPrimeiroCliqueSeguroAtivado() {
        return primeiroCliqueSeguroAtivado;
    }
}
