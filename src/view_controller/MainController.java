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
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.User;
import utils.DBConnection;
import utils.Query;

/**
 * FXML Controller class
 *
 * @author Jacobi
 */
public class MainController implements Initializable {
    private User loggedInUser;
    ObservableList<Appointment> loggedInUsersAppointments = FXCollections.observableArrayList();
    
    @FXML
    private TableView<Appointment> AppointmentsTable;
    @FXML
    private TableColumn<Appointment, String> DateColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> TimeColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> EndTimeColumn;
    @FXML
    private TableColumn<Appointment, String> MeetingTypeColumn;
    @FXML
    private TableColumn<Appointment, String> CustomerColumn;
    @FXML
    private Button WeeklyAppointmentsButton;
    @FXML
    private Button MonthlyAppointmentsButton;
    @FXML
    private Label AppointmentGreeter;
    @FXML
    private Label appointmentTimeFrameDescriptor;
    @FXML
    private Button DeleteAppointmentButton;
    @FXML
    private Button ModifyAppointmentButton;
    @FXML
    private Button AddAppointmentButton;
    @FXML
    private Button CustomerManagementButton;
    private Button ViewCustomerButton;
    @FXML
    private RadioButton WeekViewRB;
    @FXML
    private RadioButton MonthViewRB;
    @FXML
    private ToggleGroup viewSelector;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewSelector = new ToggleGroup();
        WeekViewRB.setToggleGroup(viewSelector);
        MonthViewRB.setToggleGroup(viewSelector);
        try {
            populateAppointmentsTable();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    
    }    

    public void DeleteAppointmentButtonPressed(ActionEvent event) throws SQLException {
        if(AppointmentsTable.getSelectionModel().getSelectedItem() != null) {
            Appointment selectedAppointment = AppointmentsTable.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete selected Product");
            alert.setHeaderText("Are you sure you want to delete?");
            alert.setContentText("Are you sure you want to delete this product?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                SQL.SQLQuery.deleteAppointment(selectedAppointment.getAppointmentID());
                populateAppointmentsTable();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select an appointment from the table to delete.");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }

    public void populateAppointmentsTable() throws SQLException {
        ObservableList<Appointment> appointmentsToBePopulated = FXCollections.observableArrayList();
        // Initialize product table columns
        if (WeekViewRB.isSelected()) {
            appointmentsToBePopulated = SQLQuery.retrieveAppointments(LocalDateTime.now().plusWeeks(1), loggedInUser);
        }
        if (MonthViewRB.isSelected()) {
            appointmentsToBePopulated = SQLQuery.retrieveAppointments(LocalDateTime.now().plusMonths(1), loggedInUser);
        }
        
        DateColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("startDate"));
        TimeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("startTime"));
        EndTimeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("endTime"));
        MeetingTypeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("meetingType"));
        CustomerColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("customerName"));

        // Load in Parts
        AppointmentsTable.setItems(appointmentsToBePopulated);
    }
    /*FXMLLoader loader=new FXMLLoader(getClass().getResource("Main.fxml"));
                Parent root = (Parent) loader.load();
                MainController mainController=loader.getController();
                mainController.setLoggedInUser(loggedInUser);
                Stage stage=new Stage();
                stage.setScene(new Scene(root));
                stage.show();*/
    @FXML
    public void ModifyAppointmentButtonPressed(ActionEvent event) throws IOException {
        try {
            if(AppointmentsTable.getSelectionModel().getSelectedItem() != null) {
                Appointment selectedAppointment = AppointmentsTable.getSelectionModel().getSelectedItem();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyAppointment.fxml"));
                Parent     root       = (Parent) loader.load();
                ModifyAppointmentController modifyAppointmentController=loader.getController();
                modifyAppointmentController.setAppointmentToBeModified(selectedAppointment);
                modifyAppointmentController.setUser(loggedInUser);
                Stage      stage      = new Stage();
                stage.setTitle("Modify Appointment");
                stage.setScene(new Scene(root));
                stage.show();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Please select an appointment from the table to modify.");
                Optional<ButtonType> result = alert.showAndWait();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void AddAppointmentButtonPressed(ActionEvent event) throws IOException {
        try {
            ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
            FXMLLoader loader=new FXMLLoader(getClass().getResource("AddAppointment.fxml"));
            Parent root = (Parent) loader.load();
            AddAppointmentController addAppointmentController=loader.getController();
            addAppointmentController.setUser(loggedInUser);
            Stage stage=new Stage();
            stage.setTitle("Add Appointment");
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void CustomerManagementButtonPressed(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CustomerManagement.fxml"));
        Parent     root       = (Parent) fxmlLoader.load();
        Stage      stage      = new Stage();
        stage.setTitle("Customer Management");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void setLoggedInUser(User user) throws SQLException {
        try {
            loggedInUser = user;
            AppointmentGreeter.setText("Welcome, " + loggedInUser.getUserName() + "!");
            loggedInUsersAppointments = SQLQuery.retrieveAppointments(LocalDateTime.now().plusMonths(1), loggedInUser);
            MonthViewRB.setSelected(true);
            populateAppointmentsTable();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
    
    public void WeekViewRBPressed(ActionEvent event) throws SQLException {
        this.WeekViewRB.setSelected(true);
        populateAppointmentsTable();
    }
    
    public void MonthViewRBPressed(ActionEvent event) throws SQLException {
        this.MonthViewRB.setSelected(true);
        populateAppointmentsTable();

    }
    
}