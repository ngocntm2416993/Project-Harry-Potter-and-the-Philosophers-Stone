package UI;

import javafx.application.Application;
import javafx.application.Platform; // Bổ sung import này
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Platform.setImplicitExit(false);

        // Nạp file giao diện FXML từ Scene Builder
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));

        primaryStage.setTitle("Harry Potter and the Philosopher's Stone");

        // Tạo Scene với kích thước khớp với Scene Builder
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.setResizable(false); // Cố định cửa sổ game không cho co giãn bừa bãi

        // Khi người chơi bấm nút X màu đỏ ở góc MainMenu để THOÁT GAME HOÀN TOÀN,
        // ta mới chủ động đóng toàn bộ tiến trình chạy ngầm để giải phóng RAM/CPU hoàn toàn.
        primaryStage.setOnCloseRequest(windowEvent -> {
            Platform.exit();
            System.exit(0);
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.forceGPU", "false");
        launch(args);
    }
}