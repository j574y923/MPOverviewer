package mpoverviewer.image_layer.tabcontent;

import java.util.ArrayList;
import java.util.Set;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import mpoverviewer.data_layer.data.Note;
import mpoverviewer.global.Constants;
import mpoverviewer.global.StateMachine;
import mpoverviewer.global.Variables;
import mpoverviewer.image_layer.ImageIndex;
import mpoverviewer.image_layer.ImageLoader;
import mpoverviewer.image_layer.ribbonmenu.RibbonMenuMPO;

/**
 *
 * A Staff event handler. The StaffImages implementation was getting bulky and
 * there are still many many features to be implemented here. This handler
 * primarily handles mouse events.
 *
 * @author RehdBlob
 * @since 2013.07.27
 */
public class StaffInstrumentEventHandler implements EventHandler<Event> {

    /**
     * The line number of this note, on the screen.
     */
    private int line;

    /**
     * The position of this note.
     */
    private int position;

    /**
     * Whether the mouse is in the frame or not.
     */
    private static boolean focus = false;

//    /**
//     * This is the list of image notes that we have. These should all be
//     * ImageView-type objects.
//     */
//    private static ObservableList<Node> theImages;
//
//    /** The StackPane that will display sharps, flats, etc. */
//    private static ObservableList<Node> accList;
//
    /**
     * This is the <code>ImageView</code> object responsible for displaying the
     * silhouette of the note that we are about to place on the staff.
     */
    private static ImageView silhouette;
//
//    /** The pointer to the staff object that this handler is linked to. */
//    private Staff theStaff;
//
    /**
     * This is the <code>ImageView</code> object responsible for displaying the
     * silhouette of the sharp / flat of the note that we are about to place on
     * the staff.
     */
    private static ImageView accSilhouette;

    private static HBox accAndNote;
//
//    /** The topmost image of the instrument. */
//    private StaffNote theStaffNote;
//
//    /**
//     * This is the image that holds the different types of sharps/flats etc.
//     */
//    private StaffAccidental accidental;
//
    /**
     * This is the ImageLoader class.
     */
    private static ImageLoader il;

    /**
     * This is the amount that we want to sharp / flat / etc. a note.
     */
    private static int acc = 0;

    CompositionPane s;

    /**
     * Constructor for this StaffEventHandler. This creates a handler that takes
     * a StackPane and a position on the staff.
     *
     * -@param stPane The StackPane that we are interested in. This will be
     * updated whenever the mouse moves to a new stackpane. -@param acc The
     * accidental display pane. This will be updated whenever the mouse moves to
     * a new stackpane. -@param pos The position that this handler is located on
     * the staff. This will be updated whenever the mouse moves. -@param l The
     * line of this event handler. Typically between 0 and 9. This will be
     * updated whenever the mouse moves.
     *
     * @param s The pointer to the Staff object that this event handler is
     * linked to.
     */
    private static int test = 0;

    public StaffInstrumentEventHandler(CompositionPane s, ImageLoader i) {
        this.s = s;
//            Staff s, ImageLoader i) {

//    	disableAllStackPanes();
        il = i;
        accAndNote = new HBox();
        silhouette = Variables.imageLoader.getImageView(ImageIndex.MARIO_SIL);//temp image
        silhouette.setOpacity(0.9);
        accSilhouette = Variables.imageLoader.getImageView(ImageIndex.SHARP_SIL);
        accSilhouette.setOpacity(0.9);
        accSilhouette.setVisible(false);
//        il.setImageViewport(accSilhouette, ImageIndex.DOUBLESHARP_SIL);
        accAndNote.getChildren().addAll(accSilhouette, silhouette);

        //this is the smoothest 
        s.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
//                test++;
//                if(test % 8 > 3){
//                    return;
//                }System.out.println(event.getY());
                if (blockSOMM) {
                    return;
                }
                mouseEntered();

                //focus needed to register key presses...
                s.requestFocus();
                focus = true;
                event.consume();
            }
        });

        s.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                StateMachine.getButtonsPressed().add(event.getCode());
                updateAccidental();
                escNoInstr();
            }
        });

        s.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                StateMachine.getButtonsPressed().remove(event.getCode());
                updateAccidental();
            }
        });

