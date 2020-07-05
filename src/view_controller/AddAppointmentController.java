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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String customerName = CustomerName.getText();
        int customerId = getCustomerIdfromDB(customerName);
        LocalDateTime startTime = LocalDateTime.parse(StartTime.getText(), formatter);
        LocalDateTime endTime = LocalDateTime.parse(EndTime.getText(), formatter);
        String meetingType = MeetingType.getText();

        try {
            Connection conn = DBConnection.startConnection();
            String insertStatement = "INSERT INTO appointment (customerId, userId, type, start, end) VALUES (?,?,?,?,?)";
            Query.setPreparedStatement(conn, insertStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.setInt(1, customerId);
            ps.setInt(2, userId);
            ps.setString(3, meetingType);
            ps.setString(4, startTime.toString());
            ps.setString(5, endTime.toString());
            ps.execute();
            FXMLLoader loader=new FXMLLoader(getClass().getResource("Main.fxml"));
            Parent root = (Parent) loader.load();
            MainController mainController=loader.getController();
            mainController.populateAppointmentsTable();
            ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
            

        }

       catch (SQLException e) {
            System.out.println(e.getMessage() + "unable to add appointment" );
        }
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

    
    public int getCustomerIdfromDB(String customerName) throws SQLException {
        Connection conn = DBConnection.startConnection();
        String selectStatement = "SELECT * FROM customer WHERE customerName = ?";
        Query.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = Query.getPreparedStatement();
        //key-value mapping
        ps.setString(1, customerName);
        ps.execute();
        int ID = 0;
        ResultSet rs = ps.getResultSet();
        try {
            if (rs.next()) {
                ID = rs.getInt("customerId");
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage() + "unable to get customer id" );
        }
        return ID;
    }
    
    public void setUserId(User user) {
        userId = user.getUserId();
    }
    
}
