package view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import controller.MainWindowControl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.*;
import model.FlexiRentSystem;
import model.Property;
//import ui_controls.MenuItemListener;
//import ui_controls.ComboBoxListener1;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class MainWindow{

    
    
    
    private MainWindowControl mainControl;
    private FlexiRentSystem flexiModel;    
    private HBox filterBar;
    private VBox mainView;
    //private StackPane root;
    private BorderPane root;
    private GridPane listView;
    private MenuBar menuBar;
    private ScrollPane scrollInfo;
    private VBox property;
    private Image img;
    private ImageView imgV;
    private Button pID;
    private Text address;
    private Label desc;
    
    
    private HBox detailView;
    private VBox recordView;
    private VBox itemView;
    private Label detailID;
    private Label adr;
    //private ImageView imgV2;
    private GridPane btnGrid;
    private Button rentBtn;
    private Button returnBtn;
    private Button startBtn;
    private Button endBtn;
    
    private ArrayList<Property> tmp;
    private Property curItm;
    private int status;
    
    final private Button moreButton = new Button("More details...");
    
    private ArrayList<VBox> list = new ArrayList<VBox>();
    
    final private String pathN = "/view/images/No_Image_Available.png";
    final private String folder = "/view/images/";
    
    
    
    
    public ImageView getImageView() {return imgV;}
    public String getPropertyID() {return pID.getText();}
    public Text getAddress() {return address;}
    public Label getDesc() {return desc;}
    public VBox getVBox() {return property;}
    public ArrayList<VBox> getArrayList(){return list;}
    
    public Parent asParent() {
        return root;
    }
    
    public MainWindow(MainWindowControl m, FlexiRentSystem f) {
        mainControl = m;
        flexiModel = f;
        
        setMainList(mainControl.getWholeList());
        createView();
    }
    
    public void setImageView(String fileName) {
        String p = "";
        p = folder + fileName;
        //String t = "/src/view/images/A02.jpeg";
        //System.out.print("image path" + p);
        
        
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
                imgV = new ImageView();
                imgV.setImage(noImg);
                imgV.setFitWidth(240);
                imgV.setFitHeight(160);
              
                //return noImgV;
            }
            catch (IllegalArgumentException e) {
                System.out.println("image path invalid");
                //return null;
            }
        }                
    }    
    
    public void setPropertyID(String ID) {
        pID = new Button(ID); 
        
        String n = pID.getText();
        pID.setOnAction(new DisplayDetailListener(pID.getText(),mainControl));
//        pID.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                
//               //createDetailView(pID.getText());
//                System.out.println("ID: " + pID.getText());
//            } 
//        });
        pID.setFont(Font.font("Calibri",FontWeight.BOLD, 16));
    }
    public void setAddress(int streetNum, String streetName, String suburb) {
        address = new Text(streetNum + " " + streetName + ", " + suburb);
        address.setFont(Font.font("Calibri",FontWeight.BOLD, 12));
    }
    public void setDesc(String d) {
        desc = new Label(d);
        desc.setWrapText(true);
    }
    
    public void setVBox(ImageView imgV, Text ID, Text address, Text desc){
        property = new VBox(10);
        
        property.getChildren().addAll(imgV, ID, address, desc, moreButton);
    }
    
    public void setMainList(ArrayList<Property> list) {
        
        int i = 0; 
        int num = 1;
        
        //flexiModel.getMainList();
        
        //ArrayList<Property> list = flexiModel.getPropertyList();
        
      //Grid pane for display property list
        listView = new GridPane();
        listView.setGridLinesVisible(true);
        
        listView.setPadding(new Insets(20));
        listView.setHgap(10);
        listView.setVgap(10);
        ColumnConstraints column = new ColumnConstraints(240);        
        listView.getColumnConstraints().addAll(column, column, column);
        
        String d="";
        for(Property p : list)
         {
            String propertyID = p.getPropertyID();
            int streetNum = p.getStreetNo();
            String streetName = p.getStreetName();
            String suburb = p.getSuburb();
            String fName = p.getImage();
            //System.out.println("Image" + fName);
            setImageView(fName);
            setPropertyID(propertyID);
            setAddress(streetNum, streetName, suburb);
            setDesc(d);
            
            //setVBox(getImageView(), getPropertyID(), getAddress(), getDesc());                       
            property = new VBox(10);
            
            property.getChildren().addAll(imgV, pID, address, desc);
            
            
            
            
//            pID.setOnAction(details ->
//            {  System.out.print(pID.getText());
//               createDetailView(pID.getText());
//               //changeTop();
//           });
            
            RowConstraints row = new RowConstraints(300);
            if (num%3 == 1) {
                listView.getRowConstraints().add(row);
                listView.add(property, 0, i);
                //System.out.println("Coordinates:  0, " + i);
                //System.out.println("Number:  " + num);
                num++;
            }
            else if (num%3 ==2){
                listView.add(property, 1, i);
                //System.out.println("Coordinates:  1, " + i);
                //System.out.println("Number:  " + num);
                num++;
            } 
            else {
                listView.add(property, 2, i);
                //System.out.println("Coordinates:  2, " + i);
                //System.out.println("Number:  " + num);
                i ++;
                num++;
            }
            
        }
        //mainView.getChildren().add(listView);
        
        
    }
    
    
    public void createDetailView(String ID) {
        
        if(mainControl.setFilter(ID))
            tmp = mainControl.getFilteredList();
            
        // get list item(0)
            curItm = tmp.get(0);
            System.out.println("\n ID get is: " +curItm.getPropertyID());
            status = curItm.getStatus();
        setImageView(curItm.getImage());   
        
        detailID = new Label(curItm.getPropertyID());
        
        String a = curItm.getStreetNo() + " " + curItm.getStreetName() + " " + curItm.getSuburb();
        adr = new Label(a);
        //address.setWrapText(true);
        
        Label lastMaintainDate = new Label("Last Mainetance Date: ");
        
        btnGrid = new GridPane();
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
        
        detailView = new HBox(10);
        itemView = new VBox(10);
        recordView = new VBox(10);
        itemView.getChildren().addAll(imgV, detailID, address, lastMaintainDate, btnGrid, desc);
        //detailView.getChildren().add(itemView);
        
        
        detailView.getChildren().add(itemView);
        root.setCenter(detailView);
        //detailView.setVisible(true);
        //root.getChildren().add(detailView);
       
       
        //System.out.println("That step");
        
        
        
    }
    
