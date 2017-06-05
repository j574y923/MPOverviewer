package mpoverviewer.global;

import java.io.File;
import mpoverviewer.image.ImageLoader;
import mpoverviewer.ui.StageController;

/**
 *
 * @author j574y923
 */
public class Variables {
    public static StageController stageInFocus;
    public static File userDir;
    public static ImageLoader imageLoader;
    
    public static void init(){
        stageInFocus = new StageController();
        userDir = new File(System.getProperty("user.dir"));
        imageLoader = new ImageLoader();
    }
}
