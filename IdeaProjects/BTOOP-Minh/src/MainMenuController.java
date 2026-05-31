import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javax.swing.JFrame;
import main.GamePanel; // Import class quản lý logic game 2D của bạn

public class MainMenuController {

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
            window.setTitle("Treasure Hunting"); // Tên game của bạn

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
        System.out.println("Setting Clicked!");
    }

    @FXML
    void onQuitClicked(ActionEvent event) {
        System.out.println("Exit game!");
        Platform.exit();
    }
}