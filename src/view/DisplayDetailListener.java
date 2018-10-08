package view;

import java.util.ArrayList;

import controller.MainWindowControl;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Property;

public class DisplayDetailListener implements EventHandler<ActionEvent> {
    
    private String pID;
    private MainWindowControl mainControl;
    private BorderPane root;
    private ScrollPane scroll;
    private MenuBar menu;
    private HBox detailView;
    private VBox recordView;
    private VBox itemView;
    private ImageView imgV;
    private Button rentBtn;
    private Button returnBtn;
    private Button startBtn;
    private Button endBtn;
    
    private ArrayList<Property> tmp;
    private Property curItm;
    private int status;
    private final String path = "/view/images/";
    final private String pathN = "/view/images/No_Image_Available.png";
    public DisplayDetailListener(String pID, MainWindowControl mainControl) {
        this.pID = pID;
        //this.root = root;
        this.mainControl = mainControl;
    }
    
    @Override
    public void handle(ActionEvent event) {
        // search ID
        if(mainControl.setFilter(pID))
            tmp = mainControl.getFilteredList();
            
        // get list item(0)
            curItm = tmp.get(0);
            status = curItm.getStatus();
            //System.out.println(curItm.getDescriiption());
        //
            
            
            detailView = new HBox(10);
            itemView = new VBox(10);
            recordView = new VBox(10);
            root = new BorderPane();
            
            createDetailView();
            
            
            
            scroll = new ScrollPane();               
            scroll.setContent(detailView);       
            scroll.setVbarPolicy(ScrollBarPolicy.ALWAYS); //Always show vertical scroll bar
            scroll.setHbarPolicy(ScrollBarPolicy.AS_NEEDED); // Horizontal scroll bar is only displayed when needed
            
            root.setCenter(scroll);
            
            Scene newScene = new Scene(root);
            Stage detailStage = new Stage();
            
            detailStage.setWidth(800);
            detailStage.setHeight(600);
            detailStage.setTitle("Property details"); // Set the stage title
            detailStage.setScene(newScene); // Place the scene in the stage
            
            detailStage.show(); // Display the stage
            
            
            System.out.println("This step");
            
       
            
    }
   
    public void createDetailView() {
    
        setImage();   
        
        Label propertyID = new Label(curItm.getPropertyID());
        
        String a = curItm.getStreetNo() + " " + curItm.getStreetName() + " " + curItm.getSuburb();
        Label address = new Label(a);
        //address.setWrapText(true);
        
        Label lastMaintainDate = new Label("Last Mainetance Date: ");
        
        GridPane btnGrid = new GridPane();
        btnGrid.setGridLinesVisible(true);
        
        btnGrid.setPadding(new Insets(20));
        btnGrid.setHgap(10);
        btnGrid.setVgap(10);
        ColumnConstraints column = new ColumnConstraints(160);        
        btnGrid.getColumnConstraints().addAll(column, column);
        
        rentBtn = new Button("Rent Property");
        btnGrid.add(rentBtn,0,0);
        
        returnBtn = new Button("Return Property");
        btnGrid.add(returnBtn,1,0);
        
        startBtn = new Button("Start Maintenance");
        btnGrid.add(startBtn,0,1);
        
        endBtn = new Button("Finish Maintenance");
        btnGrid.add(endBtn,1,1);
        
        
        Text desc = new Text(curItm.getDescription());
        desc.setWrappingWidth(400);
        
        
        itemView.getChildren().addAll(imgV, propertyID, address, btnGrid, desc);
        detailView.getChildren().add(itemView);
       
        //System.out.println("That step");
        
        
        
    }
    
    public void setImage() {
        String cPath = path+ curItm.getImage();
        try {
            Image img = new Image(cPath);
            imgV = new ImageView();
            imgV.setImage(img);
            imgV.setFitWidth(400);
            imgV.setFitHeight(300);
            //return imgV;
        }
        catch (IllegalArgumentException i){
           
            //TODO CHANGE WARNING TO DIALOG BOX
            System.out.println("image path invalid");
            
            try {                
                Image noImg = new Image(pathN);
                imgV = new ImageView();
                imgV.setImage(noImg);
                imgV.setFitWidth(400);
                imgV.setFitHeight(400);
              
                //return noImgV;
            }
            catch (IllegalArgumentException e) {
                System.out.println("image path invalid");
                //return null;
            }
        }
    }

}
