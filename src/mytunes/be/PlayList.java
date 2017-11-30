/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.be;

import java.sql.Time;
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

    private ObservableList<UserMedia> mediaList = FXCollections.observableArrayList();
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty title = new SimpleStringProperty();
    private int count; //The number of songs in the play list
    private Time time; //The total time of all the songs in the play list
    
    public PlayList() 
    {
    }

    public PlayList(int id, String title)
    {
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

    public ObservableList<UserMedia> getMediaList() 
    {
        return mediaList;
    }

    public void setMediaList(ObservableList<UserMedia> mediaList) 
    {
        this.mediaList = mediaList;
    }

    public int getCount()
    {
        this.count = mediaList.size();
        return count;
    }
    
    public Time getTime()
    {
        time.setTime(0);
        for (UserMedia userMedia : mediaList)
        {
           
        }
        return null;
    }
    
    //Add a song to the playlist
    public void addMedia(UserMedia selectedMedia)
    {
        mediaList.add(selectedMedia);
    }
    
    //Remove the selected song from this playlist
    public void removeMedia(UserMedia mediaToDelete)
    {
        mediaList.remove(mediaToDelete);
    }
    
    //Check if a song is already in the playlist
    public boolean containsMedia(UserMedia media)
    {
        return mediaList.contains(media);
    }
    
    //Checks if the list of song is empty
    public boolean isEmpty()
    {
        return mediaList.isEmpty();
    }
    
    //Clears all the song from the play list
    public void clearMediaList()
    {
        mediaList.clear();
    }
    
    //Gets the index of a song in the playlist
    public int getIndexOfMedia(UserMedia selected)
    {
        int i = -1;
        
        for (UserMedia media : mediaList)
        {
            i++;
            if (selected.equals(media))
            {
                return i;
            }
        }
        
        return -1;
    }
    
    //Move the song with the specified index up
    public void moveSongUp(int index)
    {
        UserMedia switchSong = mediaList.get(index-1);
        mediaList.set(index-1, mediaList.get(index));
        mediaList.set(index, switchSong);
    }
    
    //Move the song with the specified index up
    public void moveSongDown(int index)
    {
        UserMedia switchSong = mediaList.get(index+1);
        mediaList.set(index+1, mediaList.get(index));
        mediaList.set(index, switchSong);
    }
    
    @Override
    public String toString()
    {
        return "PlayList{ id=" + id + ", title=" + title + '}';
    }




}