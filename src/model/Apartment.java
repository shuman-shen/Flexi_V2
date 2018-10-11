package model;
import utilities.DateTime;
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
       
           
        
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
            String formattedDate = estimatedReturnDate.format(formatter);
           
            
            recordID = super.getPropertyID() + "_"+ customerId + "_" + formattedDate;
            
            RentalRecord r = new RentalRecord(recordID, customerId, rentDate, estimatedReturnDate);
            super.addRecord(r);
            
            System.out.println("Apartment " + super.getPropertyID() + " is now rented by customer "+ customerId);
            
            super.setToRent(true);
           
            
         
        
        
    }
        
        

    
    public double getRate(int bedNum) {
        double rate;
        
        if (bedNum == 1) rate =143;
        else if (bedNum == 2) rate =210;
        else rate = 319;
        
        return rate;
    }
    
            
    public boolean returnProperty(DateTime returnDate) {
        
        
        int actualDiff = 2;
        int lateDiff = 0;
        int estimateDiff = 2;
        
        RentalRecord rec = super.records[0];
        actualDiff = DateTime.diffDays(returnDate, rec.getRentDate());
        estimateDiff = DateTime.diffDays(rec.getEstimatedReturnDate(), rec.getRentDate());
        lateDiff = DateTime.diffDays(returnDate, rec.getEstimatedReturnDate());
        
        //status: rented
        if(super.getStatus() == 1) {
             
            if (actualDiff>0) {
                
                System.out.println("Apartment " + super.getPropertyID() + " is returned by customer "+ rec.getCustomerID());
                System.out.println("Property ID: " + super.getPropertyID()
                          + "\n" + "Address:     " + super.getStreetNo() + " " + super.getStreetName() + " " + super.getSuburb()
                          + "\n" + "Type:        Apartment"
                          + "\n" + "Bedroom:     " + super.getBedNum()
                          + "\n" + "Status:      Available");
                
                System.out.println("RENTAL RECORD");
                System.out.println("Record ID:             " + rec.getRecordID()
                          + "\n" + "Rent Date:             " + rec.getRentDate().getFormattedDate()
                          + "\n" + "Estimated Return Date: " + rec.getEstimatedReturnDate().getFormattedDate()
                          + "\n" + "Actual Return Date:    " + returnDate.getFormattedDate());
                
                super.setRate();
                System.out.println(super.getRentalRate());
                
                if (lateDiff > 0) {
                        
                    rec.setRentalFee(estimateDiff, super.getRentalRate());
                    rec.setLateFee(lateDiff, super.getRentalRate());
                    System.out.println("Rental Fee:            " + super.df2.format(rec.getRentalFee())
                              + "\n" + "Late Fee:              " + super.df2.format(rec.getLateFee()));
                }
                else {
                    rec.setRentalFee(actualDiff, super.getRentalRate());
                    System.out.println("Rental Fee:            " + super.df2.format(rec.getRentalFee()));
                }
                
                //this method will return TRUE if the property now can be rented successfully.
                rec.setActualReturnDate(returnDate);
                super.setToReturn(true);
                return true;
               
            }
            else {
                System.out.println("Apartment " + super.getPropertyID() + " could not be returned.");
                return false;}
  
         
        }
        else {
            System.out.println("Apartment " + super.getPropertyID() + " could not be returned.");
            return false;
        }        
    }

 
    
    public boolean performMaintenance() {
        // ASSUME1: apartment maintenance is ONLY called on the current day.
        // ASSUME2: apartment rented even in future days will NOT allow maintenance.
//        DateTime today = new DateTime();
//        if (super.getStatus() == 2) {
//            super.setStartMaintain(today);
//            super.resetLastMaintainDate(today);
//            
//            System.out.println("Property " + super.getPropertyID() + " is under maintainance from today.");
//            return true;
//        }
//        else if (super.getStatus() == 3){
//            System.out.println("The property " + super.getPropertyID() + " has already been on maintenance.");
//            return true;
//        }
//        else {
//            System.out.println("The property has been rented and not available for maintenance."); 
//            return false;
//        }
//        
        return true;
        //this method will return true if the property is now under maintenance.
        
        
    }

   
    public boolean completeMaintenance(DateTime completionDate) {
        int diff = 1;
     
//        diff = DateTime.diffDays(completionDate, super.getStartMaintainDate());
//        
//        //Maintenance can be set to completed on the same day of last maintenance date.
//        if (super.getStatus() == 3 && diff > -2) {
//            
//            super.setLastMaintainDate(completionDate);
//            System.out.println(super.getPropertyID() + " has all maintenance completed and ready for rent." );
//            return true;
//            
//        }
//        else {
//            System.out.println("Invalid. Cannot complete maintenance for the property." );
//            return false;
//        }
        return true;
        
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
        
        if (super.records[0] == null) {
            s2 = "RENTAL RECORD:  empty" + "\n" +
                 "----------------------------------";
            return s1 + s2;
        }
        else if(super.records[0].getActualReturnDate() == null) {
            s4 = "Record ID:             " + super.records[0].getRecordID() + "\n" +
                 "Rent Date:             " + super.records[0].getRentDate().getFormattedDate() + "\n" +
                 "Estimated Return Date: " + super.records[0].getEstimatedReturnDate().getFormattedDate() + "\n" +
                    "----------------------------------";
            for (int i= 1; i<10; i++ ) {
                if(super.records[i]== null) break;
                s4 = s4 +  "\n" +
                     "Record ID:             " + super.records[i].getRecordID() + "\n" +
                     "Rent Date:             " + super.records[i].getRentDate().getFormattedDate() + "\n" +
                     "Estimated Return Date: " + super.records[i].getEstimatedReturnDate() + "\n" +
                     "Actual Return Date:    " + super.records[i].getActualReturnDate() + "\n" +
                     "Rental Fee:            " + super.records[i].getRentalFee() + "\n" +
                     "Late Fee:              " + super.records[i].getLateFee() + "\n" +
                     "----------------------------------";
                     
           }
            return s1 + s3 + s4;
       }
        
       else {
            for (int i= 0; i<10; i++ ) {
                if(super.records[i]== null) break;
                s4 = s4 +  "\n" +
                     "Record ID:             " + super.records[i].getRecordID() + "\n" +
                     "Rent Date:             " + super.records[i].getRentDate().getFormattedDate() + "\n" +
                     "Estimated Return Date: " + super.records[i].getEstimatedReturnDate() + "\n" +
                     "Actual Return Date:    " + super.records[i].getActualReturnDate() + "\n" +
                     "Rental Fee:            " + super.records[i].getRentalFee() + "\n" +
                     "Late Fee:              " + super.records[i].getLateFee() +
                     "----------------------------------";;
            }
            return s1 + s3 + s4;
       }

       
   
    }
    
    
    
}
