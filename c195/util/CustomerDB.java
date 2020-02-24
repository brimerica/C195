package c195.util;

import c195.model.Customer;
import c195.model.Reports;
import c195.model.User;
import static c195.util.DBConnection.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CustomerDB {
    
    private static final String ADDRESS_ID = "addressId";
    private static final String ADDRESS_TABLE = "address";
    
    private static final String CUSTOMER_TABLE = "customer";
    private static final String CUSTOMER_NAME = "customerName";
    private static final String CUSTOMER_ID = "customerId";
    
    private static final String APPOINTMENT_TABLE = "appointment";
    
    public static void commitCustomerRecord(Customer cust, int addId, User user){
        Statement stmt;
        int active;
        if(user.isActive()){
            active = 1;
        } else {
            active = 0;
        }
        try{
            stmt = dbConn.createStatement();
            stmt.execute("INSERT INTO " + CUSTOMER_TABLE + " (" +
                        CUSTOMER_NAME + ", " +
                        ADDRESS_ID + ", " +
                        ACTIVE + ", " +
                        CREATE_DATE + ", " +
                        CREATED_BY + ", " +
                        LAST_UPDATED_BY + ") VALUES ('" +
                        cust.getCustomerName() + "', " +
                        addId + ", " +
                        active + ", " +
                        CURRENT_TIMESTAMP + ", '" +
                        user.getUsername() + "', '" +
                        user.getUsername() + "')");
            stmt.close();                    
        } catch (SQLException e){
            e.getMessage();
        }
    }
    
    public static void updateCustomerRecord(Customer cust, User user){
        Statement stmt;
        try{
            stmt = dbConn.createStatement();
            stmt.execute("UPDATE " + CUSTOMER_TABLE + " SET " +
                        CUSTOMER_NAME + "='" + cust.getCustomerName() + "', " +
                        LAST_UPDATED_BY + "='" + user.getUsername() + "' WHERE " +
                        CUSTOMER_ID + "=" + cust.getCustomerId());
            stmt.close();                    
        } catch (SQLException e){
            e.getMessage();
        }
    }
    
    public static void deleteCustomerRecord(Customer cust){
        Statement stmt;
        int custId = cust.getCustomerId();
        int addId = AddressDB.getAddressId(cust.getCustomerId());
        try{
            stmt = dbConn.createStatement();
            stmt.execute("DELETE FROM " + APPOINTMENT_TABLE + " WHERE " + CUSTOMER_ID + "=" + custId);
            stmt.execute("DELETE FROM " + CUSTOMER_TABLE + " WHERE " + CUSTOMER_ID + "=" + custId);
            stmt.execute("DELETE FROM " + ADDRESS_TABLE + " WHERE " + ADDRESS_ID + "=" + addId);
            stmt.close();                    
        } catch (SQLException e){
            e.getMessage();
        }
    }
    
    public static int getCustomerId(String custName){
        Statement stmt = null;
        ResultSet results = null;
        int custId = 0;
        try{
            stmt = dbConn.createStatement();
            results = stmt.executeQuery("SELECT " + CUSTOMER_ID + " FROM " + CUSTOMER_TABLE + " WHERE " + CUSTOMER_NAME + "='" + custName + "'");
            try{
                while(results.next()){
                    custId = Integer.parseInt(results.getString(CUSTOMER_ID));
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
    
    public static int newCustomerId(){
        Statement stmt;
        ResultSet results;
        int custId = 0;
        try{
            stmt = dbConn.createStatement();
            results = stmt.executeQuery("SELECT " + CUSTOMER_ID + " FROM " + CUSTOMER_TABLE + " WHERE " + CUSTOMER_ID + "= (SELECT MAX(" + CUSTOMER_ID + ") FROM " + CUSTOMER_TABLE + ")");
            try{
                while(results.next()){
                    custId = Integer.parseInt(results.getString(CUSTOMER_ID));
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
    
    public static ObservableList<String> getCustomerName(){
        ObservableList<String> customerArray = FXCollections.observableArrayList();
        Statement stmt;
        ResultSet results;
        try{
            stmt = dbConn.createStatement();
            results = stmt.executeQuery("SELECT " + CUSTOMER_NAME + " FROM " + CUSTOMER_TABLE);
            try{
                while(results.next()){
                    customerArray.add(results.getString(CUSTOMER_NAME));
                }
            } catch(SQLException e){
                e.getMessage();
            }
        } catch(SQLException e){
            e.getMessage();
        }
        return customerArray;
    }
    
    public static ObservableList<Customer> getAllCustomerRecords() throws SQLException {
        Statement stmt;
        ResultSet results;
        Customer cust;
        ObservableList<Customer> custList = FXCollections.observableArrayList();
        try{
            int addId;
            int custId;
            String customerName, address, address2, postalCode, phone, city, country;
            stmt = dbConn.createStatement();
            
            results = stmt.executeQuery("SELECT cu.customerId, a.addressId, customerName, address, address2, phone, ci.city, postalCode, co.country " +
                                        "FROM customer cu " +
                                        "JOIN address a ON cu.addressId = a.addressId " +
                                        "JOIN city ci ON a.cityId = ci.cityId " +
                                        "JOIN country co ON ci.countryId = co.countryId");       
            try{
                while(results.next()){
                    custId = results.getInt("cu.customerId");
                    addId = results.getInt("a.addressId");
                    customerName = results.getString("customerName");
                    address = results.getString("address");
                    address2 = results.getString("address2");
                    postalCode = results.getString("postalCode");
                    phone = results.getString("phone");
                    city = results.getString("ci.city");
                    country = results.getString("co.country");

                    //System.out.println("getAllCustomerRecord results: " + addId + ", " + customerName + ", " + address + ", " + address2 + ", " + postalCode + ", " + phone + ", " + city + ", " + country);
                    cust = new Customer(customerName, address, address2, postalCode, phone, city, country);
                    cust.setAddressId(addId);
                    cust.setCustomerId(custId);
                    custList.add(cust);
                }
            } catch(SQLException e){
                e.getMessage();
            }
            stmt.close();
        } catch (SQLException e){
            e.getMessage();
        }
        return custList;
        
    }
    
    public static ObservableList<Reports> getCityCount(){
        Statement stmt;
        ResultSet results;
        Reports rep;
        ObservableList<Reports> repList = FXCollections.observableArrayList();
        try{
            int total;
            String city, country;
            stmt = dbConn.createStatement();
            
            results = stmt.executeQuery("SELECT COUNT(customerName) as total, ci.city, country " +
                                        "FROM customer cu " +
                                        "JOIN address a ON cu.addressId = a.addressId " +
                                        "JOIN city ci ON a.cityId = ci.cityId " +
                                        "JOIN country co ON ci.countryId = co.countryId " +
                                        "GROUP BY ci.city");
            try{
                while(results.next()){
                    total = results.getInt("total");
                    city = results.getString("ci.city");
                    country = results.getString("country");
                    rep = new Reports();
                    rep.setCity(city);
                    rep.setTotalRecords(total);
                    rep.setCountry(country);
                    repList.add(rep);
                }
            }catch(SQLException e){
                e.getMessage();
            }
        }catch(SQLException e){
            e.getMessage();
        }
        return repList;
    }
    
}
