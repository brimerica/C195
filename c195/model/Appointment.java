package c195.model;

import c195.util.AppointmentDB;
import c195.util.UserDB;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Appointment {
    
    private final IntegerProperty appointmentId = new SimpleIntegerProperty();
    private final IntegerProperty customerId;
    private final IntegerProperty userId = new SimpleIntegerProperty();
    private final StringProperty title;
    private final StringProperty description;
    //private final StringProperty location;
    private final StringProperty type;
    //private final StringProperty url;
    private final StringProperty start;
    private final StringProperty end;
    private final StringProperty contact;
    
    private StringProperty userName = new SimpleStringProperty();
    
    public Appointment(){
        this.customerId = new SimpleIntegerProperty();
        this.title = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.type = new SimpleStringProperty();
        this.start = new SimpleStringProperty();
        this.end = new SimpleStringProperty();
        this.contact = new SimpleStringProperty();
    }

    public Appointment(String title, String description, String type, String start, String end, String contact) {
        this.customerId = new SimpleIntegerProperty(AppointmentDB.getCustomerId(contact));
        //this.userId = new SimpleIntegerProperty(UserDB.getUserId(username));
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.type = new SimpleStringProperty(type);
        this.start = new SimpleStringProperty(start);
        this.end = new SimpleStringProperty(end);
        this.contact = new SimpleStringProperty(contact);
        //this.userName = new SimpleStringProperty(UserDB.getUserName(getUserId()));
    }
    
    public String getUsername(){
        return this.userName.get();
    }
    
    public void setUsername(String username){
        this.userName.set(username);
    }
    
    public StringProperty getUsernameProperty(){
        return userName;
    }
    
    public int getAppointmentId(){
        return this.appointmentId.get();
    }
    
    public IntegerProperty getAppointmentIdProperty(){
        return appointmentId;
    }
    
    public void setAppointmentId(int appointmentId){
        this.appointmentId.set(appointmentId);
    }
    
    public int getCustomerId(){
        return this.customerId.get();
    }

    public IntegerProperty getCustomerIdProperty() {
        return customerId;
    }
    
    public void setCustomerId(int customerId){
        this.customerId.set(customerId);
    }
    
    public int getUserId(){
        return this.userId.get();
    }

    public IntegerProperty getUserIdProperty() {
        return userId;
    }
    
    public void setUserId(int userId){
        this.userId.set(userId);
    }
    
    public String getTitle() {
        return this.title.get();
    }

    public StringProperty getTitleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }
    
    public String getDescription(){
        return this.description.get();
    }

    public StringProperty getDescriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }
    
//    public String getLocation(){
//        return this.location.get();
//    }
//
//    public StringProperty getLocationProperty() {
//        return location;
//    }
//
//    public void setLocation(String location) {
//        this.location.set(location);
//    }
    
    public String getType(){
        return this.type.get();
    }

    public StringProperty getTypeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }
    
//    public String getUrl(){
//        return this.url.get();
//    }
//
//    public StringProperty getUrlProperty() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url.set(url);
//    }
    
    public String getContact(){
        return this.contact.get();
    }
    
    public StringProperty getContactProperty(){
        return contact;
    }
    
    public void setContact(String contact){
        this.contact.set(contact);
    }
    
    public String getStart(){
        return this.start.get();
    }
    
    public StringProperty getStartProperty(){
        return start;
    }
    
    public void setStart(String start){
        this.start.set(start);
    }
    
    public String getEnd(){
        return this.end.get();
    }
    
    public StringProperty getEndProperty(){
        return end;
    }
    
    public void setEnd(String end){
        this.end.set(end);
    }
    
    
}
