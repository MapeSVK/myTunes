/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BLL;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import mytunes.be.PlayList;

/**
 *
 * @author sebok
 */
class Player {

    private MediaPlayer player;

    public Player(Media media) {
        player = new MediaPlayer(media);
    }

    public Player() {

    }

    public void setVolume(double value) {
        if (player != null) {
            player.setVolume(value);
        }
    }

    public void play() throws BLLException {
        try {
            player.play();
        }
        catch (Exception ex) {
            throw new BLLException(ex);
        }
    }

    public void pause() {
        player.pause();
    }

    public Duration getCurrentTime() {
        return player.currentTimeProperty().getValue();
    }

    public void play(PlayList list) throws BLLException {
        player = new MediaPlayer(list.getCurrentlyPlaying().getMedia());
        player.play();
        player.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                try {
                    player.stop();
                    list.setNextIndex();
                    setMedia(list.getCurrentlyPlaying().getMedia());
                    Player.this.play();
                }
                catch (BLLException ex) {
                    Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public void setMedia(Media media) throws BLLException {
        try {
            player = new MediaPlayer(media);
        }
        catch (NullPointerException ex) {
            throw new BLLException("You are trying to play a not existing media! Maybe the path of this song is not located on this computer?");
        }
    }
}
