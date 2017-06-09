package mpoverviewer.composition.ui;

import mpoverviewer.ui.ribbonmenu.RibbonMenuController;

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
