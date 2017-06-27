package mpoverviewer.image_layer.tabcontent;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Event Handler for rubber band which follows mouse movements. Mouse moves and
 * the rubber band will adjust. Move to the edges of the pane and the pane will
 * readjust position.
 *
 * @author j574y923
 */
public class EventHandlerRubberBand implements EventHandler<MouseEvent> {

    RectangleRubberBand rubberBand;
    ScrollPane scrollPane;

    /**
     * Margin at the edge of the scrollpane. When a rectangle is being created
     * and the mouse resizes into this margin, the scrollpane will scroll to
     * accommodate resizing.
     */
    private static final double MARGIN = 4;

    private volatile boolean autoFlagH;
    private volatile boolean autoFlagV;
    private Timeline autoAnimationH;
    private Timeline autoAnimationV;

    /**
     *
     * @param child contained in parent, this will be where rubber band is added
     * @param parent is the layout container for child and needed for retrieving
     * scene dimensions
     */
    public EventHandlerRubberBand(Pane child, ScrollPane parent) {
        rubberBand = new RectangleRubberBand();
        child.getChildren().add(rubberBand);
        this.scrollPane = parent;
        /* rubber band resize if scrollbars change */
        this.scrollPane.hvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (autoFlagH) {
                    if(newValue.doubleValue() > oldValue.doubleValue())
                        rubberBand.resizeX(newValue.doubleValue() * 2216 + scrollPane.localToScene(scrollPane.getBoundsInLocal()).getMaxX());
                    else
                        rubberBand.resizeX(newValue.doubleValue() * 2216);//??
                }
            }

        });
        this.scrollPane.vvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (autoFlagV) {
                    rubberBand.resizeY(newValue.doubleValue() * 4366 + scrollPane.localToScene(scrollPane.getBoundsInLocal()).getMaxY());
                }
            }

        });
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
            rubberBand.begin(mouseEvent.getX(), mouseEvent.getY());
        } else if (mouseEvent.isPrimaryButtonDown()) {
            rubberBand.resize(mouseEvent.getX(), mouseEvent.getY());
            navigatePane(mouseEvent);
        } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
            rubberBand.end();
            autoFlagH = false;
            autoFlagV = false;
        }
    }

    private void navigatePane(MouseEvent mouseEvent) {
        int destX = 0;
        boolean enableX = false;
        if (mouseEvent.getSceneX() >= scrollPane.localToScene(scrollPane.getBoundsInLocal()).getMaxX() - MARGIN) {
            scrollPane.setHvalue(scrollPane.getHvalue() + 0.01);
            destX = 1;
            enableX = true;
        } else if (mouseEvent.getSceneX() <= scrollPane.localToScene(scrollPane.getBoundsInLocal()).getMinX() + MARGIN) {
            scrollPane.setHvalue(scrollPane.getHvalue() - 0.01);
            destX = 0;
            enableX = true;
        } else {
            autoFlagH = false;
            enableX = false;
        }

        int destY = 0;
        boolean enableY = false;
        if (mouseEvent.getSceneY() >= scrollPane.localToScene(scrollPane.getBoundsInLocal()).getMaxY() - MARGIN) {
            scrollPane.setVvalue(scrollPane.getVvalue() + 0.005);
            destY = 1;
            enableY = true;
        } else if (mouseEvent.getSceneY() <= scrollPane.localToScene(scrollPane.getBoundsInLocal()).getMinY() + MARGIN) {
            scrollPane.setVvalue(scrollPane.getVvalue() - 0.005);
            destY = 0;
            enableY = true;
        } else {
            autoFlagV = false;
            enableY = false;
        }

        autoScrollH(enableX, destX);
        autoScrollV(enableY, destY);
    }

    private void autoScrollH(boolean enable, int destX) {
        if (!enable) {
            return;
        }
        if (autoFlagH) {
            return;
        }
        autoFlagH = true;
        /**
         * Credit to
         * https://stackoverflow.com/questions/41723813/how-to-make-javafx-scrollpane-autoscroll-slowly-to-the-bottom
         */
        autoAnimationH = new Timeline(
                new KeyFrame(Duration.seconds(zdh(destX)),
                        new KeyValue(scrollPane.hvalueProperty(), destX)));
        autoAnimationH.play();

        Thread test = new Thread() {
            @Override
            public void run() {
                while (autoFlagH) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(EventHandlerRubberBand.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                autoAnimationH.stop();
            }
        };
        test.start();
    }

    private void autoScrollV(boolean enable, int destY) {        
        if (!enable) {
            return;
        }
        if (autoFlagV) {
            return;
        }
        autoFlagV = true;

        autoAnimationV = new Timeline(
                new KeyFrame(Duration.seconds(zdv(destY)),
                        new KeyValue(scrollPane.vvalueProperty(), destY)));
        autoAnimationV.play();

        Thread test = new Thread() {
            @Override
            public void run() {
                while (autoFlagV) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(EventHandlerRubberBand.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                autoAnimationV.stop();
            }
        };
        test.start();
    }
    
    private double zdh(int destX){
        if(destX == 1)
            return 2 - scrollPane.hvalueProperty().get() * 2;
        else
            return scrollPane.hvalueProperty().get() * 2;
    }    
    
    private double zdv(int destY){
        if(destY == 1)
            return 2 - scrollPane.hvalueProperty().get() * 2;
        else
            return scrollPane.hvalueProperty().get() * 2;
    }
}
