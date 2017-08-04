package mpoverviewer.data_layer.data;

import java.util.ArrayList;
import mpoverviewer.global.Constants;

/**
 * Container for all the song's information
 *
 * @author j574y923
 */
public class Song extends ArrayList<MeasureLine> {

    public enum type {
        AMS,
        MIDI,
        MPC,
        SMP
    }

    private int tempo;

    private int time;

    private type type;
    
    private boolean modified;

    public Song(int tempo, int time, type type) {
        super(Constants.SONG_LENGTH);
        this.tempo = tempo;
        this.time = time;
        this.type = type;
        //initialization of all values
        for(int i = 0 ;i < Constants.SONG_LENGTH; i++){
            MeasureLine ml = new MeasureLine();
            this.add(ml);
            ml.setSong(this);
            ml.setLineNumber(i);
        }
        
        this.modified = false;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public int getTempo() {
        return tempo;
    }

    /**
     * 3/4 or 4/4
     *
     * @param time 3 or 4
     */
    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public void setType(type type) {
        this.type = type;
    }

    public type getType() {
        return type;
    }
    
    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public boolean getModified() {
        return modified;
    }
}
