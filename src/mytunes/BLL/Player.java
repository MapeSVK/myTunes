/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BLL;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 *
 * @author sebok
 */
class Player
{
    private Media media;
    private double volume;
<<<<<<< HEAD
    private MediaPlayer mediaPlayer;  
=======
    private MediaPlayer player;    
>>>>>>> e88965e5d77fb00ba0dc286aed469d3c509e10af

    public Player(Media media) 
    {
       this.media = media;
    }

    public Player() {
    }
    
    public void play() {
        player.play();
    }
    
    public void pause() {
        player.pause();
    }
    
    public void setVolume(double volume) {
        this.volume = volume;
        //player.volumeProperty().setValue(this.volume);
    }

    public Duration getCurrentTime() {
        return player.currentTimeProperty().getValue();
    }

    public void setMedia(Media media) 
    {
       this.media = media;
<<<<<<< HEAD
       player = new MediaPlayer(media);
=======
       player = new MediaPlayer(this.media);
>>>>>>> e88965e5d77fb00ba0dc286aed469d3c509e10af
    }
    
}
