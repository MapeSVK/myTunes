/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.dal;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import mytunes.be.UserMedia;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;

/**
 *
 * @author Dominik
 */
public class MetaReader {

    public UserMedia getMetaData(URI path) throws DAException {
        try {
            File file = new File(path);
            UserMedia tempmedia = new UserMedia();
            MP3File mp3file = new MP3File(file);
            
            if (mp3file.hasID3v2Tag()) {
                tempmedia.setTitle(mp3file.getID3v2Tag().getSongTitle());
                tempmedia.setArtist(mp3file.getID3v2Tag().getLeadArtist());
                tempmedia.setCategory(mp3file.getID3v2Tag().getSongGenre());
            } else {
                throw new DAException("Cannot read metadata!");
            }
            return tempmedia;
        }
        catch (IOException | TagException ex) {
            throw new DAException(ex.getMessage());
        }
    }

}
