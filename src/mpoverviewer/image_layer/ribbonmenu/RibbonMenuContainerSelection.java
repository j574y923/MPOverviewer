package mpoverviewer.image_layer.ribbonmenu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Tooltip;
import mpoverviewer.data_layer.dataclipboard.DataClipboardFunctions;
import mpoverviewer.global.Variables;
import mpoverviewer.image_layer.ImageIndex;
import mpoverviewer.image_layer.tabcontent.CompositionPane;
import mpoverviewer.ui_layer.ribbonmenu.RibbonMenuContainer;
import mpoverviewer.ui_layer.tab.content.ContentControl;

/**
 *
 * @author J
 */
public class RibbonMenuContainerSelection extends RibbonMenuContainer {

    public enum Selection {
        SELECTION_NOTES,
        SELECTION_VOL,
        SELECTION_NOTES_AND_VOL,
    }
    
    private static final String DESCRIPTION_SEL = "Selected selection tool";
    
    private static final Tooltip TT_DESCRIPTION_SEL = new Tooltip(DESCRIPTION_SEL);
    
    private static final String DESCRIPTION_N = "Select Notes only:\n"
            + "Only notes will be selected and copied, deleted, etc.";

    private static final Tooltip TT_DESCRIPTION_N = new Tooltip(DESCRIPTION_N);

    private static final String DESCRIPTION_V = "Select Volume only:\n"
            + "Only volume will be selected and copied, deleted, etc.";

    private static final Tooltip TT_DESCRIPTION_V = new Tooltip(DESCRIPTION_V);

    private static final String DESCRIPTION_NAV = "Select both Notes and Volume:\n"
            + "Both notes and volume will be selected and copied, deleted, etc.";

    private static final Tooltip TT_DESCRIPTION_NAV = new Tooltip(DESCRIPTION_NAV);

    private static final String DESCRIPTION_KB = "Keep bounds Toggle:\n"
            + "Your selected bounds will no longer disappear when you release the mouse.\n"
            + "The same bounds will remain visible and can be copied, deleted, etc. and \n"
            + "will also appear on all tabs in the same window that you switch to.";

    private static final Tooltip TT_DESCRIPTION_KB = new Tooltip(DESCRIPTION_KB);

    private static final String DESCRIPTION_L = "Linear Selection Toggle:\n"
            + "Selection will no longer be a rubber band but a text selector equivalent.\n"
            + "Highlight notes the same way you would highlight text in a text editor.";

    private static final Tooltip TT_DESCRIPTION_L = new Tooltip(DESCRIPTION_L);

    private RibbonMenuButtonMPO selection;
    private RibbonMenuButtonMPO selNotes;
    private RibbonMenuButtonMPO selVol;
    private RibbonMenuButtonMPO selNotesAndVol;
    private RibbonMenuButtonMPO selKeepBounds;
    private RibbonMenuButtonMPO selLinear;
    
    private ImageIndex selectionImage;
    private boolean selectionToggle;
    private boolean keepBoundsFlag;
    private boolean linearFlag;
    
