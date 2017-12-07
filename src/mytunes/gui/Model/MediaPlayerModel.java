/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.Model;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import static javafx.beans.property.BooleanProperty.booleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.util.Duration;
import mytunes.BLL.BLLException;
import mytunes.BLL.BLLManager;
import mytunes.be.PlayList;
import mytunes.be.UserMedia;
import mytunes.be.Mode;

/**
 * Model class, responsible for separating the data from the display
 *
 * @author sebok
 */
public class MediaPlayerModel {

    private ObservableList<UserMedia> allMedia = FXCollections.observableArrayList(); //Contains all the songs
    private ObservableList<UserMedia> filteredList = FXCollections.observableArrayList();   //Contains the songs that match the current filter (if there is one)
    private ObservableList<PlayList> playlists = FXCollections.observableArrayList();
    private ObservableList<String> categories = FXCollections.observableArrayList();

    private BLLManager bllManager = new BLLManager();
    private static MediaPlayerModel instance;

    private Mode mediaMode;
    private Mode playListMode;

    public MediaPlayerModel() {
        allMedia.addListener(new ListChangeListener<UserMedia>() //Set up a change listener, so we can update the filtered list, when the main list changes
        {
            @Override
            public void onChanged(ListChangeListener.Change<? extends UserMedia> c) {
                filteredList.clear();
                filteredList.addAll(allMedia);
            }
        });
    }