//    private void changeTop() {
//        ObservableList<Node> childs = this.root.getChildren();
//  
//        if (childs.size() > 1) {
//            //
//            Node topNode = childs.get(childs.size()-1);
//           
//            // This node will be brought to the front
//            Node newTopNode = childs.get(childs.size()-2);
//                   
//            topNode.setVisible(false);
//            topNode.toBack();
//           
//            newTopNode.setVisible(true);
//        }
//    }
    
    
    
    
    //@Override // Override the start method in the Application class
    public void createView() {
        // Create a pane and set its properties
        root = new BorderPane();
        
        
        // Create main view
        mainView = new VBox(10);
        mainView.setPadding(new Insets(10, 10, 10, 10));
      
        
               
              
        //Scroll bar for the main view, 
        scrollInfo = new ScrollPane();               
        scrollInfo.setContent(mainView);       
        scrollInfo.setVbarPolicy(ScrollBarPolicy.ALWAYS); //Always show vertical scroll bar
        scrollInfo.setHbarPolicy(ScrollBarPolicy.AS_NEEDED); // Horizontal scroll bar is only displayed when needed
        
        
        createFilterBar();   
                       
        mainView.getChildren().add(filterBar);
        mainView.getChildren().add(listView);
        
        //Create menu bar
        menuBar = new MenuBar();
        createMenu();
        
        root.setTop(menuBar);
        //pane.setBottom(filterBar);
        root.setCenter(scrollInfo);
        //pane.setVisible(true);
        //root = new StackPane();
        //root.getChildren().add(pane);
        
        
    }
    
    public void createMenu() {
        
      
      //MenuBar menuBar = new MenuBar();
      Menu fileMenu = new Menu("Property");
      Menu dataMenu = new Menu("Data");
      Menu sysMenu = new Menu("System");
      
      MenuItem newMenuItem = new MenuItem("Add Property");
      newMenuItem.setOnAction(new AddPropertyListener(mainControl));
      MenuItem rentMenuItem = new MenuItem("Rent Property");
      rentMenuItem.setOnAction(new SearchListener(mainControl));
      
      
      MenuItem homeMenuItem = new MenuItem("Home Screen");
      homeMenuItem.setOnAction(event -> {
          setMainList(mainControl.getWholeList());
          mainView.getChildren().set(1, listView);
          root.setCenter(scrollInfo);
      });
      MenuItem quitMenuItem = new MenuItem("Quit");
      quitMenuItem.setOnAction(e -> Platform.exit());
      
      MenuItem importMenuItem = new MenuItem("Import Data");
      //exitMenuItem.setOnAction(new MenuItemListener(exitMenuItem));
      
      MenuItem exportMenuItem = new MenuItem("Export Data");       
      
      fileMenu.getItems().addAll(newMenuItem, rentMenuItem);
      dataMenu.getItems().addAll(importMenuItem, exportMenuItem);
      sysMenu.getItems().addAll(homeMenuItem, quitMenuItem);
      menuBar.getMenus().addAll(fileMenu, dataMenu, sysMenu);
      
      //return menuBar;     
        
        
    }
    
    
    public void createFilterBar(){
     // Create filter bar
        filterBar = new HBox(10);
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
        suburbBox.getItems().add("All Suburbs");
        
        //TODO EXCEPTION FOR NO VALUE IN LIST??
        for(String k : mainControl.getAllSuburb()) {
            suburbBox.getItems().add(k);
        }
        
        suburbBox.setValue("All Suburbs");
        
        Button filterBtn = new Button("Filter");
        filterBtn.setOnAction(event ->
        {
            
            
            boolean toFilter = mainControl.setFilter(typeBox.getValue(), roomBox.getValue(), availBox.getValue(), suburbBox.getValue());
            //listView.getChildren().removeAll();
            if (toFilter == false) {
                //listView.getChildren().removeAll();
                setMainList(mainControl.getWholeList());
                mainView.getChildren().set(1, listView);
            }
            else {
                
                //listView.getChildren().removeAll();
                setMainList(mainControl.getFilteredList());
                mainView.getChildren().set(1, listView);
                System.out.println("Filter set");
            }
            
        });
        filterBtn.setPrefSize(70, 30);
        
        filterBar.getChildren().addAll(filterLabel, typeBox, roomBox, availBox, suburbBox, filterBtn);

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
