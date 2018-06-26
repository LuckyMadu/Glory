package glory_game_controller;


import java.io.File;
import java.net.URL;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Start extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/StartUp.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("GLORY");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        
        final URL resource = getClass().getResource("/styles/login.mp3");
        final Media media = new Media(resource.toString());
        final MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        
//        String musicFile = "Mind-Bender.mp3";     // For example
//        Media sound = new Media(new File(musicFile).toURI().toString());
//        MediaPlayer mediaPlayer = new MediaPlayer(sound);
//        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
//        mediaPlayer.play();
    
    }

    public static void main(String[] args) {
        launch(args);
    }

}
