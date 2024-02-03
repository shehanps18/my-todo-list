package controller;

import db.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tm.ToDoTm;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class ToDoFormController {
    public Label lblUserId;
    public Label lblBanner;
    public AnchorPane root;
    public AnchorPane subRoot;
    public TextField txtDescription;
    public ListView<ToDoTm> lstToDo;
    public TextField txtSelectedToDo;
    public Button btnUpdate;
    public Button btnDelete;
    public String selectedId = null ;

    public void initialize() {
        lblBanner.setText("Hi " + LoginFormController.loginUserName + " Welcome to To-Do-List");
        lblUserId.setText(LoginFormController.loginUserID);
        subRoot.setVisible(false);

        loadList();
        setDisableCommon(true);

        lstToDo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ToDoTm>() {
            @Override
            public void changed(ObservableValue<? extends ToDoTm> observableValue, ToDoTm toDoTm, ToDoTm t1) {
                setDisableCommon(false);
                subRoot.setVisible(false);
                ObservableList<ToDoTm> selectedItems = lstToDo.getSelectionModel().getSelectedItems();
                if (!selectedItems.isEmpty()) {
                    ToDoTm selectedItem = selectedItems.get(0);
                    txtSelectedToDo.setText(selectedItem.getDescription());
                    selectedId = selectedItem.getId();
                }else {
                    txtSelectedToDo.clear();
                    selectedId = null;
                }

            }
        });
    }
    public  void setDisableCommon(boolean isDisable){
        btnDelete.setDisable(isDisable);
        btnUpdate.setDisable(isDisable);
        txtSelectedToDo.setDisable(isDisable);
    }

    public void btnLogOutOnAction(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to logout ?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.get().equals(ButtonType.YES)) {
            Parent parent = FXMLLoader.load(this.getClass().getResource("/view/LoginForm.fxml"));

            Scene scene = new Scene(parent);
            Stage primaryStage = (Stage) root.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            primaryStage.setTitle("Login Form");
        }
    }

    public void btnAddNewToDoOnAction() {
        lstToDo.getSelectionModel().clearSelection();
        setDisableCommon(true);
        txtDescription.requestFocus();
        subRoot.setVisible(true);

    }


    public void btnAddToListOnAction() throws RuntimeException {

        String id = autoGenerateID();
        String description = txtDescription.getText();
        String userid = lblUserId.getText();

        Connection connection = DBConnection.getInstance().getConnection();


        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into todo values (?,?,?)");
            preparedStatement.setObject(1, id);
            preparedStatement.setObject(2, description);
            preparedStatement.setObject(3, userid);
            preparedStatement.executeUpdate();
            txtDescription.clear();
            subRoot.setVisible(false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public String autoGenerateID() {
        Connection connection = DBConnection.getInstance().getConnection();
        String todoID = null;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id FROM todo ORDER BY id DESC LIMIT 1");
            boolean isExist = resultSet.next();
            if (isExist) {
                todoID = resultSet.getString(1);
                todoID = todoID.substring(1, todoID.length());
                int intID = Integer.parseInt(todoID);
                intID++;
                if (intID < 10) {
                    todoID = "T00" + intID;
                } else if (intID < 100) {
                    todoID = "T0" + intID;
                } else {
                    todoID = "T" + intID;
                }
            } else {
                todoID = "T001";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);


        }
        return todoID;
    }
    public void loadList(){
        ObservableList<ToDoTm> toDoItems = lstToDo.getItems();
        toDoItems.clear();

        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from todo where userId = ?");
            preparedStatement.setObject(1,LoginFormController.loginUserID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString(1);
                String description = resultSet.getString(2);
                String user_id = resultSet.getString(3);
                ToDoTm object = new ToDoTm(id,description,user_id);

                toDoItems.add(object);
            }
            lstToDo.refresh();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String selectedText = txtSelectedToDo.getText();
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update todo set description = ? where id = ?");
            preparedStatement.setObject(1,selectedText);
            preparedStatement.setObject(2,selectedId);
            preparedStatement.executeUpdate();

            loadList();
            txtSelectedToDo.clear();

            setDisableCommon(true);
        }  catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do you want to Delete this...?",ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get().equals(ButtonType.YES)){
            Connection connection = DBConnection.getInstance().getConnection();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("delete from todo where id = ?");
                preparedStatement.setObject(1,selectedId);
                preparedStatement.executeUpdate();
                loadList();
                txtSelectedToDo.clear();
                setDisableCommon(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


