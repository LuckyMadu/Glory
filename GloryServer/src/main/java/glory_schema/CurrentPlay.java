package glory_schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class CurrentPlay {

    public static int currentRound = 0;
    public static int submitedWords = 0;
    
    private static final List<Player> PLAYERS = Collections.synchronizedList(new ArrayList<Player>());

    private static final Map <Integer, Map<Player,PlayerStatistics>> PLAYER_ROUND_STATISTICS = new ConcurrentHashMap<Integer, Map<Player,PlayerStatistics>>();

    private static final Map<Player, Integer> SPECIAL_POINS = new ConcurrentHashMap<Player, Integer>();

    public static List<Player> getPLAYERS() {
        return PLAYERS;
    }

    public static Map<Integer, Map<Player, PlayerStatistics>> getPLAYER_ROUND_STATISTICS() {
        return PLAYER_ROUND_STATISTICS;
    }

    public static Map<Player,Integer> getSPECIAL_POINTS(){
        return SPECIAL_POINS;
    }
}