    /**
     * If the model already have an instance return it.
     *
     * Otherwise create a new instance, and return that
     *
     * @return
     */
    public static MediaPlayerModel getInstance() {
        if (instance == null) {
            instance = new MediaPlayerModel();
        }

        return instance;
    }

//******************************************************************************************************************************************************************//
//Load data
    /**
     * Attempt to load the information from the DB
     *
     * @throws ModelException
     */
    public void loadDataFromDB() throws ModelException {
        try {
            allMedia.addAll(bllManager.loadMedia());
            playlists.addAll(bllManager.loadPlayLists());
            categories.addAll(bllManager.getCategories());
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }

//******************************************************************************************************************************************************************//
//Save data
    /**
     * Add a new category to the list
     *
     * @param category
     * @throws ModelException
     */
    public void addNewCategory(String category) throws ModelException {
        category = category.trim(); //Remove tailing and leading whitespaces

        if (category.isEmpty()) //Do not accept an empty string
        {
            throw new ModelException("Nothing to add");
        }

        if (categories.contains(category)) //Do not allow duplicate entries
        {
            throw new ModelException("Category is already in the list!");
        }

        categories.add(category);
    }

    /**
     * Create a new playlist with the supplied title, and save it to the list
     * and the DB
     *
     * @param title
     * @throws ModelException
     */
    public void createNewPlayList(String title) throws ModelException {
        if (title.isEmpty()) //Do not create a playlist with an empty titly
        {
            throw new ModelException("Empty title!");
        }

        PlayList newPlayList = new PlayList();
        newPlayList.setTitle(title);

        try {
            bllManager.saveNewPlayList(newPlayList);    //Attempt to save the playlist to the DB
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }

        playlists.add(newPlayList);
    }

    /**
     * Attempt saving the media into the DB and the memory
     *
     * @param selectedSong
     * @throws ModelException
     */
    public void addNewMedia(UserMedia selectedSong) throws ModelException {
        try {
            bllManager.addNewMedia(selectedSong);
            allMedia.add(selectedSong);
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }

    /**
     * Attempt to add the selected media to the selected play list
     *
     * @param selectedMedia
     * @param selectedPlayList
     * @throws ModelException
     */
    public void addMediaToPlayList(UserMedia selectedMedia, PlayList selectedPlayList) throws ModelException {
        for (UserMedia userMedia : selectedPlayList.getMediaList()) {
            if (userMedia.getArtist().equalsIgnoreCase(selectedMedia.getArtist()) && userMedia.getTitle().equalsIgnoreCase(selectedMedia.getTitle())) {
                throw new ModelException("Media is already in the list!");
            }
        }

        try {
            bllManager.addMediaToPlayList(selectedMedia, selectedPlayList);
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }

        selectedPlayList.addMedia(selectedMedia);
    }

//******************************************************************************************************************************************************************//
//Edit data
    /**
     * Try to update an already existing play list
     *
     * @param selectedPlayList
     * @throws ModelException
     */
    public void updatePlayList(PlayList selectedPlayList) throws ModelException {
        try {
            bllManager.updatePlayList(selectedPlayList); //Attempt to update the title of the selected play list in the DB
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }

        for (PlayList list : playlists) //Update the title of the play list stored in memory
        {
            if (list.getId() == selectedPlayList.getId()) {
                list.setTitle(selectedPlayList.getTitle());
                return;
            }
        }
    }

    /**
     * Edit an already existing media object
     *
     * @param workingUserMedia
     * @throws ModelException
     */
    public void editMedia(UserMedia workingUserMedia) throws ModelException {
        try {
            bllManager.updateMedia(workingUserMedia);
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }

        for (UserMedia userMedia : allMedia) {
            if (userMedia.getId() == workingUserMedia.getId()) {
                userMedia.setArtist(workingUserMedia.getArtist());
                userMedia.setCategory(workingUserMedia.getCategory());
                userMedia.setTitle(workingUserMedia.getTitle());
                userMedia.setPath(workingUserMedia.getPath());

                return;
            }
        }
    }

//******************************************************************************************************************************************************************//
//Delete data
    /**
     * Attempt to remove the media instance from the list and the DB
     *
     * @param selected
     * @throws ModelException
     */
    public void removeMedia(UserMedia selected) throws ModelException {
        for (PlayList list : playlists) {
            if (list.containsMedia(selected)) {
                removeMediaFromPlayList(selected, list);
            }
        }

        try {
            bllManager.deleteMedia(selected);
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }

        allMedia.remove(selected);
    }

    /**
     * Attempt to delete the song from the playlist
     *
     * @param selectedMedia
     * @param selectedPlayList
     * @throws ModelException
     */
    public void removeMediaFromPlayList(UserMedia selectedMedia, PlayList selectedPlayList) throws ModelException {
        if (!selectedPlayList.containsMedia(selectedMedia)) //This should never occur
        {
            throw new ModelException("This playlist does not contain the selected media!");
        }

        try {
            bllManager.removeMediaFromPlayList(selectedMedia, selectedPlayList);
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }

        selectedPlayList.removeMedia(selectedMedia);
    }

    /**
     * Attempt to remove the play list
     *
     * @param selected
     * @throws ModelException
     */
    public void removePlayList(PlayList selected) throws ModelException {
        if (selected != null) {
            List<UserMedia> inList = new ArrayList<>();

            inList.addAll(selected.getMediaList());

            for (UserMedia userMedia : inList) //Remove the UserMedia objects found in the play list before deleting it
            {
                removeMediaFromPlayList(userMedia, selected);
            }
        }

        try {
            bllManager.deletePlayList(selected);
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }

        playlists.remove(selected);
    }

//******************************************************************************************************************************************************************//
//Getters and setters
    /**
     * Returns the ObservableList containing the filtered songs
     *
     * @return
     */
    public ObservableList<UserMedia> getMedia() {
        return this.filteredList;
    }

    /**
     * Returns the ObservableList containing the play lists
     *
     * @return
     */
    public ObservableList<PlayList> getPlayLists() {
        return this.playlists;
    }

    /**
     * Returns all of the possible categories
     *
     * @return
     */
    public ObservableList<String> getCategories() {
        return this.categories;
    }

    public Mode getMediaMode() {
        return mediaMode;
    }

    public Mode getPlayListMode() {
        return playListMode;
    }

    public void setMediaMode(Mode mediaMode) {
        this.mediaMode = mediaMode;
    }

    public void setPlayListMode(Mode playListMode) {
        this.playListMode = playListMode;
    }

    /**
     * Update the information inside the BLL to contain the latest selection
     *
     * @param selected
     * @throws ModelException
     */
    public void setSelectedPlayList(PlayList selected) throws ModelException {
        try {
            bllManager.setSelectedPLayList(selected);
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }

    /**
     * Update the information inside the BLL to contain the latest selection
     *
     * @param selected
     * @throws ModelException
     */
    public void setSelectedMedia(UserMedia selected) throws ModelException {
        try {
            bllManager.setSelectedPMedia(selected);
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }

    /**
     * Returns the currently selected playlist, which is stored in the BLL
     *
     * @return
     * @throws ModelException
     */
    public PlayList getSelectedPlayList() throws ModelException {
        try {
            return bllManager.getSelectedPlayList();
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }

    /**
     * Return the currently selected media
     *
     * @return
     * @throws ModelException
     */
    public UserMedia getSelectedMedia() throws ModelException {
        try {
            return bllManager.getSelectedMedia();
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }
    
     /**
     * Indicates whether or not the player is currently playing a song
     * @return 
     */
    public BooleanProperty isPlaying()
    {
        return bllManager.isPlaying();
    }
    
    /**
     * Set the isPlaying property of the Player class
     * @param b 
     */
    public void setPlaying(boolean b)
    {
        bllManager.setPlaying(b);
    }
    
     /**
     * Get the currentlyPlaying string property
     * @return 
     */
    public StringProperty getCurrentlyPlayingString()
    {
        return bllManager.getPlayingString();
    }
//******************************************************************************************************************************************************************//
//Other methods
    /**
     * Filter the songs based on the supplied string
     *
     * @param search
     */
    public void searchString(String search) {
        filteredList.clear();

        if (search.isEmpty()) //If the string is empty, return all media
        {
            filteredList = allMedia;
        }

        search = search.toLowerCase();

        for (UserMedia userMedia : allMedia) {
            if (userMedia.getArtist().toLowerCase().contains(search) || userMedia.getTitle().toLowerCase().contains(search)) //If the artis's name or the title of the song contains the string, treat it as a match
            {
                filteredList.add(userMedia);
            }
        }
    }

    /**
     * Attempts to retrieve the metadata of the file associated with the URI
     *
     * @param path
     * @return
     * @throws ModelException
     */
    public UserMedia getMetaData(URI path) throws ModelException {
        try {
            return bllManager.getMetaData(path);
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }

    public void moveSongUp(UserMedia selected, PlayList list) throws ModelException {
        if (selected == null) {
            throw new ModelException("No song selected!");
        }

        if (list == null) {
            throw new ModelException("No play list selected!");
        }

        int index = list.getIndexOfMedia(selected);

        if (index == 0) {
            throw new ModelException("Media is alredy on the top of the list!");
        }

        list.moveSongUp(index);
    }

    public void moveSongDown(UserMedia selected, PlayList list) throws ModelException {
        if (selected == null) {
            throw new ModelException("No song selected!");
        }

        if (list == null) {
            throw new ModelException("No play list selected!");
        }

        int index = list.getIndexOfMedia(selected);

        if (index == list.getCount() - 1) {
            throw new ModelException("Media is alredy on the bottom of the list!");
        }

        list.moveSongDown(index);
    }

    //Play everything in a play list
    public void playMedia() throws ModelException {
        try {
             bllManager.playMedia();
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }

    public void setMedia(UserMedia media) throws ModelException {
        try {
            bllManager.setMedia(media);
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }

    public void pauseMedia() {
        bllManager.pauseMedia();
    }

    public void setVolume(double vol) {
        bllManager.setVolume(vol);
    }

    public Duration getCurrentTime() {
        return bllManager.getCurrentTime();
    }

    //Set the currently playing media to the next one in the list
    public void setNextMedia() throws ModelException {
        try {
            bllManager.nextMedia();
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }

    //Set the currently playing media to the previous one in the list
    public void setPreviousMedia() throws ModelException {
        try {
            bllManager.previousMedia();
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }

    public void setMedia(PlayList selectedPlayList) throws ModelException
    {
        try 
        {
            bllManager.setMedia(selectedPlayList);
        }
        catch (BLLException ex)
        {
            throw new ModelException(ex);
        }
    }

}
