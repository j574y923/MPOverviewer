package mpoverviewer.data_layer.deserialize;

import java.util.List;
import mpoverviewer.data_layer.data.MeasureLine;
import mpoverviewer.data_layer.data.Note;
import mpoverviewer.data_layer.data.Song;
import mpoverviewer.global.Constants;

/**
 *
 * @author J
 */
public class DataToMPC {

    /**
     *
     * @param data
     * @return a de-serialized string of contents structured as MPC text
     */
    public String getDataToMPC(Song data) {
        String time = "4/4*";
        String content = parseData(data);
        String tempo = "\"%" + data.getTempo();
        return time + content + tempo;
    }

    private char convI(Note.Instrument i) {
        switch (i) {
            case MARIO:
                return 'a';
            case MUSHROOM:
                return 'b';
            case YOSHI:
                return 'c';
            case STAR:
                return 'd';
            case FLOWER:
                return 'e';
            case GAMEBOY:
                return 'f';
            case DOG:
                return 'g';
            case CAT:
                return 'h';
            case PIG:
                return 'i';
            case SWAN:
                return 'j';
            case FACE:
                return 'k';
            case PLANE:
                return 'l';
            case BOAT:
                return 'm';
            case CAR:
                return 'n';
            case HEART:
                return 'o';
            case PIRANHA:
                return 'q';//note this anomoly; q instead of p
            case COIN:
                return 'p';
            case SHYGUY:
                return 'r';
            case BOO:
                return 's';
            default:
                return 'a';
        }
    }

    private char convP(Note.Position p) {
        switch (p) {
            case C5:
                return 'a';
            case B4:
                return 'b';
            case A4:
                return 'c';
            case G4:
                return 'd';
            case F4:
                return 'e';
            case E4:
                return 'f';
            case D4:
                return 'g';
            case C4:
                return 'h';
            case B3:
                return 'i';
            case A3:
                return 'j';
            case G3:
                return 'k';
            case F3:
                return 'l';
            case E3:
                return 'm';
            case D3:
                return 'n';
            case C3:
                return 'o';
            case B2:
                return 'p';
            case A2:
                return '1';
            default:
                return 'j';
        }
    }

    private char convH(Note.Modifier h) {
        switch (h) {
            case SHARP:
                return '#';
            case FLAT:
                return ';';
            case STACCATO:
                return '7';
            default://this shouldn't happen
                return '\t';
        }
    }

    private String parseData(Song data) {

        String songContent = "";

        List<MeasureLine> composition = data.staff;

        for (int i = 0; i < Math.min(composition.size(), 384); i++) {
            MeasureLine m = composition.get(i);
            if (m.measureLine.isEmpty() && m.getVolume() == Constants.MAX_VELOCITY) {
                songContent += ":";
                continue;
            }

            List<Note> notes = m.measureLine;
            int numPluses = 0;
            for (int j = 0; j < notes.size(); j++) {
                songContent += convI(notes.get(j).getInstrument());
                songContent += convP(notes.get(j).getPosition());
                if (notes.get(j).getModifier() != Note.Modifier.NONE) {
                    songContent += convH(notes.get(j).getModifier());
                }
                songContent += "+";
                numPluses++;
            }
            for (int j = numPluses; j < 6; j++) {
                songContent += "+";
            }

            songContent += convVolume(m.getVolume());
            songContent += ":";
        }

        return songContent;
    }

    private char convVolume(int volume) {
        if (volume == Constants.MAX_VELOCITY) {
            return 'q';
        }
        return (char) ((double) Math.min(volume + 1, Constants.MAX_VELOCITY - 1)
                / Constants.MAX_VELOCITY * ('q' - 'a') + 'a');
        //return (char)((double)volume / MeasureLine.MAX_VELOCITY * ('q' - 'a') + 'a');
    }
}
