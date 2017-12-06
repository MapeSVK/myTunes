/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BLL;

import java.lang.reflect.InvocationTargetException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 *
 * @author sebok
 */
class Player
{

    private MediaPlayer player;

    public Player(Media media)
    {
        player = new MediaPlayer(media);
    }

    public Player()
    {

    }

    public void setVolume(double value)
    {
        if (player != null)
        {
            player.setVolume(value);
        }
    }

    public void play() throws BLLException
    {
        try
        {
            player.play();    
        }
        catch (Exception ex)
        {
            throw new BLLException("You are trying to play a not existing media! Maybe the path of this song is not located on this computer?");
        }
    }

    public void pause()
    {
        player.pause();
    }

    public Duration getCurrentTime()
    {
        return player.currentTimeProperty().getValue();
    }

    public void setMedia(Media media) throws BLLException
    {
        try
        {
             player = new MediaPlayer(media);
        }
        catch (NullPointerException ex)
        {
            throw new BLLException("You are trying to play a not existing media! Maybe the path of this song is not located on this computer?");
        }
    }
}
