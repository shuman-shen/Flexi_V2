package model;
import java.util.*;
import utilities.DateTime;
//import java.time.*;
//import java.time.format.DateTimeFormatter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FlexiRentSystem {

    
    //private ArrayList<Property> properties = new ArrayList<Property>();
    private ArrayList<Property> properties;
    private ArrayList<String> filteredID;    
    private ArrayList<Property> filteredList;
    private Property property;
    //private Collection<Property> c;
    
    
    private String propertyType;
    private String propertyID;
    private int streetNo;
    private String streetName ;
    private String suburb;
    private int bedNum = 3;
    private LocalDate lastMaintainDate;
    private LocalDate startMaintenance;
    private int status = 2;
    private String image;
    private String description;
    private final String checkDate = "(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/((19|20)\\d\\d)";
    private ArrayList<String> suburbList;
    
    
    public FlexiRentSystem() {
        getMainList();
        getAllSuburbs();
        
        
        //System.out.println("Tryyyyyyyyyyyyyyyyyyyyy " +properties.get(7).getPropertyID());
        //filteredList = new ArrayList<Property>(properties);
    }
    
    
    public ArrayList<Property> getPropertyList() {
        return properties;
    }
    
    public ArrayList<String> getFilteredID(){
        return filteredID;
    }
    
  
