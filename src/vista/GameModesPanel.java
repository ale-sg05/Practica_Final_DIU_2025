package vista;
import javax.swing.*;

import controlador.SnakeGame;

import java.awt.*;

public class GameModesPanel extends JPanel {
    private JButton classicButton, infiniteButton, timerButton, multiButton, backButton;
    private Image backgroundImg;

    public GameModesPanel(MainMenuPanel menu, JFrame frame) {
        setOpaque(false);
        setPreferredSize(new Dimension(700, 760));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        try {
            backgroundImg = new ImageIcon("img/marco.png").getImage();
        } catch (Exception e) {
            backgroundImg = null;
        }

        add(Box.createVerticalGlue());

        JLabel title = new JLabel("Modos de Juego");
        title.setFont(new Font("Arial", Font.BOLD, 64));
        title.setForeground(new Color(0xE0C13B));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(60, 0, 60, 0));
        add(title);

        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 40, 30));
        gridPanel.setOpaque(false);
        gridPanel.setMaximumSize(new Dimension(500, 220));
        gridPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        classicButton = new JButton("Modo Clásico");
        infiniteButton = new JButton("Modo Infinito");
        timerButton = new JButton("Modo Contrarreloj");
        multiButton = new JButton("Modo Multijugador");

        JButton[] modeButtons = {classicButton, infiniteButton, timerButton, multiButton};
        Dimension buttonSize = new Dimension(250, 50);
        for (JButton btn : modeButtons) {
            btn.setFont(new Font("Arial", Font.BOLD, 20));
            btn.setBackground(Color.WHITE);
            btn.setForeground(Color.BLACK);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            btn.setMaximumSize(buttonSize);
        }

        gridPanel.add(classicButton);
        gridPanel.add(infiniteButton);
        gridPanel.add(timerButton);
        gridPanel.add(multiButton);

        add(gridPanel);

        add(Box.createRigidArea(new Dimension(0, 30)));

        backButton = new JButton("Volver");
        backButton.setFont(new Font("Arial", Font.BOLD, 24));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(Color.BLACK);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        backButton.setMaximumSize(new Dimension(180, 40));
        add(backButton);

        add(Box.createVerticalGlue());

        backButton.addActionListener(e -> {
            frame.setContentPane(menu);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.revalidate();
            frame.repaint();
            menu.requestFocusInWindow();
        });

        classicButton.addActionListener(e -> SnakeGame.startClassicGame(frame, menu));

        infiniteButton.addActionListener(e -> SnakeGame.startInfiniteGame(frame, menu));

        timerButton.addActionListener(e -> SnakeGame.startTimerGame(frame, menu));

        multiButton.addActionListener(e -> SnakeGame.startMultiplayerGame(frame, menu));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImg != null) {
            g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public JButton getClassicButton() { return classicButton; }
    public JButton getInfiniteButton() { return infiniteButton; }
    public JButton getTimerButton() { return timerButton; }
    public JButton getMultiButton() { return multiButton; }
}