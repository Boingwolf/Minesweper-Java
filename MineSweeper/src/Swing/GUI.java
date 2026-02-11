package Swing;

import Funcoes.CelulaPack.Celula;
import Funcoes.TabuleiroPack.Tabuleiro;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class GUI extends JPanel {

    private JButton[][] buttons;
    @SuppressWarnings("FieldMayBeFinal")
    private int linhas;
    @SuppressWarnings("FieldMayBeFinal")
    private int colunas;
    @SuppressWarnings("FieldMayBeFinal")
    private Tabuleiro tabuleiro;
    private JFrame frame;
    private JLabel tempoLabel;
    private JLabel minasLabel;
    private Timer timer;
    private int elapsedSeconds;

    private final ImageIcon bombaIcon;
    private final ImageIcon bandeiraIcon;
    private final ImageIcon celulaFechadaIcon;
    private final ImageIcon celulaAbertaIcon;
    private final ImageIcon explosaoIcon;
    private final Map<Integer, ImageIcon> numeroIcons;

    private static final int CELL_ICON_SIZE = 24;
    private static final double NUMERO_ICON_SCALE = 0.85;

    public GUI(Tabuleiro tabuleiro, int linhas, int colunas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.buttons = new JButton[linhas][colunas];
        this.tabuleiro = tabuleiro;

        ImageIcon bombaTemp = carregarIcone("/images/Bomba.png", CELL_ICON_SIZE);
        ImageIcon bandeiraTemp = carregarIcone("/images/Bandeira.png", CELL_ICON_SIZE);
        ImageIcon celulaFechadaTemp = carregarIcone("/images/CelulaFechada.png", CELL_ICON_SIZE);
        ImageIcon celulaAbertaTemp = carregarIcone("/images/CelulaAberta.png", CELL_ICON_SIZE);
        ImageIcon explosaoTemp = carregarIcone("/images/Explosao.png", CELL_ICON_SIZE);

        bombaIcon = (bombaTemp != null) ? bombaTemp : criarIconeBomba(CELL_ICON_SIZE);
        bandeiraIcon = (bandeiraTemp != null) ? bandeiraTemp : criarIconeBandeira(CELL_ICON_SIZE);
        celulaFechadaIcon = (celulaFechadaTemp != null) ? celulaFechadaTemp : criarIconeCelulaFechada(CELL_ICON_SIZE);
        celulaAbertaIcon = (celulaAbertaTemp != null) ? celulaAbertaTemp : criarIconeCelulaAberta(CELL_ICON_SIZE);
        explosaoIcon = (explosaoTemp != null) ? explosaoTemp : criarIconeExplosao(CELL_ICON_SIZE);

        numeroIcons = new HashMap<>();
        int numeroIconSize = (int) Math.round(CELL_ICON_SIZE * NUMERO_ICON_SCALE);
        for (int n = 1; n <= 8; n++) {
            ImageIcon icon = carregarIcone("/images/Numero" + n + ".png", numeroIconSize);
            if (icon == null) {
                icon = criarIconeNumero(n, CELL_ICON_SIZE);
            }
            numeroIcons.put(n, icon);
        }

        setLayout(new BorderLayout());

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 8));
        tempoLabel = new JLabel("Tempo: 00:00");
        minasLabel = new JLabel("Minas: 0");
        statusPanel.add(tempoLabel);
        statusPanel.add(minasLabel);
        add(statusPanel, BorderLayout.NORTH);

        JPanel gridPanel = new PatternPanel(new GridLayout(linhas, colunas));
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                buttons[i][j] = new JButton();
                int linha = i;
                int coluna = j;
                buttons[i][j].addActionListener(e -> {
                    tabuleiro.revelarCasa(linha, coluna);
                    atualizarTela();
                    if (tabuleiro.getTabuleiro()[linha][coluna].getTemMina()) {
                        JOptionPane.showMessageDialog(this, "Você perdeu!");
                        pararTimer();
                        frame.dispose();
                        mostrarMenuInicial();
                    } else if (tabuleiro.verificarVitoria()) {
                        JOptionPane.showMessageDialog(this, "Parabéns, você venceu!");
                        pararTimer();
                        frame.dispose();
                        mostrarMenuInicial();
                    }
                });
                buttons[i][j].setBorderPainted(true);
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].setContentAreaFilled(false);
                buttons[i][j].setMargin(new Insets(0, 0, 0, 0));
                buttons[i][j].setPreferredSize(new Dimension(CELL_ICON_SIZE, CELL_ICON_SIZE));
                if (celulaFechadaIcon != null) {
                    buttons[i][j].setIcon(celulaFechadaIcon);
                }

                buttons[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {

                        if (SwingUtilities.isRightMouseButton(e)) {

                            tabuleiro.toggleFlag(linha, coluna);

                            Celula cell = tabuleiro.getTabuleiro()[linha][coluna];

                            if (cell.getFlagged()) {
                                buttons[linha][coluna].setIcon(bandeiraIcon);
                            } else if (!cell.getEstaRevelada()) {
                                buttons[linha][coluna].setIcon(celulaFechadaIcon);
                            }
                            atualizarMinasRestantes();
                        }
                    }
                });
                gridPanel.add(buttons[i][j]);
            }
        }
        add(gridPanel, BorderLayout.CENTER);
        atualizarMinasRestantes();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setPaint(new GradientPaint(0, 0, new Color(245, 245, 245), 0, getHeight(), new Color(232, 232, 232)));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }

    private void atualizarTela() {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {

                Celula cell = tabuleiro.getTabuleiro()[i][j];

                if (cell.getEstaRevelada()) {
                    ImageIcon icon = null;
                    if (cell.getTemMina()) {
                        icon = (explosaoIcon != null) ? explosaoIcon : bombaIcon;
                    } else if (cell.getVizinhas() > 0) {
                        icon = numeroIcons.get(cell.getVizinhas());
                    } else {
                        icon = celulaAbertaIcon;
                    }

                    if (icon != null) {
                        buttons[i][j].setIcon(icon);
                        buttons[i][j].setDisabledIcon(icon);
                        buttons[i][j].setText("");
                    } else if (cell.getVizinhas() > 0) {
                        buttons[i][j].setText(String.valueOf(cell.getVizinhas()));
                    } else {
                        buttons[i][j].setText("");
                    }

                    buttons[i][j].setEnabled(false);
                }
            }
        }
    }

    private void aplicarIconeJanela(JFrame targetFrame) {
        Image icon = carregarIconeJanela("/images/favicon.png");
        if (icon != null) {
            targetFrame.setIconImage(icon);
        }
    }

    private Image carregarIconeJanela(String resourcePath) {
        URL url = getClass().getResource(resourcePath);
        if (url == null) {
            return null;
        }
        return new ImageIcon(url).getImage();
    }

    private ImageIcon carregarIcone(String resourcePath, int size) {
        URL url = getClass().getResource(resourcePath);
        if (url == null) {
            return null;
        }
        ImageIcon base = new ImageIcon(url);
        Image scaled = base.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private ImageIcon criarIconeCelulaFechada(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(200, 200, 200));
        g.fillRect(0, 0, size, size);
        g.setColor(new Color(240, 240, 240));
        g.drawLine(1, 1, size - 2, 1);
        g.drawLine(1, 1, 1, size - 2);
        g.setColor(new Color(150, 150, 150));
        g.drawLine(1, size - 2, size - 2, size - 2);
        g.drawLine(size - 2, 1, size - 2, size - 2);
        g.dispose();
        return new ImageIcon(img);
    }

    private ImageIcon criarIconeCelulaAberta(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(225, 225, 225));
        g.fillRect(0, 0, size, size);
        g.setColor(new Color(210, 210, 210));
        g.drawRect(0, 0, size - 1, size - 1);
        g.dispose();
        return new ImageIcon(img);
    }

    private ImageIcon criarIconeNumero(int numero, int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(225, 225, 225));
        g.fillRect(0, 0, size, size);
        g.setColor(new Color(210, 210, 210));
        g.drawRect(0, 0, size - 1, size - 1);

        Color numeroCor;
        switch (numero) {
            case 1:
                numeroCor = new Color(25, 118, 210);
                break;
            case 2:
                numeroCor = new Color(56, 142, 60);
                break;
            case 3:
                numeroCor = new Color(211, 47, 47);
                break;
            case 4:
                numeroCor = new Color(74, 20, 140);
                break;
            case 5:
                numeroCor = new Color(109, 76, 65);
                break;
            case 6:
                numeroCor = new Color(0, 121, 107);
                break;
            case 7:
                numeroCor = new Color(97, 97, 97);
                break;
            default:
                numeroCor = new Color(33, 33, 33);
                break;
        }
        g.setColor(numeroCor);
        g.setFont(new Font("Arial", Font.BOLD, Math.max(16, size - 4)));
        String text = String.valueOf(numero);
        FontMetrics fm = g.getFontMetrics();
        int x = (size - fm.stringWidth(text)) / 2;
        int y = (size - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(text, x, y);
        g.dispose();
        return new ImageIcon(img);
    }

    private ImageIcon criarIconeBandeira(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(225, 225, 225));
        g.fillRect(0, 0, size, size);
        g.setColor(new Color(100, 100, 100));
        g.fillRect(size / 2 - 1, 4, 2, size - 6);
        g.setColor(new Color(220, 0, 0));
        int[] xs = { size / 2, size / 2, size - 4 };
        int[] ys = { 4, size / 2, size / 4 };
        g.fillPolygon(xs, ys, 3);
        g.setColor(new Color(80, 80, 80));
        g.fillRect(size / 2 - 4, size - 4, 8, 3);
        g.dispose();
        return new ImageIcon(img);
    }

    private ImageIcon criarIconeBomba(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(225, 225, 225));
        g.fillRect(0, 0, size, size);
        g.setColor(new Color(30, 30, 30));
        g.fillOval(4, 6, size - 8, size - 10);
        g.setColor(new Color(60, 60, 60));
        g.fillOval(size / 2 - 4, 2, 8, 6);
        g.setColor(new Color(255, 193, 7));
        g.fillOval(size - 6, 2, 4, 4);
        g.dispose();
        return new ImageIcon(img);
    }

    private ImageIcon criarIconeExplosao(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(225, 225, 225));
        g.fillRect(0, 0, size, size);
        g.setColor(new Color(255, 152, 0));
        int[] xs = { size / 2, size - 4, size / 2 + 3, size - 2, size / 2, 2, size / 2 - 3, 4 };
        int[] ys = { 2, size / 2 - 3, size / 2, size - 4, size / 2 + 3, size - 2, size / 2, size / 2 - 3 };
        g.fillPolygon(xs, ys, xs.length);
        g.setColor(new Color(211, 47, 47));
        g.fillOval(size / 2 - 4, size / 2 - 4, 8, 8);
        g.dispose();
        return new ImageIcon(img);
    }

    private class PatternPanel extends JPanel {
        PatternPanel(LayoutManager layout) {
            super(layout);
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(235, 235, 235));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(new Color(228, 228, 228));
            int step = CELL_ICON_SIZE;
            for (int y = 0; y < getHeight(); y += step) {
                for (int x = 0; x < getWidth(); x += step) {
                    if (((x + y) / step) % 2 == 0) {
                        g2.fillRect(x, y, step, step);
                    }
                }
            }
            g2.dispose();
        }
    }

    
    private void reiniciarJogo() {
        pararTimer();
        frame.dispose();
        iniciarJogoComDificuldade(linhas, colunas, calcularMinas(linhas, colunas));
    }
    
    private int calcularMinas(int linhas, int colunas) {
        // Cálculo aproximado de minas baseado na dificuldade
        if (linhas == 9 && colunas == 9)
            return 10; // Fácil
        if (linhas == 12 && colunas == 12)
            return 20; // Médio
        if (linhas == 16 && colunas == 16)
            return 40; // Difícil
        return (linhas * colunas) / 8; // Default
    }
    
    public void mostrarMenuInicial() {
        frame = new JFrame("MineSweeper - Menu");
        aplicarIconeJanela(frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        
        // Painel principal com fundo
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        // Título
        JLabel titulo = new JLabel("MINESWEEPER");
        titulo.setFont(new Font("Arial", Font.BOLD, 32));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titulo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // Botão Fácil
        JButton facilButton = criarBotaoMenu("Fácil (9x9)");
        facilButton.addActionListener(e -> {
            frame.dispose();
            iniciarJogoComDificuldade(9, 9, 10);
        });
        mainPanel.add(facilButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Botão Médio
        JButton medioButton = criarBotaoMenu("Médio (12x12)");
        medioButton.addActionListener(e -> {
            frame.dispose();
            iniciarJogoComDificuldade(12, 12, 20);
        });
        mainPanel.add(medioButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Botão Difícil
        JButton dificilButton = criarBotaoMenu("Difícil (16x16)");
        dificilButton.addActionListener(e -> {
            frame.dispose();
            iniciarJogoComDificuldade(16, 16, 40);
        });
        mainPanel.add(dificilButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Botão Sair
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
        aplicarIconeJanela(frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setSize(700, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        iniciarTimer();
        atualizarMinasRestantes();

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Opções");
        JMenuItem reiniciarItem = new JMenuItem("Reiniciar");
        reiniciarItem.addActionListener(e -> {
            reiniciarJogo();
        });
        JMenuItem menuInicialItem = new JMenuItem("Menu Inicial");
        menuInicialItem.addActionListener(e -> {
            pararTimer();
            frame.dispose();
            mostrarMenuInicial();
        });
        menu.add(reiniciarItem);
        menu.add(menuInicialItem);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
    }

    private void iniciarTimer() {
        elapsedSeconds = 0;
        atualizarTempoLabel();
        timer = new Timer(1000, e -> {
            elapsedSeconds++;
            atualizarTempoLabel();
        });
        timer.start();
    }

    private void pararTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    private void atualizarTempoLabel() {
        int minutos = elapsedSeconds / 60;
        int segundos = elapsedSeconds % 60;
        tempoLabel.setText(String.format("Tempo: %02d:%02d", minutos, segundos));
    }

    private void atualizarMinasRestantes() {
        int minasRestantes = tabuleiro.getMinas() - contarBandeiras();
        if (minasRestantes < 0) {
            minasRestantes = 0;
        }
        minasLabel.setText("Minas: " + minasRestantes);
    }

    private int contarBandeiras() {
        int count = 0;
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                if (tabuleiro.getTabuleiro()[i][j].getFlagged()) {
                    count++;
                }
            }
        }
        return count;
    }
}