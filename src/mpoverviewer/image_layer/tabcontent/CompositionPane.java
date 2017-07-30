package mpoverviewer.image_layer.tabcontent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import mpoverviewer.data_layer.data.MeasureLine;
import mpoverviewer.data_layer.data.Note;
import mpoverviewer.data_layer.data.Song;
import mpoverviewer.global.Constants;
import mpoverviewer.global.Variables;
import mpoverviewer.image_layer.ImageIndex;
import mpoverviewer.ui_layer.tab.TabDraggable;
import mpoverviewer.ui_layer.tab.content.ContentControl;

/**
 *
 * @author J
 */
public class CompositionPane extends ScrollPane implements ContentControl {

    private Song song;

    private Pane pane;
    private Group content;
    private int scale = 10;
    private static final int SCALE_DEFAULT = 10;
    private static int scaleShared = 10;

    private ScrollBar scrollBarH = null;
    private ScrollBar scrollBarV = null;

    private final List<ImageView> compositionBG = new ArrayList<>();
    private final List<Line> lineBG = new ArrayList<>();
    private final List<Text> measureNum = new ArrayList<>();

    private HashMap<Note, ImageView[]> composition;
    private List<Line> compositionVol;
    private List<Text> compositionVolText;

    /* 0 = no mediator, 1 = sieh and block sveh, 2 = sveh and block sieh, note: this is temporary */
    private int siehSvehMediator;    
    public void setSiehSvehMediator(int siehSvehMediator) {
        switch (siehSvehMediator) {
            case 0:
                this.siehSvehMediator = 0;
                break;
            case 1:
                this.siehSvehMediator = 1;
                break;
            case 2:
                this.siehSvehMediator = 2;
                break;
            default:
                this.siehSvehMediator = 0;
        }
    }
    public int getSiehSvehMediator() {
        return siehSvehMediator;
    }
    
    public CompositionPane(Song song) {
        super();
        
        //<note, imageNote (0) and imageAccidental (1)>
//        HashMap<Note, ImageView[]> test;
//        test = new HashMap<>();
//        pane.getChildren().addAll(test.get(new Note(0,0,0)));
//        to get notes in a rectangle, use math to determine min and max bounds and Song to iterate over notes
        
        initBG();
        compositionVol = new ArrayList<>();
        compositionVolText = new ArrayList<>();
        composition = new HashMap<>();
        initDraw(song);

        pane.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
//                if (event.isControlDown()) {
//                    if (event.getDeltaY() < 0) {
//                        zoomOut();
////                        keyControl(KeyCode.UP);
//                    } else {
//                        zoomIn();
//
////                        keyControl(KeyCode.UP);
//                    }
//                    scaleShared = scale;
//                    event.consume();
//                }
            }
        });

//        sharedZoom();
        //OPTIMIZATION: do this when switching away to another tab??
//        pane.getChildren().clear();
//        staff.clear();
//        compositionVol.clear();
        pane.addEventHandler(MouseEvent.ANY, new EventHandlerRubberBand(pane, this));
        pane.addEventFilter(InputEvent.ANY, new StaffInstrumentEventHandler(this, Variables.imageLoader));
        pane.addEventFilter(InputEvent.ANY, new StaffVolumeEventHandler(this));
        
