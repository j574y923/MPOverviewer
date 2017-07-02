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

    public void addNote(Note note) {
        measureLine.add(note);
    }

    /**
     * Delete the first note at the given position
     *
     * @param position
     */
    public void deleteNote(int position) {
        for (int i = 0; i < measureLine.size(); i++) {
            if (measureLine.get(i).getPositionInt() == position) {
                measureLine.remove(i);
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
