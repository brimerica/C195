package c195.util;

import c195.model.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    
    User currentUser;

    private static final String databaseName = "U05mxC";
    private static final String DB_URL = "jdbc:mysql://3.227.166.251/" + databaseName;
    private static final String dbUsername = "U05mxC";
    private static final String dbPassword = "53688548942";
    private static final String dbDriver = "com.mysql.jdbc.Driver";
    
    static final String CREATE_DATE = "createDate";
    static final String CREATED_BY = "createdBy";
    static final String LAST_UPDATED_BY = "lastUpdateBy";
    static final String ACTIVE = "active";
    static final String CURRENT_TIMESTAMP = "CURRENT_TIMESTAMP";
    
    static Connection dbConn;
    
    public static void makeConnection() throws ClassNotFoundException, SQLException, Exception {
        Class.forName(dbDriver);
        dbConn = DriverManager.getConnection(DB_URL, dbUsername, dbPassword);
        System.out.println("Connection Successful.");
    }
    
    public static void closeConnection() throws ClassNotFoundException, SQLException, Exception {
            dbConn.close();
            System.out.println("Connection Closed!");
    }
    
    public static Connection getConn(){
        return dbConn;
    }
    
}
