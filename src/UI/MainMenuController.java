package UI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javax.swing.JFrame;
import main.GamePanel; // Import class quản lý logic game 2D của bạn

public class gMainMenuController {

    @FXML
    private Button btnStart;

    @FXML
    private Button btnSetting;

    @FXML
    private Button btnQuit;

    @FXML
    void onStartClicked(ActionEvent event) {
        System.out.println("Loading game...");


        // 1. Tắt cửa sổ Menu JavaFX hiện tại
        Stage stage = (Stage) btnStart.getScene().getWindow();
        stage.close();

        // 2. Tạo cửa sổ JFrame mới và nhúng GamePanel vào (Giống hệt cách làm trong main.Main.java gốc)
        Platform.runLater(() -> {
            JFrame window = new JFrame();
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(false);
            window.setTitle("Harry Potter and the Philosopher's Stone"); // Tên game của bạn

            GamePanel gamePanel = new GamePanel();
            window.add(gamePanel);
            window.pack(); // Khớp kích thước cửa sổ theo cấu hình của GamePanel

            window.setLocationRelativeTo(null);
            window.setVisible(true);

            gamePanel.setUpGame();       // Khởi tạo thực thể, vật phẩm, NPC
            gamePanel.startGameThread(); // Kích hoạt vòng lặp game chạy (Game Loop)
        });
    }

    @FXML
    void onSettingClicked(ActionEvent event) {
        System.out.println("Opening Setting Menu...");
        try {
            // Lấy Stage hiện tại
            Stage stage = (Stage) btnSetting.getScene().getWindow();

            // Nạp giao diện SettingMenu từ thư mục tài nguyên res/view
            Parent root = FXMLLoader.load(getClass().getResource("/view/SettingMenu.fxml"));

            // Thay đổi nội dung hiển thị của cửa sổ sang giao diện Setting
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            System.out.println("Không thể mở giao diện Setting!");
            e.printStackTrace();
        }
    }

    @FXML
    void onQuitClicked(ActionEvent event) {
        System.out.println("Exit game!");
        Platform.exit();
    }
}