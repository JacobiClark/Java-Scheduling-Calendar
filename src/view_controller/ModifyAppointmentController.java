/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import SQL.SQLQuery;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointment;
import model.User;

/**
 * FXML Controller class
 *
 * @author Jacobi
 */
public class ModifyAppointmentController implements Initializable {
    private User loggedInUser;
    private Appointment appointmentToBeModified;
    @FXML
    private Button ModifyAppointmentCancelButton;
    @FXML
    private Button ModifyAppointmentSaveButton;
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
    
    public void setAppointmentToBeModified(Appointment appointment) {
        try {
            appointmentToBeModified = appointment;
            CustomerName.setText(appointmentToBeModified.getCustomerName());
            StartTime.setText(appointmentToBeModified.getStartDateTimeString());
            EndTime.setText(appointmentToBeModified.getEndDateTimeString());
            MeetingType.setText(appointmentToBeModified.getMeetingType());
        }
        catch (Exception e) {
            System.out.println(e.getMessage() + " unable to import appointment info :-(");
        }

    }

    @FXML
    private void ModifyAppointmentCancelButtonPressed(ActionEvent event) {

    }

    @FXML
    private void ModifyAppointmentSaveButtonPressed(ActionEvent event) throws SQLException, IOException {
        //Generate data to insert into db
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String customerName = CustomerName.getText();
        int customerId = SQLQuery.retrieveCustomerId(customerName);
        LocalDateTime startTime = LocalDateTime.parse(StartTime.getText(), formatter);
        LocalDateTime endTime = LocalDateTime.parse(EndTime.getText(), formatter);
        String meetingType = MeetingType.getText();
        //Insert appointment into db
        SQLQuery.modifyAppointment(appointmentToBeModified.getAppointmentID(), customerId, startTime, endTime, meetingType);
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
    }
    
    public void setUser(User user) {
        loggedInUser = user;
    }
    
}
