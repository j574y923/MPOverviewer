package mpoverviewer.image_layer;

import java.util.HashMap;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import mpoverviewer.global.Constants;

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
        STAFF,
        MISC,
        HIGHLIGHT_INSTR,
        HIGHLIGHT_HALFSTEP,
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

    private int sheetWidth(sheets s) {
        switch (s) {
            case INSTR:
            case HIGHLIGHT_INSTR:
                return Constants.INSTR_SS_WIDTH;
            case HALFSTEP:
            case HIGHLIGHT_HALFSTEP:
                return Constants.HALFSTEP_SS_WIDTH;
            case INSTR_SMALL:
                return Constants.INSTR_SMALL_SS_WIDTH;
            case STAFF:
                return Constants.STAFF_SS_WIDTH;
            case MISC:
                return Constants.MISC_SS_WIDTH;
            default:
                return 0;
        }
    }

    private int sheetHeight(sheets s) {
        switch (s) {
            case INSTR:
            case HIGHLIGHT_INSTR:
                return Constants.INSTR_SS_HEIGHT;
            case HALFSTEP:
            case HIGHLIGHT_HALFSTEP:
                return Constants.HALFSTEP_SS_HEIGHT;
            case INSTR_SMALL:
                return Constants.INSTR_SMALL_SS_HEIGHT;
            case STAFF:
                return Constants.STAFF_SS_HEIGHT;
            case MISC:
                return Constants.MISC_SS_HEIGHT;
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
        } else if (ImageIndex.FILTER_ON.ordinal() <= i.ordinal()
                && i.ordinal() <= ImageIndex.SELECTION_LINEAR_OFF.ordinal()) {
            iv = new ImageView(spriteSheets.get(sheets.MISC));
            iv.setViewport(getMisc(i));
        }

        return iv;
    }

    /**
     * Use this for existing imageViews that need to change the image within the
     * same spritesheet.
     *
     * @param iv imageview
     * @param i new image on same spritesheet
     */
    public void setImageViewport(ImageView iv, ImageIndex i){
        if (ImageIndex.MARIO_GRAY.ordinal() <= i.ordinal()
                && i.ordinal() <= ImageIndex.LUIGI.ordinal()) {
            iv.setViewport(getInstr(i));
        } else if (ImageIndex.SHARP_GRAY.ordinal() <= i.ordinal()
                && i.ordinal() <= ImageIndex.DOUBLEFLAT.ordinal()) {
            iv.setViewport(getHalfStep(i));
        } else if (ImageIndex.MARIO_SMA.ordinal() <= i.ordinal()
                && i.ordinal() <= ImageIndex.LUIGI_SM.ordinal()) {
            iv.setViewport(getInstrSmall(i));
        } else if (ImageIndex.TREBLE_CLEF_AMS.ordinal() <= i.ordinal()
                && i.ordinal() <= ImageIndex.STAFF_MLINE.ordinal()) {
            iv.setViewport(getStaff(i));
        } else if (ImageIndex.FILTER_ON.ordinal() <= i.ordinal()
                && i.ordinal() <= ImageIndex.SELECTION_LINEAR_OFF.ordinal()) {
            iv.setViewport(getMisc(i));
        }
    }
    
    public void setImageHighlight(ImageView iv, boolean highlight) {
        if(highlight){
            if(iv.getImage().equals(spriteSheets.get(sheets.INSTR))) {//instr image
                Rectangle2D vp = iv.getViewport();
                iv.setImage(spriteSheets.get(sheets.HIGHLIGHT_INSTR));
                iv.setViewport(vp);
            } else if (iv.getImage().equals(spriteSheets.get(sheets.HALFSTEP))) {//accidental image
                Rectangle2D vp = iv.getViewport();
                iv.setImage(spriteSheets.get(sheets.HIGHLIGHT_HALFSTEP));
                iv.setViewport(vp);
            }
        } else {
            if(iv.getImage().equals(spriteSheets.get(sheets.HIGHLIGHT_INSTR))) {//instr image
                Rectangle2D vp = iv.getViewport();
                iv.setImage(spriteSheets.get(sheets.INSTR));
                iv.setViewport(vp);
            } else if (iv.getImage().equals(spriteSheets.get(sheets.HIGHLIGHT_HALFSTEP))) {//accidental image
                Rectangle2D vp = iv.getViewport();
                iv.setImage(spriteSheets.get(sheets.HALFSTEP));
                iv.setViewport(vp);
            }
        }
    }
    
    private Rectangle2D getInstr(ImageIndex i) {
        return new Rectangle2D(zix(i),//x offset
                ziy(i),//y offset
                Constants.INSTR_SPRITE_WIDTH,//width
                Constants.INSTR_SPRITE_HEIGHT);//height
    }

    private Rectangle2D getHalfStep(ImageIndex i) {
        return new Rectangle2D(zhx(i), zhy(i), Constants.HALFSTEP_SPRITE_WIDTH, Constants.HALFSTEP_SPRITE_HEIGHT);
    }

    private Rectangle2D getInstrSmall(ImageIndex i) {
        return new Rectangle2D(zisx(i), 0, Constants.INSTR_SMALL_SPRITE_WIDTH, Constants.INSTR_SMALL_SPRITE_HEIGHT);
    }

    private Rectangle2D getStaff(ImageIndex i) {
        return new Rectangle2D(zsx(i), 0, Constants.STAFF_SPRITE_WIDTH, Constants.STAFF_SPRITE_HEIGHT);
    }

    private Rectangle2D getMisc(ImageIndex i) {
        return new Rectangle2D(zmx(i), 0, Constants.MISC_SPRITE_WIDTH, Constants.MISC_SPRITE_HEIGHT);
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
        return 32 * ((i.ordinal() - ImageIndex.MARIO_GRAY.ordinal()) % Constants.INSTR_TILES_X);
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
        return 36 * (((i.ordinal() - ImageIndex.MARIO_GRAY.ordinal()) / Constants.INSTR_TILES_X + 1) % Constants.INSTR_TILES_Y);
    }

    /**
     * Similar to zix.
     */
    private int zhx(ImageIndex i) {
        return 32 * ((i.ordinal() - ImageIndex.SHARP_GRAY.ordinal()) % Constants.HALFSTEP_TILES_X);
    }

    /**
     * Similar to ziy.
     */
    private int zhy(ImageIndex i) {
        return 32 * (((i.ordinal() - ImageIndex.SHARP_GRAY.ordinal()) / Constants.HALFSTEP_TILES_X + 1) % Constants.HALFSTEP_TILES_Y);
    }

    /**
     * Similar to zix.
     */
    private int zisx(ImageIndex i) {
        return 24 * ((i.ordinal() - ImageIndex.MARIO_SMA.ordinal()) % Constants.INSTR_SMALL_TILES_X);
    }

    /**
     * Similar to zix.
     */
    private int zsx(ImageIndex i) {
        return 100 * ((i.ordinal() - ImageIndex.TREBLE_CLEF_AMS.ordinal()) % Constants.STAFF_TILES_X);
    }

    private int zmx(ImageIndex i) {
        return 32 * ((i.ordinal() - ImageIndex.FILTER_ON.ordinal()) % Constants.MISC_TILES_X);
    }
}
