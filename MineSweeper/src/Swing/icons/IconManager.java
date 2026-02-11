package Swing.icons;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

/**
 * Gerencia os icones utilizados pela interface grafica do jogo.
 */
public class IconManager {

    private static final int TAMANHO_ICONE_CELULA = 24;
    private static final double ESCALA_ICONE_NUMERO = 1.0;

    private final ImageIcon iconeBomba;
    private final ImageIcon iconeBandeira;
    private final ImageIcon iconeCelulaFechada;
    private final ImageIcon iconeCelulaAberta;
    private final ImageIcon iconeExplosao;
    private final Map<Integer, ImageIcon> iconesNumero;

    /**
     * Carrega icones de recursos e cria fallbacks quando necessario.
     */
    public IconManager() {
        ImageIcon iconeBombaTemp = carregarIcone("/images/Bomba.png", TAMANHO_ICONE_CELULA);
        ImageIcon iconeBandeiraTemp = carregarIcone("/images/Bandeira.png", TAMANHO_ICONE_CELULA);
        ImageIcon iconeCelulaFechadaTemp = carregarIcone("/images/CelulaFechada.png", TAMANHO_ICONE_CELULA);
        ImageIcon iconeCelulaAbertaTemp = carregarIcone("/images/CelulaAberta.png", TAMANHO_ICONE_CELULA);
        ImageIcon iconeExplosaoTemp = carregarIcone("/images/Explosao.png", TAMANHO_ICONE_CELULA);

        iconeBomba = (iconeBombaTemp != null) ? iconeBombaTemp : criarIconeBomba(TAMANHO_ICONE_CELULA);
        iconeBandeira = (iconeBandeiraTemp != null) ? iconeBandeiraTemp : criarIconeBandeira(TAMANHO_ICONE_CELULA);
        iconeCelulaFechada = (iconeCelulaFechadaTemp != null) ? iconeCelulaFechadaTemp
                : criarIconeCelulaFechada(TAMANHO_ICONE_CELULA);
        iconeCelulaAberta = (iconeCelulaAbertaTemp != null) ? iconeCelulaAbertaTemp
                : criarIconeCelulaAberta(TAMANHO_ICONE_CELULA);
        iconeExplosao = (iconeExplosaoTemp != null) ? iconeExplosaoTemp : criarIconeExplosao(TAMANHO_ICONE_CELULA);

        iconesNumero = new HashMap<>();
        int tamanhoIconeNumero = (int) Math.round(TAMANHO_ICONE_CELULA * ESCALA_ICONE_NUMERO);
        for (int n = 1; n <= 8; n++) {
            ImageIcon icone = carregarIcone("/images/Numero" + n + ".png", tamanhoIconeNumero);
            if (icone == null) {
                icone = criarIconeNumero(n, TAMANHO_ICONE_CELULA);
            }
            iconesNumero.put(n, icone);
        }
    }

    /**
     * Retorna o icone de bomba.
     *
     * @return icone de bomba
     */
    public ImageIcon getBombaIcon() {
        return iconeBomba;
    }

    /**
     * Retorna o icone de bandeira.
     *
     * @return icone de bandeira
     */
    public ImageIcon getBandeiraIcon() {
        return iconeBandeira;
    }

    /**
     * Retorna o icone de celula fechada.
     *
     * @return icone de celula fechada
     */
    public ImageIcon getCelulaFechadaIcon() {
        return iconeCelulaFechada;
    }

    /**
     * Retorna o icone de celula aberta.
     *
     * @return icone de celula aberta
     */
    public ImageIcon getCelulaAbertaIcon() {
        return iconeCelulaAberta;
    }

    /**
     * Retorna o icone de explosao.
     *
     * @return icone de explosao
     */
    public ImageIcon getExplosaoIcon() {
        return iconeExplosao;
    }

    /**
     * Retorna o icone correspondente ao numero de minas vizinhas.
     *
     * @param numero numero entre 1 e 8
     * @return icone numerico
     */
    public ImageIcon getNumeroIcon(int numero) {
        return iconesNumero.get(numero);
    }

