package vista;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class HelpOverlayPanel extends JLayeredPane {
    private int slide = 0;
    private final int totalSlides = 3;
    private final JComponent parent;
    private Rectangle closeRect = new Rectangle();
    private Rectangle leftArrowRect = new Rectangle();
    private Rectangle rightArrowRect = new Rectangle();

    public HelpOverlayPanel(JComponent parent) {
        this.parent = parent;
        setOpaque(false);
        setLayout(null);
        setBounds(0, 0, parent.getWidth(), parent.getHeight());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();

                // Cerrar si clic en la 'X'
                if (closeRect.contains(p)) {
                    if (parent instanceof MainMenuPanel menu) {
                        menu.removeHelpOverlay();
                    } else if (parent instanceof SnakeView view) {
                        view.removeHelpOverlay();
                        if (view.getController() != null) view.getController().resumeAfterHelp();
                    }
                    return;
                }
                // Flecha izquierda
                if (leftArrowRect.contains(p) && slide > 0) {
                    slide--;
                    repaint();
                    return;
                }
                // Flecha derecha
                if (rightArrowRect.contains(p)) {
                    if (slide < totalSlides - 1) {
                        slide++;
                        repaint();
                    } else {
                        if (parent instanceof MainMenuPanel menu) {
                            menu.removeHelpOverlay();
                        } else if (parent instanceof SnakeView view) {
                            view.removeHelpOverlay();
                            if (view.getController() != null) view.getController().resumeAfterHelp();
                        }
                    }
                    return;
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(new Color(80, 80, 80, 180));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        int cardW = 500, cardH = 340;
        int cardX = (getWidth() - cardW) / 2;
        int cardY = (getHeight() - cardH) / 2;
        g2d.setColor(new Color(245, 235, 220));
        g2d.fillRoundRect(cardX, cardY, cardW, cardH, 30, 30);

        // 'X' para cerrar
        int xSize = 32;
        int xPad = 16;
        int xX = cardX + cardW - xSize - xPad;
        int xY = cardY + xPad;
        closeRect.setBounds(xX, xY, xSize, xSize);
        g2d.setColor(new Color(220, 100, 100));
        g2d.fillOval(xX, xY, xSize, xSize);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        int offset = 8;
        g2d.drawLine(xX + offset, xY + offset, xX + xSize - offset, xY + xSize - offset);
        g2d.drawLine(xX + xSize - offset, xY + offset, xX + offset, xY + xSize - offset);

        // Título y texto (igual que tienes)
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 28));
        Font smallFont = new Font("Arial", Font.PLAIN, 18);
        int y = cardY + 60;
        int textX = cardX + 40;

        if (slide == 0) {
            g2d.drawString("Controles y objetivo:", textX, y);
            y += 40;
            g2d.setFont(smallFont);
            g2d.drawString("• Mueve la serpiente con las flechas del teclado.", textX, y); y += 28;
            g2d.drawString("• Come manzanas para crecer y sumar puntos.", textX, y); y += 28;
            g2d.drawString("• Pierdes si chocas con tu cuerpo, los bordes,", textX, y); y += 28;
            g2d.drawString("  o si el score baja de 0.", textX, y);
        } else if (slide == 1) {
            g2d.drawString("Manzanas especiales:", textX, y);
            y += 40;
            g2d.setFont(smallFont);
            g2d.drawString("• Manzana roja: +2 puntos y +2 longitud.", textX, y); y += 28;
            g2d.drawString("• Manzana venenosa: -2 puntos y -2 longitud.", textX, y); y += 28;
            g2d.drawString("• Manzana dora: +5 puntos y +5 longitud,", textX, y); y += 28;
            g2d.drawString("  aparece solo 5 segundos.", textX, y); y += 28;
            g2d.drawString("• Cada una tiene su probabilidad y efecto.", textX, y);
        } else if (slide == 2) {
            g2d.drawString("Modos de juego:", textX, y);
            y += 40;
            g2d.setFont(smallFont);
            g2d.drawString("• Infinito: los bordes están conectados.", textX, y); y += 28;
            g2d.drawString("• Contrarreloj: come la manzana antes de que", textX, y); y += 28;
            g2d.drawString("  acabe el tiempo para sumar segundos.", textX, y); y += 28;
            g2d.drawString("• Multijugador local: dos serpientes compiten,", textX, y); y += 28;
            g2d.drawString("  gana quien sobreviva más tiempo.", textX, y);
        }

        // Flechas de navegación
        int arrowY = cardY + cardH - 40;
        int arrowSize = 30;
        if (slide > 0) {
            int lx = cardX + 20, ly = arrowY;
            int[] xPoints = {lx, lx + arrowSize, lx + arrowSize};
            int[] yPoints = {ly + arrowSize / 2, ly, ly + arrowSize};
            g2d.setColor(new Color(180, 180, 180));
            g2d.fillPolygon(xPoints, yPoints, 3);
            leftArrowRect.setBounds(lx, ly, arrowSize, arrowSize);
        } else {
            leftArrowRect.setBounds(0, 0, 0, 0);
        }
        if (slide < totalSlides - 1) {
            int rx = cardX + cardW - 20 - arrowSize, ry = arrowY;
            int[] xPoints = {rx + arrowSize, rx, rx};
            int[] yPoints = {ry + arrowSize / 2, ry, ry + arrowSize};
            g2d.setColor(new Color(180, 180, 180));
            g2d.fillPolygon(xPoints, yPoints, 3);
            rightArrowRect.setBounds(rx, ry, arrowSize, arrowSize);
        } else {
            rightArrowRect.setBounds(0, 0, 0, 0);
        }

        g2d.dispose();
    }
}