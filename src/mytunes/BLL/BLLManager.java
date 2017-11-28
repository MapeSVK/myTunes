/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BLL;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.media.MediaPlayer;
import mytunes.be.PlayList;
import mytunes.be.UserMedia;
import mytunes.dal.DAException;
import mytunes.dal.DAManager;

/**
 *
 * @author sebok
 */
public class BLLManager
{
    private DAManager mediaDAO = new DAManager();
    private MediaPlayer player;
    
    private List<String> categories = new ArrayList(); //A collection of categories

    public void deleteSong(UserMedia selectedSong)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void deletePlayList()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void addNewSong(UserMedia newSong) throws BLLException
    {
        try
        {
            mediaDAO.saveSongs(newSong);
        } 
        catch (DAException ex)
        {
            Logger.getLogger(BLLManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new BLLException(ex);
        }
    }

    public void addNewPlayList()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void play(PlayList playList)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<UserMedia> loadMedia(String filter) throws DAException
    {
        List<UserMedia> media = mediaDAO.getSongs(filter);
        
        for (UserMedia userMedia : media)   //Loop through each song
        {
            if (!categories.contains(userMedia.getCategory())) //If its category is not yet saved, add it to the category list
            {
                categories.add(userMedia.getCategory());
            }
        }
        
        return media;
    }
    
    public List<String> getCategories()
    {
        return categories;
    }

    public void updateSong(UserMedia selectedSong) throws BLLException
    {
        try
        {
            mediaDAO.editSong(selectedSong);
        }
        catch (DAException ex)
        {
            Logger.getLogger(BLLManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new BLLException(ex);
        }
    }
    
    
}