//        ArrayList<StackPane> lol = new ArrayList<>();
//        for(int i =0 ;i  < 7200; i++){
//            System.out.println(i);
//            lol.add(new StackPane());
//        }
//        pane.getChildren().addAll(lol);
    }

    @Override
    public void cleanUp() {
//        pane.getChildren().clear();
//        staff.clear();
//        compositionVol.clear();
//        compositionBG.clear();
//        lineBG.clear();
//        measureNum.clear();
    }

    private void initBG() {
        if (!compositionBG.isEmpty()) {
            return;
        }
        for (int i = 0; i < 22 * 12; i++) {
            ImageView iv = Variables.imageLoader.getImageView(ImageIndex.STAFF_BG);
            iv.setTranslateX((i % 22) * 100 + Constants.EDGE_MARGIN);
            iv.setTranslateY((i / 22) * Constants.ROW_HEIGHT_TOTAL + 16);
            compositionBG.add(iv);
        }

        for (int i = 0; i < 12; i++) {
            ImageView iv = Variables.imageLoader.getImageView(ImageIndex.TREBLE_CLEF_MPC);
            iv.setTranslateX(Constants.EDGE_MARGIN);
            iv.setTranslateY(i * Constants.ROW_HEIGHT_TOTAL + 16);
            compositionBG.add(iv);
            int eight = Constants.LINES_IN_A_ROW / 4;
            for (int j = 0; j < eight; j++) {
                Text t = new Text(Constants.EDGE_MARGIN + Constants.LINE_SPACING_OFFSET_X + j * Constants.MEASURE_NUM_SPACING_4 - 2,
                        i * Constants.ROW_HEIGHT_TOTAL + 16 - 4, "" + (1 + i * eight + j));
                measureNum.add(t);

                ImageView iv1 = Variables.imageLoader.getImageView(ImageIndex.STAFF_MLINE);
                iv1.setTranslateX(Constants.EDGE_MARGIN + Constants.LINE_SPACING_OFFSET_X + j * Constants.MEASURE_NUM_SPACING_4);
                iv1.setTranslateY(i * Constants.ROW_HEIGHT_TOTAL + 16);
                compositionBG.add(iv1);

                ImageView iv2 = Variables.imageLoader.getImageView(ImageIndex.STAFF_LINE);
                iv2.setTranslateX(Constants.EDGE_MARGIN + Constants.LINE_SPACING_OFFSET_X + j * Constants.MEASURE_NUM_SPACING_4 + Constants.LINE_SPACING);
                iv2.setTranslateY(i * Constants.ROW_HEIGHT_TOTAL + 16);
                compositionBG.add(iv2);

                ImageView iv3 = Variables.imageLoader.getImageView(ImageIndex.STAFF_LINE);
                iv3.setTranslateX(Constants.EDGE_MARGIN + Constants.LINE_SPACING_OFFSET_X + j * Constants.MEASURE_NUM_SPACING_4 + Constants.LINE_SPACING * 2);
                iv3.setTranslateY(i * Constants.ROW_HEIGHT_TOTAL + 16);
                compositionBG.add(iv3);

                ImageView iv4 = Variables.imageLoader.getImageView(ImageIndex.STAFF_LINE);
                iv4.setTranslateX(Constants.EDGE_MARGIN + Constants.LINE_SPACING_OFFSET_X + j * Constants.MEASURE_NUM_SPACING_4 + Constants.LINE_SPACING * 3);
                iv4.setTranslateY(i * Constants.ROW_HEIGHT_TOTAL + 16);
                compositionBG.add(iv4);
            }
        }

        for (int i = 0; i < 12; i++) {
            Line line = new Line();
            line.setStartX(0f);
            line.setStartY(i * Constants.ROW_HEIGHT_TOTAL + Constants.ROW_HEIGHT_NOTES);
            line.setEndX((float)Constants.WIDTH_DEFAULT);
            line.setEndY(i * Constants.ROW_HEIGHT_TOTAL + Constants.ROW_HEIGHT_NOTES);
            line.setStrokeWidth(2);
            lineBG.add(line);

            Line line2 = new Line();
            line2.setStartX(0f);
            line2.setStartY(i * Constants.ROW_HEIGHT_TOTAL + Constants.ROW_HEIGHT_NOTES + Constants.ROW_HEIGHT_VOL + 2);
            line2.setEndX((float)Constants.WIDTH_DEFAULT);
            line2.setEndY(i * Constants.ROW_HEIGHT_TOTAL + Constants.ROW_HEIGHT_NOTES + Constants.ROW_HEIGHT_VOL + 2);
            line2.setStrokeWidth(2);
            lineBG.add(line2);
        }
    }

    private void initDraw(Song song) {
        pane = new Pane();
        pane.getChildren().addAll(compositionBG);
        pane.getChildren().addAll(lineBG);
        pane.getChildren().addAll(measureNum);

        if (song != null) {
            //put the song's imageview representation into pane
            this.song = song;
            drawSong(song);
        }
        content = new Group(pane);
        setContent(content);
    }

    /**
     * Draw the song if it hasn't been done so already. Call drawSong() after
     * drawing the bg to draw the contents of a song. This can either be called
     * upon passing the song via constructor or passing the song after a new
     * CompositionPane has been declared with no song and song is later loaded
     * via openSong() in global.Functions.
     *
     * @param song passed in via constructor or from the song that gets opened
     * by openSong() in global.Functions
     */
    public void drawSong(Song song) {

        for (int i = 0; i < song.staff.size(); i++) {
            MeasureLine m = song.staff.get(i);
            
            Line vol = new Line();
            vol.setStartX(Constants.EDGE_MARGIN + Constants.LINE_SPACING_OFFSET_X + (i % Constants.LINES_IN_A_ROW) * Constants.LINE_SPACING);
            vol.setStartY((i / Constants.LINES_IN_A_ROW) * Constants.ROW_HEIGHT_TOTAL + Constants.ROW_HEIGHT_NOTES + Constants.ROW_HEIGHT_VOL + 1);
            vol.setEndX(Constants.EDGE_MARGIN + Constants.LINE_SPACING_OFFSET_X + (i % Constants.LINES_IN_A_ROW) * Constants.LINE_SPACING);
            vol.setEndY((i / Constants.LINES_IN_A_ROW) * Constants.ROW_HEIGHT_TOTAL + Constants.ROW_HEIGHT_NOTES + Constants.ROW_HEIGHT_VOL + 1 - m.getVolume() / 2);
            vol.setStroke(Color.GREEN);
            vol.setStrokeWidth(4);
            this.compositionVol.add(vol);
            
            Text volText = new Text(Constants.EDGE_MARGIN + Constants.LINE_SPACING_OFFSET_X + (i % Constants.LINES_IN_A_ROW) * Constants.LINE_SPACING,
                    (i / Constants.LINES_IN_A_ROW) * Constants.ROW_HEIGHT_TOTAL + Constants.ROW_HEIGHT_NOTES + Constants.ROW_HEIGHT_VOL + 1,
                    "" + m.getVolume());
            this.compositionVolText.add(volText);
            
            pane.getChildren().add(vol);
            pane.getChildren().add(volText);
            if(m.measureLine.isEmpty()){
                vol.setVisible(false);
                volText.setVisible(false);
            }
            
            for (Note n : m.measureLine) {



                ImageIndex imageIndex = ImageIndex.valueOf(n.getInstrument().toString());
                ImageView iv = Variables.imageLoader.getImageView(imageIndex);
                iv.setTranslateX(Constants.EDGE_MARGIN + Constants.LINE_SPACING_OFFSET_X + (i % Constants.LINES_IN_A_ROW) * Constants.LINE_SPACING - 16);
                iv.setTranslateY(16 + (i / Constants.LINES_IN_A_ROW) * Constants.ROW_HEIGHT_TOTAL + zdy(n.getPosition()) - 11);
                iv.setSmooth(false);
                ImageView[] ivArray = new ImageView[2];
                ivArray[0] = iv;
                this.composition.put(n, ivArray);

                zMod(iv, n);            
                
                pane.getChildren().add(ivArray[0]);
                if(ivArray[1] != null)
                    pane.getChildren().add(ivArray[1]);
            }
        }
    }
    
    //song data gets modified then call redraw...
