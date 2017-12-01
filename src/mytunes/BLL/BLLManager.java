/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BLL;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import mytunes.be.PlayList;
import mytunes.be.UserMedia;
import mytunes.dal.DAException;
import mytunes.dal.MediaDBManager;
import mytunes.dal.MetaReader;
import mytunes.dal.PlayListDBManager;

/**
 *
 * @author sebok
 */
public class BLLManager {

    private MediaDBManager mediaManager = new MediaDBManager();
    private MetaReader metaReader = new MetaReader();
    private PlayListDBManager listManager = new PlayListDBManager();
    private MediaPlayer player;
    private List<String> categories = new ArrayList(); //A collection of categories
    private PlayList currentPlayList;

    public void deleteMedia(UserMedia selectedMedia) throws BLLException {
        try {
            mediaManager.removeMedia(selectedMedia);
        }
        catch (DAException ex) {
            Logger.getLogger(BLLManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new BLLException(ex);
        }
    }

    public void deletePlayList(PlayList selected) throws BLLException, DAException {
        if (selected == null) {
            throw new BLLException("No playlist selected!");
        }

        //If the playlist is not empty, delete the song first
        if (!selected.isEmpty()) {
            //Delete the song from the DB
            for (UserMedia media : selected.getMediaList()) {
                removeMediaFromPlayList(media, selected);
            }
            selected.clearMediaList();
        }
        listManager.deletePlayList(selected);
    }

    //Save a new song to the DB
    public void addNewMedia(UserMedia newMedia) throws BLLException {
        try {
            mediaManager.saveMedia(newMedia);
        }
        catch (DAException ex) {
            Logger.getLogger(BLLManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new BLLException(ex);
        }
    }

    public void addNewPlayList(PlayList pl) throws DAException 
    {
        listManager.saveList(pl);
    }

    //Attemt to play all the media in the selected playlist
    public void play() throws BLLException 
    {
        if (currentPlayList == null)
        {
            throw new BLLException("No playlist selected");
        }
        while  (true)
        {
            player = new MediaPlayer(currentPlayList.getCurrentlyPlaying().getMedia());
            currentPlayList.next();
        }
    }

    public void next() throws BLLException
    {
        if (currentPlayList == null)
        {
            throw new BLLException("No playlist selected");
        }
        
        currentPlayList.next();
    }

    public void previous() throws BLLException
    {
        if (currentPlayList == null)
        {
            throw new BLLException("No playlist selected");
        }
        
        currentPlayList.previous();
    }

    //Use the DAL to load the songs, and categories
    public List<UserMedia> loadMedia() throws BLLException {
        try {
            List<UserMedia> media = mediaManager.getMedia();

            for (UserMedia userMedia : media) //Loop through each song
            {
                if (!categories.contains(userMedia.getCategory())) //If its category is not yet saved, add it to the category list
                {
                    categories.add(userMedia.getCategory());
                }
            }
            return media;
        }
        catch (DAException ex) {
            throw new BLLException(ex);
        }
    }

    public void addMediaToPlayList(UserMedia selectedMedia, PlayList selectedPlayList) throws BLLException {
        try {
            listManager.saveMediaToList(selectedPlayList, selectedMedia);
        }
        catch (DAException ex) {
            throw new BLLException(ex);
        }
    }

    public List<String> getCategories() {
        return categories;
    }

    public void updateMedia(UserMedia selectedMedia) throws BLLException {
        try {
            mediaManager.editMedia(selectedMedia);
        }
        catch (DAException ex) {
            Logger.getLogger(BLLManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new BLLException(ex);
        }
    }

    public List<PlayList> getPlayLists() throws BLLException {
        try {
            return listManager.getPlayLists();
        }
        catch (DAException ex) {
            throw new BLLException(ex);
        }
    }

    //Remove a song from a play list in the DB
    public void removeMediaFromPlayList(UserMedia mediaToDelete, PlayList selectedPlayList) throws BLLException {
        try {
            listManager.deleteMediaFromList(selectedPlayList, mediaToDelete);
        }
        catch (DAException ex) {
            throw new BLLException(ex);
        }
    }

    public void updatePlayList(PlayList selectedPlayList) throws BLLException {
        try {
            listManager.editPlaylist(selectedPlayList);
        }
        catch (DAException ex) {
            throw new BLLException(ex);
        }
    }

    public void setCurrentPlayList(PlayList list)
    {
       this.currentPlayList = list; 
    }
    
    public UserMedia getMetaData(URI path) throws BLLException
    {
        try
        {
            return metaReader.getMetaData( path);
        } 
        catch (DAException ex)
        {
            throw new BLLException(ex);
        }
    }
}
