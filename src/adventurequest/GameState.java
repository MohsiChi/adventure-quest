package adventurequest;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    public String playerName;
    public String jobName;
    public int level;
    public int hp;
    public int maxHp;
    public int attack;
    public int defense;
    public int xp;
    public int gold;
    public List<String> inventoryEntries;
    public List<String> completedQuestIds;
    public List<String> inProgressQuestIds;
    public List<Integer> inProgressCounts;
    public int totalKills;
    public int questsCompleted;
    public int deaths;
    public String mostFrequentDrop;

    public GameState() {
        inventoryEntries = new ArrayList<>();
        completedQuestIds = new ArrayList<>();
        inProgressQuestIds = new ArrayList<>();
        inProgressCounts = new ArrayList<>();
    }
}
