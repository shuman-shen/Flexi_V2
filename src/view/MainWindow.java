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
import javafx.scene.control.Alert.AlertType;
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

public class MainWindow {

    private MainWindowControl mainControl;
    private FlexiRentSystem flexiModel;
    private HBox filterBar;
    private VBox mainView;
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

    private ComboBox<String> suburbBox;
    private ArrayList<Property> tmp;

    private ArrayList<VBox> list = new ArrayList<VBox>();

    final private String pathN = "/view/images/No_Image_Available.png";
    final private String folder = "/view/images/";

    public ImageView getImageView() {
        return imgV;
    }

    public String getPropertyID() {
        return pID.getText();
    }

    public Text getAddress() {
        return address;
    }

    public Label getDesc() {
        return desc;
    }

    public VBox getVBox() {
        return property;
    }

    public ArrayList<VBox> getArrayList() {
        return list;
    }

    public MainWindow(MainWindowControl m) {
        mainControl = m;
        try {
            setMainList(mainControl.getWholeList());
            createView();
        } catch (Exception e) {
            Alert alert1 = new Alert(AlertType.ERROR);
            alert1.setHeaderText("Error.");
            alert1.showAndWait();
        }
    }

    public Parent asParent() {
        return root;
    }

    public void setImageView(String fileName) {
        String p = "";
        p = folder + fileName;
        // String t = "/src/view/images/A02.jpeg";
        // System.out.print("image path" + p);

        try {
            img = new Image(p);
            imgV = new ImageView();
            imgV.setImage(img);
            imgV.setFitWidth(240);
            imgV.setFitHeight(160);
            // return imgV;
        } catch (IllegalArgumentException i) {
            try {
                Image noImg = new Image(pathN);
                imgV = new ImageView();
                imgV.setImage(noImg);
                imgV.setFitWidth(240);
                imgV.setFitHeight(160);
            } catch (IllegalArgumentException e) {
                System.out.println("Failed to display the default image");
            }
        }
    }

    public void setPropertyID(String ID) {
        pID = new Button(ID);

        String n = pID.getText();
        pID.setOnAction(new DisplayDetailListener(pID.getText(), mainControl));
        pID.setFont(Font.font("Calibri", FontWeight.BOLD, 16));
    }

    public void setAddress(int streetNum, String streetName, String suburb) {
        address = new Text(streetNum + " " + streetName + ", " + suburb);
        address.setFont(Font.font("Calibri", FontWeight.BOLD, 12));
    }

    public void setDesc(String d) {
        desc = new Label(d);
        desc.setWrapText(true);
    }

    public void setVBox(ImageView imgV, Text ID, Text address, Text desc) {
        property = new VBox(10);

        property.getChildren().addAll(imgV, ID, address, desc);
    }

    public void setMainList(ArrayList<Property> list) {

        int i = 0;
        int num = 1;

        // Grid pane for display property list
        listView = new GridPane();
        // listView.setGridLinesVisible(true);
        listView.setPadding(new Insets(20));
        listView.setHgap(10);
        listView.setVgap(10);
        ColumnConstraints column = new ColumnConstraints(240);
        RowConstraints row = new RowConstraints(300);
        listView.getColumnConstraints().addAll(column, column, column);

        String d = "";
        for (Property p : list) {
            String propertyID = p.getPropertyID();
            int streetNum = p.getStreetNo();
            String streetName = p.getStreetName();
            String suburb = p.getSuburb();
            String fName = p.getImage();
            setImageView(fName);
            setPropertyID(propertyID);
            setAddress(streetNum, streetName, suburb);
            setDesc(d);

            property = new VBox(10);
            property.getChildren().addAll(imgV, pID, address, desc);

            // Determine item position inside grid
            if (num % 3 == 1) {
                listView.getRowConstraints().add(row);
                listView.add(property, 0, i);
                num++;
            } else if (num % 3 == 2) {
                listView.add(property, 1, i);
                num++;
            } else {
                listView.add(property, 2, i);
                i++;
                num++;
            }
        }
    }

    public void createView() {
        // root pane
        root = new BorderPane();

        // Create main view
        mainView = new VBox(10);
        mainView.setPadding(new Insets(10, 10, 10, 10));

        // Scroll bar for the main view,
        scrollInfo = new ScrollPane();
        scrollInfo.setContent(mainView);
        scrollInfo.setVbarPolicy(ScrollBarPolicy.ALWAYS); // Always show vertical scroll bar
        scrollInfo.setHbarPolicy(ScrollBarPolicy.AS_NEEDED); // Horizontal scroll bar is only displayed when needed

        createFilterBar();

        mainView.getChildren().add(filterBar);
        mainView.getChildren().add(listView);

        // Create menu bar
        menuBar = new MenuBar();
        createMenu();

        root.setTop(menuBar);
        // pane.setBottom(filterBar);
        root.setCenter(scrollInfo);
    }

