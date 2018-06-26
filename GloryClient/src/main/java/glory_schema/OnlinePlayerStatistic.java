package glory_schema;

import javafx.beans.property.SimpleStringProperty;


public class OnlinePlayerStatistic {
    private final SimpleStringProperty player;
    private final SimpleStringProperty word;
    private final SimpleStringProperty status;
    private final SimpleStringProperty gamePoints;

    public OnlinePlayerStatistic(String player, String status, String gamePoints, String word) {
            this.player = new SimpleStringProperty(player);
            this.status = new SimpleStringProperty(status);
            this.gamePoints = new SimpleStringProperty(gamePoints);
            this.word = new SimpleStringProperty(word);
        }
    
    public String getPlayer() {
        return player.get();
    }

    public void setPlayer(String username) {
        this.player.set(username);
    }

    public String getWord() {
        return word.get();
    }

    public void setWord(String word) {
        this.word.set(word);
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getGamePoints() {
        return gamePoints.get();
    }

    public void setGamePoints(String gamePoints) {
        this.gamePoints.set(gamePoints);
    }
   
}
