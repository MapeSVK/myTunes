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
import mytunes.be.PlayList;
import mytunes.be.UserMedia;
import mytunes.dal.DAException;
import mytunes.dal.MediaDBManager;
import mytunes.dal.MetaReader;
import mytunes.dal.PlayListDBManager;

/**
 *
 * @author sebok
 */
public class BLLManager 
{    
    private PlayList selectedPlayList; //The currently selected play list
    private UserMedia selectedMedia; //The currently selected media
    
    private MediaObjectManager mediaObjectManager = new MediaObjectManager();
    private PlayListManager playListManager = new PlayListManager();
    private MetaReader metaReader = new MetaReader();
    
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
    public UserMedia getSelectedMedia()throws BLLException
    {
        if (selectedMedia == null)
        {
            throw new BLLException("No media selected!");
        }
        
        return this.selectedMedia;
    }
    
    public boolean hasMedia()
    {
        return selectedMedia != null;
    }

    public boolean hasPlayList()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    
    //Attempt to update a play list in the DB
    public void updatePlayList(PlayList selectedPlayList) throws BLLException
    {
        playListManager.updatePlayList(selectedPlayList);
    }

    public void addNewMedia(UserMedia selectedSong) throws BLLException
    {
        if (selectedSong == null)
        {
            throw new BLLException("No media!");
        }
        mediaObjectManager.addNew(selectedSong);
    }
    
    //Attempts to retrieve the metadata of the file associated with the URI
    public UserMedia getMetaData(URI path) throws BLLException
    {
        try
        {
            return metaReader.getMetaData(path);
        } 
        catch (DAException ex)
        {
            throw new BLLException(ex);
        }
    }
    
    //Attempt to remove the media instance from the list and the DB
    public void removeMedia(UserMedia selected) throws BLLException
    {
        if (selected == null)
        {
            throw new BLLException("No song selected!");
        }
        
        mediaObjectManager.remove(selected);
    }
    
    //Add the selected media to the selected play list
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
    
    //Remove the selected medi from the selected play list
    public void removeMediaFromPlayList(UserMedia selectedMedia, PlayList selectedPlayList) throws BLLException
    {

        if (selectedMedia == null)
        {
            throw new BLLException("No media selected");
        }
        
        if (selectedPlayList == null)
        {
            throw new BLLException("No play list selected!");
        }
        
        playListManager.removeSongFromPlayList(selectedMedia, selectedPlayList);
    }
}
