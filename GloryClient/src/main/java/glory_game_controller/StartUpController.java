
package glory_game_controller;

import glory_schema.VariableElement;
import glory_schema.Mouse;
import glory_schema.Validator;
import static glory_game_handler.PlayerHandler.LoginPlayer;
import static glory_game_handler.PlayerHandler.removePlayer;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.swing.JOptionPane;


public class StartUpController implements Initializable {

    private Mouse mouse = new Mouse();
    private Validator validate = new Validator();
    @FXML
    private TextField username;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button exitButton;
    @FXML
    private Button startButton;
    @FXML
    private Hyperlink signUpLink;
    @FXML
    private Pane startPane;
    @FXML
    private Alert alert;
    @FXML
    private Slider slider;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        startPane.setOpacity(0);
        makeFadeInTransition();
        
        
//        String musicFile = "login.mp3";     // For example
//        Media sound = new Media(new File(musicFile).toURI().toString());
//        MediaPlayer mediaPlayer = new MediaPlayer(sound);
//        mediaPlayer.play();
//        final URL resource = getClass().getResource("/styles/Mind-Bender.mp3");
//                                        final Media media = new Media(resource.toString());
//                                        final MediaPlayer mediaPlayer = new MediaPlayer(media);
//                                        mediaPlayer.play();
        
        
        startPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouse.setX(event.getX());
                mouse.setY(event.getY());
            }

        });
        startPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                startPane.getScene().getWindow().setX(event.getScreenX() - mouse.getX() - 14);
                startPane.getScene().getWindow().setY(event.getScreenY() - mouse.getY() - 14);
            }

        });
    }

    @FXML
    private void handleExitButtonAction(ActionEvent event) {
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
            removePlayer(VariableElement.username);
            System.exit(0);
    }

    @FXML
    private void handleStart(ActionEvent event) throws IOException {
        if(validate.isInputEmpty(username) && validate.isInputEmpty(passwordField)){
            String playerName = username.getText();
            VariableElement.username = playerName;
            String password = passwordField.getText();
            String result = LoginPlayer(playerName, password);
            if (result.equals("success")) {
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
                Scene scene = new Scene(root);
                scene.getStylesheets().add("/styles/Styles.css");
                stage.setResizable(false);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(scene);
                stage.show();               
                stage = (Stage) startButton.getScene().getWindow();
                stage.close();
            } else if (result.equals("invalid login")) {
                JOptionPane.showMessageDialog(null, "Invalid Login");
            } else {
                JOptionPane.showMessageDialog(null, "Player Doesnt exists");
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Please enter user name and password");
        }
    }

    @FXML
    public void handleSignUpAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/SignUp.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(4000));
        fadeTransition.setNode(startPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
        stage = (Stage) signUpLink.getScene().getWindow();
        stage.close();
    }
    
    private void makeFadeInTransition() {
        FadeTransition fadeTransition = new FadeTransition();
            fadeTransition.setDuration(Duration.millis(4000));
            fadeTransition.setNode(startPane);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            fadeTransition.play();
    }
}
