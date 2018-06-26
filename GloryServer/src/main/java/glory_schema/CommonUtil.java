package glory_schema;

import static glory_schema.CommonData.VOWELS;
import static glory_schema.CurrentPlay.currentRound;
import static glory_schema.CurrentPlay.getPLAYERS;
import static glory_schema.CurrentPlay.getPLAYER_ROUND_STATISTICS;
import static glory_schema.CurrentPlay.getSPECIAL_POINTS;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;


public class CommonUtil {

    
    
    public static PlayerStatistics getPlayerStatisticsFromPlayer(Player player) {
        Map<Integer, Map<Player, PlayerStatistics>> playerRoundStatistics = getPLAYER_ROUND_STATISTICS();
        Map<Player, PlayerStatistics> playerStatistics = playerRoundStatistics.get(currentRound);
        return playerStatistics.get(player);
    }
    
    public static Player getLeastScorePlayer(){
        Map<Integer, Map<Player, PlayerStatistics>> playerRoundStatistics = getPLAYER_ROUND_STATISTICS();
        Map<Player, PlayerStatistics> playerStatistics = playerRoundStatistics.get(currentRound - 1);
        int lowest = Integer.MAX_VALUE;
        Player lowestPlayer = null;
        for(Entry<Player, PlayerStatistics> statistic : playerStatistics.entrySet()){
            if(statistic.getValue().getScore() < lowest){
                lowest = statistic.getValue().getScore();
                lowestPlayer = statistic.getKey();
            }
        }
        return lowestPlayer;
    }

    public static String getRandomVowels(int vowelsRequired) {
        Random r = new Random();
        String vowels = new String();
        for (int i = 0; i < vowelsRequired; i++) {
            int c = r.nextInt(VOWELS.length);
            vowels = vowels.concat(String.valueOf(VOWELS[c]));
        }
        return vowels;
    }

    public static String getRandomConsonants(int consonantsRequired) {
        Random r = new Random();
        String consonants = new String();
        for (int i = 0; i < consonantsRequired;) {
            boolean vowelFound = false;
            char c = (char) (r.nextInt(26) + 'a');
            for (char vowel : VOWELS) {
                if (c == vowel) {
                    vowelFound = true;
                }
            }
            if (vowelFound) {
                continue;
            }
            i++;
            consonants = consonants.concat(String.valueOf(c));
        }
        return consonants;
    }

    public static Player findPlayer(String username) {
        for (Player player : getPLAYERS()) {
            if (player.getUsername().equals(username)) {
                return player;
            }
        }
        return null;
    }

    public static List<Player> findRoundCompletedPlayers() {
        List<Player> roundCompletedPlayers = new ArrayList<Player>();
        for (Player player : getPLAYERS()) {
            if (player.getPlayerStatus() == PlayerStatus.ROUND_COMPLETED) {
                roundCompletedPlayers.add(player);
            }
        }
        return roundCompletedPlayers;
    }

    public static boolean checkRoundEnd() {
        boolean roundEnd = true;
        for (Player player : getPLAYERS()) {
            if (player.getPlayerStatus() != PlayerStatus.ROUND_COMPLETED) {
                if(player.getPlayerStatus() == PlayerStatus.TERMINATED){
                    continue;
                }
                roundEnd = false;
                break;
            }
        }
        return roundEnd;
    }

    public static boolean verifyWord(PlayerStatistics statistics) {
        String word = statistics.getWord();
        boolean validWord = true;
        StringBuilder buffer = new StringBuilder();
        buffer.append(statistics.getInitialLetters());
        buffer.append(statistics.getConsonants());
        buffer.append(statistics.getVowels());

        char[] result = buffer.toString().toCharArray();
        for (int i = 0; i < word.length(); i++) {
            char wordLetter = word.charAt(i);
            if (!validWord) {
                return false;
            }
            validWord = false;
            for (char c : result) {
                if (wordLetter == c) {
                    validWord = true;
                }
            }
        }
        return true;
    }

    public static int getPoints(PlayerStatistics statistics) {
        
        
        int wordValue = 0;
        int noOfInitialUsesFound = 0;
        String rev=""; 
        int res = 0;
        for (char c : statistics.getInitialLetters().toCharArray()) {
            
             if(statistics.getWord().length()>10){
               wordValue = wordValue*2;
            }
            
            
            if(statistics.getWord().length()>8){
               wordValue = wordValue+10;
            }
            
            
            for ( int i = statistics.getWord().length() - 1; i >= 0; i-- ){
            
            rev = rev + statistics.getWord().charAt(i);
            }
            if (statistics.getWord().equals(rev)){
               wordValue = wordValue + 10;
               System.out.println("You make a palindrome word, CONGRATS. You have reward Type1 and got bonus 10 points");    
            }
            else{
                System.out.println("This is not a palindrome word, No bonus points"); 
            }    
        

            if (statistics.getWord().indexOf(c) >= 0) {
                wordValue = wordValue + 5;
                noOfInitialUsesFound++;
            }
            
            for (int i=0; i<statistics.getWord().length(); i++)
            {
                char z;
                // checking character in string
                if (statistics.getWord().charAt(i) == 'z')
                res++;
            } 

            if(res<1){
                System.out.println("Your word does not contain 2 Z's");
            }
            else{
                System.out.println("Your word have 2 Z's,CONGRATS. You have reward Type2 and got bonus 10 points");
                wordValue = wordValue + 10;
                System.out.println("Final score after the reward Type 02 : "+wordValue+ "\n");
            }    
            
            
        }
        wordValue = wordValue + statistics.getWord().length() + noOfInitialUsesFound;
        return wordValue;
    }

    public static int getCurrentSpecialPoints(Player player) {
        Map<Player, Integer> specialPoints = getSPECIAL_POINTS();
        return specialPoints.get(player);
    }

    public static void calculateRoundSpecialPoints(Map<Player, PlayerStatistics> playerStatistics) {  
        int oldPoints = 0, currentRoundPoints = 0;
        Map<Player, Integer> specialPoints = getSPECIAL_POINTS();
        List<Entry<Player, PlayerStatistics>> sortedStatistics = new LinkedList<Entry<Player, PlayerStatistics>>(playerStatistics.entrySet());
        Collections.sort(sortedStatistics, new Comparator<Entry<Player, PlayerStatistics>>() {
            @Override
            public int compare(Entry<Player, PlayerStatistics> statOne, Entry<Player, PlayerStatistics> statTwo) {
                return statTwo.getValue().getScore().compareTo(statOne.getValue().getScore());
            }
        });
        int count = 1;
        for (Entry<Player, PlayerStatistics> statistic : sortedStatistics) {
            oldPoints = getCurrentSpecialPoints(statistic.getKey());
            currentRoundPoints = (playerStatistics.size() - count);
            specialPoints.put(statistic.getKey(), currentRoundPoints + oldPoints);
            count++;
        }
        
        System.out.println("Special " + specialPoints);
    }
    
    public static void clearAllPlayers(){
        List<Player> players = getPLAYERS();
        players.clear();
    }
    
    public static boolean removePlayer(String username){
        Player player = findPlayer(username);
        return getPLAYERS().remove(player);
    }
    
    public static String checkPlayable(){
        if(currentRound > 0){
            return "Round Ongoing";
        }
        else if(getPLAYERS().size() < 2){
            return "Two players needed, " + getPLAYERS().size() + " found";
        }
        else{
            return "playable";
        }
    }
    
    public static Player getTerminatedPlayer(){
        for(Player player : getPLAYERS()){
            if(player.getPlayerStatus() == PlayerStatus.TERMINATED){
                return player;
            }
        }
        return null;
    }
}
