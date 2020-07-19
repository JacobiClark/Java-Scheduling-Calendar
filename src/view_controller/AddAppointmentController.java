 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import SQL.SQLQuery;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import utils.DBConnection;
import utils.Query;

/**
 * FXML Controller class
 *
 * @author Jacobi
 */
public class AddAppointmentController implements Initializable {
    private User loggedInUser;
    @FXML
    private Button AddAppointmentCancelButton;
    @FXML
    private Button AddAppointmentSaveButton;
    @FXML
    private TextField CustomerName;
    @FXML
    private TextField Date;
    @FXML
    private TextField StartTime;
    @FXML
    private TextField EndTime;
    @FXML
    private TextField MeetingType;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void addAppointmentSaveButtonPressed(ActionEvent event) throws SQLException, IOException {
        //Generate data to insert into db
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String customerName = CustomerName.getText();
        String date = Date.getText();
        String startTime = StartTime.getText();
        String endTime = EndTime.getText();
        int customerId = SQLQuery.retrieveCustomerId(customerName);
        String meetingType = MeetingType.getText();
        //Insert appointment into db
        if(validateAppointmentClientSide(customerName, date, startTime, endTime, meetingType)) {
            LocalDateTime startLDT = LocalDateTime.parse(date+" "+StartTime.getText(), formatter);
            LocalDateTime endLDT = LocalDateTime.parse(date+" "+EndTime.getText(), formatter);
            
        }
        
        /*SQLQuery.insertAppointment(customerId,loggedInUser.getUserId(),meetingType,startLDT,endLDT);
        try {
            ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
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
        }*/
    }
    
    private static boolean validateAppointmentClientSide(String customerName, String date, String startTime, String endTime, String meetingType) {
        return (isStringValid(customerName) && isDateValid(date) && areTimesValid(startTime, endTime) && isStringValid(meetingType));
    }

    public void addAppointmentCancelButtonPressed(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Add Appointment");
        alert.setContentText("Are you sure you want to cancel adding this Appointment?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
        }
    }
    
    public void setUser(User user) {
        loggedInUser = user;
    }
    
    private static boolean isStringValid(String str) {
        return (!str.isEmpty()
                && str.matches("^[\\p{L} .'-]+$"));
    }
    
    private static boolean isDateValid(String str) {
        return (!str.isEmpty()
                && str.matches("^\\d{4}-\\d{2}-\\d{2}$"));
    }
    
    private static boolean areTimesValid(String startTime, String endTime) {
        try {
            LocalTime startOfDay = LocalTime.of(8,00);
            LocalTime endOfDay = LocalTime.of(17,00);
            DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime startLocalTime = LocalTime.parse(startTime, tf);
            LocalTime endLocalTime = LocalTime.parse(endTime, tf);
            return(startLocalTime.compareTo(endLocalTime)<0
                    && startLocalTime.compareTo(startOfDay)>0
                    && endLocalTime.compareTo(endOfDay)<0);

        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return false;

    }
    
}
