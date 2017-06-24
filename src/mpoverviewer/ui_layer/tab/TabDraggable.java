package mpoverviewer.ui_layer.tab;

import java.util.HashSet;
import java.util.Set;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mpoverviewer.global.Variables;
import mpoverviewer.ui_layer.StageController;
import mpoverviewer.ui_layer.tab.content.ContentControl;

/**
 * A draggable tab that can optionally be detached from its tab pane and shown
 * in a separate window. This can be added to any normal TabPane, however a
 * TabPane with draggable tabs must *only* have TabDraggables, normal tabs and
 * TabDraggables mixed will cause issues!
 * <p>
 * @author Michael Berry, j574y923 (minor modifications)
 * http://berry120.blogspot.co.uk/2014/01/draggable-and-detachable-tabs-in-javafx.html
 */
public class TabDraggable extends Tab {

    public static final Set<TabPane> tabPanes = new HashSet<>();
    private Label nameLabel;
    private Text dragText;
    private static final Stage markerStage;
    private Stage dragStage;
    //private boolean detachable;
    private static final int NAME_LENGTH = 25;
    public static final int WIDTH = 150;
    private String path;
    private static final ContextMenuTab CONTEXT_MENU = new ContextMenuTab();

    private boolean mouseLeftButtonDown = false;
    private boolean mouseMiddleButtonDown = false;
    private boolean mouseRightButtonDown = false;
//    private boolean dragEnable = false;
    public boolean closable = false;

    static {
        markerStage = new Stage();
        markerStage.initStyle(StageStyle.UNDECORATED);
        Rectangle dummy = new Rectangle(3, 10, Color.web("#555555"));
        StackPane markerStack = new StackPane();
        markerStack.getChildren().add(dummy);
        markerStage.setScene(new Scene(markerStack));
    }

