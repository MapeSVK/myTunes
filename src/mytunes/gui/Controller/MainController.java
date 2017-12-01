/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import mytunes.be.PlayList;
import mytunes.be.UserMedia;
import mytunes.gui.Model.MediaPlayerModel;
import mytunes.gui.Model.ModelException;

/**
 *
 * @author Mape
 */
public class MainController implements Initializable 
{
    @FXML
    private ImageView next;
    @FXML
    private ImageView previous;
    @FXML
    private ImageView play;
    @FXML
    private ImageView search;
    @FXML
    private ImageView addArrow;
    @FXML
    private ImageView upArrow;
    @FXML
    private ImageView downArrow;
    @FXML
    private Slider volumeController;
    @FXML
    private Label songName;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<PlayList> playlistTableView;
    @FXML
    private ListView<UserMedia> playlistSongsListView;
    @FXML
    private TableView<UserMedia> songsTableView;
    @FXML
    private TableColumn<PlayList, String> playListColumnName;
    @FXML
    private TableColumn<PlayList, String> playListColumnSongsCount;
    @FXML
    private TableColumn<PlayList, String> playListColumnTotalTime;
    @FXML
    private TableColumn<UserMedia, String> songsColumnTitle;
    @FXML
    private TableColumn<UserMedia, String> songsColumArtist;
    @FXML
    private TableColumn<UserMedia, String> songsColumnTime;
    @FXML
    private TableColumn<UserMedia, String> songsColumnCategory;
        
    private final Image img_next = new Image("file:images/next.png");
    private final Image img_previous = new Image("file:images/previous.png");
    private final Image img_play = new Image("file:images/play.png");
    private final Image img_search = new Image("file:images/search.png");
    private final Image img_down = new Image("file:images/down.png");
    private final Image img_up = new Image("file:images/up.png");
    private final Image img_addArrow = new Image("file:images/previous.png");
    
    private MediaPlayerModel model;

