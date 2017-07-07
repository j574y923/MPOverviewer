package mpoverviewer.image_layer.ribbonmenu;

import mpoverviewer.ui_layer.ribbonmenu.RibbonMenuController;

/**
 *
 * @author J
 */
public class RibbonMenuMPO extends RibbonMenuController {

    private RibbonMenuContainerInstr buttonLine;
    
    public RibbonMenuMPO() {
        super();
        buttonLine = new RibbonMenuContainerInstr(); 
        this.addContainer(buttonLine);
    }
    
    public RibbonMenuContainerInstr getButtonLine() {
        return buttonLine;
    }
}
