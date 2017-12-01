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
import javafx.scene.media.Media;
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
    private ObservableList<UserMedia> allMedia = FXCollections.observableArrayList();
    private ObservableList<UserMedia> filteredList = FXCollections.observableArrayList();
    private ObservableList<PlayList> playlists = FXCollections.observableArrayList();
    private ObservableList<String> categories = FXCollections.observableArrayList();
    
    private UserMedia selectedMedia;
    private PlayList selectedPlayList;
    private BLLManager bllManager = new BLLManager();
    
    
    private static MediaPlayerModel instance;
    
    public static MediaPlayerModel getInstance()
    {
        if (instance == null)
        {
            instance = new MediaPlayerModel();
        }
        
        return instance;
    }
    
    /**
     * Delete a song. Update the the observable list, and call the method from the BLL
     */
    public void deleteMedia(UserMedia selectedMedia) throws ModelException
    {
        try
        {
            if (selectedMedia == null)
            {
                throw new ModelException("No song selected!");
            }
            
            for (PlayList list : playlists) //Remove the song from all the playlists 
            {
                if (list.containsMedia(selectedMedia))
                {
                    list.removeMedia(selectedMedia);
                }
            }
            
            filteredList.remove(selectedMedia);
            allMedia.remove(selectedMedia);
            bllManager.deleteMedia(selectedMedia);
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

    
    public void editMedia(UserMedia selectedMedia) throws ModelException
    {
        if (selectedMedia == null)
        {
            throw new ModelException("No song selected");
        }
        
        this.selectedMedia  = selectedMedia;
    }
    
    /**
     * Add a new song to the UI, and update the DB
     * @param newMedia
     * @throws ModelException 
     */
    public void addNewMedia(UserMedia newMedia) throws ModelException
    {
        try
        {
            bllManager.addNewMedia(newMedia);
            allMedia.add(newMedia);
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

    
    public void previousMedia() throws ModelException
    {
        try
        {
            bllManager.next();
        }
        catch (BLLException ex)
        {
            throw new ModelException(ex.getMessage());
        }
    }
    
    public void nextMedia() throws ModelException
    {
        try
        {
            bllManager.previous();
        } 
        catch (BLLException ex)
        {
            throw new ModelException(ex.getMessage());
        }
    }
    
    public void volumeChanged()
    {
        
    }
    
    //Try to move the song up on the list
    public void moveSelectionUp(UserMedia selected, PlayList current) throws ModelException
    {
        if (selected == null)
        {
            throw new ModelException("No song selected!");
        }
        int index = current.getIndexOfMedia(selected);
        
        if (index == 0)
        {
            throw new ModelException("Song is already at the top of the playlist!");
        }
        
        current.moveSongUp(index);
    }
    
    //Change the place of a song in a playlist (move down)
    public void moveMediaDown(UserMedia selected, PlayList current) throws ModelException
    {
        if (selected == null)
        {
            throw new ModelException("No song selected!");
        }
        
        int index = current.getIndexOfMedia(selected);
        
        if (index == current.getMediaList().size()-1)
        {
            throw new ModelException("Media is already at the bottom of the playlist!");
        }
        
        current.moveSongDown(index);
    }
    
    //Remove a song from at selected playlist
    public void deleteMediaFromPlaylist(UserMedia mediaToDelete, PlayList selectedPlayList) throws ModelException 
    {
        if (mediaToDelete == null)
        {
           throw new ModelException("No media selected!");
        }
 
        selectedPlayList.removeMedia(mediaToDelete);
        
        try
        {
            bllManager.removeMediaFromPlayList(mediaToDelete, selectedPlayList);
        } 
        catch (BLLException ex)
        {
            throw new ModelException(ex.getMessage());
        }
    }
    
    //Add the selecetd song to the selected playlist
    public void addSongToPlaylist(UserMedia selectedMedia, PlayList selectedPlayList) throws ModelException
    {
        if (selectedMedia == null)
        {
           throw new ModelException("No song selected!");
        }
        if (selectedPlayList == null)
        {
            throw new ModelException("No play list selected");
        }

        if (selectedPlayList.containsMedia(selectedMedia))
        {
            throw new ModelException("Playlist already contains this song!");
        }
        
        selectedPlayList.addMedia(selectedMedia);
        try
        {
            bllManager.addMediaToPlayList(selectedMedia, selectedPlayList);
        } 
        catch (BLLException ex)
        {
            throw new ModelException(ex.getMessage());
        }

    }
    
    //Search for a given string, and use the matching songs to populate the table view.
    public void searchForMedia(String searchString)
    {
        filteredList.clear();
        
        if (searchString.isEmpty()) //If the filter is empty, add all songs to the list
        {
            filteredList.addAll(allMedia);
            return;
        }
        
        for (UserMedia userMedia : allMedia) //Otherwise, loop through all the songs, and add them to the list, if they contain the search word
        {
            if (userMedia.getTitle().contains(searchString) || userMedia.getArtist().contains(searchString))
            {
                filteredList.add(userMedia);
            }
        }
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
    
    //Attempt to play the songs in the selected play list
    public void playMedia() throws ModelException
    {
        try
        {
            bllManager.play();
        }
        catch (Exception ex)
        {
            throw new  ModelException(ex.getMessage());
        }
    }

    //Get all songs, playlists and categories from the database on startup
    public void loadMedia() throws ModelException
    {
        try
        {
            allMedia.clear();
            allMedia.addAll(bllManager.loadMedia()); //Get the songs
            categories.addAll(bllManager.getCategories());  //Get the categories
            playlists.addAll(bllManager.getPlayLists());
            filteredList.addAll(allMedia);
        } 
        catch (BLLException ex)
        {
            Logger.getLogger(MediaPlayerModel.class.getName()).log(Level.SEVERE, null, ex);
            throw new ModelException(ex.getMessage());
        }
    }
    
    //Tries to create a new play list
    public void createNewPlayList(String playListName) throws ModelException
    {
        try
        {
            if (playListName.equals(""))
            {
                throw new ModelException("Empty name!");    //Do not create a playlist with an empty name
            }
            
            PlayList p = new PlayList();
            p.setTitle(playListName);
            
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
    
    public UserMedia getSelectedMedia()
    {
        return this.selectedMedia;
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
    public void updateMedia(UserMedia selectedSong) throws ModelException
    {
        try
        {
            bllManager.updateMedia(selectedSong);
        } 
        catch (BLLException ex)
        {
            throw new ModelException(ex.getMessage());
        }
    }
    
    
    public ObservableList<PlayList> getPlayLists()
    {
        return playlists;
    }

    public ObservableList<UserMedia> getMedia()
    {
        return filteredList;
    }
    
    public ObservableList<String> getCategories()
    {
        return categories;
    }
    
    public void getMetaData(String path)
    {
        bllManager.getMetaData(path);
    }
    
    //Sets the current play list in the BLLManager.
    public void setPlayList(PlayList list)
    {
        bllManager.setCurrentPlayList(list);
    }
}
