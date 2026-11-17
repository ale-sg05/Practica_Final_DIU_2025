package modelo;
public class TimerSnakeModel extends SnakeModel {
    private int timeLeft = 20; // segundos
    private long lastTick = System.currentTimeMillis();
    private int lastDelta = 0;
    private long lastDeltaTime = 0;

    public TimerSnakeModel(int numCasillas) {
        super(numCasillas);
    }

    public void tickTimer() {
        long now = System.currentTimeMillis();
        if (now - lastTick >= 1000) {
            timeLeft--;
            lastTick = now;
        }
    }

    public void addTime(int seconds) {
        timeLeft += seconds;
        lastDelta = seconds;
        lastDeltaTime = System.currentTimeMillis();
    }

    public int getTimeLeft() { return timeLeft; }
    public int getLastDelta() { return lastDelta; }
    public boolean shouldShowDelta() {
        return System.currentTimeMillis() - lastDeltaTime < 1200 && lastDelta != 0;
    }
    public void clearDelta() { lastDelta = 0; }

    @Override
    public boolean checkApple() {
        boolean ate = false;
        if (apple != null && x[0] == apple.x && y[0] == apple.y) {
            applesEaten++;
            pendingGrowth += 1;
            spawnApple();
            addTime(2); // Manzana normal: +2s
            ate = true;
        } else if (specialApple1 != null && x[0] == specialApple1.x && y[0] == specialApple1.y) {
            applesEaten += 5;
            pendingGrowth += 5;
            specialApple1 = null;
            addTime(5); // dorada: +5s
            ate = true;
        } else if (specialApple2 != null && x[0] == specialApple2.x && y[0] == specialApple2.y) {
            applesEaten = Math.max(0, applesEaten - 2);
            int newBodyParts = Math.max(1, getBodyParts() - 2);
            setBodyParts(newBodyParts);
            pendingGrowth = Math.max(0, pendingGrowth - 2);
            specialApple2 = null;
            addTime(-2); // Verde: -2s
            ate = true;
        } else if (specialApple0 != null && x[0] == specialApple0.x && y[0] == specialApple0.y) {
            applesEaten += 3;
            pendingGrowth += 3;
            specialApple0 = null;
            addTime(3); // roja: +3s
            ate = true;
        }
        if (ate) {
            spawnApples();
        }
        return ate;
    }
}