/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.Controller;

import java.net.URL;
import java.sql.Time;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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
    UserMedia selectedSong;

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
    private void cancelNewSongClicked(ActionEvent event) 
    {
        Stage stage = (Stage) cancelNewSongButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void saveSongClicked(ActionEvent event) 
    {        
        try
        {
            String title = titleOfSongField.getText();
            String artist = songArtistField.getText();
            Time time = Time.valueOf(songTimeField.getText());
            
            selectedSong.setArtist(artist);
            selectedSong.setTitle(title);
            selectedSong.setTime(time);
        } 
        catch (Exception ex)
        {
            Logger.getLogger(NewSongController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }
    
    public void fillData()
    {
        selectedSong = model.getSelectedSong();
        
        songArtistField.setText(selectedSong.getArtist());
        songTimeField.setText(selectedSong.getTime().toString());
        titleOfSongField.setText(selectedSong.getTitle());
    }
    
    public void setModel(MediaPlayerModel model)
    {
        this.model = model;
    }
    
    private void showAlert(Exception ex)
    {
        Alert a = new Alert(Alert.AlertType.ERROR, "An error occured: " + ex.getMessage(), ButtonType.OK);
        a.show();
    }
}
