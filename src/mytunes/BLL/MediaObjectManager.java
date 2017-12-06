/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BLL;

import java.util.ArrayList;
import java.util.List;
import mytunes.be.UserMedia;
import mytunes.dal.DAException;
import mytunes.dal.DALManager;

/**
 * Handles operations such as saving and retrieving UserMedia objects
 *
 * @author sebok
 */
public class MediaObjectManager {

    private List<String> categories;
    private DALManager dalManager;

    public MediaObjectManager(DALManager dm) {
        this.dalManager = dm;
    }

    //Load the data found in the DB
    List<UserMedia> getMedia() throws BLLException {
        try {
            List<UserMedia> uMediaList = dalManager.getAllMedia();
            categories = new ArrayList<>();
            for (UserMedia userMedia : uMediaList) //Filter out the categories
            {
                if (!categories.contains(userMedia.getCategory())) {
                    categories.add(userMedia.getCategory());
                }
            }
            return uMediaList;
        }
        catch (DAException ex) {
            throw new BLLException(ex);
        }
    }

    //Get the categories
    public List<String> getCategories() throws BLLException {
        if (categories == null) {
            throw new BLLException("No data has been red in!");
        }
        return this.categories;
    }

    //Save the data of the selected media to the DB
    void addNew(UserMedia selectedSong) throws BLLException {
        try {
            dalManager.saveMedia(selectedSong);
        }
        catch (DAException ex) {
            throw new BLLException(ex);
        }
    }

    //Attempt to remove the selected object from the DB
    void remove(UserMedia selected) throws BLLException {
        try {
            dalManager.deleteMedia(selected);
        }
        catch (DAException ex) {
            throw new BLLException(ex);
        }
    }

    //Update an existing media object
    void updateMedia(UserMedia selectedMedia) throws BLLException {
        try {
            dalManager.editMedia(selectedMedia);
        }
        catch (DAException ex) {
            throw new BLLException(ex);
        }
    }

}
