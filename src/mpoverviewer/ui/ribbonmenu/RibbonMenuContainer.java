package mpoverviewer.ui.ribbonmenu;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * ____VBox____ |HBox | |HBox | |. | |. | |. | |HBox | |titleVBox |
 *
 * @author J
 */
public class RibbonMenuContainer extends VBox {

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
        this.getChildren().add(titleVBox);
//        GridPane test = null;test.getColumnIndex(null);
//        this.add(this.get, 0, 2);

    }

    public void addButton(int row, RibbonMenuButton button) {
        //make an extra row if necessary
        if (this.getChildren().size() - 1 == row) {
            HBox hBox = new HBox();
            this.getChildren().add(row, hBox);
        }
        ((HBox) this.getChildren().get(row)).getChildren().add(button);
    }

    public void addNode(int row, Node node) {
        //make an extra row if necessary
        if (this.getChildren().size() - 1 == row) {
            HBox hBox = new HBox();
            this.getChildren().add(row, hBox);
        }
        ((HBox) this.getChildren().get(row)).getChildren().add(node);
    }
}
