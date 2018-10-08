package view;

import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MenuBarView {
    
    public void creatMenu(MenuBar menuBar) {
        
        
        //MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("Property");
        Menu dataMenu = new Menu("Data");
        Menu sysMenu = new Menu("System");
        
        MenuItem newMenuItem = new MenuItem("Add Property");
        newMenuItem.setOnAction(new AddPropertyListener(mainControl));
        MenuItem rentMenuItem = new MenuItem("Rent Property");
        rentMenuItem.setOnAction(new SearchListener());
        
        
        MenuItem homeMenuItem = new MenuItem("Home Screen");
        homeMenuItem.setOnAction(event -> {
            setMainList(mainControl.getWholeList());
            mainView.getChildren().set(1, listView);
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
}
