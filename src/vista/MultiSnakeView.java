package vista;
import java.awt.*;
import java.awt.image.BufferedImage;

import controlador.MultiSnakeController;
import modelo.MultiSnakeModel;

public class MultiSnakeView extends SnakeView {
    private final MultiSnakeModel multiModel;
    private Image head2Img, body2Img;

    public MultiSnakeView(MultiSnakeModel model, boolean daltonic, int skin1, int skin2) {
        super(model, daltonic);
        this.multiModel = model;
        try {
            BufferedImage spriteSheet = javax.imageio.ImageIO.read(new java.io.File("img/snake.png"));
            int[] yOffsets = {0, 8, 24, 32};
            int y = yOffsets[skin2 % yOffsets.length];
            head2Img = spriteSheet.getSubimage(16, y, 8, 8).getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            body2Img = spriteSheet.getSubimage(40, y, 8, 8).getScaledInstance(48, 48, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            head2Img = null;
            body2Img = null;
        }
    }

    @Override
    public void draw(Graphics g) {
        
        super.draw(g);

       
        if (gameState == GameState.RUNNING) {
            for (int i = 0; i < multiModel.getBodyParts2(); i++) {
                int drawX = multiModel.getX2()[i] * TILE_SIZE;
                int drawY = multiModel.getY2()[i] * TILE_SIZE;
                if (i == 0 && head2Img != null) {
                    g.drawImage(head2Img, drawX, drawY, TILE_SIZE, TILE_SIZE, this);
                } else if (body2Img != null) {
                    g.drawImage(body2Img, drawX, drawY, TILE_SIZE, TILE_SIZE, this);
                }
            }
        }
    }

    @Override
    protected void drawScore(Graphics g) {
    }

    @Override
    protected void showGameOverScreen(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        int gameOverY = getHeight() / 2 - 40;
        g.drawString("Game Over", (getWidth() - metrics.stringWidth("Game Over")) / 2, gameOverY);

        String winnerText = "";
        if (controller instanceof MultiSnakeController multiCtrl) {
            winnerText = multiCtrl.getWinner() + " gana!";
        }
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        FontMetrics winnerMetrics = getFontMetrics(g.getFont());
        int winnerY = gameOverY + 60;
        g.drawString(winnerText, (getWidth() - winnerMetrics.stringWidth(winnerText)) / 2, winnerY);

        int buttonWidth = 180, buttonHeight = 40, spacing = 20;
        int centerX = getWidth() / 2 - buttonWidth / 2;
        int centerY = winnerY + 100;
        resumeButton.setText("Revancha");
        pauseMenuButton.setText("Menú Principal");
        resumeButton.setBounds(centerX, centerY, buttonWidth, buttonHeight);
        pauseMenuButton.setBounds(centerX, centerY + buttonHeight + spacing, buttonWidth, buttonHeight);
        resumeButton.setVisible(true);
        pauseMenuButton.setVisible(true);
        resumeButton.requestFocusInWindow();
    }
}