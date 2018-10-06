package model;
import java.util.*;
import utilities.DateTime;
//import java.time.*;
//import java.time.format.DateTimeFormatter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class FlexiRentSystem {

    
    private ArrayList<Property> properties = new ArrayList<Property>();
    
    private Property property;
    
    private String propertyType;
    private String propertyID;
    private int streetNo;
    private String streetName ;
    private String suburb;
    private int bedNum = 3;
    private DateTime lastMaintainDate;
    private DateTime startMaintenance;
    private int status = 2;
    private String image;
    private String description;
    private final String checkDate = "(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/((19|20)\\d\\d)";
    //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    //LocalDate dateTime = LocalDate.parse(str, formatter);
    
    
    public ArrayList<Property> getPropertyList() {
        return properties;
    }
    
    public void getMainList() {
    
        Connection conn = null;
        String dateL = "";
        String dateS = "";
        
        try {
            // db parameters
            String url = "jdbc:sqlite:src/database/FlexiData.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            //System.out.println("Connection to SQLite has been established.");
            
            String sql = "SELECT propertyID, streetNo, streetName, " + 
                    "suburb, bedNum, lastMaintainDate, startMaintenance, "
                    + "image, description, status FROM Property";
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
           
           // loop through the result set
           while (rs.next()) {             
               propertyID = rs.getString("propertyID");
               streetNo = rs.getInt("streetNo");               
               streetName = rs.getString("streetName");
               suburb = rs.getString("suburb");
               bedNum = rs.getInt("bedNum");
               dateL = rs.getString("lastMaintainDate");
               dateS = rs.getString("startMaintenance");
               image = rs.getString("image");               
               description = rs.getString("description"); 
               status = rs.getInt("status");
               
               //Parse date format
               lastMaintainDate = checkDayFormat(dateL);                             
               
               if(propertyID.startsWith("A")) {
                   property = new Apartment(propertyID, streetNo, streetName, 
                           suburb, bedNum, lastMaintainDate, 
                           status, image, description);
                   if(dateS != null) {
                       startMaintenance = checkDayFormat(dateS);
                   property.setStartMaintain(startMaintenance);
                   }
               }
               else {
                   property = new PremiumSuite(propertyID, streetNo, streetName, 
                           suburb, bedNum, lastMaintainDate, 
                           status, image, description);
                   if(dateS != null) {
                       startMaintenance = checkDayFormat(dateS);
                   property.setStartMaintain(startMaintenance);
                   }
               }
               properties.add(property); 
           
           }
          //TODO TYPE NOT MATCH EXCEPTION  
           
           
           for(Property p : properties) {
               System.out.println(p.getPropertyID());
           }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }      
        
    }
        
    private DateTime checkDayFormat(String in) {
        
        if(in.matches(checkDate)) {            
            String[] seperate = in.split("/");
            int d = Integer.parseInt(seperate[0]);
            int m = Integer.parseInt(seperate[1]);
            int y = Integer.parseInt(seperate[2]);           
            DateTime day = new DateTime(d,m,y);
            return day;
            
            
        }
        else {
            //TODO DATEFORMATE EXCEPTION
            System.out.println("Invalid input format.");
            return null;
        }
    }
       
        
 

    
        
        
}
