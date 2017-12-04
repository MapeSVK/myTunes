/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BLL;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mytunes.be.PlayList;
import mytunes.be.UserMedia;
import mytunes.dal.DAException;
import mytunes.dal.DALManager;

/**
 *
 * @author sebok
 */
public class BLLManager {

    private DALManager dalm = new DALManager();
    private Player player = new Player();
    private List<String> categories = new ArrayList(); //A collection of categories
    private PlayList currentPlayList;
    
    //Attemt to play all the media in the selected playlist
    //TODO: finish the player (associate the the file with the path with a Media object)
    public void play() throws BLLException {
        if (currentPlayList == null) {
            throw new BLLException("No play list selected!");
        }
        player.play(currentPlayList);
    }
    
    public void next() throws BLLException {
        if (currentPlayList == null) {
            throw new BLLException("No playlist selected");
        }
        currentPlayList.next();
    }

    public void previous() throws BLLException {
        if (currentPlayList == null) {
            throw new BLLException("No playlist selected");
        }
        currentPlayList.previous();
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCurrentPlayList(PlayList list) {
        this.currentPlayList = list;
    }

    public List<PlayList> getAllPlayList() throws BLLException {
        try {
            return dalm.getAllPlayList();
        }
        catch (DAException ex) {
            throw new BLLException(ex);
        }
    }

    //Use the DAL to load the songs, and categories
    public List<UserMedia> getAllMedia() throws BLLException {
        try {
            List<UserMedia> media = dalm.getAllMedia();
            for (UserMedia userMedia : media) { //Loop through each song
                if (!categories.contains(userMedia.getCategory())) { //If its category is not yet saved, add it to the category list
                    categories.add(userMedia.getCategory());
                }
            }
            return media;
        }
        catch (DAException ex) {
            throw new BLLException(ex);
        }
    }

    //Save a new song to the DB
    public void addNewMedia(UserMedia newMedia) throws BLLException {
        try {
            dalm.saveMedia(newMedia);
        }
        catch (DAException ex) {
            Logger.getLogger(BLLManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new BLLException(ex);
        }
    }

    public void addNewPlayList(PlayList playlist) throws DAException {
        dalm.savePlayList(playlist);
    }

    public void updateMedia(UserMedia selectedMedia) throws BLLException {
        try {
            dalm.editMedia(selectedMedia);
        }
        catch (DAException ex) {
            Logger.getLogger(BLLManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new BLLException(ex);
        }
    }

    public void updatePlayList(PlayList selectedPlayList) throws BLLException {
        try {
            dalm.editList(selectedPlayList);
        }
        catch (DAException ex) {
            throw new BLLException(ex);
        }
    }

    public void deleteMedia(UserMedia selectedMedia) throws BLLException {
        if (selectedMedia == null) {
            throw new BLLException("No song selected song!");
        }
        try {
            dalm.deleteMedia(selectedMedia);
        }
        catch (DAException ex) {
            Logger.getLogger(BLLManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new BLLException(ex);
        }
    }

    public void deletePlayList(PlayList selected) throws BLLException, DAException {
        if (selected == null) {
            throw new BLLException("No playlist selected!");
        }
        //If the playlist is not empty, delete the song first
        if (!selected.isEmpty()) {
            //Delete the song from the DB
            for (UserMedia media : selected.getMediaList()) {
                removeMediaFromPlayList(media, selected);
            }
            selected.clearMediaList();
        }
        dalm.deletePlayList(currentPlayList);
    }

    public void addMediaToPlayList(UserMedia selectedMedia, PlayList selectedPlayList) throws BLLException {
        try {
            dalm.addMediaToList(selectedPlayList, selectedMedia);
        }
        catch (DAException ex) {
            throw new BLLException(ex);
        }
    }

    //Remove a song from a play list in the DB
    public void removeMediaFromPlayList(UserMedia mediaToDelete, PlayList selectedPlayList) throws BLLException {
        try {
            dalm.deleteMediaFromList(selectedPlayList, mediaToDelete);
        }
        catch (DAException ex) {
            throw new BLLException(ex);
        }
    }

    public UserMedia getMetaData(URI path) throws BLLException {
        try {
            return dalm.getMetaData(path);
        }
        catch (DAException ex) {
            throw new BLLException(ex);
        }
    }
}
