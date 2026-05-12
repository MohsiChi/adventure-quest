package adventurequest;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class Enemy implements Combatable {
    private String name;
    private int level;
    private int hp;
    private int maxHp;
    private int attack;
    private int defense;
    private int xpReward;
    private int goldReward;
    private Map<Item, Double> dropTable;
    private static final Random random = new Random();

    public Enemy(String name, int level, int hp, int attack, int defense, int xpReward, int goldReward) {
        this.name = name;
        this.level = level;
        this.maxHp = hp;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.xpReward = xpReward;
        this.goldReward = goldReward;
        this.dropTable = new LinkedHashMap<>();
    }

    public void addDrop(Item item, double probability) {
        dropTable.put(item, probability);
    }

    public Item getRandomDrop() {
        double roll = random.nextDouble();
        double cumulative = 0.0;
        for (Map.Entry<Item, Double> entry : dropTable.entrySet()) {
            cumulative += entry.getValue();
            if (roll <= cumulative) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public int attack(Enemy e) {
        return Math.max(1, this.attack);
    }

    public int getAttackPower() {
        return attack;
    }

    @Override
    public int defend() {
        return defense;
    }

    @Override
    public String useSkill() {
        return name + " glares menacingly!";
    }

    @Override
    public String getStatus() {
        return name + " (Lv." + level + ") HP: " + hp + "/" + maxHp;
    }

    @Override
    public boolean isAlive() {
        return hp > 0;
    }

    @Override
    public String getName() { return name; }

    @Override
    public int getHp() { return hp; }

    @Override
    public int getMaxHp() { return maxHp; }

    public int getLevel() { return level; }
    public int getXpReward() { return xpReward; }
    public int getGoldReward() { return goldReward; }

    @Override
    public void takeDamage(int dmg) {
        hp = Math.max(0, hp - Math.max(0, dmg));
    }

    public void reset() {
        this.hp = this.maxHp;
    }

    @Override
    public String toString() {
        return name + " (Lv." + level + ")";
    }
}
