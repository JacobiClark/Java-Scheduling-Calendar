/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Jacobi
 */
public class AddCustomerController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private TextField address1Field;
    @FXML
    private TextField address2Field;
    @FXML
    private TextField zipField;
    @FXML
    private TextField phoneField;
    @FXML
    private ComboBox<?> cityComboBox;
    @FXML
    private TableView<?> customersTableView;
    @FXML
    private TableColumn<?, ?> custIDCol;
    @FXML
    private TableColumn<?, ?> custNameCol;
    @FXML
    private TableColumn<?, ?> custAddress1Col;
    @FXML
    private TableColumn<?, ?> custAddress2Col;
    @FXML
    private TableColumn<?, ?> custCityCol;
    @FXML
    private TableColumn<?, ?> custCountryCol;
    @FXML
    private TableColumn<?, ?> custZipCol;
    @FXML
    private TableColumn<?, ?> custPhoneCol;
    @FXML
    private Button clearButton;
    @FXML
    private Button addButton;
    @FXML
    private Button returnButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleClearButton(ActionEvent event) {
    }

    @FXML
    private void handleAddButton(ActionEvent event) {
    }

    @FXML
    private void handleReturnButton(ActionEvent event) {
    }
    
}
