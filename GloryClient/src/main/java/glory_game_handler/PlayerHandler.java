package glory_game_handler;

import com.google.gson.Gson;
import static glory_schema.FunctionElement.setPlayerJoinModelData;
import glory_schema.OnlinePlayerStatus;
import glory_schema.VariableElement;
import static glory_schema.VariableElement.ADD_PLAYER;
import static glory_schema.VariableElement.BROADCAST;
import static glory_schema.VariableElement.CHECK_TERMINATION;
import static glory_schema.VariableElement.GET;
import static glory_schema.VariableElement.GET_PLAYERS;
import static glory_schema.VariableElement.GET_ROUND_COMPLETED_PLAYERS;
import static glory_schema.VariableElement.PLAYER_CLASS;
import static glory_schema.VariableElement.PLAYER_JOIN_BROADCAST;
import static glory_schema.VariableElement.PLAYER_JOIN_LISTEN;
import static glory_schema.VariableElement.POST;
import static glory_schema.VariableElement.REGISTER_PLAYER;
import static glory_schema.VariableElement.REMOVE_PLAYER;
import static glory_schema.VariableElement.ROUND_COMPLETION_BROADCAST;
import static glory_schema.VariableElement.ROUND_COMPLETION_LISTEN;
import static glory_schema.VariableElement.TERMINATE_PLAYER;
import static glory_schema.VariableElement.username;
import static glory_schema.FunctionElement.addObservableListData;
import static glory_schema.ResponseResult.SUCCESS;
import glory_schema.OnlinePlayer;
import glory_schema.OnlinePlayerStatistic;
import static glory_game_handler.PointHandler.getSpecialPoints;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;

/**
 *
 * @author Lakshitha
 */
public class PlayerHandler {

    private static ObservableList<String> model = null;
    private static ObservableList<OnlinePlayerStatistic> playerScore = null;
    private static Label label;
    private static int counter = 10;
    private static Timer timer = new Timer();
    private static Stage stage = null;
    private static Label specialPointsLabel;

    public static void setModelReference(ObservableList<String> model) {
        PlayerHandler.model = model;
    }

    public static void setLabelReference(Label label) {
        PlayerHandler.label = label;
    }

    public static void setFrameReference(Stage stage) {
        PlayerHandler.stage = stage;
    }

    public static Label getSpecialPointsLabel() {
        return specialPointsLabel;
    }

    public static void setSpecialPointsLabel(Label specialPointsLabel) {
        PlayerHandler.specialPointsLabel = specialPointsLabel;
    }

    public static void setPlayerScore(ObservableList<OnlinePlayerStatistic> playerScore) {
        PlayerHandler.playerScore = playerScore;
    }
    
    public static String RegisterPlayer(String playerName, String playerPassword, String email) {
        String output = null;
        try {
            HttpURLConnection connection = new FactoryHandler().getServiceConnection(PLAYER_CLASS, REGISTER_PLAYER + "/"+email+"/"+playerName+"/"+playerPassword, POST);
            sendOutput(null, connection);
            output = getInput(connection);
        } catch (Exception e) {
            System.out.println(e);
        }
        return output;
    }

    public static String LoginPlayer(String playerName, String playerPassword) {
        String output = null;
        try {
            HttpURLConnection connection = new FactoryHandler().getServiceConnection(PLAYER_CLASS, ADD_PLAYER+"/"+playerName+"/"+playerPassword, POST);
            sendOutput(playerName, connection);

            output = getInput(connection);
        } catch (Exception e) {
            System.out.println(e);
        }
        return output;
    }

    public static OnlinePlayer[] getAllPlayers() {
        OnlinePlayer[] playerList = null;
        try {
            HttpURLConnection connection = new FactoryHandler().getServiceConnection(PLAYER_CLASS, GET_PLAYERS, GET);
            String output = getInput(connection);
            Gson parser = new Gson();
            playerList = parser.fromJson(output, OnlinePlayer[].class);

        } catch (IOException e) {
            System.out.println(e);
        }
        return playerList;
    }

    public static String[] getRoundCompletedPlayers() {
        String[] playerList = null;
        try {
            HttpURLConnection connection = new FactoryHandler().getServiceConnection(PLAYER_CLASS, GET_ROUND_COMPLETED_PLAYERS, GET);
            String output = getInput(connection);
            Gson parser = new Gson();
            playerList = parser.fromJson(output, String[].class);
        } catch (IOException e) {
            System.out.println(e);
        }
        return playerList;
    }

