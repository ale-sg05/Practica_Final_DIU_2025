package modelo;
import java.io.Serializable;

public class RankingEntry implements Serializable {
    public final int score;
    public final long timestamp;

    public RankingEntry(int score) {
        this.score = score;
        this.timestamp = System.currentTimeMillis();
    }
}