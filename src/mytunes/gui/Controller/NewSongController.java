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
    private Button chooseFileButton;
    @FXML
    private Button newCategoryButton;
    @FXML
    private Button cancelNewSongButton;
    @FXML
    private Button saveSongButton;
    
    private MediaPlayerModel model;
    private UserMedia selectedSong;
    private Media media;
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
        try
        {
            FileChooser fc = new FileChooser(); //Open a file chooser dialog
            fc.setTitle("Select a music file");
            String path = fc.showOpenDialog(new Stage()).toURI().toURL().toString(); //Get the selected path
            System.out.println(path);
            getMetaData(path);
        } 
        catch (MalformedURLException ex)
        {
            Logger.getLogger(NewSongController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        saveData();
    }
    
    //Try saving the data entered into the texfields
    private void saveData()
    {
        try
        {
            //Retrieve the data from the inputs
            String title = titleOfSongField.getText();
            String artist = songArtistField.getText();
            String category = chooseCategoryComboBox.getValue();
            String path = songPathField.getText();
            
            if (selectedSong != null)   //We are updating an already existing song
            {
                selectedSong.setArtist(artist);
                selectedSong.setTitle(title);
                selectedSong.setCategory(category);
                selectedSong.setPath(path);
                
                model.updateMedia(selectedSong);
            }
            else //We are creating a new song
            {
                UserMedia newSong = new UserMedia();
                newSong.setArtist(artist);
                newSong.setTitle(title);
                newSong.setCategory(category);
                newSong.setPath(path);
                
                model.addNewMedia(newSong);
            }
            closeWindow();
        } 
        catch (Exception ex)
        {
            Logger.getLogger(NewSongController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }
    
    //If we have a selection, use it to fill out the inputs
    public void fillData()
    {
        selectedSong = model.getSelectedMedia();
        
        songArtistField.setText(selectedSong.getArtist());
        songTimeField.setText(selectedSong.getTime()+ "");
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

    @FXML
    private void onKeyPressed(KeyEvent event)
    {
        if (event.getCode().equals(KeyCode.ENTER))
        {
            saveData();
        }
    }

    private void getMetaData(String path)
    {
        model.getMetaData(path);
    }
}
