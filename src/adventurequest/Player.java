package adventurequest;

public class Player {
    private Character character;
    private Inventory inventory;
    private QuestLog questLog;
    private int totalKills;
    private int questsCompleted;
    private int deaths;
    private String mostFrequentDrop;

    public Player(Character character) {
        this.character = character;
        this.inventory = new Inventory();
        this.questLog = new QuestLog();
        this.totalKills = 0;
        this.questsCompleted = 0;
        this.deaths = 0;
        this.mostFrequentDrop = "None";
        // Give starting items
        try {
            inventory.addItem("Health Potion", 2);
        } catch (InvalidItemException ignored) {}
    }

    public Character getCharacter() { return character; }
    public Inventory getInventory() { return inventory; }
    public QuestLog getQuestLog() { return questLog; }

    public String getName() { return character.getName(); }
    public String getJobName() { return character.getJobName(); }
    public int getLevel() { return character.getLevel(); }
    public int getHp() { return character.getHp(); }
    public int getMaxHp() { return character.getMaxHp(); }
    public int getAttack() { return character.getAttack(); }
    public int getDefense() { return character.getDefense(); }
    public int getXp() { return character.getXp(); }
    public int getGold() { return character.getGold(); }

    public int attack(Enemy e) { return character.attack(e); }
    public int defend() { return character.defend(); }
    public String useSkill() { return character.useSkill(); }
    public String getStatus() { return character.getStatus(); }
    public boolean isAlive() { return character.isAlive(); }
    public void takeDamage(int dmg) { character.takeDamage(dmg); }

    public void addGold(int amount) { character.addGold(amount); }
    public boolean spendGold(int amount) throws InsufficientGoldException {
        if (character.getGold() < amount) {
            throw new InsufficientGoldException(amount, character.getGold());
        }
        return character.spendGold(amount);
    }

    public void gainXp(int amount) {
        character.addXp(amount);
        checkLevelUp();
    }

    private void checkLevelUp() {
        int xpNeeded = character.getLevel() * 100;
        while (character.getXp() >= xpNeeded) {
            character.addXp(-xpNeeded);
            levelUp();
            xpNeeded = character.getLevel() * 100;
        }
    }

    private void levelUp() {
        character.setLevel(character.getLevel() + 1);
        int[] gains = character.getLevelUpGains();
        character.setMaxHp(character.getMaxHp() + gains[0]);
        character.setHp(character.getMaxHp());
        character.setAttack(character.getAttack() + gains[1]);
        character.setDefense(character.getDefense() + gains[2]);
    }

    public int getXpForNextLevel() {
        return character.getLevel() * 100;
    }

    public int getTotalKills() { return totalKills; }
    public void incrementKills() { totalKills++; }

    public int getQuestsCompleted() { return questsCompleted; }
    public void incrementQuestsCompleted() { questsCompleted++; }

    public int getDeaths() { return deaths; }
    public void incrementDeaths() { deaths++; }

    public String getMostFrequentDrop() { return mostFrequentDrop; }
    public void setMostFrequentDrop(String name) { this.mostFrequentDrop = name; }

    public void heal(int amount) { character.heal(amount); }
    public void setHp(int hp) { character.setHp(hp); }
}
