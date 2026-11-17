package modelo;
import java.util.*;
import java.io.*;

public class RankingManager {
    public enum Mode { CLASSIC, INFINITE, TIMER, MULTI }
    private static final int MAX_ENTRIES = 10;
    private static final String FILE_PREFIX = "ranking_";
    private static final String FILE_SUFFIX = ".dat";

    private static final Map<Mode, List<RankingEntry>> rankings = new EnumMap<>(Mode.class);

    static {
        for (Mode mode : Mode.values()) {
            rankings.put(mode, loadRanking(mode));
        }
    }

    public static List<RankingEntry> getRanking(Mode mode) {
        return rankings.get(mode);
    }

    public static void addScore(Mode mode, int score) {
        List<RankingEntry> list = rankings.get(mode);
        list.add(new RankingEntry(score));
        list.sort((a, b) -> Integer.compare(b.score, a.score));
        if (list.size() > MAX_ENTRIES) list.subList(MAX_ENTRIES, list.size()).clear();
        saveRanking(mode, list);
    }

    private static List<RankingEntry> loadRanking(Mode mode) {
        String file = FILE_PREFIX + mode.name().toLowerCase() + FILE_SUFFIX;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (List<RankingEntry>) in.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private static void saveRanking(Mode mode, List<RankingEntry> list) {
        String file = FILE_PREFIX + mode.name().toLowerCase() + FILE_SUFFIX;
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(list);
        } catch (Exception e) {
            
        }
    }

    public static int getMaxEntries() {
        return MAX_ENTRIES;
    }
}