//        position = pos;
//        line = l;
//        theImages = stPane.getChildren();//-
//        accList = acc.getChildren();//-
//        theStaff = s;
//        accSilhouette = new ImageView();
//        if ((Settings.debug & 0b10) == 0b10) {
//            System.out.println("Line: " + l);
//            System.out.println("Position: " + pos);
//        }
    }

    private boolean blockSOMM;
    private ImageIndex optimizeTest;

    @Override
    public void handle(Event event) {
        
        if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
                //unflag
                mouseReleased();
        }
        
        if (s.getSiehSvehMediator() == 2) {
            return;
        }
        
        Note.Instrument theInd = ((RibbonMenuMPO) Variables.stageInFocus.getRibbonMenu()).getButtonLine().getInstrSelected();
        if (theInd == null) {
            return;
        }

        if(!hasMouse(event)) {
            return;      
        }
        
        accAndNote.toFront();

        //if(no instrument selected from buttonLine)
        //return;
//    	System.out.println("TEST");
        boolean newNote = false;
        if (event instanceof MouseEvent) {
//            System.out.println(getPosition(((MouseEvent)event).getY()));
            int lineTmp = getLine(((MouseEvent) event).getX(), ((MouseEvent) event).getY());
            int positionTmp = getPosition(((MouseEvent) event).getY());
//    		
            //invalid
            if (lineTmp < 0 || positionTmp < 0) {//MOUSE_EXITED
//    	        InstrumentIndex theInd = ButtonLine.getSelectedInstrument();
                mouseExited();//theInd);
                return;
            }
//    		
//    		//new note
            if (line != lineTmp || position != positionTmp) {
                newNote = true;

                line = lineTmp;
                position = positionTmp;
//    			StackPane[] noteAndAcc = theStaff.getNoteMatrix().getNote(line, position);
//    			
//    			if(!noteAndAcc[0].isDisabled())
//    				disableAllStackPanes();
//    			
//    			theImages = noteAndAcc[0].getChildren();
//    			accList = noteAndAcc[1].getChildren();
            }
        } else if (event instanceof ScrollEvent) {
//            System.out.println(((ScrollEvent)event).getY());
            int lineTmp = getLine(((ScrollEvent) event).getX(), ((ScrollEvent) event).getY());
            int positionTmp = getPosition(((ScrollEvent) event).getY());
//    		
            //invalid
            if (lineTmp < 0 || positionTmp < 0) {//MOUSE_EXITED
//    	        InstrumentIndex theInd = ButtonLine.getSelectedInstrument();
                mouseExited();//theInd);
                return;
            }
//    		
//    		//new note
            if (line != lineTmp || position != positionTmp) {
                newNote = true;

                line = lineTmp;
                position = positionTmp;
//    			StackPane[] noteAndAcc = theStaff.getNoteMatrix().getNote(line, position);
//    			
//    			if(!noteAndAcc[0].isDisabled())
//    				disableAllStackPanes();
//    			
//    			theImages = noteAndAcc[0].getChildren();
//    			accList = noteAndAcc[1].getChildren();
            }
            mouseEntered();
        }
//    	
//        InstrumentIndex theInd = ButtonLine.getSelectedInstrument();        
//Note.Instrument theInd = ((RibbonMenuMPO)Variables.stageInFocus.getRibbonMenu()).getButtonLine().getInstrSelected();
        ImageIndex optA = ImageIndex.values()[ImageIndex.MARIO_SIL.ordinal() + theInd.ordinal()];
        if (!optA.equals(optimizeTest)) {
            il.setImageViewport(silhouette, ImageIndex.values()[ImageIndex.MARIO_SIL.ordinal() + theInd.ordinal()]);
            optimizeTest = optA;
        }
        //Drag-add notes, hold e to drag-remove notes
        if (event instanceof MouseEvent && ((MouseEvent) event).isPrimaryButtonDown()
                && newNote) {
//this is only working when the cursor is right of the note its hovering over (??)
            blockSOMM = true;
            leftMousePressed(theInd);
            System.out.println("TESTA");
            event.consume();
            StateMachine.setSongModified(true);

        } //		//Drag-remove notes
        else if (event instanceof MouseEvent && ((MouseEvent) event).isSecondaryButtonDown()) {
            //this is only working when the cursor is right of the note its hovering over
//    System.out.println("TEST");
            rightMousePressed();//theInd);
            event.consume();
            StateMachine.setSongModified(true);

        } else if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            MouseButton b = ((MouseEvent) event).getButton();
            if (b == MouseButton.PRIMARY) {
                leftMousePressed(theInd);
            } else if (b == MouseButton.SECONDARY) {
                rightMousePressed();//theInd);
            }
            event.consume();
            StateMachine.setSongModified(true);

        }// else 
        //if (event.getEventType() == MouseEvent.MOUSE_MOVED && newNote) {//was MOUSE_ENTERED
        //            focus = true;
        //            mouseEntered();//theInd);
        //            event.consume();
        //        }// else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
        //            focus = false;
        //            mouseExited(theInd);
        //            event.consume();
        //        }
        //
