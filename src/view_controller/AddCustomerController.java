/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DBConnection;
import utils.Query;

/**
 * FXML Controller class
 *
 * @author Jacobi
 */
public class AddCustomerController implements Initializable {

    @FXML
    private Button AddCustomerCancelButton;
    @FXML
    private Button addCustomerSaveButton;
    @FXML
    private TextField CustomerName;
    @FXML
    private TextField City;
    @FXML
    private TextField Zip;
    @FXML
    private TextField PhoneNumber;
    @FXML
    private TextField Address;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void addCustomerCancelButtonPressed(ActionEvent event) {

    }

    @FXML
    private void addCustomerSaveButtonPressed(ActionEvent event) throws SQLException {
        String customerName = CustomerName.getText();
        String addressId = getAddressIdfromDB(Address.getText());
        System.out.println(addressId);
        String phone = PhoneNumber.getText();
        try {
            Connection conn = DBConnection.startConnection();
            String insertStatement = "INSERT INTO customer (customerName, addressId, phone) VALUES (?,?,?)";
            Query.setPreparedStatement(conn, insertStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.setString(1, customerName);
            ps.setString(2, addressId);
            ps.setString(3, phone);
            ps.execute();
        }

       catch (SQLException e) {
            System.out.println(e.getMessage() + "unable to add customer" );
        }
    }
    
    public String getAddressIdfromDB(String address) throws SQLException {
        Connection conn = DBConnection.startConnection();
        String selectStatement = "SELECT addressId FROM address WHERE address = ?";
        Query.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = Query.getPreparedStatement();
        ps.setString(1, address);
        ps.execute();
        String Address = null;
        ResultSet rs = ps.getResultSet();
        try {
            if (rs.next()) {
                Address = rs.getString("addressId");
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage() + "unable to get customer id" );
        }
        return Address;
    }
    
}
