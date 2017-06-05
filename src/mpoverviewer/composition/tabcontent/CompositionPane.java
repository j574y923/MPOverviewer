package mpoverviewer.composition.tabcontent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import mpoverviewer.composition.data.Measure;
import mpoverviewer.composition.data.Note;
import mpoverviewer.composition.data.Song;
import mpoverviewer.global.Variables;
import mpoverviewer.image.ImageIndex;

/**
 *
 * @author J
 */
public class CompositionPane extends Pane {

    private Pane pane;
    private final ScrollBar scrollBarH = new ScrollBar();
    private final ScrollBar scrollBarV = new ScrollBar();
    private static final int SCROLLBAR_THICKNESS = 15;
    private static final int HEIGHT_DEFAULT = 4366;
    private static final int WIDTH_DEFAULT = 2216;
    private int scale = 10;
    private static final int SCALE_DEFAULT = 10;
    
    private double hMax;
    private double vMax;
    private int zoomOffsetX=0;
    private int zoomOffsetY=0;

    private final List<ImageView> compositionBG = new ArrayList<>();
    private final List<Line> lineBG = new ArrayList<>();
    private final List<Text> measureNum = new ArrayList<>();

    private List<ImageView> composition;

    public CompositionPane(Song song) {
        super();
        initBG();
        composition = new ArrayList<>();
        drawSong(song);

        initScrollBars();
        this.getChildren().addAll(scrollBarH, scrollBarV);
//        this.setScaleX(0.5);
//        this.setScaleY(0.5);
//        this.setTranslateX(-300);     
//        this.setTranslateY(this.getScene().getHeight()/4);
//        this.setFitToHeight(true);
//        this.setFitToWidth(true);
//        this.setMinSize(2000, 768);
//        this.setPrefSize(1000, 1000);
//        this.setMaxSize(1000,1000);
//this.setPrefViewportHeight(2183);

        pane.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.isControlDown()) {
                    if (event.getDeltaY() < 0) {
                        zoomOut();
                    } else {
                        zoomIn();
                    }
                } else if (event.isShiftDown()) {
                    scrollH(event.getDeltaX());
                } else {
                    scrollV(event.getDeltaY());
                }
            }
        });

        this.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
//                System.out.println("H: OLD" + oldSceneHeight + "NEW" + newSceneHeight);

                scrollBarH.setTranslateY((double) newSceneHeight - SCROLLBAR_THICKNESS);

                scrollBarV.setPrefHeight((double) newSceneHeight);
//                scrollBarV.setVisibleAmount((double) newSceneHeight);// * (double) newSceneHeight
//                        / (HEIGHT_DEFAULT * scale / SCALE_DEFAULT));
//                System.out.println((double) newSceneHeight * (double) newSceneHeight
//                        / (HEIGHT_DEFAULT * scale / SCALE_DEFAULT));
                vMax = HEIGHT_DEFAULT * scale / SCALE_DEFAULT - (double) newSceneHeight + SCROLLBAR_THICKNESS + 1;
                scrollBarV.setMax(vMax);
                scrollBarV.setVisibleAmount(vMax * (double) newSceneHeight / HEIGHT_DEFAULT);
                
                pane.setTranslateY(-scrollBarV.getValue() + zoomOffsetY);
                if(-pane.getTranslateY() > vMax)
                    pane.setTranslateY(-vMax);
//                pane.setTranslateY(-scrollBarV.getValue());
//                System.out.println(-scrollBarV.getValue());
            }
        });
        this.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
//                System.out.println("W: OLD" + oldSceneWidth + "NEW" + newSceneWidth);

                scrollBarH.setPrefWidth((double) newSceneWidth - SCROLLBAR_THICKNESS);