    /**
     * Carrega um icone de janela a partir do classpath.
     *
     * @param caminhoRecurso caminho do recurso
     * @return imagem carregada ou null se nao encontrada
     */
    public Image carregarIconeJanela(String caminhoRecurso) {
        URL url = getClass().getResource(caminhoRecurso);
        if (url == null) {
            return null;
        }
        return new ImageIcon(url).getImage();
    }

    /**
     * Carrega e redimensiona um icone a partir do classpath.
     *
     * @param caminhoRecurso caminho do recurso
     * @param tamanho        tamanho do icone
     * @return icone redimensionado ou null se nao encontrado
     */
    private ImageIcon carregarIcone(String caminhoRecurso, int tamanho) {
        URL url = getClass().getResource(caminhoRecurso);
        if (url == null) {
            return null;
        }
        ImageIcon base = new ImageIcon(url);
        Image imagemRedimensionada = base.getImage().getScaledInstance(tamanho, tamanho, Image.SCALE_SMOOTH);
        return new ImageIcon(imagemRedimensionada);
    }

    /**
     * Cria um icone simples de celula fechada.
     *
     * @param tamanho tamanho do icone
     * @return icone gerado
     */
    private ImageIcon criarIconeCelulaFechada(int tamanho) {
        BufferedImage imagem = new BufferedImage(tamanho, tamanho, BufferedImage.TYPE_INT_ARGB);
        Graphics2D grafico = imagem.createGraphics();
        grafico.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        grafico.setColor(new Color(200, 200, 200));
        grafico.fillRect(0, 0, tamanho, tamanho);
        grafico.setColor(new Color(240, 240, 240));
        grafico.drawLine(1, 1, tamanho - 2, 1);
        grafico.drawLine(1, 1, 1, tamanho - 2);
        grafico.setColor(new Color(150, 150, 150));
        grafico.drawLine(1, tamanho - 2, tamanho - 2, tamanho - 2);
        grafico.drawLine(tamanho - 2, 1, tamanho - 2, tamanho - 2);
        grafico.dispose();
        return new ImageIcon(imagem);
    }

    /**
     * Cria um icone simples de celula aberta.
     *
     * @param tamanho tamanho do icone
     * @return icone gerado
     */
    private ImageIcon criarIconeCelulaAberta(int tamanho) {
        BufferedImage imagem = new BufferedImage(tamanho, tamanho, BufferedImage.TYPE_INT_ARGB);
        Graphics2D grafico = imagem.createGraphics();
        grafico.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        grafico.setColor(new Color(225, 225, 225));
        grafico.fillRect(0, 0, tamanho, tamanho);
        grafico.setColor(new Color(210, 210, 210));
        grafico.drawRect(0, 0, tamanho - 1, tamanho - 1);
        grafico.dispose();
        return new ImageIcon(imagem);
    }

    /**
     * Cria um icone numerico para uma celula com minas vizinhas.
     *
     * @param numero  numero exibido
     * @param tamanho tamanho do icone
     * @return icone gerado
     */
    private ImageIcon criarIconeNumero(int numero, int tamanho) {
        BufferedImage imagem = new BufferedImage(tamanho, tamanho, BufferedImage.TYPE_INT_ARGB);
        Graphics2D grafico = imagem.createGraphics();
        grafico.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        grafico.setColor(new Color(225, 225, 225));
        grafico.fillRect(0, 0, tamanho, tamanho);
        grafico.setColor(new Color(210, 210, 210));
        grafico.drawRect(0, 0, tamanho - 1, tamanho - 1);

        Color corNumero;
        switch (numero) {
            case 1:
                corNumero = new Color(25, 118, 210);
                break;
            case 2:
                corNumero = new Color(56, 142, 60);
                break;
            case 3:
                corNumero = new Color(211, 47, 47);
                break;
            case 4:
                corNumero = new Color(74, 20, 140);
                break;
            case 5:
                corNumero = new Color(109, 76, 65);
                break;
            case 6:
                corNumero = new Color(0, 121, 107);
                break;
            case 7:
                corNumero = new Color(97, 97, 97);
                break;
            default:
                corNumero = new Color(33, 33, 33);
                break;
        }
        grafico.setColor(corNumero);
        grafico.setFont(new Font("Arial", Font.BOLD, Math.max(16, tamanho - 4)));
        String texto = String.valueOf(numero);
        FontMetrics metricasFonte = grafico.getFontMetrics();
        int posX = (tamanho - metricasFonte.stringWidth(texto)) / 2;
        int posY = (tamanho - metricasFonte.getHeight()) / 2 + metricasFonte.getAscent();
        grafico.drawString(texto, posX, posY);
        grafico.dispose();
        return new ImageIcon(imagem);
    }

