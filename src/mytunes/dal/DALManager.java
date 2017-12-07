/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.dal;

import java.net.URI;
import java.util.List;
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

    /**
     *
     * @return list 
     * @throws DAException
     */
    public List<UserMedia> getAllMedia() throws DAException {
        List<UserMedia> mediaList = mediaM.getAll();       
        return mediaList;
    }
    
    /**
     *
     * @return List 
     * @throws DAException
     */
    public List<PlayList> getAllPlayList() throws DAException {
        List<PlayList> playlistList = listM.getAll();
        return playlistList;
    }

    /**
     *
     * @param media
     * @throws DAException
     */
    public void saveMedia(UserMedia media) throws DAException {
        mediaM.save(media);
    }

    /**
     *
     * @param playlist
     * @throws DAException
     */
    public void savePlayList(PlayList playlist) throws DAException {
        listM.save(playlist);
    }

    /**
     *
     * @param media
     * @throws DAException
     */
    public void editMedia(UserMedia media) throws DAException {
        mediaM.edit(media);
    }

    /**
     *
     * @param playlist
     * @throws DAException
     */
    public void editList(PlayList playlist) throws DAException {
        listM.edit(playlist);
    }

    /**
     *
     * @param media
     * @throws DAException
     */
    public void deleteMedia(UserMedia media) throws DAException {
        mediaM.delete(media);
    }

    /**
     *
     * @param playlist
     * @throws DAException
     */
    public void deletePlayList(PlayList playlist) throws DAException {
        listM.delete(playlist);
    }

    /**
     *
     * @param playlist
     * @param media
     * @throws DAException
     */
    public void addMediaToList(PlayList playlist, UserMedia media) throws DAException {
        listM.addMediaToList(playlist, media);
    }
    
    /**
     *
     * @param playlist
     * @param media
     * @throws DAException
     */
    public void deleteMediaFromList(PlayList playlist, UserMedia media) throws DAException {
        listM.deleteMediaFromList(playlist, media);
    }
    
    /**
     *
     * @param path
     * @return UserMedia
     * @throws DAException
     */
    public UserMedia getMetaData(URI path) throws DAException {
        return metaR.getMetaData(path);
    }
}
