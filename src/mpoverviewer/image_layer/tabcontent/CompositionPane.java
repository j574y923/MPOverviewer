package mpoverviewer.image_layer.tabcontent;

import java.util.ArrayList;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import mpoverviewer.data_layer.data.Measure;
import mpoverviewer.data_layer.data.Note;
import mpoverviewer.data_layer.data.Song;
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
    private static final int HEIGHT_DEFAULT = 4366;
    private static final int WIDTH_DEFAULT = 2216;
    private int scale = 10;
    private static final int SCALE_DEFAULT = 10;
    private static int scaleShared = 10;

    private ScrollBar scrollBarH = null;
    private ScrollBar scrollBarV = null;

    private final List<ImageView> compositionBG = new ArrayList<>();
    private final List<Line> lineBG = new ArrayList<>();
    private final List<Text> measureNum = new ArrayList<>();

    private List<ImageView> composition;
    private List<Line> compositionVol;

//    private static RectangleRubberBand region;
//    private static double regionMinX;
//    private static double regionMinY;
//    private static double regionMaxX;
//    private static double regionMaxY;
    public CompositionPane(Song song) {
        super();
        initBG();
        compositionVol = new ArrayList<>();
        composition = new ArrayList<>();
        initDraw(song);

        pane.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.isControlDown()) {
                    if (event.getDeltaY() < 0) {
                        zoomOut();
//                        keyControl(KeyCode.UP);
                    } else {
                        zoomIn();

//                        keyControl(KeyCode.UP);
                    }
                    scaleShared = scale;
                    event.consume();
                }
            }
        });

//        sharedZoom();
        //OPTIMIZATION: do this when switching away to another tab??
//        pane.getChildren().clear();
//        composition.clear();
//        compositionVol.clear();
//        regionMinX = -1;
//        regionMinY = -1;
//        regionMaxX = -1;
//        regionMaxY = -1;
//        region = new RectangleRubberBand();
//        RectangleRubberBand.makeResizable(region);
//        region.setVisible(false);
//        pane.getChildren().add(region);
        pane.addEventHandler(MouseEvent.ANY, new EventHandlerRubberBand(pane, this));
    }

    @Override
    public void cleanUp() {
//        pane.getChildren().clear();
//        composition.clear();
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
            iv.setTranslateX((i % 22) * 100 + 8);
            iv.setTranslateY((i / 22) * 364 + 16);
            compositionBG.add(iv);
        }

        for (int i = 0; i < 12; i++) {
            ImageView iv = Variables.imageLoader.getImageView(ImageIndex.TREBLE_CLEF_MPC);
            iv.setTranslateX(8);
            iv.setTranslateY(i * 364 + 16);
            compositionBG.add(iv);
            for (int j = 0; j < 8; j++) {
                Text t = new Text(8 + 144 + j * 256, i * 364 + 16 - 4, "" + (1 + i * 8 + j));
                measureNum.add(t);

                ImageView iv1 = Variables.imageLoader.getImageView(ImageIndex.STAFF_MLINE);
                iv1.setTranslateX(8 + 144 + j * 256);
                iv1.setTranslateY(i * 364 + 16);
                compositionBG.add(iv1);

                ImageView iv2 = Variables.imageLoader.getImageView(ImageIndex.STAFF_LINE);
                iv2.setTranslateX(8 + 144 + j * 256 + 64);
                iv2.setTranslateY(i * 364 + 16);
                compositionBG.add(iv2);

                ImageView iv3 = Variables.imageLoader.getImageView(ImageIndex.STAFF_LINE);
                iv3.setTranslateX(8 + 144 + j * 256 + 128);
                iv3.setTranslateY(i * 364 + 16);
                compositionBG.add(iv3);

                ImageView iv4 = Variables.imageLoader.getImageView(ImageIndex.STAFF_LINE);
                iv4.setTranslateX(8 + 144 + j * 256 + 192);
                iv4.setTranslateY(i * 364 + 16);
                compositionBG.add(iv4);
            }
        }

        for (int i = 0; i < 12; i++) {
            Line line = new Line();
            line.setStartX(0f);
            line.setStartY(i * 364 + 296);
            line.setEndX(2216f);
            line.setEndY(i * 364 + 296);
            line.setStrokeWidth(2);
            lineBG.add(line);

            Line line2 = new Line();
            line2.setStartX(0f);
            line2.setStartY(i * 364 + 296 + 64 + 2);
            line2.setEndX(2216f);
            line2.setEndY(i * 364 + 296 + 64 + 2);
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

        for (int i = 0; i < song.composition.size(); i++) {
            Measure m = song.composition.get(i);
            for (Note n : m.measure) {

                Line vol = new Line();
                vol.setStartX(8 + 144 + (i % 32) * 64);
                vol.setStartY((i / 32) * 364 + 296 + 64 + 1);
                vol.setEndX(8 + 144 + (i % 32) * 64);
                vol.setEndY((i / 32) * 364 + 296 + 64 + 1 - m.getVolume() / 2);
                vol.setStroke(Color.GREEN);
                vol.setStrokeWidth(4);
                this.compositionVol.add(vol);

                ImageIndex imageIndex = ImageIndex.valueOf(n.getInstrument().toString());
                ImageView iv = Variables.imageLoader.getImageView(imageIndex);
                iv.setTranslateX(8 + 144 + (i % 32) * 64 - 16);
                iv.setTranslateY(16 + (i / 32) * 364 + zdy(n.getPosition()) - 11);
                iv.setSmooth(false);
                this.composition.add(iv);

                zMod(iv, n);
            }
        }

        pane.getChildren().addAll(compositionVol);
        pane.getChildren().addAll(composition);
    }

    //public void redraw(){}//song data gets modified then call redraw...
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
                zModHalfStep(iv, n.getModifier());
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

    private void zModHalfStep(ImageView iv, Note.Modifier m) {
        ImageIndex imageIndex = ImageIndex.valueOf(m.toString());
        ImageView hs = Variables.imageLoader.getImageView(imageIndex);
        hs.setTranslateX(iv.getTranslateX() - 32);
        hs.setTranslateY(iv.getTranslateY());
        composition.add(hs);
    }

    private ScrollBar getScrollBarH() {
        if (scrollBarH == null) {
            searchSB();
        }
        return scrollBarH;
    }

    private ScrollBar getScrollBarV() {
        if (scrollBarV == null) {
            searchSB();
        }
        return scrollBarV;
    }

    private void searchSB() {
        Set<Node> nodes = this.lookupAll(".scroll-bar");
        for (Node node : nodes) {
            System.out.println(node);
            if (node instanceof ScrollBar) {
                ScrollBar sb = (ScrollBar) node;
                if (sb.getOrientation() == Orientation.HORIZONTAL) {
                    System.out.println("horizontal scrollbar visible = " + sb.isVisible());
                    System.out.println("width = " + sb.getWidth());
                    System.out.println("height = " + sb.getHeight());
                    System.out.println("max = " + sb.getMax());
                    scrollBarH = sb;
                } else if (sb.getOrientation() == Orientation.VERTICAL) {
                    System.out.println("vertical scrollbar visible = " + sb.isVisible());
                    System.out.println("width = " + sb.getWidth());
                    System.out.println("height = " + sb.getHeight());
                    System.out.println("max = " + sb.getMax());
                    scrollBarV = sb;
                }
            }
        }
    }

    /**
     * @return pane for zooming calls
     * @see #zoomOut()
     * @see #zoomIn()
     */
    public Pane getPane() {
        return pane;
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
}
