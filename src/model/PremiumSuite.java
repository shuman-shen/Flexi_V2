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
            return true;
        }
        else if (numOfDay <=0 | numOfDay == 9) { 
            System.out.println("Property cannot be rented on this day.");
            return false;}
        else return true;
        
    }
    
    public void rent(String customerId, LocalDate rentDate, int numOfRentDay) {
        
        String recordID = "";
        LocalDate estimatedReturnDate = rentDate.plusDays(numOfRentDay);        
        
        int diff1 = rentDate.compareTo(super.getLastMaintainDate());
        
        
        try {
            // Shift last recorded maintain date to current schedule;
            if(checkDate(super.getLastMaintainDate(),diff1)) {
                
                LocalDate nextMaintainDate = super.getLastMaintainDate().plusDays(diff1);
                int diff2 = nextMaintainDate.compareTo(estimatedReturnDate);
                
                if(diff2 > 0) {
                    
                    String shortFormattedDate = estimatedReturnDate.format(super.getShortDateFormat());
                    String formattedEstDate = estimatedReturnDate.format(super.getDateFormat());
                   
                    
                    recordID = super.getPropertyID() + "_"+ customerId + "_" + shortFormattedDate;
           
                
                Connection conn = null;
                String url = "jdbc:sqlite:src/database/FlexiData.db";
                // create a connection to the database
                conn = DriverManager.getConnection(url);
                
                //System.out.println("Connection to SQLite has been established.");
                
                String sql1 = "INSERT INTO RentalRecords (recordID, propertyID, customerID, estimatedReturnDate) "
                        + "VALUES(?,?,?,?)";
                Statement stmt1  = conn.createStatement();                     
                PreparedStatement pstmt1 = conn.prepareStatement(sql1);
                    pstmt1.setString(1, recordID);
                    pstmt1.setString(2, super.getPropertyID());
                    pstmt1.setString(3, customerId);
                    pstmt1.setString(4, formattedEstDate);                   
                    pstmt1.executeUpdate();
                    
                    
                    
                String sql2 = "UPDATE Property SET status = ? "
                            + "WHERE propertyID = ?";
                Statement stmt2  = conn.createStatement();                     
                PreparedStatement pstmt2 = conn.prepareStatement(sql2);
                    pstmt2.setInt(1, 1);
                    pstmt2.setString(2, super.getPropertyID());                
                    pstmt2.executeUpdate();
                    conn.close();
                  
                RentalRecord r = new RentalRecord(recordID, customerId,rentDate, estimatedReturnDate);
                super.addRecord(r);
                
                System.out.println("Premium Suite " + super.getPropertyID() + " is now rented by customer "+ customerId);
                //this method will return TRUE if the property can be rented successfully.
                super.setToRent();
               
                }
                else {
                    throw new Exception();
                    }
            }   
         
            }     
      

        catch(Exception e1) {
            System.out.println("Premium Suite " + super.getPropertyID() + " could not be rented");
            
        }
       
        
        
    }
           
            
    public void returnProperty(LocalDate returnDate) {
        
        int actualDiff = 2;
        int lateDiff = 0;
        int estimateDiff = 2;
        
        RentalRecord rec = super.records.get(0);
        actualDiff = returnDate.compareTo(rec.getRentDate());
        estimateDiff = rec.getEstimatedReturnDate().compareTo(rec.getRentDate());
        lateDiff = returnDate.compareTo(rec.getEstimatedReturnDate());
        
        
        try{
             
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
                    //System.out.println("Rental Fee:            " + super.df2.format(rec.getRentalFee()));
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
        catch(Exception e2) {
            System.out.println("Premium Suite " + super.getPropertyID() + " could not be returned.");
            
        }
        
        
       
    }

 
    
    public void performMaintenance() {
        // ASSUME: maintenance date = current day.
        
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
    
    public String toString() {
        
        //propertyId:streetNumber:streetName:suburb:propertyType:numOfBedRoom:status:lastMaintainDate
        
//        String str =
//        super.getPropertyID() + ":" + super.getStreetNo() + ":" 
//        + super.getStreetName() + ":" + super.getSuburb() + ":" 
//        + getType() + ":" + super.getBedNum() + ":"
//        + super.convertStatus(super.getStatus()) + ":" + super.getLastMaintainDate().getFormattedDate();
//        
//        return str;
        return null;
    }
    
    public String getDetails() {
//        String s2 = "";
//        final String s3 = "RENTAL RECORD";
//        String s4 = "";
//     
//        
//        String s1 = "Property ID:    " + super.getPropertyID() + "\n" +
//                    "Address:        " + super.getStreetNo() + " " + super.getStreetName() + " " + super.getSuburb() + "\n" +
//                    "Type:           " + "Premium Suite" + "\n" +
//                    "Bedroom:        "  + super.getBedNum() + "\n" +
//                    "Status:      " + super.convertStatus(super.getStatus()) + "\n" +
//                    "Last maintenance: " + super.getLastMaintainDate().getFormattedDate() + "\n";
//        
//        if (super.records[0] == null) {
//            s2 = "RENTAL RECORD:  empty" + "\n"+
//                 "----------------------------------";
//            return s1 + s2;
//        }
//        else if(super.records[0].getActualReturnDate() == null) {
//            s4 = "Record ID:             " + super.records[0].getRecordID() + "\n" +
//                 "Rent Date:             " + super.records[0].getRentDate().getFormattedDate() + "\n" +
//                 "Estimated Return Date: " + super.records[0].getEstimatedReturnDate().getFormattedDate() + "\n" +
//                 "----------------------------------";
//            for (int i= 1; i<10; i++ ) {
//                if(super.records[i]== null) break;
//                s4 = s4 +  "\n" +
//                     "Record ID:             " + super.records[i].getRecordID() + "\n" +
//                     "Rent Date:             " + super.records[i].getRentDate().getFormattedDate() + "\n" +
//                     "Estimated Return Date: " + super.records[i].getEstimatedReturnDate() + "\n" +
//                     "Actual Return Date:    " + super.records[i].getActualReturnDate() + "\n" +
//                     "Rental Fee:            " + super.records[i].getRentalFee() + "\n" +
//                     "Late Fee:              " + super.records[i].getPremiumLateFee() + 
//                     "----------------------------------";
//                     
//           }
//            return s1 + s3 + s4;
//        }
//        else {
//            for (int i= 0; i<10; i++ ) {
//                if(super.records[i]== null) break;
//                s4 = s4 +  "\n" +
//                     "Record ID:             " + super.records[i].getRecordID() + "\n" +
//                     "Rent Date:             " + super.records[i].getRentDate().getFormattedDate() + "\n" +
//                     "Estimated Return Date: " + super.records[i].getEstimatedReturnDate() + "\n" +
//                     "Actual Return Date:    " + super.records[i].getActualReturnDate() + "\n" +
//                     "Rental Fee:            " + super.records[i].getRentalFee() + "\n" +
//                     "Late Fee:              " + super.records[i].getPremiumLateFee() +
//                     "----------------------------------";;
//            }
//            return s1 + s3 + s4;
//        }
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