//                System.out.println(scrollBarH.getMax());
                hMax = WIDTH_DEFAULT * scale / SCALE_DEFAULT - (double) newSceneWidth + SCROLLBAR_THICKNESS + 1;
                scrollBarH.setMax(hMax);
                scrollBarH.setVisibleAmount(hMax * (double) newSceneWidth / WIDTH_DEFAULT); 
                pane.setTranslateX(-scrollBarH.getValue() + zoomOffsetX);
                if(-pane.getTranslateX() > hMax)
                    pane.setTranslateX(-hMax);

                scrollBarV.setTranslateX((double) newSceneWidth - SCROLLBAR_THICKNESS);
            }
        });
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
            lineBG.add(line);

            Line line2 = new Line();
            line2.setStartX(0f);
            line2.setStartY(i * 364 + 296 + 64 + 2);
            line2.setEndX(2216f);
            line2.setEndY(i * 364 + 296 + 64 + 2);
            lineBG.add(line2);
        }
    }

    public void drawSong(Song song) {
        pane = new Pane();
        pane.getChildren().addAll(compositionBG);
        pane.getChildren().addAll(lineBG);
        pane.getChildren().addAll(measureNum);

//        this.getChildren().addAll(SCROLLBAR_HORIZ, SCROLLBAR_VERT);
//        ScrollBar sc = new ScrollBar();
//        sc.setOrientation(Orientation.VERTICAL);
//        sc.setPrefHeight(389);
//        sc.setTranslateX(550 - 10);
//sc.setMin(0);
//sc.setMax(1);
//        this.getChildren().add(sc);
//sc.setValue(50);
//pane.getChildren().add(sc);
//System.out.println("X"+pane.getBoundsInLocal().getMaxX());
//System.out.println("Y"+pane.getHeight());
//        pane.setMaxSize(0, 0);
//        setContent(pane);
//        pane.setScaleX(0.5);
//        pane.setScaleY(0.5);
//        pane.setTranslateX(-(int)pane.getBoundsInLocal().getMaxX()/8);
//        pane.setTranslateY(-(int)pane.getBoundsInLocal().getMaxY()/8);
//        pane.setPrefSize(1100, 2183);
//        System.out.println("xx" + this.getContent().getBoundsInLocal().getMaxX());
        if (song != null) {
            //put the song's imageview representation into pane
            draw(song);
            pane.getChildren().addAll(composition);
        }
        this.getChildren().add(pane);
    }

    private void draw(Song song) {

        for (int i = 0; i < song.composition.size(); i++) {
            Measure m = song.composition.get(i);
            for (Note n : m.measure) {
                ImageIndex imageIndex = ImageIndex.valueOf(n.getInstrument().toString());
                ImageView iv = Variables.imageLoader.getImageView(imageIndex);
                iv.setTranslateX(8 + 144 + (i % 32) * 64 - 16);
                iv.setTranslateY(16 + (i / 32) * 364 + zdy(n.getPosition()) - 11);
                this.composition.add(iv);

                zMod(iv, n);
            }
        }
    }

    //public void redraw(){}//song data gets modified then call redraw...
    private void initScrollBars() {
        scrollBarH.setBlockIncrement(500);
        scrollBarH.setMax(WIDTH_DEFAULT);
        scrollBarH.setPrefHeight(SCROLLBAR_THICKNESS);
        scrollBarH.setUnitIncrement(80);
//        scrollBarH.setVisibleAmount(USE_PREF_SIZE);
        scrollBarH.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                pane.setTranslateX(-scrollBarH.getValue() + zoomOffsetX);
            }
        });
//        scrollBarH.addEventFilter(MouseEvent.ANY, new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                if (mouseEvent.isDragDetect() || mouseEvent.isPrimaryButtonDown()) {
//                    pane.setTranslateX(-scrollBarH.getValue());
//                }
//            }
//        });
        scrollBarH.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                pane.setTranslateX(-scrollBarH.getValue() + zoomOffsetX);
            }
        });

        scrollBarV.setOrientation(Orientation.VERTICAL);
        scrollBarV.setBlockIncrement(500);
        scrollBarV.setMax(HEIGHT_DEFAULT);
        scrollBarV.setPrefWidth(SCROLLBAR_THICKNESS);
        scrollBarV.setUnitIncrement(80);
