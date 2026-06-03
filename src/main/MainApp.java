package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Nạp file giao diện FXML bạn vừa thiết kế từ Scene Builder
        Parent root = FXMLLoader.load(getClass().getResource("/res/view/MainMenu.fxml"));

        primaryStage.setTitle("Harry Potter and the Philosopher's Stone");

        // Tạo Scene với kích thước khớp với Scene Builder
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.setResizable(false); // Cố định cửa sổ game không cho co giãn bừa bãi
        primaryStage.show();
    }

    public static void main(String[] args) {
        // THÊM 2 DÒNG NÀY VÀO TRƯỚC HÀM launch(args)
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.verbose", "true"); // Dòng này để in chi tiết lỗi đồ họa nếu có
        launch(args);
    }
}