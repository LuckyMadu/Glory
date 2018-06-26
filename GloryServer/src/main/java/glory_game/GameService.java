package glory_game;

import static glory_schema.CommonUtil.checkPlayable;
import glory_schema.CurrentPlay;
import static glory_schema.ResponseResult.SUCCESS;
import glory_schema.Player;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/GameService")
public class GameService {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/startGame")
    public String startGame(Player player) {
        return checkPlayable();
    }
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/resetGame")
    public String resetGame() {
        CurrentPlay.currentRound = 0;
        return SUCCESS;
    }
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/resetFile")
    public String resetFile() {
        PrintWriter pw = null;
        String dataPath = System.getenv("http://localhost:8080/glorygame/WebResources/");
        try {
            pw = new PrintWriter("D:\\Players.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GameService.class.getName()).log(Level.SEVERE, null, ex);
        }
        pw.close();
        return SUCCESS;
    }
}
