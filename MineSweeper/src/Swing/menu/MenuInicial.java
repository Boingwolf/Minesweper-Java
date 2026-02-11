package Swing.menu;

import Swing.icons.IconManager;
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

public class MenuInicial {

    private static boolean primeiroCliqueSeguroAtivado = true;
    private final MenuCallback callback;

    public interface MenuCallback {
        void onDificuldadeSelecionada(int linhas, int colunas, int minas);
    }

    public MenuInicial(MenuCallback callback) {
        this.callback = callback;
    }

    public void mostrar() {
        JFrame frame = new JFrame("MineSweeper - Menu");
        IconManager iconManager = new IconManager();
        frame.setIconImage(iconManager.carregarIconeJanela("/images/favicon.png"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(550, 650);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel titulo = new JLabel("MINESWEEPER");
        titulo.setFont(new Font("Arial", Font.BOLD, 32));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titulo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel opcaoPanel = new JPanel();
        opcaoPanel.setLayout(new BoxLayout(opcaoPanel, BoxLayout.Y_AXIS));
        opcaoPanel.setBorder(BorderFactory.createTitledBorder("Opções"));

        JLabel labelPrimeiroClique = new JLabel("Primeiro Clique Seguro:");
        labelPrimeiroClique.setFont(new Font("Arial", Font.PLAIN, 12));
        opcaoPanel.add(labelPrimeiroClique);

        ButtonGroup grupoPrimeiroClique = new ButtonGroup();
        JRadioButton simButton = new JRadioButton("Ativado", primeiroCliqueSeguroAtivado);
        JRadioButton naoButton = new JRadioButton("Desativado", !primeiroCliqueSeguroAtivado);

        simButton.addActionListener(e -> primeiroCliqueSeguroAtivado = true);
        naoButton.addActionListener(e -> primeiroCliqueSeguroAtivado = false);

        grupoPrimeiroClique.add(simButton);
        grupoPrimeiroClique.add(naoButton);

        opcaoPanel.add(simButton);
        opcaoPanel.add(naoButton);
        opcaoPanel.setMaximumSize(new Dimension(250, 100));
        opcaoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(opcaoPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton facilButton = criarBotaoMenu("Fácil (9x9)");
        facilButton.addActionListener(e -> {
            frame.dispose();
            callback.onDificuldadeSelecionada(9, 9, 10);
        });
        mainPanel.add(facilButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton medioButton = criarBotaoMenu("Médio (12x12)");
        medioButton.addActionListener(e -> {
            frame.dispose();
            callback.onDificuldadeSelecionada(12, 12, 20);
        });
        mainPanel.add(medioButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton dificilButton = criarBotaoMenu("Difícil (16x16)");
        dificilButton.addActionListener(e -> {
            frame.dispose();
            callback.onDificuldadeSelecionada(16, 16, 40);
        });
        mainPanel.add(dificilButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton sairButton = criarBotaoMenu("Sair");
        sairButton.addActionListener(e -> System.exit(0));
        mainPanel.add(sairButton);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JButton criarBotaoMenu(String texto) {
        JButton button = new JButton(texto);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setMaximumSize(new Dimension(250, 40));
        button.setFocusPainted(false);
        return button;
    }

    public static boolean isPrimeiroCliqueSeguroAtivado() {
        return primeiroCliqueSeguroAtivado;
    }
}
