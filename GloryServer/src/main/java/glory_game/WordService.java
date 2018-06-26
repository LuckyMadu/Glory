package glory_game;

import static glory_schema.CommonData.DICTIONARY_LINK;
import static glory_schema.CommonUtil.findPlayer;
import static glory_schema.CommonUtil.getPlayerStatisticsFromPlayer;
import static glory_schema.CommonUtil.getPoints;
import static glory_schema.CommonUtil.getRandomConsonants;
import static glory_schema.CommonUtil.getRandomVowels;
import static glory_schema.CommonUtil.verifyWord;
import static glory_schema.CurrentPlay.currentRound;
import static glory_schema.CurrentPlay.getPLAYER_ROUND_STATISTICS;
import static glory_schema.CurrentPlay.getSPECIAL_POINTS;
import static glory_schema.CurrentPlay.submitedWords;
import glory_schema.PlayerStatus;
import glory_schema.ResponseResult;
import static glory_schema.ResponseResult.ERROR;
import static glory_schema.ResponseResult.INCORRECT_WORD;
import static glory_schema.ResponseResult.SUCCESS;
import glory_schema.WordStatus;
import glory_schema.PlayerStatistics;
import glory_schema.Player;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/WordService")
public class WordService {

    private final static int INITIAL_LIMIT = 3;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getInitialLetters")
    public String getInitialLetters(Player player) {

        Random r = new Random();
        String characters = new String();

        for (int i = 0; i < INITIAL_LIMIT; i++) {
            char c = (char) (r.nextInt(26) + 'a');
            characters = characters.concat(String.valueOf(c));
        }

        Player listPlayer = findPlayer(player.getUsername());
        
        if(currentRound == 0){
            Map<Player, Integer> specialPoints = getSPECIAL_POINTS();
            specialPoints.put(player, 0);
        }
        
        PlayerStatistics statistics = new PlayerStatistics();
        statistics.setInitialLetters(characters);
        statistics.setWordStatus(WordStatus.NOT_SUBMITTED);

        Map<Integer, Map<Player, PlayerStatistics>> playerRoundStatistics = getPLAYER_ROUND_STATISTICS();
        Map<Player, PlayerStatistics> playerStatistics = playerRoundStatistics.get(currentRound);
        if (playerStatistics == null) {
            playerStatistics = new HashMap<Player, PlayerStatistics>();
            playerRoundStatistics.put(currentRound, playerStatistics);
        }
        playerStatistics.put(listPlayer, statistics);
        return characters;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getLetters/{vowelsRequired}/{consonantsRequired}")
    public String getLetters(Player player, @PathParam("vowelsRequired") int vowelsRequired, @PathParam("consonantsRequired") int consonantsRequired) {
        PlayerStatistics p = getPlayerStatisticsFromPlayer(player);
        Player listPlayer = findPlayer(player.getUsername());
        listPlayer.setPlayerStatus(PlayerStatus.PLAYING);
        String voweles = getRandomVowels(vowelsRequired);
        p.setVowels(voweles);
        String consonants = getRandomConsonants(consonantsRequired);
        p.setConsonants(consonants);
        return voweles + consonants;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/submitWord/{word}")
    public String addWord(Player player, @PathParam("word") String word) {
        try {
            PlayerStatistics p = getPlayerStatisticsFromPlayer(player);
            p.setWord(word);
            submitedWords++;
            String checkUpURL = DICTIONARY_LINK + word + "/json";
            URL url = new URL(checkUpURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader br = null;
            String output = null;
            try {
                br = new BufferedReader(new InputStreamReader(
                        (connection.getInputStream())));
                output = br.readLine();
                System.out.println(output);
            } catch (FileNotFoundException ex) {
                p.setWordStatus(WordStatus.INCORRECT);
                p.setScore(0);
                return INCORRECT_WORD;
            } catch (IOException ex) {
                Logger.getLogger(WordService.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(WordService.class.getName()).log(Level.SEVERE, null, ex);
                }
                Player listPlayer = findPlayer(player.getUsername());
                listPlayer.setPlayerStatus(PlayerStatus.ROUND_COMPLETED);
            }
            if(verifyWord(p)){
                p.setWordStatus(WordStatus.CORRECT);
                p.setScore(getPoints(p));
            }
            else{
                p.setWordStatus(WordStatus.UNCOMMON);
                p.setScore(0);
                return ResponseResult.UNCOMMON;
            }
            Player listedPlayer = findPlayer(player.getUsername());
            listedPlayer.setPlayerStatus(PlayerStatus.ROUND_COMPLETED);
            return SUCCESS;
        } catch (MalformedURLException ex) {
            Logger.getLogger(WordService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WordService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ERROR;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/changeLetter/{currentLetter}")
    public String changeLetter(Player player, @PathParam("currentLetter") String currentLetter) {
        currentLetter = currentLetter.toLowerCase();
        Random r = new Random();
        char c = 'a';
        while(true){
            c = (char) (r.nextInt(26) + 'a');
            if(!(currentLetter.charAt(0) == c)){
                break;
            }
        }
        PlayerStatistics p = getPlayerStatisticsFromPlayer(player);
        if(p.getVowels().contains(currentLetter)){
            p.setVowels(p.getVowels().replaceFirst(currentLetter, String.valueOf(c)));
            System.out.println("Vowel " + p.getVowels());
        }
        else{
            p.setConsonants(p.getConsonants().replaceFirst(currentLetter, String.valueOf(c)));
            System.out.println("Consonents " + p.getConsonants());
        }
        return String.valueOf(c);
    }
}
