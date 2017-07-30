package mpoverviewer.image_layer.tabcontent;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import mpoverviewer.data_layer.data.Note;
import mpoverviewer.global.Constants;
import mpoverviewer.global.Variables;
import mpoverviewer.image_layer.ribbonmenu.RibbonMenuMPO;

/**
 * This takes care of the volume bars on the staff.
 *
 * @author RehdBlob
 * @since 2013.12.01
 *
 */
public class StaffVolumeEventHandler implements EventHandler<Event> {

    
    private CompositionPane s;
    /**
     * The line number of this volume bar, on the screen.
     */
    private int line;

    /**
     * The StackPane that this event handler is linked to.
     */
    private StackPane stp;

    /**
     * The ImageView object that is this volume bar.
     */
    private ImageView theVolBar;

    /**
     * The StaffNoteLine that this event handler is associated with.
     */
//    private StaffNoteLine theLine;
//    
//    /** The ImageLoader class. */
//    private ImageLoader il;
    
    private boolean mousePressed;
    
    /**
     * Makes a new StaffVolumeEventHandler.
     */
    public StaffVolumeEventHandler() {//StackPane st){//, ImageLoader i) {
//        stp = st;
//        il = i;
//        theVolBar = (ImageView) st.getChildren().get(0);
//        theVolBar.setImage(il.getSpriteFX(ImageIndex.VOL_BAR));
//        theVolBar.setVisible(false);
    }

    public StaffVolumeEventHandler(CompositionPane s) {
        this.s = s;
    }

    @Override
    public void handle(Event event) {
//        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
//            mousePressed((MouseEvent) event);
//        } else if (event.getEventType() == MouseEvent.DRAG_DETECTED) {
//            mouseDragStart();
//        } else if (event.getEventType() == DragEvent.DRAG_DONE) {
//            mouseDragEnd();
//        }
        if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
                //unflag
                mouseReleased();
        }
        
        if (s.getSiehSvehMediator() == 1) {
            return;
        }

        Note.Instrument theInd = ((RibbonMenuMPO) Variables.stageInFocus.getRibbonMenu()).getButtonLine().getInstrSelected();
        if (theInd == null) {
            return;
        }
        
        if (!hasMouse(event)) {
            return;
        }
        if (event instanceof MouseEvent && ((MouseEvent) event).isPrimaryButtonDown()) {
            mousePressed((MouseEvent) event);
        } else if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            //mouse click begin in volume area. set a flag that will block staffinstrumenteventhandler until mouse release
            //Note: a similar flag should be created for instrumenthandler
        } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
            //unflag
//            mouseReleased();
//see line 75
        } else if (event instanceof ScrollEvent) {
            //System.out.println(((ScrollEvent)event).getX());//scrollevent getx is the x before scroll occurs. may have to find the scrollbar for compositionpane and get the increment amount from scrollbar and add onto the x.
            scrollStart((ScrollEvent) event);
        }
    }

    /**
     * Called whenever the mouse is pressed.
     */
    private void mousePressed(MouseEvent event) {
//        if (!theLine.getNotes().isEmpty()) {
//        	if(event.getY() < 0 || stp.getHeight() < event.getY())
//        		return;
//            double h = stp.getHeight() - event.getY();
////            System.out.println("SGH:" + stp.getHeight() + "EGY:" + event.getY());
//            setVolumeDisplay(h);
//            try {
//                setVolumePercent(h / stp.getHeight());
//            } catch (IllegalArgumentException e) {
//                setVolume(Values.MAX_VELOCITY);
//                setVolumeDisplay(stp.getHeight());
//            }
//        }

        mousePressed = true;
        s.setSiehSvehMediator(2);
        
        int line = getLine(event.getX(), event.getY());
        if(!s.getSong().staff.get(line).measureLine.isEmpty()) {
            s.setVolume(line, getVolumePosition(event.getY()));
        }
    }

    /**
     * Called whenever the mouse is dragged.
     */
    private void mouseDragStart() {

    }

    /**
     * Called whenever we finish dragging the mouse.
     */
    private void mouseDragEnd() {

    }

    private void mouseReleased() {
        mousePressed = false;
        s.setSiehSvehMediator(0);
    }
    
    private void scrollStart(ScrollEvent event) {
        if(mousePressed) {
            int line = getLine(event.getX(), event.getY());
            if(!s.getSong().staff.get(line).measureLine.isEmpty()) {
                s.setVolume(line, getVolumePosition(event.getY()));
            }
        }
    }
    
    /**
     * Sets the volume of this note based on the y location of the click.
     *
     * @param y The y-location of the click.
     */
    private void setVolume(double y) {
//        theLine.setVolume(y);
    }

    /**
     * Sets the volume of this note based on the y location of the click.
     *
     * @param y The percent we want to set this note line at. Should be between
     * 0 and 1.
     */
    private void setVolumePercent(double y) throws IllegalArgumentException {
//        theLine.setVolumePercent(y);
    }

    /**
     * Displays the volume of this note line.
     *
     * @param y The volume that we want to show.
     */
    public void setVolumeDisplay(double y) {
        if (y <= 0) {
            theVolBar.setVisible(false);
            return;
        }
        theVolBar.setVisible(true);
        theVolBar.setFitHeight(y);
    }

    /**
     * Do we actually want this volume bar to be visible?
     *
     * @param b Whether this volume bar is visible or not.
     */
    public void setVolumeVisible(boolean b) {
        theVolBar.setVisible(b);
    }

    /**
     * Sets the StaffNoteLine that this event handler is controlling.
     *
     * @param s The StaffNoteLine that this handler is controlling at the
     * moment.
     */
//    public void setStaffNoteLine(StaffNoteLine s) {
//        theLine = s;
//    }
    /**
     * @return The StaffNoteLine that this handler is currently controlling.
     */
//    public StaffNoteLine getStaffNoteLine() {
//        return theLine;
//    }
    /**
     * Updates the volume display on this volume displayer.
     */
    public void updateVolume() {
//        setVolumeDisplay(theLine.getVolumePercent() * stp.getHeight());
//        if (theLine.getVolume() == 0 || theLine.isEmpty()) {
//            setVolumeVisible(false);
//        }
    }

    @Override
    public String toString() {
        return "Line: " + line;
    }
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
     * @return volume amount/position based on y coord, -1 if given y is in the
     * note margins, max of 127
     */
    private int getVolumePosition(double y) {
        if (!zby(y)) {
            return -1;
        }
        return Math.min((int)(
                ((double)(Constants.ROW_HEIGHT_TOTAL - ((int) y % Constants.ROW_HEIGHT_TOTAL) - 1) //-1 idk
                / Constants.ROW_HEIGHT_VOL)
                * Constants.MAX_VELOCITY),
                Constants.MAX_VELOCITY);
    }

    /* If valid y */
    private boolean zby(double y) {
        return y % Constants.ROW_HEIGHT_TOTAL > Constants.ROW_HEIGHT_NOTES;
    }

    /**
     *
     * @param event
     * @return if the staff area containing volumes has the mouse hovering over
     * it
     */
    private boolean hasMouse(Event event) {
        if (event instanceof MouseEvent) {
            return zby(((MouseEvent) event).getY());
        } else if (event instanceof ScrollEvent) {
            return zby(((ScrollEvent) event).getY());
        }
        return false;
    }

}
