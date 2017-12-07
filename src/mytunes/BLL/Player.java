/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BLL;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import mytunes.be.PlayList;
import mytunes.be.UserMedia;

/**
 *
 * @author sebok
 */
class Player {

    private MediaPlayer player;
    private PlayList list;
    private final BooleanProperty isPlaying = new SimpleBooleanProperty();
    private final StringProperty currentlyPlayingString = new SimpleStringProperty();
    private UserMedia currentMedia;
    
    public Player(Media media) {
        player = new MediaPlayer(media);
    }

    public Player() {
    }
    
    public String getCurrentlyPlayingString()
    {
        return currentlyPlayingString.get();
    }

    public void setCurrentlyPlayingString(String value)
    {
        currentlyPlayingString.set(value);
    }

    public StringProperty currentlyPlayingStringProperty()
    {
        return currentlyPlayingString;
    }
    
    public boolean isIsPlaying()
    {
        return isPlaying.get();
    }

    public void setIsPlaying(boolean value)
    {
        isPlaying.set(value);
    }

    public BooleanProperty isPlayingProperty()
    {
        return isPlaying;
    }

    public void setVolume(double value) {
        if (player != null) {
            player.setVolume(value);
        }
    }

    public void play() throws BLLException {
        try {
            player.play();
            isPlaying.set(true);
            setPlayingString(currentMedia);
        }
        catch (Exception ex) {
            throw new BLLException(ex);
        }
    }

    public void pause() {
        player.pause();
        currentlyPlayingString.set("PAUSED");
        isPlaying.set(false);
    }

    public Duration getCurrentTime() {
        return player.currentTimeProperty().getValue();
    }

    //Set the media to a single Media object, which will be played once
    public void setMedia(UserMedia media) throws BLLException {
        try {
            player = new MediaPlayer(media.getMedia());
            currentMedia = media;
        }
        catch (NullPointerException ex) {
            throw new BLLException("You are trying to play a not existing media! Maybe the path of this song is not located on this computer?");
        }
        
        player.setOnEndOfMedia(() ->
        {
            isPlaying.set(false);
            currentlyPlayingString.set("");
            player.stop();
        });
    }

    //Set the media using a play list
    //In this case, all the song on the play list will be played after eachother
    void setMedia(PlayList selectedPlayList) throws BLLException
    {
        list = selectedPlayList;
        player = new MediaPlayer(list.getCurrentlyPlaying().getMedia());
        currentMedia = list.getCurrentlyPlaying();
        
        player.setOnEndOfMedia(() ->
        {
            try
            {
                this.playNextSong();
            } catch (BLLException ex)
            {
                System.out.println(ex.getMessage());
            }
        });
    }

    //Play the next song on the list
    void playNextSong() throws BLLException
    {
        if (list == null)
        {
            throw new BLLException("No play list");
        }
        
        player.stop();
        list.setNextIndex();
        setMedia(list.getCurrentlyPlaying());
        setPlayingString(list.getCurrentlyPlaying());
        player.play();
        
    }

    //Play the previous song on the list
    void playPreviousSong() throws BLLException
    {
        if (list == null)
        {
            throw new BLLException("No play list");
        }
        
        player.stop();
        list.setPreviousIndex();
        setMedia(list.getCurrentlyPlaying());
        setPlayingString(list.getCurrentlyPlaying());
        player.play();
    }
    
    private void setPlayingString(UserMedia media)
    {
        currentlyPlayingString.setValue("Currently playing: " +  media.getArtist() + ": " + media.getTitle() );
    } 
}
