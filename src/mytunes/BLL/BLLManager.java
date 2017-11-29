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

/**
 *
 * @author sebok
 */
public class BLLManager
{
    private DAManager mediaDAO = new DAManager();
    private MediaPlayer player;
    
    private List<String> categories = new ArrayList(); //A collection of categories

    public void deleteSong(UserMedia selectedSong) throws BLLException
    {
        try
        {
            mediaDAO.removeSong(selectedSong);
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
            for (UserMedia song : selected.getSongs())
            {
                removeSongFromPlayList(song, selected);
            }
            selected.clearSongs();
        }
        
        mediaDAO.deletePlayList(selected);
    }

    //Save a new song to the DB
    public void addNewSong(UserMedia newSong) throws BLLException
    {
        try
        {
            mediaDAO.saveSongs(newSong);
        } 
        catch (DAException ex)
        {
            Logger.getLogger(BLLManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new BLLException(ex);
        }
    }

    public void addNewPlayList(PlayList pl) throws DAException
    {
        mediaDAO.saveList(pl);
    }

    public void play(PlayList playList)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //Use the DAL to load the songs, and categories
    public List<UserMedia> loadMedia(String filter) throws BLLException
    {
        try
        {
            List<UserMedia> media = mediaDAO.getSongs(filter);
            
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
            mediaDAO.saveSongToList(selectedPlayList, selectedSong);
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

    public void updateSong(UserMedia selectedSong) throws BLLException
    {
        try
        {
            mediaDAO.editSong(selectedSong);
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
            return mediaDAO.getPlayLists();
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
            mediaDAO.deleteSongFromList(selectedPlayList, songToDelete);
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
        mediaDAO.editPlaylist(selectedPlayList);
        }
        catch (DAException ex)
        {
            throw new BLLException(ex);
        }
    }
}
