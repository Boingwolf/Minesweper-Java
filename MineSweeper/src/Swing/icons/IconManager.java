package Swing.icons;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

public class IconManager {

    private static final int CELL_ICON_SIZE = 24;
    private static final double NUMERO_ICON_SCALE = 1.0;

    private final ImageIcon bombaIcon;
    private final ImageIcon bandeiraIcon;
    private final ImageIcon celulaFechadaIcon;
    private final ImageIcon celulaAbertaIcon;
    private final ImageIcon explosaoIcon;
    private final Map<Integer, ImageIcon> numeroIcons;

    public IconManager() {
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
    }

    public ImageIcon getBombaIcon() {
        return bombaIcon;
    }

    public ImageIcon getBandeiraIcon() {
        return bandeiraIcon;
    }

    public ImageIcon getCelulaFechadaIcon() {
        return celulaFechadaIcon;
    }

    public ImageIcon getCelulaAbertaIcon() {
        return celulaAbertaIcon;
    }

    public ImageIcon getExplosaoIcon() {
        return explosaoIcon;
    }

    public ImageIcon getNumeroIcon(int numero) {
        return numeroIcons.get(numero);
    }

    public Image carregarIconeJanela(String resourcePath) {
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
}
