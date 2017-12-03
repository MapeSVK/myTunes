/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.Controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Time;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mytunes.be.UserMedia;
import mytunes.gui.Model.MediaPlayerModel;
import mytunes.gui.Model.ModelException;

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
    private ComboBox<String> chooseCategoryComboBox;
    @FXML
    private TextField songTimeField;
    @FXML
    private TextField songPathField;
    @FXML
    private Button cancelNewSongButton;
    
    private MediaPlayerModel model;
    private UserMedia selectedSong;
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        model = MediaPlayerModel.getInstance();
        chooseCategoryComboBox.setItems(model.getCategories());
    }    
    
    //Opens a new window that lets us select a file
    @FXML
    private void chooseFileClicked(ActionEvent event) 
    {
        FileChooser fc = new FileChooser(); //Open a file chooser dialog
        fc.setTitle("Select a music file");
        File file = fc.showOpenDialog(new Stage());
        URI path = file.toURI();
        UserMedia uMedia = getMetaData(path);
        uMedia.setMedia(new Media(path.toString()));
        uMedia.setPath(path.getPath());
        setFields(uMedia);
        selectedSong = uMedia;
    }

    //Open a new window, which lets us add a new, custom category
    @FXML
    private void addNewCategoryClicked(ActionEvent event) 
    {    
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/View/CategoryAdd.fxml"));
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

    @FXML
    private void cancelNewSongClicked(ActionEvent event) 
    {
        closeWindow();
    }

    @FXML
    private void saveSongClicked(ActionEvent event) 
    {     
        try
        {
            readDataFromTextFields();
            model.addNewMedia(selectedSong);
        } 
        catch (ModelException ex)
        {
            showAlert(ex);
        }
    }
    
    //Save the media, when the enter key is pressed
    @FXML
    private void onKeyPressed(KeyEvent event)
    {
        if (event.getCode().equals(KeyCode.ENTER))
        {
            try
            {
                readDataFromTextFields();
                model.addNewMedia(selectedSong);
            } 
            catch (ModelException ex)
            {
                showAlert(ex);
            }
        }
    }
    
    //If we have a selection, use it to fill out the inputs
    public void fillData()
    {
        selectedSong = model.getSelectedMedia();
        
        songArtistField.setText(selectedSong.getArtist());
        songTimeField.setText(selectedSong.getTime() + "");
        titleOfSongField.setText(selectedSong.getTitle());
        chooseCategoryComboBox.setValue(selectedSong.getCategory());
        songPathField.setText(selectedSong.getPath());
    }

    private void showAlert(Exception ex)
    {
        Alert a = new Alert(Alert.AlertType.ERROR, "An error occured: " + ex.getMessage(), ButtonType.OK);
        a.show();
    }
    
    private void closeWindow()
    {
        Stage stage = (Stage) cancelNewSongButton.getScene().getWindow();
        stage.close();
    }

    private UserMedia getMetaData(URI path)
    {
        try
        {
            return model.getMetaData(path);
        } 
        catch (ModelException ex)
        {
            showAlert(ex);
        }
        return null;
    }

    private void setFields(UserMedia uMedia)
    {
        if (uMedia == null)
        {
            showAlert(new Exception("Empty song!"));
        }
        songArtistField.setText(uMedia.getArtist());
        songTimeField.setText(uMedia.getTimeString());
        titleOfSongField.setText(uMedia.getTitle());
        chooseCategoryComboBox.setValue(uMedia.getCategory());
        songPathField.setText(uMedia.getPath());    
        songTimeField.setText(Integer.toString(uMedia.getTime()));
    }
    
    private void readDataFromTextFields()
    {
        if (selectedSong.getArtist().isEmpty())
        {
            selectedSong.setArtist(songArtistField.getText());
        }
        if (selectedSong.getTitle().isEmpty())
        {
            selectedSong.setArtist(songTimeField.getText());
        }
        if (selectedSong.getCategory().isEmpty())
        {
            selectedSong.setArtist(chooseCategoryComboBox.getValue());
        }
    }
}
