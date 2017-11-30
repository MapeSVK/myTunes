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

    public List<UserMedia> getMedia() throws DAException {
        List<UserMedia> mediaList = new ArrayList();

        try (Connection con = cm.getConnection()) {
            PreparedStatement pstatement = con.prepareStatement("SELECT * FROM Music");
            ResultSet result = pstatement.executeQuery();
            while (result.next()) {
                UserMedia tempMedia = new UserMedia();
                tempMedia.setId(result.getInt("id"));
                tempMedia.setTitle(result.getString("title"));
                tempMedia.setArtist(result.getString("artist"));
                tempMedia.setCategory(result.getString("category"));
                tempMedia.setTime(result.getTime("time"));
                tempMedia.setPath(result.getString("path"));
                mediaList.add(tempMedia);
            }
        }
        catch (Exception e) {
            throw new DAException(e.getMessage());
        }
        return mediaList;
    }

    public void saveSongs(UserMedia media) throws DAException {
        try (Connection con = cm.getConnection()) {
            PreparedStatement pstatement = con.prepareStatement("INSERT INTO Music(title, artist, category, time, path)"
                    + "VALUES(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            pstatement.setString(1, media.getTitle());
            pstatement.setString(2, media.getArtist());
            pstatement.setString(3, media.getCategory());
            pstatement.setTime(4, media.getTime());
            pstatement.setString(5, media.getPath());
            int affected = pstatement.executeUpdate();
            if (affected < 1) {
                throw new DAException("Song could not be added!");
            }
            ResultSet rs = pstatement.getGeneratedKeys();
            if (rs.next()) {
                media.setId(rs.getInt(1));
            }
        }
        catch (Exception e) {
            throw new DAException(e.getMessage());
        }
    }


   

    public void removeMedia(UserMedia media) throws DAException {

        try (Connection con = cm.getConnection()) {
            PreparedStatement pstatement = con.prepareStatement("DELETE FROM Music WHERE id=?");
            pstatement.setInt(1, media.getId());
            int affected = pstatement.executeUpdate();
            if (affected < 1) {
                throw new DAException("Song could not be deleted!");
            }
        }
        catch (Exception e) {
            throw new DAException(e.getMessage());
        }
    }

    public void editSong(UserMedia media) throws DAException {
        try (Connection con = cm.getConnection()) {
            PreparedStatement pstatement = con.prepareStatement("UPDATE Music SET title=?, artist=?, category=?, time=?, path=? WHERE id=?");
            pstatement.setString(1, media.getTitle());
            pstatement.setString(2, media.getArtist());
            pstatement.setString(3, media.getCategory());
            pstatement.setTime(4, media.getTime());
            pstatement.setString(5, media.getPath());
            pstatement.setInt(6, media.getId());
            int affected = pstatement.executeUpdate();
            if (affected < 1) {
                throw new DAException("Song could not be edited!");
            }
        }
        catch (Exception e) {
            throw new DAException(e.getMessage());
        }
    }


    public void editPlaylist(PlayList plist) throws DAException {
        try (Connection con = cm.getConnection()) {
            PreparedStatement pstatement = con.prepareStatement("UPDATE Playlist SET title=? WHERE id=?");
            pstatement.setString(1, plist.getTitle());
            pstatement.setInt(2, plist.getId());
            int affected = pstatement.executeUpdate();
            if (affected < 1) {
                throw new DAException("Playlist could not be edited!");
            }
        }
        catch (Exception e) {
            throw new DAException(e.getMessage());
        }
    }

    public List<PlayList> getPlayLists() throws DAException {
        List<PlayList> playListList = new ArrayList();
        try (Connection con = cm.getConnection()) {
            PreparedStatement pstatement = con.prepareStatement("SELECT * FROM Playlist");
            ResultSet result = pstatement.executeQuery();
            while (result.next()) {
                PlayList tempList = new PlayList();
                tempList.setId(result.getInt("id"));
                tempList.setTitle(result.getString("title"));
                playListList.add(tempList);
            }

            PreparedStatement pstaStatement2 = con.prepareStatement(
                    "SELECT Music.* , MusicInList.listID "
                    + "FROM Music, MusicInList "
                    + "WHERE MusicInList.musicID = Music.id");
            ResultSet result2 = pstaStatement2.executeQuery();
            while (result2.next()) {
                for (PlayList playList : playListList) {
                    if (playList.getId() == result2.getInt("listID")) {
                        UserMedia tempSong = new UserMedia();
                        tempSong.setId(result2.getInt("id"));
                        tempSong.setTitle(result2.getString("title"));
                        tempSong.setArtist(result2.getString("artist"));
                        tempSong.setCategory(result2.getString("category"));
                        tempSong.setTime(result2.getTime("time"));
                        tempSong.setPath(result2.getString("path"));
                        playList.addMedia(tempSong);
                    }
                }
            }
        }
        catch (Exception e) {
            throw new DAException(e.getMessage());
        }
        return playListList;
    }

    public void deletePlayList(PlayList plist) throws DAException {
        try (Connection con = cm.getConnection()) {
            PreparedStatement pstatement = con.prepareStatement("DELETE FROM Playlist WHERE id=?");
            pstatement.setInt(1, plist.getId());
            int affected = pstatement.executeUpdate();
            if (affected < 1) {
                throw new DAException("Playlist could not be deleted!");
            }
        }
        catch (Exception e) {
            throw new DAException(e.getMessage());
        }
    }

    public void saveList(PlayList plist) throws DAException {
        try (Connection con = cm.getConnection()) {
            PreparedStatement pstatement = con.prepareStatement(
                    "INSERT INTO Playlist(title)"
                    + "VALUES(?)", Statement.RETURN_GENERATED_KEYS);
            pstatement.setString(1, plist.getTitle());
            int affected = pstatement.executeUpdate();
            if (affected < 1) {
                throw new DAException("Playlist could not be saved!");
            }
            ResultSet rs = pstatement.getGeneratedKeys();
            if (rs.next()) {
                plist.setId(rs.getInt(1));
            }
        }
        catch (Exception e) {
            throw new DAException(e.getMessage());
        }
    }

    public void saveSongToList(PlayList list, UserMedia media) throws DAException {
        try (Connection con = cm.getConnection()) {
            PreparedStatement pstatement = con.prepareStatement(
                    "INSERT INTO MusicInList(listID, musicID)"
                    + "VALUES(?, ?)");
            pstatement.setInt(1, list.getId());
            pstatement.setInt(2, media.getId());
            int affected = pstatement.executeUpdate();
            if (affected < 1) {
                throw new DAException("Media cannot be added to the playlist!");
            }
        }
        catch (Exception e) {
            throw new DAException(e.getMessage());
        }
    }

    public void deleteSongFromList(PlayList list, UserMedia media) throws DAException {
        try (Connection con = cm.getConnection()) {
            PreparedStatement pstatement = con.prepareStatement(
                    "DELETE FROM MusicInList WHERE musicID=? AND listID=?");
            pstatement.setInt(1, media.getId());
            pstatement.setInt(2, list.getId());
            int affected = pstatement.executeUpdate();
            if (affected < 1) {
                throw new DAException("Media cannot be deleted from the list!");
            }
        }
        catch (Exception e) {
            throw new DAException(e.getMessage());
        }
    }

    

}
