/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.Model;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunes.BLL.BLLManager;
import mytunes.be.PlayList;
import mytunes.be.UserMedia;

/**
 * Model class, responsible for separating the data from the display 
 * @author sebok
 */
public class MediaPlayerModel
{
    private ObservableList<UserMedia> songs = FXCollections.observableArrayList();
    private ObservableList<PlayList> playlists = FXCollections.observableArrayList();
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
    
    public void newSong() throws IOException
    {
        bllManager.addNewSong();
        
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

    public ObservableList<PlayList> getPlayLists()
    {
        return playlists;
    }

    public ObservableList<UserMedia> getSongs()
    {
        return songs;
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
    public void loadMedia(String filter)
    {
        songs.clear();
        songs.addAll(bllManager.loadMedia(filter));
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
        p.setName(playListName);
        playlists.add(p);
    }
}
