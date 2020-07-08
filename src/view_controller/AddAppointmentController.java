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
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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
    Integer userId;

    @FXML
    private Button AddAppointmentCancelButton;
    @FXML
    private Button AddAppointmentSaveButton;
    @FXML
    private TextField CustomerName;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String customerName = CustomerName.getText();
        int customerId = SQLQuery.retrieveCustomerId(customerName);
        LocalDateTime startTime = LocalDateTime.parse(StartTime.getText(), formatter);
        LocalDateTime endTime = LocalDateTime.parse(EndTime.getText(), formatter);
        String meetingType = MeetingType.getText();
        //Insert appointment into db
        SQLQuery.insertAppointment(customerId,userId,meetingType,startTime,endTime);
        //close add appointment screen and go back to main
        FXMLLoader loader=new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root = (Parent) loader.load();
        MainController mainController=loader.getController();
        mainController.populateAppointmentsTable();
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
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

    
    public void setUserId(User user) {
        userId = user.getUserId();
    }
    
}
