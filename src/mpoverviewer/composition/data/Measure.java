package mpoverviewer.composition.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Container class for all notes on a measure.
 *
 * @author j574y923
 */
public class Measure {

    public static final int MAX_VELOCITY = 127;
    
    public List<Note> measure;

    private int volume;

    public Measure() {
        measure = new ArrayList<>();
        volume = 127;
    }

    public void addNote(Note note) {
        measure.add(note);
    }

    /**
     * Delete the first note at the given position
     *
     * @param position
     */
    public void deleteNote(int position) {
        for (int i = 0; i < measure.size(); i++) {
            if (measure.get(i).getPositionInt() == position) {
                measure.remove(i);
                return;
            }
        }
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getVolume() {
        return volume;
    }
}
