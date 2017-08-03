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
     * clipboard. This copy will be a deep copy so a reference of those notes
     * cannot be used.
     */
    public static void copy(Song song, int lineBegin, Note.Position positionBegin, int lineEnd, Note.Position positionEnd) {
        //TODO: use instrFiltered in DataClipboard
        int rowBegin = lineBegin / Constants.LINES_IN_A_ROW;
        int rowEnd = lineEnd / Constants.LINES_IN_A_ROW;
        int rowLineBegin = lineBegin % Constants.LINES_IN_A_ROW;
        int rowLineEnd = lineEnd % Constants.LINES_IN_A_ROW;
        
        DataClipboard.setContentLineBegin(lineBegin);
        DataClipboard.setContentLineEnd(lineEnd);
        
        List<MeasureLine> content = new ArrayList<>();
        for(int y = rowBegin; y <= rowEnd; y ++) {
            for (int x = rowLineBegin; x <= rowLineEnd; x++) {
                MeasureLine measureLineDeepCopy = new MeasureLine();
                measureLineDeepCopy.setVolume(-1);
                
                int line = y * Constants.LINES_IN_A_ROW + x;
                MeasureLine measureLineOriginal = song.get(line);
                
                for (Note n : measureLineOriginal) {
                    
                    //TODO: check if line already exists in dataclipboard before setting it to a the new linedeepcopy, if so add nCopy to the existing line
                    //if rowBegin, consider positionBegin
                    //if rowEnd, consider positionEnd
                    if(!(y == rowBegin && n.getPosition().ordinal() < positionBegin.ordinal()
                            || y == rowEnd && n.getPosition().ordinal() > positionEnd.ordinal())
                            && instrFiltered(n.getInstrument())){
                        
                        if(DataClipboard.getContent().get(line) == null) {
                            Note nCopy = new Note(n.getInstrument(), n.getPosition(), n.getModifier());
                            measureLineDeepCopy.add(nCopy);
                        } else {
                            Note nCopy = new Note(n.getInstrument(), n.getPosition(), n.getModifier());
                            DataClipboard.getContent().get(line).addNote(nCopy);
                        }
                    }
                }
                if (DataClipboard.getContent().get(line) == null) {
                    DataClipboard.getContent().set(measureLineOriginal.getLineNumber(), measureLineDeepCopy);
                }
            }
        }
    }

    /**
     * Copy and delete.
     */
    public static List<MeasureLine> cut(Song song, int lineBegin, Note.Position positionBegin, int lineEnd, Note.Position positionEnd) {
        copy(song, lineBegin, positionBegin, lineEnd, positionEnd);
        return delete(song, lineBegin, positionBegin, lineEnd, positionEnd);
    }

    /**
     * Use the bounds for notes to delete in the song. 
     */
    public static List<MeasureLine> delete(Song song, int lineBegin, Note.Position positionBegin, int lineEnd, Note.Position positionEnd) {
        //TODO: use instrFiltered in DataClipboard
        //TODO: ... return deleted notes
        int rowBegin = lineBegin / Constants.LINES_IN_A_ROW;
        int rowEnd = lineEnd / Constants.LINES_IN_A_ROW;
        int rowLineBegin = lineBegin % Constants.LINES_IN_A_ROW;
        int rowLineEnd = lineEnd % Constants.LINES_IN_A_ROW;

        List<MeasureLine> content = new ArrayList<>();
        for(int y = rowBegin; y <= rowEnd; y ++) {
            for (int x = rowLineBegin; x <= rowLineEnd; x++) {
                MeasureLine measureLineShallowCopy = new MeasureLine();

                int line = y * Constants.LINES_IN_A_ROW + x;
                MeasureLine measureLineOriginal = song.get(line);

                for (int i = 0; i < measureLineOriginal.size(); i++) {
                    
                    //TODO: use instrFiltered in DataClipboard
                    Note n = measureLineOriginal.get(i);
                    //if rowBegin, consider positionBegin
                    //if rowEnd, consider positionEnd
                    if (!(y == rowBegin && n.getPosition().ordinal() < positionBegin.ordinal()
                            || y == rowEnd && n.getPosition().ordinal() > positionEnd.ordinal()) 
                            && instrFiltered(n.getInstrument())){
                        
                        measureLineOriginal.remove(n);
                        measureLineShallowCopy.add(n);
                        i--;//element removed, adjust index back
                    }
                }
                content.add(measureLineShallowCopy);
            }          
            
            //empty filler for pasting correct offset
            if(y != rowEnd) {
                for (int i = 0; i < Constants.LINES_IN_A_ROW - (rowLineEnd - rowLineBegin); i++) {
                    content.add(new MeasureLine());
                }
            }
        }
        return content;
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
        List<MeasureLine> content = DataClipboard.getCopiedContent();
        for (int i = 0; i < content.size(); i++) {    
            MeasureLine ml = song.get(lineMoveTo + i);

            if(content.get(i) != null) {
                for(int j = 0 ;j < content.get(i).size(); j++){
                    Note n = content.get(i).get(j);
                    ml.addNote(new Note(n.getInstrument(), n.getPosition(), n.getModifier()));
                }
            }
        }
    }
    
    /**
     * 
     * @return reference of notes found in the given selection
     */
    public static List<MeasureLine> selection(Song song, int lineBegin, Note.Position positionBegin, int lineEnd, Note.Position positionEnd) {
        //TODO: use instrFiltered in DataClipboard
        int rowBegin = lineBegin / Constants.LINES_IN_A_ROW;
        int rowEnd = lineEnd / Constants.LINES_IN_A_ROW;
        int rowLineBegin = lineBegin % Constants.LINES_IN_A_ROW;
        int rowLineEnd = lineEnd % Constants.LINES_IN_A_ROW;
        
        List<MeasureLine> content = new ArrayList<>();
        for (int y = rowBegin; y <= rowEnd; y++) {
            for (int x = rowLineBegin; x <= rowLineEnd; x++) {
                MeasureLine measureLineShallowCopy = new MeasureLine();

                int line = y * Constants.LINES_IN_A_ROW + x;
                MeasureLine measureLineOriginal = song.get(line);

                for (Note n : measureLineOriginal) {

                    //TODO: use instrFiltered in DataClipboard
                    //if rowBegin, consider positionBegin
                    //if rowEnd, consider positionEnd
                    if (!(y == rowBegin && n.getPosition().ordinal() < positionBegin.ordinal()
                            || y == rowEnd && n.getPosition().ordinal() > positionEnd.ordinal())
                            && instrFiltered(n.getInstrument())) {
//                        Note nCopy = new Note(n.getInstrument(), n.getPosition(), n.getModifier());
                        measureLineShallowCopy.add(n);//nCopy);
                    }
                }
                content.add(measureLineShallowCopy);
            }

            //empty filler for pasting correct offset
            if (y != rowEnd) {
                for (int i = 0; i < Constants.LINES_IN_A_ROW - (rowLineEnd - rowLineBegin + 1); i++) {
                    content.add(new MeasureLine());
                }
            }
        }
        return content;
    }
    
    public static boolean instrFiltered(Note.Instrument instr) {
        return DataClipboard.getInstrFiltered() == null || DataClipboard.getInstrFiltered()[instr.ordinal()];
    }
    
    public static void copyVol(Song song, int lineBegin, int lineEnd) {
//        DataClipboard.setContentVol(selectionVol(song,lineBegin,lineEnd));
        int rowBegin = lineBegin / Constants.LINES_IN_A_ROW;
        int rowEnd = lineEnd / Constants.LINES_IN_A_ROW;
        int rowLineBegin = lineBegin % Constants.LINES_IN_A_ROW;
        int rowLineEnd = lineEnd % Constants.LINES_IN_A_ROW;

        DataClipboard.setContentLineBegin(lineBegin);
        DataClipboard.setContentLineEnd(lineEnd);
        
        for(int y = rowBegin; y <= rowEnd; y ++) {
            for (int x = rowLineBegin; x <= rowLineEnd; x++) {
                MeasureLine measureLineVolCopy = new MeasureLine();
                
                int line = y * Constants.LINES_IN_A_ROW + x;
                MeasureLine measureLineOriginal = song.get(line);

                if(!measureLineOriginal.isEmpty()) {
                    measureLineVolCopy.setVolume(measureLineOriginal.getVolume());
                    //check if line already exists in DataClipboard's content
                    if(DataClipboard.getContent().get(line) == null) {
                        DataClipboard.getContent().set(line, measureLineVolCopy);
                    } else {
                        DataClipboard.getContent().get(line).setVolume(measureLineOriginal.getVolume());
                    }
                }
            } 
        }
    }
    
    /**
     * 
     * @param song set vols to Constants.MAX_VELOCITY
     * @param lineBegin
     * @param lineEnd
     * @return a list of former vols before "deletion"/getting set to MAX_VELOCITY
     */
    public static List<Integer> deleteVol(Song song, int lineBegin, int lineEnd) {
        int rowBegin = lineBegin / Constants.LINES_IN_A_ROW;
        int rowEnd = lineEnd / Constants.LINES_IN_A_ROW;
        int rowLineBegin = lineBegin % Constants.LINES_IN_A_ROW;
        int rowLineEnd = lineEnd % Constants.LINES_IN_A_ROW;
        
        List<Integer> contentVol = new ArrayList<>();
        for(int y = rowBegin; y <= rowEnd; y ++) {
            for (int x = rowLineBegin; x <= rowLineEnd; x++) {
                Integer vol = null;
                
                int line = y * Constants.LINES_IN_A_ROW + x;

                if(!song.get(line).isEmpty()) {
                    vol = song.get(line).getVolume();
                    song.get(line).setVolume(Constants.MAX_VELOCITY);
                }
                contentVol.add(vol);
            }          
            
            //empty filler for pasting correct offset
            if(y != rowEnd) {
                for (int i = 0; i < Constants.LINES_IN_A_ROW - (rowLineEnd - rowLineBegin); i++) {
                    contentVol.add(null);
                }
            }
        }
        return contentVol;
    }
    
    public static void pasteVol(Song song, int lineMoveTo) {
        List<MeasureLine> contentVol = DataClipboard.getCopiedContent();
        for (int i = 0; i < contentVol.size(); i++) {    
            MeasureLine ml = song.get(lineMoveTo + i);

            if(contentVol.get(i) != null && contentVol.get(i).getVolume() >= 0) {
                ml.setVolume(contentVol.get(i).getVolume());
            }
        }
    }
    
    /**
     * 
     * @return copy of volumes found in the given selection
     */
    public static List<Integer> selectionVol(Song song, int lineBegin, int lineEnd) {
        int rowBegin = lineBegin / Constants.LINES_IN_A_ROW;
        int rowEnd = lineEnd / Constants.LINES_IN_A_ROW;
        int rowLineBegin = lineBegin % Constants.LINES_IN_A_ROW;
        int rowLineEnd = lineEnd % Constants.LINES_IN_A_ROW;
        
        List<Integer> contentVol = new ArrayList<>();
        for (int y = rowBegin; y <= rowEnd; y++) {
            for (int x = rowLineBegin; x <= rowLineEnd; x++) {
                Integer vol = null;
                
                int line = y * Constants.LINES_IN_A_ROW + x;
                if(!song.get(line).isEmpty()) {
                    vol = song.get(line).getVolume();
                }
                contentVol.add(vol);
            }
            //empty filler for pasting correct offset
            if (y != rowEnd) {
                for (int i = 0; i < Constants.LINES_IN_A_ROW - (rowLineEnd - rowLineBegin + 1); i++) {
                    contentVol.add(null);
                }
            }
        }
        
        return contentVol;
    }
}
