/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BLL;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.media.MediaPlayer;
import mytunes.be.PlayList;
import mytunes.be.UserMedia;
import mytunes.dal.DAException;
import mytunes.dal.DAManager;

import mytunes.dal.MediaManager;

import mytunes.dal.PlayListManager;


/**
 *
 * @author sebok
 */
public class BLLManager
{
    private DAManager mediaDAO = new DAManager();
    private MediaManager mediaM = new MediaManager();

    private PlayListManager listManager = new PlayListManager();
    private MediaPlayer player;
    
    private List<String> categories = new ArrayList(); //A collection of categories


   

    public void deleteMedia(UserMedia selectedMedia) throws BLLException
    {
        try
        {
            mediaDAO.removeMedia(selectedMedia);

        } 
        catch (DAException ex)
        {
            Logger.getLogger(BLLManager.class.getName()).log(Level.SEVERE, null, ex);
            throw  new BLLException(ex);
        }
    }

    public void deletePlayList(PlayList selected) throws BLLException, DAException
    {
        if (selected == null)
        {
            throw new BLLException("No playlist selected!");
        }
        
        //If the playlist is not empty, delete the song first
        if (!selected.isEmpty())
        {
            //Delete the song from the DB
            for (UserMedia song : selected.getMediaList())
            {
                removeSongFromPlayList(song, selected);
            }
            selected.clearMediaList();
        }
        
        listManager.deletePlayList(selected);
    }

    //Save a new song to the DB
    public void addNewMedia(UserMedia newMedia) throws BLLException
    {
        try
        {
            mediaM.saveMedia(newMedia);
        } 
        catch (DAException ex)
        {
            Logger.getLogger(BLLManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new BLLException(ex);
        }
    }

    public void addNewPlayList(PlayList pl) throws DAException
    {
        listManager.saveList(pl);
    }

    public void play(PlayList playList)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //Use the DAL to load the songs, and categories
    public List<UserMedia> loadMedia() throws BLLException
    {
        try
        {
            List<UserMedia> media = mediaM.getMedia();
            
            for (UserMedia userMedia : media)   //Loop through each song
            {
                if (!categories.contains(userMedia.getCategory())) //If its category is not yet saved, add it to the category list
                {
                    categories.add(userMedia.getCategory());
                }
            }
            
            return media;
        } 
        catch (DAException ex)
        {
            throw new BLLException(ex);
        }
    }
    
    
    public void addSongToPlayList(UserMedia selectedSong, PlayList selectedPlayList) throws BLLException
    {
        try
        {
            listManager.saveMediaToList(selectedPlayList, selectedSong);
        } 
        catch (DAException ex)
        {
            throw new BLLException(ex);
        }
    }
    
    public List<String> getCategories()
    {
        return categories;
    }

    public void updateMedia(UserMedia selectedMedia) throws BLLException
    {
        try
        {
            mediaM.editMedia(selectedMedia);
        }
        catch (DAException ex)
        {
            Logger.getLogger(BLLManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new BLLException(ex);
        }
    }

    public List<PlayList> getPlayLists() throws BLLException
    {
        try
        {
            return listManager.getPlayLists();
        } 
        catch (DAException ex)
        {
            throw new BLLException(ex);
        }
    }
    
    //Remove a song from a play list in the DB
    public void removeSongFromPlayList(UserMedia songToDelete, PlayList selectedPlayList) throws BLLException
    {
        try
        {
            listManager.deleteMediaFromList(selectedPlayList, songToDelete);
        } 
        catch (DAException ex)
        {
            throw new BLLException(ex);
        }
    }

    public void updatePlayList(PlayList selectedPlayList) throws BLLException
    {
        try
        {
        listManager.editPlaylist(selectedPlayList);
        }
        catch (DAException ex)
        {
            throw new BLLException(ex);
        }
    }
}
