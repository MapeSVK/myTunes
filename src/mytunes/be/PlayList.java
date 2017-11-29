/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.be;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Mape
 */
public class PlayList {

    private ObservableList<UserMedia> songs = FXCollections.observableArrayList();
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty title = new SimpleStringProperty();


    public PlayList() {
    }

    public PlayList(int id, String title) {
        this.id.set(id);
        this.title.set(title);
    }

    public String getTitle()
    {
        return title.get();
    }

    public void setTitle(String value)
    {
        title.set(value);
    }

    public StringProperty titleProperty()
    {
        return title;
    }
    
    public int getId()
    {
        return id.get();
    }

    public void setId(int value)
    {
        id.set(value);
    }

    public IntegerProperty idProperty()
    {
        return id;
    }

    public ObservableList<UserMedia> getSongs() {
        return songs;
    }

    public void setSongs(ObservableList<UserMedia> songs) {
        this.songs = songs;
    }

    //Add a song to the playlist
    public void addSong(UserMedia selectedSong)
    {
        songs.add(selectedSong);
    }
    
    //Remove the selected song from this playlist
    public void removeSong(UserMedia songToDelete)
    {
        songs.remove(songToDelete);
    }
    
    //Check if a song is already in the playlist
    public boolean containsSong(UserMedia song)
    {
        return songs.contains(song);
    }
    
    //Checks if the list of song is empty
    public boolean isEmpty()
    {
        return songs.isEmpty();
    }
    
    //Clears all the song from the play list
    public void clearSongs()
    {
        songs.clear();
    }
    
    //Gets the index of a song in the playlist
    public int getIndexOfSong(UserMedia selected)
    {
        int i = -1;
        
        for (UserMedia song : songs)
        {
            i++;
            if (selected.equals(song))
            {
                return i;
            }
        }
        
        return -1;
    }
    
    //Move the song with the specified index up
    public void moveSongUp(int index)
    {
        UserMedia switchSong = songs.get(index-1);
        songs.set(index-1, songs.get(index));
        songs.set(index, switchSong);
    }
    
    //Move the song with the specified index up
    public void moveSongDown(int index)
    {
        UserMedia switchSong = songs.get(index+1);
        songs.set(index+1, songs.get(index));
        songs.set(index, switchSong);
    }
    
    @Override
    public String toString()
    {
        return "PlayList{ id=" + id + ", title=" + title + '}';
    }




}