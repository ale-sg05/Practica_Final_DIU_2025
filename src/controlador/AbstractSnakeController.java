package controlador;
import javax.swing.*;

import modelo.SnakeModel;
import util.AudioManager;
import vista.SnakeView;

import java.awt.event.*;

public abstract class AbstractSnakeController implements ActionListener, KeyListener {
    protected SnakeModel model;
    protected SnakeView view;
    protected Timer timer;
    protected boolean paused = false;
    protected boolean waitingFirstMove = true;
    protected boolean directionChanged = false;
    protected boolean pausedByHelp = false;
    protected int delay;

    public AbstractSnakeController(SnakeModel model, SnakeView view, int speed) {
        this.model = model;
        this.view = view;
        this.delay = speed;
        this.timer = new Timer(speed, this);
        view.addKeyListener(this);
        timer.stop();
    }

    public void togglePause() {
        if (paused) {
            timer.start();
            AudioManager.playGameMusic(delay);
        } else {
            timer.stop();
            AudioManager.pauseGameMusic();
        }
        paused = !paused;
        view.setPaused(paused);
    }

    public void pauseForHelp() {
        if (!paused) {
            timer.stop();
            AudioManager.pauseGameMusic();
            pausedByHelp = true;
            view.setPaused(true);
        } else {
            pausedByHelp = false;
        }
    }

    public void resumeAfterHelp() {
        if (pausedByHelp) {
            timer.start();
            AudioManager.playGameMusic(delay);
            view.setPaused(false);
            pausedByHelp = false;
        }
    }

    public void handleFirstMove(int key) {
        if (waitingFirstMove && (
                key == KeyEvent.VK_LEFT ||
                key == KeyEvent.VK_RIGHT ||
                key == KeyEvent.VK_UP ||
                key == KeyEvent.VK_DOWN)) {
            timer.start();
            waitingFirstMove = false;
            AudioManager.playGameMusic(delay);
            switch (key) {
                case KeyEvent.VK_LEFT -> { if (model.getDirection() != 'R') model.setDirection('L'); }
                case KeyEvent.VK_RIGHT -> { if (model.getDirection() != 'L') model.setDirection('R'); }
                case KeyEvent.VK_UP -> { if (model.getDirection() != 'D') model.setDirection('U'); }
                case KeyEvent.VK_DOWN -> { if (model.getDirection() != 'U') model.setDirection('D'); }
            }
            directionChanged = true;
        }
    }

    public void handleHelpKey() {
        if (view.isHelpOverlayVisible()) {
            view.removeHelpOverlay();
        } else {
            view.showHelpOverlay();
        }
    }

    public boolean isWaitingFirstMove() {
        return waitingFirstMove;
    }


    public boolean isPaused() { return paused; }

    // Métodos vacíos para KeyListener
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // Solo permite pausar si NO está esperando el primer movimiento
        if (key == KeyEvent.VK_SPACE && !waitingFirstMove) {
            togglePause();
            return;
        }

        handleFirstMove(key);

        switch (key) {
            case KeyEvent.VK_LEFT -> { if (model.getDirection() != 'R') model.setDirection('L'); }
            case KeyEvent.VK_RIGHT -> { if (model.getDirection() != 'L') model.setDirection('R'); }
            case KeyEvent.VK_UP -> { if (model.getDirection() != 'D') model.setDirection('U'); }
            case KeyEvent.VK_DOWN -> { if (model.getDirection() != 'U') model.setDirection('D'); }
        }
    }


}