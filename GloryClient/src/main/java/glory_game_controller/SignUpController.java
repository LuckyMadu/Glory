package glory_game_controller;



import glory_schema.VariableElement;
import glory_schema.Mouse;
import static glory_schema.ResponseResult.SUCCESS;
import glory_schema.Validator;
import static glory_game_handler.PlayerHandler.RegisterPlayer;
import static glory_game_handler.PlayerHandler.removePlayer;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.swing.JOptionPane;

public class SignUpController implements Initializable {
    private Mouse mouse = new Mouse();
    private Validator validate = new Validator();
    @FXML
    private Button exitButton;
    @FXML 
    private Button backButton;
    @FXML
    private Button signUpButton;
    @FXML
    private Pane signUpPane;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField emailField;
    @FXML
    private Alert alert;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        signUpPane.setOpacity(0);
        makeFadeInTransition();
        
        
        signUpPane.setOnMousePressed((MouseEvent event) -> {
            mouse.setX(event.getX());
            mouse.setY(event.getY());
        });
        signUpPane.setOnMouseDragged((MouseEvent event) -> {
            signUpPane.getScene().getWindow().setX(event.getScreenX() - mouse.getX() - 14);
            signUpPane.getScene().getWindow().setY(event.getScreenY() - mouse.getY() - 14);
        });
    }    
    
    @FXML
    private void backAction(ActionEvent event) throws IOException{
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
    private void exitAction(ActionEvent event){
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
            removePlayer(VariableElement.username);
            System.exit(0);
    }
    
    @FXML
    private void registerAction(ActionEvent event){
        if(validate.isInputEmpty(usernameField) && validate.isInputEmpty(emailField) && validate.isInputEmpty(passwordField)){
            String name = usernameField.getText();
            String password = passwordField.getText();
            String email = emailField.getText();
            String result = RegisterPlayer(name,password,email);   
            if(result.equals(SUCCESS)){
                JOptionPane.showMessageDialog(null, "Sucess");
            }
            else{
                JOptionPane.showMessageDialog(null, "Error");
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "fields cannot be empty");
        }
    }   
    
    private void makeFadeInTransition() {
        FadeTransition fadeTransition = new FadeTransition();
            fadeTransition.setDuration(Duration.millis(4000));
            fadeTransition.setNode(signUpPane);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            fadeTransition.play();
    }
}
