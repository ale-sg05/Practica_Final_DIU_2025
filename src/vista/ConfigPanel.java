package vista;
import javax.swing.*;

import modelo.AudioConfig;
import modelo.GameConfig;
import util.AudioManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ConfigPanel extends JPanel {
    private static int selectedSpeedIndex = 1; // 0: Lento, 1: Moderado, 2: Rápido
    private static int selectedMapSizeIndex = 1; // 0: Pequeño, 1: Mediano, 2: Grande
    private static int selectedSkinIndex = 0;

    private JButton backButton, skinIzquierdaBtn, skinDerechaBtn;
    private JLabel sprite1, sprite2;
    private ImageIcon[] headIcons;
    private ImageIcon[] bodyIcons;
    private int currentSkinIndex = 0;

    private JToggleButton velocidadLentoBtn, velocidadMedioBtn, velocidadRapidoBtn;
    private ButtonGroup velocidadGroup;

    private JToggleButton mapaPequeBtn, mapaMedioBtn, mapaGrandeBtn;
    private ButtonGroup mapaGroup;

    private Image backgroundImg;

    private JSlider masterVolumeSlider, musicVolumeSlider, effectsVolumeSlider;
    private JCheckBox muteAllBox, muteMusicBox, muteEffectsBox;

    private boolean openedFromMenu = false;

    public ConfigPanel(boolean openedFromMenu) {
        this.openedFromMenu = openedFromMenu;

        setPreferredSize(new Dimension(700, 760));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Carga la imagen de fondo
        try {
            backgroundImg = ImageIO.read(new File("img/marco.png"));
        } catch (IOException e) {
            backgroundImg = null;
        }

        setOpaque(false); // Para que se pinte el fondo personalizado

        // Añade espacio arriba para centrar verticalmente
        add(Box.createVerticalGlue());

        OutlinedLabel title = new OutlinedLabel("Configuración");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(new Color(0xE0C13B));
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 30, 0));
        add(title);

        OutlinedLabel speedLabel = new OutlinedLabel("Velocidad:");
        speedLabel.setForeground(Color.WHITE);
        speedLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        speedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(speedLabel);

        JPanel velocidadPanel = new JPanel();
        velocidadPanel.setOpaque(false);
        velocidadPanel.setLayout(new BoxLayout(velocidadPanel, BoxLayout.X_AXIS));
        velocidadPanel.setMaximumSize(new Dimension(350, 50));
        velocidadPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        velocidadLentoBtn = new JToggleButton("Lento");
        velocidadMedioBtn = new JToggleButton("Moderado");
        velocidadRapidoBtn = new JToggleButton("Rápido");

        velocidadGroup = new ButtonGroup();
        velocidadGroup.add(velocidadLentoBtn);
        velocidadGroup.add(velocidadMedioBtn);
        velocidadGroup.add(velocidadRapidoBtn);

        for (JToggleButton btn : new JToggleButton[]{velocidadLentoBtn, velocidadMedioBtn, velocidadRapidoBtn}) {
            btn.setFont(new Font("Arial", Font.BOLD, 18));
            btn.setFocusPainted(false);
            btn.setBackground(Color.LIGHT_GRAY);
            btn.setForeground(Color.BLACK);
            velocidadPanel.add(btn);
            velocidadPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        }
        // Selecciona el botón según la última selección
        switch (selectedSpeedIndex) {
            case 0 -> velocidadLentoBtn.setSelected(true);
            case 1 -> velocidadMedioBtn.setSelected(true);
            case 2 -> velocidadRapidoBtn.setSelected(true);
        }

        add(velocidadPanel);

        add(Box.createRigidArea(new Dimension(0, 20)));

        OutlinedLabel mapLabel = new OutlinedLabel("Tamaño del mapa:");
        mapLabel.setForeground(Color.WHITE);
        mapLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        mapLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(mapLabel);

        JPanel mapaPanel = new JPanel();
        mapaPanel.setOpaque(false);
        mapaPanel.setLayout(new BoxLayout(mapaPanel, BoxLayout.X_AXIS));
        mapaPanel.setMaximumSize(new Dimension(350, 50));
        mapaPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mapaPequeBtn = new JToggleButton("Pequeño");
        mapaMedioBtn = new JToggleButton("Mediano");
        mapaGrandeBtn = new JToggleButton("Grande");

        mapaGroup = new ButtonGroup();
        mapaGroup.add(mapaPequeBtn);
        mapaGroup.add(mapaMedioBtn);
        mapaGroup.add(mapaGrandeBtn);

        for (JToggleButton btn : new JToggleButton[]{mapaPequeBtn, mapaMedioBtn, mapaGrandeBtn}) {
            btn.setFont(new Font("Arial", Font.BOLD, 18));
            btn.setFocusPainted(false);
            btn.setBackground(Color.LIGHT_GRAY);
            btn.setForeground(Color.BLACK);
            mapaPanel.add(btn);
            mapaPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        }
        // Selecciona el botón según la última selección
        switch (selectedMapSizeIndex) {
            case 0 -> mapaPequeBtn.setSelected(true);
            case 1 -> mapaMedioBtn.setSelected(true);
            case 2 -> mapaGrandeBtn.setSelected(true);
        }

        add(mapaPanel);

        add(Box.createRigidArea(new Dimension(0, 20)));

        OutlinedLabel skinLabel = new OutlinedLabel("Aspecto de la serpiente:");
        skinLabel.setForeground(Color.WHITE);
        skinLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        skinLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(skinLabel);

        JPanel skinPanel = new JPanel();
        skinPanel.setOpaque(false);
        skinPanel.setLayout(new BoxLayout(skinPanel, BoxLayout.X_AXIS));
        skinPanel.setMaximumSize(new Dimension(350, 60));
        skinPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Añade pegamento a la izquierda
        skinPanel.add(Box.createHorizontalGlue());

        skinIzquierdaBtn = new JButton("<<");
        skinIzquierdaBtn.setFont(new Font("Arial", Font.BOLD, 22));
        skinIzquierdaBtn.setFocusPainted(false);
        skinPanel.add(skinIzquierdaBtn);

        // Espacio entre botón y sprite
        skinPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        // Carga los sprites ANTES de crear el JLabel
        int numSkins = 4;
        headIcons = new ImageIcon[numSkins];
        bodyIcons = new ImageIcon[numSkins];
        try {
            BufferedImage spriteSheet = ImageIO.read(new File("img/snake.png"));
            int[] yOffsets = {0, 8, 24, 32}; // y para cada skin
            for (int i = 0; i < numSkins; i++) {
                int y = yOffsets[i];
                BufferedImage headImg = spriteSheet.getSubimage(16, y, 8, 8); // cabeza izquierda
                BufferedImage bodyImg = spriteSheet.getSubimage(40, y, 8, 8);
                headIcons[i] = new ImageIcon(headImg.getScaledInstance(48, 48, Image.SCALE_SMOOTH));
                bodyIcons[i] = new ImageIcon(bodyImg.getScaledInstance(48, 48, Image.SCALE_SMOOTH));
            }
        } catch (IOException e) {
            for (int i = 0; i < numSkins; i++) {
                headIcons[i] = new ImageIcon();
                bodyIcons[i] = new ImageIcon();
            }
        }
        // Selecciona la skin guardada
        currentSkinIndex = selectedSkinIndex; // Esto ya lo tienes
        // Si quieres que siempre use el de GameConfig:
        currentSkinIndex = GameConfig.getSkinIndex();
        selectedSkinIndex = currentSkinIndex;

        sprite1 = new JLabel(headIcons[currentSkinIndex]);
        sprite2 = new JLabel(bodyIcons[currentSkinIndex]);
        skinPanel.add(sprite1);
        skinPanel.add(sprite2);

        // Espacio entre sprite y botón
        skinPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        skinDerechaBtn = new JButton(">>");
        skinDerechaBtn.setFont(new Font("Arial", Font.BOLD, 22));
        skinDerechaBtn.setFocusPainted(false);
        skinPanel.add(skinDerechaBtn);

        // Añade pegamento a la derecha
        skinPanel.add(Box.createHorizontalGlue());

        add(skinPanel);

        add(Box.createRigidArea(new Dimension(0, 20)));

        // --- AUDIO ---
        OutlinedLabel audioLabel = new OutlinedLabel("Audio:");
        audioLabel.setForeground(Color.WHITE);
        audioLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        audioLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(audioLabel);

        JPanel audioPanel = new JPanel();
        audioPanel.setOpaque(false);
        audioPanel.setLayout(new BoxLayout(audioPanel, BoxLayout.Y_AXIS));
        audioPanel.setMaximumSize(new Dimension(350, 100));
        audioPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Checkboxes
        muteAllBox = new JCheckBox("Silenciar todo");
        muteMusicBox = new JCheckBox("Silenciar música");
        muteEffectsBox = new JCheckBox("Silenciar SFX");
        for (JCheckBox cb : new JCheckBox[]{muteAllBox, muteMusicBox, muteEffectsBox}) {
            cb.setForeground(Color.WHITE);
            cb.setOpaque(false);
            cb.setFont(new Font("Arial", Font.PLAIN, 22)); // Igual que daltonicMode
            cb.setAlignmentX(Component.CENTER_ALIGNMENT);   // Igual que daltonicMode
            audioPanel.add(cb);
        }
        muteAllBox.setSelected(AudioConfig.muteAll);
        muteMusicBox.setSelected(AudioConfig.muteMusic);
        muteEffectsBox.setSelected(AudioConfig.muteEffects);

        add(audioPanel);

        add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel daltonicLabel = new JLabel("Modo daltónico:");
        daltonicLabel.setForeground(Color.WHITE);
        daltonicLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        daltonicLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(daltonicLabel);

        String[] modos = {"Ninguno", "Protanopía", "Deuteranopía", "Tritanopía"};
        JComboBox<String> daltonicCombo = new JComboBox<>(modos);
        daltonicCombo.setMaximumSize(new Dimension(250, 32));
        daltonicCombo.setFont(new Font("Arial", Font.BOLD, 18));
        daltonicCombo.setSelectedIndex(GameConfig.getDaltonicMode().ordinal());
        daltonicCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(daltonicCombo);

        daltonicCombo.addActionListener(e -> {
            GameConfig.setDaltonicMode(GameConfig.DaltonicMode.values()[daltonicCombo.getSelectedIndex()]);
        });

        add(Box.createVerticalStrut(30)); // Espacio antes del botón

        // Panel para centrar el botón "Volver"
        JPanel backPanel = new JPanel();
        backPanel.setOpaque(false);
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.X_AXIS));
        backPanel.setMaximumSize(new Dimension(350, 60));
        backPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        backButton = new JButton("Volver");
        backButton.setFont(new Font("Arial", Font.BOLD, 22));
        backButton.setFocusPainted(false);
        backPanel.add(Box.createHorizontalGlue());
        backPanel.add(backButton);
        backPanel.add(Box.createHorizontalGlue());

        add(backPanel);

        add(Box.createVerticalGlue()); // Espacio abajo

        // Listeners para cambiar skin
        skinIzquierdaBtn.addActionListener(e -> {
            currentSkinIndex = (currentSkinIndex - 1 + headIcons.length) % headIcons.length;
            sprite1.setIcon(headIcons[currentSkinIndex]);
            sprite2.setIcon(bodyIcons[currentSkinIndex]);
            selectedSkinIndex = currentSkinIndex;
        });
        skinDerechaBtn.addActionListener(e -> {
            currentSkinIndex = (currentSkinIndex + 1) % headIcons.length;
            sprite1.setIcon(headIcons[currentSkinIndex]);
            sprite2.setIcon(bodyIcons[currentSkinIndex]);
            selectedSkinIndex = currentSkinIndex;
        });

        setToggleButtonStyle(velocidadLentoBtn);
        setToggleButtonStyle(velocidadMedioBtn);
        setToggleButtonStyle(velocidadRapidoBtn);
        setToggleButtonStyle(mapaPequeBtn);
        setToggleButtonStyle(mapaMedioBtn);
        setToggleButtonStyle(mapaGrandeBtn);

        muteAllBox.addActionListener(e -> applyAudioConfig());
        muteMusicBox.addActionListener(e -> applyAudioConfig());
        muteEffectsBox.addActionListener(e -> applyAudioConfig());
    }

    private void setToggleButtonStyle(JToggleButton btn) {
        btn.addChangeListener(e -> {
            if (btn.isSelected()) {
                btn.setBackground(Color.GRAY); // Inactivo
                btn.setForeground(Color.WHITE);
            } else {
                btn.setBackground(Color.LIGHT_GRAY); // Activo
                btn.setForeground(Color.BLACK);
            }
        });
    }

    public void saveConfig() {
        if (velocidadLentoBtn.isSelected()) selectedSpeedIndex = 0;
        else if (velocidadMedioBtn.isSelected()) selectedSpeedIndex = 1;
        else selectedSpeedIndex = 2;

        // Aplica la velocidad seleccionada a GameConfig
        int speed = switch (selectedSpeedIndex) {
            case 0 -> 200; // Lento
            case 1 -> 100; // Moderado
            case 2 -> 50;  // Rápido
            default -> 100;
        };
        GameConfig.setSpeed(speed);

        if (mapaPequeBtn.isSelected()) selectedMapSizeIndex = 0;
        else if (mapaMedioBtn.isSelected()) selectedMapSizeIndex = 1;
        else selectedMapSizeIndex = 2;

        int numCasillas = switch (selectedMapSizeIndex) {
            case 0 -> 15; // Pequeño
            case 1 -> 24; // Mediano
            case 2 -> 40; // Grande
            default -> 24;
        };
        GameConfig.setNumCasillas(numCasillas);

        GameConfig.setSkinIndex(selectedSkinIndex);

        AudioConfig.muteAll = muteAllBox.isSelected();
        AudioConfig.muteMusic = muteMusicBox.isSelected();
        AudioConfig.muteEffects = muteEffectsBox.isSelected();
    }

    // Añadido: Método para aplicar configuración de audio
    private void applyAudioConfig() {
        AudioConfig.muteAll = muteAllBox.isSelected();
        AudioConfig.muteMusic = muteMusicBox.isSelected();
        AudioConfig.muteEffects = muteEffectsBox.isSelected();

        if (AudioConfig.muteAll || AudioConfig.muteMusic) {
            MainMenuPanel.stopMenuMusic();
            AudioManager.stopGameMusic();
        } else {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            // Si se abrió desde el menú, reproduce la música del menú
            if (openedFromMenu) {
                AudioManager.playMenuMusic();
            } else {
                AudioManager.playGameMusic(GameConfig.getSpeed());
            }
        }
    }

    // Getters para el controlador
    public JButton getBackButton() { return backButton; }
    public JButton getSkinIzquierdaBtn() { return skinIzquierdaBtn; }
    public JButton getSkinDerechaBtn() { return skinDerechaBtn; }
    public JLabel getSprite1() { return sprite1; }
    public JLabel getSprite2() { return sprite2; }
    public int getCurrentSkinIndex() { return currentSkinIndex; }
    public int getSelectedSpeedIndex() { return selectedSpeedIndex; }
    public int getSelectedMapSizeIndex() { return selectedMapSizeIndex; }
    public int getMasterVolume() { return masterVolumeSlider.getValue(); }
    public int getMusicVolume() { return musicVolumeSlider.getValue(); }
    public int getEffectsVolume() { return effectsVolumeSlider.getValue(); }
    public boolean isMuteAll() { return muteAllBox.isSelected(); }
    public boolean isMuteMusic() { return muteMusicBox.isSelected(); }
    public boolean isMuteEffects() { return muteEffectsBox.isSelected(); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImg != null) {
            g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
        }
    }

    
    private static class OutlinedLabel extends JLabel {
        private Color outlineColor = Color.BLACK;
        private int outlineThickness = 4;

        public OutlinedLabel(String text) {
            super(text);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setFont(getFont());
            String text = getText();
            FontMetrics fm = g2.getFontMetrics();
            int x = 0;
            int y = fm.getAscent();

            
            for (int dx = -outlineThickness; dx <= outlineThickness; dx++) {
                for (int dy = -outlineThickness; dy <= outlineThickness; dy++) {
                    if (dx != 0 || dy != 0) {
                        g2.setColor(outlineColor);
                        g2.drawString(text, x + dx, y + dy);
                    }
                }
            }
            
            g2.setColor(getForeground());
            g2.drawString(text, x, y);
            g2.dispose();
        }
    }
}