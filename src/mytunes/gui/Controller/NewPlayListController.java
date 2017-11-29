/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import mytunes.be.PlayList;
import mytunes.gui.Model.MediaPlayerModel;
import mytunes.gui.Model.ModelException;

/**
 * FXML Controller class
 *
 * @author sebok
 */
public class NewPlayListController implements Initializable
{

    @FXML
    private TextField txtFieldName;

    private MediaPlayerModel model;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        //Try saving whent the enter key is pressed
        txtFieldName.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event)
            {
                if (event.getCode().equals(KeyCode.ENTER))
                {
                    saveData();
                }
            }
        });
    }    

    @FXML
    private void btnSaveClick(ActionEvent event)
    {
        saveData();
    }
    
    

    @FXML
    private void btnCancelClick(ActionEvent event)
    {
        closeWindow();
    }
    
    //Name can be saved by either clicking the "Save" buttom, or pressong Enter
    private void saveData()
    {
        try
        {
            String playListName = txtFieldName.getText();
            model.createNewPlayList(playListName);
        } 
        catch (ModelException ex)
        {
            Logger.getLogger(NewPlayListController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
        
        closeWindow();
    }
    
    public void setModel(MediaPlayerModel model)
    {
        this.model = model;
    }
    
    private void closeWindow()
    {
        Stage stage = (Stage) txtFieldName.getScene().getWindow();
        stage.close();
    }

    public void setText(PlayList list) throws Exception
    {
        try 
        {
            txtFieldName.setText(list.getTitle());
        }
        catch (NullPointerException ex)
        {
            throw new Exception("No list selected!");
        }
    }
    
    private void showAlert(Exception ex)
    {
        Alert a = new Alert(Alert.AlertType.ERROR, "An error occured: " + ex.getMessage(), ButtonType.OK);
        a.show();
    }
}
