package model;
import utilities.DateTime;


public class PremiumSuite extends Property{
    
    public PremiumSuite(String propertyID, int streetNo, String streetName, String suburb, int bedNum, DateTime lastMaintainDate) {
            super(propertyID, streetNo, streetName, suburb, bedNum, lastMaintainDate);
      
    }
    
    /* If a Premium Suite is in a status of Available,
     * system automatically shift last maintenance date for 10-days rounds 
      until the nearest maintenance date before the desired rentDate when possible. */

    public boolean checkDate(DateTime date, int numOfDay) {
        if (numOfDay > 10) {
            int j = numOfDay / 10;
            DateTime newDate = new DateTime(date, j*10);            
            super.setLastMaintainDate(newDate);
            return true;
        }
        else if (numOfDay <=0 | numOfDay == 9) { 
            System.out.println("Property cannot be rented on this day.");
            return false;}
        else return true;
    }
    
    public boolean rent(String customerId, DateTime rentDate, int numOfRentDay) {
        
        String recordID = "";
        DateTime estimatedReturnDate = new DateTime(rentDate, numOfRentDay);        
        
        int diff1 = DateTime.diffDays(rentDate, super.getLastMaintainDate());
        
        
        if(super.getStatus()==2) {
            // Shift last recorded maintain date to current schedule;
            if(checkDate(super.getLastMaintainDate(),diff1)) {
                
                DateTime nextMaintainDate = new DateTime(super.getLastMaintainDate(), 10);
                int diff2 = DateTime.diffDays(nextMaintainDate, estimatedReturnDate);
                
                if(diff2 > 0) {
                recordID = super.getPropertyID() + "_"+ customerId + "_" + estimatedReturnDate.getEightDigitDate();
                
                RentalRecord r = new RentalRecord(recordID, customerId,rentDate, estimatedReturnDate);
                super.addRecord(r);
                
                System.out.println("Premium Suite " + super.getPropertyID() + " is now rented by customer "+ customerId);
                //this method will return TRUE if the property can be rented successfully.
                super.setToRent(true);
                return true;
                }
                else {
                    System.out.println("Premium Suite " + super.getPropertyID() + " could not be rented");
                    return false;}
                
            }
            
            else {
                System.out.println("Premium Suite " + super.getPropertyID() + " could not be rented");
                return false;    
            }      
      
        }
        else {
            System.out.println("Premium Suite " + super.getPropertyID() + " could not be rented");
            return false;
        }
       
        
    }
           
            
    public boolean returnProperty(DateTime returnDate) {
        
        int actualDiff = 2;
        int lateDiff = 0;
        int estimateDiff = 2;
        
        RentalRecord rec = super.records[0];
        actualDiff = DateTime.diffDays(returnDate, rec.getRentDate());
        estimateDiff = DateTime.diffDays(rec.getEstimatedReturnDate(), rec.getRentDate());
        lateDiff = DateTime.diffDays(returnDate, rec.getEstimatedReturnDate());
        
        
        if(super.getStatus() == 1) {
             
            if (actualDiff>0) {
                
                System.out.println("Premium Suite " + super.getPropertyID() + " is returned by customer "+ rec.getCustomerID());
                System.out.println("Property ID: " + super.getPropertyID()
                          + "\n" + "Address:     " + super.getStreetNo() + " " + super.getStreetName() + " " + super.getSuburb()
                          + "\n" + "Type:        Premium Suite"
                          + "\n" + "Bedroom:     " + super.getBedNum()
                          + "\n" + "Status:      Available");
                
                System.out.println("RENTAL RECORD");
                System.out.println("Record ID:             " + rec.getRecordID()
                          + "\n" + "Rent Date:             " + rec.getRentDate().getFormattedDate()
                          + "\n" + "Estimated Return Date: " + rec.getEstimatedReturnDate().getFormattedDate()
                          + "\n" + "Actual Return Date:    " + returnDate.getFormattedDate());
                
                super.setRate();
                if (lateDiff > 0) {
                    rec.setRentalFee(estimateDiff, super.getRentalRate());
                    rec.setLateFee(lateDiff, super.getRentalRate());
                    System.out.println("Rental Fee:            " + super.df2.format(rec.getRentalFee())
                              + "\n" + "Late Fee:              " + super.df2.format(rec.getPremiumLateFee()));
                }
                else {
                    rec.setRentalFee(actualDiff, super.getRentalRate());
                    System.out.println("Rental Fee:            " + super.df2.format(rec.getRentalFee()));
                }
                
                //this method will return TRUE if the property now can be rented successfully.
                super.setToReturn(true);
                return true;
               
            }
            else {
                System.out.println("Premium Suite " + super.getPropertyID() + " could not be returned.");
                return false;}
  
         
        }
        else {
            System.out.println("Premium Suite " + super.getPropertyID() + " could not be returned.");
            return false;
        }
        
        
       
    }

 
    
    public boolean performMaintenance() {
        // ASSUME: maintenance date = current day.

        DateTime today = new DateTime();
        if (super.getStatus() == 2) {
            super.setStartMaintain(today);
            super.resetLastMaintainDate(today);
            
            System.out.println("Property " + super.getPropertyID() + " is under maintainance from today.");
            return true;
        }
        else if (super.getStatus() == 3){
            System.out.println("The property " + super.getPropertyID() + " has already been on maintenance.");
            return true;
        }
        else {
            System.out.println("The property has been rented and not available for maintenance."); 
            return false;
        }
    }

   
    public boolean completeMaintenance(DateTime completionDate) {
        int diff = 1;
        
        diff = DateTime.diffDays(completionDate, super.getLastMaintainDate());
        
      //Maintenance can be set to completed on the same day of last maintenance date.
        if (super.getStatus() == 3 && diff > -2) {
            
            super.setLastMaintainDate(completionDate);
            System.out.println(super.getPropertyID() + " has all maintenance completed and ready for rent." );
            return true;
            
        }
        else {
            System.out.println("Invalid. Cannot complete maintenance for the property." );
            return false;
        }
    }
    
    public String toString() {
        
        //propertyId:streetNumber:streetName:suburb:propertyType:numOfBedRoom:status:lastMaintainDate
        
        String str =
        super.getPropertyID() + ":" + super.getStreetNo() + ":" 
        + super.getStreetName() + ":" + super.getSuburb() + ":" 
        + getType() + ":" + super.getBedNum() + ":"
        + super.convertStatus(super.getStatus()) + ":" + super.getLastMaintainDate().getFormattedDate();
        
        return str;
    }
    
    public String getDetails() {
        String s2 = "";
        final String s3 = "RENTAL RECORD";
        String s4 = "";
     
        
        String s1 = "Property ID:    " + super.getPropertyID() + "\n" +
                    "Address:        " + super.getStreetNo() + " " + super.getStreetName() + " " + super.getSuburb() + "\n" +
                    "Type:           " + "Premium Suite" + "\n" +
                    "Bedroom:        "  + super.getBedNum() + "\n" +
                    "Status:      " + super.convertStatus(super.getStatus()) + "\n" +
                    "Last maintenance: " + super.getLastMaintainDate().getFormattedDate() + "\n";
        
        if (super.records[0] == null) {
            s2 = "RENTAL RECORD:  empty" + "\n"+
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
                     "Late Fee:              " + super.records[i].getPremiumLateFee() + 
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
                     "Late Fee:              " + super.records[i].getPremiumLateFee() +
                     "----------------------------------";;
            }
            return s1 + s3 + s4;
        }
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
