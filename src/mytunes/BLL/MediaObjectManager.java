/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BLL;

import java.util.List;
import mytunes.be.UserMedia;
import mytunes.dal.DAException;
import mytunes.dal.MediaDBManager;

/**
 * Handles operations such as saving and retrieving UserMedia objects
 * @author sebok
 */
public class MediaObjectManager
{
    private MediaDBManager mediaDBManager = new MediaDBManager();

    //Load the data found in the DB
    List<UserMedia> getMedia() throws BLLException
    {
        try
        {
            return mediaDBManager.getMedia();
        }
        catch (DAException ex)
        {
            throw new BLLException(ex);
        }
    }
    
    
    //Save the data of the selected media to the DB
    void addNew(UserMedia selectedSong) throws BLLException
    {
        try
        {
            mediaDBManager.saveMedia(selectedSong);
        } 
        catch (DAException ex)
        {
            throw new BLLException(ex);
        }
    }
    
}
