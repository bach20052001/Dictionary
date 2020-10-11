package sample;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ModifyController {

    @FXML
    private TextArea word;

    @FXML
    private TextArea mean;

    @FXML
    private Button modifyButton;

    @FXML
    private Button closeButton;

    public void run() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("ModifyView.fxml"));
            Scene scene = new Scene(root);
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Sửa từ");
            window.setScene(scene);
            window.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void modifyButtonClicked() {
        if(word.getText().length() == 0) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cảnh báo");
            alert.setHeaderText(null);
            alert.setContentText("Bạn chưa nhập từ. Vui lòng kiểm tra lại!");
            alert.showAndWait();
        }
        else if(mean.getText().length() == 0) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cảnh báo");
            alert.setHeaderText(null);
            alert.setContentText("Bạn chưa nhập nghĩa. Vui lòng kiểm tra lại!");
            alert.showAndWait();
        }
        else if(!Controller.contains(word.getText())) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cảnh báo");
            alert.setHeaderText(null);
            alert.setContentText("Từ bạn nhập không có trong từ điển!");
            alert.showAndWait();
        }
        else {
            Controller.isChanged = true;
            Controller.manage.modifyWord(word.getText(), mean.getText());
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Sửa từ thành công!");
            alert.showAndWait();
            Stage stage = (Stage) modifyButton.getScene().getWindow();
            stage.close();
        }
    }

    public void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

}