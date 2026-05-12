package adventurequest;

import java.util.*;

public class GameData {
    public static final List<Item> ALL_ITEMS = new ArrayList<>();
    public static final List<Enemy> ALL_ENEMIES = new ArrayList<>();
    public static final List<Quest> ALL_QUESTS = new ArrayList<>();

    private static final Map<String, Item> itemMap = new HashMap<>();
    private static final Map<String, Enemy> enemyMap = new HashMap<>();
    private static final Map<String, Quest> questMap = new HashMap<>();

    static {
        initItems();
        initEnemies();
        initQuests();
    }

    // --- Items (8+) ---
    private static void initItems() {
        addItem(new Item("Wooden Sword", ItemType.WEAPON, 30, 5));
        addItem(new Item("Iron Sword", ItemType.WEAPON, 80, 12));
        addItem(new Item("Mage Staff", ItemType.WEAPON, 90, 15));
        addItem(new Item("Leather Armor", ItemType.ARMOR, 40, 5));
        addItem(new Item("Chain Mail", ItemType.ARMOR, 100, 12));
        addItem(new Item("Dragon Scale", ItemType.ARMOR, 150, 18));
        addItem(new Item("Health Potion", ItemType.POTION, 25, 30));
        addItem(new Item("Greater Potion", ItemType.POTION, 60, 80));
    }

    private static void addItem(Item item) {
        ALL_ITEMS.add(item);
        itemMap.put(item.getName().toLowerCase(), item);
    }

    // --- Enemies (5+) ---
    private static void initEnemies() {
        // Slime
        Enemy slime = new Enemy("Slime", 1, 20, 5, 1, 15, 5);
        slime.addDrop(getItemByName("Health Potion"), 0.40);
        addEnemy(slime);

        // Goblin
        Enemy goblin = new Enemy("Goblin", 2, 35, 10, 3, 30, 12);
        goblin.addDrop(getItemByName("Wooden Sword"), 0.25);
        addEnemy(goblin);

        // Skeleton
        Enemy skeleton = new Enemy("Skeleton", 3, 50, 14, 5, 50, 20);
        skeleton.addDrop(getItemByName("Leather Armor"), 0.20);
        addEnemy(skeleton);

        // Dark Mage
        Enemy darkMage = new Enemy("Dark Mage", 4, 45, 22, 4, 70, 35);
        darkMage.addDrop(getItemByName("Mage Staff"), 0.15);
        addEnemy(darkMage);

        // Dragon
        Enemy dragon = new Enemy("Dragon", 6, 120, 30, 15, 200, 100);
        dragon.addDrop(getItemByName("Dragon Scale"), 0.30);
        addEnemy(dragon);

        // Bandit (extra enemy for variety)
        Enemy bandit = new Enemy("Bandit", 2, 30, 8, 2, 25, 15);
        bandit.addDrop(getItemByName("Health Potion"), 0.30);
        addEnemy(bandit);

        // Wolf
        Enemy wolf = new Enemy("Wolf", 1, 25, 7, 2, 20, 8);
        wolf.addDrop(getItemByName("Health Potion"), 0.20);
        addEnemy(wolf);
    }

    private static void addEnemy(Enemy enemy) {
        ALL_ENEMIES.add(enemy);
        enemyMap.put(enemy.getName().toLowerCase(), enemy);
    }

    // --- Quests (5+) ---
    private static void initQuests() {
        addQuest(new Quest("Q001", "Slime Extermination",
                "Clear out the slimes infesting the village outskirts.",
                "Slime", 3, 30, 50, getItemByName("Health Potion")));

        addQuest(new Quest("Q002", "Goblin Trouble",
                "Goblins have been raiding farmsteads. Deal with them.",
                "Goblin", 5, 60, 100, getItemByName("Iron Sword")));

        addQuest(new Quest("Q003", "Skeleton Slayer",
                "Undead skeletons haunt the old cemetery. Put them to rest.",
                "Skeleton", 4, 90, 150, getItemByName("Chain Mail")));

        addQuest(new Quest("Q004", "Dark Mage Hunt",
                "A rogue mage threatens the kingdom with forbidden magic.",
                "Dark Mage", 3, 150, 250, getItemByName("Greater Potion")));

        addQuest(new Quest("Q005", "Dragon Slayer",
                "An ancient dragon terrorizes the realm. Only the bravest may attempt this.",
                "Dragon", 1, 500, 500, getItemByName("Dragon Scale")));

        addQuest(new Quest("Q006", "Wolf Pack",
                "Wolves have been attacking travelers on the main road.",
                "Wolf", 4, 40, 80, getItemByName("Leather Armor")));

        addQuest(new Quest("Q007", "Bandit Camp",
                "Bandits have set up camp nearby. Drive them out.",
                "Bandit", 4, 50, 90, getItemByName("Wooden Sword")));
    }

    private static void addQuest(Quest quest) {
        ALL_QUESTS.add(quest);
        questMap.put(quest.getId(), quest);
    }

    // --- Lookup methods ---
    public static Item getItemByName(String name) {
        return itemMap.get(name.toLowerCase());
    }

    public static Enemy getEnemyByName(String name) {
        Enemy template = enemyMap.get(name.toLowerCase());
        if (template != null) {
            // Return a fresh copy so HP is reset
            Enemy copy = new Enemy(template.getName(), template.getLevel(),
                    template.getMaxHp(), template.getAttackPower(), template.defend(),
                    template.getXpReward(), template.getGoldReward());
            // Copy drop table
            return copy;
        }
        return null;
    }

    public static Enemy getEnemyTemplate(String name) {
        return enemyMap.get(name.toLowerCase());
    }

    public static Quest getQuestById(String id) {
        return questMap.get(id);
    }
}
