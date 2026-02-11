package Swing.utils;

import java.awt.Color;

/**
 * Define as cores usadas pelos temas da interface.
 */
public enum Tema {
    CLARO(
            new Color(245, 245, 245),
            new Color(232, 232, 232),
            new Color(235, 235, 235),
            new Color(228, 228, 228),
            new Color(250, 250, 250),
            new Color(30, 30, 30),
            new Color(245, 245, 245),
            new Color(30, 30, 30),
            new Color(240, 240, 240),
            new Color(30, 30, 30)),
    ESCURO(
            new Color(40, 40, 45),
            new Color(25, 25, 28),
            new Color(45, 45, 50),
            new Color(38, 38, 42),
            new Color(35, 35, 38),
            new Color(230, 230, 230),
            new Color(35, 35, 38),
            new Color(230, 230, 230),
            new Color(50, 50, 56),
            new Color(230, 230, 230));

    private final Color fundoGradienteTopo;
    private final Color fundoGradienteBase;
    private final Color gridBase;
    private final Color gridAlternado;
    private final Color painelFundo;
    private final Color textoPadrao;
    private final Color menuFundo;
    private final Color menuTexto;
    private final Color botaoFundo;
    private final Color botaoTexto;

    Tema(Color fundoGradienteTopo, Color fundoGradienteBase, Color gridBase, Color gridAlternado,
            Color painelFundo, Color textoPadrao, Color menuFundo, Color menuTexto, Color botaoFundo,
            Color botaoTexto) {
        this.fundoGradienteTopo = fundoGradienteTopo;
        this.fundoGradienteBase = fundoGradienteBase;
        this.gridBase = gridBase;
        this.gridAlternado = gridAlternado;
        this.painelFundo = painelFundo;
        this.textoPadrao = textoPadrao;
        this.menuFundo = menuFundo;
        this.menuTexto = menuTexto;
        this.botaoFundo = botaoFundo;
        this.botaoTexto = botaoTexto;
    }

    public Color getFundoGradienteTopo() {
        return fundoGradienteTopo;
    }

    public Color getFundoGradienteBase() {
        return fundoGradienteBase;
    }

    public Color getGridBase() {
        return gridBase;
    }

    public Color getGridAlternado() {
        return gridAlternado;
    }

    public Color getPainelFundo() {
        return painelFundo;
    }

    public Color getTextoPadrao() {
        return textoPadrao;
    }

    public Color getMenuFundo() {
        return menuFundo;
    }

    public Color getMenuTexto() {
        return menuTexto;
    }

    public Color getBotaoFundo() {
        return botaoFundo;
    }

    public Color getBotaoTexto() {
        return botaoTexto;
    }
}
