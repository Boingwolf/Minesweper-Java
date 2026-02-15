package Swing.utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Exibe diálogos respeitando o tema atual da aplicação.
 */
public final class ThemedDialog {

    private ThemedDialog() {
    }

    /**
     * Mostra um diálogo temático com botão padrão.
     *
     * @param parent   componente pai
     * @param titulo   título da janela
     * @param conteudo conteúdo textual
     */
    public static void mostrar(Component parent, String titulo, String conteudo) {
        mostrar(parent, titulo, conteudo, "Fechar", 360);
    }

    /**
     * Mostra um diálogo temático customizável.
     *
     * @param parent     componente pai
     * @param titulo     título da janela
     * @param conteudo   conteúdo textual
     * @param textoBotao texto do botão de ação
     * @param altura     altura da janela
     */
    public static void mostrar(Component parent, String titulo, String conteudo, String textoBotao, int altura) {
        Tema tema = TemaManager.getTemaAtual();

        JDialog dialog = criarDialog(parent, titulo);

        JPanel painelRaiz = new JPanel(new BorderLayout(0, 10));
        painelRaiz.setBackground(tema.getPainelFundo());
        painelRaiz.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JTextArea areaTexto = new JTextArea(conteudo);
        areaTexto.setEditable(false);
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        areaTexto.setCaretPosition(0);
        areaTexto.setBackground(tema.getPainelFundo());
        areaTexto.setForeground(tema.getTextoPadrao());
        areaTexto.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane scroll = new JScrollPane(areaTexto);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(tema.getPainelFundo());
        scroll.setBackground(tema.getPainelFundo());
        scroll.setOpaque(true);

        JButton botaoFechar = new JButton(textoBotao);
        botaoFechar.setFocusPainted(false);
        botaoFechar.setOpaque(true);
        botaoFechar.setBackground(tema.getBotaoFundo());
        botaoFechar.setForeground(tema.getBotaoTexto());
        botaoFechar.addActionListener(e -> dialog.dispose());

        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        painelAcoes.setBackground(tema.getPainelFundo());
        painelAcoes.add(botaoFechar);

        painelRaiz.add(scroll, BorderLayout.CENTER);
        painelRaiz.add(painelAcoes, BorderLayout.SOUTH);

        dialog.setContentPane(painelRaiz);
        dialog.setSize(460, altura);
        dialog.setMinimumSize(dialog.getSize());
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    /**
     * Solicita um texto ao utilizador com diálogo temático.
     *
     * @param parent      componente pai
     * @param titulo      título da janela
     * @param mensagem    mensagem exibida acima do campo
     * @param valorPadrao valor inicial do campo
     * @return texto confirmado, ou null quando cancelado
     */
    public static String solicitarTexto(Component parent, String titulo, String mensagem, String valorPadrao) {
        Tema tema = TemaManager.getTemaAtual();
        JDialog dialog = criarDialog(parent, titulo);

        final String[] resultado = new String[1];

        JPanel painelRaiz = new JPanel(new BorderLayout(0, 12));
        painelRaiz.setBackground(tema.getPainelFundo());
        painelRaiz.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel painelConteudo = new JPanel(new GridLayout(2, 1, 0, 8));
        painelConteudo.setBackground(tema.getPainelFundo());

        JLabel labelMensagem = new JLabel(mensagem);
        labelMensagem.setForeground(tema.getTextoPadrao());

        JTextField campoTexto = new JTextField(valorPadrao == null ? "" : valorPadrao, 20);
        campoTexto.setBackground(tema.getBotaoFundo());
        campoTexto.setForeground(tema.getBotaoTexto());
        campoTexto.setCaretColor(tema.getBotaoTexto());
        campoTexto.setBorder(BorderFactory.createLineBorder(tema.getMenuFundo()));

        painelConteudo.add(labelMensagem);
        painelConteudo.add(campoTexto);

        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setFocusPainted(false);
        botaoCancelar.setOpaque(true);
        botaoCancelar.setBackground(tema.getBotaoFundo());
        botaoCancelar.setForeground(tema.getBotaoTexto());
        botaoCancelar.addActionListener(e -> {
            resultado[0] = null;
            dialog.dispose();
        });

        JButton botaoConfirmar = new JButton("Confirmar");
        botaoConfirmar.setFocusPainted(false);
        botaoConfirmar.setOpaque(true);
        botaoConfirmar.setBackground(tema.getBotaoFundo());
        botaoConfirmar.setForeground(tema.getBotaoTexto());
        botaoConfirmar.addActionListener(e -> {
            resultado[0] = campoTexto.getText();
            dialog.dispose();
        });

        campoTexto.addActionListener(e -> {
            resultado[0] = campoTexto.getText();
            dialog.dispose();
        });

        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        painelAcoes.setBackground(tema.getPainelFundo());
        painelAcoes.add(botaoCancelar);
        painelAcoes.add(botaoConfirmar);

        painelRaiz.add(painelConteudo, BorderLayout.CENTER);
        painelRaiz.add(painelAcoes, BorderLayout.SOUTH);

        dialog.setContentPane(painelRaiz);
        dialog.setSize(460, 180);
        dialog.setMinimumSize(dialog.getSize());
        dialog.setLocationRelativeTo(parent);
        dialog.getRootPane().setDefaultButton(botaoConfirmar);

        SwingUtilities.invokeLater(campoTexto::requestFocusInWindow);
        dialog.setVisible(true);

        return resultado[0];
    }

    private static JDialog criarDialog(Component parent, String titulo) {
        Window owner = parent != null ? SwingUtilities.getWindowAncestor(parent) : null;

        if (owner instanceof Frame frame) {
            return new JDialog(frame, titulo, true);
        }
        if (owner instanceof Dialog dialogOwner) {
            return new JDialog(dialogOwner, titulo, true);
        }
        return new JDialog((Frame) null, titulo, true);
    }
}
