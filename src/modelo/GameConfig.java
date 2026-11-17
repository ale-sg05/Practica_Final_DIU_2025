package modelo;
public class GameConfig {
    public enum DaltonicMode { NONE, PROTANOPIA, DEUTERANOPIA, TRITANOPIA }
    private static DaltonicMode daltonicMode = DaltonicMode.NONE;
    private static int speed = 100;
    private static int numCasillas = 24;
    private static int skinIndex = 0;

    public static int getSpeed() { return speed; }
    public static void setSpeed(int s) { speed = s; }

    public static int getNumCasillas() { return numCasillas; }
    public static void setNumCasillas(int n) { numCasillas = n; }

    public static DaltonicMode getDaltonicMode() {
        return daltonicMode;
    }

    public static void setDaltonicMode(DaltonicMode mode) {
        daltonicMode = mode;
    }

    public static boolean isDaltonic() {
        return daltonicMode != DaltonicMode.NONE;
    }

    public static int getSkinIndex() { return skinIndex; }
    public static void setSkinIndex(int idx) { skinIndex = idx; }
}