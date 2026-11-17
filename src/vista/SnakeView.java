package vista;
import javax.swing.*;

import controlador.SnakeController;
import modelo.InfiniteSnakeModel;
import modelo.RankingEntry;
import modelo.RankingManager;
import modelo.SnakeModel;
import modelo.TimerSnakeModel;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class SnakeView extends JPanel {
    protected SnakeModel model;
    protected SnakeController controller;
    public Image appleImg, headUpImg, headDownImg, headLeftImg, headRightImg, bodyImg;
    public Image[] specialApples = new Image[3];
    private Image backgroundImg;
    public int WIDTH, HEIGHT, TILE_SIZE;
    public JButton tryAgainButton, exitButton, resumeButton, pauseMenuButton;
    private JButton iconButton;
    private ImageIcon iconImage;
    private Image keyboardKeysImg;
    private HelpOverlayPanel helpOverlay;
    public GameState gameState = GameState.RUNNING;
    private boolean paused = false;
    private final int MARGIN_LEFT = 50, MARGIN_RIGHT = 50, MARGIN_BOTTOM = 60, MARGIN_TOP = 60;

    public enum GameState { RUNNING, GAME_OVER }

    public SnakeView(SnakeModel model, boolean daltonic) {
        this.model = model;
        this.WIDTH = model.getWidth();
        this.HEIGHT = model.getHeight();
        this.TILE_SIZE = model.getTileSize();
        setPreferredSize(new Dimension(WIDTH + MARGIN_LEFT + MARGIN_RIGHT, HEIGHT + MARGIN_TOP + MARGIN_BOTTOM));
        setBackground(Color.DARK_GRAY);
        setFocusable(true);
        setLayout(null);

        // Fondo
        try { backgroundImg = ImageIO.read(new File("img/marco.png")); } catch (IOException e) { backgroundImg = null; }

        // Botón pausa
        try { iconImage = new ImageIcon("img/pausa.png"); } catch (Exception e) { iconImage = null; }
        iconButton = new JButton();
        if (iconImage != null) {
            int iconSize = 28;
            Image img = iconImage.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
            iconButton.setIcon(new ImageIcon(img));
        } else iconButton.setText("Pausa");
        iconButton.setContentAreaFilled(false);
        iconButton.setBorderPainted(false);
        iconButton.setFocusPainted(false);
        iconButton.setOpaque(false);
        add(iconButton);
        iconButton.addActionListener(e -> {
            if (controller != null) controller.togglePause();
            requestFocusInWindow(); // <-- Esto asegura que las flechas sigan funcionando
        });

        // Botones game over y pausa
        tryAgainButton = new JButton("Reintentar");
        exitButton = new JButton("Menú Principal");
        resumeButton = new JButton("Reanudar");
        pauseMenuButton = new JButton("Menú Principal");

        for (JButton btn : new JButton[]{tryAgainButton, exitButton, resumeButton, pauseMenuButton}) {
            add(btn);
            btn.setVisible(false);
        }

        tryAgainButton.addActionListener(e -> {
            if (controller != null) {
                controller.restartGame();
                
                tryAgainButton.setVisible(false);
                exitButton.setVisible(false);
            }
        });
        exitButton.addActionListener(e -> {
            if (onReturnToMenu != null) onReturnToMenu.run();
        });
        resumeButton.addActionListener(e -> {
            if (controller != null) {
                controller.restartGame();
               
                resumeButton.setVisible(false);
                pauseMenuButton.setVisible(false);
                tryAgainButton.setVisible(false);
                exitButton.setVisible(false);
            }
            this.requestFocusInWindow();
        });
        pauseMenuButton.addActionListener(e -> {
            if (onReturnToMenu != null) onReturnToMenu.run();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImg != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            double scale = 1.15;
            int imgWidth = (int) (getWidth() * scale);
            int imgHeight = (int) (getHeight() * scale);
            int x = (getWidth() - imgWidth) / 2 - 3;
            int y = (getHeight() - imgHeight) / 2;
            g2.drawImage(backgroundImg, x, y, imgWidth, imgHeight, this);
        }
        g.setColor(Color.BLACK);
        g.fillRect(MARGIN_LEFT, MARGIN_TOP, WIDTH, HEIGHT);

        drawScore(g);

        // Posiciona los botones arriba derecha
        int buttonWidth = 36, buttonHeight = 36, spacing = 10;
        int pauseButtonX = getWidth() - buttonWidth - 116, buttonY = 24;
        iconButton.setBounds(pauseButtonX, buttonY, buttonWidth, buttonHeight);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate(MARGIN_LEFT, MARGIN_TOP);
        draw(g2d);
        g2d.dispose();

        if (gameState == GameState.RUNNING && isPaused()) {
            Graphics2D g2dOverlay = (Graphics2D) g.create();
            g2dOverlay.setColor(new Color(80, 80, 80, 120));
            g2dOverlay.fillRect(0, 0, getWidth(), getHeight());
            g2dOverlay.setFont(new Font("Arial", Font.BOLD, 58));
            g2dOverlay.setColor(new Color(0xE0C13B));
            g2dOverlay.drawString("PAUSA", getWidth() / 2 - 90, getHeight() / 2 - 40);
            g2dOverlay.dispose();
            positionPauseButtons();
        } else if (gameState == GameState.GAME_OVER) {
            showGameOverScreen(g);
        } else {
            resumeButton.setVisible(false);
            pauseMenuButton.setVisible(false);
        }
    }

    public void draw(Graphics g) {
        if (gameState == GameState.RUNNING) {
            // Manzanas
            if (model.getApple() != null)
                g.drawImage(appleImg, model.getApple().x * TILE_SIZE, model.getApple().y * TILE_SIZE, TILE_SIZE, TILE_SIZE, this);
            if (model.getSpecialApple0() != null)
                g.drawImage(specialApples[0], model.getSpecialApple0().x * TILE_SIZE, model.getSpecialApple0().y * TILE_SIZE, TILE_SIZE, TILE_SIZE, this);
            if (model.getSpecialApple1() != null)
                g.drawImage(specialApples[1], model.getSpecialApple1().x * TILE_SIZE, model.getSpecialApple1().y * TILE_SIZE, TILE_SIZE, TILE_SIZE, this);
            if (model.getSpecialApple2() != null)
                g.drawImage(specialApples[2], model.getSpecialApple2().x * TILE_SIZE, model.getSpecialApple2().y * TILE_SIZE, TILE_SIZE, TILE_SIZE, this);

            // Serpiente
            for (int i = 0; i < model.getBodyParts(); i++) {
                int drawX = model.getX()[i] * TILE_SIZE;
                int drawY = model.getY()[i] * TILE_SIZE;
                if (i == 0) {
                    Image headImg = switch (model.getDirection()) {
                        case 'U' -> headUpImg;
                        case 'D' -> headDownImg;
                        case 'L' -> headLeftImg;
                        case 'R' -> headRightImg;
                        default -> headRightImg;
                    };
                    g.drawImage(headImg, drawX, drawY, TILE_SIZE, TILE_SIZE, this);
                } else {
                    g.drawImage(bodyImg, drawX, drawY, TILE_SIZE, TILE_SIZE, this);
                }
            }
        }
    }

    protected void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        int scoreX = 120, scoreY = 50;
        g.drawString("Score: " + Math.max(0, model.getApplesEaten()), scoreX, scoreY);

        // --- NUEVO: Best Score ---
        // Obtén el mejor score del ranking
        int bestScore = 0;
        RankingManager.Mode mode = RankingManager.Mode.CLASSIC;
        if (model instanceof InfiniteSnakeModel) {
            mode = RankingManager.Mode.INFINITE;
        } else if (model instanceof TimerSnakeModel) {
            mode = RankingManager.Mode.TIMER;
        }
        java.util.List<RankingEntry> ranking = RankingManager.getRanking(mode);
        if (!ranking.isEmpty()) {
            bestScore = ranking.get(0).score;
        }

        // Dibuja el texto "Best Score: x" en dorado, alineado verticalmente con el score
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.setColor(new Color(0xE0C13B));
        String bestScoreText = "Best Score: " + bestScore;

        // Calcula la posición: entre el score y el botón de pausa
        int pauseButtonX = getWidth() - 36 - 116;
        int bestScoreWidth = g.getFontMetrics().stringWidth(bestScoreText);
        int bestScoreX = scoreX + (pauseButtonX - scoreX - bestScoreWidth) / 2;
        g.drawString(bestScoreText, bestScoreX, scoreY);
    }

    protected void showGameOverScreen(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        int gameOverY = getHeight() / 2 - 40;
        g.drawString("Game Over", (getWidth() - metrics.stringWidth("Game Over")) / 2, gameOverY);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        FontMetrics scoreMetrics = getFontMetrics(g.getFont());
        String scoreText = "Score: " + Math.max(0, model.getApplesEaten());
        int scoreY = gameOverY + 60;
        g.drawString(scoreText, (getWidth() - scoreMetrics.stringWidth(scoreText)) / 2, scoreY);

        // Posiciona los botones centrados respecto al panel
        int buttonWidth = 180, buttonHeight = 40, spacing = 20;
        int centerX = getWidth() / 2 - buttonWidth / 2;
        int centerY = scoreY + 60;
        tryAgainButton.setBounds(centerX, centerY, buttonWidth, buttonHeight);
        exitButton.setBounds(centerX, centerY + buttonHeight + spacing, buttonWidth, buttonHeight);

        tryAgainButton.setVisible(true);
        exitButton.setVisible(true);
    }

    private void positionPauseButtons() {
        int pauseButtonWidth = 180, pauseButtonHeight = 40, pauseSpacing = 20;
        int centerX = getWidth() / 2 - pauseButtonWidth / 2;
        int centerY = getHeight() / 2 + 60;
        resumeButton.setBounds(centerX, centerY, pauseButtonWidth, pauseButtonHeight);
        pauseMenuButton.setBounds(centerX, centerY + pauseButtonHeight + pauseSpacing, pauseButtonWidth, pauseButtonHeight);
        resumeButton.setVisible(true);
        pauseMenuButton.setVisible(true);
        resumeButton.requestFocusInWindow();
    }

    private Runnable onReturnToMenu;

    public void setOnReturnToMenu(Runnable r) {
        this.onReturnToMenu = r;
    }

    public JButton getPauseButton() { return iconButton; }
    public void setPaused(boolean paused) {
        this.paused = paused;
        repaint();
    }
    public boolean isPaused() {
        return controller != null && controller.isPaused();
    }
    public void setController(SnakeController controller) { this.controller = controller; }
    public void setKeyboardKeysImg(Image img) { this.keyboardKeysImg = img; }
    public boolean isHelpOverlayVisible() { return helpOverlay != null; }
    public void showHelpOverlay() {
        if (helpOverlay == null) {
            if (controller != null) controller.pauseForHelp();
            helpOverlay = new HelpOverlayPanel(this);
            helpOverlay.setBounds(0, 0, getWidth(), getHeight());
            setLayout(null);
            add(helpOverlay, Integer.valueOf(1000));
            setComponentZOrder(helpOverlay, 0);
            helpOverlay.repaint();
            helpOverlay.requestFocusInWindow();
            revalidate();
            repaint();
        }
    }
    public void removeHelpOverlay() {
        if (helpOverlay != null) {
            remove(helpOverlay);
            helpOverlay = null;
            if (controller != null) controller.resumeAfterHelp();
            revalidate();
            repaint();
        }
    }
    public SnakeController getController() { return controller; }
}