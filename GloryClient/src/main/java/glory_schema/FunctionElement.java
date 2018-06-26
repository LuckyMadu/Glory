package glory_schema;

import glory_game_controller.MainMenuController;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class FunctionElement {

    private static Set<String> completedPlayerSet;
    private static final int COLUMN_WIDTH = 150;
    public static MainMenuController mainMenu;

    public static void setPlayerJoinModelData(OnlinePlayer[] players, ObservableList<String> model) {
        for (OnlinePlayer p : players) {
            System.out.println(p.getUsername());
            if (p.getUsername().equals(VariableElement.username)) {
                model.add("You joined");
            } else {
                model.add(p.getUsername() + " joined");
            }
        }
    }

    public static void setPlayerJoinModelData(String player, ObservableList<String> model) {
        model.add(0, player + " joined");
        if(model.size() >= 2 && VariableElement.isWaiting){
            try {
                mainMenu.openGame();
            } catch (IOException ex) {
                Logger.getLogger(FunctionElement.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void setRoundCompletedModelData(String[] statistics, ObservableList<String> model) {
        completedPlayerSet = new HashSet<>();
        for (String p : statistics) {
            completedPlayerSet.add(p);
            p = p.replaceAll("@", " ");
            model.add(p);
        }
    }

    public static void setRoundCompletedModelData(String statistic, ObservableList<String> model) {
        if (completedPlayerSet.add(statistic)) {
            statistic = statistic.replaceAll("@", " ");
            model.add(0, statistic);
        }
    }
    
    
    private static void setObservableListData(ObservableList<OnlinePlayerStatistic> data, String [] playerStatistics){
        completedPlayerSet = new HashSet<>();
        for(String p : playerStatistics){
            completedPlayerSet.add(p);
            String [] statisticParts = p.trim().split("@");
            OnlinePlayerStatistic playerStatistic = new OnlinePlayerStatistic(statisticParts[0], statisticParts[1], statisticParts[2], statisticParts[3]);
            data.add(playerStatistic);
        }
    }
    
    public static void addObservableListData(String statistic, ObservableList<OnlinePlayerStatistic> data){
        if (completedPlayerSet.add(statistic)) {
            String [] statisticParts = statistic.trim().split("@");
            OnlinePlayerStatistic playerStatistic = new OnlinePlayerStatistic(statisticParts[0], statisticParts[1], statisticParts[2], statisticParts[3]);
            data.add(playerStatistic);
        }
    }
    
    public static void setTableColumns(TableView<OnlinePlayerStatistic> scoreTable, ObservableList<OnlinePlayerStatistic> data, String [] playerScores){
        scoreTable.setEditable(true);
        
        TableColumn playerColumn = new TableColumn("Player");
        playerColumn.setMinWidth(COLUMN_WIDTH);
        playerColumn.setCellValueFactory(new PropertyValueFactory<OnlinePlayerStatistic,String>("Player"));

//        TableColumn wordColumn = new TableColumn("Status");
//        wordColumn.setMinWidth(COLUMN_WIDTH);
//        wordColumn.setCellValueFactory(
//            new PropertyValueFactory<PlayerStatistic,String>("Status")
//        );

        TableColumn statusColumn = new TableColumn("GamePoints");
        statusColumn.setMinWidth(COLUMN_WIDTH);
        statusColumn.setCellValueFactory(new PropertyValueFactory<OnlinePlayerStatistic,String>("GamePoints")
        );
        
        TableColumn gamePointsColumn = new TableColumn("Word");
        gamePointsColumn.setMinWidth(COLUMN_WIDTH);
        gamePointsColumn.setCellValueFactory(new PropertyValueFactory<OnlinePlayerStatistic,String>("Word")
        );
                        
        setObservableListData(data, playerScores);
        scoreTable.setItems(data);
        scoreTable.getColumns().addAll(playerColumn, statusColumn, gamePointsColumn);
    }
}
