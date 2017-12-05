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
import mytunes.be.Mode;
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

    private void setUpPlayListCellFactories() {
        //Set up cell factories
        playListColumnName.setCellValueFactory(new PropertyValueFactory("title"));
        playListColumnSongsCount.setCellValueFactory(new PropertyValueFactory("count"));
        playListColumnTotalTime.setCellValueFactory(new PropertyValueFactory("timeFormattedAsString"));

        //Set the width of the columns
        playlistTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        playListColumnName.prefWidthProperty().bind(playlistTableView.widthProperty().divide(2));
        playListColumnSongsCount.prefWidthProperty().bind(playlistTableView.widthProperty().divide(4));
        playListColumnTotalTime.prefWidthProperty().bind(playlistTableView.widthProperty().divide(4));
    }

    private void setUpSongsCellFactories() {
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
    private void updateListView() {
        PlayList selectedPlayList = playlistTableView.getSelectionModel().getSelectedItem();
        if (selectedPlayList == null) {
            return;
        }
        playlistSongsListView.setItems(selectedPlayList.getMediaList());
        try {
            model.setSelectedPlayList(selectedPlayList);
        }
        catch (ModelException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }

    //Call a method to load data from the DB
    private void loadMedia() {
        try {
            model.loadDataFromDB();
        }
        catch (ModelException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }

    //Set up listeners
    private void setListenersAndEventHandlers() {
        //Update the list view containing the songs in the selected play list, whenever a new play list is selected
        playlistTableView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                updateListView();
            }
        }
        );

        //Add a new event handler, so that search can be performed by pressing enter
        searchField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    String searchString = searchField.getText();
                    searchForString(searchString);
                }
            }
        });
    }

//******************************************************************************************************************************************************************//
//GUI controls events
    //Remove a media from the table view and the DB
    @FXML
    private void deleteSongClicked(ActionEvent event) {
        if (showConfirmationDialog("Are you sure you want to delete this song?")) {
            return;
        }

        UserMedia selected = songsTableView.getSelectionModel().getSelectedItem();

        try {
            model.removeMedia(selected);
        }
        catch (ModelException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);

        }
    }

    @FXML
    private void nextArrowClicked() {

    }

    @FXML
    private void previousArrowClicked(MouseEvent event) {
    }

    @FXML
    private void playArrowClicked(MouseEvent event) {
        UserMedia selectedMedia = songsTableView.getSelectionModel().getSelectedItem();
        model.setMedia(selectedMedia.getMedia());
        model.playMedia();
    }

    @FXML
    private void volumeClicked(MouseEvent event) {
        UserMedia selectedMedia = songsTableView.getSelectionModel().getSelectedItem();
        model.setMedia(selectedMedia.getMedia());
        model.setVolume(volumeController.getValue());
    }

    //Search for the string
    @FXML
    private void searchClicked(MouseEvent event) {
        String searchString = searchField.getText();
        searchForString(searchString);
    }

    //Add the selected song to the selected playlist
    @FXML
    private void addArrowClicked(MouseEvent event) {
        PlayList selectedPlayList = playlistTableView.getSelectionModel().getSelectedItem();
        UserMedia selectedMedia = songsTableView.getSelectionModel().getSelectedItem();

        try {
            model.addMediaToPlayList(selectedMedia, selectedPlayList);
        }
        catch (ModelException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }

    //Delete the selected play list
    @FXML
    private void deletePlaylistClicked(ActionEvent event)
    {
        PlayList selected = playlistTableView.getSelectionModel().getSelectedItem();
        
        if (selected != null)
        {
            if (showConfirmationDialog("Are you sure you want to delete this play list?"))
            {
                return;
            }
        }
        
        try
        {
            model.removePlayList(selected);
        }
        catch (ModelException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }

    //Delete the selected song from the selected play list
    @FXML
    private void deleteSongFromPlaylistClicked(ActionEvent event) {
        if (showConfirmationDialog("Are you sure you want to delete this song from the play list?")) {
            return;
        }

        PlayList selectedPlayList = playlistTableView.getSelectionModel().getSelectedItem();
        UserMedia selectedMedia = playlistSongsListView.getSelectionModel().getSelectedItem();

        try {
            model.removeMediaFromPlayList(selectedMedia, selectedPlayList);
        }
        catch (ModelException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }

    //Move the selected media up in the selected list (UI only)
    @FXML
    private void upArrowClicked(MouseEvent event) {
        UserMedia selected = playlistSongsListView.getSelectionModel().getSelectedItem();
        PlayList list = playlistTableView.getSelectionModel().getSelectedItem();

        try {
            model.moveSongUp(selected, list);
            playlistSongsListView.getSelectionModel().select(selected);
        }
        catch (ModelException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }

    //Move the selected media down in the selected list (UI only)
    @FXML
    private void downArrowClicked(MouseEvent event) {
        UserMedia selected = playlistSongsListView.getSelectionModel().getSelectedItem();
        PlayList list = playlistTableView.getSelectionModel().getSelectedItem();

        try {
            model.moveSongDown(selected, list);
            playlistSongsListView.getSelectionModel().select(selected);
        }
        catch (ModelException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }

//******************************************************************************************************************************************************************//
//Editor windows
    //Open a new window where we can add a new song
    @FXML
    private void addNewSongClicked(ActionEvent event) {
        try {
            model.setMediaMode(Mode.NEW);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/View/NewSong.fxml"));

            Parent root1 = (Parent) fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        }
        catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }

    //Open an new window where we can edit an existing song
    @FXML
    private void editSongClicked(ActionEvent event) {
        try {
            model.setMediaMode(Mode.EDIT);
            model.setSelectedMedia(songsTableView.getSelectionModel().getSelectedItem());

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/View/NewSong.fxml"));

            Parent root1 = (Parent) fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        }
        catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
        catch (ModelException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }

    }

    //Open a new window where we can add a new play list
    @FXML
    private void addNewPlaylistClicked(ActionEvent event) {
        try {
            model.setPlayListMode(Mode.NEW);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/View/NewPlayList.fxml"));

            Parent root1 = (Parent) fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        }
        catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }

    //Open a new window where we can edit an existing play list
    @FXML
    private void editPlaylistClicked(ActionEvent event) {
        try {
            model.setPlayListMode(Mode.EDIT);
            model.setSelectedPlayList(playlistTableView.getSelectionModel().getSelectedItem());

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/View/NewPlayList.fxml"));

            Parent root1 = (Parent) fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        }
        catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
        catch (ModelException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }

//******************************************************************************************************************************************************************//
//Helper methods
    //Search the kist in the model for the give string
    private void searchForString(String search) {
        model.searchString(search);
    }

    //Show a new alert window, with the text of the error
    private void showAlert(Exception ex) {
        Alert a = new Alert(Alert.AlertType.ERROR, "An error occured: " + ex.getMessage(), ButtonType.OK);
        a.show();
    }

    //Close the application
    @FXML
    private void closeAppClicked(ActionEvent event) {
        Stage stage = (Stage) play.getScene().getWindow();
        stage.close();
    }

    //Show a window with the specified prompt text
    //Returns with the value the user selected (continue or not)
    private boolean showConfirmationDialog(String prompt) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, prompt, ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait();

        if (confirmation.getResult() == ButtonType.NO) {
            return true;
        }

        return false;
    }
}
