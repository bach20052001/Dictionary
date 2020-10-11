package sample;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DeleteController {

    @FXML
    private Button deleteButton;

    @FXML
    private Button closeButton;

    @FXML
    private TextArea word;

    public void run() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("DeleteView.fxml"));
            Scene scene = new Scene(root);
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Xóa từ");
            window.setScene(scene);
            window.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteButtonClicked() {
        if(word.getText().length() == 0) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cảnh báo");
            alert.setHeaderText(null);
            alert.setContentText("Bạn chưa nhập từ. Vui lòng kiểm tra lại!");
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
            Controller.manage.deleteWord(word.getText(), null);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Xóa từ thành công!");
            alert.showAndWait();
            Stage stage = (Stage) deleteButton.getScene().getWindow();
            stage.close();
        }
    }

    public void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

}