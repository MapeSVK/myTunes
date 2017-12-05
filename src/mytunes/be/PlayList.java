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
    private double totalTimeInMiliseconds;
    private final StringProperty timeFormattedAsString = new SimpleStringProperty();
    
    public PlayList()
    {
        setCount(0);
        totalTimeInMiliseconds = 0;
    }
    
    public String getTimeFormattedAsString()
    {
        return timeFormattedAsString.get();
    }

    public void setTimeFormattedAsString(String value)
    {
        timeFormattedAsString.set(value);
    }

    public StringProperty timeFormattedAsStringProperty()
    {
        return timeFormattedAsString;
    }
    
    //Update the StringProperty to reflect the latest changes (addition or deletion)
    private void updateStringTime()
    {
        int day = (int)TimeUnit.SECONDS.toDays((long) totalTimeInMiliseconds);        
        double hours = TimeUnit.SECONDS.toHours((long) totalTimeInMiliseconds) - (day *24);
        double minute = TimeUnit.SECONDS.toMinutes((long) totalTimeInMiliseconds) - (TimeUnit.SECONDS.toHours((long) totalTimeInMiliseconds)* 60);
        double second = TimeUnit.SECONDS.toSeconds((long) totalTimeInMiliseconds) - (TimeUnit.SECONDS.toMinutes((long) totalTimeInMiliseconds) *60);

        timeFormattedAsString.setValue(String.format("%02d:%02d:%02d", hours, minute, second));
    }
    
    public int getCount()
    {
        return count.get();
    }

    public void setCount(int value)
    {
        count.set(value);
    }

    public IntegerProperty countProperty()
    {
        return count;
    }

    public PlayList(int id, String title) {
        this.id.set(id);
        this.title.set(title);
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String value) {
        title.set(value);
    }

    public StringProperty titleProperty() {
        return title;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int value) {
        id.set(value);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public ObservableList<UserMedia> getMediaList() {
        return mediaList;
    }

    public void setMediaList(ObservableList<UserMedia> mediaList) {
        this.mediaList = mediaList;
    }

    //Add a song to the playlist
    public void addMedia(UserMedia selectedMedia) 
    {
        mediaList.add(selectedMedia);
        totalTimeInMiliseconds += selectedMedia.getTime();
        updateStringTime();
        setCount(getCount()+1);
    }

    //Remove the selected song from this playlist
    public void removeMedia(UserMedia mediaToDelete) {
        mediaList.remove(mediaToDelete);
        totalTimeInMiliseconds -= mediaToDelete.getTime();
        updateStringTime();
        setCount(getCount()-1);
    }

    //Check if a song is already in the playlist
    public boolean containsMedia(UserMedia media) {
        return mediaList.contains(media);
    }

    //Checks if the list of song is empty
    public boolean isEmpty() {
        return mediaList.isEmpty();
    }

    //Clears all the song from the play list
    public void clearMediaList() {
        mediaList.clear();
    }

    //Gets the index of a song in the playlist
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
    public void moveSongUp(int index) 
    {
        UserMedia switchSong = mediaList.get(index - 1);
        mediaList.set(index - 1, mediaList.get(index));
        mediaList.set(index, switchSong);
    }

    //Move the song with the specified index up
    public void moveSongDown(int index) 
    {
        UserMedia switchSong = mediaList.get(index + 1);
        mediaList.set(index + 1, mediaList.get(index));
        mediaList.set(index, switchSong);
    }

    @Override
    public String toString() 
    {
        return "PlayList{ id=" + id + ", title=" + title + '}';
    }
}
