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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
    private Button AddAppointmentCancelButton;
    @FXML
    private Button ModifyAppointmentSaveButton;
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

    public void setAppointmentToBeModified(Appointment appointment) {
        try {
            appointmentToBeModified = appointment;
            CustomerName.setText(appointmentToBeModified.getCustomerName());
            Date.setText(appointmentToBeModified.getStartDate());
            StartTime.setText(appointmentToBeModified.getStartTime().toString());
            EndTime.setText(appointmentToBeModified.getEndTime().toString());
            MeetingType.setText(appointmentToBeModified.getMeetingType());
        } catch (Exception e) {
            System.out.println(e.getMessage() + " unable to import appointment info :-(");
        }

    }

    public void setUser(User user) {
        loggedInUser = user;
    }

    @FXML
    private void addAppointmentCancelButtonPressed(ActionEvent event) throws IOException, SQLException {
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root = (Parent) loader.load();
        MainController mainController = loader.getController();
        mainController.setLoggedInUser(loggedInUser);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private static boolean validateAppointmentClientSide(String customerName, String date, String startTime, String endTime, String meetingType) {
        return (isStringValid(customerName) && isDateValid(date) && areTimesValid(startTime, endTime) && isStringValid(meetingType));
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
            LocalTime startOfDay = LocalTime.of(8, 00);
            LocalTime endOfDay = LocalTime.of(17, 00);
            DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime startLocalTime = LocalTime.parse(startTime, tf);
            LocalTime endLocalTime = LocalTime.parse(endTime, tf);
            return (startLocalTime.compareTo(endLocalTime) < 0
                    && startLocalTime.compareTo(startOfDay) > 0
                    && endLocalTime.compareTo(endOfDay) < 0);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @FXML
    private void modifyAppointmentSaveButtonPressed(ActionEvent event) throws SQLException, IOException {
        if (validateAppointmentClientSide(CustomerName.getText(), Date.getText(), StartTime.getText(), EndTime.getText(), MeetingType.getText())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
            String customerName = CustomerName.getText();
            String date = Date.getText();
            String startTime = StartTime.getText();
            String endTime = EndTime.getText();
            String meetingType = MeetingType.getText();
            String startString = date + " " + StartTime.getText() + ":00.0";
            String endString = date + " " + EndTime.getText() + ":00.0";
            LocalDateTime startLDT = LocalDateTime.parse(startString, formatter);
            LocalDateTime endLDT = LocalDateTime.parse(endString, formatter);
            try {
                int customerId = SQLQuery.retrieveCustomerId(customerName);
                if (validateAppointmentClientSide(customerName, date, startTime, endTime, meetingType)) {
                    SQLQuery.modifyAppointment(appointmentToBeModified.getAppointmentID(), customerId, startLDT, endLDT, meetingType);
                    try {
                        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
                        Parent root = (Parent) loader.load();
                        MainController mainController = loader.getController();
                        mainController.setLoggedInUser(loggedInUser);
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setHeaderText("Unable to add appointment.");
                    errorAlert.setContentText("Please review all text fields and ensure no overlapping appointments .");
                    errorAlert.showAndWait();
                }
            } catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Failed to find customer. Would you like to open customer management?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CustomerManagement.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Customer Management");
                    stage.setScene(new Scene(root));
                    stage.show();
                }
            }
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Invalid appointment input");
            errorAlert.setContentText("Please verify all entered data.");
            errorAlert.showAndWait();
        }
    }
}
