package glory_game_handler;

import glory_schema.VariableElement;
import static glory_schema.VariableElement.BROADCAST;
import static glory_schema.VariableElement.NEXT_ROUND_BROADCAST;
import static glory_schema.VariableElement.POST;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;


public class RoundHandler {

    public static void notifyNextRoundStart() {
        String output = null;
        try {
            HttpURLConnection connection = new FactoryHandler().getServiceConnection(BROADCAST, NEXT_ROUND_BROADCAST, POST);
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
