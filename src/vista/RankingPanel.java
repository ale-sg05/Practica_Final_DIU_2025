package vista;
import javax.swing.*;

import modelo.RankingEntry;
import modelo.RankingManager;

import java.awt.*;
import java.util.List;

public class RankingPanel extends JPanel {
    private RankingManager.Mode currentMode = RankingManager.Mode.CLASSIC;
    private final JButton classicBtn = new JButton("Clásico");
    private final JButton infiniteBtn = new JButton("Infinito");
    private final JButton timerBtn = new JButton("Contrarreloj");
    private final JButton backBtn = new JButton("Volver");
    private final JLabel[] entries = new JLabel[10];
    private Image backgroundImg;

    public RankingPanel(JFrame frame, MainMenuPanel menu) {
        setPreferredSize(new Dimension(700, 760));
        setOpaque(false);

        // Cargar la imagen de fondo igual que en MainMenuPanel
        try {
            backgroundImg = new ImageIcon("img/marco.png").getImage();
        } catch (Exception e) {
            backgroundImg = null;
        }

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(40, 40, 40));

        // Título
        JLabel title = new JLabel("Ranking");
        title.setFont(new Font("Arial", Font.BOLD, 68));
        title.setForeground(new Color(0xE0C13B));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(80));
        add(title);
        add(Box.createVerticalStrut(5));

        // Panel de botones de modos
        JPanel modePanel = new JPanel();
        modePanel.setOpaque(false);
        modePanel.setLayout(new BoxLayout(modePanel, BoxLayout.X_AXIS));
        Dimension btnSize = new Dimension(130, 40);
        for (JButton btn : new JButton[]{classicBtn, infiniteBtn, timerBtn}) {
            btn.setFont(new Font("Arial", Font.BOLD, 16));
            btn.setPreferredSize(btnSize);
            btn.setMaximumSize(btnSize);
            btn.setMinimumSize(btnSize);
            btn.setFocusPainted(false);
            btn.setBackground(new Color(60, 60, 60));
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            modePanel.add(btn);
            modePanel.add(Box.createHorizontalStrut(30));
        }
        modePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(modePanel);
        add(Box.createVerticalStrut(10));

        // Ranking entries
        JPanel entriesPanel = new JPanel();
        entriesPanel.setOpaque(false);
        entriesPanel.setLayout(new BoxLayout(entriesPanel, BoxLayout.Y_AXIS));
        for (int i = 0; i < entries.length; i++) {
            entries[i] = new JLabel();
            entries[i].setFont(new Font("Consolas", Font.BOLD, 28));
            entries[i].setForeground(Color.WHITE);
            entries[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            entriesPanel.add(entries[i]);
            entriesPanel.add(Box.createVerticalStrut(4));
        }
        entriesPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(entriesPanel);

        // Botón volver más arriba
        add(Box.createVerticalStrut(10));
        backBtn.setFont(new Font("Arial", Font.BOLD, 16));
        backBtn.setPreferredSize(new Dimension(130, 40));
        backBtn.setMaximumSize(new Dimension(130, 40));
        backBtn.setMinimumSize(new Dimension(130, 40));
        backBtn.setFocusPainted(false);
        backBtn.setBackground(new Color(60, 60, 60));
        backBtn.setForeground(Color.WHITE);
        backBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(backBtn);

        add(Box.createVerticalGlue());

        // Listeners
        classicBtn.addActionListener(e -> { currentMode = RankingManager.Mode.CLASSIC; updateRanking(); });
        infiniteBtn.addActionListener(e -> { currentMode = RankingManager.Mode.INFINITE; updateRanking(); });
        timerBtn.addActionListener(e -> { currentMode = RankingManager.Mode.TIMER; updateRanking(); });
        backBtn.addActionListener(e -> {
            frame.setContentPane(menu);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.revalidate();
            frame.repaint();
        });

        updateRanking();
    }

    private void updateRanking() {
        String dots = "........................";
        List<RankingEntry> list = RankingManager.getRanking(currentMode);
        for (int i = 0; i < entries.length; i++) {
            if (i < list.size()) {
                entries[i].setText(String.format("#%d%s(%d)", i + 1, dots, list.get(i).score));
            } else {
                entries[i].setText(String.format("#%d%s(-)", i + 1, dots));
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImg != null) {
            g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
        }
    }
}