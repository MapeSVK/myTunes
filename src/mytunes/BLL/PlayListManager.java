/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BLL;

import java.util.List;
import mytunes.be.PlayList;
import mytunes.be.UserMedia;
import mytunes.dal.DAException;
import mytunes.dal.DALManager;

/**
 * Handles operations such as saving and retrieving PlayList objects
 *
 * @author sebok
 */
public class PlayListManager
{

    private DALManager dalManager;

    public PlayListManager(DALManager dm)
    {
        this.dalManager = dm;
    }

<<<<<<< HEAD
    /**
     * Attempt to load the play list from the DB
     * 
     * @return List
     * @throws BLLException 
     */
    List<PlayList> loadPlayLists() throws BLLException {
        try {
=======
    //Attempt to load the play list from the DB
    List<PlayList> loadPlayLists() throws BLLException
    {
        try
        {
>>>>>>> e46496733085b22fdae456867cfadc6cbf190867
            return dalManager.getAllPlayList();
        }
        catch (DAException ex)
        {
            throw new BLLException(ex);
        }
    }

<<<<<<< HEAD
    /**
     * Attempt to save a new playlist into the DB 
     * 
     * @param newPlayList
     * @throws BLLException 
     */
    void saveNewPlayList(PlayList newPlayList) throws BLLException {
        try {
=======
    void saveNewPlayList(PlayList newPlayList) throws BLLException
    {
        try
        {
>>>>>>> e46496733085b22fdae456867cfadc6cbf190867
            dalManager.savePlayList(newPlayList);
        }
        catch (DAException ex)
        {
            throw new BLLException(ex);
        }
    }
<<<<<<< HEAD
    
    /**
     * Attempt to update existng playlist in the DB
     * 
     * @param selectedPlayList
     * @throws BLLException 
     */
    void updatePlayList(PlayList selectedPlayList) throws BLLException {
        try {
=======

    void updatePlayList(PlayList selectedPlayList) throws BLLException
    {
        try
        {
>>>>>>> e46496733085b22fdae456867cfadc6cbf190867
            dalManager.editList(selectedPlayList);
        }
        catch (DAException ex)
        {
            throw new BLLException(ex);
        }
    }

<<<<<<< HEAD
    /**
     * Remove the selected play list from the DB
     * 
     * @param selected
     * @throws BLLException 
     */
    void removePlayList(PlayList selected) throws BLLException {
        try {
=======
    //Remove the selected play list from the DB
    void removePlayList(PlayList selected) throws BLLException
    {
        try
        {
>>>>>>> e46496733085b22fdae456867cfadc6cbf190867
            dalManager.deletePlayList(selected);
        }
        catch (DAException ex)
        {
            throw new BLLException(ex);
        }
    }

<<<<<<< HEAD
    /**
     * Update the selected play list in the DB
     * @param selectedMedia
     * @param selectedPlayList
     * @throws BLLException 
     */
    void addMediaToPlayList(UserMedia selectedMedia, PlayList selectedPlayList) throws BLLException {
        try {
=======
    //Update the selected play list in the DB
    void addMediaToPlayList(UserMedia selectedMedia, PlayList selectedPlayList) throws BLLException
    {
        try
        {
>>>>>>> e46496733085b22fdae456867cfadc6cbf190867
            dalManager.addMediaToList(selectedPlayList, selectedMedia);
        } catch (DAException ex)
        {
            throw new BLLException(ex);
        }
    }

<<<<<<< HEAD
    /**
     * Update the selected play list in the DB
     * 
     * @param selectedMedia
     * @param selectedPlayList
     * @throws BLLException 
     */
    void removeSongFromPlayList(UserMedia selectedMedia, PlayList selectedPlayList) throws BLLException {
        try {
=======
    //Update the selected play list in the DB
    void removeSongFromPlayList(UserMedia selectedMedia, PlayList selectedPlayList) throws BLLException
    {
        try
        {
>>>>>>> e46496733085b22fdae456867cfadc6cbf190867
            dalManager.deleteMediaFromList(selectedPlayList, selectedMedia);
        } catch (DAException ex)
        {
            throw new BLLException(ex);
        }
    }
}
