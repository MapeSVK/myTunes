/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.be;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mape
 */
public class PlayList {
    private int id;
    private String name;    
    private List<UserMedia> songs = new ArrayList();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserMedia> getSongs() {
        return songs;
    }

    public void setSongs(List<UserMedia> songs) {
        this.songs = songs;
    }
}