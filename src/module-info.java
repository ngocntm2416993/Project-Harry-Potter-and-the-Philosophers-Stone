module TreasureHunting {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    exports UI;
    exports main;
    opens UI to javafx.fxml;

    // Đảm bảo mở quyền truy cập cho thư mục fxml nếu hệ thống yêu cầu
    opens view to javafx.fxml;
}