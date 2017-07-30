package mpoverviewer.image_layer.ribbonmenu;

import mpoverviewer.ui_layer.ribbonmenu.RibbonMenuController;

/**
 *
 * @author J
 */
public class RibbonMenuMPO extends RibbonMenuController {

    private RibbonMenuContainerInstr buttonLine;
    
    private RibbonMenuContainerSelection selectionToolbar;
    
    public RibbonMenuMPO() {
        super();
        buttonLine = new RibbonMenuContainerInstr(); 
        selectionToolbar = new RibbonMenuContainerSelection();
        this.addContainer(buttonLine);
        this.addContainer(selectionToolbar);
    }
    
    public RibbonMenuContainerInstr getButtonLine() {
        return buttonLine;
    }
    
    public RibbonMenuContainerSelection getSelectionToolbar() {
        return selectionToolbar;
    }
}
