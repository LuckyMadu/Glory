package glory_game_handler;

import com.google.gson.Gson;
import static glory_schema.VariableElement.GAME_CLASS;
import static glory_schema.VariableElement.POST;
import static glory_schema.VariableElement.START_GAME;
import static glory_schema.VariableElement.username;
import glory_schema.OnlinePlayer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GameHandler {

    public static String startGame() {
        String output = null;
        try {
            HttpURLConnection connection = new FactoryHandler().getServiceConnection(GAME_CLASS, START_GAME, POST);
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
