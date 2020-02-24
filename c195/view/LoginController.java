package c195.view;

import c195.Main;
import c195.model.User;
import c195.util.AppointmentDB;
import c195.util.UserDB;
import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class LoginController implements Initializable {
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private Label username;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Label password;
    
    @FXML
    private Label locale;
    
    @FXML
    private Label appointment;
        
    @FXML
    private Label software;
    
    @FXML
    private Button loginButton;
    
    @FXML
    private Button exitButton;
    
    public static User currentUser;
    Main mainApp = new Main();
    private static DateTimeFormatter UTCDateTimeDTF = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss z");
    private static LocalDateTime currentDateTime = LocalDateTime.now();
    private static ZoneId currentZoneID = ZoneId.systemDefault();
    private static ZonedDateTime UTCDateTime = currentDateTime.atZone(currentZoneID).withZoneSameInstant(ZoneId.of("UTC"));
    
    private static ResourceBundle localRB = ResourceBundle.getBundle("c195.util/resource", Locale.getDefault());
    
    @FXML
    private void handleLoginButtonAction(ActionEvent event) throws Exception {
        String checkUsername = usernameField.getText();
        String checkPassword = passwordField.getText();
        currentUser = UserDB.userLogin(checkUsername, checkPassword);
        if(currentUser!=null){
            ((Node) event.getSource()).getScene().getWindow().hide();
            mainApp.showMenu(currentUser);
            mainApp.showAppointmentScreen(currentUser);
            logging(checkUsername, "valid");
            if(!AppointmentDB.isSoonAppt().equals("")){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Appointment upcoming");
                alert.setHeaderText("Appointment upcoming");
                alert.setContentText("There is an appointment scheduled within the next 15 minutes with " + AppointmentDB.isSoonAppt() + "!");
                alert.showAndWait();
            }
        } else{
            logging(checkUsername, "error");
        }
    }
    
    @FXML
    private void handleExitButtonAction(ActionEvent event) throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(localRB.getString("applicationExit"));
        alert.setHeaderText(localRB.getString("applicationExit"));
        alert.setContentText(localRB.getString("applicationExitBody"));
        Optional<ButtonType> choice = alert.showAndWait();
            if(choice.get() == ButtonType.OK){
                logging(null, "loginexit");
                Platform.exit();
            }
    }
    
    public static void logging(String user, String status) throws IOException{
        String filepath = "src/c195/logs/logsFile.txt";
        FileWriter filewriter = new FileWriter(filepath, true);
        PrintWriter logfile = new PrintWriter(filewriter);
        if(status.equals("valid")){
            logfile.println(UTCDateTime.format(UTCDateTimeDTF) + " -- Successful login of user " + user);
            logfile.close();
        } else if(status.equals("error")){
            logfile.println(UTCDateTime.format(UTCDateTimeDTF) + " -- LOGIN ERROR: " + user + " is not a valid username or bad password." );
            logfile.close();
        } else if(status.equals("logout")){
            logfile.println(UTCDateTime.format(UTCDateTimeDTF) + " -- " + user + " logged out of the application.");
            logfile.close();
        } else if(status.equals("exit")){
            logfile.println(UTCDateTime.format(UTCDateTimeDTF) + " -- " + user + " exited the application.");
            logfile.close();
        } else {
            logfile.println(UTCDateTime.format(UTCDateTimeDTF) + " -- Application exited before login.");
            logfile.close();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rb = ResourceBundle.getBundle("c195.util/resource", Locale.getDefault());
        username.setText(rb.getString("Username"));
        password.setText(rb.getString("Password"));
        locale.setText(rb.getString("Language"));
        loginButton.setText(rb.getString("login"));
        exitButton.setText(rb.getString("exit"));
        appointment.setText(rb.getString("appointment"));
        software.setText(rb.getString("software"));
    }
    
}
