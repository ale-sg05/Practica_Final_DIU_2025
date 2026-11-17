package controlador;
import java.awt.event.*;

import modelo.MultiSnakeModel;
import util.AudioManager;
import vista.MultiSnakeView;
import vista.SnakeView;
import vista.SnakeView.GameState;

public class MultiSnakeController extends SnakeController {
    private final MultiSnakeModel multiModel;
    private String winner = "";

    public MultiSnakeController(MultiSnakeModel model, MultiSnakeView view, int speed) {
        super(model, view, speed);
        this.multiModel = model;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        super.keyPressed(e);
        if (key == KeyEvent.VK_SPACE) return;

        // Jugador 1 (Flechas)
        switch (key) {
            case KeyEvent.VK_LEFT -> { if (multiModel.getDirection() != 'R') multiModel.setDirection('L'); }
            case KeyEvent.VK_RIGHT -> { if (multiModel.getDirection() != 'L') multiModel.setDirection('R'); }
            case KeyEvent.VK_UP -> { if (multiModel.getDirection() != 'D') multiModel.setDirection('U'); }
            case KeyEvent.VK_DOWN -> { if (multiModel.getDirection() != 'U') multiModel.setDirection('D'); }
        }
        // Jugador 2 (WASD)
        switch (key) {
            case KeyEvent.VK_W -> { if (multiModel.getDirection2() != 'D') multiModel.setDirection2('U'); }
            case KeyEvent.VK_S -> { if (multiModel.getDirection2() != 'U') multiModel.setDirection2('D'); }
            case KeyEvent.VK_A -> { if (multiModel.getDirection2() != 'R') multiModel.setDirection2('L'); }
            case KeyEvent.VK_D -> { if (multiModel.getDirection2() != 'L') multiModel.setDirection2('R'); }
        }
    }

    public void startBothPlayers() {
        this.waitingFirstMove = false;
        timer.start();
        AudioManager.playGameMusic(delay);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isPaused()) return;
        if (view.gameState == SnakeView.GameState.RUNNING) {
            multiModel.moveBoth();
            multiModel.checkAppleBoth();

            if (multiModel.getApplesEaten() < 0) {
                endGame("Jugador 2");
                return;
            }
            if (multiModel.getApplesEaten2() < 0) {
                endGame("Jugador 1");
                return;
            }
            checkMultiplayerCollisions();
            view.repaint();
        }
    }

    private void checkMultiplayerCollisions() {
        for (int i = 1; i < multiModel.getBodyParts(); i++) {
            if (multiModel.getX()[0] == multiModel.getX()[i] && multiModel.getY()[0] == multiModel.getY()[i]) {
                endGame("Jugador 2");
                return;
            }
        }
        for (int i = 0; i < multiModel.getBodyParts2(); i++) {
            if (multiModel.getX()[0] == multiModel.getX2()[i] && multiModel.getY()[0] == multiModel.getY2()[i]) {
                endGame("Jugador 2");
                return;
            }
        }
        for (int i = 1; i < multiModel.getBodyParts2(); i++) {
            if (multiModel.getX2()[0] == multiModel.getX2()[i] && multiModel.getY2()[0] == multiModel.getY2()[i]) {
                endGame("Jugador 1");
                return;
            }
        }
        for (int i = 0; i < multiModel.getBodyParts(); i++) {
            if (multiModel.getX2()[0] == multiModel.getX()[i] && multiModel.getY2()[0] == multiModel.getY()[i]) {
                endGame("Jugador 1");
                return;
            }
        }

        // Colisión con bordes
        if (multiModel.getX()[0] < 0 || multiModel.getX()[0] >= multiModel.getNumCols() ||
            multiModel.getY()[0] < 0 || multiModel.getY()[0] >= multiModel.getNumRows()) {
            endGame("Jugador 2");
            return;
        }
        if (multiModel.getX2()[0] < 0 || multiModel.getX2()[0] >= multiModel.getNumCols() ||
            multiModel.getY2()[0] < 0 || multiModel.getY2()[0] >= multiModel.getNumRows()) {
            endGame("Jugador 1");
            return;
        }
    }

    private void endGame(String winner) {
        timer.stop();
        view.gameState = SnakeView.GameState.GAME_OVER;
        AudioManager.stopGameMusic();
        AudioManager.playGameOverSound();
        this.winner = winner;
        view.repaint();
    }

    public String getWinner() {
        return winner;
    }

    @Override
    public void restartGame() {
        multiModel.reset();
        view.gameState = SnakeView.GameState.RUNNING;
        view.setPaused(false);
        directionChanged = false;
        waitingFirstMove = true;
        timer.restart();
        view.repaint();
        view.requestFocusInWindow();
    }
}
