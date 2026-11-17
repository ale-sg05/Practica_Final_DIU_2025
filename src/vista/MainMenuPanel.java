package vista;
import javax.swing.*;

import util.MusicPlayer;

import java.awt.*;

public class MainMenuPanel extends JPanel {
    private JButton playButton, configButton, helpButton, rankingButton, exitButton;
    private JLabel title;
    private Image backgroundImg;
    private HelpOverlayPanel helpOverlay;
    private static MusicPlayer menuMusic = new MusicPlayer();

    private final int PANEL_WIDTH = 700;
    private final int PANEL_HEIGHT = 760;

    public MainMenuPanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setOpaque(false);

        // Cargar la imagen de fondo
        try {
            backgroundImg = new ImageIcon("img/marco.png").getImage();
        } catch (Exception e) {
            backgroundImg = null;
        }

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        title = new JLabel("SNAKE");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 84));
        title.setForeground(new Color(0xE0C13B));
        title.setBorder(BorderFactory.createEmptyBorder(80, 0, 60, 0));

        playButton = new JButton("Jugar");
        configButton = new JButton("Configuración");
        helpButton = new JButton("Ayuda");
        rankingButton = new JButton("Ranking");
        exitButton = new JButton("Salir");

        Dimension buttonSize = new Dimension(250, 50);
        JButton[] buttons = {playButton, configButton, helpButton, rankingButton, exitButton};
        for (JButton btn : buttons) {
            btn.setMaximumSize(buttonSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setFont(new Font("Arial", Font.BOLD, 24));
            btn.setFocusPainted(false);
            btn.setBackground(Color.WHITE);
            btn.setForeground(Color.BLACK);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        }

        add(Box.createVerticalGlue());
        add(title);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(playButton);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(configButton);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(helpButton);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(rankingButton);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(exitButton);
        add(Box.createVerticalGlue());

        helpButton.addActionListener(e -> showHelpOverlay());
        menuMusic.play("audio/menu.wav", true, true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImg != null) {
            g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
        }
        if (helpOverlay != null) {
            helpOverlay.setBounds(0, 0, getWidth(), getHeight());
        }
    }

    public JButton getPlayButton() { return playButton; }
    public JButton getConfigButton() { return configButton; }
    public JButton getHelpButton() { return helpButton; }
    public JButton getRankingButton() { return rankingButton; }
    public JButton getExitButton() { return exitButton; }

    public void showHelpOverlay() {
        if (helpOverlay == null) {
            setLayout(null);
            helpOverlay = new HelpOverlayPanel(this);
            helpOverlay.setBounds(0, 0, getWidth(), getHeight());
            add(helpOverlay, Integer.valueOf(1000));
            setComponentZOrder(helpOverlay, 0);

            playButton.setVisible(false);
            configButton.setVisible(false);
            helpButton.setVisible(false);
            rankingButton.setVisible(false);
            exitButton.setVisible(false);
            title.setVisible(false);

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
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            playButton.setVisible(true);
            configButton.setVisible(true);
            helpButton.setVisible(true);
            rankingButton.setVisible(true);
            exitButton.setVisible(true);
            title.setVisible(true);

            revalidate();
            repaint();
        }
    }

    public static void stopMenuMusic() {
        menuMusic.stop();
    }

    public static void playMenuMusic() {
        menuMusic.play("audio/menu.wav", true, true);
    }
}