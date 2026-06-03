package UI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.io.IOException;

public class SettingMenuController {

    public enum BackTarget { MAIN_MENU, PAUSE }

    @FXML private Slider    sliderMusic;
    @FXML private Button    btnBack;
    @FXML private ImageView imgBarFull;

    private final Rectangle clip = new Rectangle();

    private BackTarget backTarget = BackTarget.MAIN_MENU;
    private Stage      pauseStage = null;
    private Runnable   onResume   = null;

    // Callback đơn giản: gọi khi back — do bên ngoài truyền vào
    private Runnable onBack = null;

    public void setOnBack(Runnable onBack) {
        this.onBack = onBack;
    }

    public void setBackTarget(BackTarget target, Stage stage, Runnable onResume) {
        this.backTarget = target;
        this.pauseStage = stage;
        this.onResume   = onResume;
    }

    @FXML
    public void initialize() {
        if (sliderMusic == null || imgBarFull == null) return;
        imgBarFull.setClip(clip);
        sliderMusic.valueProperty().addListener((obs, o, n) -> updateBarFull());
        Platform.runLater(() -> {
            sliderMusic.applyCss();
            sliderMusic.layout();
            javafx.scene.Node track = sliderMusic.lookup(".track");
            if (track != null) {
                javafx.geometry.Bounds ts = track.localToScene(track.getBoundsInLocal());
                javafx.geometry.Bounds ps = imgBarFull.getParent()
                        .localToScene(imgBarFull.getParent().getBoundsInLocal());
                imgBarFull.setLayoutX(ts.getMinX() - ps.getMinX());
                imgBarFull.setLayoutY(ts.getMinY() - ps.getMinY());
                imgBarFull.setFitWidth(ts.getWidth());
                imgBarFull.setFitHeight(ts.getHeight());
                clip.setHeight(ts.getHeight());
                sliderMusic.setUserData(ts.getWidth());
            }
            updateBarFull();
        });
    }

    private void updateBarFull() {
        if (sliderMusic.getUserData() == null) return;
        double trackW  = (double) sliderMusic.getUserData();
        double percent = (sliderMusic.getValue() - sliderMusic.getMin())
                / (sliderMusic.getMax() - sliderMusic.getMin());
        clip.setWidth(trackW * percent);
    }

    @FXML
    void onBackClicked(ActionEvent event) {
        Stage stage = (Stage) btnBack.getScene().getWindow();

    /* Mẹo kiểm tra: Nếu Stage này ở trạng thái KHÔNG CÓ VIỀN (UNDECORATED)
       thì chứng tỏ nó được mở từ màn hình Pause Game của Swing.
       Lúc này ta chỉ cần đóng nó đi!
    */
        if (stage.getStyle() == javafx.stage.StageStyle.UNDECORATED) {
            stage.close();
        } else {
            // Ngược lại, nếu mở từ MainMenu, ta chuyển cảnh về MainMenu bình thường
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                stage.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}