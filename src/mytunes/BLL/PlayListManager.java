/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BLL;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mytunes.be.PlayList;
import mytunes.dal.DAException;
import mytunes.dal.PlayListDBManager;

/**
 * Handles operations such as saving and retrieving PlayList objects
 * @author sebok
 */
public class PlayListManager
{
    private PlayListDBManager listDBManager = new PlayListDBManager();

    void saveNewPlayList(PlayList newPlayList) throws BLLException
    {
        try
        {
            listDBManager.saveList(newPlayList);
        }
        catch (DAException ex)
        {
            throw new BLLException(ex);
        }   
    }

    void updatePlayList(PlayList selectedPlayList) throws BLLException
    {
        try
        {
           listDBManager.editPlaylist(selectedPlayList);
        }
        catch (DAException ex)
        {
            throw new BLLException(ex);
        }
    }
    
    //Attempt to load the play list from the DB
    List<PlayList> loadPlayLists() throws BLLException
    {
        try
        {
            return listDBManager.getPlayLists();
        } 
        catch (DAException ex)
        {
            throw new BLLException(ex);
        }
    }
    
}
