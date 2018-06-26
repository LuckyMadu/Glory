package glory_game_controller;

import glory_schema.VariableElement;
import static glory_schema.VariableElement.username;
import glory_schema.Mouse;
import glory_schema.Validator;
import static glory_game_handler.PlayerHandler.checkTermination;
import static glory_game_handler.PlayerHandler.removePlayer;
import static glory_game_handler.WordHandler.changeLetter;
import static glory_game_handler.WordHandler.getInitialLetters;
import static glory_game_handler.WordHandler.getLetters;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.swing.JOptionPane;



public class GameWindowController implements Initializable {

    private String initialLetters = null;
    private static int counter = 10;
    private Timer timer = new Timer();
    private final TextField[] letterFields = new TextField[10];
    private Mouse mouse = new Mouse();
    private Validator validate = new Validator();
    @FXML
    private TextField initialLetterOne, initialLetterTwo,initialLetterThree;
    @FXML
    private TextField noOfVowels, noOfConsonants;
    @FXML
    private AnchorPane letterPane;
    @FXML
    private Button requestButton;
    @FXML
    private Button backButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button letterChangeButton;
    @FXML
    private Label timerLabel;
    @FXML
    private String selectedLetter;
    @FXML
    private Node selectedNode;
    @FXML
    private TextField changeLetter;
    @FXML
    private Pane gamePane;
    @FXML
    private Label userNameLabel;
    @FXML
    private Alert alert;
    @FXML
    private ImageView letterBag;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        gamePane.setOpacity(0);
        makeFadeInTransition();
        
        String termination = checkTermination();
        if (termination.equals(username + " Terminated")) {
                Stage stage = new Stage();
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
                } catch (IOException ex) {
                    Logger.getLogger(GameWindowController.class.getName()).log(Level.SEVERE, null, ex);
                }
                Scene scene = new Scene(root);
                scene.getStylesheets().add("/styles/Styles.css");
                stage.setResizable(false);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(scene);
                stage.show();
                stage = (Stage) exitButton.getScene().getWindow();
                stage.close();
        } else {
            userNameLabel.setText(VariableElement.username);
            initialLetters = getInitialLetters();
            initialLetterOne.setText(String.valueOf(initialLetters.charAt(0)).toUpperCase());
            initialLetterTwo.setText(String.valueOf(initialLetters.charAt(1)).toUpperCase());
            initialLetterThree.setText(String.valueOf(initialLetters.charAt(2)).toUpperCase());
            gamePane.setOnMousePressed((MouseEvent event) -> {
                mouse.setX(event.getX());
                mouse.setY(event.getY());
            });
            gamePane.setOnMouseDragged((MouseEvent event) -> {
                gamePane.getScene().getWindow().setX(event.getScreenX() - mouse.getX() - 14);
                gamePane.getScene().getWindow().setY(event.getScreenY() - mouse.getY() - 14);
            });
        }
    }

    public void getRequiredLetters() {
        RotateTransition rt = new RotateTransition(Duration.millis(3000), letterBag);
        rt.setByAngle(360);
        rt.setCycleCount(3);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.play();
        exitButton.setDisable(true);
        if(validate.isNumeric(noOfVowels.getText())&& validate.isNumeric(noOfConsonants.getText())){
            if(validate.isInputEmpty(noOfVowels) && validate.isInputEmpty(noOfConsonants)){
                int vowelsRequired = Integer.parseInt(noOfVowels.getText());
                int consonantsRequired = Integer.parseInt(noOfConsonants.getText());
                if (consonantsRequired + vowelsRequired > 8) {
                    JOptionPane.showMessageDialog(null, "You can not select more than 8");
                } 
                else if(consonantsRequired==0 || vowelsRequired==0 ){
                    JOptionPane.showMessageDialog(null,"You Have to Select From Both Letter Categories");
                }
                else {
                    String letters = getLetters(vowelsRequired, consonantsRequired);
                    int count = 0;
                    for (Node node : letterPane.getChildren()) {
                        if (count == letters.length()) {
                            break;
                        }
                        if (node instanceof TextField) {
                            ((TextField) node).setText(String.valueOf(letters.charAt(count)).toUpperCase());
                        }
                        count++;
                    }
                    requestButton.setDisable(true);
                    VariableElement.initialLetters = initialLetters;
                    VariableElement.letters = initialLetters + letters;
                    timer = new Timer();
                    counter = 10;
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(() -> {
                                if (counter == 0) {
                                    timer.cancel();
                                    Stage stage = new Stage();
                                    Parent root = null;
                                    try {
                                        root = FXMLLoader.load(getClass().getResource("/fxml/GamePlay.fxml"));
                                    } catch (IOException ex) {
                                        Logger.getLogger(GameWindowController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    Scene scene = new Scene(root);
                                    scene.getStylesheets().add("/styles/Styles.css");
                                    stage.setResizable(false);
                                    stage.initStyle(StageStyle.UNDECORATED);
                                    stage.setScene(scene);
                                    stage.show();

                                    stage = (Stage) requestButton.getScene().getWindow();
                                    stage.close();
                                } else {
                                    timerLabel.setText(String.valueOf(counter));
                                    if (counter == 10) {
                                        final URL resource = getClass().getResource("/styles/sec10.mp3");
                                        final Media media = new Media(resource.toString());
                                        final MediaPlayer mediaPlayer = new MediaPlayer(media);
                                        mediaPlayer.play();
                                    } else if (counter == 9 || counter == 8 || counter == 7 || counter == 6 || counter == 5 || counter == 4) {
                                        timerLabel.setStyle("-fx-text-fill:linear-gradient(#66ff66, #00cc00);");
                                    } else {
                                        timerLabel.setStyle("-fx-text-fill:  linear-gradient(#ff5400,#be1d00);");
                                    }
                                    counter--;
                                }
                            });
                        }
                    }, 0, 1000);
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "you have to select from both categories");
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Select a numeric value");
        }
    }

    public void getSelectedText() {
        for (Node node : letterPane.getChildren()) {
            if (node instanceof TextField) {
                node.setStyle("-fx-background-color:linear-gradient(to left, #1D976C , #93F9B9);");
                if (node.isFocused()) {
                    selectedNode = node;
                    node.setStyle("-fx-background-color:linear-gradient(to left, #1A2980 , #26D0CE);");
                    selectedLetter = ((TextField) node).getText();
                }
            }
        }
    }

    @FXML
    private void letterChangeAction(ActionEvent event) {
        String newLetter = changeLetter(selectedLetter);
        VariableElement.letters = VariableElement.letters.replaceFirst(selectedLetter, newLetter);
        ((TextField) selectedNode).setText(newLetter.toUpperCase());
        letterPane.setDisable(true);
    }

    @FXML
    private void backAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void exitAction(ActionEvent event) {
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
            removePlayer(VariableElement.username);
            System.exit(0);
    }
    
       private void makeFadeInTransition() {
        FadeTransition fadeTransition = new FadeTransition();
            fadeTransition.setDuration(Duration.millis(3000));
            fadeTransition.setNode(gamePane);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            fadeTransition.play();
    }
}
