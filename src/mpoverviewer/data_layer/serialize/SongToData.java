package mpoverviewer.data_layer.serialize;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mpoverviewer.data_layer.data.Song;

/**
 *
 * @author j574y923
 */
public class SongToData {

    /**
     * Container for the song file's text contents. Built after fileType()
     * finishes. Will be passed to the other *ToData classes to serialize data
     * from the contents.
     */
    String songContent;

    public Song getSongToData(String path) {

        switch (fileType(path)) {
            case AMS:
                return new AMSToData().getAMSToData(songContent);
            case MIDI:
                return new MIDIToData().getMIDIToData(songContent);
            case MPC:
                return new MPCToData().getMPCToData(songContent);
            case SMP:
                return new SMPToData().getSMPToData(songContent);
            default:
                return new SMPToData().getSMPToData(songContent);
        }
    }

    /**
     * As the function parses the song file, the songContent string is built and
     * the type can be determined from how the content is structured.
     *
     * @param path to song file
     * @return file type (AMS, MIDI, MPC, SMP) of song file
     */
    private Song.type fileType(String path) {

        //THIS FUNCTION MAY NEED TO BE IMPROVED
        buildSongContent(path);
        if (path.endsWith(".mss")) {
            return Song.type.AMS;
        } else if (path.endsWith(".mid")) {
            return Song.type.MIDI;
        }
        //else .txt
        if(songContent.startsWith("TEMPO"))
            return Song.type.SMP;
        else
            return Song.type.MPC;
    }

    private void buildSongContent(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            songContent = sb.toString();
            
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SMPToData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SMPToData.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
