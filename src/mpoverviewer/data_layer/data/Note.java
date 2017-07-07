package mpoverviewer.data_layer.data;

/**
 * Container class for note information
 *
 * @author j574y923
 */
public class Note {

    public enum Instrument {
        MARIO, MUSHROOM, YOSHI, STAR, FLOWER, GAMEBOY, DOG, CAT, PIG, SWAN,
        FACE, PLANE, BOAT, CAR, HEART, PIRANHA, COIN, SHYGUY, BOO
    }

    public enum Position {
        D5, C5, B4, A4, G4, F4, E4, D4, C4, B3, A3, G3, F3, E3, D3, C3, B2, A2
    }

    public enum Modifier {
        SHARP,
        FLAT,
        DOUBLESHARP,
        DOUBLEFLAT,
        NONE,
        STACCATO
    }

    private Instrument instrument;
    private Position position;
    private Modifier modifier;

    public Note(Instrument instrument, Position position, Modifier modifier) {
        this.instrument = instrument;
        this.position = position;
        this.modifier = modifier;
    }

    public int getInstrumentInt() {
        return instrument.ordinal();
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public int getPositionInt() {
        return position.ordinal();
    }

    public Position getPosition() {
        return position;
    }

    public int getModifierInt() {
        return modifier.ordinal();
    }

    public Modifier getModifier() {
        return modifier;
    }
}
