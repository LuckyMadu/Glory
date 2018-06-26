package glory_game_controller;

import glory_schema.VariableElement;
import static glory_schema.FunctionElement.setTableColumns;
import glory_schema.Mouse;
import glory_schema.OnlinePlayerStatistic;
import static glory_game_handler.PlayerHandler.getRoundCompletedPlayers;
import static glory_game_handler.PlayerHandler.listenToRoundCompletionEvent;
import static glory_game_handler.PlayerHandler.notifyRoundCompletion;
import static glory_game_handler.PlayerHandler.removePlayer;
import static glory_game_handler.PlayerHandler.setLabelReference;
import static glory_game_handler.PlayerHandler.setPlayerScore;
import static glory_game_handler.PlayerHandler.setSpecialPointsLabel;
import static glory_game_handler.PlayerHandler.terminatePlayer;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.swing.JOptionPane;


public class RoundCompleteController implements Initializable {

    private String[] allCompletedPlayer;
    final ObservableList<String> model = FXCollections.observableArrayList();
    final ObservableList<OnlinePlayerStatistic> playerStatistics = FXCollections.observableArrayList();
    private Mouse mouse = new Mouse();
    @FXML
    private Label nextRoundTime;
    @FXML
    private Label specialPoints;
    @FXML
    private Label roundNumber;
    @FXML
    private Button donatePerk;
    @FXML
    private TableView<OnlinePlayerStatistic> scoreTable;
    @FXML
    private Pane completePane;
    @FXML
    private Label userNameLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        completePane.setOpacity(0);
        makeFadeInTransition();
        
        roundNumber.setText(String.valueOf(VariableElement.currentRound));
        userNameLabel.setText(VariableElement.username);
        allCompletedPlayer = getRoundCompletedPlayers();
        setSpecialPointsLabel(specialPoints);
        setLabelReference(nextRoundTime);
        setPlayerScore(playerStatistics);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setTableColumns(scoreTable, playerStatistics, allCompletedPlayer);
                listenToRoundCompletionEvent();
            }
        };
        Thread t = new Thread(runnable);
        t.setDaemon(true);
        t.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ScoringMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        notifyRoundCompletion();
        completePane.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                mouse.setX(event.getX());
                mouse.setY(event.getY());
            }
        
        });
        completePane.setOnMouseDragged(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                completePane.getScene().getWindow().setX(event.getScreenX() - mouse.getX() - 14);
                completePane.getScene().getWindow().setY(event.getScreenY() - mouse.getY() - 14);
            }
        
        });
    }

    @FXML
    private void donateAction(ActionEvent event) {
        if(VariableElement.specialPoints < 1){
            JOptionPane.showMessageDialog(null,"Cannot donate Points");
        }
        else{
            JOptionPane.showMessageDialog(null,"Donate Activated");
            //donatePlayer();
        }
    }
    
    private void makeFadeInTransition() {
        FadeTransition fadeTransition = new FadeTransition();
            fadeTransition.setDuration(Duration.millis(3000));
            fadeTransition.setNode(completePane);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            fadeTransition.play();
    }
    
    
}
