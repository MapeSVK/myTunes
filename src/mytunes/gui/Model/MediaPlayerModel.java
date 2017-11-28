/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.Model;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunes.BLL.BLLException;
import mytunes.BLL.BLLManager;
import mytunes.be.PlayList;
import mytunes.be.UserMedia;
import mytunes.dal.DAException;

/**
 * Model class, responsible for separating the data from the display 
 * @author sebok
 */
public class MediaPlayerModel
{
    private ObservableList<UserMedia> songs = FXCollections.observableArrayList();
    private ObservableList<PlayList> playlists = FXCollections.observableArrayList();
    private ObservableList<String> categories = FXCollections.observableArrayList();
    
    private UserMedia selectedSong;
    private BLLManager bllManager = new BLLManager();
    
    
    /**
     * Delete a song. Update the the observable list, and call the method from the BLL
     */
    public void deleteSong(UserMedia selectedSong) throws ModelException
    {
        if (selectedSong == null)
        {
            throw new ModelException("No song selected!");
        }
        songs.remove(selectedSong);
        bllManager.deleteSong(selectedSong);
    }
    
    public void deletePlaylist()
    {
        bllManager.deletePlayList();
    }
    
    public void newPlayList()
    {
        bllManager.addNewPlayList();
    }
    
    public void editSong(UserMedia selectedSong) throws ModelException
    {
        if (selectedSong == null)
        {
            throw new ModelException("No song selected");
        }
        
        this.selectedSong  = selectedSong;
    }
    
    public void updateSong(UserMedia selectedSong) throws ModelException
    {
        try
        {
            bllManager.updateSong(selectedSong);
        } 
        catch (BLLException ex)
        {
            throw new ModelException(ex.getMessage());
        }
    }
   
    public void addNewSong(UserMedia newSong) throws ModelException
    {
        try
        {
            bllManager.addNewSong(newSong);
            songs.add(newSong);
        } 
        catch (BLLException ex)
        {
            Logger.getLogger(MediaPlayerModel.class.getName()).log(Level.SEVERE, null, ex);
            throw new ModelException(ex.getMessage());
        }
    }
    
    public void editPlaylist()
    {
        
    }

    
    public void previousMedia()
    {
        
    }
    
    public void nextMedia()
    {
        
    }
    
    public void volumeChanged()
    {
        
    }
    
    public void moveSongUp()
    {
    
    }
    
    public void moveSongDown()
    {
    
    }
    
    public void deleteSongFromPlaylist() 
    {
    
    }
    
    public void addSongToPlaylist()
    {
    
    }
    
    public void searchSong(String searchString)
    {
    
    }
    
    //Add a category to the list, to make it appear in the comboBox
    public void addNewCategory(String category) throws ModelException
    {
        //Do not allow empty categories
        if (category.equals(""))
        {
            throw new ModelException("Empty category is not allowed!");
        }
        
        //Do not add already existing categories
        if (categories.contains(category))
        {
            throw new ModelException("Category is already in the list!");
        }
        
        categories.add(category);
    }

    public ObservableList<PlayList> getPlayLists()
    {
        return playlists;
    }

    public ObservableList<UserMedia> getSongs()
    {
        return songs;
    }
    
    public ObservableList<String> getCategories()
    {
        return categories;
    }
    
    public void playMedia(PlayList playList) throws Exception
    {
        if (playList == null)
        {
            throw new ModelException("Empty object");
        }
        bllManager.play(playList);
    }

    //Get all songs from the database on startup
    public void loadMedia(String filter) throws ModelException
    {
        try
        {
            songs.clear();
            songs.addAll(bllManager.loadMedia(filter)); //Get the songs
            categories.addAll(bllManager.getCategories());  //Get the categories
        } 
        catch (DAException ex)
        {
            Logger.getLogger(MediaPlayerModel.class.getName()).log(Level.SEVERE, null, ex);
            throw new ModelException(ex.getMessage());
        }
    }
    
    public UserMedia getSelectedSong()
    {
        return this.selectedSong;
    }

    public void createNewPlayList(String playListName) throws ModelException
    {
        if (playListName.equals(""))
        {
            throw new ModelException("Empty name!");
        }
        PlayList p = new PlayList();
        p.setTitle(playListName);
        playlists.add(p);
    }

}
