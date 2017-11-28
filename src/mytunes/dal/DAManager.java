/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import mytunes.be.PlayList;
import mytunes.be.UserMedia;

/**
 *
 * @author Dominik
 */
public class DAManager {

    private ConnectionManager cm = new ConnectionManager();
    
    private List<PlayList> playlistList = new ArrayList();
    
    public List<UserMedia> getSongs(String filter) {
        List<UserMedia> songList = new ArrayList();
        
        try (Connection con = cm.getConnection()) {
            PreparedStatement pstatement = con.prepareStatement("SELECT * FROM Music WHERE title LIKE ?");
            pstatement.setString(1, "%" + filter + "%");

            ResultSet result = pstatement.executeQuery();
            while (result.next()) {
                UserMedia tempSong = new UserMedia();
                tempSong.setTitle(result.getString("title"));
                tempSong.setArtist(result.getString("artist"));
                tempSong.setCategory(result.getString("category"));
                tempSong.setTime(result.getTime("time"));
                tempSong.setPath(result.getString("path"));

                songList.add(tempSong);
            }
        }
        catch (Exception e) {
        }
        
        for (UserMedia userMedia : songList) {
            System.out.println(userMedia.toString());
        }
        
        return songList;
    }
}
