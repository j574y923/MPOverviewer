package mpoverviewer.ui.tab;

import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import mpoverviewer.global.Functions;
import mpoverviewer.global.Variables;

/**
 * Context menu (or right click menu) that pops down when right clicking a tab's
 * label. Every TabDraggable object will have a reference to this menu.
 *
 * @author j574y923
 */
public class ContextMenuTab extends ContextMenu {

    /**
     * tabRef is a reference to the TabDraggable that has the mouse cursor
     * hovering over its label. This is needed to refer to the correct tab when
     * choosing which tabs to close with the close tab functions in this context
     * menu.
     */
    private TabDraggable tabRef;

    private MenuItem itemClose;
    private MenuItem itemCloseOthers;
    private MenuItem itemCloseRight;
    private MenuItem itemNew;
    private MenuItem itemOpen;

    ContextMenuTab() {
        super();

        defClose();
        defCloseOthers();
        defCloseRight();
        defNew();
        defOpen();

        getItems().addAll(itemClose, itemCloseOthers, itemCloseRight,
                new SeparatorMenuItem(), itemNew, itemOpen);
    }

    /**
     * tabRef should be set whenever the mouse hovers over a new tab's label.
     *
     * @param tab that the mouse is hovering over
     */
    public void setTabRef(TabDraggable tab) {
        tabRef = tab;
    }

    public TabDraggable getTabRef() {
        return tabRef;
    }

    private void defClose() {
        itemClose = new MenuItem("Close");
        itemClose.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                tabRef.closeTab();
            }
        });
    }

    private void defCloseOthers() {
        itemCloseOthers = new MenuItem("Close Other Tabs");
        itemCloseOthers.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                int index = index();
                List<Tab> tabs = Variables.stageInFocus.getTabPane().getTabs();
                closeRight(index, tabs);
                for (int i = index - 1; i >= 0; i--) {
                    ((TabDraggable) tabs.get(i)).closeTab();
                }
            }
        });
    }

    private void defCloseRight() {
        itemCloseRight = new MenuItem("Close Tabs to the Right");
        itemCloseRight.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                int index = index();
                List<Tab> tabs = Variables.stageInFocus.getTabPane().getTabs();
                closeRight(index, tabs);
            }
        });
    }

    private void defNew() {
        itemNew = new MenuItem("New File");
        itemNew.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        itemNew.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Functions.newTab();
            }
        });
    }

    private void defOpen() {
        itemOpen = new MenuItem("Open File");
        itemOpen.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        itemOpen.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Functions.openSong();
            }
        });
    }

    private int index() {
        int i = 0;
        for (Tab tab : Variables.stageInFocus.getTabPane().getTabs()) {
            if (tab.equals(tabRef)) {
                break;
            }
            i++;
        }
        return i;
    }

    private void closeRight(int index, List<Tab> tabs) {
//        List<Tab> tabs = MPOverviewer.stageInFocus.getTabPane().getTabs();
        int end = tabs.size() - 1;
        for (int i = index; i < end; i++) {
            ((TabDraggable) tabs.get(index + 1)).closeTab();
        }
    }
}
