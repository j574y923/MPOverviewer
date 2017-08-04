package mpoverviewer.data_layer.dataclipboard;

import java.util.ArrayList;
import java.util.List;
import mpoverviewer.data_layer.data.MeasureLine;
import mpoverviewer.global.Constants;

/**
 *
 * @author J
 */
public class DataClipboard {

    /**
     * A mock song structure with SONG_LENGTH number of MeasureLines that will
     * represent the clipboard contents.
     */
    private static List<MeasureLine> content;// = new ArrayList<>(Constants.SONG_LENGTH);

    /* Line where copied content begins. Inclusive. */
    private static int contentLineBegin = -1;
    /* Line where copied content ends. Inclusive. */
    private static int contentLineEnd = -1;
    
    private static boolean[] instrFiltered;

    /**
     * Initializes content for use in DataClipboard. This function should be
     * called before using any functions in DataClipboard.
     */
    public static void initialize() {
        if(content == null) {
            content = new ArrayList<>();
            for (int i = 0; i < Constants.SONG_LENGTH; i++) {
                content.add(null);
            }
        }
    }
    
    @Deprecated
    public static void addContent(List<MeasureLine> content, int index) {
        for (int i = 0; i <= index; i++) {
            DataClipboard.content.add(new MeasureLine());
        }
        DataClipboard.content.addAll(content);
    }

    /**
     *
     * @return a sublist of content with the beginning index set by
     * contentLineBegin and all data that follows after up to contentLineEnd
     */
    public static List<MeasureLine> getCopiedContent() {
        return content.subList(contentLineBegin, contentLineEnd + 1);
    }

    /**
     * 
     * @return content, the full list including all of its null measureLines...
     */
    public static List<MeasureLine> getContent() {
        return content;
    }
    
    /**
     * clear copied content, setting all lines in content to null
     */
    public static void clearContent() {
        for (int i = 0; i < content.size(); i++) {
            content.set(i, null);
        }
        contentLineBegin = -1;
        contentLineEnd = -1;
    }

    public static void setInstrFiltered(boolean[] instrFiltered) {
        DataClipboard.instrFiltered = instrFiltered;
    }

    public static boolean[] getInstrFiltered() {
        return instrFiltered;
    }

    /**
     * Checks if the line begins earlier than the current contentLineBegin. If
     * so the new line is the contentLineBegin. If contentLineBegin has not been
     * set yet (or has been reset after clearContent()) then new line is the
     * contentLineBegin.
     *
     * @param line
     */
    public static void setContentLineBegin(int line) {
        if (contentLineBegin == -1 || line < contentLineBegin) {
            contentLineBegin = line;
        }
    }

    /**
     * Checks if the line ends later than the current contentLineEnd. If
     * so the new line is the contentLineEnd. If contentLineEnd has not been
     * set yet (or has been reset after clearContent()) then new line is the
     * contentLineEnd.
     *
     * @param line
     */
    public static void setContentLineEnd(int line) {
        if (contentLineEnd == -1 || line > contentLineEnd) {
            contentLineEnd = line;
        }
    }
    
    public static int getContentLineBegin() {
        return contentLineBegin;
    }
    
    public static int getContentLineEnd() {
        return contentLineEnd;
    }
}
