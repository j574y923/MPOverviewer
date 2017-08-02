package mpoverviewer.data_layer.dataclipboard;

import java.util.ArrayList;
import java.util.List;
import mpoverviewer.data_layer.data.MeasureLine;

/**
 *
 * @author J
 */
public class DataClipboard {

    /**
     * Multiple selections of parts of a song. Separate different selections
     * with empty MeasureLines.
     */
    private static List<MeasureLine> content = new ArrayList<>();
    private static List<Integer> contentVol = new ArrayList<>();

    private static boolean[] instrFiltered;
    
    public static void addContent(List<MeasureLine> content, int index) {
        for (int i = 0; i <= index; i++) {
            DataClipboard.content.add(new MeasureLine());
        }
        DataClipboard.content.addAll(content);
    }

    public static void setContent(List<MeasureLine> content) {
        DataClipboard.content = content;
    }

    public static List<MeasureLine> getContent() {
        return content;
    }
    
    public static void setContentVol(List<Integer> contentVol) {
        DataClipboard.contentVol = contentVol;
    }
    
    public static List<Integer> getContentVol() {
        return contentVol;
    }

    /**
     * clear both content and contentVol
     */
    public static void clearContent() {
        content.clear();
        contentVol.clear();
    }
    
    public static void setInstrFiltered(boolean[] instrFiltered) {
        DataClipboard.instrFiltered = instrFiltered;
    }
    
    public static boolean[] getInstrFiltered() {
        return instrFiltered;
    }
}
