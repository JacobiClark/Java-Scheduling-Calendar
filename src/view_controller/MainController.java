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
    LocalDateTime oneWeek = LocalDateTime.now().plusWeeks(1);
    LocalDateTime oneMonth = LocalDateTime.now().plusMonths(1);

    
    @FXML
    private TableView<Appointment> AppointmentsTable;
    @FXML
    private TableColumn<Appointment, LocalDate> DateColumn;
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
        this.WeekViewRB.setToggleGroup(viewSelector);
        this.MonthViewRB.setToggleGroup(viewSelector);
    }    

    public void DeleteAppointmentButtonPressed(ActionEvent event) throws SQLException {
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

    public void populateAppointmentsTable() throws SQLException {
        // Initialize product table columns
        if (this.WeekViewRB.isSelected()) {
            loggedInUsersAppointments = SQLQuery.retrieveAppointments(oneWeek, loggedInUser);
        }
        if (this.MonthViewRB.isSelected()) {
            loggedInUsersAppointments = SQLQuery.retrieveAppointments(oneWeek, loggedInUser);
        }

        DateColumn.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDate>("date"));
        TimeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("startTime"));
        EndTimeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("endTime"));
        MeetingTypeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("meetingType"));
        CustomerColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("customerName"));

        // Load in Parts
        AppointmentsTable.setItems(loggedInUsersAppointments);
    }

    @FXML
    private void ModifyAppointmentButtonPressed(ActionEvent event) {
    }

    @FXML
    public void AddAppointmentButtonPressed(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddAppointment.fxml"));
        Parent     root       = (Parent) fxmlLoader.load();
        Stage      stage      = new Stage();
        stage.setTitle("Add Appointment");
        stage.setScene(new Scene(root));
        stage.show();
        AddAppointmentController controller   = fxmlLoader.getController();
        controller.setUserId(loggedInUser);
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
            loggedInUsersAppointments = SQLQuery.retrieveAppointments(oneMonth, loggedInUser);
            this.MonthViewRB.setSelected(true);
            populateAppointmentsTable();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
    
    public void WeekViewRBPressed(ActionEvent event) throws SQLException {
        this.WeekViewRB.setSelected(true);
    }
    
    public void MonthViewRBPressed(ActionEvent event) throws SQLException {
        this.MonthViewRB.setSelected(true);
    }
    
}