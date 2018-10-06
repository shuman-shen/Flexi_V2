package controller;

import java.util.ArrayList;

import javafx.application.Application;
import model.*;
import view.MainWindow;

public class MainWindowControl {
    
    private MainWindow m;
    private FlexiRentSystem f;
    public MainWindowControl(MainWindow m, FlexiRentSystem f) {
        this.m = m;
        this.f = f;
    }
    
    public MainWindow getMainWindow() {
        return m;
    }
    
    public FlexiRentSystem getFlexiRentSystem() {
        return f;
    }
    
    public void Test() {
       
        f.getMainList();
        
        ArrayList<Property> l = f.getPropertyList();
        
        String ID = f.getPropertyList().get(0).getPropertyID();
        String desc = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        
        for(Property p : l) {
            String propertyID = p.getPropertyID();
            int streetNum = p.getStreetNo();
            String streetName = p.getStreetName();
            String suburb = p.getSuburb();
            String path = p.getImage();
            
            m.setImageView(path);
            m.setPropertyID(propertyID);
            m.setAddress(streetNum, streetName, suburb);
            m.setDesc(desc);
            
            m.setVBox(m.getImageView(), m.getPropertyID(), m.getAddress(), m.getDesc());           
            m.getArrayList().add(m.getVBox());
            
        }
        

        
        
        
        
    }
}
