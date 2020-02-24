
package c195.view;

import c195.Main;
import c195.model.Appointment;
import c195.model.Customer;
import c195.model.Reports;
import c195.model.User;
import c195.util.AppointmentDB;
import c195.util.CustomerDB;
import c195.util.UserDB;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class ReportsViewController implements Initializable {

    @FXML
    ComboBox<String> consultantList;
    
    @FXML
    TableView<Appointment> consultantScheduleTable;
    
    @FXML
    TableView<Reports> typeMonthTable;
    
    @FXML
    TableView<Reports> custFromCity;
    
    @FXML
    TableColumn<Appointment, String> createdBySchedule;
    
    @FXML
    TableColumn<Appointment, String> titleSchedule;
          
    @FXML
    TableColumn<Appointment, String> descriptionSchedule;
    
    @FXML
    TableColumn<Appointment, String> typeSchedule;
            
    @FXML
    TableColumn<Appointment, String> contactSchedule;

    @FXML
    TableColumn<Appointment, String> startSchedule;
    
    @FXML
    TableColumn<Appointment, String> endSchedule;
    
    @FXML
    TableColumn<Reports, String> typeMonth;
    
    @FXML
    TableColumn<Reports, String> monthMonth;
    
    @FXML
    TableColumn<Reports, Integer> numberMonth;
    
    @FXML
    TableColumn<Reports, String> country;
    
    @FXML
    TableColumn<Reports, String> city;
    
    @FXML
    TableColumn<Reports, Integer> totalCust;
            
    private static ObservableList<Appointment> apptList = FXCollections.observableArrayList();
    private static ObservableList<Reports> reportList = FXCollections.observableArrayList();
    private static ObservableList<Reports> cityList = FXCollections.observableArrayList();
    private User currentUser;
    private Main mainApp;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        consultantList.setItems(UserDB.getAllUsers());
    }    
    
    public void transferInfo(Main app, User user){
        this.currentUser = user;
        this.mainApp = app;    
    }
    
    public void handleConsultantSelection(){
        
        //set the list of Appointment objects to an observableList called apptList
        apptList = AppointmentDB.apptByConsultant(consultantList.getValue());

        //set Appointment objects in apptList to the consultantScheduleTable tableview
        consultantScheduleTable.setItems(apptList);
        
        //getting Appointment Object property from a lambda expression to populate on each TableView TableColumn
        createdBySchedule.setCellValueFactory(cellData -> cellData.getValue().getUsernameProperty());
        titleSchedule.setCellValueFactory(cellData -> cellData.getValue().getTitleProperty());
        descriptionSchedule.setCellValueFactory(cellData -> cellData.getValue().getDescriptionProperty());
        typeSchedule.setCellValueFactory(cellData -> cellData.getValue().getTypeProperty());
        contactSchedule.setCellValueFactory(cellData -> cellData.getValue().getContactProperty());
        startSchedule.setCellValueFactory(cellData -> cellData.getValue().getStartProperty());
        endSchedule.setCellValueFactory(cellData -> cellData.getValue().getEndProperty());
    }
    
    public void handleTypeByMonth(){
        reportList = AppointmentDB.getTypesByMonth();
        
        typeMonthTable.setItems(reportList);
        
        monthMonth.setCellValueFactory(cellData -> cellData.getValue().getMonthProperty());
        numberMonth.setCellValueFactory(cellData -> cellData.getValue().getTotalRecordsProperty().asObject());
        typeMonth.setCellValueFactory(cellData -> cellData.getValue().getTypeProperty());
    }
    
    public void handleCustByCity(){
        cityList = CustomerDB.getCityCount();
        
        custFromCity.setItems(cityList);
        
        city.setCellValueFactory(cellData -> cellData.getValue().getCityProperty());
        country.setCellValueFactory(cellData -> cellData.getValue().getCountryProperty());
        totalCust.setCellValueFactory(cellData -> cellData.getValue().getTotalRecordsProperty().asObject());
    }
    
}
