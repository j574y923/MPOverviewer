package mpoverviewer.global;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import javafx.scene.control.Tab;
import javafx.stage.FileChooser;
import mpoverviewer.data_layer.serialize.SongToData;
import mpoverviewer.image_layer.tabcontent.CompositionPaneSP;
import mpoverviewer.ui_layer.tab.TabDraggable;
import mpoverviewer.ui_layer.tab.content.ContentControl;

/**
 * Class for easy reference to hotkey function definitions
 *
 * Ctrl+N:
 *
 * @see #newTab()
 *
 * Ctrl+O:
 * @see #openSong()
 *
 * Ctrl+W:
 * @see #closeTab()
 *
 * @author j574y923
 */
public class Functions {

    /**
     * Ctrl+N: New "Untitled" tab one tab to the right of the currently focused
     * tab
     */
    public static void newTab() {
        TabDraggable newTab = new TabDraggable("Untitled");
        int i = Variables.stageInFocus.getTabPane().getSelectionModel().getSelectedIndex();
        Variables.stageInFocus.getTabPane().getTabs().add(i + 1, newTab);
        Variables.stageInFocus.getTabPane().getSelectionModel().selectNext();

        ((TabDraggable) Variables.stageInFocus.getTabPane().getSelectionModel()
                .getSelectedItem()).setContentControl(new CompositionPaneSP(null));
        ((ContentControl) Variables.stageInFocus.getTabPane().getSelectionModel()
                .getSelectedItem().getContent()).constructedBehavior();
    }

    /**
     * Ctrl+O: File navigation window opens where the user can then select
     * several text files to plop into the program as new tabs, each tab with a
     * selected file's name and path.
     *
     */
    public static void openSong() {
        try {
            FileChooser f = new FileChooser();
            f.setInitialDirectory(Variables.userDir);
            FileChooser.ExtensionFilter ext
                    = new FileChooser.ExtensionFilter("SMP,MPC,AMS,midi (*.txt, *.mss, *.mid)",
                            "*.txt", "*.mss", "*.mid");
            f.getExtensionFilters().add(ext);
            List<File> files = f.showOpenMultipleDialog(null);
            if (files != null) {
                HashSet<String> paths = getTabPaths();
                for (File file : files) {
//                    System.out.println(file.toString());
                    if (!paths.contains(file.toString())) {
                        TabDraggable newTab = new TabDraggable(file.getName());
                        newTab.setPath(file.toString());
                        int i = Variables.stageInFocus.getTabPane().getSelectionModel().getSelectedIndex();
                        Variables.stageInFocus.getTabPane().getTabs().add(i + 1, newTab);
                        Variables.stageInFocus.getTabPane().getSelectionModel().selectNext();

                        ((TabDraggable) Variables.stageInFocus.getTabPane().getSelectionModel()
                                .getSelectedItem()).setContentControl(
                                        new CompositionPaneSP(new SongToData().getSongToData(file.toString())));
                        ((ContentControl) Variables.stageInFocus.getTabPane().getSelectionModel()
                                .getSelectedItem().getContent()).constructedBehavior();
                    }
                }
                Variables.userDir = new File(files.get(0).getParent());
            }
        } catch (Exception e) {
            System.out.println("Not a valid song file.");//Dialog.showDialog("Not a valid song file.");
        }
    }

    /**
     * Used only by the openSong() function. Take every TabDraggable object from
     * the focused stage's tabPane, take the object's path variable, and place
     * it in a hash table.
     *
     * @return hash table of TabDraggable paths inside the focused stage's
     * tabPane
     */
    private static HashSet<String> getTabPaths() {
        HashSet<String> paths = new HashSet<>();
        for (Tab tab : Variables.stageInFocus.getTabPane().getTabs()) {
            if (((TabDraggable) tab).getPath() != null) {
                paths.add(((TabDraggable) tab).getPath());
            }
        }
        return paths;
    }

    /**
     * Ctrl+W: Close current focused tab
     */
    public static void closeTab() {
        if (Variables.stageInFocus.getTabPane().getTabs().isEmpty()) {
            return;
        }

        TabDraggable tab = (TabDraggable) Variables.stageInFocus.getTabPane().getSelectionModel().getSelectedItem();
        tab.closeTab();
    }
}
