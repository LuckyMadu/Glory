package glory_game_controller;


import glory_schema.VariableElement;
import glory_schema.Mouse;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;


public class InstructionMenuController implements Initializable {

    private Mouse mouse = new Mouse();
    @FXML
    private Button backButton;
    @FXML
    private Pane instructionPane;
    @FXML
    private Label userNameLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        instructionPane.setOpacity(0);
        makeFadeInTransition();
        
        userNameLabel.setText(VariableElement.username);
        instructionPane.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                mouse.setX(event.getX());
                mouse.setY(event.getY());
            }
        
        });
        instructionPane.setOnMouseDragged(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                instructionPane.getScene().getWindow().setX(event.getScreenX() - mouse.getX() - 14);
                instructionPane.getScene().getWindow().setY(event.getScreenY() - mouse.getY() - 14);
            }
        
        });
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
    
       private void makeFadeInTransition() {
        FadeTransition fadeTransition = new FadeTransition();
            fadeTransition.setDuration(Duration.millis(3000));
            fadeTransition.setNode(instructionPane);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            fadeTransition.play();
    }
}
