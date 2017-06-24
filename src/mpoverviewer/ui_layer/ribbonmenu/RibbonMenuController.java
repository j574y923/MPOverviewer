package mpoverviewer.ui_layer.ribbonmenu;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 *
 * @author J
 */
public class RibbonMenuController extends HBox {

    private static final String COLLAPSE_DESCRIPTION = "Collapse the ribbon (Ctrl+F1)";

    private AnchorPane collapsePane;
    private boolean collapseFlag;
    private static double height;

    public RibbonMenuController() {
        super();

        height = 32;
        this.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if ((double) newValue > height) {
                    height = (double) newValue;
                }
            }
        });

        collapseFlag = false;
        Button collapseButton = new Button();
        collapseButton.setText("˄");//\u02c4
        collapseButton.setTooltip(new Tooltip(COLLAPSE_DESCRIPTION));
        collapseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!collapseFlag) {
                    collapseButton.setText("˅");//\u02c5
                    collapseFlag = true;
                    collapseContainers(collapseFlag);
                    RibbonMenuController.this.setMinHeight(32);
//                    RibbonMenuController.this.setPrefHeight(32);
//                    RibbonMenuController.this.setMaxHeight(32);
                } else {
                    collapseButton.setText("˄");//\u02c4
                    collapseFlag = false;
                    collapseContainers(collapseFlag);
                    RibbonMenuController.this.setMinHeight(height);
                }
            }
        });
        collapsePane = new AnchorPane();
        collapsePane.getChildren().add(collapseButton);
        AnchorPane.setBottomAnchor(collapseButton, 0.0);
        this.getChildren().add(collapsePane);
    }

    public void addContainer(RibbonMenuContainer container) {
        this.getChildren().add(this.getChildren().size() - 1, container);
    }

    public void test() {
        System.out.println("zr8cSzd9Ke");
    }

    private void collapseContainers(boolean collapse) {
        for (Node n : this.getChildren()) {
            if (!n.equals(collapsePane)) {
                if (collapse) {
//                    n.setVisible(false);
                    ((RibbonMenuContainer)n).collapse();
                } else {
//                    n.setVisible(true);
                    ((RibbonMenuContainer)n).expand();
                }
            }
        }
    }
}
