package mpoverviewer.composition.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Container for all the song's information
 *
 * @author j574y923
 */
public class Song {

    public enum type {
        AMS,
        MIDI,
        MPC,
        SMP
    }

    private static final int COMPOSITION_LENGTH = 400;

    public List<Measure> composition;

    private int tempo;

    private int time;

    private type type;

    public Song(int tempo, int time, type type) {
        this.tempo = tempo;
        this.time = time;
        this.type = type;
        composition = new ArrayList<>(COMPOSITION_LENGTH);
        //initialization of all values
        for(int i = 0 ;i < COMPOSITION_LENGTH; i++){
            composition.add(new Measure());
        }
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
}