//    public void redrawSong() {
//        this.composition.clear();
//        this.pane.getChildren().clear();
//        drawSong(song);
//    }

    private void zoomOut() {
//        System.out.println("zoom Out");
        if (scale > 2) {
            scale--;
        }
        double scaleVal = (double) scale / SCALE_DEFAULT;
        System.out.println("GBIP" + pane.getBoundsInParent().getMinX());

        //Apply zoom to all tabs currently selected in their respective windows
        if (TabDraggable.tabPanes.isEmpty()) {
            pane.setScaleX(scaleVal);
            pane.setScaleY(scaleVal);
            pane.setTranslateX(0);
            pane.setTranslateX(-(int) pane.getBoundsInParent().getMinX());
            pane.setTranslateY(0);
            pane.setTranslateY(-(int) pane.getBoundsInParent().getMinY());
        } else {
            for (TabPane tabPane : TabDraggable.tabPanes) {

                Pane paneSel = ((CompositionPane) tabPane.getSelectionModel().getSelectedItem().getContent()).getPane();

                paneSel.setScaleX(scaleVal);
                paneSel.setScaleY(scaleVal);

                paneSel.setTranslateX(0);
                paneSel.setTranslateX(-(int) paneSel.getBoundsInParent().getMinX());
                paneSel.setTranslateY(0);
                paneSel.setTranslateY(-(int) paneSel.getBoundsInParent().getMinY());
            }
        }
    }

    private void zoomIn() {
//        System.out.println("zoom In");
        if (scale < 20) {
            scale++;
        }
        double scaleVal = (double) scale / SCALE_DEFAULT;
        System.out.println("GBIP" + pane.getBoundsInParent().getMinX());
        //Apply zoom to all tabs currently selected in their respective windows
        if (TabDraggable.tabPanes.isEmpty()) {
            pane.setScaleX(scaleVal);
            pane.setScaleY(scaleVal);
            pane.setTranslateX(0);
            pane.setTranslateX(-(int) pane.getBoundsInParent().getMinX());
            pane.setTranslateY(0);
            pane.setTranslateY(-(int) pane.getBoundsInParent().getMinY());
        } else {
            for (TabPane tabPane : TabDraggable.tabPanes) {

                Pane paneSel = ((CompositionPane) tabPane.getSelectionModel().getSelectedItem().getContent()).getPane();

                paneSel.setScaleX(scaleVal);
                paneSel.setScaleY(scaleVal);

                paneSel.setTranslateX(0);
                paneSel.setTranslateX(-(int) paneSel.getBoundsInParent().getMinX());
                paneSel.setTranslateY(0);
                paneSel.setTranslateY(-(int) paneSel.getBoundsInParent().getMinY());
            }
        }
    }

    private int zdy(Note.Position p) {
        return 16 * p.ordinal();
    }

    private void zMod(ImageView iv, Note n) {
        switch (n.getModifier()) {
            case SHARP:
            case FLAT:
            case DOUBLESHARP:
            case DOUBLEFLAT:
                zModHalfStep(iv, n);
            case NONE:
                //nothing changes
                break;
            case STACCATO:
//                ImageIndex imageIndex = ImageIndex.valueOf(n.getInstrument().toString()
//                        + "_SIL");
                Rectangle2D rect = iv.getViewport();
                iv.setViewport(new Rectangle2D(rect.getMinX(),
                        rect.getMinY() + 72, rect.getWidth(), rect.getHeight()));
        }
    }

    private void zModHalfStep(ImageView iv, Note n) {
        Note.Modifier m = n.getModifier();
        ImageIndex imageIndex = ImageIndex.valueOf(m.toString());
        ImageView hs = Variables.imageLoader.getImageView(imageIndex);
        hs.setTranslateX(iv.getTranslateX() - 32);
        hs.setTranslateY(iv.getTranslateY());
        
        ImageView[] ivArray = composition.get(n);
        ivArray[1] = hs;
//        staff.add(hs);
    }

    /**
     * @return pane for zooming calls
     * @see #zoomOut()
     * @see #zoomIn()
     */
    public Pane getPane() {
        return pane;
    }
    
    public Song getSong() {
        return song;
    }

    @Override
    public Node getNode() {
        return this;
    }

    @Override
    public void keyControl(KeyCode key, boolean altDown, boolean ctrlDown, boolean shiftDown) {
        switch (key) {
            case LEFT:
//                getScrollBarH().decrement();
//                System.out.println(this.getHvalue());
                this.setHvalue(this.getHvalue() - 0.05);
                break;
            case RIGHT:
//                getScrollBarH().increment();
                this.setHvalue(this.getHvalue() + 0.05);
                break;
            case UP:
//                getScrollBarV().decrement();
                this.setVvalue(this.getVvalue() - 0.01);
                break;
            case DOWN:
//                getScrollBarV().increment();
                this.setVvalue(this.getVvalue() + 0.01);
                break;
            case PAGE_UP:
                for (int i = 0; i < this.heightProperty().intValue(); i += 50) {
//                    getScrollBarV().decrement();
                    this.setVvalue(this.getVvalue() - 0.01);
                }
                break;
            case PAGE_DOWN:
                for (int i = 0; i < this.heightProperty().intValue(); i += 50) {
//                    getScrollBarV().increment();
                    this.setVvalue(this.getVvalue() + 0.01);
                }
                break;
            case MINUS:
                if (ctrlDown) {
                    zoomOut();
                }
                break;
            case PLUS:
            case EQUALS:
                if (ctrlDown) {
                    zoomIn();
                }
                break;
            default:
        }
    }

    @Override
    public String getTitle() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sharedBehavior() {
        sharedZoom();
    }

    private void sharedZoom() {

        //Apply zoom to the tab selected just now
        while (scale < scaleShared) {
            zoomIn();
        }
        while (scale > scaleShared) {
            zoomOut();
        }
    }

    @Override
    public void constructedBehavior() {
        sharedZoom();
    }
    
    public void removeNote(int line, Note.Position position){
        System.out.println(position);
        Note n = song.staff.get(line).removeNote(position.ordinal());
        if(n != null){
            pane.getChildren().remove(composition.get(n)[0]);
            if(composition.get(n)[1] != null)
                pane.getChildren().remove(composition.get(n)[1]);
            composition.remove(n);
        }
        if(song.staff.get(line).measureLine.isEmpty()){
//            System.out.println("SRSLY?" +compositionVol.get(line) );
            compositionVol.get(line).setVisible(false);
            compositionVolText.get(line).setVisible(false);
        }
    }
    
    public void removeNote(int line, Note n){
//        System.out.println(n.getPosition());
//remove all... seems to have same behavior
//List<ImageView> ivArr = new ArrayList<>();
//for(Note n : notes){
//    ivArr.add(composition.get(n)[0]);
//    if(composition.get(n)[1] != null)
//        ivArr.add(composition.get(n)[1]);
//}
//pane.getChildren().removeAll(ivArr);
        song.staff.get(line).measureLine.remove(n);
        if(n != null){
            pane.getChildren().remove(composition.get(n)[0]);
            if(composition.get(n)[1] != null)
                pane.getChildren().remove(composition.get(n)[1]);
            composition.remove(n);
        }
        if(song.staff.get(line).measureLine.isEmpty()){
//            System.out.println("SRSLY?" +compositionVol.get(line) );
            compositionVol.get(line).setVisible(false);
            compositionVolText.get(line).setVisible(false);
        }
    }
    
    public void addNote(int line, Note.Instrument instrument, Note.Position position, Note.Modifier modifier){
        System.out.println(position + " " + instrument);
        Note n = new Note(instrument, position, modifier);
        if(song.staff.get(line).addNote(n)){
            //TODO: add new note imageview...
            ImageIndex imageIndex = ImageIndex.valueOf(n.getInstrument().toString());
            ImageView iv = Variables.imageLoader.getImageView(imageIndex);
            iv.setTranslateX(Constants.EDGE_MARGIN + Constants.LINE_SPACING_OFFSET_X + (line % Constants.LINES_IN_A_ROW) * Constants.LINE_SPACING - 16);
            iv.setTranslateY(16 + (line / Constants.LINES_IN_A_ROW) * Constants.ROW_HEIGHT_TOTAL + zdy(n.getPosition()) - 11);
            ImageView[] ivArray = new ImageView[2];
            ivArray[0] = iv;
            this.composition.put(n, ivArray);

            zMod(iv, n);            

            redrawLine(line);
//            pane.getChildren().add(ivArray[0]);
//            if(ivArray[1] != null)
//                pane.getChildren().add(ivArray[1]);
        }
        else {
            song.staff.get(line).bringNoteToFront(n);
            
            redrawLine(line);
        }
    }
    
    /**
     * Should be called whenever a new note is added to the composition hashmap
     * to properly organize the notes. New note should be added to the song and
     * composition before calling this. First remove all note ImageViews on that
     * line from the pane. Then put all note ImageViews on that line into the
     * pane again.
     *
     * @param line at which new note is placed
     */
    public void redrawLine(int line){
        for(Note n : song.staff.get(line).measureLine){
            ImageView[] ivArray = this.composition.get(n);
            
            pane.getChildren().remove(ivArray[0]);
            if(ivArray[1] != null)
                pane.getChildren().remove(ivArray[1]);
        }
        
        for(Note n : song.staff.get(line).measureLine){
            ImageView[] ivArray = this.composition.get(n);
            
            pane.getChildren().add(ivArray[0]);
            if(ivArray[1] != null)
                pane.getChildren().add(ivArray[1]);
        }
        
        compositionVol.get(line).setVisible(true);
        compositionVolText.get(line).setVisible(true);
    }
    
    /**
     * Should be called whenever a new note is added or a line is modified in
     * the song object THEN redrawLine should happen subsequently. This will
     * compare the notes in composition hashmap and the notes in the song.
     * Updates composition to include the notes that have been added or removed
     * and adds or removes respective imageViews.
     *
     * @param line at which notes have changed
     */
    public void reloadLine(int line) {
        for(Note n : song.staff.get(line).measureLine){
            if(!this.composition.containsKey(n)) {
                ImageIndex imageIndex = ImageIndex.valueOf(n.getInstrument().toString());
                ImageView iv = Variables.imageLoader.getImageView(imageIndex);
                iv.setTranslateX(Constants.EDGE_MARGIN + Constants.LINE_SPACING_OFFSET_X + (line % Constants.LINES_IN_A_ROW) * Constants.LINE_SPACING - 16);
                iv.setTranslateY(16 + (line / Constants.LINES_IN_A_ROW) * Constants.ROW_HEIGHT_TOTAL + zdy(n.getPosition()) - 11);
                ImageView[] ivArray = new ImageView[2];
                ivArray[0] = iv;
                this.composition.put(n, ivArray);

                zMod(iv, n);
            }
        }
    }
    
    /**
     * Create a shadow around the note image for the note passed in.
     * 
     * @param n Note passed in
     * @param highlight flag to create the highlight effect
     */
    private List<Note> highlightedNotes = new ArrayList<>();
    public void highlightNote(Note n, boolean highlight){
        ImageView[] ivArray = this.composition.get(n);
        Variables.imageLoader.setImageHighlight(ivArray[0], highlight);
        if (ivArray[1] != null) {
            Variables.imageLoader.setImageHighlight(ivArray[1], highlight);
        }
        if(highlight) {
            highlightedNotes.add(n);
        }
    }
    
    public void unhighlightAllNotes() {
        for(Note n : highlightedNotes) {
            ImageView[] ivArray = this.composition.get(n);
            
            if(ivArray != null){
                Variables.imageLoader.setImageHighlight(ivArray[0], false);
                if(ivArray[1] != null) {
                    Variables.imageLoader.setImageHighlight(ivArray[1], false);
                }
            }
        }
        highlightedNotes.clear();
    }
    
    public void setVolume(int line, int volume) {
        song.staff.get(line).setVolume(volume);
        compositionVol.get(line).setEndY((line / Constants.LINES_IN_A_ROW) * Constants.ROW_HEIGHT_TOTAL + Constants.ROW_HEIGHT_NOTES + Constants.ROW_HEIGHT_VOL + 1 - volume / 2);
        compositionVolText.get(line).setText("" + volume);
    }
}
