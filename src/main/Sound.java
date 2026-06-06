package main;

import java.net.URL;
import javax.sound.sampled.*;

public class Sound {
    Clip clip;
    URL soundURL[] = new URL[30];
    private float volumePercent = 100f;

    // Cache: mỗi index có 1 Clip đã load sẵn, không load lại mỗi lần play
    private Clip[] cachedClips = new Clip[30];

    /**
     * Load trước tất cả sound effect vào cache.
     * Gọi một lần duy nhất sau khi gán xong soundURL[].
     */
    public void preloadAll() {
        for (int i = 0; i < soundURL.length; i++) {
            if (soundURL[i] != null) {
                cachedClips[i] = loadClip(i);
            }
        }
    }

    private Clip loadClip(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            Clip c = AudioSystem.getClip();
            c.open(ais);
            applyVolumeToClip(c);
            return c;
        } catch (Exception e) {
            System.err.println("Sound: lỗi load file index " + i);
            e.printStackTrace();
            return null;
        }
    }

    /** Dùng cho music: load + giữ clip hiện tại (hành vi cũ) */
    public void setFile(int i) {
        if (i < 0 || i >= soundURL.length || soundURL[i] == null) {
            System.err.println("Sound: không tìm thấy soundURL[" + i + "]");
            return;
        }
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

    /**
     * Play sound effect từ cache — KHÔNG tạo Clip mới, không block.
     * Gọi thay cho setFile(i) + play() khi phát SE.
     */
    public void playCached(int i) {
        if (i < 0 || i >= cachedClips.length) return;
        Clip c = cachedClips[i];
        if (c == null) return;
        // Nếu đang phát thì tua về đầu để phát lại ngay
        if (c.isRunning()) c.stop();
        c.setFramePosition(0);
        applyVolumeToClip(c);
        c.start();
    }

    public void play() {
        if (clip != null) clip.start();
    }

    public void loop() {
        if (clip != null) clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        if (clip != null) clip.stop();
    }

    public void setVolume(float percent) {
        this.volumePercent = Math.max(0f, Math.min(100f, percent));
        applyVolume();
        // Cập nhật volume cho tất cả cached clip
        for (Clip c : cachedClips) {
            if (c != null) applyVolumeToClip(c);
        }
    }

    public float getVolume() { return volumePercent; }

    private void applyVolume() {
        if (clip != null) applyVolumeToClip(clip);
    }

    private void applyVolumeToClip(Clip c) {
        if (c == null) return;
        if (!c.isControlSupported(FloatControl.Type.MASTER_GAIN)) return;
        FloatControl gain = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
        float min = gain.getMinimum();
        float max = gain.getMaximum();
        float val;
        if (volumePercent <= 0f) {
            val = min;
        } else {
            val = min + (max - min) * (float)(Math.log10(1 + volumePercent * 9 / 100.0) / Math.log10(10));
        }
        gain.setValue(val);
    }
}
