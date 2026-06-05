package main;

import java.net.URL;
import javax.sound.sampled.*;

public class Sound {
    Clip clip;
    URL soundURL[] = new URL[30];
    private float volumePercent = 100f; // 0 → 100

    public void setFile(int i) {
        // Null-check index
        if (i < 0 || i >= soundURL.length || soundURL[i] == null) {
            System.err.println("Sound: không tìm thấy soundURL[" + i + "]");
            return;
        }
        // Đóng clip cũ trước khi mở mới
        if (clip != null && clip.isOpen()) {
            clip.stop();
            clip.close();
        }
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            applyVolume();
        } catch (Exception e) {
            System.err.println("Sound: lỗi load file index " + i);
            e.printStackTrace();
        }
    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }

    /**
     * Đặt âm lượng: percent từ 0.0 (tắt) đến 100.0 (max)
     */
    public void setVolume(float percent) {
        this.volumePercent = Math.max(0f, Math.min(100f, percent));
        applyVolume();
    }

    public float getVolume() {
        return volumePercent;
    }

    private void applyVolume() {
        if (clip == null) return;
        if (!clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) return;

        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        // MASTER_GAIN tính bằng decibel: min thường -80dB, max 6dB
        float min = gainControl.getMinimum(); // thường -80.0
        float max = gainControl.getMaximum(); // thường 6.0

        float gain;
        if (volumePercent <= 0f) {
            gain = min; // tắt hoàn toàn
        } else {
            // Convert percent → dB theo logarithm tự nhiên
            gain = min + (max - min) * (float)(Math.log10(1 + volumePercent * 9 / 100) / Math.log10(10));
        }

        gainControl.setValue(gain);
    }
}