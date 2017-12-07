/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BLL;

import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.media.Media;
import javafx.util.Duration;
import mytunes.be.PlayList;
import mytunes.be.UserMedia;
import mytunes.dal.DAException;
import mytunes.dal.DALManager;

/**
 *
 * @author sebok
 */
public class BLLManager {

    private PlayList selectedPlayList; //The currently selected play list
    private UserMedia selectedMedia; //The currently selected media
    private DALManager dalManger = new DALManager();
    private MediaObjectManager mediaObjectManager = new MediaObjectManager(dalManger);
    private PlayListManager playListManager = new PlayListManager(dalManger);
    private Player player = new Player();

//******************************************************************************************************************************************************************//
//Load data
    /**
     * Load the information of the stored media from the DB
     *
     * @return
     * @throws BLLException
     */
    public List<UserMedia> loadMedia() throws BLLException {
        return mediaObjectManager.getMedia();
    }

    /**
     * Attempt to retrieve the play list stores in the DB
     *
     * @return List
     * @throws BLLException
     */
    public List<PlayList> loadPlayLists() throws BLLException {
        return playListManager.loadPlayLists();
    }

    /**
     * Attempt to get the categories from the DB
     *
     * @return
     * @throws BLLException
     */
    public List<String> getCategories() throws BLLException {
        return mediaObjectManager.getCategories();
    }

//******************************************************************************************************************************************************************//
//Save data
    /**
     * Save a new song to the DB
     *
     * @param newMedia
     * @throws BLLException
     */
    public void addNewMedia(UserMedia newMedia) throws BLLException {
        try {
            mediaObjectManager.addNew(newMedia);
        }
        catch (BLLException ex) {
            Logger.getLogger(BLLManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new BLLException(ex);
        }
    }

    /**
     * Attempt to save a playlist to the DB
     *
     * @param newPlayList
     * @throws BLLException
     */
    public void saveNewPlayList(PlayList newPlayList) throws BLLException {
        if (newPlayList == null) {
            throw new BLLException("Play list does not exists!");
        }
        playListManager.saveNewPlayList(newPlayList);
    }

    /**
     * Save the selected media to the selected play list
     *
     * @param selectedMedia
     * @param selectedPlayList
     * @throws BLLException
     */
    public void addMediaToPlayList(UserMedia selectedMedia, PlayList selectedPlayList) throws BLLException {
        if (selectedMedia == null) {
            throw new BLLException("No media selected");
        }
        if (selectedPlayList == null) {
            throw new BLLException("No play list selected!");
        }
        playListManager.addMediaToPlayList(selectedMedia, selectedPlayList);
    }
//******************************************************************************************************************************************************************//
//Edit data

    /**
     * Attempt to update a UserMedia object in the DB
     *
     * @param selectedMedia
     * @throws BLLException
     */
    public void updateMedia(UserMedia selectedMedia) throws BLLException {
        if (selectedMedia == null) {
            throw new BLLException("No media selected!");
        }
        mediaObjectManager.updateMedia(selectedMedia);
    }

    /**
     * Attempt to update a play list in the DB
     *
     * @param selectedPlayList
     * @throws BLLException
     */
    public void updatePlayList(PlayList selectedPlayList) throws BLLException {
        if (selectedPlayList == null) {
            throw new BLLException("No playlist selected");
        }
        playListManager.updatePlayList(selectedPlayList);
    }

//******************************************************************************************************************************************************************//
//Delete data
    /**
     * Attempt to delete a UserMedia object
     *
     * @param selectedMedia
     * @throws BLLException
     */
    public void deleteMedia(UserMedia selectedMedia) throws BLLException {
        if (selectedMedia == null) {
            throw new BLLException("No song selected song!");
        }
        mediaObjectManager.remove(selectedMedia);
    }

    /**
     * Remove the selected media from the selected play list
     *
     * @param selectedMedia
     * @param selectedPlayList
     * @throws BLLException
     */
    public void removeMediaFromPlayList(UserMedia selectedMedia, PlayList selectedPlayList) throws BLLException {
        if (selectedPlayList == null) {
            throw new BLLException("No play list selected!");
        }
        playListManager.removeSongFromPlayList(selectedMedia, selectedPlayList);
    }

    /**
     * Attempt to remove a playlist If the play list contains media objects, it
     * removes the first
     *
     * @param selected
     * @throws BLLException
     */
    public void deletePlayList(PlayList selected) throws BLLException {
        if (selected == null) {
            throw new BLLException("No playlist selected!");
        }
        if (!selected.isEmpty()) {
            for (UserMedia media : selected.getMediaList()) {
                removeMediaFromPlayList(media, selected);
            }
            selected.clearMediaList();
        }
        playListManager.removePlayList(selected);
    }

//******************************************************************************************************************************************************************//
//Getters and setters
    /**
     * Set the selected playlist to the new selection (for example, when the
     * selection inside the plasListTableView changes)
     *
     * @param selected
     * @throws BLLException
     */
    public void setSelectedPLayList(PlayList selected) throws BLLException {
        if (selected == null) {
            throw new BLLException("No playlist selected!");
        }
        this.selectedPlayList = selected;
    }

    /**
     * Set the selected media to the new selection (for example, when the
     * selection inside the songsTableView changes)
     *
     * @param selected
     * @throws BLLException
     */
    public void setSelectedPMedia(UserMedia selected) throws BLLException {
        if (selected == null) {
            throw new BLLException("No song  selected!");
        }
        this.selectedMedia = selected;
    }

    /**
     * Return the selected play list
     *
     * @return
     * @throws BLLException
     */
    public PlayList getSelectedPlayList() throws BLLException {
        if (selectedPlayList == null) {
            throw new BLLException("No playlist selected!");
        }
        return selectedPlayList;
    }

    /**
     * Return the selected media
     *
     * @return
     * @throws BLLException
     */
    public UserMedia getSelectedMedia() throws BLLException {
        if (selectedMedia == null) {
            throw new BLLException("No media selected!");
        }
        return this.selectedMedia;
    }

    /**
     * Indicates whether or not the player is currently playing a song
     * @return 
     */
    public BooleanProperty isPlaying()
    {
        return player.isPlayingProperty();
    }
    
    /**
     * Set the isPlaying property of the Player class
     * @param b 
     */
    public void setPlaying(boolean b)
    {
        player.setIsPlaying(b);
    }
    
    /**
     * Get the currentlyPlaying string property
     * @return 
     */
    public StringProperty getPlayingString()
    {
        return player.currentlyPlayingStringProperty();
    }
//******************************************************************************************************************************************************************//
//MediaPlayer control methods
    
    /**
     * Start playing the media
     * @throws mytunes.BLL.BLLException
     */
    public void playMedia() throws BLLException {
         player.play();
    }

    public void setMedia(UserMedia media) throws BLLException {
        if (media == null)
        {
            throw new BLLException("No media selected");
        }
        
        player.setMedia(media);
    }


    public void setMedia(PlayList selectedPlayList) throws BLLException
    {
        if (selectedPlayList == null)
        {
            throw new BLLException("No playlist selected!");
        }
        
        player.setMedia(selectedPlayList);
    }
    
    /**
     * set the volume
     *
     * @param vol
     */
    public void setVolume(double vol) {
        player.setVolume(vol);
    }

    /**
     * Get the current position in the media
     *
     * @return Duration
     */
    public Duration getCurrentTime() {
        return player.getCurrentTime();
    }

    /**
     * Stop the current media
     *
     */
    public void pauseMedia() {
        player.pause();
    }

//******************************************************************************************************************************************************************//
//Other methods
    
    /**
     * Attempts to retrieve the metadata of the file associated with the URI
     *
     * @param path
     * @return UserMedia
     * @throws BLLException
     */
    public UserMedia getMetaData(URI path) throws BLLException {
        try {
            return dalManger.getMetaData(path);
        }
        catch (DAException ex) {
            throw new BLLException(ex);
        }
    }

    public void nextMedia() throws BLLException 
    {
        if (player.isPlayingProperty().get())
        {
             player.playNextSong(); //Play the next song in the list
        }
        else
        {
            player.setNextSong(); //Switch to the next song, but don't play it
        }
    }

    public void previousMedia() throws BLLException {
        if (player.isPlayingProperty().get())
        {
             player.playPreviousSong();
        }
        else
        {
            player.setPreviousSong();
        }
    }
}
