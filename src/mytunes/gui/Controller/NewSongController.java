/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    MediaPlayerModel model;
    UserMedia selectedSong;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
    }    
    
    
    
    @FXML
    private void chooseFileClicked(ActionEvent event) {
    }

    @FXML
    private void addNewCategoryClicked(ActionEvent event) 
    {    
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/View/CategoryAdd.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            
            CategoryAddController controller = fxmlLoader.getController();
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
    private void cancelNewSongClicked(ActionEvent event) 
    {
        closeWindow();
    }

    @FXML
    private void saveSongClicked(ActionEvent event) 
    {        
        try
        {
            String title = titleOfSongField.getText();
            String artist = songArtistField.getText();
            String category = chooseCategoryComboBox.getValue();
            Time time = Time.valueOf(songTimeField.getText());
            
            if (selectedSong != null)   //We are updating an already existing song
            {
                selectedSong.setArtist(artist);
                selectedSong.setTitle(title);
                selectedSong.setTime(time);
                selectedSong.setCategory(category);
                
                model.updateSong(selectedSong);
            }
            else //We are creating a new song
            {
                UserMedia newSong = new UserMedia();
                newSong.setArtist(artist);
                newSong.setTitle(title);
                newSong.setTime(time);
                newSong.setCategory(category);
                newSong.setPath("Test path");
                
                model.addNewSong(newSong);
            }
            closeWindow();
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
        chooseCategoryComboBox.setValue(selectedSong.getCategory());
    }
    
    /**
     * Add the shared model, and bind the comboBox to the ObservableList inside the model
     * @param model 
     */
    public void setModel(MediaPlayerModel model)
    {
        this.model = model;
        chooseCategoryComboBox.setItems(model.getCategories());
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
}
