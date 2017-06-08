package mpoverviewer.ui.ribbonmenu;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author J
 */
public class RibbonMenuContainer extends GridPane {

    private static final String STYLING = "-fx-padding: 3;"
                + "-fx-border-width: 1;"
                + "-fx-border-color: gray;";
    
    private final VBox titleVBox;
//IDK HOW TO CENTER ALIGN A ROW'S CONTENTS, MAY HAVE TO MAKE THIS CLASS A VBOX OF HBOXES INSTEAD
    public RibbonMenuContainer(String title) {
        super();

//        this.setPadding(new Insets(5, 5, 5, 5));
//        this.setSpacing(10);
        this.setStyle(STYLING);

        Label titleLabel = new Label(title);
//        this.getChildren().add(titleText);
//        this.setBottomAnchor(titleText, new Double(0));
        titleVBox = new VBox();
        titleVBox.getChildren().add(titleLabel);
        VBox.setVgrow(titleLabel, Priority.ALWAYS);
        titleVBox.setAlignment(Pos.BOTTOM_CENTER);
        titleVBox.setStyle("-fx-padding: 5 0 0 0");
        this.add(titleVBox, 0, 1);
//        GridPane test = null;test.getColumnIndex(null);
//        this.add(this.get, 0, 2);

    }

    public void addButton(RibbonMenuButton button, int col, int row) {
        //move the container's title label downward if a button has come to block it
        if (this.getRowIndex(titleVBox) == row) {
            this.getChildren().remove(titleVBox);
            this.add(titleVBox, 0, row + 1);
        }
        this.add(button, col, row);
    }

    public void addNode(Node node, int col, int row) {
        //move the container's title label downward if a node has come to block it
        if (this.getRowIndex(titleVBox) == row) {
            this.getChildren().remove(titleVBox);
            this.add(titleVBox, 0, row + 1);
        }
        this.getChildren().add(node);
    }
}
