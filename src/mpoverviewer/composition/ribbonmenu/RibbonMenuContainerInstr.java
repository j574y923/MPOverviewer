package mpoverviewer.composition.ribbonmenu;

import mpoverviewer.global.Variables;
import mpoverviewer.image.ImageIndex;
import mpoverviewer.ui.ribbonmenu.RibbonMenuButton;
import mpoverviewer.ui.ribbonmenu.RibbonMenuContainer;

/**
 *
 * @author J
 */
public class RibbonMenuContainerInstr extends RibbonMenuContainer {

    public RibbonMenuContainerInstr() {
        super("Instruments");

        RibbonMenuButton instrSelected = new RibbonMenuButton();
        this.addButton(instrSelected, 0, 0);

        for (int i = ImageIndex.MARIO_SMA.ordinal(); i <= ImageIndex.BOO_SMA.ordinal(); i++) {
            RibbonMenuButtonMPO rmb = new RibbonMenuButtonMPO();
            rmb.setGraphic(Variables.imageLoader.getImageView(ImageIndex.values()[i]));
            this.addButton(rmb, i - ImageIndex.MARIO_SMA.ordinal() + 1, 0);
        }
    }

}