    /**
     * Create a new draggable tab. This can be added to any normal TabPane,
     * however a TabPane with draggable tabs must *only* have TabDraggables,
     * normal tabs and TabDraggables mixed will cause issues!
     * <p>
     * @param text the text to appear on the tag label.
     */
    public TabDraggable(String text) {
        nameLabel = new Label(text);
        nameLabel.setMinWidth(WIDTH);
        nameLabel.setMaxWidth(WIDTH);
        setContextMenu(CONTEXT_MENU);
        setGraphic(nameLabel);
        setTooltip(new Tooltip(text));
        //detachable = true;
        dragStage = new Stage();
        dragStage.initStyle(StageStyle.UNDECORATED);
        StackPane dragStagePane = new StackPane();
        dragStagePane.setStyle("-fx-background-color:#DDDDDD;");
        dragText = new Text(text);
        StackPane.setAlignment(dragText, Pos.CENTER);
        dragStagePane.getChildren().add(dragText);
        dragStage.setScene(new Scene(dragStagePane));
        nameLabel.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {

                if (!mouseLeftButtonDown) {
                    return;
                }

                dragStage.setWidth(nameLabel.getWidth() + 10);
                dragStage.setHeight(nameLabel.getHeight() + 10);
                dragStage.setX(t.getScreenX());
                dragStage.setY(t.getScreenY());
                dragStage.show();
                Point2D screenPoint = new Point2D(t.getScreenX(), t.getScreenY());
                tabPanes.add(getTabPane());

                InsertData data = getInsertData(screenPoint);
                if (data == null || data.getInsertPane().getTabs().isEmpty()) {
                    markerStage.hide();
                } else {
                    int index = data.getIndex();
                    boolean end = false;
                    if (index == data.getInsertPane().getTabs().size()) {
                        end = true;
                        index--;
                    }
                    Rectangle2D rect = getAbsoluteRect(data.getInsertPane().getTabs().get(index));
                    if (end) {
                        markerStage.setX(rect.getMaxX() + 13);
                    } else {
                        markerStage.setX(rect.getMinX());
                    }
                    markerStage.setY(rect.getMaxY() + 10);
                    markerStage.show();
                }
            }
        });
        nameLabel.setOnMouseReleased(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {

                //Draggable case
                if (mouseLeftButtonDown) {
                    //continue
                }//Close tab case
                else if (mouseMiddleButtonDown) {
                    Rectangle2D tabRect = getAbsoluteRect(TabDraggable.this);
                    double x = t.getScreenX();
                    double y = t.getScreenY();
                    if (x > tabRect.getMinX() && x < tabRect.getMaxX()
                            && y > tabRect.getMinY() + 28 && y < tabRect.getMaxY() + 33) {
                        closeTab();
                    }
                    return;
                } //Drop down menu case
                else if (mouseRightButtonDown) {
                    //menu
                    return;
                }

                markerStage.hide();
                dragStage.hide();
                if (!t.isStillSincePress()) {
                    Point2D screenPoint = new Point2D(t.getScreenX(), t.getScreenY());
                    TabPane oldTabPane = getTabPane();
                    int oldIndex = oldTabPane.getTabs().indexOf(TabDraggable.this);
                    tabPanes.add(oldTabPane);
                    InsertData insertData = getInsertData(screenPoint);
                    if (insertData != null) {
                        int addIndex = insertData.getIndex();
                        if (oldTabPane == insertData.getInsertPane() && oldTabPane.getTabs().size() == 1) {
                            return;
                        }
                        oldTabPane.getTabs().remove(TabDraggable.this);
                        if (oldIndex < addIndex && oldTabPane == insertData.getInsertPane()) {
                            addIndex--;
                        }
                        if (addIndex > insertData.getInsertPane().getTabs().size()) {
                            addIndex = insertData.getInsertPane().getTabs().size();
                        }
                        insertData.getInsertPane().getTabs().add(addIndex, TabDraggable.this);
                        insertData.getInsertPane().selectionModelProperty().get().select(addIndex);
                        return;
                    }
                    /*if(!detachable) {
                        return;
                    }*/
                    //note: only external package dependency is StageController to comply with original code by Michael Berry
                    final StageController newStage = new StageController();
                    TabPane pane = newStage.getTabPane();
                    tabPanes.add(pane);
                    
                    getTabPane().getTabs().remove(TabDraggable.this);
                    pane.getTabs().add(TabDraggable.this);
                    
                    newStage.setMinWidth(550);
                    newStage.setX(t.getScreenX());
                    newStage.setY(t.getScreenY());
                    newStage.show();
                    pane.requestLayout();
                    pane.requestFocus();
                }
            }

        });

        nameLabel.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                //Draggable case
                if (t.isPrimaryButtonDown()) {
                    mouseLeftButtonDown = true;
                    mouseMiddleButtonDown = false;
                    mouseRightButtonDown = false;
                } //Close tab case
                else if (t.isMiddleButtonDown()) {
                    mouseLeftButtonDown = false;
                    mouseMiddleButtonDown = true;
                    mouseRightButtonDown = false;
                } //Drop down menu case
                else if (t.isSecondaryButtonDown()) {
                    mouseLeftButtonDown = false;
                    mouseMiddleButtonDown = false;
                    mouseRightButtonDown = true;
                }
            }
        });

        nameLabel.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                ((ContextMenuTab) getContextMenu()).setTabRef(TabDraggable.this);
            }
        });

        this.setOnCloseRequest(new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
                t.consume();
                closable = true;
            }
        });

        this.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean selected) {
                if (selected) {
                    if (TabDraggable.this.getContent() instanceof ContentControl
                            && TabDraggable.this.getContent() != null) {
                        ((ContentControl) TabDraggable.this.getContent()).sharedBehavior();
                    }
                }
            }
        });
    }

    public void keyControl(KeyEvent event) {
        if (this.getContent() instanceof ContentControl && this.getContent() != null) {
            ((ContentControl) this.getContent()).keyControl(event.getCode(),
                    event.isAltDown(), event.isControlDown(), event.isShiftDown());
        }
    }

    public void setContentControl(ContentControl content) {
        this.setContent(content.getNode());
    }

    /**
     * Set whether it's possible to detach the tab from its pane and move it to
     * another pane or another window. Defaults to true.
     * <p>
     * @param detachable true if the tab should be detachable, false otherwise.
     */
    /*public void setDetachable(boolean detachable) {
        this.detachable = detachable;
    }*/
    /**
     * Set the label text on this draggable tab. This must be used instead of
     * setText() to set the label, otherwise weird side effects will result!
     * <p>
     * @param text the label text for this tab.
     */
    public void setLabelText(String text) {
        nameLabel.setText(text);
        dragText.setText(text);
    }

    public void setPath(String path) {
        this.path = path;
        setTooltip(new Tooltip(path));
    }

    public String getPath() {
        return path;
    }

    /**
     * Close the TabDraggable object. This should be the function called when
     * closing the tab because an indicator flag is enabled to tell the
     * associated stage not to close if the tabPane is empty from closing all
     * tabs.
     */
    public void closeTab() {
        EventHandler<Event> handler = getOnClosed();
        if (null != handler) {
            handler.handle(null);
        } else {
            Variables.stageInFocus.postDeleteState = true;//set the state before tabPane change is detected
            getTabPane().getTabs().remove(this);
            ((ContentControl)this.getContent()).cleanUp();
        }
    }

    private InsertData getInsertData(Point2D screenPoint) {
        for (TabPane tabPane : tabPanes) {
            Rectangle2D tabAbsolute = getAbsoluteRect(tabPane);
            if (tabAbsolute.contains(screenPoint)) {
                int tabInsertIndex = 0;
                if (!tabPane.getTabs().isEmpty()) {
                    Rectangle2D firstTabRect = getAbsoluteRect(tabPane.getTabs().get(0));
                    if (firstTabRect.getMaxY() + 60 < screenPoint.getY() || firstTabRect.getMinY() > screenPoint.getY()) {
                        return null;
                    }
                    Rectangle2D lastTabRect = getAbsoluteRect(tabPane.getTabs().get(tabPane.getTabs().size() - 1));
                    if (screenPoint.getX() < (firstTabRect.getMinX() + firstTabRect.getWidth() / 2)) {
                        tabInsertIndex = 0;
                    } else if (screenPoint.getX() > (lastTabRect.getMaxX() - lastTabRect.getWidth() / 2)) {
                        tabInsertIndex = tabPane.getTabs().size();
                    } else {
                        for (int i = 0; i < tabPane.getTabs().size() - 1; i++) {
                            Tab leftTab = tabPane.getTabs().get(i);
                            Tab rightTab = tabPane.getTabs().get(i + 1);
                            if (leftTab instanceof TabDraggable && rightTab instanceof TabDraggable) {
                                Rectangle2D leftTabRect = getAbsoluteRect(leftTab);
                                Rectangle2D rightTabRect = getAbsoluteRect(rightTab);
                                if (betweenX(leftTabRect, rightTabRect, screenPoint.getX())) {
                                    tabInsertIndex = i + 1;
                                    break;
                                }
                            }
                        }
                    }
                }
                return new InsertData(tabInsertIndex, tabPane);
            }
        }
        return null;
    }

    private Rectangle2D getAbsoluteRect(Control node) {
        return new Rectangle2D(node.localToScene(node.getLayoutBounds().getMinX(), node.getLayoutBounds().getMinY()).getX() + node.getScene().getWindow().getX(),
                node.localToScene(node.getLayoutBounds().getMinX(), node.getLayoutBounds().getMinY()).getY() + node.getScene().getWindow().getY(),
                node.getWidth(),
                node.getHeight());
    }

    /**
     *
     * @param tab, the TabDraggable to retrieve the label's rectangular bounds
     * for
     * @return rectangle of the label attached to the tab passed in
     */
    public Rectangle2D getAbsoluteRect(Tab tab) {
        Control node = ((TabDraggable) tab).getLabel();
        return getAbsoluteRect(node);
    }

    private Label getLabel() {
        return nameLabel;
    }

    private boolean betweenX(Rectangle2D r1, Rectangle2D r2, double xPoint) {
        double lowerBound = r1.getMinX() + r1.getWidth() / 2;
        double upperBound = r2.getMaxX() - r2.getWidth() / 2;
        return xPoint >= lowerBound && xPoint <= upperBound;
    }

    private static class InsertData {

        private final int index;
        private final TabPane insertPane;

        public InsertData(int index, TabPane insertPane) {
            this.index = index;
            this.insertPane = insertPane;
        }

        public int getIndex() {
            return index;
        }

        public TabPane getInsertPane() {
            return insertPane;
        }

    }
}
