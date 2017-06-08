package mpoverviewer.ui;

import java.util.logging.Level;
import java.util.logging.Logger;
import mpoverviewer.ui.tab.TabDraggable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import mpoverviewer.global.Flags;
import mpoverviewer.global.Variables;
import mpoverviewer.ui.menubar.MenuBarController;
import mpoverviewer.ui.ribbonmenu.RibbonMenuController;

/**
 * Stage that represents a window and its components. Every window is
 * represented by a StageController object. Inside every StageController object
 * lies a menuBar represented by MenuBarController object and a tabPane of
 * TabDraggable objects.
 *
 * @author j574y923
 */
public class StageController extends Stage {

    /**
     * Vertically lay out the whole ui with a VBox.
     */
    private final VBox vBox;
    private MenuBarController menuBar;
    private TabPane tabPane;

    /**
     * Used to locate a class that derives the RibbonMenuController class.
     */
    private static String rmcResource = null;

    /**
     * Turn this flag on to signal that a tab has just been deleted. This flag
     * will determine behavior of either closing the StageController object or
     * not when tabPane detect if the tabPane becomes empty (all of its
     * TabDraggable objects are removed).
     */
    public boolean postDeleteState = false;

    public StageController() {
        super();

        menuBar = new MenuBarController();

        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

        /**
         * Stage's tabPane has its tabs change. When that tabPane is empty: Hide
         * the stage only when the tab gets dragged to another stage window,
         * Closing should keep the stage shown.
         */
        tabPane.getTabs().addListener(new ListChangeListener<Tab>() {

            @Override
            public void onChanged(ListChangeListener.Change<? extends Tab> change) {
                if (tabPane.getTabs().isEmpty()) {
                    //HIDE ONLY WHEN THE TAB GETS DRAGGED TO ANOTHER WINDOW. CLOSING SHOULD KEEP THE STAGE SHOWN
                    if (!postDeleteState) {
                        hide();
                    }
                }
                postDeleteState = false;
            }
        });

        //REMOVE ARROW KEY SELECTION, USE CONTENT.JAVA AS A WRAPPER FOR COMPOSITIONPANE WITH A FUNCTION THAT HANDLES KEYEVENTS
        tabPane.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                Tab tab = tabPane.getSelectionModel().getSelectedItem();
                ((TabDraggable) tab).keyControl(event);

                switch (event.getCode()) {
                    case LEFT:
                    case RIGHT:
                    case UP:
                    case DOWN:
                        event.consume();
                    default:
                    //nothing

                }
            }
        });

        /**
         * Release mouse click on the close button. By default mouse press will
         * activate the close button but using this EventHandler will make sure
         * the closing functionality happens upon mouse release.
         */
        tabPane.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (Variables.stageInFocus.getTabPane().getTabs().isEmpty()) {
                    return;
                }
                TabDraggable tab = (TabDraggable) Variables.stageInFocus.getTabPane().getSelectionModel().getSelectedItem();
                if (tab.closable) {
                    Rectangle2D tabRect = tab.getAbsoluteRect(tab);
                    double x = t.getScreenX();
                    double y = t.getScreenY();
                    if (x > tabRect.getMaxX() + 30 - 18 && x < tabRect.getMaxX() + 30 - 8
                            && y > tabRect.getMaxY() + 33 - 16 && y < tabRect.getMaxY() + 33 - 6) {
                        tab.closeTab();

//                        postDeleteState = true;
                    } else {
                        tab.closable = false;
                    }
                }
            }
        });

        /**
         * Press mouse right button down and make the tab getting clicked on
         * non-closable. Don't want the right mouse button to activate the close
         * button when it should only be bringing down the right click menu.
         */
        tabPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (Variables.stageInFocus.getTabPane().getTabs().isEmpty()) {
                    return;
                }
                if (t.isSecondaryButtonDown()) {
                    TabDraggable tab = (TabDraggable) Variables.stageInFocus.getTabPane().getSelectionModel().getSelectedItem();
                    tab.closable = false;
                }
            }
        });

        /**
         * Listener detects when the stage is focused. Focus this stage and then
         * set the stageInFocus global variable to this StageControls object.
         */
        focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                focusThis();
            }
        });

        /**
         * Stage calls hide() or close() and exits the stage's window. Remove
         * tabs when this happens.
         */
        setOnHiding(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent t) {
                TabDraggable.tabPanes.remove(tabPane);
            }
        });

//        RibbonMenuController rmc = new RibbonMenuController();
//        RibbonMenuContainer rmca = new RibbonMenuContainer("test");
//        RibbonMenuButton rmb = new RibbonMenuButton();
//        rmb.setGraphic(Variables.imageLoader.getImageView(ImageIndex.BOAT_SMA));
//        RibbonMenuButton rmb1 = new RibbonMenuButton();
//        rmb1.setGraphic(Variables.imageLoader.getImageView(ImageIndex.MUSHROOM_SMA));
////        rmb.setText("TEST");
//        rmca.addButton(rmb, 0, 0);
//        rmca.addButton(rmb1, 0, 1);
//        rmc.addContainer(rmca);
//        RibbonMenuContainer rmca2 = new RibbonMenuContainer("test2");
//        RibbonMenuButton rmb2 = new RibbonMenuButton();
//        rmb2.setGraphic(Variables.imageLoader.getImageView(ImageIndex.PIG));
////        rmb.setText("TEST");
//        rmca2.addButton(rmb2, 0, 0);
//        rmc.addContainer(rmca2);
////        setRibbonMenu(new RMC());
        vBox = new VBox();
        vBox.getChildren().addAll(menuBar, tabPane);
//        vBox.getChildren().add(1, rmc);
        Scene scene = new Scene(vBox, 550, 350);
        setScene(scene);

        if (Flags.ribbonMenuRender && rmcResource != null) {
            recreateRibbonMenu();
        }
    }

    public MenuBarController getMenuBar() {
        return menuBar;
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    private void focusThis() {
        Variables.stageInFocus = this;
    }

    public void setRibbonMenu(RibbonMenuController rmc) {
        if (!Flags.ribbonMenuRender) {
            return;
        }
        if (rmcResource == null) {
            rmcResource = rmc.getClass().toString();//.getComponentType();
            rmcResource = rmcResource.substring(6);//remove "class "        
            System.out.println(rmcResource);
        }

        //Add between menubar and tabpane
        vBox.getChildren().add(1, rmc);
    }

    /**
     * Use reflection to get any object derived from the base
     * RibbonMenuController class creating a new object of that derived type.
     * Set the stage's ribbon menu as that derived ribbon menu object.
     */
    private void recreateRibbonMenu() {

        try {
            Object rmc = Class.forName(rmcResource).newInstance();
//            ((RibbonMenuController) b).test();
            setRibbonMenu((RibbonMenuController) rmc);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(StageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    class test<T>{
//        T test(){
////            T a = a.getClass().newInstance();
//          return null;  
//        }
//    }
}

//class RMC extends RibbonMenuController {
//
//    RMC() {
//
//    }
//}
