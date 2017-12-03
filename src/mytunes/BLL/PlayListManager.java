/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BLL;

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
        if (newPlayList == null)
        {
            throw new BLLException("Play list does not exists!");
        }
        
        try
        {
            listDBManager.saveList(newPlayList);
        }
        catch (DAException ex)
        {
            throw new BLLException(ex);
        }    }

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
    
}
