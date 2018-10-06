

import javafx.application.Application;
import view.MainWindow;
import controller.MainWindowControl;
import model.*;

public class Test {
    
    
    
    
    
    
    
    public static void main(String[] args) {


        
        Application.launch(MainWindow.class, args);
        FlexiRentSystem f = new FlexiRentSystem();
        MainWindowControl Mcontrol = new MainWindowControl(m, f);
        
        Mcontrol.Test();
        
        Application.launch(MainWindow.class, args);
      
      
  }
    
    
    
    
}
