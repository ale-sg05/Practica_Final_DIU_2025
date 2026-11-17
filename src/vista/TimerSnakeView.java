package vista;
import java.awt.*;

import modelo.RankingEntry;
import modelo.RankingManager;
import modelo.TimerSnakeModel;

public class TimerSnakeView extends SnakeView {
    private TimerSnakeModel timerModel;

    public TimerSnakeView(TimerSnakeModel model, boolean daltonic) {
        super(model, daltonic);
        this.timerModel = model;
    }

    @Override
    protected void drawScore(Graphics g) {
        int y = 50;
        int time = Math.max(0, timerModel.getTimeLeft());
        int hours = time / 3600;
        int minutes = (time % 3600) / 60;
        int seconds = time % 60;
        String timeStr = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        int score = Math.max(0, timerModel.getApplesEaten());
        int bestScore = 0;
        java.util.List<RankingEntry> ranking = RankingManager.getRanking(RankingManager.Mode.TIMER);
        if (!ranking.isEmpty()) bestScore = ranking.get(0).score;

        Font font = new Font("Arial", Font.BOLD, 20);
        g.setFont(font);

        String scoreStr = "Score: " + score;
        String bestScoreStr = "Best: " + bestScore;

        int space = 25;
        int totalWidth = g.getFontMetrics().stringWidth(scoreStr)
                       + g.getFontMetrics().stringWidth(timeStr)
                       + g.getFontMetrics().stringWidth(bestScoreStr)
                       + 2 * space;

        int panelWidth = getWidth();
        int x = (panelWidth - totalWidth) / 2;

        g.setColor(Color.WHITE);
        g.drawString(scoreStr, x, y);

        x += g.getFontMetrics().stringWidth(scoreStr) + space;
        g.setColor(Color.YELLOW);
        g.drawString(timeStr, x, y);

        x += g.getFontMetrics().stringWidth(timeStr) + space;
        g.setColor(new Color(0xE0C13B));
        g.drawString(bestScoreStr, x, y);

        if (timerModel.shouldShowDelta()) {
            int delta = timerModel.getLastDelta();
            String deltaStr = (delta > 0 ? "+" : "") + delta;
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.setColor(delta > 0 ? Color.GREEN : Color.RED);
            int deltaX = (panelWidth - totalWidth) / 2
                       + g.getFontMetrics(font).stringWidth(scoreStr)
                       + space
                       + g.getFontMetrics(font).stringWidth(timeStr)
                       + 5;
            g.drawString("(" + deltaStr + ")", deltaX, y + 22);
        }
    }
}