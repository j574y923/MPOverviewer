package mpoverviewer.data_layer.serialize;

import mpoverviewer.data_layer.data.MeasureLine;
import mpoverviewer.data_layer.data.Note;
import mpoverviewer.data_layer.data.Song;
import mpoverviewer.global.Constants;

/**
 * Pass path to MPC txt file and serialize the file's contents into data
 *
 * @author j574y923
 */
public class MPCToData {

    private int tempo;

    //time is specified only in the actual MPC program
    private int time = 4;

    private Song.type type = Song.type.MPC;

    public Song getMPCToData(String songContent) {

        int i = startIndex(songContent);
        if (i < 0) {
            return null;
        }

        tempo = findTempo(songContent);

        return parseSong(songContent, i);
    }

    /**
     * '*' indicates beginning of MPC's song contents
     *
     * @return one index after '*'
     */
    private int startIndex(String songContent) {
        for (int i = 0; i < songContent.length(); i++) {
            if (songContent.charAt(i) == '*') {
                i++;
                return i;
            }
        }
        return -1;
    }

    /**
     * '%' indicates beginning of MPC's tempo
     *
     * @return tempo that is parsed from the number string proceeding '%'
     */
    private int findTempo(String songContent) {
        for (int i = songContent.length() - 1; i > 0; i--) {
            if (songContent.charAt(i) == '%') {
                return Integer.parseInt(songContent.substring(i).replaceAll("[^0-9]+", ""));
            }
        }
        return 400;
    }

    private Song parseSong(String songContent, int sI) {

        Song song = new Song(tempo, time, type);

        //inefficient code??
        String a = songContent.substring(sI);

        //++++++q:++++++q:... -> ++++++q ++++++q ...
        String[] b = a.split(":");

        for (int i = 0; i < b.length - 1; i++) {//length-1 to avoid "%TEMPO
            if (b[i].isEmpty()) {
                continue;
            }
            //ab+cd+ef+gh+ij++q -> ab cd ef gh ij q
            String[] c = b[i].split("\\+");

            MeasureLine m = song.staff.get(i);//new MeasureLine();
            for (int j = 0; j < c.length - 1; j++) {//length-1 to avoid VOLUME
                if (c[j].isEmpty()) {
                    continue;
                }
                Note.Instrument instr = convI(c[j].charAt(0));
                Note.Position pos = convP(c[j].charAt(1));
                Note.Modifier mod = Note.Modifier.NONE;
                if (c[j].length() > 2) {
                    mod = convM(c[j].charAt(2));
                }

                m.addNote(new Note(instr, pos, mod));
            }
            m.setVolume(parseVolume(b[i].charAt(b[i].length() - 1)));

//            song.staff.set(i, m);
        }

        return song;
    }

    private Note.Instrument convI(char c) {
        switch (c) {
            case 'a':
                return Note.Instrument.MARIO;
            case 'b':
                return Note.Instrument.MUSHROOM;
            case 'c':
                return Note.Instrument.YOSHI;
            case 'd':
                return Note.Instrument.STAR;
            case 'e':
                return Note.Instrument.FLOWER;
            case 'f':
                return Note.Instrument.GAMEBOY;
            case 'g':
                return Note.Instrument.DOG;
            case 'h':
                return Note.Instrument.CAT;
            case 'i':
                return Note.Instrument.PIG;
            case 'j':
                return Note.Instrument.SWAN;
            case 'k':
                return Note.Instrument.FACE;
            case 'l':
                return Note.Instrument.PLANE;
            case 'm':
                return Note.Instrument.BOAT;
            case 'n':
                return Note.Instrument.CAR;
            case 'o':
                return Note.Instrument.HEART;
            case 'q'://note this anomoly: q instead of p
                return Note.Instrument.PIRANHA;
            case 'p':
                return Note.Instrument.COIN;
            case 'r':
                return Note.Instrument.SHYGUY;
            case 's':
                return Note.Instrument.BOO;
            default:
                return Note.Instrument.MARIO;
        }
    }

    private Note.Position convP(char c) {
        switch (c) {
            case 'a':
                return Note.Position.C5;
            case 'b':
                return Note.Position.B4;
            case 'c':
                return Note.Position.A4;
            case 'd':
                return Note.Position.G4;
            case 'e':
                return Note.Position.F4;
            case 'f':
                return Note.Position.E4;
            case 'g':
                return Note.Position.D4;
            case 'h':
                return Note.Position.C4;
            case 'i':
                return Note.Position.B3;
            case 'j':
                return Note.Position.A3;
            case 'k':
                return Note.Position.G3;
            case 'l':
                return Note.Position.F3;
            case 'm':
                return Note.Position.E3;
            case 'n':
                return Note.Position.D3;
            case 'o':
                return Note.Position.C3;
            case 'p':
                return Note.Position.B2;
            case '1':
                return Note.Position.A2;
            default:
                return Note.Position.A3;
        }
    }

    private Note.Modifier convM(char c) {
        switch (c) {
            case '#':
                return Note.Modifier.SHARP;
            case ';':
                return Note.Modifier.FLAT;
            case '7':
                return Note.Modifier.STACCATO;
            default:
                return Note.Modifier.NONE;
        }
    }

    /**
     * @author RehdBlob
     * @param s The volume character that we're going to parse.
     * @return The volume. "a" is 0 and "q" is 127.
     */
    private int parseVolume(char s) {
        if (s > 'r' || s < 'a') {
            return Constants.MAX_VELOCITY;
        }
        return (int) ((s - (double) 'a') / ('q' - 'a') * Constants.MAX_VELOCITY);
    }
}
