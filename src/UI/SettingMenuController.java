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

    @FXML private Slider    sliderMusic;
    @FXML private Slider    sliderSE;
    @FXML private Button    btnBack;
    @FXML private ImageView imgBarFullMusic;
    @FXML private ImageView imgBarFullSE;

    private final Rectangle clipMusic = new Rectangle();
    private final Rectangle clipSE    = new Rectangle();

    private Runnable onBack = null;
    public void setOnBack(Runnable onBack) { this.onBack = onBack; }

    @FXML
    public void initialize() {
        setupSlider(sliderMusic, imgBarFullMusic, clipMusic, true);
        setupSlider(sliderSE,    imgBarFullSE,    clipSE,    false);
    }

    private void setupSlider(Slider slider, ImageView imgBarFull,
                             Rectangle clip, boolean isMusic) {
        if (slider == null || imgBarFull == null) return;

        imgBarFull.setClip(clip);

        // 1. Đồng bộ chính xác giá trị từ UI hệ thống Swing (0 -> 100) vào Slider JavaFX
        main.GamePanel gp = main.GamePanelHolder.instance;
        if (gp != null && gp.ui != null) {
            slider.setValue(isMusic ? gp.ui.musicVolume : gp.ui.seVolume);
        }

        // 2. Lắng nghe thay đổi → Đồng bộ song song cả biến UI (int) lẫn thực thể âm thanh (float)
        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateBarFull(slider, clip);
            main.GamePanel gp2 = main.GamePanelHolder.instance;
            if (gp2 != null) {
                if (isMusic) {
                    gp2.ui.musicVolume = newVal.intValue(); // Đồng bộ biến hiển thị Swing
                    gp2.music.setVolume(newVal.floatValue()); // Cập nhật âm thanh thực tế
                } else {
                    gp2.ui.seVolume = newVal.intValue(); // Đồng bộ biến hiển thị Swing
                    gp2.se.setVolume(newVal.floatValue()); // Cập nhật âm thanh thực tế
                }
            }
        });

        // 3. Căn vị trí imgBarFull bằng cơ chế Luồng đồ họa JavaFX (Giữ nguyên - Đang chạy rất tốt)
        Platform.runLater(() -> {
            slider.applyCss();
            slider.layout();
            javafx.scene.Node track = slider.lookup(".track");
            if (track != null) {
                javafx.geometry.Bounds ts = track.localToScene(track.getBoundsInLocal());
                javafx.geometry.Bounds ps = imgBarFull.getParent()
                        .localToScene(imgBarFull.getParent().getBoundsInLocal());
                imgBarFull.setLayoutX(ts.getMinX() - ps.getMinX());
                imgBarFull.setLayoutY(ts.getMinY() - ps.getMinY());
                imgBarFull.setFitWidth(ts.getWidth());
                imgBarFull.setFitHeight(ts.getHeight());
                clip.setHeight(ts.getHeight());
                slider.setUserData(ts.getWidth());
            }
            updateBarFull(slider, clip);
        });
    }

    private void updateBarFull(Slider slider, Rectangle clip) {
        if (slider.getUserData() == null) return;
        double trackW  = (double) slider.getUserData();
        double percent = (slider.getValue() - slider.getMin())
                / (slider.getMax() - slider.getMin());
        clip.setWidth(trackW * percent);
    }

    @FXML
    void onBackClicked(ActionEvent event) {
        if (onBack != null) {
            onBack.run();
            return;
        }
        Stage stage = (Stage) btnBack.getScene().getWindow();
        if (stage.getStyle() == javafx.stage.StageStyle.UNDECORATED) {
            stage.close();
        } else {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                stage.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}