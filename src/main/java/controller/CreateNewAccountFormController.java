package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    public  void initialize(){
        setLblVisibility(false);
        setDisableCommon(true);
    }
    public void registerBtnOnAction(ActionEvent actionEvent) {
        String newPassword = txtNewPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        if (newPassword.equals(confirmPassword)){

            setLblVisibility(false);
            setBorderColor("transparent");

        }else{
            setBorderColor("red");

            setLblVisibility(true);
            txtNewPassword.requestFocus();
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

}
