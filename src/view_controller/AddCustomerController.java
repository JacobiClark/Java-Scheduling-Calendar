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
    private Button AddCustomerSaveButton;
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

    @FXML
    private void addCustomerCancelButtonPressed(ActionEvent event) throws IOException {
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CustomerManagement.fxml"));
        Parent     root       = (Parent) fxmlLoader.load();
        Stage      stage      = new Stage();
        stage.setTitle("Customer Management");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void addCustomerSaveButtonPressed(ActionEvent event) throws SQLException {
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
        SQL.SQLQuery.insertCustomer(customerName, addressId);
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
