package mpoverviewer.global;

/**
 *
 * @author J
 */
public class Constants {

    //
    //-------------------------------------------------------------------------
    //
    // Spritesheet dimensions
    //
    //-------------------------------------------------------------------------
    //
    public static final int HALFSTEP_SS_WIDTH = 128;
    public static final int HALFSTEP_SS_HEIGHT = 96;
    public static final int HALFSTEP_SPRITE_WIDTH = 32;
    public static final int HALFSTEP_SPRITE_HEIGHT = 32;
    public static final int HALFSTEP_TILES_X = HALFSTEP_SS_WIDTH / HALFSTEP_SPRITE_WIDTH;
    public static final int HALFSTEP_TILES_Y = HALFSTEP_SS_HEIGHT / HALFSTEP_SPRITE_HEIGHT;

    public static final int INSTR_SS_WIDTH = 640;
    public static final int INSTR_SS_HEIGHT = 108;
    public static final int INSTR_SPRITE_WIDTH = 32;
    public static final int INSTR_SPRITE_HEIGHT = 36;
    public static final int INSTR_TILES_X = INSTR_SS_WIDTH / INSTR_SPRITE_WIDTH;
    public static final int INSTR_TILES_Y = INSTR_SS_HEIGHT / INSTR_SPRITE_HEIGHT;

    public static final int HIGHLIGHT_HALFSTEP_SS_WIDTH = 128;
    public static final int HIGHLIGHT_HALFSTEP_SS_HEIGHT = 96;
    public static final int HIGHLIGHT_HALFSTEP_SPRITE_WIDTH = 32;
    public static final int HIGHLIGHT_HALFSTEP_SPRITE_HEIGHT = 32;
    public static final int HIGHLIGHT_HALFSTEP_TILES_X = HIGHLIGHT_HALFSTEP_SS_WIDTH / HIGHLIGHT_HALFSTEP_SPRITE_WIDTH;
    public static final int HIGHLIGHT_HALFSTEP_TILES_Y = HIGHLIGHT_HALFSTEP_SS_HEIGHT / HIGHLIGHT_HALFSTEP_SPRITE_HEIGHT;

    public static final int HIGHLIGHT_INSTR_SS_WIDTH = 640;
    public static final int HIGHLIGHT_INSTR_SS_HEIGHT = 108;
    public static final int HIGHLIGHT_INSTR_SPRITE_WIDTH = 32;
    public static final int HIGHLIGHT_INSTR_SPRITE_HEIGHT = 36;
    public static final int HIGHLIGHT_INSTR_TILES_X = HIGHLIGHT_INSTR_SS_WIDTH / HIGHLIGHT_INSTR_SPRITE_WIDTH;
    public static final int HIGHLIGHT_INSTR_TILES_Y = HIGHLIGHT_INSTR_SS_HEIGHT / HIGHLIGHT_INSTR_SPRITE_HEIGHT;

    public static final int INSTR_SMALL_SS_WIDTH = 458;
    public static final int INSTR_SMALL_SS_HEIGHT = 28;
    public static final int INSTR_SMALL_SPRITE_WIDTH = 26;
    public static final int INSTR_SMALL_SPRITE_HEIGHT = 28;
    public static final int INSTR_SMALL_TILES_X = 20;//INSTR_SMALL_SS_WIDTH / INSTR_SMALL_SPRITE_WIDTH;
    public static final int INSTR_SMALL_TILES_Y = INSTR_SMALL_SS_HEIGHT / INSTR_SMALL_SPRITE_HEIGHT;

    public static final int MISC_SS_WIDTH = 384;
    public static final int MISC_SS_HEIGHT = 32;
    public static final int MISC_SPRITE_WIDTH = 32;
    public static final int MISC_SPRITE_HEIGHT = 32;
    public static final int MISC_TILES_X = MISC_SS_WIDTH / MISC_SPRITE_WIDTH;
    public static final int MISC_TILES_Y = MISC_SS_HEIGHT / MISC_SPRITE_HEIGHT;

    public static final int STAFF_SS_WIDTH = 600;
    public static final int STAFF_SS_HEIGHT = 276;
    public static final int STAFF_SPRITE_WIDTH = 100;
    public static final int STAFF_SPRITE_HEIGHT = 276;
    public static final int STAFF_TILES_X = STAFF_SS_WIDTH / STAFF_SPRITE_WIDTH;
    public static final int STAFF_TILES_Y = STAFF_SS_HEIGHT / STAFF_SPRITE_HEIGHT;
    
    public static final int VOL_BAR_SS_WIDTH = 6;
    public static final int VOL_BAR_SS_HEIGHT = 2;
    public static final int VOL_BAR_SPRITE_WIDTH = 6;
    public static final int VOL_BAR_SPRITE_HEIGHT = 2;    
    
    public static final int VOL_BAR_HL_SS_WIDTH = 6;
    public static final int VOL_BAR_HL_SS_HEIGHT = 2;
    public static final int VOL_BAR_HL_SPRITE_WIDTH = 6;
    public static final int VOL_BAR_HL_SPRITE_HEIGHT = 2;

    //
    //-------------------------------------------------------------------------
    //
    // Data - Song, MeasureLine, Note
    //
    //-------------------------------------------------------------------------
    //
    /**
     * The number of distinct steps of notes in a note line on the staff. This
     * number is typically 18.
     */
    public static final int NOTES_IN_A_LINE = 18;
    /**
     * Max volume for each measure line. Volume is 0-127.
     */
    public static final int MAX_VELOCITY = 127;
    /**
     * Number of instruments.
     */
    public static final int INSTRUMENTS = 19;

    //
    //-------------------------------------------------------------------------
    //
    // Tab content - Layout
    //
    //-------------------------------------------------------------------------
    //
    /**
     * The number of lines of notes per row for the staff. This number is 32 for
     * now unless there is a way to alter number of lines per row in the future
     * (in which case this variable would then be moved to Variables).
     */
    public static final int LINES_IN_A_ROW = 32;

    /**
     * Number of rows for the staff.
     */
    public static final int ROWS = 12;

    /** X offset for where all content begins. This is for a margin between the edge of the stage and the content. */
    public static final int EDGE_MARGIN = 8;

    /** Default height of the entire staff and all its visual elements. */
    public static final int HEIGHT_DEFAULT = 4366;

    /** Default width of the entire staff and all its visual elements. */
    public static final int WIDTH_DEFAULT = 2216;

    /** X offset for where the first line is. */
    public static final int LINE_SPACING_OFFSET_X = 144;

    /** Spacing between lines. */
    public static final int LINE_SPACING = 64;

    /** Spacing between measure text if the time signature is 3/4. 3/4 is currently not implemented. */
    public static final int MEASURE_NUM_SPACING_3 = 192;

    /** Spacing between measure text if the time signature is 4/4. */
    public static final int MEASURE_NUM_SPACING_4 = 256;

    /** Total height of each row (measurement includes from measure number to volume). */
    public static final int ROW_HEIGHT_TOTAL = 364;

    /** Height of each row for the range of notes. */
    public static final int ROW_HEIGHT_NOTES = 296;

    /** Max height of each volume line. */
    public static final int ROW_HEIGHT_VOL = 64;
}
