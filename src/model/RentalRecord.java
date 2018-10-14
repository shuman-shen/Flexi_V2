package model;
import utilities.DateTime;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RentalRecord {
    //private Property property;    
    private String recordID;
    private String customerID;
    private LocalDate rentDate;
    private LocalDate estimatedReturnDate;
    private LocalDate actualReturnDate;
    private double rentalFee;
    private double lateFee = 0;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    DecimalFormat df2 = new DecimalFormat(".##");
    
  
    
    public RentalRecord(String recordID, String customerID, LocalDate rentDate, LocalDate estimatedReturnDate) {
        this.recordID = recordID;
        this.customerID = customerID;
        this.rentDate = rentDate;
        this.estimatedReturnDate = estimatedReturnDate;
    }
    
    
    public String getRecordID() {
        return recordID;
    }
    public String getCustomerID() {
        return recordID;
    }
    public LocalDate getRentDate() {
        return rentDate;
        
    }
    public LocalDate getEstimatedReturnDate() {
        return estimatedReturnDate;
        
    }
    public LocalDate getActualReturnDate() {
        return actualReturnDate;
        
    }
    public double getRentalFee() {
        return rentalFee;
        
    }
    public double getLateFee() {
        return lateFee;
        
    }
    public double getPremiumLateFee() {
        return lateFee;
    }
    
    
    public void setRentalFee(double rentalFee) {
        this.rentalFee = rentalFee;
    }
    public void setLateFee(double lateFee) {
        this.lateFee = lateFee;
    }
    public void setActualReturnDate(LocalDate actual) {
        actualReturnDate = actual;
        
    }
    public void setRentalFee(int diff, double rentalRate) {
        rentalFee = diff * rentalRate;      
        
    }
    public void setLateFee(int lateDiff, double rentalRate) {
        
        lateFee = lateDiff * rentalRate * 1.15;
    }
    public void setPremiumLateFee(int lateDiff) {
        lateFee = lateDiff * 662;
    }
    
    
    
    
    
    // @override
    public String toString() {
        
        //recordId:rentDate:estimatedReturnDate:actualReturnDate:rentalFee:lateFee
        
        if(actualReturnDate == null) {
            return recordID+":"+rentDate+":"+estimatedReturnDate+":"+"none"+""+":"+"none"+":"+"none\n";
        }
        else {
            return recordID+":"+rentDate+":"+estimatedReturnDate.format(formatter)+":"
        +actualReturnDate.format(formatter)+":"+df2.format(rentalFee)+":"+df2.format(lateFee) +"\n";
        }
       
   }
    
    
    
    
    
    public String getDetails() {
        
       
              
//        String recordDetail;
//        
//        // formatting rental and late fee to 2 decimal places
//        
//        if (actualReturnDate == null) {
//            recordDetail = "Record ID:             "+ recordID
//                    + "\n"+"Rent Date:             " + rentDate.getFormattedDate()
//                    + "\n"+"Estimated Return Date: " + estimatedReturnDate.getFormattedDate();
//        }
//        else {
//            recordDetail = "Record ID:             "+ recordID
//                    + "\n"+"Rent Date:             " + rentDate
//                    + "\n"+"Estimated Return Date: " + estimatedReturnDate.getFormattedDate()
//                    + "\n"+"Actual Return Date:    " + actualReturnDate.getFormattedDate()
//                    + "\n"+"Rental Fee:            " + df2.format(rentalFee)
//                    + "\n"+"Late Fee:              " + df2.format(lateFee);
//            
//        }
//        return recordDetail;
//        
//        
        return null;
    }
    
    
    
    
    
  

    
    
}
