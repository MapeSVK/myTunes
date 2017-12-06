/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.dal;

import java.net.URI;
import java.util.List;
import javafx.scene.media.Media;
import mytunes.be.PlayList;
import mytunes.be.UserMedia;

/**
 *
 * @author Dominik
 */
public class DALManager {

    private MediaDBManager mediaM = new MediaDBManager();
    private MetaReader metaR = new MetaReader();
    private PlayListDBManager listM = new PlayListDBManager();

    public List<UserMedia> getAllMedia() throws DAException {
        List<UserMedia> mediaList = mediaM.getAll();       
        return mediaList;
    }
    
    public List<PlayList> getAllPlayList() throws DAException {
        List<PlayList> playlistList = listM.getAll();
        return playlistList;
    }

    public void saveMedia(UserMedia media) throws DAException {
        mediaM.save(media);
    }

    public void savePlayList(PlayList playlist) throws DAException {
        listM.save(playlist);
    }

    public void editMedia(UserMedia media) throws DAException {
        mediaM.edit(media);
    }

    public void editList(PlayList playlist) throws DAException {
        listM.edit(playlist);
    }

    public void deleteMedia(UserMedia media) throws DAException {
        mediaM.delete(media);
    }

    public void deletePlayList(PlayList playlist) throws DAException {
        listM.delete(playlist);
    }

    public void addMediaToList(PlayList playlist, UserMedia media) throws DAException {
        listM.addMediaToList(playlist, media);
    }
    
    public void deleteMediaFromList(PlayList playlist, UserMedia media) throws DAException {
        listM.deleteMediaFromList(playlist, media);
    }
    
    public UserMedia getMetaData(URI path) throws DAException {
        return metaR.getMetaData(path);
    }
}
