package util;
import modelo.AudioConfig;

public class AudioManager {
    private static final MusicPlayer gameMusic = new MusicPlayer();
    private static final MusicPlayer menuMusic = new MusicPlayer();
    private static final MusicPlayer gameOverEffect = new MusicPlayer();
    private static final MusicPlayer eatEffect = new MusicPlayer();

    public static void playMenuMusic() {
        if (!AudioConfig.muteAll && !AudioConfig.muteMusic)
            menuMusic.play("audio/menu.wav", true, true);
    }

    public static void stopMenuMusic() {
        menuMusic.stop();
    }

    public static void playGameMusic(int speed) {
        if (AudioConfig.muteAll || AudioConfig.muteMusic) return;
        String track = switch (speed) {
            case 200 -> "audio/lenta.wav";
            case 100 -> "audio/moderada.wav";
            default -> "audio/rapidovss.wav";
        };
        gameMusic.play(track, true, true);
    }

    public static void pauseGameMusic() {
        gameMusic.pause();
    }

    public static void resumeGameMusic(int speed) {
        playGameMusic(speed);
    }

    public static void stopGameMusic() {
        gameMusic.stop();
    }

    public static void playGameOverSound() {
        if (!AudioConfig.muteAll && !AudioConfig.muteEffects)
            gameOverEffect.play("audio/gameover.wav", false, false);
    }

    public static void playEatSound() {
        if (!AudioConfig.muteAll && !AudioConfig.muteEffects)
            eatEffect.play("audio/eat.wav", false, false);
    }
}