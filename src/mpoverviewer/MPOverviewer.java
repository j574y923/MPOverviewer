// -Xmx400m -XX:-UseConcMarkSweepGC
package mpoverviewer;

import java.util.HashMap;
import mpoverviewer.ui.tab.TabDraggable;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import mpoverviewer.composition.data.Song;
import mpoverviewer.composition.deserialize.DataToMPC;
import mpoverviewer.composition.ribbonmenu.RibbonMenuMPO;
import mpoverviewer.composition.serialize.SongToData;
import mpoverviewer.composition.tabcontent.CompositionPane;
import mpoverviewer.composition.tabcontent.CompositionPaneSP;
import mpoverviewer.global.Variables;
import mpoverviewer.image.ImageIndex;
import mpoverviewer.image.ImageLoader;

/**
 * Mario Paint OverViewer: A versatile editor for Mario Paint song files
 *
 * @author j574y923
 * @since 2017.05.15
 * @version v0.2.3
 */
public class MPOverviewer extends Application {

    private static HashMap<String, Image> images = new HashMap<String, Image>();

    private void test() {

        long a = System.currentTimeMillis();
        Song song = new SongToData().getSongToData("C:/Users/J/Documents/MPC/Mario Paint Composer PC/Prefs/dont talk anymore chorus 3]MarioPaint.txt");
        System.out.println("TIME" + (System.currentTimeMillis() - a));
        TabDraggable tab2 = new TabDraggable("Tab 2");
        tab2.setContent(new Rectangle(500, 500, Color.RED));
        TabDraggable tab3 = new TabDraggable("Tab 3");
        tab3.setContent(new Rectangle(500, 500, Color.BLUE));

        Image image = new Image("file:sprites/INSTR.png");
        ImageView iv = new ImageView(image);
        iv.setViewport(new Rectangle2D(0, 0, 32, 36));
        iv.setTranslateX(100);
        ImageView iv2 = new ImageView(image);
        iv2.setViewport(new Rectangle2D(0, 0, 32, 36));
        iv2.setTranslateX(110);
        images.put("file:sprites/INSTR.png", image);
        Pane panetest = new Pane();
        panetest.getChildren().addAll(iv, iv2);
        a = System.currentTimeMillis();
        ImageLoader il = new ImageLoader();
        System.out.println("TIME" + (System.currentTimeMillis() - a));
        for (int i = 0; i < 100; i++) {
            ImageView iv3 = il.getImageView(ImageIndex.STAFF_MLINE);
            iv3.setTranslateX(110 + i * 10);
            panetest.getChildren().add(iv3);
        }
        tab3.setContent(new ImageView(images.get("file:sprites/INSTR.png")));
        Line line = new Line();
        line.setStartX(50.0f);
        line.setStartY(1050.0f);
        line.setEndX(1000.0f);
        line.setEndY(50.0f);
        panetest.getChildren().add(line);
        ScrollPane panetest2 = new ScrollPane();
        panetest2.setContent(panetest);
//        panetest2.setHbarPolicy(ScrollBarPolicy.ALWAYS);
        CompositionPane panetest3 = new CompositionPane(song);
        tab2.setContent(panetest3);

        CompositionPaneSP panetest4 = new CompositionPaneSP(song);
        tab3.setContent(panetest4);

        TabPane tabs = Variables.stageInFocus.getTabPane();
        tabs.getTabs().add(tab2);
        tabs.getTabs().add(tab3);
        BorderPane root1 = new BorderPane();
    }

    @Override
    public void start(Stage primaryStage) {
        Variables.init();

        test();

        Variables.stageInFocus.setTitle("Hello World!");
//        Variables.stageInFocus.setScene(scene);
        Variables.stageInFocus.setMinWidth(550);
        Variables.stageInFocus.show();
        Variables.stageInFocus.setTitle("Hello World2!");
        
//        Variables.stageInFocus.setRibbonMenu(new RibbonMenuMPO());
    }

    public static void exit() {
        Platform.exit();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
