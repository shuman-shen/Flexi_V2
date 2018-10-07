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

    
    //private ArrayList<Property> properties = new ArrayList<Property>();
    private HashMap<String, Property> properties;
    private ArrayList<String> filteredID;    
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
    
    public FlexiRentSystem() {
        getMainList();
        //filteredList = new ArrayList<Property>(properties);
    }
    
    
    public HashMap<String, Property> getPropertyList() {
        return properties;
    }
    
    public ArrayList<String> getFilteredID(){
        return filteredID;
    }
    
    public void getFilterList(String propertyType, int bedNum, int status, String suburb) {
        
        Connection conn = null;
        String pType = "";
        String bNum = "";
        String cType = "";
        String s = "";
        ArrayList<String> type = new ArrayList<String>();
        String all = "";
        
        if (propertyType.equals("Apartment")) {
            pType = "propertyID like \'A%\'";
            type.add(pType);
        }
        else if (propertyType.startsWith("Prem")) {
            pType = "propertyID like \"S%\"";
            type.add(pType);
        }

        if(bedNum != 0) {
            bNum = "bedNum = "+bedNum;
            type.add(bNum);
        }
        
        if(status != 0) {
            cType = "status = " + status;
            type.add(cType);
        }
        if(suburb.equals("General")== false) {
            s = "suburb = \'" + suburb +"\'";
            type.add(s);
        }
        
        
        
        if (type.size() == 1 ) {
            all = type.get(0);
            System.out.println(all);
        }
        else {
            for(int i =0; i<(type.size()-1); i++) {
                all = all + " " + type.get(i) + " AND ";
            }
            all = all +type.get(type.size()-1);
            System.out.println(all);
        }
        
        filteredID = new ArrayList<String>();
        
        try {
            // db parameters
            String url = "jdbc:sqlite:src/database/FlexiData.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            //System.out.println("Connection to SQLite has been established.");
            
            String sql = "SELECT propertyID FROM Property WHERE " + all;
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
           
           // loop through the result set
           while (rs.next()) {             
               propertyID = rs.getString("propertyID");
               //System.out.print(propertyID + ":");
               filteredID.add(propertyID);
               
           }
          //TODO TYPE NOT MATCH EXCEPTION  
          for(String k : filteredID) {
              System.out.println(k);
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
    
    public void getMainList() {
        properties = new HashMap<String, Property>();
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
               //System.out.print(propertyID + ":");
               streetNo = rs.getInt("streetNo");
               //System.out.print(streetNo + ":");
               streetName = rs.getString("streetName");
               //System.out.print(streetName + ":");
               suburb = rs.getString("suburb");
               //System.out.print(suburb + ":");
               bedNum = rs.getInt("bedNum");
               //System.out.print(bedNum + ":");
               dateL = rs.getString("lastMaintainDate");
               //System.out.print(dateL + ":");
               dateS = rs.getString("startMaintenance");
               //System.out.print(dateS + ":");
               image = rs.getString("image");  
               //System.out.print(image + ":");
               description = rs.getString("description"); 
               //System.out.print(description + ":");
               status = rs.getInt("status");
               //System.out.print(status + "\n");
               
               
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
               properties.put(propertyID,property); 
           
           }
          //TODO TYPE NOT MATCH EXCEPTION  
           
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