//    public Collection getCollection() {
//        return c;
//    }
    
    public ArrayList<Property> getFilteredList() {
        
        return filteredList;
    }
    
    public ArrayList<String> getSuburbList(){
        return suburbList;
    }
    
    public String addApartment(int streetNo, String streetName, 
            String suburb, int bedNum, String image, String description) {
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String today = LocalDate.now().format(formatter);
        
        generateID();
        propertyID = "A_" + propertyID;
        Property p = new Apartment(propertyID, streetNo, streetName, 
                suburb,bedNum,LocalDate.now(), 
                status, image,description);
        insertNew(propertyID, streetNo, streetName, 
                suburb,bedNum,today, 
                2, image , description);
        properties.add(p);
        return propertyID;
        }
           
   public String addPremiumSuite(int streetNo, String streetName, 
           String suburb, LocalDate lastMaintainDate, String image, String description) {
       
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
       String formattedDate = lastMaintainDate.format(formatter);
       
       
       generateID();
       propertyID = "S_" + propertyID;
       Property p = new Apartment(propertyID, streetNo, streetName, 
               suburb,3,lastMaintainDate, 
               2, image,description);
       //put try catch inside this method
       
       insertNew(propertyID, streetNo, streetName, 
               suburb,bedNum,formattedDate, 
               2 , image , description);
       properties.add(p);
       return propertyID;
       
       
       
   }
        
        
        
    
    public void generateID() {
        
        
        Random r = new Random();
        int n;
        char c;
        propertyID = "";
                
        for (int i=0; i<4; i++) {
            n = r.nextInt(10);
            propertyID = propertyID + Integer.toString(n);                             
        }
            
        for (int j=0; j<4; j++) {
            c = (char)(r.nextInt(26) + 'a');
            propertyID = propertyID + Character.toString(c);
        }
            
        propertyID = propertyID.toUpperCase();
        
       
    }
    
    
    
    public void insertNew(String ID, int streetNo, String streetName, 
            String suburb, int bedNum, String lastMaintainDate, 
            int status, String image, String description) {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:src/database/FlexiData.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            //System.out.println("Connection to SQLite has been established.");
            
            String sql = "INSERT INTO Property (propertyID, streetNo, streetName, "
                    + "suburb, bedNum, lastMaintainDate, "
                    + "status, image,description) VALUES(?,?,?,?,?,?,?,?,?)";
            Statement stmt  = conn.createStatement();                     
            PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, ID);
                pstmt.setInt(2, streetNo);
                pstmt.setString(3, streetName);
                pstmt.setString(4, suburb);
                pstmt.setInt(5, bedNum);
                pstmt.setString(6, lastMaintainDate);
                pstmt.setInt(7, status);
                pstmt.setString(8, image);
                pstmt.setString(9, description);
               
                pstmt.executeUpdate();
                conn.close();
                
        }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }
       
        
    }
    
    
    //Generate suburb list for displayed at MainWindow filter
    public void getAllSuburbs() {
        suburbList = new ArrayList<String>();
        String s;
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:src/database/FlexiData.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            //System.out.println("Connection to SQLite has been established.");
            
            String sql = "SELECT suburb FROM Property";
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
           
           // loop through the result set
           while (rs.next()) {             
               s = rs.getString("suburb");
               if(!suburbList.contains(s))
               suburbList.add(s);
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
    
    //Search main property list stored with a list of property IDs
    public void searchListWithID() {
        //boolean found = false;
        //Iterator<Property>  iter = filteredList.iterator();
       
        filteredList = new ArrayList<Property>();
        for ( Property curItem : properties )
        {
            for(String t : filteredID) {
                if (curItem.getPropertyID().equals(t)) {
                    //System.out.println(curItem.getSuburb());
                    filteredList.add(curItem);
                    //found = true;
                    break;
                }
            }
        }
    }
    
    // Filter out the property matches specific propertyID
    public boolean setFilterList(String propertyID) {
        System.out.println("\n ID input " + propertyID);
        Connection conn = null;
        String pID = "";
        filteredID = new ArrayList<String>();
        try {
            // db parameters
            String url = "jdbc:sqlite:src/database/FlexiData.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            //System.out.println("Connection to SQLite has been established.");
            
            String sql = "SELECT propertyID FROM Property WHERE propertyID = \'" + propertyID + "\'";
            System.out.println(sql);
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
           
           // loop through the result set
           while (rs.next()) {             
               pID = rs.getString("propertyID");
               System.out.println("\n ID FROM DATABASE: " + pID);
               filteredID.add(pID);
           }
          //TODO TYPE NOT MATCH EXCEPTION  
          if(pID != null) {          
          searchListWithID();
          // TODO EXCEPTION REQUIRED TO SUBSTITUDE BOOLEAN RETURN VALUE
          conn.close();
          
          return true;}
          
          else {
              
              conn.close();
              return false;}
          
          
           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    // Filter out property list according to MainWindow filter conditions
    public void setFilterList(String propertyType, int bedNum, int status, String suburb) {
        
        Connection conn = null;
        String pType = "";
        String bNum = "";
        String cType = "";
        String s = "";
        ArrayList<String> type = new ArrayList<String>();
        String all = "";
        filteredID = new ArrayList<String>();
        
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
          
          searchListWithID();
           
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
    
    // Connect and generate whole list of properties from database
    public void getMainList() {
        properties = new ArrayList<Property>();
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
               System.out.print(propertyID + ":");
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
               lastMaintainDate = convertDate(dateL);                             
               
               if(propertyID.startsWith("A")) {
                   property = new Apartment(propertyID, streetNo, streetName, 
                           suburb, bedNum, lastMaintainDate, 
                           status, image, description);
                   if(dateS != null) {
                       startMaintenance = convertDate(dateS);
                   property.setStartMaintain(startMaintenance);
                   }
               }
               else {
                   property = new PremiumSuite(propertyID, streetNo, streetName, 
                           suburb, bedNum, lastMaintainDate, 
                           status, image, description);
                   if(dateS != null) {
                       startMaintenance = convertDate(dateS);
                   property.setStartMaintain(startMaintenance);
                   }
               }
               properties.add(property); 
           
           }
           conn.close();
           
           
           for(Property pp : properties) {
               pp.getAllRecords();
           }
           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
            
        
    }
        
    private LocalDate convertDate(String in) {
        
        if(in.matches(checkDate)) {            
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
          LocalDate date = LocalDate.parse(in, formatter);
            return date;
            
            
        }
        else {
            //TODO DATEFORMATE EXCEPTION
            System.out.println("Date data corrupted.");
            return null;
        }
    }
       
        
 

    
        
        
}
