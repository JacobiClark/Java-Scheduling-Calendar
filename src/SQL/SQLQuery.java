/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SQL;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;
import model.User;
import utils.DBConnection;
import utils.Query;
import static view_controller.CustomerManagementController.customers;
import view_controller.MainController;

/**
 *
 * @author Jacobi
 */
public class SQLQuery {
    public static Boolean authenticate(String userName, String password) throws SQLException {
        Connection conn = DBConnection.startConnection();
        String selectStatement = "SELECT * FROM user WHERE userName = ? AND password = ?";
        Query.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = Query.getPreparedStatement();
        ps.setString(1, userName);
        ps.setString(2, password);
        ps.execute();        
        ResultSet rs = ps.getResultSet();
        return rs.next();
    }
    
    public static User createUser(String userName) throws SQLException {
        try {
            Connection conn = DBConnection.startConnection();
            String selectStatement = "SELECT * FROM user WHERE userName = ?";
            Query.setPreparedStatement(conn, selectStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.setString(1, userName);
            ps.execute();        
            ResultSet rs = ps.getResultSet();
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
            return loggedInUser;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ObservableList<Customer> retrieveCustomers() throws SQLException{
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        try {
            Connection conn = DBConnection.startConnection();
            String selectStatement = "SELECT customerId, customerName, phone, address, city, postalCode, country FROM customer INNER JOIN address ON customer.addressId = address.addressId INNER JOIN city ON address.cityId = city.cityId INNER JOIN country on city.countryId = country.countryId";
            Query.setPreparedStatement(conn, selectStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.execute();        
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                Customer customer  = new Customer(
                    rs.getInt("customerId"),
                    rs.getString("customerName"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("city"),
                    rs.getString("postalCode"),
                    rs.getString("country")
                );
                customers.add(customer);
            }
        }
        catch (SQLException e) {
            System.out.println("failed to create appointment" );
        }
        return customers;
    }

    public static ObservableList<Appointment> retrieveAppointments (LocalDateTime endTime, User loggedInUser) throws SQLException{
        ObservableList<Appointment> loggedInUsersAppointments = FXCollections.observableArrayList();
        int userId = loggedInUser.getUserId();
        LocalDateTime now = LocalDateTime.now().minusYears(222);            
        try {
            Connection conn = DBConnection.startConnection();
            String selectStatement = "SELECT * FROM user JOIN appointment ON user.userId = appointment.userId INNER JOIN customer ON appointment.customerId = customer.customerId WHERE user.userId=? AND appointment.end between ? and ?";
            Query.setPreparedStatement(conn, selectStatement);
            PreparedStatement ps = Query.getPreparedStatement();
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
                loggedInUsersAppointments.add(appointment);
            }
        }
        catch (SQLException e) {
            System.out.println("failed to retrieve appointments" );
        }
        return loggedInUsersAppointments;

    }
    
    public static Integer retrieveCustomerId(String customerName) throws SQLException {
        Integer ID = null;
        try {
            Connection conn = DBConnection.startConnection();
            String selectStatement = "SELECT * FROM customer WHERE customerName = ?";
            Query.setPreparedStatement(conn, selectStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            //key-value mapping
            ps.setString(1, customerName);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            if (rs.next()) {
                ID = rs.getInt("customerId");
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage() + "unable to get customer id" );
        }
        return ID;
    }
    public static String retrieveAddressId(String address, String phone) throws SQLException {
        String addressId = null;
        try {      
            Connection conn = DBConnection.startConnection();
            String selectStatement = "SELECT addressId FROM address WHERE address = ? AND phone = ?";
            Query.setPreparedStatement(conn, selectStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.setString(1, address);
            ps.setString(2, phone);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            if (rs.next()) {
                addressId = rs.getString("addressId");
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage() + "unable to get address id" );
        }
        return addressId;
    }
    
    public static void insertAppointment(int customerId, int userId, String meetingType, LocalDateTime startTime, LocalDateTime endTime) throws SQLException {
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
        }
       catch (SQLException e) {
            System.out.println(e.getMessage() + "unable to add appointment" );
        }
    }
    
    public static void deleteAppointment(int appointmentId) throws SQLException {
        try {
            Connection conn = DBConnection.startConnection();
            String deleteStatement = "DELETE FROM appointment WHERE appointmentId = ?";
            Query.setPreparedStatement(conn, deleteStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.setInt(1, appointmentId);
            ps.execute();          
        }
       catch (SQLException e) {
            System.out.println(e.getMessage() + "unable to delete appointment" );
        }
    }
    
    public static void deleteCustomer(int customerId) throws SQLException {
        try {
            Connection conn = DBConnection.startConnection();
            String deleteStatement = "DELETE FROM customer WHERE customerId = ?";
            Query.setPreparedStatement(conn, deleteStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.setInt(1, customerId);
            ps.execute();          
        }
       catch (SQLException e) {
            System.out.println(e.getMessage() + "unable to delete customer" );
        }
    }
    
    public static void insertAddress(String phone, String address) throws SQLException {
        try {
            Connection conn = DBConnection.startConnection();
            String insertStatement = "INSERT INTO address (phone, address) VALUES (?,?)";
            Query.setPreparedStatement(conn, insertStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.setString(1, phone);
            ps.setString(2, address);
            ps.execute();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage() + "unable to add address" );
        }
    }

    public static void insertCustomer(String customerName, String addressId) throws SQLException {
        try {
            Connection conn = DBConnection.startConnection();
            String insertStatement = "INSERT INTO customer (customerName, addressId) VALUES (?,?)";
            Query.setPreparedStatement(conn, insertStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.setString(1, customerName);
            ps.setString(2, addressId);
            ps.execute();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage() + "unable to insert customer" );
        }
    }
}
