/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.Model;

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
    private PlayList selectedPlayList;
    private BLLManager bllManager = new BLLManager();
    
    
    /**
     * Delete a song. Update the the observable list, and call the method from the BLL
     */
    public void deleteSong(UserMedia selectedSong) throws ModelException
    {
        try
        {
            if (selectedSong == null)
            {
                throw new ModelException("No song selected!");
            }
            songs.remove(selectedSong);
            bllManager.deleteSong(selectedSong);
        }
        catch (BLLException ex)
        {
            Logger.getLogger(MediaPlayerModel.class.getName()).log(Level.SEVERE, null, ex);
            throw new ModelException(ex.getMessage());
        }
    }
    
    public void deletePlaylist(PlayList selected) throws ModelException
    {
        try
        {
            bllManager.deletePlayList(selected);
            playlists.remove(selected);
        }
        catch (Exception ex)
        {
            throw new ModelException(ex.getMessage());
        }   
    }

    
    public void editSong(UserMedia selectedSong) throws ModelException
    {
        if (selectedSong == null)
        {
            throw new ModelException("No song selected");
        }
        
        this.selectedSong  = selectedSong;
    }
    
    /**
     * Add a new song to the UI, and update the DB
     * @param newSong
     * @throws ModelException 
     */
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
    
    public void editPlaylist(PlayList selectedPlaylist) throws ModelException
    {
        if (selectedPlaylist == null)
        {
            throw new ModelException("No playlist selected!");
        }
        
        this.selectedPlayList = selectedPlaylist;
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
    
    //Try to move the song up on the list
    public void moveSongUp(UserMedia selected, PlayList current) throws ModelException
    {
        if (selected == null)
        {
            throw new ModelException("No song selected!");
        }
        int index = current.getIndexOfSong(selected);
        
        if (index == 0)
        {
            throw new ModelException("Song is already at the top of the playlist!");
        }
        
        current.moveSongUp(index);
    }
    
    public void moveSongDown(UserMedia selected, PlayList current) throws ModelException
    {
        if (selected == null)
        {
            throw new ModelException("No song selected!");
        }
        
        int index = current.getIndexOfSong(selected);
        
        if (index == current.getSongs().size()-1)
        {
            throw new ModelException("Song is already at the bottom of the playlist!");
        }
        
        current.moveSongDown(index);
    }
    
    public void deleteSongFromPlaylist(UserMedia songToDelete, PlayList selectedPlayList) throws ModelException 
    {
        if (songToDelete == null)
        {
           throw new ModelException("No song selected!");
        }
 
        selectedPlayList.removeSong(songToDelete);
        
        try
        {
            bllManager.removeSongFromPlayList(songToDelete, selectedPlayList);
        } 
        catch (BLLException ex)
        {
            throw new ModelException(ex.getMessage());
        }
    }
    
    //Add the selecetd song to the selected playlist
    public void addSongToPlaylist(UserMedia selectedSong, PlayList selectedPlayList) throws ModelException
    {
        if (selectedSong == null)
        {
           throw new ModelException("No song selected!");
        }
        if (selectedPlayList == null)
        {
            throw new ModelException("No play list selected");
        }
        if (selectedPlayList.containsSong(selectedSong))
        {
            throw new ModelException("Playlist already contains this song!");
        }
        
        selectedPlayList.addSong(selectedSong);
        try
        {
            bllManager.addSongToPlayList(selectedSong, selectedPlayList);
        } 
        catch (BLLException ex)
        {
            throw new ModelException(ex.getMessage());
        }

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
        bllManager.play(playList);
    }

    //Get all songs, playlists and categories from the database on startup
    public void loadMedia() throws ModelException
    {
        try
        {
            songs.clear();
            songs.addAll(bllManager.loadMedia()); //Get the songs
            categories.addAll(bllManager.getCategories());  //Get the categories
            playlists.addAll(bllManager.getPlayLists());
        } 
        catch (BLLException ex)
        {
            Logger.getLogger(MediaPlayerModel.class.getName()).log(Level.SEVERE, null, ex);
            throw new ModelException(ex.getMessage());
        }
    }

    public void createNewPlayList(String playListName) throws ModelException
    {
        try
        {
            if (playListName.equals(""))
            {
                throw new ModelException("Empty name!");
            }
            PlayList p = new PlayList();
            p.setTitle(playListName);
            System.out.println(p.toString());
            
            for (PlayList pl : playlists)   //Loop through the playlist and check their names
            {
                if (pl.getTitle().equals(playListName)) //If the name is already in use, throw an exception
                {
                    throw new ModelException("Name is already in use!");
                }
            }
            
            playlists.add(p);
            bllManager.addNewPlayList(p);
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
    
    public PlayList getSelectedPlayList()
    {
        return this.selectedPlayList;
    }
    /**
     * Update the name of an already existing play list
     * @param selectedPlayList 
     */
    public void updatePlayList(PlayList selectedPlayList) throws ModelException
    {
        try
        {
            bllManager.updatePlayList(selectedPlayList);
        } 
        catch (BLLException ex)
        {
            throw new ModelException(ex.getMessage());
        }    
    }
    
     /**
     * Update the data of an already existing song
     * @param selectedSong
     * @throws ModelException 
     */
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
}
