package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javafx.application.Application;
import model.*;
import view.MainWindow;

public class MainWindowControl {
    
  
    private FlexiRentSystem flexiModel;
    private LocalDate today;
    
    public MainWindowControl(FlexiRentSystem f) {
        flexiModel = f;
        //filteredList = new ArrayList<Property>(flexiModel.getPropertyList());
        
    }   
    
    
    public FlexiRentSystem getFlexiRentSystem() {
        return flexiModel;
    }
    
    public ArrayList<Property> getFilteredList(){
        return flexiModel.getFilteredList();
    }
//  
    public ArrayList<Property> getWholeList(){
        flexiModel.getMainList();
        return flexiModel.getPropertyList();
    }
    
    public ArrayList<String> getAllSuburb() {
        return flexiModel.getSuburbList();
    }
    
    
    public void rent(String propertyID, String customerId, LocalDate rentDate, int numOfRentDay) {
        flexiModel.setFilterList(propertyID);
        flexiModel.getFilteredList().get(0).rent(customerId, rentDate, numOfRentDay);
    }
    
    public void returnProperty(String propertyID, LocalDate returnDate) {
        flexiModel.setFilterList(propertyID);
        flexiModel.getFilteredList().get(0).returnProperty(returnDate);
    }
    
    public void performMaintenance(String propertyID) {
        flexiModel.setFilterList(propertyID);
        flexiModel.getFilteredList().get(0).performMaintenance();
    }
    
    public void completeMaintenance(String propertyID, LocalDate completionDate) {
        flexiModel.setFilterList(propertyID);
        flexiModel.getFilteredList().get(0).completeMaintenance(completionDate);
    }
    
    
    
   
//   public Collection<Property> getCollection(){
//       return flexiModel.getCollection();
//   }
    
    public String createApartment(String image, String streetNum, String streetName, String suburb, int bedNum, String desc) throws Exception{
        String id = "1";
        
//        if(image.equals("") || streetName.equals("") || streetNum.equals("") || suburb.equals("") || desc.equals("")) {
//            throw new Exception("Empty fields exist");
//        }
//        else {
//            
            
            System.out.println("RECEIVED");
            int streetNo = Integer.parseInt(streetNum);
            id = flexiModel.addApartment(streetNo, streetName, suburb, bedNum, image, desc);
            System.out.println(id + " return to control");
            
           
            
        //}
        return id;
    }
    
    public String createPremiumSuite(String image, String streetNum, String streetName, String suburb, LocalDate lastMaintainDate, String desc) throws Exception{
        today = LocalDate.now();
        
        if(image.equals("") || streetName.equals("") || streetNum.equals("") || suburb.equals("") ||lastMaintainDate.equals("") || desc.equals("")) {
            throw new Exception("Empty fields exist");
        }
        else if(lastMaintainDate.compareTo(today)>0){
            throw new Exception("Last maintenance Date shall not be later than today.");
            
        }
        else {
            System.out.println("RECEIVED");
            int streetNo = Integer.parseInt(streetNum);
            
            String id = flexiModel.addPremiumSuite(streetNo, streetName, suburb, lastMaintainDate, image, desc);
            System.out.println(id + " return to control");
            
            
            return id;
        }
    
    
    }
    
    
    public boolean setFilter(String type, String bedNum, String condition, String suburbText) {
   
        String pType;
        int num;
        int cType;
        String suburb;
        if(type.startsWith("All")){
            pType = "All";
        }
        else pType = type;
        
        if (bedNum.startsWith("All")) {
            num = 0;           
        }
        else {
            num = Integer.parseInt(Character.toString(bedNum.charAt(0)));
        }
        
        if (condition.startsWith("All")) {
            cType = 0;
        }
        else if(condition.equals("Available")){
            cType = 2;
        }
        else if(condition.equals("Rent")) {
            cType = 1;            
        }
        else {cType = 3;}
        
        
        if(suburbText.equals("All Suburbs"))
        {
            suburb = "General";
        }       
        else {
            
           suburb = suburbText;
        }
        if (pType.equals("All") && num == 0 && cType == 0 && suburb.equals("General")) 
            return false;
        else {
            flexiModel.setFilterList(pType, num, cType, suburb);
            
            return true;
        }
        
        
    }
    
        
    public boolean setFilter(String propertyID) {
        return flexiModel.setFilterList(propertyID);
    }
    
    
        
        
        
        
    
}
