package mpoverviewer.composition.ui;

import mpoverviewer.global.Variables;
import mpoverviewer.image.ImageIndex;
import mpoverviewer.ui.ribbonmenu.RibbonMenuButton;
import mpoverviewer.ui.ribbonmenu.RibbonMenuContainer;

/**
 *
 * @author J
 */
public class RibbonMenuContainerInstr extends RibbonMenuContainer {

    private boolean filterFlag;
    private boolean[] instrFiltered;

    public RibbonMenuContainerInstr() {
        super("Instruments");

        filterFlag = true;
        instrFiltered = new boolean[ImageIndex.BOO_SMA.ordinal() - ImageIndex.MARIO_SMA.ordinal() + 1];

        RibbonMenuButtonMPO instrSelected = new RibbonMenuButtonMPO();
        instrSelected.setPrefHeight(32);
        instrSelected.setPrefWidth(32);
        instrSelected.setMinWidth(32);
        this.addButton(0, instrSelected);

        RibbonMenuButtonMPO filter = new RibbonMenuButtonMPO();
//        filter.setMaxHeight(32);
//        filter.setMaxWidth(32);
        filter.setGraphic(Variables.imageLoader.getImageView(ImageIndex.FILTER_ON));
        this.addButton(1, filter);

        for (int i = ImageIndex.MARIO_SMA.ordinal(); i <= ImageIndex.BOO_SMA.ordinal(); i++) {
            RibbonMenuButtonMPO rmb = new RibbonMenuButtonMPO();
            rmb.setGraphic(Variables.imageLoader.getImageView(ImageIndex.values()[i]));
            this.addButton(0, rmb);

            RibbonMenuButtonMPO rmbFilter = new RibbonMenuButtonMPO();
            rmbFilter.setPrefHeight(28);
            rmbFilter.setPrefWidth(28);
            this.addButton(1, rmbFilter);
            instrFiltered[i - ImageIndex.MARIO_SMA.ordinal()] = false;
        }
    }

}