    public static void notifyPlayerJoin() {
        String output = null;
        try {
            HttpURLConnection connection = new FactoryHandler().getServiceConnection(BROADCAST, PLAYER_JOIN_BROADCAST, POST);
            String input = VariableElement.username;
            OutputStream os = connection.getOutputStream();
            os.write(input.getBytes());
            os.flush();
            output = getInput(connection);
            System.out.println(output);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void listenToJoinEvent() {
        Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();
        WebTarget target = client.target(VariableElement.IP + BROADCAST + PLAYER_JOIN_LISTEN);

        EventInput eventInput = target.request().get(EventInput.class);
        while (!eventInput.isClosed()) {
            final InboundEvent inboundEvent = eventInput.read();
            if (VariableElement.playerStatus == OnlinePlayerStatus.PLAYING) {
                break;
            }
            if (inboundEvent == null) {
                break;
            }
            Platform.runLater(new Runnable() {
                public void run() {
                    setPlayerJoinModelData(inboundEvent.readData(String.class), model);
                }
            });

        }
        System.out.println("Done");
    }

    public static void notifyRoundCompletion() {
        String output = null;
        try {
            System.out.println(username + " notifing completion");
            HttpURLConnection connection = new FactoryHandler().getServiceConnection(BROADCAST, ROUND_COMPLETION_BROADCAST, POST);
            sendOutput(username, connection);
            output = getInput(connection);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void terminatePlayer() {
        String output = null;
        try {
            HttpURLConnection connection = new FactoryHandler().getServiceConnection(PLAYER_CLASS, TERMINATE_PLAYER, POST);
            sendOutput(username, connection);
            output = getInput(connection);
            System.out.println(output);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void listenToRoundCompletionEvent() {
        Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();
        WebTarget target = client.target(VariableElement.IP + BROADCAST + ROUND_COMPLETION_LISTEN);

        EventInput eventInput = target.request().get(EventInput.class);
        while (!eventInput.isClosed()) {
            final InboundEvent inboundEvent = eventInput.read();
            if (inboundEvent == null) {
                break;
            }
            System.out.println(username + " > " + inboundEvent.readData(String.class));

            counter = 10;
            timer = new Timer();
            if (inboundEvent.readData(String.class).equals("roundEnd")) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        String sp = getSpecialPoints(username);
                        VariableElement.specialPoints = Integer.parseInt(sp);
                        specialPointsLabel.setText(sp);
                    }
                });
                timer.schedule(new TimerTask() {
                    public void run() {
                        Platform.runLater(new Runnable() {
                            public void run() {
                                if (counter == 0) {
                                    timer.cancel();
                                    Stage stage = (Stage) label.getScene().getWindow();
                                    stage.close();
                                    Parent root = null;
                                    try {
                                        root = FXMLLoader.load(getClass().getResource("/fxml/GameWindow.fxml"));
                                    } catch (IOException ex) {
                                        Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    Scene scene = new Scene(root);
                                    scene.getStylesheets().add("/styles/Styles.css");
                                    stage.setResizable(false);
                                    stage.setScene(scene);
                                    stage.show();
                                } else {
                                    label.setText(String.valueOf(counter));
                                    counter--;
                                }
                            }
                        });
                    }
                }, 0, 1000);
                System.out.println(username + " stopped listening");
                break;
            } else if (inboundEvent.readData(String.class).equals("gameComplete")) {
                Platform.runLater(() -> {
                    Stage scoringMenu = new Stage();
                    Parent root = null;
                    try {
                        root = FXMLLoader.load(new Object().getClass().getResource("/fxml/ScoringMenu.fxml"));
                    } catch (IOException ex) {
                        Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Scene scene = new Scene(root);
                    scene.getStylesheets().add("/styles/Styles.css");
                    scoringMenu.setResizable(false);
                    scoringMenu.initStyle(StageStyle.UNDECORATED);
                    scoringMenu.setScene(scene);
                    scoringMenu.show();
                    scoringMenu = (Stage) label.getScene().getWindow();
                    scoringMenu.close();

                });
                break;
            } else {
                Platform.runLater(new Runnable() {
                    public void run() {
                        addObservableListData(inboundEvent.readData(String.class), playerScore);
                    }
                });

            }
        }
    }

    public static String removePlayer(String username) {
        String output = null;
        try {
            HttpURLConnection connection = new FactoryHandler().getServiceConnection(PLAYER_CLASS, REMOVE_PLAYER, GET);
            sendOutput(username, connection);
            output = getInput(connection);
        } catch (IOException e) {
            System.out.println(e);
        }
        return output;
    }
    
    public static String checkTermination(){
        String output = null;
        try {
            HttpURLConnection connection = new FactoryHandler().getServiceConnection(PLAYER_CLASS, CHECK_TERMINATION, POST);
            sendOutput(username, connection);
            output = getInput(connection);
        } catch (Exception e) {
            System.out.println(e);
        }
        return output;
    }

    private static void sendOutput(String username, HttpURLConnection connection) {
        try {
            OnlinePlayer player = new OnlinePlayer();
            player.setUsername(username);
            String input = new Gson().toJson(player);
            OutputStream os = connection.getOutputStream();
            os.write(input.getBytes());
            os.flush();
        } catch (IOException ex) {
            Logger.getLogger(WordHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String getInput(HttpURLConnection connection) {
        BufferedReader br = null;
        String output = null;
        try {
            br = new BufferedReader(new InputStreamReader(
                    (connection.getInputStream())));
            output = br.readLine();
            return output;
        } catch (IOException ex) {
            Logger.getLogger(WordHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(WordHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return output;
    }
}
