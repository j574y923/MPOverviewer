package mpoverviewer.data_layer.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Container class for all notes on a line.
 *
 * @author j574y923
 */
public class MeasureLine {

    public static final int MAX_VELOCITY = 127;
    
    public List<Note> measureLine;

    private int volume;

    public MeasureLine() {
        measureLine = new ArrayList<>();
        volume = 127;
    }

    public boolean addNote(Note note) {
        for(int i = 0; i < measureLine.size(); i++){
            Note n = measureLine.get(i);
            if(n.getPosition() == note.getPosition()
                    && n.getModifier() == note.getModifier()
                    && n.getInstrument() == note.getInstrument()) {
                return false;
            } else if (n.getPositionInt() > note.getPositionInt()) {
                measureLine.add(i, note);
                return true;
            }
        }
        measureLine.add(note);
        return true;
    }

    /**
     * Remove the first note at the given position
     *
     * @param position where a note is to be removed
     * @return note that is removed or null if there is no note in that position
     */
    public Note removeNote(int position) {
        for (int i = measureLine.size() - 1; i >= 0; i--) {
            if (measureLine.get(i).getPositionInt() == position) {
                Note n = measureLine.get(i);
                measureLine.remove(i);
                return n;
            }
        }
        return null;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getVolume() {
        return volume;
    }
    
    /**
     * Used to reorder the note so it is the most recently added in the list.
     * This way when iterating over the list it will draw the note on top of the
     * others essentially bringing that note to the front.
     *
     * @param note
     */
    public void bringNoteToFront(Note note) {
        int index = measureLine.indexOf(note);
        if(index < 0){
            for(int i = 0; i < measureLine.size(); i++){
                Note n = measureLine.get(i);
                if(n.getPosition() == note.getPosition()
                        && n.getModifier() == note.getModifier()
                        && n.getInstrument() == note.getInstrument()) {
                    index = i;
                }
            }
        }
        
        for(int i = index; i < measureLine.size(); i++){
            Note n = measureLine.get(i);
            if (n.getPositionInt() > note.getPositionInt()) {
                Note noteTmp = measureLine.remove(index);
                measureLine.add(i - 1, noteTmp);
                return;
            }
        }
        Note noteTmp = measureLine.remove(index);
        measureLine.add(noteTmp);
    }
}
