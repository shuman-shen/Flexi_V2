package controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javafx.application.Application;
import model.*;
import view.MainWindow;

public class MainWindowControl {
    
  
    private FlexiRentSystem flexiModel;
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
        return flexiModel.getPropertyList();
    }
    
    public ArrayList<String> getAllSuburb() {
        return flexiModel.getSuburbList();
    }
    
   
//   public Collection<Property> getCollection(){
//       return flexiModel.getCollection();
//   }
    
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
