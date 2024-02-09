package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class CreateNewAccountFormController {
    public PasswordField txtNewPassword;
    public PasswordField txtConfirmPassword;
    public Label lblPasswordNotMatch1;
    public Label lblPasswordNotMatch2;
    public TextField txtUserName;
    public TextField txtEmail;
    public Button btnRegister;
    public Label lblId;
    public Button btnAddNewUser;
    public AnchorPane   root1;

    public  void initialize(){
        setLblVisibility(false);
        setDisableCommon(true);
    }
    public void registerBtnOnAction(ActionEvent actionEvent) {

        if (txtUserName.getText().trim().isEmpty()){
            txtUserName.requestFocus();
        } else if (txtEmail.getText().trim().isEmpty()) {
            txtEmail.requestFocus();
        } else if (txtNewPassword.getText().trim().isEmpty()) {
            txtNewPassword.requestFocus();
        } else if (txtConfirmPassword.getText().trim().isEmpty()) {
            txtConfirmPassword.requestFocus();
        }else{
            String newPassword = txtNewPassword.getText();
            String confirmPassword = txtConfirmPassword.getText();

            if (newPassword.equals(confirmPassword)){

                setLblVisibility(false);
                setBorderColor("transparent");

                register();

            }else{
                setBorderColor("red");

                setLblVisibility(true);
                txtNewPassword.requestFocus();
            }

        }




    }
    public  void setBorderColor(String color){
        txtNewPassword.setStyle("-fx-border-color: "+color);
        txtConfirmPassword.setStyle("-fx-border-color: "+ color);
    }
    public void setLblVisibility(boolean isVisible){
        lblPasswordNotMatch1.setVisible(isVisible);
        lblPasswordNotMatch2.setVisible(isVisible);
    }
    public void btnAddNewUserOnAction(ActionEvent actionEvent) {

        setDisableCommon(false);
        txtUserName.requestFocus();
        lblId.setVisible(true);
        //Connection connection = DBConnection.getInstance().getConnection();
        autoGenerateID();
    }
    public void setDisableCommon(boolean isDisable){
        txtUserName.setDisable(isDisable);
        txtEmail.setDisable(isDisable);
        txtNewPassword.setDisable(isDisable);
        txtConfirmPassword.setDisable(isDisable);
    }
    public void autoGenerateID(){
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id FROM user ORDER BY id DESC LIMIT 1");
            boolean isExist = resultSet.next();
            if (isExist){
                String userID = resultSet.getString(1);
                userID = userID.substring(1, userID.length());
                int intID = Integer.parseInt(userID);
                intID++;
                if (intID<10){
                    lblId.setText("U00"+intID);
                } else if (intID<100) {
                    lblId.setText("U0"+intID);
                }else{
                    lblId.setText("U"+intID);
                }
            }else{
                lblId.setText("U001");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void register(){
        Connection connection = DBConnection.getInstance().getConnection();
        String id = lblId.getText();
        String userName = txtUserName.getText();
        String email = txtEmail.getText();
        String password = txtConfirmPassword.getText();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user VALUES (?,?,?,?)");
            preparedStatement.setObject(1, id);
            preparedStatement.setObject(2,userName);
            preparedStatement.setObject(3,email);
            preparedStatement.setObject(4,password);

            int i = preparedStatement.executeUpdate();
            if (i!= 0){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Success...!");
                alert.showAndWait();
                Parent parent = FXMLLoader.load(this.getClass().getResource("/view/LoginForm.fxml"));
                Scene scene = new Scene(parent);

                Stage primaryStage = (Stage) root1.getScene().getWindow();
                primaryStage.setScene(scene);
                primaryStage.setTitle("Login Form");
                primaryStage.centerOnScreen();
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
