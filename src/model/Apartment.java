package model;
import utilities.DateTime;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Apartment extends Property {

    
    
    public Apartment(String propertyID, int streetNo, String streetName, 
            String suburb, int bedNum, LocalDate lastMaintainDate) {
        super(propertyID, streetNo, streetName, suburb, bedNum, lastMaintainDate);
        
    }
    
    public Apartment(String propertyID, int streetNo, String streetName, 
            String suburb, int bedNum, LocalDate lastMaintainDate, 
            int status, String image, String description) {
        super(propertyID, streetNo, streetName, suburb, bedNum, lastMaintainDate, 
                status, image, description);
        
       
    }
    
    
        /*Each Apartment can be rented for:
        - a minimum of 2 days if the rental day is between Sunday and Thursday inclusively
        - a minimum of 3 days if the rental day is Friday or Saturday
        - a maximum of 28 days*/
        
      
    
    
    public void rent(String customerId, LocalDate rentDate, int numOfRentDay) {
        
     
        String recordID;
        LocalDate estimatedReturnDate = rentDate.plusDays(numOfRentDay);
             
            String shortFormattedDate = estimatedReturnDate.format(super.getShortDateFormat());
            String formattedEstDate = estimatedReturnDate.format(super.getDateFormat());
            String formattedRentDate = rentDate.format(super.getDateFormat());
            String pID = super.getPropertyID();
            
            recordID = super.getPropertyID() + "_"+ customerId + "_" + shortFormattedDate;
            
            Connection conn = null;
            try {
                // db parameters
                String url = "jdbc:sqlite:src/database/FlexiData.db";
                // create a connection to the database
                conn = DriverManager.getConnection(url);
                
                String sql1 = "INSERT INTO RentalRecord (recordID, propertyID, customerID, rentDate, estimatedReturnDate) "
                        + "VALUES(?,?,?,?,?)";
                Statement stmt1  = conn.createStatement();                     
                PreparedStatement pstmt1 = conn.prepareStatement(sql1);
                    pstmt1.setString(1, recordID);
                    pstmt1.setString(2, pID);
                    pstmt1.setString(3, customerId);
                    pstmt1.setString(4, formattedRentDate);
                    pstmt1.setString(5, formattedEstDate);                   
                    pstmt1.executeUpdate();
                    
                System.out.println("first done.");    
                    
                String sql2 = "UPDATE Property SET status = ? "
                            + "WHERE propertyID = ?";
                Statement stmt2  = conn.createStatement();                     
                PreparedStatement pstmt2 = conn.prepareStatement(sql2);
                    pstmt2.setInt(1, 1);
                    pstmt2.setString(2, super.getPropertyID());                
                    pstmt2.executeUpdate();
                    
                    System.out.println("second done."); 
                    conn.close();
                    
            }
                catch (SQLException e) {
                    System.out.println(e.getMessage() + "TAG E");
                }
            
            
            
            
            
            
            RentalRecord r = new RentalRecord(recordID, customerId, rentDate, estimatedReturnDate);
            super.addRecord(r);
            
            System.out.println("Apartment " + super.getPropertyID() + " is now rented by customer "+ customerId);
            
            super.setToRent();
           
            
         
        
        
    }
        
        

    
    public double getRate(int bedNum) {
        double rate;
        
        if (bedNum == 1) rate =143;
        else if (bedNum == 2) rate =210;
        else rate = 319;
        
        return rate;
    }
    
            
    public void returnProperty(LocalDate returnDate) throws Exception {
        
        
        int actualDiff = 1;
        int lateDiff = 0;
        int estimateDiff = 2;
        
        
        //ascending order
        System.out.println("SSSSS2222: " + super.getRecords().size()); 
        RentalRecord rec = super.getRecords().get(0);
        
         
        actualDiff = returnDate.compareTo(rec.getRentDate());   
        System.out.println("1:" + actualDiff);
        estimateDiff = rec.getEstimatedReturnDate().compareTo(rec.getRentDate());
        System.out.println("2:" + estimateDiff);
        lateDiff = returnDate.compareTo(rec.getEstimatedReturnDate());
        System.out.println("23:" + lateDiff);
       
      
         if (actualDiff>0) {
                System.out.println("SSSSS22221: "+ "here");
                super.setRate();
                System.out.println("SSSSS22221: "+ "there");
                
                if (lateDiff > 0) {
                    System.out.println("SSSSS1: " + super.getRentalRate());    
                    rec.setRentalFee(estimateDiff, super.getRentalRate());
                    rec.setLateFee(lateDiff, super.getRentalRate());
         
                }
                else {
          
                    rec.setRentalFee(actualDiff, super.getRentalRate());                   
                }
                
                rec.setActualReturnDate(returnDate);
                System.out.println("SSSSS2: " + rec.getActualReturnDate());                          
                
             try {   
                Connection conn = null;
              
                    // db parameters
                    String url = "jdbc:sqlite:src/database/FlexiData.db";
                    // create a connection to the database
                    conn = DriverManager.getConnection(url);
                    
              
                    
                    String sql1 = "UPDATE RentalRecord SET actualReturnDate = ? , "
                                + "rentalFee = ? , "
                                + "lateFee = ? "
                                + "WHERE recordId = ?";
                    Statement stmt1  = conn.createStatement();                     
                    PreparedStatement pstmt1 = conn.prepareStatement(sql1);
                        pstmt1.setString(1, returnDate.format(getDateFormat()));
                        pstmt1.setDouble(2, rec.getRentalFee());
                        pstmt1.setDouble(3, rec.getLateFee());
                        pstmt1.setString(4, rec.getRecordID());    
                        pstmt1.executeUpdate();
                        
                       String id = super.getPropertyID(); 
                       String sql2 = "UPDATE Property SET status = ? "
                                + "WHERE propertyID = ?";
                       Statement stmt2  = conn.createStatement();                     
                       PreparedStatement pstmt2 = conn.prepareStatement(sql2);
                        pstmt2.setInt(1, 2);
                        pstmt2.setString(2, id);                
                        pstmt2.executeUpdate();    
                        
                        conn.close(); 
                        //super.setToReturn();
            }       
            catch(Exception e) {    
               throw e;
            } 
            }
         else { 
             throw new Exception();
          }
        }
           
   

 
    
    public void performMaintenance() throws SQLException {
        // ASSUME1: apartment maintenance is ONLY called on the current day.
        // ASSUME2: apartment rented even in future days will NOT allow maintenance.
        LocalDate today = LocalDate.now();            
        super.setStartMaintain(today);
        super.resetLastMaintainDate(today);
            
            Connection conn = null;
            try {
            // db parameters
            String url = "jdbc:sqlite:src/database/FlexiData.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            String sql1 = "UPDATE Property SET lastMaintainDate = ? , "
                        + "startMaintenance = ? ,"
                        + "status = ? "
                      
                        + "WHERE propertyID = ?";
           
            Statement stmt1  = conn.createStatement();                     
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
                pstmt1.setString(1, super.getLastMaintainDate().format(getDateFormat()));
                pstmt1.setString(2, super.getStartMaintainDate().format(getDateFormat()));
                pstmt1.setInt(3, 3);
                pstmt1.setString(4, super.getPropertyID());
              
                pstmt1.executeUpdate();
             
                
                conn.close();
                
            }
            catch(SQLException sql) {
               throw sql;
            }
        
        
        //this method will return true if the property is now under maintenance.
        
        
    }

   
    public void completeMaintenance(LocalDate completionDate) {
        int diff = 1;
     
        diff = completionDate.compareTo(super.getStartMaintainDate());
        
        //Maintenance can be set to completed on the same day of last maintenance date.
        try {
        if (diff >= 0) {
            
            super.setLastMaintainDate(completionDate);
            
            
            Connection conn = null;
            
         // db parameters
            String url = "jdbc:sqlite:src/database/FlexiData.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            String sql1 = "UPDATE Property SET lastMaintainDate = ? , "
                    + "status = ? "                  
                    + "WHERE propertyID = ?";
            
            
            Statement stmt1  = conn.createStatement();                     
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
                pstmt1.setString(1, super.getLastMaintainDate().format(getDateFormat()));
                pstmt1.setInt(2, 2);
                pstmt1.setString(3, super.getPropertyID());             
                pstmt1.executeUpdate();            

                conn.close();
                System.out.println(super.getPropertyID() + " has all maintenance completed and ready for rent." );
            
        }
        else {
            
            throw new Exception();
        }
        }
        catch(Exception e4) {
            System.out.println(e4.getMessage() + "TAG E4");
        }
        
        
    }
    
    public String getType() {
        String type = "Apartment";
        return type;
    }
    
    public String toString() {
        
        //propertyId:streetNumber:streetName:suburb:propertyType:numOfBedRoom:status
        
        String str =
        super.getPropertyID() + ":" + super.getStreetNo() + ":" 
        + super.getStreetName() + ":" + super.getSuburb() + ":" 
        + getType() + ":" + super.getBedNum() + ":" + super.convertStatus(super.getStatus()) + ":"
        + super.getImage() + ":" + super.getDescription() + "\n";
        
        return str;
    }
    
    public String getDetails() {
        return null;
       
        
      

       
   
    }
    
    
    
}
