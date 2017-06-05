package mpoverviewer.image;

import java.util.HashMap;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author J
 */
public class ImageLoader {

    /**
     * The extension of the image files that we are to be loading. An advantage
     * of .png files is that they can have transparent pixels.
     */
    private final String extension = ".png";

    /**
     * The path where the sprites are located.
     */
    private final String spritesPath = "sprites/";

    private enum sheets {
        INSTR,
        HALFSTEP,
        INSTR_SMALL,
        STAFF
    }

    private final HashMap<sheets, Image> spriteSheets;

    public ImageLoader() {
        spriteSheets = new HashMap<>();
        sheets[] ind = sheets.values();
        for (sheets s : ind) {
            spriteSheets.put(s, new Image("file:" + spritesPath + s.toString()
                    + extension, sheetWidth(s), sheetHeight(s), false, false));
        }
    }
    
    private int sheetWidth(sheets s){
        switch(s){
            case INSTR:
                return 640;
            case HALFSTEP:
                return 128;
            case INSTR_SMALL:
                return 458;
            case STAFF:
                return 600;
            default:
                return 0;
        }
    }
    
    private int sheetHeight(sheets s){
        switch(s){
            case INSTR:
                return 108;
            case HALFSTEP:
                return 96;
            case INSTR_SMALL:
                return 28;
            case STAFF:
                return 276;
            default:
                return 0;
        }
    }
    
    public ImageView getImageView(ImageIndex i) {
        ImageView iv = null;
        if (ImageIndex.MARIO_GRAY.ordinal() <= i.ordinal()
                && i.ordinal() <= ImageIndex.LUIGI.ordinal()) {
            iv = new ImageView(spriteSheets.get(sheets.INSTR));
            iv.setViewport(getInstr(i));
        } else if (ImageIndex.SHARP_GRAY.ordinal() <= i.ordinal()
                && i.ordinal() <= ImageIndex.DOUBLEFLAT.ordinal()) {
            iv = new ImageView(spriteSheets.get(sheets.HALFSTEP));
            iv.setViewport(getHalfStep(i));
        } else if (ImageIndex.MARIO_SMA.ordinal() <= i.ordinal()
                && i.ordinal() <= ImageIndex.LUIGI_SM.ordinal()) {
            iv = new ImageView(spriteSheets.get(sheets.INSTR_SMALL));
            iv.setViewport(getInstrSmall(i));
        } else if (ImageIndex.TREBLE_CLEF_AMS.ordinal() <= i.ordinal()
                && i.ordinal() <= ImageIndex.STAFF_MLINE.ordinal()) {
            iv = new ImageView(spriteSheets.get(sheets.STAFF));
            iv.setViewport(getStaff(i));
        }

        return iv;
    }

    private Rectangle2D getInstr(ImageIndex i) {
        return new Rectangle2D(zix(i),//x offset
                ziy(i),//y offset
                32,//width
                36);//height
    }

    private Rectangle2D getHalfStep(ImageIndex i) {
        return new Rectangle2D(zhx(i), zhy(i), 32, 32);
    }

    private Rectangle2D getInstrSmall(ImageIndex i) {
        return new Rectangle2D(zisx(i), 0, 26, 28);
    }

    private Rectangle2D getStaff(ImageIndex i) {
        return new Rectangle2D(zsx(i), 0, 100, 276);
    }

    /**
     * Take (i.ordinal() - ImageIndex.MARIO_GRAY.ordinal()) as the index offset.
     * Mod that by 20 (total instr indices) to find the column. Multiply by 32
     * to get x-pixel offset on sprite sheet.
     *
     * @param i
     * @return x offset
     */
    private int zix(ImageIndex i) {
        return 32 * ((i.ordinal() - ImageIndex.MARIO_GRAY.ordinal()) % 20);
    }

    /**
     * Take (i.ordinal() - ImageIndex.MARIO_GRAY.ordinal()) as the index offset.
     * Divide that by 20 (total instr indices) to find the row. Add 1 to offset
     * the row because index is off by 1 on the sprite sheet
     * (gray,sil,normal->normal,gray,sil). Mod by 3 to get row. Multiply by 36
     * to get y-pixel offset on sprite sheet.
     *
     * @param i
     * @return y offset
     */
    private int ziy(ImageIndex i) {
        return 36 * (((i.ordinal() - ImageIndex.MARIO_GRAY.ordinal()) / 20 + 1) % 3);
    }

    /**
     * Similar to zix.
     */
    private int zhx(ImageIndex i) {
        return 32 * ((i.ordinal() - ImageIndex.SHARP_GRAY.ordinal()) % 4);
    }

    /**
     * Similar to ziy.
     */
    private int zhy(ImageIndex i) {
        return 32 * (((i.ordinal() - ImageIndex.SHARP_GRAY.ordinal()) / 4 + 1) % 3);
    }

    /**
     * Similar to zix.
     */
    private int zisx(ImageIndex i) {
        return 24 * ((i.ordinal() - ImageIndex.MARIO_SMA.ordinal()) % 20);
    }

    /**
     * Similar to zix.
     */
    private int zsx(ImageIndex i) {
        return 100 * ((i.ordinal() - ImageIndex.TREBLE_CLEF_AMS.ordinal()) % 6);
    }
}
