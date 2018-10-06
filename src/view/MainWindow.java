package view;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.*;
//import ui_controls.MenuItemListener;
//import ui_controls.ComboBoxListener1;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class MainWindow extends Application{

    
    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        // Create a pane and set its properties
        BorderPane root = new BorderPane();
        
        
        // Create filter bar
        VBox mainView = new VBox(10);
        mainView.setPadding(new Insets(10, 10, 10, 10));
        //filterBar.prefHeight(30);
        
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
        //cbo.setOnAction(new ComboBoxListener1(cbo));
        
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
       
       
        
        //Scroll Pane for display property information
        ScrollPane scrollInfo = new ScrollPane();
        //scrollInfo.fitToWidthProperty().set(true);
        //scrollInfo.fitToHeightProperty().set(true);
        
        //GridPane propertyInfo = new GridPane();
        Image image = new Image("/view/images/A01.jpeg");
        ImageView iv1 = new ImageView();
        iv1.setImage(image);
        
        iv1.setFitWidth(240);
        iv1.setFitHeight(100);
        
        //iv2.setPreserveRatio(true);
        //iv1.setSmooth(true);
        //iv1.setCache(true);
        
        Button button2 = new Button("My Button");
        button2.setPrefSize(240, 200);
        
        Button button3 = new Button("My Button");
        button3.setPrefSize(240, 200);
        
        scrollInfo.setContent(mainView);
        //scrollInfo.setContent(propertyInfo);
        
        
        // Always show vertical scroll bar
        scrollInfo.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        
        // Horizontal scroll bar is only displayed when needed
        scrollInfo.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        
        GridPane listView = new GridPane();
        listView.setGridLinesVisible(true);
        
        listView.setPadding(new Insets(20));
        listView.setHgap(10);
        listView.setVgap(10);
        ColumnConstraints column = new ColumnConstraints(240);
        //ColumnConstraints column2 = new ColumnConstraints(240);
        //ColumnConstraints column3 = new ColumnConstraints(240);
        listView.getColumnConstraints().addAll(column, column, column);
        
        
        listView.add(iv1, 0, 0);
        
        listView.add(button2, 0, 1);
        listView.add(button3, 0, 2);
        
        mainView.getChildren().addAll(filterBar, listView);
        
        
    
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
    public static void main(String[] args) {
        launch(args);
    }
    
    
}
