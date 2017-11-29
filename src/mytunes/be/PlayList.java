/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.be;

import java.util.ArrayList;
import java.util.List;
import jdk.nashorn.internal.runtime.UserAccessorProperty;

/**
 *
 * @author Mape
 */
public class PlayList {
    private int id;
    private String title;    
    private List<UserMedia> songs = new ArrayList();

    public PlayList() {
    }

    public PlayList(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;

    }

    public List<UserMedia> getSongs() {
        return songs;
    }

    public void setSongs(List<UserMedia> songs) {
        this.songs = songs;
    }
    
    public void addSong(UserMedia song) {
        this.songs.add(song);
    }
}