    /**
     * Cria um icone simples de bandeira.
     *
     * @param tamanho tamanho do icone
     * @return icone gerado
     */
    private ImageIcon criarIconeBandeira(int tamanho) {
        BufferedImage imagem = new BufferedImage(tamanho, tamanho, BufferedImage.TYPE_INT_ARGB);
        Graphics2D grafico = imagem.createGraphics();
        grafico.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        grafico.setColor(new Color(225, 225, 225));
        grafico.fillRect(0, 0, tamanho, tamanho);
        grafico.setColor(new Color(100, 100, 100));
        grafico.fillRect(tamanho / 2 - 1, 4, 2, tamanho - 6);
        grafico.setColor(new Color(220, 0, 0));
        int[] pontosX = { tamanho / 2, tamanho / 2, tamanho - 4 };
        int[] pontosY = { 4, tamanho / 2, tamanho / 4 };
        grafico.fillPolygon(pontosX, pontosY, 3);
        grafico.setColor(new Color(80, 80, 80));
        grafico.fillRect(tamanho / 2 - 4, tamanho - 4, 8, 3);
        grafico.dispose();
        return new ImageIcon(imagem);
    }

    /**
     * Cria um icone simples de bomba.
     *
     * @param tamanho tamanho do icone
     * @return icone gerado
     */
    private ImageIcon criarIconeBomba(int tamanho) {
        BufferedImage imagem = new BufferedImage(tamanho, tamanho, BufferedImage.TYPE_INT_ARGB);
        Graphics2D grafico = imagem.createGraphics();
        grafico.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        grafico.setColor(new Color(225, 225, 225));
        grafico.fillRect(0, 0, tamanho, tamanho);
        grafico.setColor(new Color(30, 30, 30));
        grafico.fillOval(4, 6, tamanho - 8, tamanho - 10);
        grafico.setColor(new Color(60, 60, 60));
        grafico.fillOval(tamanho / 2 - 4, 2, 8, 6);
        grafico.setColor(new Color(255, 193, 7));
        grafico.fillOval(tamanho - 6, 2, 4, 4);
        grafico.dispose();
        return new ImageIcon(imagem);
    }

    /**
     * Cria um icone simples de explosao.
     *
     * @param tamanho tamanho do icone
     * @return icone gerado
     */
    private ImageIcon criarIconeExplosao(int tamanho) {
        BufferedImage imagem = new BufferedImage(tamanho, tamanho, BufferedImage.TYPE_INT_ARGB);
        Graphics2D grafico = imagem.createGraphics();
        grafico.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        grafico.setColor(new Color(225, 225, 225));
        grafico.fillRect(0, 0, tamanho, tamanho);
        grafico.setColor(new Color(255, 152, 0));
        int[] pontosX = { tamanho / 2, tamanho - 4, tamanho / 2 + 3, tamanho - 2, tamanho / 2, 2, tamanho / 2 - 3,
                4 };
        int[] pontosY = { 2, tamanho / 2 - 3, tamanho / 2, tamanho - 4, tamanho / 2 + 3, tamanho - 2, tamanho / 2,
                tamanho / 2 - 3 };
        grafico.fillPolygon(pontosX, pontosY, pontosX.length);
        grafico.setColor(new Color(211, 47, 47));
        grafico.fillOval(tamanho / 2 - 4, tamanho / 2 - 4, 8, 8);
        grafico.dispose();
        return new ImageIcon(imagem);
    }
}
