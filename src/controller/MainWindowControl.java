package controller;

import java.util.ArrayList;

import javafx.application.Application;
import model.*;
import view.MainWindow;

public class MainWindowControl {
    
  
    private FlexiRentSystem flexiModel;
    private String propertyID;
    private int streetNum;
    private String streetName;
    private String suburb;
    private String fileName;
    final private String folder = "/view/";
    
    public MainWindowControl(FlexiRentSystem f) {
        flexiModel = f;
   
    }
    
  
    
    public FlexiRentSystem getFlexiRentSystem() {
        return flexiModel;
    }
    
    
        

        
        
        
        
    
}
