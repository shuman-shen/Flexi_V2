package model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;




public class PremiumSuite extends Property{
    
    public PremiumSuite(String propertyID, int streetNo, String streetName, 
            String suburb, int bedNum, LocalDate lastMaintainDate) {
            super(propertyID, streetNo, streetName, suburb, bedNum, lastMaintainDate);
      
    }
    
    public PremiumSuite(String propertyID, int streetNo, String streetName, 
            String suburb, int bedNum, LocalDate lastMaintainDate, 
            int status, String image, String description) {
        super(propertyID, streetNo, streetName, suburb, bedNum, lastMaintainDate, 
                status, image, description);
        
       
    }
    
    /* If a Premium Suite is in a status of Available,
     * system automatically shift last maintenance date for 10-days rounds 
      until the nearest maintenance date before the desired rentDate when possible. */

    private boolean checkDate(LocalDate date, int numOfDay) {
        if (numOfDay > 10) {
            int j = numOfDay / 10;
            LocalDate newDate = date.plusDays(j*10);            
            super.setLastMaintainDate(newDate);
            
            //System.out.println("New Date: " + newDate.toString());
            return true;
        }
        else if (numOfDay <=0 | numOfDay == 9) { 
            //System.out.println("Cannot be rented on this day.");
            return false;}
        else return true;
        
    }
    
    public void rent(String customerId, LocalDate rentDate, int numOfRentDay) throws Exception {
        
        String recordID = "";
        LocalDate estimatedReturnDate = rentDate.plusDays(numOfRentDay);        
        
       
        
        int diff1 = rentDate.compareTo(super.getLastMaintainDate());
        
       
            // Shift last recorded maintain date to current schedule;
            if(checkDate(super.getLastMaintainDate(),diff1)) {
                
                LocalDate nextMaintainDate = super.getLastMaintainDate().plusDays(10);
                int diff2 = nextMaintainDate.compareTo(estimatedReturnDate);
             
                
                if(diff2 > 0) {
                    
                    try {
                    String shortFormattedDate = estimatedReturnDate.format(super.getShortDateFormat());
                    String formattedEstDate = estimatedReturnDate.format(super.getDateFormat());
                   
                    
                    recordID = super.getPropertyID() + "_"+ customerId + "_" + shortFormattedDate;
                  
                
                    Connection conn = null;
        
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
                            pstmt1.setString(2, super.getPropertyID());
                            pstmt1.setString(3, customerId);
                            pstmt1.setString(4, rentDate.format(getDateFormat()));
                            pstmt1.setString(5, formattedEstDate);                   
                            pstmt1.executeUpdate();
                            
                    
                            
                        String sql2 = "UPDATE Property SET status = ? "
                                    + "WHERE propertyID = ?";
                        Statement stmt2  = conn.createStatement();                     
                        PreparedStatement pstmt2 = conn.prepareStatement(sql2);
                            pstmt2.setInt(1, 1);
                            pstmt2.setString(2, super.getPropertyID());                
                            pstmt2.executeUpdate();
                            
                       
                            conn.close();                   
                    
                    RentalRecord r = new RentalRecord(recordID, customerId, rentDate, estimatedReturnDate);
                    super.addRecord(r);                                      
                    
                    super.setToRent();
       
                    }
                    catch(Exception a1) {
                        throw a1;
                    }                
                }
            
            }
            else System.out.println("Date out of range.");
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
                    rec.setPremiumLateFee(lateDiff);
                    //System.out.println("Rental Fee:            " + super.df2.format(rec.getRentalFee())
                     //         + "\n" + "Late Fee:              " + super.df2.format(rec.getLateFee()));
                }
                else {
                    //rec.setLateFee(0, super.getRentalRate());
                    rec.setRentalFee(actualDiff, getRate(3));
                    
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
                        
                        System.out.print("rate " + getRate(3));
                        
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
            
            Connection conn = null;
            try {
            // db parameters
            String url = "jdbc:sqlite:src/database/FlexiData.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            //System.out.println("Connection to SQLite has been established.");
            
            String sql1 = "UPDATE Property SET lastMaintainDate = ? , "
                        + "startMaintenance = ? ,"
                        + "status = ? "
                      
                        + "WHERE propertyID = ?";
            
            System.out.println(sql1);
            Statement stmt1  = conn.createStatement();                     
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
                pstmt1.setString(1, super.getLastMaintainDate().format(getDateFormat()));
                pstmt1.setString(2, super.getStartMaintainDate().format(getDateFormat()));
                pstmt1.setInt(3, 3);
                pstmt1.setString(4, super.getPropertyID());
              
                pstmt1.executeUpdate();
             
                
                conn.close();
              
                System.out.println("done here");
                
                
                System.out.println("status " + super.getStatus());
                
            }
            catch(SQLException sql) {
                //System.out.println(sql.getMessage());
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
            
            //System.out.println("Connection to SQLite has been established.");
            
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
            System.out.println(e4.getMessage());
        }
        
        
    }
    
    public String toString() {
        
        //propertyId:streetNumber:streetName:suburb:propertyType:numOfBedRoom:status:lastMaintainDate
        
            String str =
            super.getPropertyID() + ":" + super.getStreetNo() + ":" 
            + super.getStreetName() + ":" + super.getSuburb() + ":" 
            + getType() + ":" + super.getBedNum() + ":"
            + super.convertStatus(super.getStatus()) + ":" 
            + super.getLastMaintainDate().format(getDateFormat()) + ":" 
            + super.getImage() + ":"
            + super.getDescription() +  "\n";
            
            return str;
       
    }
    
    public String getDetails() {
        
        return null;
    }


  
    public double getRate(int bedNum) {
        
        final double rate = 554;
        return rate;
    }


    public String getType() {
        String type = "Premium Suite";
        return type;
    }
    

    
    
}
