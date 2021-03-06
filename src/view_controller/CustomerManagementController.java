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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;
import utils.DBConnection;
import utils.Query;

/**
 * FXML Controller class
 *
 * @author Jacobi
 */
public class CustomerManagementController implements Initializable {

    public static ObservableList<Customer> customers = FXCollections.observableArrayList();

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

    public void populateCustomersTable() throws SQLException {
        customers = SQL.SQLQuery.retrieveCustomers();
        NameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
        PhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));
        AddressColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        CityColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("city"));
        ZipColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("zip"));
        CountryColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("country"));
        CustomersTable.setItems(customers);
    }

    /**
     * Initializes the controller class.
     */
    public void initialize(URL url, ResourceBundle rb) {
        try {
            populateCustomersTable();
        } catch (SQLException e) {
            System.out.println(e.getMessage() + " unable to populate customers table");
        }
    }
    
    @FXML
    //Lambda function that closes the current screen on click. Lambda function was used here due to the function only needing to
    //be called once, so the problem of closing out of a window is easier though of in terms of an isolated function rather than object
    //which a method is being called upon.
    private void BackToCalendarButtonPressed(ActionEvent event) throws IOException {
        WindowCloser closer = () -> ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
        closer.closeWindow();
    }

    @FXML
    public void DeleteCustomerButtonPressed(ActionEvent event) throws SQLException {
        if (CustomersTable.getSelectionModel().getSelectedItem() != null) {
            Customer selectedCustomer = CustomersTable.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete selected customer");
            alert.setHeaderText("Are you sure you want to delete?");
            alert.setContentText("Are you sure you want to delete this customer?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.out.println(selectedCustomer.getCustomerId());
                SQL.SQLQuery.deleteCustomer(selectedCustomer.getCustomerId());
                populateCustomersTable();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select a customer from the table to delete.");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }

    @FXML
    private void ModifyCustomerButtonPressed(ActionEvent event) throws IOException {
        if (CustomersTable.getSelectionModel().getSelectedItem() != null) {
            Customer selectedCustomer = CustomersTable.getSelectionModel().getSelectedItem();
            ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyCustomer.fxml"));
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setTitle("Modify Customer");
            stage.setScene(new Scene(root));
            stage.show();
            ModifyCustomerController modifyCustomerController = loader.getController();
            modifyCustomerController.setCustomerToBeModified(selectedCustomer);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select a customer from the table to modify.");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }

    @FXML
    private void AddCustomerButtonPressed(ActionEvent event) throws IOException {
        try {
            ((Node) (event.getSource())).getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddCustomer.fxml"));
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add Customer");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Lambda Interface
    interface WindowCloser {
        void closeWindow();
    }
}
