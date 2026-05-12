package adventurequest;

public class Quest {
    private String id;
    private String name;
    private String description;
    private String targetEnemy;
    private int targetCount;
    private int rewardGold;
    private int rewardXp;
    private Item rewardItem;

    public Quest(String id, String name, String description, String targetEnemy,
                 int targetCount, int rewardGold, int rewardXp, Item rewardItem) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.targetEnemy = targetEnemy;
        this.targetCount = targetCount;
        this.rewardGold = rewardGold;
        this.rewardXp = rewardXp;
        this.rewardItem = rewardItem;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getTargetEnemy() { return targetEnemy; }
    public int getTargetCount() { return targetCount; }
    public int getRewardGold() { return rewardGold; }
    public int getRewardXp() { return rewardXp; }
    public Item getRewardItem() { return rewardItem; }

    @Override
    public String toString() {
        return name + " [Target: " + targetEnemy + " x" + targetCount + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Quest) {
            return this.id.equals(((Quest) obj).id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
