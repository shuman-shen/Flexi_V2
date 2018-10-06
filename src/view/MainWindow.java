package view;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.*;
//import ui_controls.MenuItemListener;
//import ui_controls.ComboBoxListener1;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class MainWindow extends Application{

    
    private GridPane listView;
    private VBox property;
    private Image img;
    private ImageView imgV;
    private Text propertyID;
    private Text address;
    private Text desc;
    final private Button moreButton = new Button("More details...");
    
    private ArrayList<VBox> list = new ArrayList<VBox>();
    final private String path = "/view/";
    final private String pathN = "/view/No_Image_Available.png";
    
    
    public ImageView getImageView() {return imgV;}
    public Text getPropertyID() {return propertyID;}
    public Text getAddress() {return address;}
    public Text getDesc() {return desc;}
    public VBox getVBox() {return property;}
    public ArrayList<VBox> getArrayList(){return list;}
    
    public void setImageView(String fileName) {
        String p = "";
        p = path + fileName;
        
        
        try {
            img = new Image(p);
            imgV = new ImageView();
            imgV.setImage(img);
            imgV.setFitWidth(240);
            imgV.setFitHeight(160);
            //return imgV;
        }
        catch (IllegalArgumentException i){
           
            //TODO CHANGE WARNING TO DIALOG BOX
            System.out.println("image path invalid");
            
            try {
                Image noImg = new Image(pathN);
                ImageView noImgV = new ImageView();
                noImgV.setImage(noImg);
                //return noImgV;
            }
            catch (IllegalArgumentException e) {
                System.out.println("image path invalid");
                //return null;
            }          
        }                
    }    
    
    public void setPropertyID(String ID) {
        propertyID = new Text(ID); 
        propertyID.setFont(Font.font("Verdana",FontWeight.BOLD, 20));
    }
    public void setAddress(int streetNum, String streetName, String suburb) {
        address = new Text(streetNum + " " + streetName + ", " + suburb);
        address.setFont(Font.font("Verdana",FontWeight.BOLD, 14));
    }
    public void setDesc(String d) {
        desc = new Text(d);
    }
    
    public void setVBox(ImageView imgV, Text ID, Text address, Text desc){
        property = new VBox(10);
        
        property.getChildren().addAll(imgV, ID, address, desc, moreButton);
        list.add(property);
    }
    
    
    
    public void inputList() {
        int i = 0; 
        int num = 1;
        int j = 0;
        for(VBox v : list) {
            
            if (num%3 == 1) {
                listView.add(v, 0, i);
                i++;
                num++;
            }
            else if (num%3 ==2){
                listView.add(v, 1, i);
                i++;
                num++;
            } 
            else {
                listView.add(v, 2, i);
                i = 0;
                num++;
            }
        }
    }
    
    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        // Create a pane and set its properties
        BorderPane root = new BorderPane();
        
        
        // Create main view
        VBox mainView = new VBox(10);
        mainView.setPadding(new Insets(10, 10, 10, 10));
      
        
        // Create filter bar
        HBox filterBar = new HBox(10);
        mainView.setPadding(new Insets(10, 10, 10, 10));
        
        Label filterLabel = new Label("Filters: ");
        filterLabel.setPrefSize(56, 30);
        filterLabel.setFont(new Font(14));
        filterLabel.setAlignment(Pos.CENTER);
        //filterLabel.setStyle("-fx-border-color: black");
        
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("All Property Types","Apartment", "Premium Suite");
        typeBox.setValue("All Property Types");
        
        ComboBox<String> roomBox = new ComboBox<>();
        roomBox.getItems().addAll("All Bedrooms Types","3 Bedrooms","2 Bedrooms", "1 Bedroom");
        roomBox.setValue("All Bedrooms Types");        
        
        ComboBox<String> availBox = new ComboBox<>();
        availBox.getItems().addAll("All Conditions","Available","Rent", "Under Maintenance");
        availBox.setValue("All Conditions");

        //TODO: generate suburb list from database
        ComboBox<String> suburbBox = new ComboBox<>();
        suburbBox.getItems().addAll("All Suburbs", "Melbourne","Fitzory", "Kensington","Carlton");
        suburbBox.setValue("All Suburbs");
        
        Button searchBtn = new Button("Search");
        searchBtn.setPrefSize(70, 30);
        
        filterBar.getChildren().addAll(filterLabel, typeBox, roomBox, availBox, suburbBox, searchBtn);
       
              
        //Scroll bar for the main view, 
        ScrollPane scrollInfo = new ScrollPane();               
        scrollInfo.setContent(mainView);       
        scrollInfo.setVbarPolicy(ScrollBarPolicy.ALWAYS); //Always show vertical scroll bar
        scrollInfo.setHbarPolicy(ScrollBarPolicy.AS_NEEDED); // Horizontal scroll bar is only displayed when needed
        
        //Grid pane for display property list
        listView = new GridPane();
        listView.setGridLinesVisible(true);
        
        listView.setPadding(new Insets(20));
        listView.setHgap(10);
        listView.setVgap(10);
        ColumnConstraints column = new ColumnConstraints(240);        
        listView.getColumnConstraints().addAll(column, column, column);
        //inputList();
        
                       
        mainView.getChildren().addAll(filterBar, listView);
        
        
        //Create menu bar
        MenuBar menuBar = new MenuBar();
        creatMenu(menuBar);
        
        root.setTop(menuBar);
        //root.setBottom(filterBar);
        root.setCenter(scrollInfo);
        
        // Create a scene and place the pane in the stage
        Scene scene = new Scene(root);
        primaryStage.setWidth(820);
        primaryStage.setHeight(500);
        primaryStage.setTitle("Flexi Rent System"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage
    }
    
    public void creatMenu(MenuBar menuBar) {
        
      
      //MenuBar menuBar = new MenuBar();
      Menu fileMenu = new Menu("Property");
      Menu dataMenu = new Menu("Data");
      Menu sysMenu = new Menu("System");
      
      MenuItem newMenuItem = new MenuItem("Add Property");
      newMenuItem.setOnAction(new AddPropertyListener());
      MenuItem rentMenuItem = new MenuItem("Rent Property");
      rentMenuItem.setOnAction(new SearchListener());
      MenuItem returnMenuItem = new MenuItem("Return Property");
      returnMenuItem.setOnAction(new SearchListener());
      MenuItem startMenuItem = new MenuItem("Property Maintenance");
      startMenuItem.setOnAction(new SearchListener());
      MenuItem endMenuItem = new MenuItem("Complete Maintenance");
      endMenuItem.setOnAction(new SearchListener());
      MenuItem allMenuItem = new MenuItem("Display All Properties");
      //allMenuItem.setOnAction(new DisplayListener(allMenuItem));
      
      
      MenuItem homeMenuItem = new MenuItem("Home Screen");
      //saveMenuItem.setOnAction(new MenuItemListener(saveMenuItem));
      MenuItem quitMenuItem = new MenuItem("Quit");
      
      MenuItem importMenuItem = new MenuItem("Import Data");
      //exitMenuItem.setOnAction(new MenuItemListener(exitMenuItem));
      
      MenuItem exportMenuItem = new MenuItem("Export Data");       
      
      fileMenu.getItems().addAll(newMenuItem, rentMenuItem, returnMenuItem, startMenuItem, endMenuItem, allMenuItem);
      dataMenu.getItems().addAll(importMenuItem, exportMenuItem);
      sysMenu.getItems().addAll(homeMenuItem, quitMenuItem);
      menuBar.getMenus().addAll(fileMenu, dataMenu, sysMenu);
      
      //return menuBar;     
        
        
    }
    
    public void createSearchDialog() {
        
        
        
        
    }
    
    
    
    
    
    /**
    * The main method is only needed for the IDE with limited JavaFX support. Not
    * needed for running from the command line.
    */
   // public static void main(String[] args) {
   //     launch(args);
   // }
    
    
}
