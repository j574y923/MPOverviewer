package mpoverviewer.image_layer.tabcontent;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
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
 * @author J
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

    private volatile boolean contFlag;
    private Task task;
    private Timeline animation;

    /**
     *
     * @param rubberBand is already created
     */
//    public EventHandlerRubberBand(RectangleRubberBand rubberBand) {
//        rubberBand = rubberBand;
////        keyControlRef = (CompositionPaneSP) rubberBand
////                .getParent()//pane
////                .getParent()//group
////                .getParent()//scrollskin$4 (?)
////                .getParent()//scrollskin$3 (?)
////                .getParent();//compositionpane
//    }
    /**
     *
     * @param child contained in parent, this will be where rubber band is added
     * @param parent is the layout container for child and needed for retrieving
     * scene dimensions
     */
    public EventHandlerRubberBand(Pane child, ScrollPane parent) {
        rubberBand = new RectangleRubberBand();
//        Pane test = new Pane();
//        test.getChildren().add(rubberBand);
//        test.addEventHandler(MouseEvent.ANY, this);
//        child.getChildren().add(test);
        child.getChildren().add(rubberBand);
        this.scrollPane = parent;
//        scrollPane.getChildren().add(rubberBand);
//        this.scrollPane = scrollPane;
//        keyControlRef = (CompositionPaneSP) pane
//                .getParent()//group
//                .getParent()//scrollskin$4 (?)
//                .getParent()//scrollskin$3 (?)
//                .getParent();//compositionpane

        /* rubber band resize if scrollbars change */
        this.scrollPane.hvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (contFlag) {
                    rubberBand.resizeX(newValue.doubleValue() * 2216 + scrollPane.localToScene(scrollPane.getBoundsInLocal()).getMaxX());
                }
            }

        });
        this.scrollPane.vvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (contFlag) {
                    rubberBand.resizeY(newValue.doubleValue() * 4366 + scrollPane.localToScene(scrollPane.getBoundsInLocal()).getMaxY());
                }
            }

        });
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        contFlag = false;
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
            rubberBand.begin(mouseEvent.getX(), mouseEvent.getY());
//                    System.out.println("BEG"+ mouseEvent.getX());
        } else if (mouseEvent.isPrimaryButtonDown()) {
            rubberBand.resize(mouseEvent.getX(), mouseEvent.getY());
//                    System.out.println(mouseEvent.getX());
//            System.out.println(mouseEvent.getSceneX());
            navigatePane(mouseEvent);
        } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
            rubberBand.end();
            if (animation != null) {
                animation = new Timeline(
                        new KeyFrame(Duration.seconds(1),
                                new KeyValue(scrollPane.hvalueProperty(), scrollPane.hvalueProperty().get()),
                                new KeyValue(scrollPane.vvalueProperty(), scrollPane.vvalueProperty().get())));
                animation.play();
            }
        }
    }

    private void navigatePane(MouseEvent mouseEvent) {
        if (mouseEvent.getSceneX() >= scrollPane.localToScene(scrollPane.getBoundsInLocal()).getMaxX() - MARGIN) {
            scrollPane.setHvalue(scrollPane.getHvalue() + 0.01);
        } else if (mouseEvent.getSceneX() <= scrollPane.localToScene(scrollPane.getBoundsInLocal()).getMinX() + MARGIN) {
            scrollPane.setHvalue(scrollPane.getHvalue() - 0.01);
        }

        if (mouseEvent.getSceneY() >= scrollPane.localToScene(scrollPane.getBoundsInLocal()).getMaxY() - MARGIN) {
            scrollPane.setVvalue(scrollPane.getVvalue() + 0.005);
//            System.out.println("A" + mouseEvent.getSceneY() + "B" + scrollPane.localToScene(scrollPane.getBoundsInLocal()).getMaxY());
        } else if (mouseEvent.getSceneY() <= scrollPane.localToScene(scrollPane.getBoundsInLocal()).getMinY() + MARGIN) {
            scrollPane.setVvalue(scrollPane.getVvalue() - 0.005);
        }

        navPaneContinuous(mouseEvent.getSceneX(), mouseEvent.getSceneY());
    }

    //Exception in thread "JavaFX Application Thread" java.lang.ArrayIndexOutOfBoundsException this is madness
    private void navPaneContinuous(double sceneX, double sceneY) {
        if (sceneX >= scrollPane.localToScene(scrollPane.getBoundsInLocal()).getMaxX() - MARGIN && sceneX < 2216) {
            if (contFlag) {
                return;
            }
            contFlag = true;
            /**
             * Credit to https://stackoverflow.com/questions/41723813/how-to-make-javafx-scrollpane-autoscroll-slowly-to-the-bottom
             */
            animation = new Timeline(
                new KeyFrame(Duration.seconds(2),
                    new KeyValue(scrollPane.hvalueProperty(), 1),
                    new KeyValue(scrollPane.vvalueProperty(), 1)));
            animation.play();
        }
        else {
//            System.out.println("STOP");
            if(animation != null){
                animation = new Timeline(
                    new KeyFrame(Duration.seconds(1),
                        new KeyValue(scrollPane.hvalueProperty(), scrollPane.hvalueProperty().get()),
                        new KeyValue(scrollPane.vvalueProperty(), scrollPane.vvalueProperty().get())));
                animation.play();
            }
        }
    }
}
