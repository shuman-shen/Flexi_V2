package view;

import java.time.LocalDate;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Optional;


import controller.MainWindowControl;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
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
import javafx.util.Pair;
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
    private DatePicker datePicker;
    private ComboBox<Integer> lengthBox;
    private ComboBox<Integer> lengthBoxP;
    private VBox dBox;
    
    private ArrayList<Property> tmp;
    private Property curItm;
    private int status;
    private final String path = "/view/images/";
    final private String pathN = "/view/images/No_Image_Available.png";
    private LocalDate rentDate;
    private LocalDate returnDate;
    private int duration;
    private int dayOfWeek;
    private String customerID;
    private LocalDate operateDate;
   
    
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
            menu = new MenuBar();
            createMenu();
            
            
            scroll = new ScrollPane();               
            scroll.setContent(detailView);       
            scroll.setVbarPolicy(ScrollBarPolicy.ALWAYS); //Always show vertical scroll bar
            scroll.setHbarPolicy(ScrollBarPolicy.AS_NEEDED); // Horizontal scroll bar is only displayed when needed
            
            root.setTop(menu);
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
        status = curItm.getStatus();
        
        String a = curItm.getStreetNo() + " " + curItm.getStreetName() + ", " + curItm.getSuburb();
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
        
       
        
        rentBtn.setOnAction(rent ->{
            
            try {
            
            checkStatus();
            Dialog<Data> dialog = new Dialog<>();
            dialog.setTitle("Rent Property");
            dialog.setHeaderText(null);
            //DialogPane dialogPane = dialog.getDialogPane();
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            Label customer = new Label("Customer ID");
            TextField cId = new TextField("C_001");
            
            Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
            cId.textProperty().addListener((observable, oldValue, newValue) -> {
                okButton.setDisable(newValue.trim().isEmpty());
            });
//            datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
//                okButton.setDisable(oldValue.toString().trim().isEmpty()||newValue.toString().trim().isEmpty());
//            });
            
            
            Label date = new Label("Check-in Date");
            datePicker = new DatePicker(LocalDate.now());
//            datePicker.setDayCellFactory(picker -> new DateCell() {
//                @Override
//                public void updateItem(LocalDate date, boolean empty) {
//                    super.updateItem(date, empty);
//                    setDisable(empty || date.compareTo(LocalDate.now())<0);
//                }
//            });
            datePicker.setEditable(false);
            Label length = new Label("Staying Length");
            lengthBox = new ComboBox<>();
            
            int c = 2;
            int l = 29;
            dayOfWeek = datePicker.getValue().getDayOfWeek().getValue();
            
            if(curItm.getPropertyID().startsWith("S")) {
                c = 1;
                l = 11;
            }
            else if (dayOfWeek==5 || dayOfWeek == 6 ) {
                c =3;
            }
                for (int i = c; i<l; i++) {
                    lengthBox.getItems().add(i);
                } 
                lengthBox.setValue(c);
            
            
            
            
            //lengthBoxP = new ComboBox<>();
            
            
            dBox = new VBox(10);
            dBox.getChildren().setAll(customer, cId, date, datePicker, length, lengthBox);
            
            datePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {            
          
                @Override
                public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
                        LocalDate newValue) {
                    // TODO Auto-generated method stub
                    
                    //dayOfWeek = datePicker.getValue().getDayOfWeek().getValue();
                    int j = 2;
                    
                    if(curItm.getPropertyID().startsWith("A")) {
                    if(dayOfWeek==5 && dayOfWeek ==6 ) {
                        j = 3;
                    }
                    else {
                        j = 2;
                    }
                        
                        lengthBox = new ComboBox<>();
                        for (int i = j; i<29; i++) {
                            lengthBox.getItems().add(i);
                        } 
                        lengthBox.setValue(j);
                        dBox.getChildren().set(5, lengthBox);
                    } }                                                              
                
                                                   
            });
            
     
            dialog.getDialogPane().setContent(dBox);
            //Platform.runLater(textField::requestFocus);
            
            dialog.setResultConverter((ButtonType button) -> {
                if (button == ButtonType.OK) {                  
                    return new Data(cId.getText(), datePicker.getValue(), lengthBox.getValue());               

                  }                    
 
                  
                return null;
            });
            
            Optional<Data> request = dialog.showAndWait();
            
            request.ifPresent((Data data) -> {
                    customerID = data.custID;
                    rentDate = data.checkInDate;
                    this.duration = data.duration;
                    
                    System.out.println("Checkin dATE" + rentDate) ;
                    
                    try {
                        mainControl.rent(pID, customerID, rentDate, duration);
                        
                        
                        status = 1;
                        setButtons();
                        //curItm.setToRent();
                        final String msg = "been rented";
                        doneAlert(msg, pID);
                        
                        
                    }
                    catch(Exception sql) {
                        throw sql;
                    }
                    
            });
            
            }
            catch(Exception rentEx) {
                errorAlert();
            }
            
           
                   
            
        });
        
        
        
        
        
        
        returnBtn = new Button("Return Property");
        btnGrid.add(returnBtn,1,0);
        returnBtn.setOnAction(returnEvent ->{
                
               //try {
//                int s = 0;
//                mainControl.setFilter(pID);
//                        
//                curItm = mainControl.getFilteredList().get(0);
//                s = curItm.getStatus();
//                        
//                if (status != s) {
//                    status = s;
//                    setButtons();
//                    throw new Exception();
//                }
                   
                //checkStatus();
                
                Dialog<LocalDate> dialog = new Dialog<>();
                dialog.setTitle("Return Property");
                dialog.setHeaderText(null);
                //DialogPane dialogPane = dialog.getDialogPane();
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
           
                
               //validation for empty date
                
                Label date = new Label("Return Date");
                datePicker = new DatePicker(LocalDate.now());
                datePicker.setEditable(false);
                
                VBox rBox = new VBox(10);
                rBox.getChildren().setAll(date, datePicker);
                
                
                dialog.getDialogPane().setContent(rBox);
                //Platform.runLater(textField::requestFocus);               
                //System.out.println("here ");
                
                Optional<LocalDate> request2 = dialog.showAndWait();
                System.out.println("here1");
                
                dialog.setResultConverter((ButtonType button) -> {
                    if (button == ButtonType.OK) {                  
                        System.out.println("here2");
                        returnDate = datePicker.getValue();
                        System.out.println("here2");
                        return returnDate;               

                      }                    
     
                      
                    else return null;
                });
                
                if(request2.isPresent()){
                        System.out.println("there");
                        returnDate = datePicker.getValue();
                        System.out.println("return " + returnDate);
                        System.out.println(curItm.getRecords().size());
                        
                        
                        //try {
                            mainControl.returnProperty(pID, returnDate);
                            status = 2;
                            setButtons();
                            final String msg = "been returned";
                            doneAlert(msg, pID);
                            
                            
//                        }
//                        catch(Exception sql) {
//                            System.out.println("error here");
//                            //throw sql;
//                        }
                        
                }
//                
//                }
//                
//                catch(Exception returnEx) {
//                    //errorAlert();
//                    System.out.println(returnEx.getMessage());
//                }
            
        });
        
        
        
        startBtn = new Button("Start Maintenance");
        btnGrid.add(startBtn,0,1);
        
        startBtn.setOnAction(start ->{
            
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Please confirm...");
            alert.setHeaderText(null);
            alert.setContentText("Do you want to start maitenance?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                status = 3;
                setButtons();
                final String msg = "been under maitenance since today";
                doneAlert(msg, pID);
            } 
            
            
        });
        
        
        endBtn = new Button("Finish Maintenance");
        btnGrid.add(endBtn,1,1);
       
        