//        else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
//            blockSOMM = false;
//        }
    }

    private void mouseReleased() {
        blockSOMM = false;
        s.setSiehSvehMediator(0);
    }
    
    /**
     * The method that is called when the left mouse button is pressed. This is
     * generally the signal to add an instrument to that line.
     *
     * @param theInd The InstrumentIndex corresponding to what instrument is
     * currently selected.
     */
    private void leftMousePressed(Note.Instrument theInd) {
        
        s.setSiehSvehMediator(1);
        
        if (StateMachine.getButtonsPressed().contains(KeyCode.E)) {
            removeNote();
        } else {
            placeNote(theInd);
        }
    }

    /**
     * Places a note where the mouse currently is.
     *
     * @param theInd The <code>InstrumentIndex</code> that we are going to use
     * to place this note.
     */
    private void placeNote(Note.Instrument theInd) {
//        s.getPane().getChildren().remove(accAndNote);
        s.addNote(line, theInd, Note.Position.values()[position], zacc());
//        boolean mute = StateMachine.isMutePressed();
//        boolean muteA = StateMachine.isMuteAPressed();
//
//        if (!mute && !muteA)
//            playSound(theInd, position, acc);
//
//        theStaffNote = new StaffNote(theInd, position, acc);
//
//        theStaffNote.setMuteNote(muteA ? 2 : mute ? 1 : 0);
//
//        if (!mute && !muteA) {
//            theStaffNote.setImageViewport(il.getSpriteFX(theInd.imageIndex()));
//        } else if (mute) {
//            theStaffNote.setImageViewport(il.getSpriteFX(theInd.imageIndex().alt()));
//        } else if (muteA) {
//            theStaffNote.setImageViewport(il.getSpriteFX(theInd.imageIndex()
//                    .silhouette()));
//        }
//
//        accidental = new StaffAccidental(theStaffNote);
//        accidental.setImageViewport(il.getSpriteFX(Staff.switchAcc(acc)));
//
//        theImages.remove(silhouette);
//        accList.remove(accSilhouette);
//
//        if (!theImages.contains(theStaffNote))
//            theImages.add(theStaffNote);
//
//        if (!accList.contains(accidental))
//            accList.add(accidental);
//
//        StaffNoteLine temp = theStaff.getSequence().getLine(
//                line + StateMachine.getMeasureLineNum());
//
//        if (temp.isEmpty()) {
//            temp.setVolumePercent(((double) Values.DEFAULT_VELOCITY)
//                    / Values.MAX_VELOCITY);
//        }
//
//        if (!temp.contains(theStaffNote))
//            temp.add(theStaffNote);
//        StaffVolumeEventHandler sveh = theStaff.getNoteMatrix().getVolHandler(
//                line);
//        sveh.updateVolume();
//        theStaff.redraw();
    }

    /**
     * The method that is called when the right mouse button is pressed. This is
     * generally the signal to remove the instrument from that line.
     *
     * @param theInd The InstrumentIndex corresponding to what instrument is
     * currently selected. (currently not actually used, but can be extended
     * later to selectively remove instruments.
     */
    private void rightMousePressed() {//InstrumentIndex theInd) {

        s.setSiehSvehMediator(1);
        removeNote();

    }

    /**
     * This removes a note.
     */
    private void removeNote() {
//        s.getPane().getChildren().remove(accAndNote);
//        theImages.remove(silhouette);
//        accList.remove(accSilhouette);
//
//        if (!theImages.isEmpty())
//            theImages.remove(theImages.size() - 1);
//        if (!accList.isEmpty())
//            accList.remove(0);
//
//        StaffNoteLine temp = theStaff.getSequence().getLine(
//                line + StateMachine.getMeasureLineNum());
        s.removeNote(line, Note.Position.values()[position]);
//        if (!temp.isEmpty()) {
//            ArrayList<StaffNote> nt = temp.getNotes();
//            for (int i = nt.size() - 1; i >= 0; i--) {
//                StaffNote s = nt.get(i);
//                if (s.getPosition() == position) {
//                    nt.remove(i);
//                    break;
//                }
//            }
//        }

//        if (temp.isEmpty()) {
//            StaffVolumeEventHandler sveh = theStaff.getNoteMatrix()
//                    .getVolHandler(line);
//            sveh.setVolumeVisible(false);
//        }
//        mouseExited();
//        mouseEntered();
//        theStaff.redraw();
    }

    /**
     * The method that is called when the mouse enters the object.
     *
     * @param theInd The InstrumentIndex corresponding to what instrument is
     * currently selected.
     */
    private void mouseEntered() {//InstrumentIndex theInd) {
//        StateMachine.setFocusPane(this);
//        theStaff.getNoteMatrix().setFocusPane(this);
//        updateAccidental();
//        silhouette.setImageViewport(il.getSpriteFX(theInd.imageIndex().silhouette()));
        if (!s.getPane().getChildren().contains(accAndNote))//        if (!theImages.contains(silhouette))
        {
            s.getPane().getChildren().add(accAndNote);//            theImages.add(silhouette);
        }        //high cpu usage, use a newNote flag to prevent translate from happening every time mouse moved
        accAndNote.setTranslateX((line % Constants.LINES_IN_A_ROW) * Constants.LINE_SPACING + Constants.EDGE_MARGIN + Constants.LINE_SPACING_OFFSET_X - 48);
        accAndNote.setTranslateY((line / Constants.LINES_IN_A_ROW) * Constants.ROW_HEIGHT_TOTAL + (position) * 16 + 5);
//        accSilhouette.setImageViewport(il
//                .getSpriteFX(Staff.switchAcc(acc).silhouette()));
//        if (!accList.contains(accSilhouette))
//            accList.add(accSilhouette);
//        silhouette.setVisible(true);
//        accSilhouette.setVisible(true);
    }

    /**
     * The method that is called when the mouse exits the object.
     *
     * @param children List of Nodes that we have here, hopefully full of
     * ImageView-type objects.
     * @param theInd The InstrumentIndex corresponding to what instrument is
     * currently selected.
     */
    private void mouseExited() {//InstrumentIndex theInd) {
        if (s.getPane().getChildren().contains(accAndNote)) {
            s.getPane().getChildren().remove(accAndNote);
        }
//    	if(silhouette.getImage() != null){
//            s.getPane().getChildren().remove(accAndNote);//theImages.remove(silhouette);
//        }
//    	if(accSilhouette.getImage() != null){
//            s.getPane().getChildren().remove(accSilhouette);//accList.remove(accSilhouette);
//        }
    }

    /**
     * Updates how much we want to sharp / flat a note.
     */
    public static void updateAccidental() {
        if (!focus) {
            return;
        }
        Set<KeyCode> bp = StateMachine.getButtonsPressed();
        boolean ctrl = bp.contains(KeyCode.CONTROL);
        boolean shift = bp.contains(KeyCode.SHIFT);
        boolean alt = bp.contains(KeyCode.ALT) || bp.contains(KeyCode.ALT_GRAPH);

        if (alt && ctrl) {
            acc = -2;
        } else if (ctrl && shift) {
            acc = 2;
        } else if (shift) {
            acc = 1;
        } else if (alt || ctrl) {
            acc = -1;
        } else {
            acc = 0;
        }

        switch (acc) {
            case 2:
                il.setImageViewport(accSilhouette, ImageIndex.DOUBLESHARP_SIL);//accSilhouette.setImageViewport(il.getSpriteFX(ImageIndex.DOUBLESHARP_SIL));
                break;
            case 1:
                il.setImageViewport(accSilhouette, ImageIndex.SHARP_SIL);//accSilhouette.setImageViewport(il.getSpriteFX(ImageIndex.SHARP_SIL));
                break;
            case -1:
                il.setImageViewport(accSilhouette, ImageIndex.FLAT_SIL);//accSilhouette.setImageViewport(il.getSpriteFX(ImageIndex.FLAT_SIL));
                break;
            case -2:
                il.setImageViewport(accSilhouette, ImageIndex.DOUBLEFLAT_SIL);//accSilhouette.setImageViewport(il.getSpriteFX(ImageIndex.DOUBLEFLAT_SIL));
                break;
            default:
                accSilhouette.setVisible(false);
                break;
        }

        if (acc != 0) {
            accSilhouette.setVisible(true);
        }

//        if (acc != 0 && !accList.contains(accSilhouette))
//            accList.add(accSilhouette);
//        if (acc != 0 && !theImages.contains(silhouette)) {
//            theImages.add(silhouette);
//            silhouette.setImageViewport(il.getSpriteFX(ButtonLine
//                    .getSelectedInstrument().imageIndex().silhouette()));
//            silhouette.setVisible(true);
//        }
        //Cannot use this in a static context... will fix this later
//        if ((Settings.debug & 0b01) == 0b01) {
//            System.out.println(this);
//        }
    }
