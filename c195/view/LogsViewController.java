/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195.view;

import c195.Main;
import c195.model.User;
import java.awt.Desktop;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author brime
 */
public class LogsViewController implements Initializable {
    
    @FXML
    TextArea logview;
    
    @FXML
    Button logfileBtn;

    private User currentUser;
    private Main mainApp;
    private File logfile = new File("src/c195/logs/logsFile.txt");
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Scanner s = new Scanner(logfile);
            while (s.hasNext()) {
              logview.appendText(s.nextLine()+"\n");
            }
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        }
    }
    
    public void transferInfo(Main app, User user){
        this.currentUser = user;
        this.mainApp = app;    
    }
    
    @FXML
    public void handleLogView(){
        if(Desktop.isDesktopSupported()){
            try{
                Desktop.getDesktop().open(logfile);
            } catch (IOException e) {
                e.getMessage();
            }
        }
    }    
}