startBtn.setOnAction(start ->{
            
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Please confirm...");
            alert.setHeaderText(null);
            alert.setContentText("Do you want to complete maitenance?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                status = 3;
                setButtons();
                final String msg = "completed maintenance";
                doneAlert(msg, pID);
            } 
            
            
        });
        
        
        Text desc = new Text(curItm.getDescription());
        desc.setWrappingWidth(400);
        
        



        setButtons();
        
        
        
        itemView.getChildren().addAll(imgV, propertyID, address, btnGrid, desc);
        detailView.getChildren().add(itemView);
       
        //System.out.println("That step");
        
        
        
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
//        homeMenuItem.setOnAction(event -> {
//            setMainList(mainControl.getWholeList());
//            mainView.getChildren().set(1, listView);
//            root.setCenter(scrollInfo);
//        });
        MenuItem quitMenuItem = new MenuItem("Quit");
        quitMenuItem.setOnAction(e -> Platform.exit());
        
        MenuItem importMenuItem = new MenuItem("Import Data");
        //exitMenuItem.setOnAction(new MenuItemListener(exitMenuItem));
        
        MenuItem exportMenuItem = new MenuItem("Export Data");       
        
        fileMenu.getItems().addAll(newMenuItem, rentMenuItem);
        dataMenu.getItems().addAll(importMenuItem, exportMenuItem);
        sysMenu.getItems().addAll(homeMenuItem, quitMenuItem);
        menu.getMenus().addAll(fileMenu, dataMenu, sysMenu);
        
        //return menuBar;     
          
          
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
    
    private class Data {

        String custID;
        LocalDate checkInDate;
        Integer duration;

        public Data(String custID, LocalDate checkInDate, Integer duration) {
            this.custID = custID;
            this.checkInDate = checkInDate;
            this.duration = duration;
        }
    }
    
    public void doneAlert(String msg, String pID) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText("Done.");
        alert.setContentText("Property " + pID + " has successfully " + msg +".");
        alert.showAndWait();
        
    }
    public void errorAlert() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Opps, status doesn't match database. \n"
                + "Information will be refreshed. \n"
                + "Please try again.");
        alert.showAndWait();
        
    }
    
    public void setButtons() {
        if(status == 1) {            
            rentBtn.setDisable(true);
            returnBtn.setDisable(false);
            startBtn.setDisable(true);
            endBtn.setDisable(true);
        }
        else if (status == 2) {
            rentBtn.setDisable(false);
            returnBtn.setDisable(true);
            startBtn.setDisable(false);
            endBtn.setDisable(true);
        }
        else {
            rentBtn.setDisable(true);
            returnBtn.setDisable(true);
            startBtn.setDisable(true);
            endBtn.setDisable(false);
        }
    }
    
    public void checkStatus() throws Exception{
        int s = 0;
        mainControl.setFilter(pID);
                
        curItm = mainControl.getFilteredList().get(0);
        s = curItm.getStatus();
        System.out.print("current status in database: " + s);
        System.out.print("current status in object: " + status);
                
        if (status != s) {
            status = s;
            setButtons();
            System.out.print("Not match");
            throw new Exception();
        }
    }
    
    
    

}
