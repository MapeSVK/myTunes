/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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

    public List<UserMedia> getSongs(String filter) throws DAException {
        List<UserMedia> songList = new ArrayList();

        try (Connection con = cm.getConnection()) {
            PreparedStatement pstatement = con.prepareStatement("SELECT * FROM Music WHERE title LIKE ?");
            pstatement.setString(1, "%" + filter + "%");
            ResultSet result = pstatement.executeQuery();
            while (result.next()) {
                UserMedia tempSong = new UserMedia();
                tempSong.setId(result.getInt("id"));
                tempSong.setTitle(result.getString("title"));
                tempSong.setArtist(result.getString("artist"));
                tempSong.setCategory(result.getString("category"));
                tempSong.setTime(result.getTime("time"));
                tempSong.setPath(result.getString("path"));
                songList.add(tempSong);
            }
        }
        catch (Exception e) {
            throw new DAException(e.getMessage());
        }
        return songList;
    }
    
    public void saveSongs(UserMedia song) throws DAException{
        try (Connection con = cm.getConnection()) {
            PreparedStatement pstatement = con.prepareStatement("INSERT INTO Music(title, artist, category, time, path)"
                                                              + "VALUES(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            pstatement.setString(1, song.getTitle());
            pstatement.setString(2, song.getArtist());
            pstatement.setString(3, song.getCategory());
            pstatement.setTime(4, song.getTime());
            pstatement.setString(5, song.getPath());
            int affected = pstatement.executeUpdate();
            if (affected<1)
                throw new DAException("Song could not be added");
            
            ResultSet rs = pstatement.getGeneratedKeys();
            if (rs.next()) {
                song.setId(rs.getInt(1));
            }
        }
        catch (Exception e) {
            throw new DAException(e.getMessage());
        }
    }
}
