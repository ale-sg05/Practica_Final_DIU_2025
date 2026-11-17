package modelo;
import java.awt.*;
import java.util.Random;

public class SnakeModel {
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private int numCols;
    private int numRows;
    private int tileSize;
    private int GAME_UNITS;

    protected int[] x;
    protected int[] y;
    private int bodyParts = 3;
    protected int applesEaten;
    protected Point apple;
    protected Point specialApple0;
    protected Point specialApple1;
    protected Point specialApple2;
    private Random rand = new Random();
    private char direction = 'R';
    private boolean running = false;
    protected int pendingGrowth = 0;
    private long specialApple1SpawnTime = 0;
    private static final int SPECIAL_APPLE1_DURATION_MS = 5000;

    public SnakeModel(int numCasillas) {
        this.numCols = numCasillas;
        this.numRows = numCasillas;
        this.tileSize = WIDTH / numCasillas;
        this.GAME_UNITS = numCols * numRows;
        x = new int[GAME_UNITS];
        y = new int[GAME_UNITS];
        
    }

    public void reset() {
        bodyParts = 3;
        applesEaten = 0;
        direction = 'R';
        int startX = numCols / 2;
        int startY = numRows / 2;
        for (int i = 0; i < bodyParts; i++) {
            x[i] = startX - i;
            y[i] = startY;
        }
        spawnApples();
        running = true;
    }

    protected void spawnApple() {
        apple = randomFreeCell();
    }

    protected void spawnApples() {
        apple = null;
        specialApple0 = null;
        specialApple1 = null;
        specialApple2 = null;

        // 1. Marca todas las posiciones ocupadas por la serpiente en un array booleano
        boolean[][] ocupado = new boolean[numCols][numRows];
        for (int i = 0; i < bodyParts; i++) {
            ocupado[x[i]][y[i]] = true;
        }

        // 2. Genera una lista de índices libres (más eficiente que Point)
        java.util.List<Integer> libres = new java.util.ArrayList<>();
        for (int i = 0; i < numCols; i++) {
            for (int j = 0; j < numRows; j++) {
                if (!ocupado[i][j]) {
                    libres.add(i * numRows + j);
                }
            }
        }

        if (libres.isEmpty()) return;

        // 4. Coloca la manzana normal en una posición aleatoria
        int idx = libres.remove(rand.nextInt(libres.size()));
        apple = new Point(idx / numRows, idx % numRows);

        // 5. Coloca manzanas especiales solo si quedan libres
        if (!libres.isEmpty() && rand.nextDouble() < 0.4) {
            int specialIdx = libres.remove(rand.nextInt(libres.size()));
            Point special = new Point(specialIdx / numRows, specialIdx % numRows);
            if (rand.nextBoolean()) {
                specialApple0 = special;
            } else {
                specialApple2 = special;
            }
        }

        if (!libres.isEmpty() && rand.nextDouble() < 0.15) {
            int special1Idx = libres.remove(rand.nextInt(libres.size()));
            specialApple1 = new Point(special1Idx / numRows, special1Idx % numRows);
            specialApple1SpawnTime = System.currentTimeMillis();
        }
    }

    public Point randomFreeCell() {
        boolean[][] ocupado = new boolean[numCols][numRows];
        for (int i = 0; i < bodyParts; i++) {
            ocupado[x[i]][y[i]] = true;
        }
        java.util.List<Integer> libres = new java.util.ArrayList<>();
        for (int i = 0; i < numCols; i++) {
            for (int j = 0; j < numRows; j++) {
                if (!ocupado[i][j]) {
                    libres.add(i * numRows + j);
                }
            }
        }
        if (libres.isEmpty()) return null;
        int idx = libres.get(rand.nextInt(libres.size()));
        return new Point(idx / numRows, idx % numRows);
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U' -> y[0] -= 1;
            case 'D' -> y[0] += 1;
            case 'L' -> x[0] -= 1;
            case 'R' -> x[0] += 1;
        }
        if (pendingGrowth > 0 && bodyParts < x.length) {
            bodyParts++;
            pendingGrowth--;
        }
    }

    public boolean checkApple() {
        Point head = getHead();
        boolean ate = false;

        if (specialApple0 != null && head.equals(specialApple0)) {
            grow(2);
            addScore(2);
            ate = true;
        } else if (specialApple2 != null && head.equals(specialApple2)) {
            grow(-2);
            addScore(-2);
            ate = true;
        } else if (head.equals(apple)) {
            grow(1);
            addScore(1);
            ate = true;
        }
        // Lógica para specialApple1
        if (specialApple1 != null && head.equals(specialApple1)) {
            grow(5);
            addScore(5);
            specialApple1 = null;
            ate = true;
        }

        if (ate) {
            // Si tienes AudioManager, usa AudioManager.playEatSound();
            spawnApples();
        }
        return ate;
    }

    public boolean checkCollisions() {
        // Comienza en 1, no en bodyParts
        for (int i = 1; i < bodyParts; i++) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
                return true;
            }
        }
        if (x[0] < 0 || x[0] >= numCols || y[0] < 0 || y[0] >= numRows) {
            running = false;
            return true;
        }
        return false;
    }

    public void grow(int n) {
        if (n > 0) {
            pendingGrowth += n;
        } else if (n < 0) {
            bodyParts += n;
            if (bodyParts < 1) bodyParts = 1;
            if (pendingGrowth > 0) {
                pendingGrowth = Math.max(0, pendingGrowth + n);
            }
        }
    }

    public void addScore(int n) {
        applesEaten += n;
    }

    public void updateSpecialApple1() {
        if (specialApple1 != null && System.currentTimeMillis() - specialApple1SpawnTime > SPECIAL_APPLE1_DURATION_MS) {
            specialApple1 = null;
        }
    }

    public Point getHead() {
        return new Point(x[0], y[0]);
    }

    public int getNumCols() { return numCols; }
    public int getNumRows() { return numRows; }
    public int getTileSize() { return tileSize; }
    public int getWidth() { return WIDTH; }
    public int getHeight() { return HEIGHT; }
    public int getGameUnits() { return GAME_UNITS; }
    public int[] getX() { return x; }
    public int[] getY() { return y; }
    public int getBodyParts() { return bodyParts; }
    public int getApplesEaten() { return applesEaten; }
    public Point getApple() { return apple; }
    public Point getSpecialApple0() { return specialApple0; }
    public Point getSpecialApple1() { return specialApple1; }
    public Point getSpecialApple2() { return specialApple2; }
    public char getDirection() { return direction; }
    public boolean isRunning() { return running; }

    public void setX(int[] x) { this.x = x; }
    public void setY(int[] y) { this.y = y; }
    public void setBodyParts(int bodyParts) { this.bodyParts = bodyParts; }
    public void setApplesEaten(int applesEaten) { this.applesEaten = applesEaten; }
    public void setApple(Point apple) { this.apple = apple; }
    public void setDirection(char direction) { this.direction = direction; }
    public void setRunning(boolean running) { this.running = running; }
    public void setTileSize(int tileSize) { this.tileSize = tileSize; }
    public int getPendingGrowth() { return pendingGrowth; }
    public void setPendingGrowth(int value) { this.pendingGrowth = value; }
}