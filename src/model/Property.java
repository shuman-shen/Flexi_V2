package model;
import java.text.DecimalFormat;
import java.time.LocalDate;

import utilities.DateTime;

public abstract class Property {
    
    private String propertyID;
    private int streetNo;
    private String streetName;
    private String suburb;
    private int bedNum;
    private LocalDate lastMaintainDate;
    private double rentalRate;
    private LocalDate startMaintenance;
    private int status = 2; // 1: rented, 2: available/returned, 3: under maintenance 
    private String image;
    private String description;
    
    RentalRecord[] records = new RentalRecord[10];
    DecimalFormat df2 = new DecimalFormat("#.00");
    
    public Property(String propertyID, int streetNo, String streetName, 
            String suburb, int bedNum, LocalDate lastMaintainDate, 
            int status, String image, String description) {
        this.propertyID = propertyID;
        this.streetNo = streetNo;
        this.streetName = streetName;
        this.suburb = suburb;
        this.bedNum = bedNum;
        this.lastMaintainDate = lastMaintainDate;
        this.status = status;
        this.image = image;
        this.description = description;
        
       
    }
    
    public Property(String propertyID, int streetNo, String streetName, 
            String suburb, int bedNum, LocalDate lastMaintainDate ) {
        this.propertyID = propertyID;
        this.streetNo = streetNo;
        this.streetName = streetName;
        this.suburb = suburb;
        this.bedNum = bedNum;
        this.lastMaintainDate = lastMaintainDate;
        
       
    }
    
    public String getPropertyID() {return propertyID;}
    public int getStreetNo() {return streetNo;};
    public String getStreetName() {return streetName;};
    public String getSuburb(){return suburb;};
    public int getBedNum() {return bedNum;};
    public double getRentalRate() {return rentalRate;}
    public int getStatus() {return status;}
    public LocalDate getStartMaintainDate() {return startMaintenance;}
    public LocalDate getLastMaintainDate() {return lastMaintainDate;}
    public String getImage() {return this.image;}
    public String getDescription() {return description;}
    
    public void setToRent(boolean check) {
        if(check == true) status = 1;        
    }
    
    public void setToReturn(boolean check) {
        if(check == true) status = 2;        
    }
    
    public void setToMaintain(boolean check) {
        if(check == true) status = 3;        
    }
    
    public void setRate() {
        rentalRate = getRate(bedNum);
    }
    
    public void setStartMaintain(LocalDate today) {
        startMaintenance = today;
        status = 3;
    }
    
    public void setLastMaintainDate(LocalDate newDate) {
        lastMaintainDate = newDate;
        status = 2;
    }
    
    public void resetLastMaintainDate(LocalDate startMaintenance) {
        lastMaintainDate = startMaintenance;
    }
    
    public abstract double getRate(int bedNum);
    
    public abstract String getType();
    
    public abstract boolean rent(String customerId, DateTime rentDate, int numOfRentDay);
    
    public abstract boolean checkDate(DateTime date, int numOfDay);
    
    public abstract boolean returnProperty(DateTime returnDate);  
    
    public abstract boolean performMaintenance();       
    
    public abstract boolean completeMaintenance(DateTime completionDate);
   
    public abstract String toString(); 
   
    public abstract String getDetails();
    
    
    public String convertStatus(int s) {
        String str = "";
        if (s == 1) str = "Rented";
        if (s == 2) str = "Available";
        if (s == 3) str = "Under_Maintenance";
        
        return str;
    }

    
    //use an array to implement that rental record collection for each property, 
    //in which the first element of the array is always the latest rental record
    //the second element of the array is always the second latest rental record, and so on. 
    //the 10 most recent times that property has been rented
   
    public void addRecord(RentalRecord record) {
        int count = 9;
        for(int i = 0; i<10; i++) {
            if(records[i]==null) {
                count = i;
                break;
            }
            
        }
        if (count == 0) records[0] = record;
        else {
            for (int i = count; i> 0; i--) {
                records[i] = records[i-1];
            }
            
            records[0] = record;
        }           
        
        
    }
    
}
