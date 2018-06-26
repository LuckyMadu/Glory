package glory_game;

import com.google.gson.Gson;
import glory_schema.CommonUtil;
import static glory_schema.CommonUtil.findPlayer;
import static glory_schema.CommonUtil.findRoundCompletedPlayers;
import static glory_schema.CommonUtil.getPlayerStatisticsFromPlayer;
import static glory_schema.CommonUtil.getTerminatedPlayer;
import glory_schema.CurrentPlay;
import glory_schema.PlayerStatus;
import static glory_schema.ResponseResult.ERROR;
import static glory_schema.ResponseResult.INVALID_LOGIN;
import static glory_schema.ResponseResult.SUCCESS;
import glory_schema.Player;
import glory_schema.PlayerStatistics;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/PlayerService")
public class PlayerService {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/registerPlayer/{email}/{username}/{password}")
    public String registerPlayer(@PathParam("email") String email, @PathParam("username") String username, @PathParam("password") String password) {
        try {
            String dataPath = System.getenv("http://localhost:8080/glorygame/WebResources/");
            FileWriter fw = new FileWriter("D:\\Players.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.println(email + " " + username + " " + password);
            out.flush();
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return SUCCESS;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/addPlayer/{username}/{password}")
    public String addPlayer(@PathParam("username") String username, @PathParam("password") String password) throws IOException {
        BufferedReader reader = null;
        boolean isValid = false;
        try {
            Player player = new Player();
            player.setUsername(username);
            player.setPassword(password);
            reader = new BufferedReader(new FileReader("D:\\Players.txt"));
            String dataPath = System.getenv("http://localhost:8080/glorygame/WebResources/");
            //reader = new BufferedReader(new FileReader(dataPath +"/Players.txt"));
            String text = null;
            while ((text = reader.readLine()) != null) {
                String[] user = text.split(" ");
                System.out.println("Text " + text);
                if (username.equals(user[1]) && password.equals(user[2])) {
                    System.out.println("Valid user");
                    isValid = true;
                    break;
                }
            }   reader.close();
            if(!isValid){
                return INVALID_LOGIN;
            }
            List<Player> players = CurrentPlay.getPLAYERS();
            boolean isAdded = players.contains(player);
            if (isAdded) {
                player = findPlayer(player.getUsername());
                player.setPlayerStatus(PlayerStatus.JOINED);
                return SUCCESS;
            }   
            players.add(0, player);
            System.out.println(player.getUsername() + " Joined");
            Player listPlayer = findPlayer(player.getUsername());
            listPlayer.setPlayerStatus(PlayerStatus.JOINED);
            return SUCCESS;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PlayerService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(PlayerService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getPlayers")
    public List<Player> getPlayers() {
        return CurrentPlay.getPLAYERS();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getRoundCompletedPlayers")
    public String getRoundCompletedPlayers() {
        List<String> completedPlayer = new ArrayList<String>();
        List<Player> players = findRoundCompletedPlayers();
        for (Player player : players) {
            PlayerStatistics statistics = getPlayerStatisticsFromPlayer(player);
            String statisticString = player.getUsername() + "@" + statistics.getWordStatus()
                    + "@" + statistics.getScore() + "@" + statistics.getWord();
            completedPlayer.add(statisticString);
        }
        return new Gson().toJson(completedPlayer);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/removePlayer")
    public String removePlayer(Player player) {
        boolean isRemoved = CommonUtil.removePlayer(player.getUsername());
        if (isRemoved) {
            return SUCCESS;
        } else {
            return ERROR;
        }
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/clearAllPlayers")
    public String clearAllPlayers() {
        CommonUtil.clearAllPlayers();
        return SUCCESS;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/terminatePlayer")
    public String terminatePlayer(String player) {
        System.out.println("Perk Activated");
        Player terminatePlayer = findPlayer(CommonUtil.getLeastScorePlayer().getUsername());
        terminatePlayer.setPlayerStatus(PlayerStatus.TERMINATED);
        return "player terminated";
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/checkTermination")
    public String checkTermination(Player player) {
        Player terminatedPlayer = getTerminatedPlayer();
        if (terminatedPlayer != null) {
            return terminatedPlayer.getUsername() + " Terminated";
        } else {
            return "No One Terminated";
        }
    }
}
