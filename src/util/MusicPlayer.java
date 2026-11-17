package util;
import javax.sound.sampled.*;

import modelo.AudioConfig;

import java.io.File;

public class MusicPlayer {
    private Clip clip;
    private AudioInputStream audioIn;
    private String loadedFile = null;

    public void play(String filePath, boolean loop, boolean isMusic) {
        if (AudioConfig.muteAll) return;
        if (isMusic && AudioConfig.muteMusic) return;
        if (!isMusic && AudioConfig.muteEffects) return;

        try {
            if (clip == null || !filePath.equals(loadedFile)) {
                stop();
                audioIn = AudioSystem.getAudioInputStream(new File(filePath));
                clip = AudioSystem.getClip();
                clip.open(audioIn);
                loadedFile = filePath;
            } else {
                clip.setFramePosition(0);
            }
            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.start();
            }
        } catch (Exception e) {
            System.err.println("No se pudo reproducir el audio: " + filePath);
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
            loadedFile = null;
        }
        if (audioIn != null) {
            try { audioIn.close(); } catch (Exception ignored) {}
            audioIn = null;
        }
    }

    public void pause() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void resume() {
        if (clip != null && !clip.isRunning()) {
            clip.start();
        }
    }
}