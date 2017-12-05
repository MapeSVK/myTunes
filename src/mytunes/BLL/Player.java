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
class Player {

    private Media media;
    private MediaPlayer player;

    public Player(Media media) {
        this.media = media;
    }

    public Player() {

    }

    public void setVolume(double value) {
        if (player != null) {
            player.setVolume(value);
        }
    }

    public void play() {
        player.play();
    }

    public void pause() {
        player.pause();
    }

    public Duration getCurrentTime() {
        return player.currentTimeProperty().getValue();
    }

    public void setMedia(Media media) {
        this.media = media;
        player = new MediaPlayer(this.media);
    }

}