//
//    /**
//     * Called whenever we request a redraw of the staff.
//     */
//    public void redraw() {
//        if (!focus)
//            return;
//        InstrumentIndex ind = ButtonLine.getSelectedInstrument();
//        mouseExited(ind);
//        mouseEntered(ind);
//    }
//
//    /**
//     * Plays a sound given an index and a position.
//     *
//     * @param theInd
//     *            The index at which this instrument is located at.
//     * @param pos
//     *            The position at which this note is located at.
//     * @param acc
//     *            The sharp / flat that we want to play this note at.
//     */
//    private static void playSound(InstrumentIndex theInd, int pos, int acc) {
//        SoundfontLoader.playSound(Values.staffNotes[pos].getKeyNum(), theInd,
//                acc);
//    }
//

    /**
     * Sets the amount that we want to sharp / flat a note.
     *
     * @param accidental Any integer between -2 and 2.
     */
    public void setAcc(int accidental) {
        acc = accidental;
    }

    /**
     * @return The amount that a note is to be offset from its usual position.
     */
    public int getAcc() {
        return acc;
    }

    /**
     * @return The line that this handler is located on.
     */
    public int getLine() {
        return line;
    }

    /**
     * @return Whether the mouse is currently in the frame.
     */
    public boolean hasMouse() {
        return focus;
    }

