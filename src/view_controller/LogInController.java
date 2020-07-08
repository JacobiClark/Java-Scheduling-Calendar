/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import SQL.SQLQuery;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DBConnection;
import utils.Query;
import model.User;
import view_controller.MainController;

/**
 * FXML Controller class
 *
 * @author Jacobi
 */
public class LogInController implements Initializable {

    @FXML
    private TextField userNameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button logInButton;
    String invalidCredentialsHeaderText = "Authentication failure";
    String invalidCredentialsContentText = "Incorrect Username or Password";
    Locale locale = Locale.getDefault();
    String systemLanguage = locale.getDisplayLanguage();


    @FXML
    private void logInButtonPushed(ActionEvent event) throws SQLException {
        String userName = userNameField.getText();
        String password = passwordField.getText();
        //Initiate connection to database
               /* Connection conn = DBConnection.startConnection();
        Query.setStatement(conn);     
        Statement statement = Query.getStatement();        
        String selectStatement = "SELECT * FROM user WHERE userName='" + userName + "' AND password='" + password + "'"; 
        statement.execute(selectStatement);
        ResultSet rs = statement.getResultSet();*/
        if (SQLQuery.authenticate(userName, password)) {
            // Do successful authentication handling
            User loggedInUser = SQLQuery.createUser(userName);
            try {
                ((Node) (event.getSource())).getScene().getWindow().hide();
                FXMLLoader loader=new FXMLLoader(getClass().getResource("Main.fxml"));
                Parent root = (Parent) loader.load();
                MainController mainController=loader.getController();
                mainController.setLoggedInUser(loggedInUser);
                Stage stage=new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setHeaderText(invalidCredentialsHeaderText);
            errorAlert.setContentText(invalidCredentialsContentText);
            errorAlert.showAndWait();
        }

    }

    public void autoLogIn() {
            userNameField.setText("test");
            passwordField.setText("test");
        }
    /**
     * Initializes the controller class.
     */
    @Override

    public void initialize(URL url, ResourceBundle rb) {
        if (systemLanguage.equals("French")) {
            userNameField.setText("Nom d'utilisateur");
            passwordField.setText("mot de passe");
        }
        if (systemLanguage.equals("German")) {
            userNameField.setText("Nutzername");
            passwordField.setText("Passwor");
        }
        autoLogIn();
    }   
    
}
