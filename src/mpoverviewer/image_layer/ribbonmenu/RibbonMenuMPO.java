package mpoverviewer.image_layer.ribbonmenu;

import mpoverviewer.ui_layer.ribbonmenu.RibbonMenuController;

/**
 *
 * @author J
 */
public class RibbonMenuMPO extends RibbonMenuController {

    public RibbonMenuMPO() {
        super();
        this.addContainer(new RibbonMenuContainerInstr());
    }
}
