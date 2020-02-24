package c195.view;

import c195.Main;
import c195.model.Appointment;
import c195.model.User;
import c195.util.AppointmentDB;
import c195.util.CustomerDB;
import c195.util.UserDB;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class AppointmentViewController implements Initializable {

    @FXML
    Button appmntNewBtn;
    
    @FXML
    Button appmntEditBtn;
    
    @FXML
    Button appmntDeleteBtn;
    
    @FXML
    Button appmntSaveBtn;
    
    @FXML
    Button appmntCancelBtn;
    
    @FXML
    Tab monthTab;
    
    @FXML
    Tab weeklyTab;
    
    @FXML
    TableView<Appointment> monthAppmntTable;
    
    @FXML
    TableView<Appointment> weekAppmntTable;
    
    @FXML
    TextField titleTextField;
    
    @FXML
    TextField descTextField;
    
    @FXML
    ComboBox<String> contactComboBox;
    
    @FXML
    ComboBox<String> typeComboBox;
    
    @FXML
    DatePicker dateDP;
    
    @FXML
    ComboBox<String> startTimeComboBox;
    
    @FXML
    ComboBox<String> endTimeComboBox;
    
    @FXML
    TableColumn<Appointment, String> monthTitle;
    
    @FXML
    TableColumn<Appointment, String> monthDescription;
    
    @FXML
    TableColumn<Appointment, String> monthStart;
    
    @FXML
    TableColumn<Appointment, String> monthEnd;
    
    @FXML
    TableColumn<Appointment, String> monthType;
    
    @FXML
    TableColumn<Appointment, String> monthContact;
    
    @FXML
    TableColumn<Appointment, String> monthUser;
    
    @FXML
    TableColumn<Appointment, String> weekTitle;
    
    @FXML
    TableColumn<Appointment, String> weekDescription;
    
    @FXML
    TableColumn<Appointment, String> weekStart;
    
    @FXML
    TableColumn<Appointment, String> weekEnd;
    
    @FXML
    TableColumn<Appointment, String> weekType;
    
    @FXML
    TableColumn<Appointment, String> weekContact;
    
    @FXML
    TableColumn<Appointment, String> weekUser;
    
    //private FormatStyle style = FormatStyle.SHORT;
    private ObservableList<Appointment> appointmentMonthList = FXCollections.observableArrayList();
    private ObservableList<Appointment> appointmentWeekList = FXCollections.observableArrayList();
    private ObservableList<String> typeList = FXCollections.observableArrayList();
    private ObservableList<String> startTimes = FXCollections.observableArrayList();
    private ObservableList<String> endTimes = FXCollections.observableArrayList();
    private static DateTimeFormatter shortTimeFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private static DateTimeFormatter shortDateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
    private static DateTimeFormatter shortDateTimeFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
    private static DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    private static DateTimeFormatter timestampFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static ZoneId currentZoneID = ZoneId.systemDefault();
    private static LocalDateTime currentDateTime = LocalDateTime.now();
    private static LocalDate currentDate = currentDateTime.toLocalDate();
    private static LocalTime currentTime = currentDateTime.toLocalTime();
    
  
    User currentUser;
    Main mainApp;
    private static Appointment selectedAppointment;
    
    private static int editApptId = 0;;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setApptFieldsDisabled(true);
        
        typeList.add("Consultation");
        typeList.add("Personal");
        typeList.add("Meeting");
        typeList.add("Contract");
        
        updateAppmntTableView();
        
        //Lambda expression to listen for startTimeComboBox selection change
        startTimeComboBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            endTimes.clear();
            if(newValue != null){
                LocalTime businessHours = LocalTime.of(8,0);
                LocalTime selectedValue = LocalTime.parse(newValue, shortTimeFormat);
                do{
                    if(selectedValue.isBefore(businessHours)){
                        endTimes.add(businessHours.format(shortTimeFormat));
                        businessHours = businessHours.plusMinutes(15);
                    } else {
                        businessHours = businessHours.plusMinutes(15);
                    }
                } while(businessHours.isBefore(LocalTime.of(17,15)));
                endTimeComboBox.setItems(endTimes);
            }
        });

    }
    
    public void transferInfo(Main app, User user){
        this.currentUser = user;
        this.mainApp = app; 
    }

    public void handleApptNewBtn(ActionEvent event){
        setApptFieldsDisabled(false);
        
        LocalTime businessHoursEnd = LocalTime.of(17,15);
        
        contactComboBox.setItems(CustomerDB.getCustomerName());
        typeComboBox.setItems(typeList);
        if(currentDateTime.isAfter(LocalDateTime.of(currentDate, businessHoursEnd))){
            dateDP.setValue(currentDate.plusDays(1));
        } else {
            dateDP.setValue(currentDate);
        }
        setStartTimesCB();
    }
    
    public void handleApptEditBtn(ActionEvent event){
        try{
            //Enable fields and set there values to the selected appointments values
            setApptFieldsDisabled(false);
            
            //Selected appointment
            selectedAppointment = monthAppmntTable.getSelectionModel().getSelectedItem();
            editApptId = selectedAppointment.getAppointmentId();
            LocalDateTime startLDT = LocalDateTime.parse(selectedAppointment.getStart(), dateTimeFormat);
            LocalDateTime endLDT = LocalDateTime.parse(selectedAppointment.getEnd(), dateTimeFormat);
            
            //set combobox listings
            contactComboBox.setItems(CustomerDB.getCustomerName());
            typeComboBox.setItems(typeList);
            
            //Pull values from current appointment and place in the Fields/Comboboxs/datepicker
            titleTextField.setText(selectedAppointment.getTitle());
            descTextField.setText(selectedAppointment.getDescription());
            contactComboBox.setValue(selectedAppointment.getContact());
            typeComboBox.setValue(selectedAppointment.getType());
            dateDP.setValue(startLDT.toLocalDate());
            setStartTimesCB();
            startTimeComboBox.getSelectionModel().select(startLDT.format(shortTimeFormat));
            endTimeComboBox.getSelectionModel().select(endLDT.format(shortTimeFormat));

        } catch(Exception error){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Modify Error");
            alert.setHeaderText("Modify Error");
            alert.setContentText("Please select an item to modify.");
            alert.showAndWait();
        }

    }
    
    public void handleApptCancelBtn(ActionEvent event) throws NullPointerException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel save?");
        alert.setHeaderText("Cancel Appointment save?");
        alert.setContentText("Are you sure you want to cancel and clear out all values?");
        Optional<ButtonType> choice = alert.showAndWait();
        if(choice.get() == ButtonType.OK){             
            setApptFieldsDisabled(true);
            editApptId = 0;
        }
    }
    
    public void handleApptDeleteBtn(ActionEvent event){
        selectedAppointment = monthAppmntTable.getSelectionModel().getSelectedItem();
        
        if(selectedAppointment != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("Delete Customer?");
            alert.setContentText("Are you sure you want to delete " + selectedAppointment.getTitle() + "?");
            Optional<ButtonType> choice = alert.showAndWait();
            if(choice.get() == ButtonType.OK){             
                AppointmentDB.deleteAppointmentRecord(selectedAppointment);
                updateAppmntTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Delete Error");
            alert.setHeaderText("Delete Error");
            alert.setContentText("Please select a customer to delete.");
            alert.showAndWait();
        }
    }
    
    public void handleApptSaveBtn(ActionEvent event){
        
         //Pull values from fields into variables
        String apptTitle = titleTextField.getText();
        String apptDesc = descTextField.getText();
        String apptContact = null;
        String apptType = null;
        String apptStartTime = "";
        String apptEndTime = "";
        LocalDate localDate = null;
        LocalDateTime newStartTimeLDT = null;
        LocalDateTime newEndTimeLDT = null;
        
        try{
            apptContact = contactComboBox.getSelectionModel().getSelectedItem();
            apptType = typeComboBox.getSelectionModel().getSelectedItem();
        
            try{
                //Setting Date, Start and End Times from the DatePicker and time ComboBoxes
                localDate = dateDP.getValue();
                LocalTime startTime = LocalTime.parse(startTimeComboBox.getSelectionModel().getSelectedItem(), shortTimeFormat);
                LocalTime endTime = LocalTime.parse(endTimeComboBox.getSelectionModel().getSelectedItem(), shortTimeFormat);

                //Setting LocalDateTime object with date and times from LocalDate and LocalTime
                LocalDateTime startDT = LocalDateTime.of(localDate, startTime);
                LocalDateTime endDT = LocalDateTime.of(localDate, endTime);

                //Converting LocalDateTime to UTC timezone times
                ZonedDateTime startUTC = startDT.atZone(currentZoneID).withZoneSameInstant(ZoneId.of("UTC"));
                ZonedDateTime endUTC = endDT.atZone(currentZoneID).withZoneSameInstant(ZoneId.of("UTC"));
                apptStartTime = startUTC.format(timestampFormat);
                apptEndTime = endUTC.format(timestampFormat);

                //for time check
                newStartTimeLDT = startUTC.toLocalDateTime();
                newEndTimeLDT = endUTC.toLocalDateTime();
        
                if(apptTitle.isEmpty()){
                    formAlert("title");
                } else if(apptDesc.isEmpty()){
                    formAlert("description");
                } else if(apptContact == null){
                    formAlert("contact");
                } else if(apptType == null){
                    formAlert("type");
                } else if(localDate == null){
                    formAlert("date");
                } else if(editApptId==0){

                    //Set new Appointment variable called newAppt
                    Appointment newAppt;

                    //Create new Customer object called cust with form values as parameters
                    newAppt = new Appointment(apptTitle, apptDesc, apptType, apptStartTime, apptEndTime, apptContact);
                    newAppt.setUserId(UserDB.getUserId(currentUser.getUsername()));
                    newAppt.setUsername(currentUser.getUsername());

                    if(AppointmentDB.isValidApptTime(newStartTimeLDT, newEndTimeLDT, editApptId)){

                        //Saves values to Appointment DB table
                        AppointmentDB.commitAppointmentRecord(newAppt, currentUser);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("New Appointment Saved");
                        alert.setHeaderText("New Appointment Saved");
                        alert.setContentText("Your appointment has been saved!");
                        alert.showAndWait();
                        //Refresh the Appointment Table
                        updateAppmntTableView();

                        //Disable Appointment form fields/ComboBoxes and clear values after save
                        setApptFieldsDisabled(true);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Time Conflict");
                        alert.setHeaderText("Time Conflict");
                        alert.setContentText("The time frame you selected conflicts with a current appointment, please adjust your appointment time. Thanks.");
                        alert.showAndWait();

                    }

                } else {

                    Appointment appt = new Appointment(apptTitle, apptDesc, apptType, apptStartTime, apptEndTime, apptContact);

                    appt.setAppointmentId(editApptId);
                    appt.setCustomerId(CustomerDB.getCustomerId(apptContact));
                    appt.setUserId(UserDB.getUserId(appt.getUsername()));
                    appt.setUsername(appt.getUsername());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Update Confirmation");
                    alert.setHeaderText("Update Appointment?");
                    alert.setContentText("Are you sure you want to update this record?");
                    Optional<ButtonType> choice = alert.showAndWait();
                    if(choice.get() == ButtonType.OK){
                        if(AppointmentDB.isValidApptTime(newStartTimeLDT, newEndTimeLDT, editApptId)){
                            //Saves values to Appointment DB table
                            AppointmentDB.updateAppointmentRecord(appt, currentUser);
                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Appointment Saved");
                            alert.setHeaderText("Appointment Saved");
                            alert.setContentText("Your appointment has been updated!");
                            alert.showAndWait();

                            //Refresh the Appointment Table
                            updateAppmntTableView();

                            //Disable Appointment form fields/ComboBoxes and clear values after save
                            setApptFieldsDisabled(true);
                            editApptId = 0;
                        } else {
                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Time Conflict");
                            alert.setHeaderText("Time Conflict");
                            alert.setContentText("The time frame you selected conflicts with a current appointment, please adjust your appointment time. Thanks.");
                            alert.showAndWait();
                        }
                    }
                }
            }catch(Exception e){
                formAlert("start/end time");
                System.out.println(e.getMessage());
            }
        }catch(Exception e){
            formAlert("contact/type");
            e.getMessage();
        }
    }
    
    public void handleDateUpdate(){
        if(dateDP.getValue() != null){
            setStartTimesCB();
        }
    }
    
    public void updateAppmntTableView(){
        try{
            //Pull all valid appointments that are in the future no more than 30 days
            appointmentMonthList = AppointmentDB.getAllAppointmentRecords(30);
            
            //Pull all valid appointments that are in the future no more than 7 days
            appointmentWeekList = AppointmentDB.getAllAppointmentRecords(7);
        }catch(SQLException e){
            e.getMessage();
        }
        
        //Set items from ObservableList appointmentMonthList and appointmentWeekList into the TableView
        monthAppmntTable.setItems(appointmentMonthList);
        weekAppmntTable.setItems(appointmentWeekList);
        
        //Gets values from the ObservableList appointmentMonthList and sets them to the TableView Columns as needed
        //Lambda expressions used for these
        monthTitle.setCellValueFactory(cellData -> cellData.getValue().getTitleProperty());
        monthDescription.setCellValueFactory(cellData -> cellData.getValue().getDescriptionProperty());
        monthStart.setCellValueFactory(cellData -> cellData.getValue().getStartProperty());
        monthEnd.setCellValueFactory(cellData -> cellData.getValue().getEndProperty());
        monthType.setCellValueFactory(cellData -> cellData.getValue().getTypeProperty());
        monthContact.setCellValueFactory(cellData -> cellData.getValue().getContactProperty());
        monthUser.setCellValueFactory(cellData -> cellData.getValue().getUsernameProperty());
        
        //Gets values from the ObservableList appointmentWeekList and sets them to the TableView Columns as needed
        //Lambda expressions used for these
        weekTitle.setCellValueFactory(cellData -> cellData.getValue().getTitleProperty());
        weekDescription.setCellValueFactory(cellData -> cellData.getValue().getDescriptionProperty());
        weekStart.setCellValueFactory(cellData -> cellData.getValue().getStartProperty());
        weekEnd.setCellValueFactory(cellData -> cellData.getValue().getEndProperty());
        weekType.setCellValueFactory(cellData -> cellData.getValue().getTypeProperty());
        weekContact.setCellValueFactory(cellData -> cellData.getValue().getContactProperty());
        weekUser.setCellValueFactory(cellData -> cellData.getValue().getUsernameProperty());
    }
    
    public void setStartTimesCB() throws NullPointerException{

        startTimes.clear();
        
        LocalTime businessHoursStart = LocalTime.of(8,0);
        LocalTime businessHoursEnd = LocalTime.of(17,15);
        LocalDate selectedDate = dateDP.getValue();
        
        if(selectedDate.isEqual(currentDate)){
            do{
                if(currentTime.isBefore(businessHoursStart)){
                    startTimes.add(businessHoursStart.format(shortTimeFormat));
                    businessHoursStart = businessHoursStart.plusMinutes(15);
                } else {
                    businessHoursStart = businessHoursStart.plusMinutes(15);
                }
            } while(businessHoursStart.isBefore(businessHoursEnd));
            startTimes.remove(startTimes.size() - 1);
            startTimeComboBox.setItems(startTimes);
        }
        if(selectedDate.isAfter(currentDate)){
            do{
                startTimes.add(businessHoursStart.format(shortTimeFormat));
                businessHoursStart = businessHoursStart.plusMinutes(15);
            } while(businessHoursStart.isBefore(businessHoursEnd));
            startTimes.remove(startTimes.size() - 1);
            startTimeComboBox.setItems(startTimes);
        }
    }
    
    public void setApptFieldsDisabled(boolean bool){
        if(bool){
            titleTextField.clear();
            descTextField.clear();
            contactComboBox.setItems(null);
            typeComboBox.setItems(null);
            startTimeComboBox.setItems(null);
            endTimeComboBox.setItems(null);
            dateDP.setValue(null);
        }
        titleTextField.setDisable(bool);
        descTextField.setDisable(bool);
        dateDP.setDisable(bool);
        startTimeComboBox.setDisable(bool);
        endTimeComboBox.setDisable(bool);
        contactComboBox.setDisable(bool);
        typeComboBox.setDisable(bool);
        appmntSaveBtn.setDisable(bool);
        appmntCancelBtn.setDisable(bool);

    }
    
    public void formAlert(String field){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Form Error");
        alert.setHeaderText("Form Error");
        alert.setContentText("Please include a " + field + " for this appointment.");
        alert.showAndWait();
    }
   
}