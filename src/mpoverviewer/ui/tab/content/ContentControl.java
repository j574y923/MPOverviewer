package mpoverviewer.ui.tab.content;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;

/**
 *
 * @author j574y923
 */
public interface ContentControl {

    void cleanUp();
    
    Node getNode();

    String getTitle();

    void keyControl(KeyCode key, boolean altDown, boolean ctrlDown, boolean shiftDown);

    /**
     * Function call whenever a new tab is selected with TabDraggable's
     * selectedProperty() listener. This should define functionality for that
     * tab that is selected. The functionality is ideally to normalize the
     * properties of that tab to be similar to the other tabs.
     */
    void sharedBehavior();

//    void keyControlAlt(KeyCode key);
//    
//    void keyControlCtrl(KeyCode key);
//    
//    void keyControlShift(KeyCode key);
}
