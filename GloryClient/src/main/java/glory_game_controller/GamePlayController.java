package glory_game_controller;


import glory_schema.VariableElement;
import glory_schema.Mouse;
import static glory_schema.ResponseResult.UNCOMMON;
import static glory_schema.ResponseResult.SUCCESS;
import glory_schema.Validator;
import static glory_game_handler.WordHandler.addWord;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.swing.JOptionPane;


public class GamePlayController implements Initializable {
    private Mouse mouse = new Mouse();
    private Timer timer = new Timer();
    private Validator validate = new Validator();
    private static int counter = 100;
    private static double progressCount = 0.00;
    private String letters,initial;
    @FXML
    private AnchorPane letterPane;
    @FXML
    private TextField wordField;
    @FXML
    private Button submitButton;
    @FXML
    private Button autoButton;
    @FXML
    private Pane gamePlayPane;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label currentRound;
     @FXML
    private Label autoChance1,autoChance2;
    @FXML
    private ProgressIndicator progressBar;
    @FXML
    private Alert alert;
    
    static final int SIZE=26;
    private static String s1;
    private Dictionary dict;
    private String s;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        gamePlayPane.setOpacity(0);
        makeFadeInTransition();
        
        userNameLabel.setText("LOG IN AS "+VariableElement.username);
        letters = VariableElement.letters;
        initial = VariableElement.initialLetters;
        VariableElement.currentRound++;
        currentRound.setText("Round " + String.valueOf(VariableElement.currentRound));
        int count = 0;
        for (Node node : letterPane.getChildren()) {
            if (count == letters.length()) {
                break;
            }
            if (node instanceof TextField) {
                if(count == 0 || count == 1 || count == 2){
                    ((TextField) node).setText(String.valueOf(initial.charAt(count)).toUpperCase());
                    node.setDisable(true);
                }
                else{
                    ((TextField) node).setText(String.valueOf(letters.charAt(count)).toUpperCase());
                    //if(letters.charAt(count) == VariableElement.initialLetters.charAt(0) || letters.charAt(count) == VariableElement.initialLetters.charAt(1)){
                    //node.setDisable(true);
                }              
            }
            count++;
        }

        timer = new Timer();
            counter = 100;
            progressCount = 0.00;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        if (counter == 0) {                         
                            sendWord();                            
                        } else {         
                            progressBar.setProgress(progressCount);
                            progressCount=progressCount+0.01;
                            counter--;
                        }
                    });
                }
            }, 0, 1000);
        
        gamePlayPane.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                mouse.setX(event.getX());
                mouse.setY(event.getY());
            }
        
        });
        gamePlayPane.setOnMouseDragged(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                gamePlayPane.getScene().getWindow().setX(event.getScreenX() - mouse.getX() - 14);
                gamePlayPane.getScene().getWindow().setY(event.getScreenY() - mouse.getY() - 14);
            }
        
        });
    }

    public void sendWord() {
        timer.cancel();
        String word;
        Stage stage = new Stage();
        if(wordField.getText()== null || wordField.getText().length() == 0 ){
            word = "noword";
        }
        else{
            word = wordField.getText();
        }
        String response = addWord(word);
        if (response.equals(SUCCESS)) {
            JOptionPane.showMessageDialog(null, "Correct Word");
        } else if (response.equals(UNCOMMON)) {
                JOptionPane.showMessageDialog(null, "You Can Only Used Given Letters");
        } else {
                JOptionPane.showMessageDialog(null, "Incorrect Word/Empty or No initial letter"); 
        }
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/RoundComplete.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(GameWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
            
    }
    
    
    static class TrieNode {

        TrieNode[] Child = new TrieNode[SIZE];
        boolean leaf;

        public TrieNode() {
            leaf = false;
            for (int i = 0; i < SIZE; i++) {
                Child[i] = null;
            }
        }
    }

    static void insert(TrieNode root, String Key) {
        int n = Key.length();
        TrieNode pChild = root;

        for (int i = 0; i < n; i++) {
            int index = Key.charAt(i) - 'a';

            if (pChild.Child[index] == null) {
                pChild.Child[index] = new TrieNode();
            }

            pChild = pChild.Child[index];
        }
        pChild.leaf = true;
    }

     String searchWord(TrieNode root, boolean Hash[],String str) {
        if (root.leaf == true) {
           System.out.println(str);
           wordField.setText(str);
           
        }
        for (int K = 0; K < SIZE; K++) {
            if (Hash[K] == true && root.Child[K] != null) {
                char c = (char) (K + 'a');
                searchWord(root.Child[K], Hash, str + c);
            }
        }
        return str;
    }

     String PrintAllWords(char Arr[], TrieNode root, int n) {
        boolean[] Hash = new boolean[SIZE];

        for (int i = 0; i < n; i++) {
            Hash[Arr[i] - 'a'] = true;
        }
        TrieNode pChild = root;
        String str = "";
        for (int i = 0; i < SIZE; i++) {
            if (Hash[i] == true && pChild.Child[i] != null) {
                str = str + (char) (i + 'a');
                str = "";
               
             s1 = searchWord(pChild.Child[i], Hash, str);    
            }
           
        }
        return s1;
         }
    
    
  
    
    
    public String autoSearch() throws FileNotFoundException, IOException{
        
        autoButton.setVisible(false);
        autoChance1.setVisible(false);
        autoChance2.setVisible(false);
        
        
     try ( //ClassLoader classloader = getClass().getClassLoader();
        //File filePath = new File(classloader.getResource("D:\\word.txt").getFile());
            BufferedReader reader = new BufferedReader(new FileReader("D:\\word.txt"))) {
            int Counter = 1;
            String line;
            while ((line = reader.readLine()) != null) {
                //read the line
                Scanner scanner = new Scanner(line);
                TrieNode root = new TrieNode();
                for (String token : line.split("@")) {
                    insert(root, token);
                    
                }
            String fulWord = letters+initial;
            char ar[] = fulWord.toCharArray();  
            int N = ar.length;
            s= PrintAllWords(ar, root, N);
            }
        }
        return s;
        
//        String[] fruits = {"zoo","cat","bus","pet","dog","mist"};
//        int idx = new Random().nextInt(fruits.length);
//        String random = (fruits[idx]);
//        wordField.setText(random);
//        autoButton.setVisible(false);
//        autoChance1.setVisible(false);
//        autoChance2.setVisible(false);
        //    static void insert(TrieNode root, String Key) {
//        int n = Key.length();
//        TrieNode pChild = root;
//
//        for (int i = 0; i < n; i++) {
//            int index = Key.charAt(i) - 'a';
//
//            if (pChild.Child[index] == null) {
//                pChild.Child[index] = new TrieNode();
//            }
//
//            pChild = pChild.Child[index];
//        }
//        pChild.leaf = true;
//    }
        
    }
    
    
    
     private void makeFadeInTransition() {
        FadeTransition fadeTransition = new FadeTransition();
            fadeTransition.setDuration(Duration.millis(3000));
            fadeTransition.setNode(gamePlayPane);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            fadeTransition.play();
    }
}
