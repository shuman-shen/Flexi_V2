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
            String formattedRentDate = estimatedReturnDate.format(super.getDateFormat());
            String pID = super.getPropertyID();
            
            recordID = super.getPropertyID() + "_"+ customerId + "_" + shortFormattedDate;
            
            Connection conn = null;
            try {
                // db parameters
                String url = "jdbc:sqlite:src/database/FlexiData.db";
                // create a connection to the database
                conn = DriverManager.getConnection(url);
                
                //System.out.println("Connection to SQLite has been established.");
                
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
                    System.out.println(e.getMessage());
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
    
            
    public void returnProperty(LocalDate returnDate) {
        
        
        int actualDiff = 1;
        int lateDiff = 0;
        int estimateDiff = 2;
        
        
        //ascending order
        RentalRecord rec = super.getRecords().get(0);
        
        actualDiff = returnDate.compareTo(rec.getRentDate());
        estimateDiff = rec.getEstimatedReturnDate().compareTo(rec.getRentDate());
        lateDiff = returnDate.compareTo(rec.getEstimatedReturnDate());
       
            try { 
            if (actualDiff>0) {
                
                super.setRate();
                
                if (lateDiff > 0) {
                        
                    rec.setRentalFee(estimateDiff, super.getRentalRate());
                    rec.setLateFee(lateDiff, super.getRentalRate());
                    //System.out.println("Rental Fee:            " + super.df2.format(rec.getRentalFee())
                     //         + "\n" + "Late Fee:              " + super.df2.format(rec.getLateFee()));
                }
                else {
                    //rec.setLateFee(0, super.getRentalRate());
                    rec.setRentalFee(actualDiff, super.getRentalRate());
                    
                }
                
                //this method will return TRUE if the property now can be rented successfully.
                rec.setActualReturnDate(returnDate);
                
                Connection conn = null;
              
                    // db parameters
                    String url = "jdbc:sqlite:src/database/FlexiData.db";
                    // create a connection to the database
                    conn = DriverManager.getConnection(url);
                    
                    //System.out.println("Connection to SQLite has been established.");
                    
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
                        
                        
                    String sql2 = "UPDATE Property SET status = ? "
                                + "WHERE propertyID = ?";
                    Statement stmt2  = conn.createStatement();                     
                    PreparedStatement pstmt2 = conn.prepareStatement(sql2);
                        pstmt2.setInt(1, 2);
                        pstmt2.setString(2, super.getPropertyID());                
                        pstmt2.executeUpdate();    
                        
                        conn.close(); 
                super.setToReturn();
                
               
            }
            else {
                
               
            
                throw new Exception();
            }
            }
            catch(Exception e) {    
                System.out.println("Apartment " + super.getPropertyID() + " could not be returned.");
            }
  
         
        }
           
   

 
    
    public void performMaintenance() {
        // ASSUME1: apartment maintenance is ONLY called on the current day.
        // ASSUME2: apartment rented even in future days will NOT allow maintenance.
        LocalDate today = LocalDate.now();
        
            super.setStartMaintain(today);
            super.resetLastMaintainDate(today);
            
            System.out.println("Property " + super.getPropertyID() + " is under maintainance from today.");
            Connection conn = null;
            try {
            // db parameters
            String url = "jdbc:sqlite:src/database/FlexiData.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            //System.out.println("Connection to SQLite has been established.");
            
            String sql1 = "UPDATE Property SET lastMaitaindDate = ? , "
                        + "startMaintenance = ? "
                      
                        + "WHERE propertyID = ?";
            Statement stmt1  = conn.createStatement();                     
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
                pstmt1.setString(1, super.getLastMaintainDate().format(getDateFormat()));
                pstmt1.setString(2, super.getStartMaintainDate().format(getDateFormat()));
                pstmt1.setString(3, super.getPropertyID());
              
                pstmt1.executeUpdate();
             
                
                conn.close();
                
                
            }
            catch(SQLException sql) {
                System.out.println("Maintenance EROR");
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
            System.out.println(super.getPropertyID() + " has all maintenance completed and ready for rent." );
            
            Connection conn = null;
            
         // db parameters
            String url = "jdbc:sqlite:src/database/FlexiData.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            //System.out.println("Connection to SQLite has been established.");
            
            String sql1 = "UPDATE Property SET lastMaitaindDate = ? "                     
                        + "WHERE propertyID = ?";
            Statement stmt1  = conn.createStatement();                     
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
                pstmt1.setString(1, super.getLastMaintainDate().format(getDateFormat()));
                pstmt1.setString(2, super.getPropertyID());             
                pstmt1.executeUpdate();            

                conn.close();
        
            
        }
        else {
            
            throw new Exception();
        }
        }
        catch(Exception e4) {
            System.out.println("Invalid. Cannot complete maintenance for the property." );
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
        + getType() + ":" + super.getBedNum() + ":" + super.convertStatus(super.getStatus());
        
        return str;
    }
    
    public String getDetails() {
       
        String s2 = "";
        final String s3 = "RENTAL RECORD" + "\n";
        String s4 = "";
     
        
        String s1 = "Property ID:    " + super.getPropertyID() + "\n" +
                    "Address:        " + super.getStreetNo() + " " + super.getStreetName() + " " + super.getSuburb() + "\n" +
                    "Type:           " + "Apartment" + "\n" +
                    "Bedroom:        "  + super.getBedNum() + "\n" +
                    "Status:      " + super.convertStatus(super.getStatus()) + "\n";
        
        if (super.getRecords().get(0) == null) {
            s2 = "RENTAL RECORD:  empty" + "\n" +
                 "----------------------------------";
            return s1 + s2;
        }
        else if(super.getRecords().get(0).getActualReturnDate() == null) {
         
            s4 = "Record ID:             " + super.getRecords().get(0).getRecordID() + "\n" +
                 "Rent Date:             " + super.getRecords().get(0).getRentDate().format(getDateFormat()) + "\n" +
                 "Estimated Return Date: " + super.getRecords().get(0).getEstimatedReturnDate().format(getDateFormat()) + "\n" +
                    "----------------------------------";
            for (int i= 1; i<super.getRecords().size(); i++ ) {
                if(super.getRecords().get(i)== null) break;
                s4 = s4 +  "\n" +
                     "Record ID:             " + super.getRecords().get(i).getRecordID() + "\n" +
                     "Rent Date:             " + super.getRecords().get(i).getRentDate().format(getDateFormat()) + "\n" +
                     "Estimated Return Date: " + super.getRecords().get(i).getEstimatedReturnDate() + "\n" +
                     "Actual Return Date:    " + super.getRecords().get(i).getActualReturnDate() + "\n" +
                     "Rental Fee:            " + super.getRecords().get(i).getRentalFee() + "\n" +
                     "Late Fee:              " + super.getRecords().get(i).getLateFee() + "\n" +
                     "----------------------------------";
                     
           }
            return s1 + s3 + s4;
       }
        
       else {
            for (int i= 0; i<super.getRecords().size(); i++ ) {
                if(super.getRecords().get(i)== null) break;
                s4 = s4 +  "\n" +
                     "Record ID:             " + super.getRecords().get(i).getRecordID() + "\n" +
                     "Rent Date:             " + super.getRecords().get(i).getRentDate().format(getDateFormat()) + "\n" +
                     "Estimated Return Date: " + super.getRecords().get(i).getEstimatedReturnDate() + "\n" +
                     "Actual Return Date:    " + super.getRecords().get(i).getActualReturnDate() + "\n" +
                     "Rental Fee:            " + super.getRecords().get(i).getRentalFee() + "\n" +
                     "Late Fee:              " + super.getRecords().get(i).getLateFee() +
                     "----------------------------------";;
            }
            return s1 + s3 + s4;
       }

       
   
    }
    
    
    
}
