package c195.view;

import c195.Main;
import c195.model.User;
import c195.util.DBConnection;
import c195.util.UserDB;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;


public class MenuController implements Initializable {

    @FXML
    private MenuItem menuLogout;
    
    @FXML
    private MenuItem menuAppointment;
    
    @FXML
    private Parent menuBorderPane;

    @FXML
    private Menu menuUser;
    
    @FXML
    private Menu menuTimeZone;

    Main mainApp;
    User currentUser;
    TimeZone currentTZ = TimeZone.getDefault();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void handleMenuExit(ActionEvent event) throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Application");
        alert.setHeaderText("Exit Application?");
        alert.setContentText("Are you sure you want to exit out of the application?");
        Optional<ButtonType> choice = alert.showAndWait();
        if(choice.get() == ButtonType.OK){
            UserDB.setInactiveUser(currentUser);
            LoginController.logging(currentUser.getUsername(), "exit");
            Platform.exit();
        }
    }
    
    @FXML
    private void handleMenuLogout(ActionEvent event) throws Exception{
        UserDB.setInactiveUser(currentUser);
        LoginController.logging(currentUser.getUsername(), "logout");
        Stage mStage = (Stage) menuBorderPane.getScene().getWindow();
        mStage.hide();
        
        Stage lStage = new Stage();
        mainApp.showLoginScreen(lStage);
    }
    
    public void transferInfo(Main app, User user){
        this.currentUser = user;
        this.mainApp = app;
        
        menuLogout.setText("Logout " + user.getUsername());
        menuUser.setText("(Currently Logged in: " + user.getUsername() + " )");
        menuTimeZone.setText("Current Time Zone - " + currentTZ.getDisplayName());
    }
    
    @FXML
    private void handleMenuAppointment(ActionEvent event){ 
        mainApp.showAppointmentScreen(currentUser);
    }
    
    @FXML
    private void handleMenuCustomer(ActionEvent event){ 
        mainApp.showCustomersScreen(currentUser);
    }
    
        @FXML
    private void handleMenuReports(ActionEvent event){ 
        mainApp.showReportsScreen(currentUser);
    }
    
        @FXML
    private void handleMenuLogs(ActionEvent event){ 
        mainApp.showLogsScreen(currentUser);
    }
    
}
