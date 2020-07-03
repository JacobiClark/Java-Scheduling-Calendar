/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

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
    public static ObservableList<Appointment>loggedInUsersAppointments= FXCollections.observableArrayList();
    private User loggedInUser;
    LocalDateTime now = LocalDateTime.now().minusYears(222);
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
    private void populateAppointmentsTable(LocalDateTime endTime) throws SQLException{
        loggedInUsersAppointments.clear();
        try {
            Connection conn = DBConnection.startConnection();
            String selectStatement = "SELECT * FROM user JOIN appointment ON user.userId = appointment.userId INNER JOIN customer ON appointment.customerId = customer.customerId WHERE user.userId=? AND appointment.end between ? and ?";
            Query.setPreparedStatement(conn, selectStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            int userId = loggedInUser.getUserId();
            //key-value mapping
            ps.setInt(1, userId);
            ps.setTimestamp(2, Timestamp.valueOf(now));
            ps.setTimestamp(3, Timestamp.valueOf(endTime));
            ps.execute();        
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                Appointment appointment  = new Appointment(
                    rs.getInt("appointmentId"),
                    rs.getInt("customerId"),
                    rs.getInt("userId"),
                    rs.getString("start"),
                    rs.getString("end"),
                    rs.getString("type"),
                    rs.getString("customerName")                    
                );
                System.out.println(appointment.getStartTime());
                loggedInUsersAppointments.add(appointment);
            }
        }
        catch (SQLException e) {
            System.out.println("failed to create appointment" );
        }
        populateAppointmentsTable();
    }
    private void DeleteAppointmentButtonPressed(Integer inte) {
        System.out.println(inte);
    }

    public void populateAppointmentsTable() {
        // Initialize product table columns
        DateColumn.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDate>("date"));
        TimeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("startTime"));
        EndTimeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("endTime"));
        MeetingTypeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("meetingType"));
        CustomerColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("customerName"));

        // Load in Parts
        AppointmentsTable.setItems(loggedInUsersAppointments);
    }


    @FXML
    private void DeleteAppointmentButtonPressed(ActionEvent event) {
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
    private void CustomerManagementButtonPressed(ActionEvent event) {
    }
    @FXML
    private void viewCustomerButtonPressed(ActionEvent event) {
    
    }
    public void setLoggedInUser(User user) throws SQLException {
        try {
            loggedInUser = user;
            AppointmentGreeter.setText("Welcome, " + loggedInUser.getUserName() + "!");
            populateAppointmentsTable(oneMonth);
            this.MonthViewRB.setSelected(true);

            //populateAppointmentsTable();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
    
    public void WeekViewRBPressed(ActionEvent event) throws SQLException {
        populateAppointmentsTable(oneWeek);  
        appointmentTimeFrameDescriptor.setText("Weekly Appointments");
    }
    
    public void MonthViewRBPressed(ActionEvent event) throws SQLException {
        populateAppointmentsTable(oneMonth);
        appointmentTimeFrameDescriptor.setText("Monthly Appointments");
    }
    
}