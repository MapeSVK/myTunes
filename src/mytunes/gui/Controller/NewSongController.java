/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import mytunes.be.UserMedia;
import mytunes.gui.Model.MediaPlayerModel;

/**
 * FXML Controller class
 *
 * @author Mape
 */
public class NewSongController implements Initializable {

    @FXML
    private TextField titleOfSongField;
    @FXML
    private TextField songArtistField;
    @FXML
    private ComboBox<?> chooseCategoryComboBox;
    @FXML
    private TextField songTimeField;
    @FXML
    private TextField songPathField;
    @FXML
    private Button chooseFileButton;
    @FXML
    private Button newCategoryButton;
    @FXML
    private Button cancelNewSongButton;
    @FXML
    private Button saveSongButton;
    
    MediaPlayerModel model;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    
    
    
    
    @FXML
    private void chooseFileClicked(ActionEvent event) {
    }

    @FXML
    private void addNewCategoryClicked(ActionEvent event) {
    }

    @FXML
    private void cancelNewSongClicked(ActionEvent event) {
    }

    @FXML
    private void saveSongClicked(ActionEvent event) {
    }
    
    private void fillData()
    {
        UserMedia selectedSong = model.getSelectedSong();
        
        songArtistField.setText(selectedSong.getArtist());
        songTimeField.setText(selectedSong.getTime().toString());
        titleOfSongField.setText(selectedSong.getTitle());
    }
    
    public void setModel(MediaPlayerModel model)
    {
        //Set the model to the same as the other controllerd
        this.model = model;
        //Use the model to fill the relevant textboxes (TODO: Maybe figure out something better?)
        fillData();
    }
    
    
}
