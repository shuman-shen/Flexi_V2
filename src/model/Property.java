package model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

//import utilities.DLocalDate

public abstract class Property {
    
    private String propertyID;
    private int streetNo;
    private String streetName;
    private String suburb;
    private int bedNum;
    private LocalDate lastMaintainDate;
    //private LocalDate estReturnDate;
    private double rentalRate;
    private LocalDate startMaintenance;
    private int status = 2; // 1: rented, 2: available/returned, 3: under maintenance 
    private String image;
    private String description;
    private DateTimeFormatter shortDate = DateTimeFormatter.ofPattern("ddMMyyyy");
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private RentalRecord record;
    private ArrayList<RentalRecord> records;
    private DecimalFormat df2 = new DecimalFormat("#.00");
    
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
    
    public DateTimeFormatter getDateFormat() {return formatter;}
    public DateTimeFormatter getShortDateFormat() {return shortDate;}
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
    
    public void setToRent() {
        status = 1;        
    }
    
    public void setToReturn() {
       status = 2;        
    }
    
    public void setToMaintain() {
        status = 3;        
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
    
    public abstract void rent(String customerId, LocalDate rentDate, int numOfRentDay);
    
    public abstract void returnProperty(LocalDate returnDate);  
    
    public abstract void performMaintenance();       
    
    public abstract void completeMaintenance(LocalDate completionDate);
   
    public abstract String toString(); 
   
    public abstract String getDetails();
    
    public ArrayList<RentalRecord> getRecords(){
        return records;
    }
    
    
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
        records.add(0, record); 
        System.out.print(records.get(0).getRecordID());
        
        
    }
    
    
    public void getAllRecords() {
        
        records = new ArrayList<RentalRecord>();
        Connection conn = null;
        
        String rID = "";
        String cID = "";
        String rentDate = "";
        String estDate = "";
        String actualDate = "";
        double fee;
        double lateFee;
    
        
        
        
        try {
            // db parameters
            String url = "jdbc:sqlite:src/database/FlexiData.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            //System.out.println("Connection to SQLite has been established.");
            
            String sql = "SELECT recordID, customerID, "
                    + "rentDate, estimatedReturnDate, actualReturnDate, "
                    + "rentalFee, lateFee FROM RentalRecord WHERE propertyID = \'" + propertyID
                    + "\' ORDER BY rentDate DESC";
            
            System.out.println(sql);
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
           
           // loop through the result set
           while (rs.next()) {             
               rID = rs.getString("recordID");
               cID = rs.getString("customerID");
               rentDate = rs.getString("rentDate");
               estDate = rs.getString("estimatedReturnDate");
               actualDate = rs.getString("actualReturnDate");
               fee = rs.getDouble("rentalFee");
               lateFee = rs.getDouble("lateFee");               
               
           
           
           
           
           if(rID!=null&& rentDate !=null) {
          //TODO TYPE NOT MATCH EXCEPTION  
               
               LocalDate rDate = LocalDate.parse(rentDate, formatter);
               LocalDate eDate = LocalDate.parse(estDate, formatter);
               
               record = new RentalRecord(rID, cID, rDate , eDate);
               
               if(actualDate != null) {
                   System.out.println("Check date string emptiness");
                   
                   LocalDate aDate = LocalDate.parse(actualDate, getDateFormat());
                   record.setActualReturnDate(aDate);
                   record.setRentalFee(fee);
                   record.setLateFee(lateFee);          
               }
               System.out.println("rID " + rID + " is added.");
               records.add(record);
               System.out.println(record.getRecordID());
               
           }
           }
          
           conn.close();
  
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }             
   
    
    
         
   
    
}
