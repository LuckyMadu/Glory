package glory_game;

import static glory_schema.CurrentPlay.getPLAYER_ROUND_STATISTICS;
import static glory_schema.CurrentPlay.getSPECIAL_POINTS;
import glory_schema.Player;
import glory_schema.PlayerStatistics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/PointService")
public class PointService {

     
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getSpecialPoints")
    public String getSpecialPoints(Player player) {
        return getSPECIAL_POINTS().get(player).toString();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getFinalScore")
    public List<String> getFinalScore(){
        Map<Integer, Map<Player, PlayerStatistics>> playerRoundStatistics = getPLAYER_ROUND_STATISTICS();   
        Map<String, Integer> playerFinalScores = new HashMap<String, Integer>();
        for(Map<Player, PlayerStatistics> playerStats : playerRoundStatistics.values()){
            for(Entry<Player, PlayerStatistics> entry : playerStats.entrySet()){
                if(!playerFinalScores.containsKey(entry.getKey().getUsername())){
                    playerFinalScores.put(entry.getKey().getUsername(), 0);
                }
                int score = playerFinalScores.get(entry.getKey().getUsername());
                score = score + entry.getValue().getScore();
                playerFinalScores.put(entry.getKey().getUsername(), score);
            }
        }
        List<String> finalScroes = new ArrayList<String>();
        for(Entry<String,Integer> playerStats : playerFinalScores.entrySet()){
            finalScroes.add(playerStats.getKey()+"@"+playerStats.getValue());
        }
        System.out.println("Final Score " + finalScroes);
        return finalScroes;
    }
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String test() {
        return "Hello";
    }
}
