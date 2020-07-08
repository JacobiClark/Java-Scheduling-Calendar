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
        String phone = PhoneNumber.getText();
        String address = Address.getText();
        SQL.SQLQuery.insertAddress(phone, address);
        String addressId = SQL.SQLQuery.retrieveAddressId(address,phone);
        //Insert new address and generate addrssId Primary key
        SQL.SQLQuery.insertCustomer(customerName, addressId);
    }

    
}
