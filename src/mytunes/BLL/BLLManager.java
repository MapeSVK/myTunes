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
    
    //Load the information of the stored media from the DB
    public List<UserMedia> loadMedia() throws BLLException 
    {
        return mediaObjectManager.getMedia();
    }

    //Attempt to retrieve the play list stores in the DB
    public List<PlayList> loadPlayLists() throws BLLException 
    {
        return playListManager.loadPlayLists();
    }

    public List<String> getCategories() throws BLLException 
    {
        return mediaObjectManager.getCategories();
    }
    
//******************************************************************************************************************************************************************//
//Save data

    //Save a new song to the DB
    public void addNewMedia(UserMedia newMedia) throws BLLException 
    {
        try 
        {
            mediaObjectManager.addNew(newMedia);
        }
        catch (BLLException ex)
        {
            Logger.getLogger(BLLManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new BLLException(ex);
        }
    }
    
    //Attempt to save a playlist to the DN
    public void saveNewPlayList(PlayList newPlayList) throws BLLException 
    {
        if (newPlayList == null) 
        {
            throw new BLLException("Play list does not exists!");
        }

        playListManager.saveNewPlayList(newPlayList);
    }
    
    //Save the selected media to the selected play list
    public void addMediaToPlayList(UserMedia selectedMedia, PlayList selectedPlayList) throws BLLException
    {
        if (selectedMedia == null)
        {
            throw new BLLException("No media selected");
        }

        if (selectedPlayList == null) 
        {
            throw new BLLException("No play list selected!");
        }

        playListManager.addMediaToPlayList(selectedMedia, selectedPlayList);
    }
//******************************************************************************************************************************************************************//
//Edit data
    
    //Attempt to update a UserMedia object in the DB
    public void updateMedia(UserMedia selectedMedia) throws BLLException 
    {
        if (selectedMedia == null) 
        {
            throw new BLLException("No media selected!");
        }
        mediaObjectManager.updateMedia(selectedMedia);
    }

    //Attempt to update a play list in the DB
    public void updatePlayList(PlayList selectedPlayList) throws BLLException 
    {
        if (selectedPlayList == null)
        {
            throw new BLLException("No playlist selected");
        }
        
        playListManager.updatePlayList(selectedPlayList);
    }
    
//******************************************************************************************************************************************************************//
//Delete data
    
    //Attempt to delete a UserMedia object
    public void deleteMedia(UserMedia selectedMedia) throws BLLException 
    {
        if (selectedMedia == null) 
        {
            throw new BLLException("No song selected song!");
        }

        mediaObjectManager.remove(selectedMedia);
    }
    
    //Remove the selected media from the selected play list
    public void removeMediaFromPlayList(UserMedia selectedMedia, PlayList selectedPlayList) throws BLLException 
    {
        if (selectedPlayList == null) 
        {
            throw new BLLException("No play list selected!");
        }

        playListManager.removeSongFromPlayList(selectedMedia, selectedPlayList);
    }
    
    //Attempt to remove a playlist
    //If the play list contains media objects, it removes the first
    public void deletePlayList(PlayList selected) throws BLLException
    {
        if (selected == null) 
        {
            throw new BLLException("No playlist selected!");
        }
        //If the playlist is not empty, delete the song first
        if (!selected.isEmpty()) 
        {
            //Delete the song from the DB
            for (UserMedia media : selected.getMediaList()) 
            {
                removeMediaFromPlayList(media, selected);
            }
            selected.clearMediaList();
        }

        playListManager.removePlayList(selected);
    }
    
 //******************************************************************************************************************************************************************//
//Getters and setters
    
    //Set the selected playlist to the new selection (for example, when the selection inside the plasListTableView changes)
    public void setSelectedPLayList(PlayList selected) throws BLLException
    {
        if (selected == null) 
        {
            throw new BLLException("No playlist selected!");
        }

        this.selectedPlayList = selected;
    }

    //Set the selected media to the new selection (for example, when the selection inside the songsTableView changes)
    public void setSelectedPMedia(UserMedia selected) throws BLLException
    {
        if (selected == null) 
        {
            throw new BLLException("No song  selected!");
        }

        this.selectedMedia = selected;
    }

    //Return the selected play list
    public PlayList getSelectedPlayList() throws BLLException 
    {
        if (selectedPlayList == null) 
        {
            throw new BLLException("No playlist selected!");
        }

        return selectedPlayList;
    }

    //Return the selected media
    public UserMedia getSelectedMedia() throws BLLException 
    {
        if (selectedMedia == null) 
        {
            throw new BLLException("No media selected!");
        }

        return this.selectedMedia;
    }
    
//******************************************************************************************************************************************************************//
//Other methods

    public boolean hasMedia() 
    {
        return selectedMedia != null;
    }


    public boolean hasPlayList() 
    {
        return selectedPlayList != null;
    }

    //Attempts to retrieve the metadata of the file associated with the URI
    public UserMedia getMetaData(URI path) throws BLLException
    {
        try 
        {
            return dalManger.getMetaData(path);
        }
        catch (DAException ex)
        {
            throw new BLLException(ex);
        }
    }
   
    public void playMedia(Media media) 
    {
        player.setMedia(media);
        player.play();
    }
   
    public void setVolume(double vol) 
    {
        player.setVolume(vol);
    }
    
    public Duration getCurrentTime() {
        return player.getCurrentTime();
    }
}
