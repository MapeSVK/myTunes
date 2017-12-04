/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.Model;

import java.net.URI;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import mytunes.BLL.BLLException;
import mytunes.BLL.BLLManager;
import mytunes.be.PlayList;
import mytunes.be.UserMedia;

/**
 * Model class, responsible for separating the data from the display 
 * @author sebok
 */
public class MediaPlayerModel
{
    private ObservableList<UserMedia> allMedia = FXCollections.observableArrayList(); //Contains all the songs
    private ObservableList<UserMedia> filteredList = FXCollections.observableArrayList();   //Contains the songs that match the current filter (if there is one)
    private ObservableList<PlayList> playlists = FXCollections.observableArrayList();
    private ObservableList<String> categories = FXCollections.observableArrayList();

    private BLLManager bllManager = new BLLManager();
        
    private static MediaPlayerModel instance;
    
    public MediaPlayerModel()
    {
        allMedia.addListener(new ListChangeListener<UserMedia>()    //Set up a change listener, so we can update the filtered list, when the main list changes
        {
            @Override
            public void onChanged(ListChangeListener.Change<? extends UserMedia> c)
            {
                filteredList.clear();
                filteredList.addAll(allMedia);
            }
        });
    }
    
    //If the model already have an instance return it.
    //Otherwise create a new instance, and return that.
    public static MediaPlayerModel getInstance()
    {
        if (instance == null)
        {
            instance = new MediaPlayerModel();
        }
        
        return instance;
    }
    
    //Check if we have selected a media object
    public boolean hasSelectedMedia()
    {
        return bllManager.hasMedia();
    }
    
    //Check if we have selected a PlayList object
    public boolean hasSelectedPlayList()
    {
        return bllManager.hasPlayList();
    }
    
    //Attempt to load the information from the DB
    public void loadDataFromDB() throws ModelException
    {
        try
        {
            allMedia.addAll(bllManager.loadMedia());
            playlists.addAll(bllManager.loadPlayLists());
            categories.addAll(bllManager.getCategories());
        } 
        catch (BLLException ex)
        {
            throw new ModelException(ex);
        }
    }
    
    //Returns the ObservableList containing the filtered songs
    public ObservableList<UserMedia> getMedia()
    {
        return this.filteredList;
    }
    
    //Returns the ObservableList containing the play lists
    public ObservableList<PlayList> getPlayLists()
    {
       return this.playlists;
    }
       
    //Returns all of the possible categories
    public ObservableList<String> getCategories()
    {
        return this.categories;
    }
    
    //Add a new category to the list
    public void addNewCategory(String category) throws ModelException
    {
        category = category.trim(); //Remove tailing and leading whitespaces
        
        if (category.isEmpty()) //Do not accept an empty string
        {
            throw new ModelException("Nothing to add");
        }
                
        if (categories.contains(category))  //Do not allow duplicate entries
        {
            throw new ModelException("Category is already in the list!");
        }
        
        categories.add(category);
    }
    
    //Update the information inside the BLL to contain the latest selection
    public void setSelectedPlayList(PlayList selected) throws ModelException
    {
        try 
        {
            bllManager.setSelectedPLayList(selected);
        }
        catch (BLLException ex)
        {
            throw new ModelException(ex);
        }
    }
    
    //Update the information inside the BLL to contain the latest selection
    public void setSelectedMedia(UserMedia selected) throws ModelException
    {
        try
        {
            bllManager.setSelectedPMedia(selected);
        } 
        catch (BLLException ex)
        {
            throw new ModelException(ex);
        }
    }
    
    //Filter the songs based on the supplied string
    public void searchString(String search)
    {
        filteredList.clear();
        
        if (search.isEmpty())   //If the string is empty, return all media
        {
            filteredList = allMedia;
        }
        
        for (UserMedia userMedia : allMedia)
        {
            if (userMedia.getArtist().contains(search) || userMedia.getTitle().contains(search))    //If the artis's name or the title of the song contains the string, treat it as a match
            {
                filteredList.add(userMedia);
            }
        }
    }
    
    //Returns the currently selected playlist, which is stored in the BLL
    public PlayList getSelectedPlayList() throws ModelException
    {
        try
        {
            return bllManager.getSelectedPlayList();
        }
        catch (BLLException ex)
        {
            throw new ModelException(ex);
        }
    }
    
    //Create a new playlist with the supplied title, and save it to the list and the DB
    public void createNewPlayList(String title) throws ModelException
    {
        if (title.isEmpty())    //Do not create a playlist with an empty titly
        {
            throw new ModelException("Empty title!");
        }
        
        PlayList newPlayList = new PlayList();
        newPlayList.setTitle(title);
        
        try 
        {
            bllManager.saveNewPlayList(newPlayList);    //Attempt to save the playlist to the DB
        }
        catch (BLLException ex)
        {
            throw new ModelException(ex);
        }
        
        playlists.add(newPlayList);
    }
    
    //Try to update an already existing play list
    public void updatePlayList(PlayList selectedPlayList) throws ModelException
    {
        try
        {
            bllManager.updatePlayList(selectedPlayList); //Attempt to update the title of the selected play list in the DB
        } 
        catch (BLLException ex)
        {
            throw new ModelException(ex);
        }
        
        for (PlayList list : playlists) //Update the title of the play list stored in memory
        {
            if (list.getId() ==  selectedPlayList.getId())
            {
                list.setTitle(selectedPlayList.getTitle());
            }
        }
    }
    
    //Attempt saving the media into the DB and he memory
    public void addNewMedia(UserMedia selectedSong) throws ModelException
    {
        try 
        {
            bllManager.addNewMedia(selectedSong);
            allMedia.add(selectedSong);
        }
        catch (BLLException ex)
        {
            throw new ModelException(ex);
        }
    }
    
    //Return the currently selected media
    public UserMedia getSelectedMedia() throws ModelException
    {
        try
        {
            return bllManager.getSelectedMedia();
        } 
        catch (BLLException ex)
        {
            throw new ModelException(ex);
        }
    }
    
    //Attempts to retrieve the metadata of the file associated with the URI
    public UserMedia getMetaData(URI path) throws ModelException
    {
        try
        {
            return bllManager.getMetaData(path);
        } 
        catch (BLLException ex)
        {
            throw new ModelException(ex);
        }
    }

    //Attempt to remove the media instance from the list and the DB
    public void removeMedia(UserMedia selected) throws ModelException
    {
        try
        {
           bllManager.removeMedia(selected);
           allMedia.remove(selected);
        }
        catch(BLLException ex)
        {
            throw new ModelException(ex);
        }
    }
}
