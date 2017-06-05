/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpoverviewer.ui.menubar;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
//import mpoverviewer.MPOverviewer;
import mpoverviewer.global.Functions;
import mpoverviewer.global.Variables;

/**
 * Menu bar attached to every window with custom design and functionality made
 * for this program.
 *
 * @author j574y923
 */
public class MenuBarController extends MenuBar {

//    public MenuBar menuBar;
    public MenuBarController() {
        //Menu Bar
//        menuBar = new MenuBar();

        //Menu 1
//        Menu menuFile = new Menu("File");
//        Menu menuEdit = new Menu("Edit");
//        Menu menuSelection = new Menu("Selection");
//        Menu menuFind = new Menu("Find");
//        Menu menuView = new Menu("View");
//        Menu menuGoto = new Menu("Goto");
//        Menu menuTools = new Menu("Tools");
//        Menu menuArrangement = new Menu("Arrangement");
//        Menu menuPreferences = new Menu("Preferences");
//        Menu menuHelp = new Menu("Help");
//
//        //Menu Items
//        MenuItem itmNewS = new MenuItem("New Song");
//        MenuItem itmOpenS = new MenuItem("Open Song");
//        MenuItem itmSaveS = new MenuItem("Save Song");
//        MenuItem itmCloseS = new MenuItem("Close Song");
//        MenuItem itmExit = new MenuItem("Exit");
//
//        itmOpenS.setOnAction(new EventHandler<ActionEvent>() {
//            public void handle(ActionEvent event) {
//                openSong();
//            }
//        });
//        itmExit.setOnAction(new EventHandler<ActionEvent>() {
//            public void handle(ActionEvent event) {
//                Platform.exit();
//            }
//        });
//
//        menuFile.getItems().addAll(itmNewS, itmOpenS, itmSaveS, itmCloseS,
//                itmExit);
//
//        menuBar.getMenus().addAll(menuFile, menuEdit, menuSelection, menuFind,
//                menuView, menuGoto, menuTools, menuArrangement, menuPreferences,
//                menuHelp);
        Menu menuFile = new Menu("File");
        Menu menuEdit = new Menu("Edit");
        Menu menuSelection = new Menu("Selection");
        Menu menuFind = new Menu("Find");
        Menu menuView = new Menu("View");
        Menu menuGoto = new Menu("Goto");
        Menu menuTools = new Menu("Tools");
        Menu menuArrangement = new Menu("Arrangement");
        Menu menuPreferences = new Menu("Preferences");
        Menu menuHelp = new Menu("Help");

        MenuItem itmNewS = new MenuItem("New Song");
        MenuItem itmOpenS = new MenuItem("Open Song");
        MenuItem itmSaveS = new MenuItem("Save Song");
        MenuItem itmCloseS = new MenuItem("Close Song");
        MenuItem itmExit = new MenuItem("Exit");

        
        itmNewS.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Functions.newTab();
            }
        });
        itmNewS.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        
        itmOpenS.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Functions.openSong();
            }
        });
        itmOpenS.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        
        itmCloseS.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Functions.closeTab();
            }
        });
        itmCloseS.setAccelerator(new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN));
        
        itmExit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Platform.exit();
            }
        });

        menuFile.getItems().addAll(itmNewS, itmOpenS, itmSaveS, itmCloseS,
                itmExit);

        getMenus().addAll(menuFile, menuEdit, menuSelection, menuFind,
                menuView, menuGoto, menuTools, menuArrangement, menuPreferences,
                menuHelp);
    }
}
