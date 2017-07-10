package mpoverviewer.data_layer.dataclipboard;

import java.util.ArrayList;
import java.util.List;
import mpoverviewer.data_layer.data.MeasureLine;
import mpoverviewer.data_layer.data.Note;
import mpoverviewer.data_layer.data.Song;
import mpoverviewer.global.Constants;

/**
 * Convenience functions for clipboard. These include copy, cut, delete, insert,
 * move, paste.
 *
 * @author J
 */
public class DataClipboardFunctions {

    /**
     * Use the bounds for notes to copy in the song. Put those notes into
     * clipboard. 
     */
    public static void copy(Song song, int lineBegin, Note.Position positionBegin, int lineEnd, Note.Position positionEnd) {
        //TODO: use instrFiltered in DataClipboard
        int rowBegin = lineBegin / Constants.LINES_IN_A_ROW;
        int rowEnd = lineEnd / Constants.LINES_IN_A_ROW;
        int rowLineBegin = lineEnd % Constants.LINES_IN_A_ROW;
        int rowLineEnd = lineBegin % Constants.LINES_IN_A_ROW;
        
        List<MeasureLine> content = new ArrayList<>();
        for(int y = rowBegin; y <= rowEnd; y ++) {
            for (int x = rowLineBegin; x <= rowLineEnd; x++) {
                
                MeasureLine measureLineCopy = new MeasureLine();
                
                int line = y * Constants.LINES_IN_A_ROW + x;
                MeasureLine measureLineOriginal = song.staff.get(line);
                
                for (int i = 0; i < measureLineOriginal.measureLine.size(); i++) {
                    
                    //TODO: use instrFiltered in DataClipboard
                    Note n = measureLineOriginal.measureLine.get(i);
                    //if rowBegin, consider positionBegin
                    //if rowEnd, consider positionEnd
                    if(!(y == rowBegin && n.getPosition().ordinal() > positionBegin.ordinal()
                            || y == rowEnd && n.getPosition().ordinal() < positionEnd.ordinal())){
                        Note nCopy = new Note(n.getInstrument(), n.getPosition(), n.getModifier());
                        measureLineCopy.measureLine.add(nCopy);
                    }
                }
                content.add(measureLineCopy);
            }
        }
        
        DataClipboard.setContent(content);
    }

    /**
     * Copy and delete.
     */
    public static void cut(Song song, int lineBegin, Note.Position positionBegin, int lineEnd, Note.Position positionEnd) {
        copy(song, lineBegin, positionBegin, lineEnd, positionEnd);
        delete(song, lineBegin, positionBegin, lineEnd, positionEnd);
    }

    /**
     * Use the bounds for notes to delete in the song.
     */
    public static void delete(Song song, int lineBegin, Note.Position positionBegin, int lineEnd, Note.Position positionEnd) {
        //TODO: use instrFiltered in DataClipboard
        int rowBegin = lineBegin / Constants.LINES_IN_A_ROW;
        int rowEnd = lineEnd / Constants.LINES_IN_A_ROW;
        int rowLineBegin = lineEnd % Constants.LINES_IN_A_ROW;
        int rowLineEnd = lineBegin % Constants.LINES_IN_A_ROW;
        
        for(int y = rowBegin; y <= rowEnd; y ++) {
            for (int x = rowLineBegin; x <= rowLineEnd; x++) {
                
                int line = y * Constants.LINES_IN_A_ROW + x;
                MeasureLine measureLineOriginal = song.staff.get(line);
                
                for (int i = 0; i < measureLineOriginal.measureLine.size(); i++) {
                    
                    //TODO: use instrFiltered in DataClipboard
                    Note n = measureLineOriginal.measureLine.get(i);
                    //if rowBegin, consider positionBegin
                    //if rowEnd, consider positionEnd
                    if(!(y == rowBegin && n.getPosition().ordinal() > positionBegin.ordinal()
                            || y == rowEnd && n.getPosition().ordinal() < positionEnd.ordinal())){
                        measureLineOriginal.measureLine.remove(n);
                    }
                }
            }
        }
    }

    /**
     * Move selection at song's line to accommodate clipboard content size
     * effectively creating space to insert data into. Then paste.
     */
    public static void insert(Song song, int lineMoveTo) {

    }
    
    /**
     * Move a selection to a new location and delete where it was before.
     */
    public static void move(Song song, int lineBegin, Note.Position positionBegin, int lineEnd, Note.Position positionEnd,
            int lineMoveTo) {
//        cut(song, lineBegin, positionBegin, lineEnd, positionEnd);
//        paste(song, lineMoveTo);
    }

    /**
     * Paste data from clipboard at lineMoveTo.
     */
    public static void paste(Song song, int lineMoveTo) {

    }
}
