package mpoverviewer.image_layer.tabcontent;

import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

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

    private static final double MARGIN = 4;

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
        child.getChildren().add(rubberBand);
        this.scrollPane = parent;
//        scrollPane.getChildren().add(rubberBand);
//        this.scrollPane = scrollPane;
//        keyControlRef = (CompositionPaneSP) pane
//                .getParent()//group
//                .getParent()//scrollskin$4 (?)
//                .getParent()//scrollskin$3 (?)
//                .getParent();//compositionpane
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
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
    }
}
