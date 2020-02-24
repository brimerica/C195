package c195.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Customer {

    private StringProperty customerName = new SimpleStringProperty();
    private IntegerProperty customerId;
    private BooleanProperty active = new SimpleBooleanProperty();
    private StringProperty address;
    private StringProperty address2;
    private IntegerProperty addressId = new SimpleIntegerProperty();
    private StringProperty postalCode;
    private StringProperty phone;
    private StringProperty city;
    private StringProperty country;
    

    public Customer(){
        
    }
    
    public Customer(String customerName, String address, String address2, String postalCode, String phone, String city, String country) {
        setCustomerName(customerName);
        this.address = new SimpleStringProperty(address);
        this.address2 = new SimpleStringProperty(address2);
        this.postalCode = new SimpleStringProperty(postalCode);
        this.phone = new SimpleStringProperty(phone);
        this.city = new SimpleStringProperty(city);
        this.country = new SimpleStringProperty(country);
        this.customerId = new SimpleIntegerProperty();
        setActive(false);
    }

    public String getCustomerName() {
        return this.customerName.get();
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }
    
    public StringProperty getCustomerNameProperty(){
        return customerName;
    }

    public int getCustomerId() {
        return this.customerId.get();
    }

    public void setCustomerId(int customerId) {
        this.customerId.set(customerId);
    }
    
    public IntegerProperty getCustomerIdProperty(){
        return customerId;
    }
    
    public int getAddressId(){
        return this.addressId.get();
    }
    
    public void setAddressId(int addressId){
        this.addressId.set(addressId);
    }
    
    public boolean isActive(){
        return this.active.get();
    }

    public BooleanProperty isActiveProperty() {
        return active;
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }
    
     public String getAddress() {
        return this.address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }
    
    public StringProperty getAddressProperty(){
        return address;
    }

    public String getAddress2() {
        return this.address2.get();
    }

    public void setAddress2(String address2) {
        this.address2.set(address2);
    }
    
    public StringProperty getAddress2Property(){
        return address2;
    }

    public String getPostalCode() {
        return this.postalCode.get();
    }

    public void setPostalCode(String postalCode) {
        this.postalCode.set(postalCode);
    }
    
    public StringProperty getPostalCodeProperty(){
        return postalCode;
    }

    public String getPhone() {
        return this.phone.get();
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }
    
    public StringProperty getPhoneProperty(){
        return phone;
    }
    
    public String getCity() {
        return this.city.get();
    }

    public void setCity(String city) {
        this.city.set(city);
    }
    
    public StringProperty getCityProperty(){
        return city;
    }

    public String getCountry() {
        return this.country.get();
    }

    public void setCountry(String country) {
        this.country.set(country);
    }
    
    public StringProperty getCountryProperty(){
        return country;
    }
}
