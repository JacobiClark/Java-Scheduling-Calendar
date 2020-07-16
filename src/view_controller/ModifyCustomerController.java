/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import model.Customer;

/**
 * FXML Controller class
 *
 * @author Jacobi
 */
public class ModifyCustomerController implements Initializable {
    private Customer customerToBeModified;
    @FXML
    private Button ModifyCustomerCancelButton;
    @FXML
    private Button ModifyCustomerSaveButton;
    @FXML
    private TextField CustomerName;
    @FXML
    private TextField PhoneNumber;
    @FXML
    private TextField Address;
    @FXML
    private TextField City;
    @FXML
    private TextField Zip;
    @FXML
    private TextField Country;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void setCustomerToBeModified(Customer customer) {
        try {
            customerToBeModified = customer;
            CustomerName.setText(customer.getCustomerName());
            PhoneNumber.setText(customer.getPhone());
            Address.setText(customer.getAddress());
        }
        catch (Exception e) {
            System.out.println(e.getMessage() + " unable to import customer info :-(");
        }
    }

    @FXML
    private void modifyCustomerCancelButtonPressed(ActionEvent event) {
    }

    @FXML
    private void modifyCustomerSaveButtonPressed(ActionEvent event) throws SQLException {
        String customerName = CustomerName.getText();
        String phone = PhoneNumber.getText();
        String address = Address.getText();
        String city = City.getText();
        String zip = Zip.getText();
        String country = Country.getText();
        SQL.SQLQuery.insertCountry(country);
        int countryId = SQL.SQLQuery.retrieveCountryId(country);
        SQL.SQLQuery.insertCity(city, countryId);
        int cityId = SQL.SQLQuery.retrieveCityId(city, countryId);
        SQL.SQLQuery.insertAddress(cityId, phone, address, zip);
        int addressId = SQL.SQLQuery.retrieveAddressId(address, cityId, phone);
        //Insert new address and generate addrssId Primary key
        SQL.SQLQuery.modifyCustomer(customerToBeModified.getCustomerId(), customerName, addressId);
        try {
            ((Node) (event.getSource())).getScene().getWindow().hide();
            FXMLLoader loader=new FXMLLoader(getClass().getResource("CustomerManagement.fxml"));
            Parent root = (Parent) loader.load();
            Stage stage=new Stage();
            stage.setTitle("CustomerManagement");
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
