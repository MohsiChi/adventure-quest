package adventurequest;

public class BattleResult {
    public boolean victory;
    public boolean playerDead;
    public int xpGained;
    public int goldGained;
    public Item droppedItem;
    public String logText;

    public BattleResult(boolean victory, boolean playerDead, int xpGained, int goldGained,
                        Item droppedItem, String logText) {
        this.victory = victory;
        this.playerDead = playerDead;
        this.xpGained = xpGained;
        this.goldGained = goldGained;
        this.droppedItem = droppedItem;
        this.logText = logText;
    }
}
