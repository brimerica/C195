package c195.util;

import c195.model.User;
import static c195.util.DBConnection.dbConn;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

public class UserDB {
    
    private static final String USER_TABLE = "user";
    private static final String USER_ID = "userId";
    private static final String USERNAME = "userName";
    private static final String PASSWORD = "password";
    
    private static ResourceBundle localRB = ResourceBundle.getBundle("c195.util/resource", Locale.getDefault());
    
    public static int getUserId(String userName){
        Statement stmt = null;
        ResultSet results = null;
        int userId = 0;
        try{
            stmt = dbConn.createStatement();
            results = stmt.executeQuery("SELECT " + USER_ID + " FROM " + USER_TABLE + " WHERE " + USERNAME + "='" + userName + "'");
            try{
                while(results.next()){
                    userId = Integer.parseInt(results.getString(USER_ID));
                }
            } catch(SQLException e){
                e.getMessage();
            }
            stmt.close();
        } catch (SQLException e){
            e.getMessage();
        }
        return userId;
    }
    
    public static String getUserName(int userId){
        Statement stmt = null;
        ResultSet results = null;
        String username = "";
        try{
            stmt = dbConn.createStatement();
            results = stmt.executeQuery("SELECT " + USERNAME + " FROM " + USER_TABLE + " WHERE " + USER_ID + "='" + userId + "'");
            try{
                while(results.next()){
                    username = results.getString(USERNAME);
                }
            } catch(SQLException e){
                e.getMessage();
            }
            stmt.close();
        } catch (SQLException e){
            e.getMessage();
        }
        return username;
    }
    
    public static ObservableList<String> getAllUsers(){
        ObservableList<String> usernames = FXCollections.observableArrayList();
        Statement stmt;
        ResultSet results;
        try{
            stmt = dbConn.createStatement();
            results = stmt.executeQuery("SELECT " + USERNAME + " FROM " + USER_TABLE);
            try{
                while(results.next()){
                    usernames.add(results.getString(USERNAME));
                }
            } catch(SQLException e){
                e.getMessage();
            }
        } catch(SQLException e){
            e.getMessage();
        }
        return usernames;
    }
    
    public static User userLogin(String username, String password){
        Statement stmt = null;
        ResultSet result = null;
        User user;
        try{
            stmt = dbConn.createStatement();
            result = stmt.executeQuery("SELECT * FROM user WHERE userName='" + username + "' AND password='" + password + "'");
            if(result.next()){
                user = new User(result.getInt("userId"),result.getString("userName"),result.getString("password"),true);
                try{
                    stmt.execute("UPDATE user SET active=1 WHERE userName='" + username + "'");
                    stmt.close();                    
                } catch (SQLException e){
                    e.getMessage();
                }

            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(localRB.getString("userErrorTitle"));
                alert.setHeaderText(localRB.getString("userErrorHeader"));
                alert.setContentText(username + " " + localRB.getString("userErrorContent"));
                alert.showAndWait();
                return null;
            }
        } catch (SQLException e){
            System.out.println("Error: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database connection Error");
            alert.setHeaderText("Database connection Error");
            alert.setContentText("There was a problem connecting to the database. Error: " + e.getMessage());
            alert.showAndWait();
            return null;
        }
        return user;
    }
    
    public static void setInactiveUser(User user){
        Statement stmt = null;
        user.setActive(false);
        String username = user.getUsername();
        try{
            stmt = dbConn.createStatement();
            stmt.execute("UPDATE user SET active=0 WHERE userName='" + username + "'");
            stmt.close();                    
        } catch (SQLException e){
            e.getMessage();
        }
    }
}
