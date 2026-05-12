package adventurequest;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameFileHandler {
    private static final String SAVE_PATH = "save.csv";

    public static String getSavePath() { return SAVE_PATH; }

    public static void save(GameState state) throws IOException {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(SAVE_PATH), "UTF-8"))) {

            // Line 1: PLAYER data
            writer.println(String.format("PLAYER|%s|%s|%d|%d|%d|%d|%d|%d|%d",
                    state.playerName, state.jobName, state.level, state.hp, state.maxHp,
                    state.attack, state.defense, state.xp, state.gold));

            // Line 2: INVENTORY
            StringBuilder invLine = new StringBuilder("INVENTORY");
            for (String entry : state.inventoryEntries) {
                invLine.append("|").append(entry);
            }
            writer.println(invLine.toString());

            // Line 3: QUESTS_COMPLETED
            StringBuilder completedLine = new StringBuilder("QUESTS_COMPLETED");
            for (String id : state.completedQuestIds) {
                completedLine.append("|").append(id);
            }
            writer.println(completedLine.toString());

            // Line 4: QUESTS_IN_PROGRESS
            StringBuilder progressLine = new StringBuilder("QUESTS_IN_PROGRESS");
            for (int i = 0; i < state.inProgressQuestIds.size(); i++) {
                progressLine.append("|").append(state.inProgressQuestIds.get(i))
                        .append(":").append(state.inProgressCounts.get(i));
            }
            writer.println(progressLine.toString());

            // Line 5: ANALYTICS
            writer.println(String.format("ANALYTICS|%d|%d|%d|%s",
                    state.totalKills, state.questsCompleted, state.deaths,
                    state.mostFrequentDrop != null ? state.mostFrequentDrop : "None"));
        }
    }

    public static GameState load() throws IOException {
        File file = new File(SAVE_PATH);
        if (!file.exists()) {
            return null;
        }

        GameState state = new GameState();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(SAVE_PATH), "UTF-8"))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 0) continue;

                switch (parts[0]) {
                    case "PLAYER":
                        if (parts.length >= 10) {
                            state.playerName = parts[1];
                            state.jobName = parts[2];
                            state.level = Integer.parseInt(parts[3]);
                            state.hp = Integer.parseInt(parts[4]);
                            state.maxHp = Integer.parseInt(parts[5]);
                            state.attack = Integer.parseInt(parts[6]);
                            state.defense = Integer.parseInt(parts[7]);
                            state.xp = Integer.parseInt(parts[8]);
                            state.gold = Integer.parseInt(parts[9]);
                        }
                        break;

                    case "INVENTORY":
                        for (int i = 1; i < parts.length; i++) {
                            state.inventoryEntries.add(parts[i]);
                        }
                        break;

                    case "QUESTS_COMPLETED":
                        for (int i = 1; i < parts.length; i++) {
                            state.completedQuestIds.add(parts[i]);
                        }
                        break;

                    case "QUESTS_IN_PROGRESS":
                        for (int i = 1; i < parts.length; i++) {
                            String[] qp = parts[i].split(":");
                            state.inProgressQuestIds.add(qp[0]);
                            state.inProgressCounts.add(Integer.parseInt(qp[1]));
                        }
                        break;

                    case "ANALYTICS":
                        if (parts.length >= 5) {
                            state.totalKills = Integer.parseInt(parts[1]);
                            state.questsCompleted = Integer.parseInt(parts[2]);
                            state.deaths = Integer.parseInt(parts[3]);
                            state.mostFrequentDrop = parts[4];
                        }
                        break;
                }
            }
        }
        return state;
    }

    public static Player loadPlayer() {
        try {
            GameState state = load();
            if (state == null) return null;

            Character character;
            switch (state.jobName) {
                case "Warrior":
                    character = new Warrior(state.playerName);
                    break;
                case "Mage":
                    character = new Mage(state.playerName);
                    break;
                case "Archer":
                    character = new Archer(state.playerName);
                    break;
                default:
                    return null;
            }

            character.setLevel(state.level);
            character.setMaxHp(state.maxHp);
            character.setHp(state.hp);
            character.setAttack(state.attack);
            character.setDefense(state.defense);
            character.addXp(state.xp);
            character.setGold(state.gold);

            Player player = new Player(character);
            player.getInventory().clear();

            // Restore inventory
            for (String entry : state.inventoryEntries) {
                String[] itemParts = entry.split(":");
                if (itemParts.length >= 5) {
                    String itemName = itemParts[0];
                    int quantity = Integer.parseInt(itemParts[4]);
                    Item item = GameData.getItemByName(itemName);
                    if (item != null) {
                        for (int j = 0; j < quantity; j++) {
                            player.getInventory().addItem(item);
                        }
                    }
                }
            }

            // Restore completed quests
            for (String id : state.completedQuestIds) {
                Quest q = GameData.getQuestById(id);
                if (q != null) {
                    player.getQuestLog().addCompletedQuest(q);
                }
            }

            // Restore in-progress quests
            for (int i = 0; i < state.inProgressQuestIds.size(); i++) {
                Quest q = GameData.getQuestById(state.inProgressQuestIds.get(i));
                if (q != null) {
                    player.getQuestLog().addInProgressQuest(q, state.inProgressCounts.get(i));
                }
            }

            // Restore analytics
            while (player.getTotalKills() < state.totalKills) player.incrementKills();
            while (player.getQuestsCompleted() < state.questsCompleted) player.incrementQuestsCompleted();
            while (player.getDeaths() < state.deaths) player.incrementDeaths();
            player.setMostFrequentDrop(state.mostFrequentDrop);

            return player;
        } catch (IOException e) {
            return null;
        }
    }

    public static void savePlayer(Player player) {
        GameState state = new GameState();
        state.playerName = player.getName();
        state.jobName = player.getJobName();
        state.level = player.getLevel();
        state.hp = player.getHp();
        state.maxHp = player.getMaxHp();
        state.attack = player.getAttack();
        state.defense = player.getDefense();
        state.xp = player.getXp();
        state.gold = player.getGold();

        // Inventory entries: name:type:value:statBonus:quantity
        for (Inventory.InventoryEntry entry : player.getInventory().getEntries()) {
            String s = String.format("%s:%s:%d:%d:%d",
                    entry.item.getName(), entry.item.getType().name(),
                    entry.item.getValue(), entry.item.getStatBonus(), entry.quantity);
            state.inventoryEntries.add(s);
        }

        // Quest data
        for (Quest q : player.getQuestLog().getCompleted()) {
            state.completedQuestIds.add(q.getId());
        }
        for (Quest q : player.getQuestLog().getInProgress()) {
            state.inProgressQuestIds.add(q.getId());
            state.inProgressCounts.add(player.getQuestLog().getProgress(q));
        }

        // Analytics
        state.totalKills = player.getTotalKills();
        state.questsCompleted = player.getQuestsCompleted();
        state.deaths = player.getDeaths();
        state.mostFrequentDrop = player.getMostFrequentDrop();

        try {
            save(state);
        } catch (IOException e) {
            System.err.println("Failed to save game: " + e.getMessage());
        }
    }

    public static boolean saveFileExists() {
        return new File(SAVE_PATH).exists();
    }
}
