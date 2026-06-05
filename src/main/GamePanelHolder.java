package main;

/**
 * Lưu instance GamePanel đang chạy để các class khác (SettingMenuController)
 * có thể truy cập mà không cần truyền tham số qua nhiều lớp.
 */
public class GamePanelHolder {
    public static GamePanel instance = null;
}