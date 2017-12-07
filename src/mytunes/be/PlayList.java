/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.be;

import java.util.concurrent.TimeUnit;
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
    private final IntegerProperty count = new SimpleIntegerProperty();
    private double totalTimeInSeconds;
    private final StringProperty timeFormattedAsString = new SimpleStringProperty();
    private int currentlyPlayingIndex;
    
    /**
     *
     */
    public PlayList()
    {
        setCount(0);
        totalTimeInSeconds = 0;
        currentlyPlayingIndex = 0;
    }
    
    /**
     *
     * @return
     */
    public String getTimeFormattedAsString()
    {
        return timeFormattedAsString.get();
    }

    /**
     *
     * @param value
     */
    public void setTimeFormattedAsString(String value)
    {
        timeFormattedAsString.set(value);
    }

    /**
     *
     * @return
     */
    public StringProperty timeFormattedAsStringProperty()
    {
        return timeFormattedAsString;
    }
    
    //Update the StringProperty to reflect the latest changes (addition or deletion)
    private void updateStringTime()
    {
        long timeInLong = new Double(totalTimeInSeconds).longValue();
        
        int day = (int)TimeUnit.SECONDS.toDays(timeInLong);        
        long hours = TimeUnit.SECONDS.toHours(timeInLong) - (day *24);
        long minute = TimeUnit.SECONDS.toMinutes(timeInLong) - (TimeUnit.SECONDS.toHours(timeInLong)* 60);
        long second = TimeUnit.SECONDS.toSeconds(timeInLong) - (TimeUnit.SECONDS.toMinutes(timeInLong) *60);

        timeFormattedAsString.setValue(String.format("%02d:%02d:%02d", hours, minute, second));
    }
    
    /**
     *
     * @return
     */
    public int getCount()
    {
        return count.get();
    }

    /**
     *
     * @param value
     */
    public void setCount(int value)
    {
        count.set(value);
    }

    /**
     *
     * @return
     */
    public IntegerProperty countProperty()
    {
        return count;
    }

    /**
     *
     * @param id
     * @param title
     */
    public PlayList(int id, String title) {
        this.id.set(id);
        this.title.set(title);
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title.get();
    }

    /**
     *
     * @param value
     */
    public void setTitle(String value) {
        title.set(value);
    }

    /**
     *
     * @return
     */
    public StringProperty titleProperty() {
        return title;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id.get();
    }

    /**
     *
     * @param value
     */
    public void setId(int value) {
        id.set(value);
    }

    /**
     *
     * @return
     */
    public IntegerProperty idProperty() {
        return id;
    }

    /**
     *
     * @return
     */
    public ObservableList<UserMedia> getMediaList() {
        return mediaList;
    }

    /**
     *
     * @param mediaList
     */
    public void setMediaList(ObservableList<UserMedia> mediaList) {
        this.mediaList = mediaList;
    }

    //Add a song to the playlist

    /**
     *
     * @param selectedMedia
     */
    public void addMedia(UserMedia selectedMedia) 
    {
        mediaList.add(selectedMedia);
        totalTimeInSeconds += selectedMedia.getTime();
        updateStringTime();
        setCount(getCount()+1);
    }

    //Remove the selected song from this playlist

    /**
     *
     * @param mediaToDelete
     */
    public void removeMedia(UserMedia mediaToDelete) {
        mediaList.remove(mediaToDelete);
        totalTimeInSeconds -= mediaToDelete.getTime();
        updateStringTime();
        setCount(getCount()-1);
    }

    //Check if a song is already in the playlist

    /**
     *
     * @param media
     * @return
     */
    public boolean containsMedia(UserMedia media) {
        //return mediaList.contains(media);
        for (UserMedia userMedia : mediaList)
        {
            if (userMedia.getId() == media.getId())
            {
                return true;
            }
        }
        return false;
    }

    //Checks if the list of song is empty

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return mediaList.isEmpty();
    }

    //Clears all the song from the play list

    /**
     *
     */
    public void clearMediaList() {
        mediaList.clear();
    }

    //Gets the index of a song in the playlist

    /**
     *
     * @param selected
     * @return
     */
    public int getIndexOfMedia(UserMedia selected) {
        int i = -1;
        for (UserMedia media : mediaList) {
            i++;
            if (selected.equals(media)) {
                return i;
            }
        }
        return -1;
    }

    //Move the song with the specified index up

    /**
     *
     * @param index
     */
    public void moveSongUp(int index) 
    {
        UserMedia switchSong = mediaList.get(index - 1);
        mediaList.set(index - 1, mediaList.get(index));
        mediaList.set(index, switchSong);
    }

    //Move the song with the specified index up

    /**
     *
     * @param index
     */
    public void moveSongDown(int index) 
    {
        UserMedia switchSong = mediaList.get(index + 1);
        mediaList.set(index + 1, mediaList.get(index));
        mediaList.set(index, switchSong);
    }

    //Return the media that is currently being played

    /**
     *
     * @return
     */
    public UserMedia getCurrentlyPlaying()
    {
        return mediaList.get(currentlyPlayingIndex);
    }
    
    //Set the index to the next UserMedia object. 
    //If the end of the list is reached, loop around to the first one

    /**
     *
     */
    public void setNextIndex()
    {
        if (currentlyPlayingIndex < mediaList.size() - 1)
        {
            this.currentlyPlayingIndex++;
        }
        else
        {
            currentlyPlayingIndex = 0;
        }
    }
    
    //Set the index to the privious UserMedia object. 
    //If the beginnig of the list is reached, loop around to the last one

    /**
     *
     */
    public void setPreviousIndex()
    {
        if (currentlyPlayingIndex > 0)
        {
            this.currentlyPlayingIndex--;
        }
        else
        {
            currentlyPlayingIndex = mediaList.size() - 1;
        }
    }
    
    /**
     *
     * @return
     */
    @Override
    public String toString() 
    {
        return "PlayList{ id=" + id + ", title=" + title + '}';
    }
}
