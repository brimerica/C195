package c195.util;

import c195.model.Appointment;
import c195.model.Reports;
import c195.model.User;
import static c195.util.DBConnection.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AppointmentDB {
    private static final String APPOINTMENT_TABLE = "appointment";
    private static final String APPOINTMENT_ID = "appointmentId";
    private static final String CONTACT = "contact";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String TYPE = "type";
    private static final String START = "start";
    private static final String END = "end";
    private static final String URL = "url";
    private static final String LOCATION = "location";
    
    private static final String CUSTOMER_ID = "customerId";
    private static final String USER_ID = "userId";
    
    private static DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    private static ZoneId defaultzid = ZoneId.systemDefault();
    
    public static void commitAppointmentRecord(Appointment appt, User user){
        Statement stmt;
        Timestamp startSQLTS = Timestamp.valueOf(appt.getStart());
        Timestamp endSQLTS = Timestamp.valueOf(appt.getEnd());
        try{
            stmt = dbConn.createStatement();
            stmt.execute("INSERT INTO " + APPOINTMENT_TABLE + " (" +
                        CUSTOMER_ID + ", " +
                        USER_ID + ", " +
                        TITLE + ", " +
                        DESCRIPTION + ", " +
                        START + ", " +
                        END + ", " +
                        CONTACT + ", " +
                        TYPE + ", " +
                        URL + ", " +
                        LOCATION + ", " +
                        CREATE_DATE + ", " +
                        CREATED_BY + ", " +
                        LAST_UPDATED_BY + ") VALUES (" +
                        CustomerDB.getCustomerId(appt.getContact()) + ", " +
                        UserDB.getUserId(user.getUsername()) + ", '" +
                        appt.getTitle() + "', '" +
                        appt.getDescription() + "', '" +
                        startSQLTS + "', '" +
                        endSQLTS + "', '" +
                        appt.getContact() + "', '" +
                        appt.getType() + "', '', '', " +
                        CURRENT_TIMESTAMP + ", '" +
                        user.getUsername() + "', '" +
                        user.getUsername() + "')");
            stmt.close();                    
        } catch (SQLException e){
            e.getMessage();
        }
    }
    
    public static void updateAppointmentRecord(Appointment appt, User user){
        Statement stmt;
        Timestamp startSQLTS = Timestamp.valueOf(appt.getStart());
        Timestamp endSQLTS = Timestamp.valueOf(appt.getEnd());
        try{
            stmt = dbConn.createStatement();
            stmt.execute("UPDATE " + APPOINTMENT_TABLE + " SET " +
                        CUSTOMER_ID + "=" + appt.getCustomerId() + ", " +
                        CONTACT + "='" + appt.getContact() + "', " +
                        TITLE + "='" + appt.getTitle() + "', " +
                        DESCRIPTION + "='" + appt.getDescription() + "', " +
                        TYPE + "='" + appt.getType() + "', " +
                        START + "='" + startSQLTS + "', " +
                        END + "='" + endSQLTS + "', " +
                        LAST_UPDATED_BY + "='" + user.getUsername() + "' WHERE " +
                        APPOINTMENT_ID + "=" + appt.getAppointmentId());
            stmt.close();                    
        } catch (SQLException e){
            e.getMessage();
        }
    }
    
    public static void deleteAppointmentRecord(Appointment appt){
        Statement stmt;
        int apptId = appt.getAppointmentId();
        try{
            stmt = dbConn.createStatement();
            stmt.execute("DELETE FROM " + APPOINTMENT_TABLE + " WHERE " + APPOINTMENT_ID + "=" + apptId);
            stmt.close();                    
        } catch (SQLException e){
            e.getMessage();
        }        
    }
    
    public static int getCustomerId(String custName){
        Statement stmt;
        ResultSet results;
        int custId = 0;
        try{
            stmt = dbConn.createStatement();
            results = stmt.executeQuery("SELECT " + CUSTOMER_ID + " FROM " + APPOINTMENT_TABLE + " WHERE " + CONTACT + "='" + custName + "'");
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
    
    public static int getUserId(){
        int userId = 0;
        //Do more here
        return userId;
    }
    
    public static ObservableList<Appointment> getAllAppointmentRecords(int days) throws SQLException{
        Statement stmt;
        ResultSet results;
        Appointment appt;
        ObservableList<Appointment> apptList = FXCollections.observableArrayList();
        
        try{
            int custId, userId, apptId;
            String title, description, contact, type, createdBy;
            Timestamp start, end;
            stmt = dbConn.createStatement();
            Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC));
            Timestamp plusDays = Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC).plusDays(days));
            results = stmt.executeQuery("SELECT appointmentId, customerId, userId, title, description, contact, type, start, end, createdBy FROM appointment");       
            try{
                while(results.next()){
                    
                    apptId = results.getInt("appointmentId");
                    custId = results.getInt("customerId");
                    userId = results.getInt("userId");
                    title = results.getString("title");
                    description = results.getString("description");
                    contact = results.getString("contact");
                    type = results.getString("type");
                    start = results.getTimestamp("start");
                    end = results.getTimestamp("end");
                    createdBy = results.getString("createdBy");
                    
                    if(start.after(currentTime) && start.before(plusDays)){
                        ZonedDateTime utcStart = start.toLocalDateTime().atZone(ZoneId.of("UTC"));
                        ZonedDateTime localStart = utcStart.withZoneSameInstant(defaultzid);

                        ZonedDateTime utcEnd = end.toLocalDateTime().atZone(ZoneId.of("UTC"));
                        ZonedDateTime localEnd = utcEnd.withZoneSameInstant(defaultzid);

                        appt = new Appointment(title, description, type, localStart.format(dateTimeFormat), localEnd.format(dateTimeFormat), contact);
                        appt.setUserId(userId);
                        appt.setCustomerId(custId);
                        appt.setAppointmentId(apptId);
                        appt.setUsername(createdBy);
                        apptList.add(appt);
                    }
                }
            } catch(SQLException e){
                e.getMessage();
            }
            stmt.close();
        } catch (SQLException e){
            e.getMessage();
        }
        return apptList;
        
    }
    
    public static boolean isValidApptTime(LocalDateTime newStartLDT, LocalDateTime newEndLDT, int apptId){
        Statement stmt;
        ResultSet results;
        boolean isOpenTime = true;
        try{
            Timestamp currentStart, currentEnd;
            int recordApptId;
            stmt = dbConn.createStatement();
            results = stmt.executeQuery("SELECT start, end, appointmentId FROM appointment");
            try{
                while(results.next()){
                    recordApptId = results.getInt("appointmentId");
                    currentStart = results.getTimestamp("start");
                    currentEnd = results.getTimestamp("end");
                    if(recordApptId!=apptId && isOpenTime==true){
                        LocalDateTime currentStartLDT = currentStart.toLocalDateTime();
                        LocalDateTime currentEndLDT = currentEnd.toLocalDateTime();    
                        if((newStartLDT.isAfter(currentStartLDT) && newEndLDT.isBefore(currentEndLDT)) || 
                           (newEndLDT.isBefore(currentEndLDT) && newEndLDT.isAfter(currentStartLDT)) || 
                           (newStartLDT.isBefore(currentEndLDT) && newStartLDT.isAfter(currentStartLDT)) ||
                           (newStartLDT.isBefore(currentStartLDT) && newEndLDT.isAfter(currentEndLDT)) ||
                           (newStartLDT.isEqual(currentStartLDT)) || (newEndLDT.isEqual(currentEndLDT))){
                            isOpenTime = false;
                        } else {
                            isOpenTime = true;
                        }
                    }
                }
            } catch(SQLException ex){
                ex.getMessage();
            }
        } catch(SQLException ex){
            ex.getMessage();
        }
        return isOpenTime;
    }
    
    public static String isSoonAppt(){
        Statement stmt;
        ResultSet results;
        String apptContact = "";
        boolean isSoon = false;
        LocalDateTime currentDT = LocalDateTime.now(ZoneOffset.UTC);
        long difference = 0;
        try{
            Timestamp apptStart;
            stmt = dbConn.createStatement();
            results = stmt.executeQuery("SELECT start, contact FROM appointment");
            try{
                if(results.next() == false){
                    apptContact = "";
                } else {
                    do{
                        apptStart = results.getTimestamp("start");
                        LocalDateTime apptStartLDT = apptStart.toLocalDateTime();
                        difference = ChronoUnit.MINUTES.between(currentDT, apptStartLDT);
                        if(difference<15 && difference>0){
                            apptContact = results.getString("contact");
                            isSoon = true;
                        } else {
                            apptContact = "";
                        }
                    } while(results.next() && isSoon == false);
                }
            } catch(SQLException ex){
                ex.getMessage();
            }
        } catch(SQLException ex){
            ex.getMessage();
        }
        return apptContact;
    }
    
    public static ObservableList<Appointment> apptByConsultant(String user){
        Appointment appt;
        Statement stmt;
        ResultSet results;
        ObservableList<Appointment> apptList = FXCollections.observableArrayList();
        try{
            String title, description, contact, type, createdBy;
            Timestamp start, end;
            stmt = dbConn.createStatement();
            results = stmt.executeQuery("SELECT createdBy, title, description, type, contact, start, end FROM appointment WHERE createdBy='" + user + "'");       
            try{
                while(results.next()){
                    
                    title = results.getString("title");
                    description = results.getString("description");
                    contact = results.getString("contact");
                    type = results.getString("type");
                    start = results.getTimestamp("start");
                    end = results.getTimestamp("end");
                    createdBy = results.getString("createdBy");

                    
                    ZonedDateTime utcStart = start.toLocalDateTime().atZone(ZoneId.of("UTC"));
                    ZonedDateTime localStart = utcStart.withZoneSameInstant(defaultzid);

                    ZonedDateTime utcEnd = end.toLocalDateTime().atZone(ZoneId.of("UTC"));
                    ZonedDateTime localEnd = utcEnd.withZoneSameInstant(defaultzid);

                    appt = new Appointment();
                    appt.setTitle(title);
                    appt.setDescription(description);
                    appt.setType(type);
                    appt.setContact(contact);
                    appt.setStart(localStart.format(dateTimeFormat));
                    appt.setEnd(localEnd.format(dateTimeFormat));
                    appt.setUsername(createdBy);
                    apptList.add(appt);
                }
            } catch(SQLException e){
                e.getMessage();
            }
            stmt.close();
        } catch (SQLException e){
            e.getMessage();
        }
        return apptList;
    }
    
    public static ObservableList<Reports> getTypesByMonth(){
        Reports monthType;
        Statement stmt;
        ResultSet results;
        ObservableList<Reports> repList = FXCollections.observableArrayList();
        try{
            String month, type;
            int totalRecords;
            stmt = dbConn.createStatement();
            results = stmt.executeQuery("SELECT MONTHNAME(start) as monthName, COUNT(type) as total, type FROM appointment GROUP BY MONTH(start), type");
            try{
                while(results.next()){
                    
                    month = results.getString("monthName");
                    totalRecords = results.getInt("total");
                    type = results.getString("type");
                     
                    monthType = new Reports();
                    monthType.setMonth(month);
                    monthType.setType(type);
                    monthType.setTotalRecords(totalRecords);
                    
                    repList.add(monthType);
                }
            } catch(SQLException e){
                e.getMessage();
            }
        } catch(SQLException e){
            e.getMessage();
        }
        return repList;
    }
}
