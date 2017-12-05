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
    private MediaPlayer player;
    
    public Player(Media media) {
        player = new MediaPlayer(media);
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
        player.volumeProperty().setValue(volume);
    }

    public Duration getCurrentTime() {
        return player.currentTimeProperty().getValue();
    }

    public void setMedia(Media media) {
       this.media = media;
    }
    
}