    public RibbonMenuContainerSelection() {
        super("Selection tools");
        
        selectionImage = ImageIndex.SELECTION_NOTES_AND_VOL;
        selection = new RibbonMenuButtonMPO();
        selection.setGraphic(Variables.imageLoader.getImageView(selectionImage));
        selection.setTooltip(TT_DESCRIPTION_SEL);
        selNotes = new RibbonMenuButtonMPO();
        selNotes.setGraphic(Variables.imageLoader.getImageView(ImageIndex.SELECTION_NOTES));
        selNotes.setTooltip(TT_DESCRIPTION_N);
        selVol = new RibbonMenuButtonMPO();
        selVol.setGraphic(Variables.imageLoader.getImageView(ImageIndex.SELECTION_VOL));
        selVol.setTooltip(TT_DESCRIPTION_V);
        selNotesAndVol = new RibbonMenuButtonMPO();
        selNotesAndVol.setGraphic(Variables.imageLoader.getImageView(ImageIndex.SELECTION_NOTES_AND_VOL));
        selNotesAndVol.setTooltip(TT_DESCRIPTION_NAV);        
        
        selection.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(selectionToggle) {
                    selection.setGraphic(Variables.imageLoader.getImageView(selectionImage));
                    selectionToggle = false;
                } else {
                    selection.setGraphic(Variables.imageLoader.getImageView(ImageIndex.valueOf(selectionImage.toString() + "_HL")));
                    selectionToggle = true;
                }
            }});
        selNotes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectionImage = ImageIndex.SELECTION_NOTES;
                if(selectionToggle) {
                    selection.setGraphic(Variables.imageLoader.getImageView(ImageIndex.valueOf(selectionImage.toString() + "_HL")));
                } else {
                    selection.setGraphic(Variables.imageLoader.getImageView(selectionImage));
                }
                
                ((CompositionPane) Variables.stageInFocus.getTabPane().getSelectionModel()
                        .getSelectedItem().getContent()).getEHRB().selectNotes();
                
                ((CompositionPane) Variables.stageInFocus.getTabPane().getSelectionModel()
                        .getSelectedItem().getContent()).unhighlightAllVols();
                DataClipboardFunctions.deselVols();
            }
        });
        selVol.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectionImage = ImageIndex.SELECTION_VOL;
                if(selectionToggle) {
                    selection.setGraphic(Variables.imageLoader.getImageView(ImageIndex.valueOf(selectionImage.toString() + "_HL")));
                } else {
                    selection.setGraphic(Variables.imageLoader.getImageView(selectionImage));
                }     
                
                ((CompositionPane) Variables.stageInFocus.getTabPane().getSelectionModel()
                        .getSelectedItem().getContent()).getEHRB().selectVols();
                
                ((CompositionPane) Variables.stageInFocus.getTabPane().getSelectionModel()
                        .getSelectedItem().getContent()).unhighlightAllNotes();
                DataClipboardFunctions.deselNotes();
            }
        });
        selNotesAndVol.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectionImage = ImageIndex.SELECTION_NOTES_AND_VOL;
                if(selectionToggle) {
                    selection.setGraphic(Variables.imageLoader.getImageView(ImageIndex.valueOf(selectionImage.toString() + "_HL")));
                } else {
                    selection.setGraphic(Variables.imageLoader.getImageView(selectionImage));
                }            
 
                ((CompositionPane) Variables.stageInFocus.getTabPane().getSelectionModel()
                        .getSelectedItem().getContent()).getEHRB().selectNotes();
                                
                ((CompositionPane) Variables.stageInFocus.getTabPane().getSelectionModel()
                        .getSelectedItem().getContent()).getEHRB().selectVols();
            }
        });
        
        selKeepBounds = new RibbonMenuButtonMPO();
        selKeepBounds.setGraphic(Variables.imageLoader.getImageView(ImageIndex.SELECTION_KEEPBOUNDS_MULTITAB_OFF));
        selKeepBounds.setTooltip(TT_DESCRIPTION_KB);
        selKeepBounds.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setKeepBounds(!keepBoundsFlag);
            }
        });
        selLinear = new RibbonMenuButtonMPO();
        selLinear.setGraphic(Variables.imageLoader.getImageView(ImageIndex.SELECTION_LINEAR_OFF));
        selLinear.setTooltip(TT_DESCRIPTION_L);        
        selLinear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setLinear(!linearFlag);
            }
        });

        this.addButton(0, selection);
        this.addButton(1, selNotes);
        this.addButton(1, selVol);
        this.addButton(1, selNotesAndVol);
        this.addButton(1, selKeepBounds);
        this.addButton(1, selLinear);
    }
    
    public void setKeepBounds(boolean keepBoundsFlag) {
        this.keepBoundsFlag = keepBoundsFlag;
        if (keepBoundsFlag) {
            selKeepBounds.setGraphic(Variables.imageLoader.getImageView(ImageIndex.SELECTION_KEEPBOUNDS_MULTITAB_ON));
        } else {
            selKeepBounds.setGraphic(Variables.imageLoader.getImageView(ImageIndex.SELECTION_KEEPBOUNDS_MULTITAB_OFF));
        }
    }
    
    public void setLinear(boolean linearFlag) {
        this.linearFlag = linearFlag;
        if (linearFlag) {
            selLinear.setGraphic(Variables.imageLoader.getImageView(ImageIndex.SELECTION_LINEAR_ON));
        } else {
            selLinear.setGraphic(Variables.imageLoader.getImageView(ImageIndex.SELECTION_LINEAR_OFF));
        }
    }
    
    /**
     * 
     * @return if the selection rubber band tool is now toggled on
     */
    public boolean selectionToolToggled() {
        return selectionToggle;
    }
    
    /**
     * 
     * @return selection type based on selection enum of notes, vol, or
     * notes_and_vol
     */
    public Selection getSelectionType() {
        return Selection.valueOf(selectionImage.toString());
    }
    
    /**
     * 
     * @return flag indicating toggle of keep bounds option. Keep bounds means
     * to retain the rubberband and also apply that rubberband to all tabs in 
     * its window.
     */
    public boolean getKeepBoundsFlag() {
        return keepBoundsFlag;
    }
    
    /**
     *
     * @return flag indicating toggle of linear selection. Linear selection used
     * to select a linear range of lines.
     */
    public boolean getLinearFlag() {
        return linearFlag;
    }
}