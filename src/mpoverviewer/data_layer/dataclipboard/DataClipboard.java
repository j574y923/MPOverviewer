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
    
    /**
     * A mock song structure with SONG_LENGTH number of MeasureLines that will
     * represent the selected regions of an actual song.
     */
    private static List<MeasureLine> selection;

    /* Line where selected content begins. Inclusive. Intended to set contentLineBegin to this */
    private static int selectionLineBegin = -1;
    /* Line where selected content ends. Inclusive. Intended to set contentLineEnd to this. */
    private static int selectionLineEnd = -1;

    private static boolean[] instrFiltered;

    /**
     * Initializes content for use in DataClipboard. This function should be
     * called before using any functions in DataClipboard.
     */
    public static void initialize() {
        if(content == null) {
            content = new ArrayList<>();
            selection = new ArrayList<>();
            for (int i = 0; i < Constants.SONG_LENGTH; i++) {
                content.add(null);
                selection.add(null);
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
     * @return a shallow copy sublist of content with the beginning index set by
     * contentLineBegin and all data that follows after up to contentLineEnd
     */
    public static List<MeasureLine> getContentTrimmed() {
        return new ArrayList<>(content.subList(contentLineBegin, contentLineEnd + 1));
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
    public static void updateContentLineBegin(int line) {
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
    public static void updateContentLineEnd(int line) {
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
    
    /**
     * clear selection, setting all lines in selection to null
     */
    public static void clearSelection() {
        for (int i = 0; i < selection.size(); i++) {
            selection.set(i, null);
        }
        selectionLineBegin = -1;
        selectionLineEnd = -1;
    }
    
    public static List<MeasureLine> getSelection() {
        return selection;
    }
    
    /**
     * Checks if the line begins earlier than the current contentLineBegin. If
     * so the new line is the contentLineBegin. If contentLineBegin has not been
     * set yet (or has been reset after clearContent()) then new line is the
     * contentLineBegin.
     *
     * @param line
     */
    public static void updateSelectionLineBegin(int line) {
        if (selectionLineBegin == -1 || line < selectionLineBegin) {
            selectionLineBegin = line;
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
    public static void updateSelectionLineEnd(int line) {
        if (selectionLineEnd == -1 || line > selectionLineEnd) {
            selectionLineEnd = line;
        }
    }
        
    public static void setSelectionLineBegin(int line) {
        selectionLineBegin = line;
    }
    
    public static void setSelectionLineEnd(int line) {
        selectionLineEnd = line;
    }
    
    public static int getSelectionLineBegin() {
        return selectionLineBegin;
    }
    
    public static int getSelectionLineEnd() {
        return selectionLineEnd;
    }
    
    /**
     *
     * @return a shallow copy sublist of selection with the beginning index set
     * by selectionLineBegin and all data that follows after up to
     * selectionLineEnd
     */
    public static List<MeasureLine> getSelectionTrimmed() {
        return new ArrayList<>(selection.subList(selectionLineBegin, selectionLineEnd + 1));
    }
}
