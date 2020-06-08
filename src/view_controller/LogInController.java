/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import utils.DBConnection;
import utils.Query;
import model.User;

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
    
    String OutOfBoundsException = "IndexOutOfBoundsException: ";
    Locale locale = Locale.getDefault();
    String systemLanguage = locale.getDisplayLanguage();

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
            passwordField.setText("Passwort");
            }
    }   

    @FXML
    private void logInButtonPushed(ActionEvent event) throws SQLException {
        String userName = userNameField.getText();
        String password = passwordField.getText();
        //Initiate connection to database
        Connection conn = DBConnection.startConnection();
        Query.setStatement(conn);     
        Statement statement = Query.getStatement();        
        String selectStatement = "SELECT * FROM user WHERE userName='" + userName + "' AND password='" + password + "'"; 
        statement.execute(selectStatement);
        ResultSet rs = statement.getResultSet();
        int Id;
        String user;
        String pass;
        boolean active;
        LocalDateTime createDate;
        String createdBy;
        LocalDateTime lastUpdate;
        String lastUpdateBy;
        try {
            rs.next();
            User loggedInUser = new User(
                rs.getInt("userId"),
                rs.getString("userName"),
                rs.getString("password"),
                rs.getBoolean("active"),
                rs.getTimestamp("createDate").toLocalDateTime(),
                rs.getString("createdBy"),
                rs.getTimestamp("lastUpdate").toLocalDateTime(),
                rs.getString("lastUpdateBy")
                );
            System.out.println("Credentials valid");
        }
        catch (Exception e) {
            System.err.println(OutOfBoundsException + e.getMessage());
        }

    }
    
}
