package controlador;
import javax.swing.*;

import modelo.GameConfig;
import modelo.InfiniteSnakeModel;
import modelo.MultiSnakeModel;
import modelo.SnakeModel;
import modelo.TimerSnakeModel;
import vista.ConfigPanel;
import vista.GameModesPanel;
import vista.InfiniteSnakeView;
import vista.MainMenuPanel;
import vista.MultiSnakeView;
import vista.RankingPanel;
import vista.SnakeView;
import vista.TimerSnakeView;


public class SnakeGame {
    public static void start() {
        JFrame frame = new JFrame("Snake Game");
        MainMenuPanel menu = new MainMenuPanel();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setContentPane(menu);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        menu.getPlayButton().addActionListener(e -> {
            GameModesPanel modesPanel = new GameModesPanel(menu, frame);
            frame.setContentPane(modesPanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.revalidate();
            frame.repaint();
            modesPanel.requestFocusInWindow();
        });

        menu.getConfigButton().addActionListener(e -> {
            ConfigPanel configPanel = new ConfigPanel(true);
            frame.setContentPane(configPanel);
            frame.pack();
            frame.setLocationRelativeTo(null);

            configPanel.getBackButton().addActionListener(ev -> {
                configPanel.saveConfig();
                frame.setContentPane(menu);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.revalidate();
                menu.requestFocusInWindow();
            });
        });

        menu.getRankingButton().addActionListener(e -> {
            RankingPanel rankingPanel = new RankingPanel(frame, menu);
            frame.setContentPane(rankingPanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.revalidate();
            frame.repaint();
        });

        menu.getExitButton().addActionListener(e -> frame.dispose());
    }

    public static void startClassicGame(JFrame frame, MainMenuPanel menu) {
        MainMenuPanel.stopMenuMusic();
        int numCasillas = GameConfig.getNumCasillas();
        int speed = GameConfig.getSpeed();
        SnakeModel model = new SnakeModel(numCasillas);
        model.reset();
        SnakeView view = new SnakeView(model, GameConfig.isDaltonic());
        SnakeController controller = new SnakeController(model, view, speed);
        view.setController(controller);

        view.setOnReturnToMenu(() -> {
            frame.setContentPane(menu);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.revalidate();
            frame.repaint();
            menu.requestFocusInWindow();
            MainMenuPanel.playMenuMusic();
        });

        frame.setContentPane(view);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();
        view.requestFocusInWindow();
    }

    public static void startInfiniteGame(JFrame frame, MainMenuPanel menu) {
        MainMenuPanel.stopMenuMusic();
        int numCasillas = GameConfig.getNumCasillas();
        int speed = GameConfig.getSpeed();
        // infinito
		InfiniteSnakeModel model = new InfiniteSnakeModel(numCasillas);
        model.reset();
		InfiniteSnakeView view = new InfiniteSnakeView(model, GameConfig.isDaltonic());
		SnakeController controller = new SnakeController(model, view, speed);
		view.setController(controller);

        view.setOnReturnToMenu(() -> {
            frame.setContentPane(menu);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.revalidate();
            frame.repaint();
            menu.requestFocusInWindow();
            MainMenuPanel.playMenuMusic();
        });

        frame.setContentPane(view);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();
        view.requestFocusInWindow();
    }

    public static void startTimerGame(JFrame frame, MainMenuPanel menu) {
        MainMenuPanel.stopMenuMusic();
        int numCasillas = GameConfig.getNumCasillas();
        int speed = GameConfig.getSpeed();
        // contrarreloj
		TimerSnakeModel model = new TimerSnakeModel(numCasillas);
        model.reset();
		TimerSnakeView view = new TimerSnakeView(model, GameConfig.isDaltonic());
		SnakeController controller = new SnakeController(model, view, speed);
		view.setController(controller);

        view.setOnReturnToMenu(() -> {
            frame.setContentPane(menu);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.revalidate();
            frame.repaint();
            menu.requestFocusInWindow();
            MainMenuPanel.playMenuMusic();
        });

        frame.setContentPane(view);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();
        view.requestFocusInWindow();
    }

    public static void startMultiplayerGame(JFrame frame, MainMenuPanel menu) {
        MainMenuPanel.stopMenuMusic();
        int numCasillas = GameConfig.getNumCasillas();
        int speed = GameConfig.getSpeed();
        int skin1 = GameConfig.getSkinIndex();
        int numSkins = 4;
        int skin2 = (skin1 + 1) % numSkins;
        MultiSnakeModel model = new MultiSnakeModel(numCasillas);
        MultiSnakeView view = new MultiSnakeView(model, GameConfig.isDaltonic(), skin1, skin2);
        MultiSnakeController controller = new MultiSnakeController(model, view, speed);
        view.setController(controller);

        view.setOnReturnToMenu(() -> {
            frame.setContentPane(menu);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.revalidate();
            frame.repaint();
            menu.requestFocusInWindow();
            MainMenuPanel.playMenuMusic();
        });

        frame.setContentPane(view);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();
        view.requestFocusInWindow();
    }
}