    public void initialize(URL url, ResourceBundle rb) {
        next.setImage(img_next);
        previous.setImage(img_previous);
        play.setImage(img_play);
        search.setImage(img_search);
        upArrow.setImage(img_up);
        downArrow.setImage(img_down);
        addArrow.setImage(img_addArrow);
        
        model = MediaPlayerModel.getInstance();
        
        setUpPlayListCellFactories();
        setUpSongsCellFactories();
        
        loadMedia();
        
        playlistTableView.setItems(model.getPlayLists());
        songsTableView.setItems(model.getMedia());
        
        
        //Create a new listener and bind it to the play list tableView. Used to update the listview of the current playlist, when the selection changes
        playlistTableView.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener()
            {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue)
                {
                    updateListView();
                }
        }
        );
        
        //Add a new event handler, so that search can be performed by pressing enter
        searchField.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event)
            {
                if (event.getCode() == KeyCode.ENTER)
                {
                    String searchString = searchField.getText();
                    searchForString(searchString);
                }
            }
        });
    }    

    //Update the listView to show the songs found in the selected play list
    private void updateListView()
    {
        PlayList selectedPlayList = playlistTableView.getSelectionModel().getSelectedItem();
        if (selectedPlayList != null)
        {
            playlistSongsListView.setItems(selectedPlayList.getMediaList());
        }
    }
    
    private void setUpPlayListCellFactories()
    {
        //Set up cell factories
        playListColumnName.setCellValueFactory(new PropertyValueFactory("title"));
        playListColumnSongsCount.setCellValueFactory(new PropertyValueFactory("count"));
        playListColumnTotalTime.setCellValueFactory(new PropertyValueFactory("totalTime"));
        
        //Set the width of the columns
        playlistTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        playListColumnName.prefWidthProperty().bind(playlistTableView.widthProperty().divide(2));
        playListColumnSongsCount.prefWidthProperty().bind(playlistTableView.widthProperty().divide(4));
        playListColumnTotalTime.prefWidthProperty().bind(playlistTableView.widthProperty().divide(4));
    }

    private void setUpSongsCellFactories()
    {
        //Set up cell factories
        songsColumnTitle.setCellValueFactory(new PropertyValueFactory("title"));
        songsColumArtist.setCellValueFactory(new PropertyValueFactory("artist"));
        songsColumnCategory.setCellValueFactory(new PropertyValueFactory("category"));
        songsColumnTime.setCellValueFactory(new PropertyValueFactory("timeString"));
        
        //Set the width of the columns
        songsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        songsColumnTitle.prefWidthProperty().bind(songsTableView.widthProperty().divide(4));
        songsColumArtist.prefWidthProperty().bind(songsTableView.widthProperty().divide(4));
        songsColumnCategory.prefWidthProperty().bind(songsTableView.widthProperty().divide(4));
        songsColumnTime.prefWidthProperty().bind(songsTableView.widthProperty().divide(4));

    }

    
    @FXML
    private void nextArrowClicked(MouseEvent event) 
    {
        try
        {
            model.nextMedia();
        } 
        catch (ModelException ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }

    @FXML
    private void previousArrowClicked(MouseEvent event)
    {
        try
        {
            model.previousMedia();
        } 
        catch (ModelException ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }
    
    //Attempt to play the songs in the selected playlist
    @FXML
    private void playArrowClicked(MouseEvent event) {
        try
        {
            PlayList playList = playlistTableView.getSelectionModel().getSelectedItem(); //Get the selected playlist
            songName.setText("Currently playing: " + playList.getCurrentlyPlaying().getTitle()); //Get the currently playing song, and set the label text to its title
            model.playMedia();  //Attempt to play the songs
        } 
        catch (Exception ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }
    
    //Searc the songs for the entered text
    @FXML
    private void searchClicked(MouseEvent event) 
    {
        String searchString = searchField.getText();
        searchForString(searchString);
    }
    
    //Runs when the user clicks the "Search" button (image), or presses enter while inside the searchbox
    private void searchForString(String searchString)
    {
        model.searchForMedia(searchString);
    }

    //Adds a selected song to the selected playlist
    @FXML
    private void addArrowClicked(MouseEvent event) 
    {
        try
        {
            UserMedia selectedSong = songsTableView.getSelectionModel().getSelectedItem();
            PlayList selectedPlayList = playlistTableView.getSelectionModel().getSelectedItem();
            
            model.addSongToPlaylist(selectedSong, selectedPlayList);
        }
        catch (ModelException ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }
    
    //Deletes the slected song from the playlists it is on
    @FXML
    private void deleteSongFromPlaylistClicked(ActionEvent event) 
    {
        try
        {
            UserMedia selectedSong = playlistSongsListView.getSelectionModel().getSelectedItem();
            PlayList selectedPlayList = playlistTableView.getSelectionModel().getSelectedItem();
            
            model.deleteMediaFromPlaylist(selectedSong, selectedPlayList);
        } 
        catch (ModelException ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }
    
    //Opens a new window whixh allows us to name a new window
    @FXML
    private void addNewPlaylistClicked(ActionEvent event)
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/View/NewPlayList.fxml"));
            
            Parent root1 = (Parent) fxmlLoader.load();
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } 
        catch (IOException ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }
    
    //Opens a the same window that thw addNewPlayList() uses, but fills out the text field with the name of the selected playlist.
    //Allows us to change the name of said playlist
    @FXML
    private void editPlaylistClicked(ActionEvent event) 
    {
        try
        {
            PlayList selectedPlayList = playlistTableView.getSelectionModel().getSelectedItem();
            model.editPlaylist(selectedPlayList);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/View/NewPlayList.fxml"));
            
            Parent root1 = (Parent) fxmlLoader.load();
            
            NewPlayListController controller = fxmlLoader.getController();
            controller.setText(playlistTableView.getSelectionModel().getSelectedItem());
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } 
        catch (Exception ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }

    //Try to delete the selected playlist
    @FXML
    private void deletePlaylistClicked(ActionEvent event) 
    {
        try
        {
            PlayList selected = playlistTableView.getSelectionModel().getSelectedItem();
            model.deletePlaylist(selected);
        } 
        catch (ModelException ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }
    
    //Move the selected song up on the playlist
    @FXML
    private void upArrowClicked(MouseEvent event) 
    {
        try
        {
            UserMedia selcted = playlistSongsListView.getSelectionModel().getSelectedItem();
            PlayList current = playlistTableView.getSelectionModel().getSelectedItem();
            model.moveSelectionUp(selcted, current);
            
            playlistSongsListView.getSelectionModel().select(selcted);
        } 
        catch (ModelException ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }
    
    //Move the selected song down on the playlist
    @FXML
    private void downArrowClicked(MouseEvent event) 
    {
        try
        {
            UserMedia selcted = playlistSongsListView.getSelectionModel().getSelectedItem();
            PlayList current = playlistTableView.getSelectionModel().getSelectedItem();
            model.moveMediaDown(selcted, current);
            
            playlistSongsListView.getSelectionModel().select(selcted);
        }
        catch (ModelException ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }

    //Create new window to add a new song
    @FXML
    private void addNewSongClicked(ActionEvent event)
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/View/NewSong.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } 
        catch (IOException ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }
    
    //Create new window to edit a song
    @FXML
    private void editSongClicked(ActionEvent event)
    {
        try
        {
            UserMedia selectedSong = songsTableView.getSelectionModel().getSelectedItem();
            model.editMedia(selectedSong);
            
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/View/NewSong.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            
            NewSongController controller = fxmlLoader.getController();
            controller.fillData();
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } 
        catch (Exception ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }

    //Try to delete the selected song from the table view
    @FXML
    private void deleteSongClicked(ActionEvent event) 
    {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to delete the song?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> selection = a.showAndWait();
        
        if (selection.get() == ButtonType.NO)
        {
            return;
        }
        
        UserMedia selectedSong = songsTableView.getSelectionModel().getSelectedItem();
        try
        {
            model.deleteMedia(selectedSong);
        } 
        catch (ModelException ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }
  
    /**
     * Closes the window
     * @param event 
     */
    @FXML
    private void closeAppClicked(ActionEvent event)
    {
        Stage stage = (Stage) volumeController.getScene().getWindow();
        stage.close();
    }

    //Loads all media on startup.
    //The filter is currently a placeholder
    private void loadMedia()
    {
        try
        {
            model.loadMedia();
        } 
        catch (ModelException ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }

    //Shows an alert window with the description of the error
    private void showAlert(Exception ex)
    {
        Alert a = new Alert(Alert.AlertType.ERROR, "An error occured: " + ex.getMessage(), ButtonType.OK);
        a.show();
    }
    
}
