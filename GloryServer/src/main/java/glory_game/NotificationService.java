package glory_game;

import glory_schema.CommonUtil;
import static glory_schema.CommonUtil.calculateRoundSpecialPoints;
import static glory_schema.CommonUtil.checkRoundEnd;
import static glory_schema.CommonUtil.getPlayerStatisticsFromPlayer;
import static glory_schema.CurrentPlay.currentRound;
import static glory_schema.CurrentPlay.getPLAYER_ROUND_STATISTICS;
import glory_schema.Player;
import glory_schema.PlayerStatistics;
import java.util.Map;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.glassfish.jersey.media.sse.SseFeature;


@Singleton
@Path("/BroadCaster")
public class NotificationService {

    private final SseBroadcaster broadcaster = new SseBroadcaster();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/sendPlayerJoin")
    public String broadcastJoinMessage(String message) {
        OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
        OutboundEvent event = eventBuilder.name("joined")
                .mediaType(MediaType.TEXT_PLAIN_TYPE)
                .data(String.class, message)
                .build();

        broadcaster.broadcast(event);

        return "You have Joined";
    }

    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    @Path("/listenPlayerJoin")
    public EventOutput listenToJoinBroadcast() {
        final EventOutput eventOutput = new EventOutput();
        this.broadcaster.add(eventOutput);
        return eventOutput;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/sendPlayerRoundCompletion")
    public String broadCastRoundCompletionMessage(Player player) {
        PlayerStatistics statistics = getPlayerStatisticsFromPlayer(player);
        OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
        OutboundEvent event = eventBuilder.name("completed")
                .mediaType(MediaType.TEXT_PLAIN_TYPE)
                .data(String.class, player.getUsername() + "@" + statistics.getWordStatus()
                        + "@" + statistics.getScore() + "@" + statistics.getWord())
                .build();
        broadcaster.broadcast(event);
        synchronized (NotificationService.class) {
            if (checkRoundEnd()) {
                broadcastNextRoundMessage(player.getUsername());
                Map<Integer, Map<Player, PlayerStatistics>> playerRoundStatistics = getPLAYER_ROUND_STATISTICS();
                Map<Player, PlayerStatistics> playerStatistics = playerRoundStatistics.get(currentRound);
                calculateRoundSpecialPoints(playerStatistics);
                currentRound++;
                return "starting round " + currentRound;
            }
        }
        return "Your have completed the round " + currentRound;
    }

    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    @Path("/listenRoundCompletion")
    public EventOutput listenToRoundCompletionBroadcast() {
        final EventOutput eventOutput = new EventOutput();
        this.broadcaster.add(eventOutput);
        return eventOutput;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/sendNextRoundStart")
    public String broadcastNextRoundMessage(String lastPlayerName) {
        OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
        if (currentRound == 3) {
            OutboundEvent event = eventBuilder.name("starting")
                    .mediaType(MediaType.TEXT_PLAIN_TYPE)
                    .data(String.class, "gameComplete")
                    .build();
            broadcaster.broadcast(event);
            currentRound = 0;
            return "gameComplete";
        }
        OutboundEvent event = eventBuilder.name("starting")
                .mediaType(MediaType.TEXT_PLAIN_TYPE)
                .data(String.class, "roundEnd")
                .build();
        broadcaster.broadcast(event);
        return "next round starting";
    }
}
