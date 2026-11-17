package modelo;
import util.AudioManager;

public class MultiSnakeModel extends SnakeModel {
    // Segunda serpiente
    protected int[] x2, y2;
    protected int bodyParts2 = 3;
    protected char direction2 = 'L';
    protected boolean alive2 = true;
    protected int applesEaten2 = 0; 
    protected int pendingGrowth2 = 0;

    public MultiSnakeModel(int numCasillas) {
        super(numCasillas);
        x2 = new int[getGameUnits()];
        y2 = new int[getGameUnits()];
        reset();
    }

    public int[] getX2() { return x2; }
    public int[] getY2() { return y2; }
    public int getBodyParts2() { return bodyParts2; }
    public char getDirection2() { return direction2; }
    public boolean isAlive2() { return alive2; }
    public void setDirection2(char dir) { direction2 = dir; }
    public int getApplesEaten2() { return applesEaten2; }

    
    public void moveBoth() {
        // Mueve serpiente 1
        for (int i = getBodyParts(); i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (getDirection()) {
            case 'U' -> y[0] -= 1;
            case 'D' -> y[0] += 1;
            case 'L' -> x[0] -= 1;
            case 'R' -> x[0] += 1;
        }
        if (pendingGrowth > 0 && getBodyParts() < x.length) {
            setBodyParts(getBodyParts() + 1);
            pendingGrowth--;
        }

        // Mueve serpiente 2
        for (int i = bodyParts2; i > 0; i--) {
            x2[i] = x2[i - 1];
            y2[i] = y2[i - 1];
        }
        switch (direction2) {
            case 'U' -> y2[0] -= 1;
            case 'D' -> y2[0] += 1;
            case 'L' -> x2[0] -= 1;
            case 'R' -> x2[0] += 1;
        }
        if (pendingGrowth2 > 0 && bodyParts2 < x2.length) {
            bodyParts2++;
            pendingGrowth2--;
        }
    }

    
    public void checkAppleBoth() {
       
        boolean ate1 = false;
        if (apple != null && x[0] == apple.x && y[0] == apple.y) {
            applesEaten++;
            pendingGrowth++;
            spawnApple();
            ate1 = true;
        } else if (specialApple1 != null && x[0] == specialApple1.x && y[0] == specialApple1.y) {
            applesEaten += 3;
            pendingGrowth += 3;
            specialApple1 = null;
            ate1 = true;
        } else if (specialApple2 != null && x[0] == specialApple2.x && y[0] == specialApple2.y) {
            applesEaten -= 2;
            int newBodyParts = Math.max(1, getBodyParts() - 2);
            setBodyParts(newBodyParts);
            pendingGrowth = Math.max(0, pendingGrowth - 2);
            specialApple2 = null;
            ate1 = true;
        } else if (specialApple0 != null && x[0] == specialApple0.x && y[0] == specialApple0.y) {
            applesEaten += 3;
            pendingGrowth += 3;
            specialApple0 = null;
            ate1 = true;
        }

        boolean ate2 = false;
        if (!ate1 && apple != null && x2[0] == apple.x && y2[0] == apple.y) {
            addScore2(1);
            grow2(1);
            spawnApple();
            ate2 = true;
        } else if (!ate1 && specialApple1 != null && x2[0] == specialApple1.x && y2[0] == specialApple1.y) {
            applesEaten2 += 3;
            pendingGrowth2 += 3;
            specialApple1 = null;
            ate2 = true;
        } else if (!ate1 && specialApple2 != null && x2[0] == specialApple2.x && y2[0] == specialApple2.y) {
            applesEaten2 -= 2;
            int newBodyParts2 = Math.max(1, bodyParts2 - 2);
            bodyParts2 = newBodyParts2;
            pendingGrowth2 = Math.max(0, pendingGrowth2 - 2);
            specialApple2 = null;
            ate2 = true;
        } else if (!ate1 && specialApple0 != null && x2[0] == specialApple0.x && y2[0] == specialApple0.y) {
            applesEaten2 += 3;
            pendingGrowth2 += 3;
            specialApple0 = null;
            ate2 = true;
        }

        
        if (ate1 || ate2) {
            AudioManager.playEatSound();
            spawnApples();
        }
    }

    public void grow2(int n) {
        if (n > 0) {
            pendingGrowth2 += n;
        } else if (n < 0) {
            bodyParts2 += n;
            if (bodyParts2 < 1) bodyParts2 = 1;
            if (pendingGrowth2 > 0) {
                pendingGrowth2 = Math.max(0, pendingGrowth2 + n);
            }
        }
    }

    public void addScore2(int n) {
        applesEaten2 += n;
    }

    @Override
    public void reset() {
        
        setBodyParts(3);
        setApplesEaten(0);
        setDirection('R');
        setPendingGrowth(0);
        int cols = getNumCols();
        int rows = getNumRows();
        for (int i = 0; i < getBodyParts(); i++) {
            getX()[i] = getBodyParts() - 1 - i;
            getY()[i] = 0;
        }

        bodyParts2 = 3;
        applesEaten2 = 0;
        direction2 = 'L';
        pendingGrowth2 = 0;
        for (int i = 0; i < bodyParts2; i++) {
            x2[i] = cols - bodyParts2 + i;
            y2[i] = rows - 1;
        }

        spawnApples();
    }
}