package c195;

import c195.model.User;
import c195.util.DBConnection;
import c195.view.AppointmentViewController;
import c195.view.CustomersViewController;
import c195.view.LogsViewController;
import c195.view.MenuController;
import c195.view.ReportsViewController;
import java.io.IOException;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    
    private BorderPane menu;
    private Stage mainWindow;
    private User currentUser;
    private Main mainApp;
    
    @Override
    public void start(Stage loginStage) throws Exception {
        showLoginScreen(loginStage);
    }

    public static void main(String[] args) {
        try {
            DBConnection.makeConnection();       
            launch(args);
            DBConnection.closeConnection();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
    public void showLoginScreen(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/Login.fxml"));
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }
    
    public void showMenu(User user){
        this.currentUser = user;
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/Menu.fxml"));
            menu = (BorderPane) loader.load();
            
            mainWindow = new Stage();
            
            // Show the scene containing the menu layout.
            Scene scene = new Scene(menu);
            mainWindow.setTitle("Appointment Scheduler");
            mainWindow.setScene(scene);
            mainWindow.setMinHeight(768);
            mainWindow.setMinWidth(1024);
            
            
            // Give the controller access to the main app.
            MenuController mController = loader.getController();
            mController.transferInfo(this, user);
            
            mainWindow.show();
        } catch (IOException e) {
            e.getMessage();
        }
    }
    
    public void showAppointmentScreen(User user){
        this.currentUser = user;
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/AppointmentView.fxml"));
            AnchorPane appmntScreen = (AnchorPane) loader.load();

            menu.setCenter(appmntScreen);
            
            // Give the controller access to the main app.
            AppointmentViewController avController = loader.getController();
            avController.transferInfo(this, user);
            
        } catch (IOException e) {
            e.getMessage();
        }
    }
    
    public void showCustomersScreen(User user){
        this.currentUser = user;
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/CustomersView.fxml"));
            AnchorPane custScreen = (AnchorPane) loader.load();

            menu.setCenter(custScreen);
            
            // Give the controller access to the main app.
            CustomersViewController cvController = loader.getController();
            cvController.transferInfo(this, user);
            
        } catch (IOException e) {
            e.getMessage();
        }
    }
    
    public void showReportsScreen(User user){
        this.currentUser = user;
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/ReportsView.fxml"));
            AnchorPane reportScreen = (AnchorPane) loader.load();

            menu.setCenter(reportScreen);
            
            // Give the controller access to the main app.
            ReportsViewController rvController = loader.getController();
            rvController.transferInfo(this, user);
            
        } catch (IOException e) {
            e.getMessage();
        }
    }
    
    public void showLogsScreen(User user){
        this.currentUser = user;
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/LogsView.fxml"));
            AnchorPane reportScreen = (AnchorPane) loader.load();

            menu.setCenter(reportScreen);
            
            // Give the controller access to the main app.
            LogsViewController lvController = loader.getController();
            lvController.transferInfo(this, user);
            
        } catch (IOException e) {
            e.getMessage();
        }
    }
    
}
