package mpoverviewer.image_layer.ribbonmenu;

import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import mpoverviewer.data_layer.data.Note;
import mpoverviewer.data_layer.dataclipboard.DataClipboard;
import mpoverviewer.global.Constants;
import mpoverviewer.global.Variables;
import mpoverviewer.image_layer.ImageIndex;
import mpoverviewer.ui_layer.ribbonmenu.RibbonMenuButton;
import mpoverviewer.ui_layer.ribbonmenu.RibbonMenuContainer;

/**
 *
 * @author J
 */
public class RibbonMenuContainerInstr extends RibbonMenuContainer {

    private static final String DESCRIPTION = "Instruments:\n"
            + "Click one of the instruments to select it and use for note placement.\n"
            + "Filter:\n"
            + "Hover over one of the instruments and press 'F' to toggle it in the filter\n"
            + "or click one of the slots on the filter row to toggle it in the filter.\n"
            + "Click the filter icon to toggle the filter.";

    private static final Tooltip TT_DESCRIPTION = new Tooltip(DESCRIPTION);

    private int instrSelected;
    private boolean filterFlag;
    private boolean[] instrFiltered;
    private final List<RibbonMenuButtonMPO> instrFilteredButtons;
    
    private RibbonMenuButton instrSelectedButton;

    //remove blue border when clicking on the button
//    private static final String STYLE_BORDER_OFF = "-fx-focus-color: transparent;-fx-padding:1;";
    public RibbonMenuContainerInstr() {
        super("Instruments");

        instrSelected = -1;
        filterFlag = true;
        instrFiltered = new boolean[Constants.INSTRUMENTS];//ImageIndex.BOO_SMA.ordinal() - ImageIndex.MARIO_SMA.ordinal() + 1];
        instrFilteredButtons = new ArrayList<>();

        instrSelectedButton = new RibbonMenuButton();
        instrSelectedButton.setPrefHeight(34);
        instrSelectedButton.setPrefWidth(34);
        instrSelectedButton.setMinWidth(34);
        instrSelectedButton.setMinHeight(34);
        instrSelectedButton.setFocusTraversable(false);
        instrSelectedButton.setTooltip(TT_DESCRIPTION);
        this.addButton(0, instrSelectedButton);

        RibbonMenuButtonMPO filter = new RibbonMenuButtonMPO();
//        filter.setMaxHeight(32);
//        filter.setMaxWidth(32);
        filter.setGraphic(Variables.imageLoader.getImageView(ImageIndex.FILTER_ON));
//        filter.setFocusTraversable(false);
        filter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!filterFlag) {
                    filter.setGraphic(Variables.imageLoader.getImageView(ImageIndex.FILTER_ON));
                    filterFlag = true;
                    sil_to_sma();
                    
                    DataClipboard.setInstrFiltered(instrFiltered);
                } else {
                    filter.setGraphic(Variables.imageLoader.getImageView(ImageIndex.FILTER_OFF));
                    filterFlag = false;
                    sma_to_sil();
                    
                    DataClipboard.setInstrFiltered(null);
                }
            }
        });
        filter.setTooltip(TT_DESCRIPTION);
        this.addButton(1, filter);

        for (int i = ImageIndex.MARIO_SMA.ordinal(); i <= ImageIndex.BOO_SMA.ordinal(); i++) {
            RibbonMenuButtonMPO rmb = new RibbonMenuButtonMPO();
            rmb.setGraphic(Variables.imageLoader.getImageView(ImageIndex.values()[i]));
            //using i inside eventhandler requires a final value
            final int i2 = i;
            rmb.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    instrSelectedButton.setGraphic(Variables.imageLoader.getImageView(
                            ImageIndex.values()[i2 - ImageIndex.MARIO_SMA.ordinal() + ImageIndex.MARIO.ordinal()]));
                    instrSelected = i2 - ImageIndex.MARIO_SMA.ordinal();
                    System.out.println(Note.Instrument.values()[instrSelected]);
                }
            });
            this.addButton(0, rmb);

            RibbonMenuButtonMPO rmbFilter = new RibbonMenuButtonMPO();
            rmbFilter.setPrefHeight(28);
            rmbFilter.setPrefWidth(28);
