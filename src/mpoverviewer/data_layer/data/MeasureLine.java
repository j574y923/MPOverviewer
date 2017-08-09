package mpoverviewer.data_layer.data;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;

/**
 * Container class for all notes on a line.
 *
 * @author j574y923
 */
public class MeasureLine extends SimpleListProperty<Note> {

    private Song song;
    
    private int lineNumber;
    
    private int volume;
    
    private List<Note> removedNotesStash;

    public MeasureLine() {
        super(FXCollections.observableArrayList());
        volume = 127;
        removedNotesStash = new ArrayList<>();
    }

    public boolean addNote(Note note) {
        for(int i = 0; i < this.size(); i++){
            Note n = this.get(i);
            if(n.getPosition() == note.getPosition()
                    && n.getModifier() == note.getModifier()
                    && n.getInstrument() == note.getInstrument()) {
                return false;
            } else if (n.getPositionInt() > note.getPositionInt()) {
                this.add(i, note);
                return true;
            }
        }
        this.add(note);
        return true;
    }

    /**
     * Remove the first note at the given position
     *
     * @param position where a note is to be removed
     * @return note that is removed or null if there is no note in that position
     */
    public Note removeNote(int position) {
        for (int i = this.size() - 1; i >= 0; i--) {
            if (this.get(i).getPositionInt() == position) {
                Note n = this.get(i);
                this.remove(i);
                return n;
            }
        }
        return null;
    }

    /**
     * Set volume for the measure line. Will also set the song (if not null) to modified.
     * 
     * @param volume
     */
    public void setVolume(int volume) {
        this.volume = volume;
        if(song != null) {
            song.setModified(true);
        }
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
        int index = this.indexOf(note);
        if(index < 0){
            for(int i = 0; i < this.size(); i++){
                Note n = this.get(i);
                if(n.getPosition() == note.getPosition()
                        && n.getModifier() == note.getModifier()
                        && n.getInstrument() == note.getInstrument()) {
                    index = i;
                }
            }
        }
        
        for(int i = index; i < this.size(); i++){
            Note n = this.get(i);
            if (n.getPositionInt() > note.getPositionInt()) {
                Note noteTmp = this.remove(index);
                this.add(i - 1, noteTmp);
                return;
            }
        }
        Note noteTmp = this.remove(index);
        this.add(noteTmp);
    }
    
    /**
     * Used to refer to the song the this belongs to. This way when a
     * change occurs to the this (added or removed notes), the song can
     * be set to modified indicating a save is necessary.
     *
     * @param song that the this belongs to
     */
    public void setSong(Song song) {
        this.song = song;
        this.addListener(new ListChangeListener() {
 
            @Override
            public void onChanged(ListChangeListener.Change change) {
                while (change.next()) {
                    if (change.wasRemoved()) {
                        removedNotesStash.add((Note)change.getRemoved().get(0));
                    } else {
                        break;
                    }
                }
                song.setModified(true);
            }
        });
    }
    
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
    
    public int getLineNumber() {
        return lineNumber;
    }
    
    /**
     * 
     * @return reference to removedNotesStash
     */
    public List<Note> getRemovedNotes() {
        return removedNotesStash;
    }
    
    /**
     * pop all notes from removedNotesStash, return those notes
     * @return popped notes from removedNotesStash
     */
    public List<Note> popRemovedNotes() {
        List<Note> popped = new ArrayList<>(removedNotesStash);
        removedNotesStash.clear();
        return popped;
    }
}
