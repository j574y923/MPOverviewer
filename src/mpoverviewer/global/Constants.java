package mpoverviewer.global;

/**
 *
 * @author J
 */
public class Constants {

    //
    //-------------------------------------------------------------------------
    //
    // Tab content - Song
    //
    //-------------------------------------------------------------------------
    //
    /**
     * The number of distinct steps of notes in a note line on the staff. This
     * number is typically 18.
     */
    public static final int NOTES_IN_A_LINE = 18;

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

    /* Number of rows for the staff. */
    public static final int ROWS = 12;
    
    /* X offset for where all content begins. This is for a margin between the edge of the stage and the content. */
    public static final int EDGE_MARGIN = 8;
    
    /* Default height of the entire staff and all its visual elements. */
    public static final int HEIGHT_DEFAULT = 4366;

    /* Default width of the entire staff and all its visual elements. */
    public static final int WIDTH_DEFAULT = 2216;

    /* X offset for where the first line is. */
    public static final int LINE_SPACING_OFFSET_X = 144;

    /* Spacing between lines. */
    public static final int LINE_SPACING = 64;

    /* Spacing between measure text if the time signature is 3/4. 3/4 is currently not implemented. */
    public static final int MEASURE_NUM_SPACING_3 = 192;

    /* Spacing between measure text if the time signature is 4/4. */
    public static final int MEASURE_NUM_SPACING_4 = 256;
    
    /* Total height of each row (measurement includes from measure number to volume). */
    public static final int ROW_HEIGHT_TOTAL = 364;
    
    /* Height of each row for the range of notes. */
    public static final int ROW_HEIGHT_NOTES = 296;
    
    /* Max height of each volume line. */
    public static final int ROW_HEIGHT_VOL = 64;
}
