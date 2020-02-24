package c195.util;

import c195.model.User;
import static c195.util.DBConnection.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CityDB {
    
    private static final String CITY_TABLE = "city";
    private static final String CITY = "city";
    private static final String CITY_ID = "cityId";
        
    private static final String COUNTRY_TABLE = "country";
    private static final String COUNTRY = "country";
    private static final String COUNTRY_ID = "countryId";
    
    
    public static ObservableList<String> getCityName(){
        ObservableList<String> cityArray = FXCollections.observableArrayList();
        Statement stmt;
        ResultSet results;
        try{
            stmt = dbConn.createStatement();
            results = stmt.executeQuery("SELECT " + CITY_TABLE + " FROM " + CITY);
            try{
                while(results.next()){
                    cityArray.add(results.getString(CITY));
                }
            } catch(SQLException e){
                e.getMessage();
            }
        } catch(SQLException e){
            e.getMessage();
        }
        return cityArray;
    }
    
    public static int getCityId(String city){
        int cityId = 0;
        Statement stmt;
        ResultSet results;
        try{
            stmt = dbConn.createStatement();
            results = stmt.executeQuery("SELECT " + CITY_ID + " FROM " + CITY + " WHERE " + CITY + "='" + city + "'");
            try{
                while(results.next()){
                    cityId = Integer.parseInt(results.getString(CITY_ID));
                }
            } catch(SQLException e){
                e.getMessage();
            }
        } catch(SQLException e){
            e.getMessage();
        }
        return cityId;
    }
    
    public static ObservableList<String> getCountryName(){
        ObservableList<String> countryArray = FXCollections.observableArrayList();
        Statement stmt;
        ResultSet results;
        try{
            stmt = dbConn.createStatement();
            results = stmt.executeQuery("SELECT " + COUNTRY_TABLE + " FROM " + COUNTRY);
            try{
                while(results.next()){
                    countryArray.add(results.getString(COUNTRY));
                }
            } catch(SQLException e){
                e.getMessage();
            }
        } catch(SQLException e){
            e.getMessage();
        }
        return countryArray;
    }
    
    public static int getCountryId(String city){
        int countryId = 0;
        Statement stmt;
        ResultSet results;
        try{
            stmt = dbConn.createStatement();
            results = stmt.executeQuery("SELECT " + COUNTRY_ID + " FROM " + CITY + " WHERE " + CITY + "='" + city + "'");
            try{
                while(results.next()){
                    countryId = Integer.parseInt(results.getString(COUNTRY_ID));
                }
            } catch(SQLException e){
                e.getMessage();
            }
        } catch(SQLException e){
            e.getMessage();
        }
        System.out.println("Retrieved Country ID: " + countryId);
        return countryId;
    }
}