//        scrollBarV.setVisibleAmount(100);
        scrollBarV.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                pane.setTranslateY(-scrollBarV.getValue() + zoomOffsetY);
            }
        });
        scrollBarV.addEventFilter(MouseEvent.ANY, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
//                if(mouseEvent.getEventType().equals(MouseEvent.MOUSE_PRESSED)){
////                    mouseEvent.getScreenY()
//                }
//                if (mouseEvent.isPrimaryButtonDown()) {
//                    pane.setTranslateY(-scrollBarV.getValue()); //
//                    scrollBarV.setValue(mouseEvent.getScreenY());
//                    System.out.println(mouseEvent.getScreenY());
//                }
            }
        });
        scrollBarV.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                pane.setTranslateY(-scrollBarV.getValue() + zoomOffsetY);
            }
        });
    }

    private void scrollH(double amount) {
//        System.out.println(scrollBarH.getValue());
        scrollBarH.setValue(scrollBarH.getValue() - amount);
        if (scrollBarH.getValue() < 0) {
            scrollBarH.setValue(0);
        } else if (scrollBarH.getValue() > hMax) {//WIDTH_DEFAULT * scale / SCALE_DEFAULT
            scrollBarH.setValue(hMax);//WIDTH_DEFAULT * scale / SCALE_DEFAULT
        }
        pane.setTranslateX(-scrollBarH.getValue() + zoomOffsetX);
    }

    private void scrollV(double amount) {
//        System.out.println(scrollBarV.getValue());
        scrollBarV.setValue(scrollBarV.getValue() - amount);
        if (scrollBarV.getValue() < 0) {
            scrollBarV.setValue(0);
        } else if (scrollBarV.getValue() > vMax) {
            scrollBarV.setValue(vMax);
        }
        pane.setTranslateY(-scrollBarV.getValue() + zoomOffsetY);
    }

    private void zoomOut() {
//        System.out.println("zoom Out");
        if (scale > 2) {
            scale--;
        }
        double scaleVal = (double) scale / SCALE_DEFAULT;
        pane.setScaleX(scaleVal);
        pane.setScaleY(scaleVal);
        double transScale = 4 / scaleVal;
//        System.out.println(transScale);
//        pane.setTranslateX(-(int) pane.getBoundsInLocal().getMaxX() / transScale);
//        pane.setTranslateY(-(int) pane.getBoundsInLocal().getMaxY() / transScale);
//        pane.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
//        pane.setPrefSize(WIDTH_DEFAULT * scaleVal, HEIGHT_DEFAULT * scaleVal);
        System.out.println("GBIP" + pane.getBoundsInParent().getMinX());
//        pane.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);

        pane.setTranslateX(0);
        zoomOffsetX = -(int) pane.getBoundsInParent().getMinX();
        pane.setTranslateX(zoomOffsetX);
        pane.setTranslateY(0);
        zoomOffsetY = -(int) pane.getBoundsInParent().getMinY();
        pane.setTranslateY(zoomOffsetY);
    }

    private void zoomIn() {
//        System.out.println("zoom In");
        if (scale < 20) {
            scale++;
        }
        double scaleVal = (double) scale / SCALE_DEFAULT;
        pane.setScaleX(scaleVal);
        pane.setScaleY(scaleVal);
//        pane.setPrefSize(WIDTH_DEFAULT * scaleVal, HEIGHT_DEFAULT * scaleVal);
        System.out.println("GBIP" + pane.getBoundsInParent().getMinX());
        pane.setTranslateX(0);
        zoomOffsetX = -(int) pane.getBoundsInParent().getMinX();
        pane.setTranslateX(zoomOffsetX);
        pane.setTranslateY(0);
        zoomOffsetY = -(int) pane.getBoundsInParent().getMinY();
        pane.setTranslateY(zoomOffsetY);
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
//                Rectangle2D rect = iv.getViewport();
//                iv.setViewport(new Rectangle2D(rect.getMinX(),
//                        rect.getMinY() + 72, rect.getWidth(), rect.getHeight()));
        }
    }

    private void zModHalfStep(ImageView iv, Note.Modifier m) {
        ImageIndex imageIndex = ImageIndex.valueOf(m.toString());
        ImageView hs = Variables.imageLoader.getImageView(imageIndex);
        hs.setTranslateX(iv.getTranslateX() - 32);
        hs.setTranslateY(iv.getTranslateY());
        composition.add(hs);
    }

}
