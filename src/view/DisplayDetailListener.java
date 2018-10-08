package view;

import java.util.ArrayList;

import controller.MainWindowControl;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.BorderPane;
import model.Property;

public class DisplayDetailListener implements EventHandler<ActionEvent> {
    
    private String pID;
    private MainWindowControl mainControl;
    private BorderPane root;
    private ArrayList<Property> tmp;
    private Property curItm;
    
    public DisplayDetailListener(String pID, BorderPane root, MainWindowControl mainControl) {
        this.pID = pID;
        this.mainControl = mainControl;
    }
    
    @Override
    public void handle(ActionEvent event) {
        // search ID
        if(mainControl.setFilter(pID))
            tmp = mainControl.getFilteredList();
            
        // get list item(0)
            curItm = tmp.get(0);
            //System.out.println(curItm.getDescriiption());
        //
        
    }

}
