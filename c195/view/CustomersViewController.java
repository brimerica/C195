/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195.view;

import c195.Main;

import c195.model.Customer;
import c195.model.User;
import c195.util.AddressDB;
import c195.util.CityDB;
import c195.util.CustomerDB;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class CustomersViewController implements Initializable {
    
    @FXML
    ComboBox cityComboBox;
    
    @FXML
    ComboBox countryComboBox;
    
    @FXML
    Button customerNewBtn;
    
    @FXML
    Button customerSaveBtn;
    
    @FXML
    Button customerCancelBtn;
    
    @FXML
    TextField nameTextField;
    
    @FXML
    TextField addressTextField;
    
    @FXML
    TextField address2TextField;
    
    @FXML
    TextField phoneTextField;
    
    @FXML
    TextField postalCodeTextField;
    
    @FXML
    TableView<Customer> customerTable;
    
    @FXML
    TableColumn<Customer, String> customerName;
    
    @FXML
    TableColumn<Customer, String> customerAddress;
    
    @FXML
    TableColumn<Customer, String> customerAddress2;
    
    @FXML
    TableColumn<Customer, String> customerCity;
    
    @FXML
    TableColumn<Customer, String> customerPostalCode;
    
    @FXML
    TableColumn<Customer, String> customerPhone;
    
    @FXML
    TableColumn<Customer, String> customerCountry;
    
    
    private ObservableList<Customer> customerList = FXCollections.observableArrayList();
    private Main mainApp;
    private static User currentUser;
    private static int editCustId = 0;
    private static int editAddId = 0;
    Customer selectedCustomer;
    private static int selectedCustomerIndex;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setCustFieldsDisabled(true);
        
        updateCustTableView();
    }  
    
    public void transferInfo(Main app, User user){
        this.currentUser = user;
        this.mainApp = app;
    }
    
    @FXML
    public void handleCustNewBtn(ActionEvent event){
        
        setCustFieldsDisabled(false);
        
        cityComboBox.setItems(CityDB.getCityName());
        countryComboBox.setItems(CityDB.getCountryName());
        
    }
    
    @FXML
    public void handleCustEditBtn(ActionEvent event){
        try{
            selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            selectedCustomerIndex = customerTable.getSelectionModel().getSelectedIndex();
            editCustId = selectedCustomer.getCustomerId();
            editAddId = selectedCustomer.getAddressId();
            
            cityComboBox.setItems(CityDB.getCityName());
            countryComboBox.setItems(CityDB.getCountryName());
            
            nameTextField.setText(selectedCustomer.getCustomerName());
            addressTextField.setText(selectedCustomer.getAddress());
            address2TextField.setText(selectedCustomer.getAddress2());
            phoneTextField.setText(selectedCustomer.getPhone());
            postalCodeTextField.setText(selectedCustomer.getPostalCode());
            cityComboBox.setValue(selectedCustomer.getCity());
            countryComboBox.setValue(selectedCustomer.getCountry());
            
            setCustFieldsDisabled(false);
            
        } catch(Exception error){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Modify Error");
            alert.setHeaderText("Modify Error");
            alert.setContentText("Please select an item to modify.");
            alert.showAndWait();
        }
    }
    
    @FXML
    public void handleCustDeleteBtn(ActionEvent event){
        selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        
        if(selectedCustomer != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("Delete Customer?");
            alert.setContentText("Are you sure you want to delete " + selectedCustomer.getCustomerName() + "?");
            Optional<ButtonType> choice = alert.showAndWait();
            if(choice.get() == ButtonType.OK){             
                CustomerDB.deleteCustomerRecord(selectedCustomer);
                customerList.remove(selectedCustomer);
                
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Delete Error");
            alert.setHeaderText("Delete Error");
            alert.setContentText("Please select a customer to delete.");
            alert.showAndWait();
        }
    }
    
    @FXML
    public void handleCustSaveBtn(ActionEvent event){
        
         //Pull values from fields into variables
        String custName = nameTextField.getText();
        String custAdd = addressTextField.getText();
        String custAdd2 = address2TextField.getText();
        String custCity = null;
        String custCountry = null;
        String custPostCode = postalCodeTextField.getText();
        String custPhone = phoneTextField.getText();
        
        try{
            custCity = cityComboBox.getSelectionModel().getSelectedItem().toString();
            custCountry = countryComboBox.getSelectionModel().getSelectedItem().toString();
        } catch(Exception e){
            formAlert("a city/country");
            e.getMessage();
        }
        
        if(custName.isEmpty()){
            formAlert("a name");
        } else if(custAdd.isEmpty()){
            formAlert("an address");
        } else if(custPostCode.isEmpty()){
            formAlert("a postal code");
        } else if(custPhone.isEmpty()){
            formAlert("a phone number");
        } else if(editCustId==0){
            
            //Set new Customer variable called cust
            Customer newCust;

            //Create new Customer object called cust with form values as parameters
            newCust = new Customer(custName, custAdd, custAdd2, custPostCode, custPhone, custCity, custCountry);

            //Saves values to Address and Customer DB tables
            AddressDB.commitAddressRecord(newCust, currentUser);
            CustomerDB.commitCustomerRecord(newCust, AddressDB.newAddressId(), currentUser);
            newCust.setAddressId(AddressDB.newAddressId());
            newCust.setCustomerId(CustomerDB.newCustomerId());
            
        } else {
            
            Customer editCust;
            
            editCust = new Customer(custName, custAdd, custAdd2, custPostCode, custPhone, custCity, custCountry);
            editCust.setCustomerId(editCustId);
            editCust.setAddressId(editAddId);
            
            AddressDB.updateAddressRecord(editCust, currentUser);
            CustomerDB.updateCustomerRecord(editCust, currentUser);
            
            customerList.remove(selectedCustomerIndex);
            customerList.add(selectedCustomerIndex, editCust);
            
        }
        
        //Refresh the Customer Table
        updateCustTableView();
        
        //Disable Customer form fields/ComboBoxes and clear values after save
        setCustFieldsDisabled(true);
        
        editCustId = 0;
    }
    
    @FXML
    public void handleCustCanceleBtn(ActionEvent event){
        
        //Disable Customer form fields/ComboBoxes and clear values on Cancel
        setCustFieldsDisabled(true);
        editCustId = 0;
    }
    
    public void updateCustTableView(){
        try{
            customerList = CustomerDB.getAllCustomerRecords();
        }catch(SQLException e){
            e.getMessage();
        }
        
        customerTable.setItems(customerList);
        
        customerName.setCellValueFactory(cellData -> cellData.getValue().getCustomerNameProperty());
        customerAddress.setCellValueFactory(cellData -> cellData.getValue().getAddressProperty());
        customerAddress2.setCellValueFactory(cellData -> cellData.getValue().getAddress2Property());
        customerCity.setCellValueFactory(cellData -> cellData.getValue().getCityProperty());
        customerPostalCode.setCellValueFactory(cellData -> cellData.getValue().getPostalCodeProperty());
        customerPhone.setCellValueFactory(cellData -> cellData.getValue().getPhoneProperty());
        customerCountry.setCellValueFactory(cellData -> cellData.getValue().getCountryProperty());
    }
    
    public void setCustFieldsDisabled(boolean bool){
        if(bool){
            nameTextField.clear();
            addressTextField.clear();
            address2TextField.clear();
            phoneTextField.clear();
            postalCodeTextField.clear();
            cityComboBox.setItems(null);
            countryComboBox.setItems(null);
        }
        nameTextField.setDisable(bool);
        addressTextField.setDisable(bool);
        address2TextField.setDisable(bool);
        phoneTextField.setDisable(bool);
        postalCodeTextField.setDisable(bool);
        cityComboBox.setDisable(bool);
        countryComboBox.setDisable(bool);
        customerSaveBtn.setDisable(bool);
        customerCancelBtn.setDisable(bool);
    }
    
    public void formAlert(String field){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Form Error");
        alert.setHeaderText("Form Error");
        alert.setContentText("Please include " + field + " for this customer.");
        alert.showAndWait();
    }

}
