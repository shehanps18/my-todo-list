package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class ToDoFormController {
    public Label lblUserId;
    public Label lblBanner;
    public AnchorPane root;
    public AnchorPane subRoot;
    public TextField txtDescription;

    public void initialize(){
        lblBanner.setText("Hi "+LoginFormController.loginUserName+" Welcome to To-Do-List");
        lblUserId.setText(LoginFormController.loginUserID);
        subRoot.setVisible(false);
    }

    public void btnLogOutOnAction(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do you wnat to logout ?",ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.get().equals(ButtonType.YES)){
            Parent parent = FXMLLoader.load(this.getClass().getResource("/view/LoginForm.fxml"));

            Scene scene = new Scene(parent);
            Stage primaryStage = (Stage) root.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            primaryStage.setTitle("Login Form");
        }
    }

    public void btnAddNewToDoOnAction(ActionEvent actionEvent) {
        subRoot.setVisible(true);
        txtDescription.requestFocus();
    }


    public void btnAddToListOnAction(ActionEvent actionEvent) {
    }
}

