/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
public class MainController implements Initializable {
    
    
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
    private Button newPlaylistButton;
    @FXML
    private Button editPlaylistButton;
    @FXML
    private Button deletePlaylistButton;
    @FXML
    private Button deleteSongFromPlaylistButton;
    @FXML
    private Button newSongButton;
    @FXML
    private Button editSongButton;
    @FXML
    private Button closeApp;
    @FXML
    private Button deleteSongButton;

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
    
    private MediaPlayerModel model = new MediaPlayerModel();

    public void initialize(URL url, ResourceBundle rb) {
        next.setImage(img_next);
        previous.setImage(img_previous);
        play.setImage(img_play);
        search.setImage(img_search);
        upArrow.setImage(img_up);
        downArrow.setImage(img_down);
        addArrow.setImage(img_addArrow);
        
        setUpPlayListCellFactories();
        setUpSongsCellFactories();
        
        loadMedia();
        
        playlistTableView.setItems(model.getPlayLists());
        songsTableView.setItems(model.getSongs());
        
        
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
    }    

    //Update the listView to show the songs found in the selected play list
    private void updateListView()
    {
        PlayList selectedPlayList = playlistTableView.getSelectionModel().getSelectedItem();
        playlistSongsListView.setItems(selectedPlayList.getSongs());
    }
    
    private void setUpPlayListCellFactories()
    {
        playListColumnName.setCellValueFactory(new PropertyValueFactory("title"));
        playListColumnSongsCount.setCellValueFactory(new PropertyValueFactory("count"));
        playListColumnTotalTime.setCellValueFactory(new PropertyValueFactory("totalTime"));
    }

    private void setUpSongsCellFactories()
    {
        songsColumnTitle.setCellValueFactory(new PropertyValueFactory("title"));
        songsColumArtist.setCellValueFactory(new PropertyValueFactory("artist"));
        songsColumnCategory.setCellValueFactory(new PropertyValueFactory("category"));
        songsColumnTime.setCellValueFactory(new PropertyValueFactory("time"));
    }
    
    
    @FXML
    private void nextArrowClicked(MouseEvent event) {
        model.nextMedia();
    }

    @FXML
    private void previousArrowClicked(MouseEvent event) {
        model.previousMedia();
    }

    @FXML
    private void playArrowClicked(MouseEvent event) {
        try
        {
            PlayList playList = playlistTableView.getSelectionModel().getSelectedItem();
            model.playMedia(playList);
        } 
        catch (Exception ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            Alert a = new Alert(Alert.AlertType.ERROR, "An error occured: " + ex.getMessage(), ButtonType.OK);
            a.show();
        }
    }

    @FXML
    private void searchClicked(MouseEvent event) {
        String searchString = searchField.getText();
        model.searchSong(searchString);
    }

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

    @FXML
    private void addNewPlaylistClicked(ActionEvent event)
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/View/NewPlayList.fxml"));
            
            Parent root1 = (Parent) fxmlLoader.load();
            
            NewPlayListController controller = fxmlLoader.getController();
            controller.setModel(model);
            
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

    @FXML
    private void editPlaylistClicked(ActionEvent event) 
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/View/NewPlayList.fxml"));
            
            Parent root1 = (Parent) fxmlLoader.load();
            
            NewPlayListController controller = fxmlLoader.getController();
            controller.setModel(model);
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

    @FXML
    private void deleteSongFromPlaylistClicked(ActionEvent event) {
        model.deleteSongFromPlaylist();
    }

    @FXML
    private void upArrowClicked(MouseEvent event) {
        model.moveSongUp();
    }

    @FXML
    private void downArrowClicked(MouseEvent event) {
        model.moveSongDown();
    }

    //Create new window to add a new song
    @FXML
    private void addNewSongClicked(ActionEvent event)
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/View/NewSong.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            
            NewSongController controller = fxmlLoader.getController();
            controller.setModel(model);
            
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
            model.editSong(selectedSong);
            
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/View/NewSong.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            
            NewSongController controller = fxmlLoader.getController();
            controller.setModel(model);
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

    @FXML
    private void deleteSongClicked(ActionEvent event) {
        UserMedia selectedSong = songsTableView.getSelectionModel().getSelectedItem();
        try
        {
            model.deleteSong(selectedSong);
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
            String filter = "";
            model.loadMedia(filter);
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
