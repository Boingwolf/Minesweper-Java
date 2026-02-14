package Swing.utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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
