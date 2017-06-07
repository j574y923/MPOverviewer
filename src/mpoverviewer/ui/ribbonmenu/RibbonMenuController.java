package mpoverviewer.ui.ribbonmenu;

import javafx.scene.layout.HBox;

/**
 *
 * @author J
 */
public class RibbonMenuController extends HBox {

    public RibbonMenuController() {
        super();
    }

    public void addContainer(RibbonMenuContainer container) {
        this.getChildren().add(container);
    }
}
