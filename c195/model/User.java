package c195.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class User {
    
    private IntegerProperty userId;
    private StringProperty username;
    private StringProperty password;
    private BooleanProperty active;
    
    public User(int userId, String username, String password, boolean active){
        this.userId = new SimpleIntegerProperty(userId);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.active = new SimpleBooleanProperty(active);
    }
    
    public User(){
        
    }
    
    public void setUsername(String username){
        this.username.set(username);
    }
    
    public String getUsername(){
        return this.username.get();
    }
    
    public StringProperty getUsernameProperty(){
        return username;
    }
    
    public void setPassword(String password){
        this.password.set(password);
    }
    
    public String getPassword(){
        return this.password.get();
    }
    
    public StringProperty getPasswordProperty(){
        return password;
    }

    public int getUserId() {
        return this.userId.get();
    }

    public void setUserId(int userId) {
        this.userId.set(userId);
    }
    
    public IntegerProperty getUserIdProperty(){
        return userId;
    }

    public boolean isActive() {
        return this.active.get();
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }
    
    public BooleanProperty isActiveProperty(){
        return active;
    }
    
}
