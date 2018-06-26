package glory_game_handler;

import glory_schema.VariableElement;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.ws.rs.core.MediaType;



public class FactoryHandler {

    public HttpURLConnection getServiceConnection(String service, String action, String method) throws MalformedURLException, IOException {
        URL url = new URL(VariableElement.IP + service + action);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON);
        return connection;
    }
}
