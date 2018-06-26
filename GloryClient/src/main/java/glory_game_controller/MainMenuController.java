package glory_game_controller;

import glory_schema.VariableElement;
import static glory_schema.VariableElement.currentRound;
import glory_schema.FunctionElement;
import static glory_schema.FunctionElement.setPlayerJoinModelData;
import glory_schema.Mouse;
import glory_schema.OnlinePlayerStatus;
import glory_schema.OnlinePlayer;
import glory_game_handler.GameHandler;
import static glory_game_handler.PlayerHandler.getAllPlayers;
import static glory_game_handler.PlayerHandler.listenToJoinEvent;
import static glory_game_handler.PlayerHandler.notifyPlayerJoin;
import static glory_game_handler.PlayerHandler.removePlayer;
import static glory_game_handler.PlayerHandler.setModelReference;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.swing.JOptionPane;


public class MainMenuController implements Initializable {

    ObservableList<String> model = FXCollections.observableArrayList();
    private Mouse mouse= new Mouse();
    private String[] playerNames;
    private OnlinePlayer[] allPlayers;

    @FXML
    private ListView<String> listBox;
    @FXML
    private Label userNameLabel;
    @FXML
    private Button startButton;
    @FXML
    private Button backButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button instructionButton;
    @FXML
    private AnchorPane extendableNotificationPane;
    @FXML
    private Rectangle clipRect;
    @FXML
    private Pane mainPane;
    @FXML
    private Alert alert;
    @FXML
    private ImageView loader;
    @FXML
    private Button start,stop;
    
    
    final URL resource = getClass().getResource("/styles/Mind-Bender.mp3");
    final Media media = new Media(resource.toString());
    final MediaPlayer mediaPlayer = new MediaPlayer(media);
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
//        final URL resource = getClass().getResource("/styles/Mind-Bender.mp3");
//        final Media media = new Media(resource.toString());
//        final MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(1);
        //mediaPlayer.play();
        mainPane.setOpacity(0);
        makeFadeInTransition();
        
        userNameLabel.setText("LOG IN AS "+(VariableElement.username).toUpperCase());
        allPlayers = getAllPlayers();
        setPlayerJoinModelData(allPlayers, model);
        listBox.setItems(model);
        setModelReference(model);
        Thread t = new Thread(() -> {
            notifyPlayerJoin();
            if (currentRound == 0) {
                listenToJoinEvent();
            }
        });
        t.setDaemon(true);
        t.start();

        double widthInitial = 200;
        double heightInitial = 400;
        clipRect = new Rectangle();
        clipRect.setWidth(widthInitial);
        clipRect.setHeight(0);
        clipRect.translateYProperty().set(heightInitial);
        extendableNotificationPane.setClip(clipRect);
        extendableNotificationPane.translateYProperty().set(-heightInitial);
        extendableNotificationPane.prefHeightProperty().set(0);
        mainPane.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                mouse.setX(event.getX());
                mouse.setY(event.getY());
            }
        
        });
        mainPane.setOnMouseDragged(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                mainPane.getScene().getWindow().setX(event.getScreenX() - mouse.getX() - 14);
                mainPane.getScene().getWindow().setY(event.getScreenY() - mouse.getY() - 14);
            }
        
        });
    }

    public void startGame(ActionEvent event) throws IOException {
        
        loader.setVisible(true);
        String playableStatus = GameHandler.startGame();
        if (playableStatus.equals("playable")) {
            VariableElement.isWaiting = false;
            //Remove Label Named Waiting
            openGame();
        } else {
            VariableElement.isWaiting = true;
            FunctionElement.mainMenu = this;
            //Disable Start Button
            //Add new Label Named Waiting
            JOptionPane.showMessageDialog(null, playableStatus);
        }
    }

    @FXML
    private void notificationClick() {
        loader.setVisible(true);
        clipRect.setWidth(extendableNotificationPane.getWidth());
        if (clipRect.heightProperty().get() != 0) {
            Timeline timelineUp = new Timeline();
            final KeyValue kvUp1 = new KeyValue(clipRect.heightProperty(), 0);
            final KeyValue kvUp2 = new KeyValue(clipRect.translateYProperty(), extendableNotificationPane.getHeight());
            final KeyValue kvUp4 = new KeyValue(extendableNotificationPane.prefHeightProperty(), 0);
            final KeyValue kvUp3 = new KeyValue(extendableNotificationPane.translateYProperty(), -extendableNotificationPane.getHeight());

            final KeyFrame kfUp = new KeyFrame(Duration.millis(200), kvUp1, kvUp2, kvUp3, kvUp4);
            timelineUp.getKeyFrames().add(kfUp);
            timelineUp.play();
        } else {
            Timeline timelineDown = new Timeline();
            final KeyValue kvDwn1 = new KeyValue(clipRect.heightProperty(), extendableNotificationPane.getHeight());
            final KeyValue kvDwn2 = new KeyValue(clipRect.translateYProperty(), 0);
            final KeyValue kvDwn4 = new KeyValue(extendableNotificationPane.prefHeightProperty(), extendableNotificationPane.getHeight());
            final KeyValue kvDwn3 = new KeyValue(extendableNotificationPane.translateYProperty(), 0);
            final KeyFrame kfDwn = new KeyFrame(Duration.millis(200), createBouncingEffect(extendableNotificationPane.getHeight()), kvDwn1, kvDwn2, kvDwn3, kvDwn4);
            timelineDown.getKeyFrames().add(kfDwn);
            timelineDown.play();
        }
    }

    private EventHandler<ActionEvent> createBouncingEffect(double height) {
        final Timeline timelineBounce = new Timeline();
        timelineBounce.setCycleCount(2);
        timelineBounce.setAutoReverse(true);
        final KeyValue kv1 = new KeyValue(clipRect.heightProperty(), (height - 15));
        final KeyValue kv2 = new KeyValue(clipRect.translateYProperty(), 15);
        final KeyValue kv3 = new KeyValue(extendableNotificationPane.translateYProperty(), -15);
        final KeyFrame kf1 = new KeyFrame(Duration.millis(100), kv1, kv2, kv3);
        timelineBounce.getKeyFrames().add(kf1);

        EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                timelineBounce.play();
            }
        };
        return handler;
    }

    @FXML
    private void backAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/StartUp.fxml"));
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
    private void instructionMenuAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/InstructionMenu.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();

        stage = (Stage) instructionButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void exitAction(ActionEvent event) {
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
            removePlayer(VariableElement.username);
            System.exit(0);
    }

    public void openGame() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/GameWindow.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();

        VariableElement.playerStatus = OnlinePlayerStatus.PLAYING;
        stage = (Stage) startButton.getScene().getWindow();
        stage.close();
    }
    
    private void makeFadeInTransition() {
        FadeTransition fadeTransition = new FadeTransition();
            fadeTransition.setDuration(Duration.millis(3000));
            fadeTransition.setNode(mainPane);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            fadeTransition.play();
    }
    
    
    public void startAction(){
        mediaPlayer.play();
    }
    
   
    public void stopAction(){
        mediaPlayer.stop();
    }
}
