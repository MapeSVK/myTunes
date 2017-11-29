/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.be;

import java.util.ArrayList;
import java.util.List;
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
    
    //Check if a song is already in the playlist
    public boolean containsSong(UserMedia song)
    {
        return songs.contains(song);
    }
    
    @Override
    public String toString()
    {
        return "PlayList{ id=" + id + ", title=" + title + '}';
    }

}