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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import mytunes.be.PlayList;
import mytunes.be.UserMedia;
import mytunes.dal.MetaReader;
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
        
        setListenersAndEventHandlers();
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
  
    //Update the listView to show the songs found in the selected play list, and update the BLLManager to contain the latest selection
    private void updateListView()
    {
        PlayList selectedPlayList = playlistTableView.getSelectionModel().getSelectedItem();

        playlistSongsListView.setItems(selectedPlayList.getMediaList());
        try
        {
            model.setSelectedPlayList(selectedPlayList);
        } 
        catch (ModelException ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }
 
    //Call a method to load data from the DB
    private void loadMedia()
    {
        try
        {
            model.loadDataFromDB();
        } 
        catch (ModelException ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }
    
    //Set up listeners
    private void setListenersAndEventHandlers()
    {
        //Update the list view containing the songs in the selected play list, whenever a new play list is selected
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

    @FXML
    private void nextArrowClicked()
    {
        
    }
    
    
    @FXML
    private void previousArrowClicked(MouseEvent event)
    {
    }

    @FXML
    private void playArrowClicked(MouseEvent event)
    {
    }

    @FXML
    private void searchClicked(MouseEvent event)
    {
    }

    @FXML
    private void addArrowClicked(MouseEvent event)
    {
    }

    @FXML
    private void addNewPlaylistClicked(ActionEvent event)
    {
    }

    @FXML
    private void editPlaylistClicked(ActionEvent event)
    {
    }

    @FXML
    private void deletePlaylistClicked(ActionEvent event)
    {
    }

    @FXML
    private void deleteSongFromPlaylistClicked(ActionEvent event)
    {
    }

    @FXML
    private void upArrowClicked(MouseEvent event)
    {
    }

    @FXML
    private void downArrowClicked(MouseEvent event)
    {
    }

    @FXML
    private void addNewSongClicked(ActionEvent event)
    {
    }

    @FXML
    private void editSongClicked(ActionEvent event)
    {
    }

    @FXML
    private void closeAppClicked(ActionEvent event)
    {
    }

    @FXML
    private void deleteSongClicked(ActionEvent event)
    {
    }

    
    private void searchForString(String search)
    {
       model.searchString(search);
    }
    
    //Show a new alert window, with the text of the error
    private void showAlert(ModelException ex)
    {
        Alert a = new Alert(Alert.AlertType.ERROR, "An error occured: " + ex.getMessage(), ButtonType.OK);
        a.show();
    }

}