/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BLL;

import javafx.scene.media.MediaPlayer;
import mytunes.be.PlayList;
import mytunes.be.UserMedia;

/**
 *
 * @author sebok
 */
class Player
{
    private MediaPlayer player;
    
    void play(PlayList playList)
    {
        for (UserMedia m : playList.getMediaList())
        {
            player = new MediaPlayer(m.getMedia());
            player.play();
        }
    }
    
}