//    @Override
//    public String toString() {
//        String out = "Line: " + (StateMachine.getMeasureLineNum() + line)
//                + "\nPosition: " + position + "\nAccidental: " + acc;
//        return out;
//    }
    /**
     *
     * @param x mouse pos for entire scene
     * @param y mouse pos for entire scene
     * @return line based on x and y coord
     */
    private int getLine(double x, double y) {

        if (x < 122 || x > Constants.WIDTH_DEFAULT - 48 || !zby(y))//122 is arbitrary, 48 is arbitrary
        {
            return -1;
        }
        return (((int) x - 122) / 64)
                + ((int) y / Constants.ROW_HEIGHT_TOTAL) * Constants.LINES_IN_A_ROW;
    }

    /**
     *
     * @param y mouse pos for entire scene
     * @return note position based on y coord, -1 if given y is in the volume
     * margins
     */
    private int getPosition(double y) {
        if (!zby(y)) {
            return -1;
        }
        return (((int) y % Constants.ROW_HEIGHT_TOTAL - 10) / 16);//10 is arbitrary
    }

    /* If valid y */
    private boolean zby(double y) {
        return y % Constants.ROW_HEIGHT_TOTAL <= Constants.ROW_HEIGHT_NOTES;
    }

    private Note.Modifier zacc() {
        switch (acc) {
            case 2:
                return Note.Modifier.DOUBLESHARP;
            case 1:
                return Note.Modifier.SHARP;
            case 0:
                return Note.Modifier.NONE;
            case -1:
                return Note.Modifier.FLAT;
            case -2:
                return Note.Modifier.DOUBLEFLAT;
            default:
                return Note.Modifier.NONE;
        }
    }

    private void escNoInstr() {
        if (StateMachine.getButtonsPressed().contains(KeyCode.ESCAPE)) {
            ((RibbonMenuMPO) Variables.stageInFocus.getRibbonMenu()).getButtonLine().setNoInstr();
        }
    }
    
    /**
     * 
     * @param event
     * @return if the staff area containing instruments has the mouse hovering over it
     */
    private boolean hasMouse(Event event) {
        if(event instanceof MouseEvent) {
            return getPosition(((MouseEvent)event).getY()) > -1;
        } else if (event instanceof ScrollEvent) {
            return getPosition(((ScrollEvent)event).getY()) > -1;
        }
        return false;
    }
}
