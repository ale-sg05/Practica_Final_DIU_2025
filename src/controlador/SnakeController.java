package controlador;
import javax.swing.*;

import modelo.GameConfig;
import modelo.InfiniteSnakeModel;
import modelo.RankingManager;
import modelo.SnakeModel;
import modelo.TimerSnakeModel;
import modelo.RankingManager.Mode;
import util.AudioManager;
import vista.ConfigPanel;
import vista.SnakeView;
import vista.SnakeView.GameState;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SnakeController extends AbstractSnakeController {
    public SnakeController(SnakeModel model, SnakeView view, int speed) {
        super(model, view, speed);
        loadSkinImages(GameConfig.getSkinIndex());
        timer.stop();
        loadKeyboardImage();
    }

    private void loadSkinImages(int skinIndex) {
        try {
            String spritePath = switch (GameConfig.getDaltonicMode()) {
                case PROTANOPIA -> "img/snake_prot.png";
                case DEUTERANOPIA -> "img/snake_deut.png";
                case TRITANOPIA -> "img/snake_trit.png";
                default -> "img/snake.png";
            };
            BufferedImage spriteSheet = ImageIO.read(new File(spritePath));
            int[] yOffsets = {0, 8, 24, 32};
            int y = yOffsets[skinIndex];

            view.headUpImg    = scaleSprite(spriteSheet.getSubimage(8,  y, 8, 8));
            view.headLeftImg  = scaleSprite(spriteSheet.getSubimage(16, y, 8, 8));
            view.headDownImg  = scaleSprite(spriteSheet.getSubimage(24, y, 8, 8));
            view.headRightImg = scaleSprite(spriteSheet.getSubimage(32, y, 8, 8));
            view.bodyImg      = scaleSprite(spriteSheet.getSubimage(40, y, 8, 8));
            view.appleImg     = scaleSprite(spriteSheet.getSubimage(48, 24, 8, 8));
            view.specialApples[0] = scaleSprite(spriteSheet.getSubimage(48, 0, 8, 8));
            view.specialApples[1] = scaleSprite(spriteSheet.getSubimage(48, 8, 8, 8));
            view.specialApples[2] = scaleSprite(spriteSheet.getSubimage(48, 32, 8, 8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage scaleSprite(BufferedImage sprite) {
        int tileSize = model.getTileSize();
        Image tmp = sprite.getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH);
        BufferedImage scaled = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaled.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return scaled;
    }

    private void loadKeyboardImage() {
        try {
            Image img = new ImageIcon("img/keyboard-arrows.png").getImage();
            view.setKeyboardKeysImg(img);
        } catch (Exception e) {
            view.setKeyboardKeysImg(null);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isPaused()) return;
        if (view.gameState == SnakeView.GameState.RUNNING) {
            model.move();
            boolean ate = model.checkApple();
            if (ate) AudioManager.playEatSound();
            model.updateSpecialApple1();

            if (model.getApplesEaten() < 0) {
                endGame();
                return;
            }
            if (model.checkCollisions()) {
                endGame();
                return;
            }
            if (model instanceof TimerSnakeModel timerModel) {
                timerModel.tickTimer();
                if (timerModel.getTimeLeft() <= 0) {
                    endGame();
                    return;
                }
            }
            directionChanged = false;
        }
        view.repaint();
    }

    private void endGame() {
        timer.stop();
        view.gameState = SnakeView.GameState.GAME_OVER;
        AudioManager.stopGameMusic();
        AudioManager.playGameOverSound();
        RankingManager.Mode rankingMode = RankingManager.Mode.CLASSIC;
        if (model instanceof InfiniteSnakeModel) {
            rankingMode = RankingManager.Mode.INFINITE;
        } else if (model instanceof TimerSnakeModel) {
            rankingMode = RankingManager.Mode.TIMER;
        }
        RankingManager.addScore(rankingMode, model.getApplesEaten());
        view.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        super.keyPressed(e);
        if (key == KeyEvent.VK_SPACE) return;

        if (directionChanged) return;
        if (view.gameState != SnakeView.GameState.RUNNING) return;

        switch (key) {
            case KeyEvent.VK_LEFT -> { if (model.getDirection() != 'R') { model.setDirection('L'); directionChanged = true; } }
            case KeyEvent.VK_RIGHT -> { if (model.getDirection() != 'L') { model.setDirection('R'); directionChanged = true; } }
            case KeyEvent.VK_UP -> { if (model.getDirection() != 'D') { model.setDirection('U'); directionChanged = true; } }
            case KeyEvent.VK_DOWN -> { if (model.getDirection() != 'U') { model.setDirection('D'); directionChanged = true; } }
        }
    }

    public void applyConfig(ConfigPanel configPanel) {
        int speedIndex = configPanel.getSelectedSpeedIndex();
        switch (speedIndex) {
            case 0 -> delay = 200;
            case 1 -> delay = 100;
            case 2 -> delay = 50;
        }
        int skinIndex = configPanel.getCurrentSkinIndex();
        loadSkinImages(skinIndex);
        view.repaint();
    }

    public void restartGame() {
        model.reset();
        view.gameState = SnakeView.GameState.RUNNING;
        view.setPaused(false);
        directionChanged = false;
        waitingFirstMove = true;
        timer.restart();
        view.repaint();
        view.requestFocusInWindow();
    }
}