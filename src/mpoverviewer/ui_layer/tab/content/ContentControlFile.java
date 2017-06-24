package mpoverviewer.ui_layer.tab.content;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;

/**
 * A possible implementation of TabDraggable content...
 *
 * @author j574y923
 */
public abstract class ContentControlFile<T extends Node> extends Node {

    private String title;
    
    private String path;
    
    private final Node content;

    ContentControlFile(String title, Node content) throws NoSuchMethodException{
//        T t = null;
//        t.getClass().getMethod("ScrollPane", null);
        this.title = title;
        this.content = content;
    }
    
    public Node getNode() {
        return content;
    }

    public void keyControl(KeyCode key, boolean altDown, boolean ctrlDown, boolean shiftDown) {
        throw new UnsupportedOperationException("ContentControlFile.keyControl() Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

//    void keyControlAlt(KeyCode key);
//    
//    void keyControlCtrl(KeyCode key);
//    
//    void keyControlShift(KeyCode key);
}
