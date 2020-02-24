package c195.util;

import c195.model.Customer;
import c195.model.User;
import static c195.util.DBConnection.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddressDB {
        
    private static final String ADDRESS_TABLE = "address";
    private static final String ADDRESS_ID = "addressId";
    private static final String ADDRESS = "address";
    private static final String ADDRESS2 = "address2";
    private static final String POSTAL_CODE = "postalCode";
    private static final String PHONE = "phone";
    
    private static final String CUSTOMER_TABLE = "customer";
    private static final String CUSTOMER_ID = "customerId";
    private static final String CITY_ID = "cityId";
    
    public static void commitAddressRecord(Customer cust, User user){
        Statement stmt;
        try{
            stmt = dbConn.createStatement();
            stmt.execute("INSERT INTO " + ADDRESS_TABLE + " (" +
                        ADDRESS + ", " +
                        ADDRESS2 + ", " +
                        CITY_ID + ", " +
                        PHONE + ", " +
                        POSTAL_CODE + ", " +
                        CREATE_DATE + ", " +
                        CREATED_BY + ", " +
                        LAST_UPDATED_BY + ") VALUES ('" +
                        cust.getAddress() + "', '" +
                        cust.getAddress2() + "', " +
                        CityDB.getCityId(cust.getCity()) + ", '" +
                        cust.getPhone() + "', '" +
                        cust.getPostalCode() + "', " +
                        CURRENT_TIMESTAMP + ", '" +
                        user.getUsername() + "', '" +
                        user.getUsername() + "')");
            stmt.close(); 
        } catch (SQLException e){
            e.getMessage();
        }
    }
    
    public static void updateAddressRecord(Customer cust, User user){
        Statement stmt;
        try{
            stmt = dbConn.createStatement();
            stmt.execute("UPDATE " + ADDRESS_TABLE + " SET " +
                        ADDRESS + "='" + cust.getAddress() + "', " +
                        ADDRESS2 + "='" + cust.getAddress2() + "', " +
                        CITY_ID + "=" + CityDB.getCityId(cust.getCity()) + ", " +
                        PHONE + "='" + cust.getPhone() + "', " +
                        POSTAL_CODE + "='" + cust.getPostalCode() + "', " +
                        LAST_UPDATED_BY + "='" + user.getUsername() + "' WHERE " +
                        ADDRESS_ID + "=" + cust.getAddressId());
            stmt.close(); 
        } catch (SQLException e){
            e.getMessage();
        }
    }
    
    public static int newAddressId(){
        Statement stmt;
        ResultSet results;
        int custId = 0;
        try{
            stmt = dbConn.createStatement();
            results = stmt.executeQuery("SELECT " + ADDRESS_ID + " FROM " + ADDRESS_TABLE + " WHERE " + ADDRESS_ID + "= (SELECT MAX(" + ADDRESS_ID + ") FROM " + ADDRESS_TABLE + ")");
            try{
                while(results.next()){
                    custId = Integer.parseInt(results.getString(ADDRESS_ID));
                }
            } catch(SQLException e){
                e.getMessage();
            }
            stmt.close();
        } catch (SQLException e){
            e.getMessage();
        }
        return custId;
    }
    
    public static int getAddressId(int custId){
        Statement stmt;
        ResultSet results;
        int addId = 0;
        try{
            stmt = dbConn.createStatement();
            results = stmt.executeQuery("SELECT " + ADDRESS_ID + " FROM " + CUSTOMER_TABLE + " WHERE " + CUSTOMER_ID + "='" + custId + "'");
            try{
                while(results.next()){
                    addId = Integer.parseInt(results.getString(ADDRESS_ID));
                }
            } catch(SQLException e){
                e.getMessage();
            }
            stmt.close();
        } catch (SQLException e){
            e.getMessage();
        }
        return addId;
    }
    
}
