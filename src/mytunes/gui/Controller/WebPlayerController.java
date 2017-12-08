/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dominik
 */
public class WebPlayerController implements Initializable {

    @FXML
    private WebView webView;
    @FXML
    private TextField fieldURL;
    @FXML
    private Button loadURL;

    private String title;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setListener();
    }

    private void urlLoad() {
        String url = fieldURL.getText();
        String[] spliturl = url.split(" ");

        if (url.startsWith("h")) {
            webView.getEngine().load(url);
        } else {
            for (int i = 0; i < spliturl.length; i++) {
                if (spliturl[i].contains("height=")) {
                    spliturl[i] = "height=\"97%\"";
                }
                if (spliturl[i].contains("width=")) {
                    spliturl[i] = "width=\"100%\"";
                }
            }
            String correctedURL = "";
            for (String string : spliturl) {
                correctedURL += (string + " ");
            }
            webView.getEngine().getTitle();
            webView.getEngine().loadContent(correctedURL);
        }
    }

    /**
     * press enter to load the content
     */
    private void setListener() {
        fieldURL.setOnKeyPressed(new EventHandler<KeyEvent>() {

            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER ) {
                    urlLoad();
                }
            }
        });
    }

    @FXML
    private void clickLoadURL(KeyEvent event) {
        urlLoad();
    }

    @FXML
    private void clickClose(ActionEvent event) {
        webView.getEngine().load(null);
        Stage stage = (Stage) webView.getScene().getWindow();
        stage.close();
    }

}
