package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFormController {
    public AnchorPane root;

    public void loginBtnOnAction(MouseEvent mouseEvent) {
    }

    public void lblCreateNewAccountOnMouseClick(MouseEvent mouseEvent) throws IOException {
        Parent parent = FXMLLoader.load(this.getClass().getResource("/view/CreateNewAccountForm.fxml"));
        Scene scene = new Scene(parent);

        Stage primaryStage = (Stage) root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Register Form");
        primaryStage.centerOnScreen();
    }
}