//            ImageView test = 
//                    Variables.imageLoader.getImageView(ImageIndex.values()[i - ImageIndex.MARIO_SMA.ordinal() + ImageIndex.MARIO.ordinal()]);
//            test.setViewport(new Rectangle2D(test.getViewport().getMinX(), 
//                    test.getViewport().getMinY(), 26, 26));
//            rmbFilter.setGraphic(test);
//            rmbFilter.setStyle(STYLE_BORDER_OFF);
            rmbFilter.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int buttonIndex = i2 - ImageIndex.MARIO_SMA.ordinal();
                    if (!instrFiltered[buttonIndex]) {
//                            rmbFilter.setGraphic(Variables.imageLoader.getImageView(ImageIndex.values()[i2]));
                        instrFilterGraphic(buttonIndex);
                        instrFiltered[buttonIndex] = true;
                        
                        DataClipboard.setInstrFiltered(instrFiltered);
                    } else {
                        rmbFilter.setGraphic(null);
                        instrFiltered[buttonIndex] = false;
                    }
                }
            });
            //Like in the previous MPCOverviewer, hover over the instrument and press F to put into filter
            rmb.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    //focus required to register keyevent in button
                    rmb.requestFocus();
                }
            });
            rmb.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    //defocus
                    instrSelectedButton.requestFocus();
                }
            });
            rmb.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode() == KeyCode.F) {
                        int buttonIndex = i2 - ImageIndex.MARIO_SMA.ordinal();
                        if (!instrFiltered[buttonIndex]) {
//                            rmbFilter.setGraphic(Variables.imageLoader.getImageView(ImageIndex.values()[i2]));
                            instrFilterGraphic(buttonIndex);
                            instrFiltered[buttonIndex] = true;      
                            
                            DataClipboard.setInstrFiltered(instrFiltered);
                        } else {
                            rmbFilter.setGraphic(null);
                            instrFiltered[buttonIndex] = false;
                        }
                    }
                }
            });
            this.addButton(1, rmbFilter);
            instrFiltered[i - ImageIndex.MARIO_SMA.ordinal()] = false;
            instrFilteredButtons.add(rmbFilter);
        }
    }

    public Note.Instrument getInstrSelected() {
        return instrSelected < 0 ? null : Note.Instrument.values()[instrSelected];
    }

    public boolean getFilterFlag() {
        return filterFlag;
    }

    public boolean[] getInstrFiltered() {
        return instrFiltered;
    }
    
    public void setNoInstr() {
        instrSelected = -1;
        instrSelectedButton.setGraphic(null);
    }

    private void instrFilterGraphic(int buttonIndex) {
        if (filterFlag) {
            zsg_sma(buttonIndex);
        } else {
            zsg_sil(buttonIndex);
        }
    }

    private void sma_to_sil() {
        for (int i = 0; i < instrFiltered.length; i++) {
            if (instrFiltered[i]) {
                zsg_sil(i);
            }
        }
    }

    private void sil_to_sma() {
        for (int i = 0; i < instrFiltered.length; i++) {
            if (instrFiltered[i]) {
                zsg_sma(i);
            }
        }
    }

    private void zsg_sma(int buttonIndex) {
        instrFilteredButtons.get(buttonIndex)
                .setGraphic(Variables.imageLoader.getImageView(ImageIndex.values()[buttonIndex + ImageIndex.MARIO_SMA.ordinal()]));
    }

    private void zsg_sil(int buttonIndex) {
        //Clip image from 32x32 to 26x26
        ImageView iv
                = Variables.imageLoader.getImageView(ImageIndex.values()[buttonIndex + ImageIndex.MARIO_SIL.ordinal()]);
        iv.setViewport(new Rectangle2D(iv.getViewport().getMinX(), iv.getViewport().getMinY(), 26, 26));
        instrFilteredButtons.get(buttonIndex).setGraphic(iv);
    }

}