    public void createMenu() {

        Menu fileMenu = new Menu("Property");
        Menu dataMenu = new Menu("Data");
        Menu sysMenu = new Menu("System");

        MenuItem newMenuItem = new MenuItem("Add Property");
        newMenuItem.setOnAction(new AddPropertyListener(mainControl));
        MenuItem rentMenuItem = new MenuItem("Rent Property");

        MenuItem homeMenuItem = new MenuItem("Home Screen");
        homeMenuItem.setOnAction(event -> {

            try {
                setMainList(mainControl.getWholeList());
                mainView.getChildren().set(1, listView);
                createFilterBar();
                filterBar.getChildren().set(4, suburbBox);
                root.setCenter(scrollInfo);
            }

            catch (Exception e) {
                Alert alert2 = new Alert(AlertType.INFORMATION);
                alert2.setHeaderText("Error.");
                alert2.showAndWait();

            }

        });
        MenuItem quitMenuItem = new MenuItem("Quit");
        quitMenuItem.setOnAction(e -> Platform.exit());

        MenuItem importMenuItem = new MenuItem("Import Data");
        importMenuItem.setOnAction(new ImportListener(mainControl));
        MenuItem exportMenuItem = new MenuItem("Export Data");
        exportMenuItem.setOnAction(new ExportListener(mainControl));

        fileMenu.getItems().add(newMenuItem);
        dataMenu.getItems().addAll(importMenuItem, exportMenuItem);
        sysMenu.getItems().addAll(homeMenuItem, quitMenuItem);
        menuBar.getMenus().addAll(fileMenu, dataMenu, sysMenu);
    }

    public void createFilterBar() {
        // Create filter bar
        filterBar = new HBox(10);
        mainView.setPadding(new Insets(10, 10, 10, 10));

        Label filterLabel = new Label("Filters: ");
        filterLabel.setPrefSize(56, 30);
        filterLabel.setFont(new Font(14));
        filterLabel.setAlignment(Pos.CENTER);
        // filterLabel.setStyle("-fx-border-color: black");

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("All Property Types", "Apartment", "Premium Suite");
        typeBox.setValue("All Property Types");

        ComboBox<String> roomBox = new ComboBox<>();
        roomBox.getItems().addAll("All Bedrooms Types", "3 Bedrooms", "2 Bedrooms", "1 Bedroom");
        roomBox.setValue("All Bedrooms Types");

        ComboBox<String> availBox = new ComboBox<>();
        availBox.getItems().addAll("All Conditions", "Available", "Rent", "Under Maintenance");
        availBox.setValue("All Conditions");

        createSuburbBox();

        Button filterBtn = new Button("Filter");
        filterBtn.setOnAction(event -> {
            try {
                boolean toFilter = mainControl.setFilter(typeBox.getValue(), roomBox.getValue(), availBox.getValue(),
                        suburbBox.getValue());

                if (toFilter == false) {
                    setMainList(mainControl.getWholeList());
                    mainView.getChildren().set(1, listView);
                    createFilterBar();
                    filterBar.getChildren().set(4, suburbBox);
                } else {
                    setMainList(mainControl.getFilteredList());
                    mainView.getChildren().set(1, listView);
                    System.out.println("Filter set");
                }
            } catch (Exception e) {
                Alert alert2 = new Alert(AlertType.INFORMATION);
                alert2.setHeaderText("Error.");
                alert2.showAndWait();

            }
        });
        filterBtn.setPrefSize(70, 30);
        filterBar.getChildren().addAll(filterLabel, typeBox, roomBox, availBox, suburbBox, filterBtn);

    }

    public void createSuburbBox() {
        // TODO: generate suburb list from database
        suburbBox = new ComboBox<>();
        suburbBox.getItems().add("All Suburbs");

        // TODO EXCEPTION FOR NO VALUE IN LIST??
        for (String k : mainControl.getAllSuburb()) {
            suburbBox.getItems().add(k);
        }

        suburbBox.setValue("All Suburbs");
    }

    public void createSearchDialog() {

    }

    /**
     * The main method is only needed for the IDE with limited JavaFX support. Not
     * needed for running from the command line.
     */
    // public static void main(String[] args) {
    // launch(args);
    // }

}
