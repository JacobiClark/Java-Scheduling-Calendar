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
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;
import model.User;
import utils.AppointmentTypeCount;

/**
 * FXML Controller class
 *
 * @author Jacobi
 */
public class ReportsController implements Initializable {

    @FXML
    private TableView<Appointment> ConsultantScheduleTable;
    @FXML
    private TableColumn<Appointment, String> DateColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> TimeColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> EndTimeColumn;
    @FXML
    private TableColumn<Appointment, String> TypeColumn;
    @FXML
    private TableColumn<Appointment, String> CustomerColumn;
    @FXML
    private TextField ConsultantNameField;

    @FXML
    private TableView<AppointmentTypeCount> AppointmentsFromMonthTable;
    @FXML
    private TableColumn<AppointmentTypeCount, String> MeetingTypeMonthlyColumn;
    @FXML
    private TableColumn<AppointmentTypeCount, String> AmountColumn;
    @FXML
    public TextField MonthYearField;
    
    @FXML
    private TableView<User> ConsultantsTable;
    @FXML
    private TableColumn<User, String> ConsultantColumn;
    
    @FXML
    private TableView<Customer> CustomersTable;
    @FXML
    private TableColumn<Customer, String> CustomersColumn;
    
    @FXML
    private Button ExitButton;
    @FXML
    private Button GenerateAppointmentsForConsultantButton;
    @FXML
    private Button GenerateAppointmentsFromMonthButton;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateConsultantsTable();
        populateCustomersTable();
    }
    
    private void populateConsultantsTable() {
        try {
            ObservableList<User> usersToBePopulated = SQLQuery.retrieveAllUsers();
            ConsultantColumn.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));
            ConsultantsTable.setItems(usersToBePopulated);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void populateCustomersTable() {
        try {
            ObservableList<Customer> customersToBePopulated = SQLQuery.retrieveAllCustomers();
            CustomersColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
            CustomersTable.setItems(customersToBePopulated);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static boolean isMonthYearValid(String str) {
        return (!str.isEmpty()
                && str.matches("^\\d{2}/\\d{4}$"));
    }

    @FXML
    private void generateAppointmentsFromMonthButtonPressed(ActionEvent event) {
        ObservableList<AppointmentTypeCount> appointmentTypesByMonthToBePopulated = FXCollections.observableArrayList();
        String monthYear = MonthYearField.getText();
        String month = monthYear.substring(0,2);
        String year = monthYear.substring(monthYear.length()-4);
        int daysInMonth = YearMonth.of(Integer.parseInt(year), Integer.parseInt(month)).lengthOfMonth();
        if(isMonthYearValid(monthYear)) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime beginningOfMonthLDT = LocalDateTime.parse(year+"-"+month+"-01 00:00", formatter);
                LocalDateTime endOfMonthLDT = LocalDateTime.parse(year+"-"+month+"-"+daysInMonth+" 11:59", formatter);
                appointmentTypesByMonthToBePopulated = SQLQuery.retrieveAppointmentsFromMonth(beginningOfMonthLDT, endOfMonthLDT);
                MeetingTypeMonthlyColumn.setCellValueFactory(new PropertyValueFactory<AppointmentTypeCount, String>("type"));
                AmountColumn.setCellValueFactory(new PropertyValueFactory<AppointmentTypeCount, String>("count"));
                AppointmentsFromMonthTable.setItems(appointmentTypesByMonthToBePopulated);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please verify that input matches required formatting.");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }

    @FXML
    private void ExitButtonPressed(ActionEvent event) {
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void generateAppointmentsForConsultantButtonPressed(ActionEvent event) throws SQLException {
        String consultantName = ConsultantNameField.getText();
        int consultantId = SQLQuery.retrieveUserId(consultantName);
        ObservableList<Appointment>  appointmentsToBePopulated = SQLQuery.retrieveAllAppointments(consultantId);
        DateColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("startDate"));
        TimeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("startTime"));
        EndTimeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("endTime"));
        TypeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("meetingType"));
        CustomerColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("customerName"));
        ConsultantScheduleTable.setItems(appointmentsToBePopulated);        
    }

    
    
}
