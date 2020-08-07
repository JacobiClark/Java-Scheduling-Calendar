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
import javafx.scene.control.Alert;
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
            City.setText(customer.getCity());
            Zip.setText(customer.getZip());
            Country.setText(customer.getCountry());
        } catch (Exception e) {
            System.out.println(e.getMessage() + " unable to import customer info :-(");
        }
    }

    @FXML
    private void modifyCustomerCancelButtonPressed(ActionEvent event) throws IOException {
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CustomerManagement.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Customer Management");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void modifyCustomerSaveButtonPressed(ActionEvent event) throws SQLException {
        String customerName = CustomerName.getText();
        String phone = PhoneNumber.getText();
        String address = Address.getText();
        String city = City.getText();
        String zip = Zip.getText();
        String country = Country.getText();
        if (validateCustomerFields(customerName, phone, address, city, zip, country)) {
            SQL.SQLQuery.insertCountry(country);
            int countryId = SQL.SQLQuery.retrieveCountryId(country);
            SQL.SQLQuery.insertCity(city, countryId);
            int cityId = SQL.SQLQuery.retrieveCityId(city, countryId);
            SQL.SQLQuery.insertAddress(cityId, phone, address, zip);
            int addressId = SQL.SQLQuery.retrieveAddressId(address, cityId, phone);
            //Insert new address and generate addrssId Primary key
            SQL.SQLQuery.modifyCustomer(customerToBeModified.getCustomerId(), customerName, addressId);
            try {
                ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerManagement.fxml"));
                Parent root = (Parent) loader.load();
                Stage stage = new Stage();
                stage.setTitle("CustomerManagement");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Invalid customer input");
            errorAlert.setContentText("Please verify all entered data.");
            errorAlert.showAndWait();
        }
    }

    private static boolean validateCustomerFields(String customerName, String PhoneNumber, String Address, String City, String Zip, String Country) {
        return (isStringValid(customerName) && isPhoneNumberValid(PhoneNumber) && isStringValid(Address) && isStringValid(City) && isStringValid(Zip) && isStringValid(Country));
    }

    private static boolean isStringValid(String str) {
        return (!str.isEmpty()
                && str.matches("^[#.0-9a-zA-Z\\s,-]+$"));
    }

    private static boolean isPhoneNumberValid(String str) {
        return (!str.isEmpty()
                && str.matches("^\\d{3}-\\d{3}-\\d{4}$"));
    }

}
