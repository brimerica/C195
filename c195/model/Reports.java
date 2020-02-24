package c195.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Reports {
    
    private StringProperty type = new SimpleStringProperty();
    private IntegerProperty totalRecords = new SimpleIntegerProperty();
    private StringProperty month = new SimpleStringProperty();
    
    private StringProperty city = new SimpleStringProperty();
    private StringProperty country = new SimpleStringProperty();

    public Reports() {
    }
    
    public StringProperty getTypeProperty(){
        return type;
    }

    public String getType() {
        return this.type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }
    
    public int getTotalRecords(){
        return this.totalRecords.get();
    }

    public IntegerProperty getTotalRecordsProperty() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords.set(totalRecords);
    }
    
    public String getMonth(){
        return this.month.get();
    }

    public StringProperty getMonthProperty() {
        return month;
    }

    public void setMonth(String month) {
        this.month.set(month);
    }
    
     public StringProperty getCityProperty(){
        return city;
    }

    public String getCity() {
        return this.city.get();
    }

    public void setCity(String city) {
        this.city.set(city);
    }
    
    public StringProperty getCountryProperty(){
        return country;
    }

    public String getCountry() {
        return this.country.get();
    }

    public void setCountry(String country) {
        this.country.set(country);
    }
}
