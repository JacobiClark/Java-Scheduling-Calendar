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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;
import utils.DBConnection;
import utils.Query;
import static view_controller.MainController.loggedInUsersAppointments;

/**
 * FXML Controller class
 *
 * @author Jacobi
 */
public class CustomerManagementController implements Initializable {
    public static ObservableList<Customer>customers= FXCollections.observableArrayList();


    @FXML
    private Label appointmentTimeFrameDescriptor;
    @FXML
    private TableView<Customer> CustomersTable;
    @FXML
    private TableColumn<Customer, String> NameColumn;
    @FXML
    private TableColumn<Customer, String> PhoneNumberColumn;
    @FXML
    private TableColumn<Customer, String> AddressColumn;
    @FXML
    private TableColumn<Customer, String> CityColumn;
    @FXML
    private TableColumn<Customer, String> ZipColumn;
    @FXML
    private TableColumn<Customer, String> CountryColumn;
    @FXML
    private Button DeleteCustomerButton;
    @FXML
    private Button ModifyCustomerButton;
    @FXML
    private Button AddCustomerButton;
    @FXML
    private Button BackToCalendarButton;
    
    public void populateCustomersTable() {
        // Initialize product table columns
        NameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
        PhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));
        AddressColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        CityColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("city"));
        ZipColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("zip"));
        CountryColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("country"));
        // Load in Parts
        CustomersTable.setItems(customers);
    }

    /**
     * Initializes the controller class.
     */
    public void initialize(URL url, ResourceBundle rb) {
        try {
            getCustomersFromDB();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerManagementController.class.getName()).log(Level.SEVERE, null, ex);
        }
        populateCustomersTable();
        
    }
    
    public void getCustomersFromDB() throws SQLException{
        customers.clear();
        try {
            Connection conn = DBConnection.startConnection();
            String selectStatement = "SELECT customerId, customerName, phone, address, city, postalCode, country FROM customer INNER JOIN address ON customer.addressId = address.addressId INNER JOIN city ON address.cityId = city.cityId INNER JOIN country on city.countryId = country.countryId";
            Query.setPreparedStatement(conn, selectStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.execute();        
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                Customer customer  = new Customer(
                    rs.getString("customerId"),
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
    }

    @FXML
    private void DeleteCustomerButtonPressed(ActionEvent event) {
    }

    @FXML
    private void ModifyCustomerButtonPressed(ActionEvent event) {
    }

    @FXML
    private void AddCustomerButtonPressed(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddCustomer.fxml"));
        Parent     root       = (Parent) fxmlLoader.load();
        Stage      stage      = new Stage();
        stage.setTitle("Add Customer");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void BackToCalendarButtonPressed(ActionEvent event) {
    }
    
}
