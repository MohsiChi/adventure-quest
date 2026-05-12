package adventurequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameFrame extends JFrame {
    private Player player;
    private BattleManager battleManager;
    private Quest currentQuest;

    // UI Components
    private JLabel nameLabel, jobLabel, levelLabel, hpLabel;
    private JProgressBar xpBar;
    private JLabel xpLabel;
    private DefaultListModel<String> questListModel;
    private JList<String> questList;
    private JTextArea battleLogArea;
    private JLabel goldLabel, statsLabel;

    // Buttons
    private JButton acceptQuestBtn, startQuestBtn, attackBtn, skillBtn, defendBtn, useItemBtn;
    private JButton shopBtn, backpackBtn;

    // Analytics
    private java.util.Map<String, Integer> dropCounts;

    public GameFrame() {
        super("Adventure Quest");
        dropCounts = new java.util.HashMap<>();
        initPlayer();
        initUI();
        refreshAll();
    }

    private void initPlayer() {
        player = GameFileHandler.loadPlayer();
        if (player == null) {
            CreateCharDialog dialog = new CreateCharDialog(this);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                player = dialog.createPlayer();
            } else {
                // User cancelled — create default character
                player = new Player(new Warrior("Adventurer"));
            }
        }
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveAndExit();
            }
        });

        // --- LEFT PANEL ---
        JPanel leftPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        leftPanel.setPreferredSize(new Dimension(280, 0));

        // Character Panel (top-left)
        JPanel charPanel = new JPanel();
        charPanel.setLayout(new BoxLayout(charPanel, BoxLayout.Y_AXIS));
        charPanel.setBorder(BorderFactory.createTitledBorder("Character Info"));

        nameLabel = new JLabel();
        jobLabel = new JLabel();
        levelLabel = new JLabel();
        hpLabel = new JLabel();
        xpBar = new JProgressBar(0, 100);
        xpBar.setStringPainted(true);
        xpLabel = new JLabel();

        charPanel.add(nameLabel);
        charPanel.add(jobLabel);
        charPanel.add(levelLabel);
        charPanel.add(hpLabel);
        charPanel.add(Box.createVerticalStrut(5));
        charPanel.add(new JLabel("Experience:"));
        charPanel.add(xpBar);
        charPanel.add(xpLabel);

        // Quest Panel (bottom-left)
        JPanel questPanel = new JPanel(new BorderLayout(5, 5));
        questPanel.setBorder(BorderFactory.createTitledBorder("Quests"));

        questListModel = new DefaultListModel<>();
        questList = new JList<>(questListModel);
        questList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane questScroll = new JScrollPane(questList);

        questList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateButtonStates();
            }
        });

        questPanel.add(questScroll, BorderLayout.CENTER);

        leftPanel.add(charPanel);
        leftPanel.add(questPanel);

        // --- CENTER: Battle Log ---
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Battle Log"));
        battleLogArea = new JTextArea();
        battleLogArea.setEditable(false);
        battleLogArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane logScroll = new JScrollPane(battleLogArea);
        centerPanel.add(logScroll, BorderLayout.CENTER);

        // --- RIGHT: Buttons ---
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(0, 1, 5, 5));
        rightPanel.setPreferredSize(new Dimension(150, 0));

        acceptQuestBtn = new JButton("Accept Quest");
        startQuestBtn = new JButton("Start Quest");
        attackBtn = new JButton("Attack");
        skillBtn = new JButton("Use Skill");
        defendBtn = new JButton("Defend");
        useItemBtn = new JButton("Use Item");
        shopBtn = new JButton("Open Shop");
        backpackBtn = new JButton("View Backpack");

        acceptQuestBtn.addActionListener(e -> acceptQuest());
        startQuestBtn.addActionListener(e -> startQuest());
        attackBtn.addActionListener(e -> doAction("attack"));
        skillBtn.addActionListener(e -> doAction("skill"));
        defendBtn.addActionListener(e -> doAction("defend"));
        useItemBtn.addActionListener(e -> useBattleItem());
        shopBtn.addActionListener(e -> openShop());
        backpackBtn.addActionListener(e -> openBackpack());

        rightPanel.add(acceptQuestBtn);
        rightPanel.add(startQuestBtn);
        rightPanel.add(new JSeparator());
        rightPanel.add(attackBtn);
        rightPanel.add(skillBtn);
        rightPanel.add(defendBtn);
        rightPanel.add(useItemBtn);
        rightPanel.add(new JSeparator());
        rightPanel.add(shopBtn);
        rightPanel.add(backpackBtn);

        // --- BOTTOM: Status Bar ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        goldLabel = new JLabel();
        statsLabel = new JLabel();
        bottomPanel.add(goldLabel, BorderLayout.WEST);
        bottomPanel.add(statsLabel, BorderLayout.EAST);
        bottomPanel.setBorder(BorderFactory.createLoweredSoftBevelBorder());

        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        updateButtonStates();
        setSize(900, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateButtonStates() {
        boolean inBattle = battleManager != null && battleManager.getEnemy() != null
                && battleManager.getEnemy().isAlive();
        boolean hasQuest = currentQuest != null;
        boolean questSelected = questList.getSelectedIndex() >= 0;

        acceptQuestBtn.setEnabled(!inBattle);
        startQuestBtn.setEnabled(!inBattle && questSelected && currentQuest == null);
        attackBtn.setEnabled(inBattle);
        skillBtn.setEnabled(inBattle);
        defendBtn.setEnabled(inBattle);
        useItemBtn.setEnabled(inBattle && player.getInventory().getEntries().size() > 0);
        shopBtn.setEnabled(!inBattle);
        backpackBtn.setEnabled(!inBattle);
    }

    private void acceptQuest() {
        // Show quest selection
        String[] questNames = new String[GameData.ALL_QUESTS.size()];
        for (int i = 0; i < GameData.ALL_QUESTS.size(); i++) {
            Quest q = GameData.ALL_QUESTS.get(i);
            String status;
            if (player.getQuestLog().getCompleted().contains(q)) {
                status = " [Completed]";
            } else if (player.getQuestLog().getInProgress().contains(q)) {
                status = " [In Progress]";
            } else if (player.getQuestLog().getAccepted().contains(q)) {
                status = " [Accepted]";
            } else {
                status = " [Available]";
            }
            questNames[i] = q.getName() + status;
        }

        String selected = (String) JOptionPane.showInputDialog(this,
                "Select a quest to view:", "Accept Quest",
                JOptionPane.PLAIN_MESSAGE, null, questNames, questNames[0]);

        if (selected == null) return;

        int idx = -1;
        for (int i = 0; i < questNames.length; i++) {
            if (questNames[i].equals(selected)) { idx = i; break; }
        }
        if (idx < 0) return;

        Quest quest = GameData.ALL_QUESTS.get(idx);

        if (player.getQuestLog().getCompleted().contains(quest)) {
            JOptionPane.showMessageDialog(this,
                    "You have already completed this quest.",
                    "Quest Completed", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (player.getQuestLog().getInProgress().contains(quest)) {
            JOptionPane.showMessageDialog(this,
                    "This quest is already in progress!\nProgress: " +
                            player.getQuestLog().getProgress(quest) + "/" + quest.getTargetCount(),
                    "Quest In Progress", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (player.getQuestLog().getAccepted().contains(quest)) {
            JOptionPane.showMessageDialog(this,
                    "You have already accepted this quest. Start it to begin!",
                    "Quest Accepted", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        QuestDetailDialog dialog = new QuestDetailDialog(this, quest);
        dialog.setVisible(true);
        if (dialog.isAccepted()) {
            player.getQuestLog().acceptQuest(quest);
            appendLog("Quest accepted: " + quest.getName());
        }
        refreshAll();
    }

    private void startQuest() {
        int idx = questList.getSelectedIndex();
        if (idx < 0) return;

        // Find the quest from the displayed list
        Quest quest = findQuestFromDisplayIndex(idx);
        if (quest == null) return;

        if (!player.getQuestLog().getAccepted().contains(quest)) {
            JOptionPane.showMessageDialog(this,
                    "Accept the quest first before starting it.",
                    "Not Accepted", JOptionPane.WARNING_MESSAGE);
            return;
        }

        player.getQuestLog().startQuest(quest);
        currentQuest = quest;

        // Spawn enemy
        Enemy enemy = GameData.getEnemyByName(quest.getTargetEnemy());
        if (enemy == null) {
            appendLog("ERROR: Enemy not found: " + quest.getTargetEnemy());
            return;
        }
        battleManager = new BattleManager(player, enemy);
        appendLog("=== Quest Started: " + quest.getName() + " ===");
        appendLog("A " + enemy.getName() + " (Lv." + enemy.getLevel() + ") appears!");
        appendLog("Enemy HP: " + enemy.getHp() + "/" + enemy.getMaxHp());
        refreshAll();
    }

    private void doAction(String action) {
        if (battleManager == null || !battleManager.getEnemy().isAlive()) {
            appendLog("No enemy to fight! Start a quest first.");
            return;
        }

        BattleResult result = battleManager.executeTurn(action);
        appendLog(result.logText);

        if (result.victory) {
            // Enemy defeated
            player.gainXp(result.xpGained);
            player.addGold(result.goldGained);
            player.incrementKills();

            // Track drops
            if (result.droppedItem != null) {
                player.getInventory().addItem(result.droppedItem);
                int count = dropCounts.getOrDefault(result.droppedItem.getName(), 0) + 1;
                dropCounts.put(result.droppedItem.getName(), count);
                updateMostFrequentDrop();
            }

            // Update quest progress
            player.getQuestLog().updateProgress(battleManager.getEnemyName());

            // Check quest completion
            Quest completed = player.getQuestLog().checkCompletion();
            if (completed != null) {
                player.getQuestLog().completeQuest(completed);
                player.incrementQuestsCompleted();

                // Give quest rewards
                player.gainXp(completed.getRewardXp());
                player.addGold(completed.getRewardGold());
                if (completed.getRewardItem() != null) {
                    player.getInventory().addItem(completed.getRewardItem());
                }

                appendLog("=== QUEST COMPLETED: " + completed.getName() + " ===");
                appendLog("Rewards: +" + completed.getRewardXp() + " XP, +" +
                        completed.getRewardGold() + " Gold" +
                        (completed.getRewardItem() != null ? ", " + completed.getRewardItem().getName() : ""));

                currentQuest = null;
                battleManager = null;
            } else if (currentQuest != null) {
                // Spawn next enemy
                Enemy next = GameData.getEnemyByName(currentQuest.getTargetEnemy());
                if (next != null) {
                    battleManager.setEnemy(next);
                    appendLog("Another " + next.getName() + " appears! (" +
                            player.getQuestLog().getProgress(currentQuest) + "/" +
                            currentQuest.getTargetCount() + " defeated)");
                }
            }

            // Check level up
            int prevLevel = player.getLevel();
            player.gainXp(result.xpGained); // XP already added, but trigger check
            if (player.getLevel() > prevLevel) {
                appendLog("*** LEVEL UP! You are now level " + player.getLevel() + "! ***");
            }
        }

        if (result.playerDead) {
            player.incrementDeaths();
            player.setHp(player.getMaxHp() / 2);
            appendLog("You wake up at a safe location with half HP restored.");
            battleManager = null;
            currentQuest = null;
        }

        refreshAll();
    }

    private void useBattleItem() {
        if (battleManager == null || !battleManager.getEnemy().isAlive()) return;

        // Show potions from inventory
        java.util.List<Inventory.InventoryEntry> potions = new java.util.ArrayList<>();
        for (Inventory.InventoryEntry entry : player.getInventory().getEntries()) {
            if (entry.item.getType() == ItemType.POTION && entry.quantity > 0) {
                potions.add(entry);
            }
        }

        if (potions.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "You have no potions to use!", "No Items",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] potionNames = new String[potions.size()];
        for (int i = 0; i < potions.size(); i++) {
            Inventory.InventoryEntry e = potions.get(i);
            potionNames[i] = e.item.getName() + " x" + e.quantity +
                    " (Heals " + e.item.getStatBonus() + " HP)";
        }

        String selected = (String) JOptionPane.showInputDialog(this,
                "Select a potion to use:", "Use Item",
                JOptionPane.PLAIN_MESSAGE, null, potionNames, potionNames[0]);

        if (selected == null) return;

        int idx = -1;
        for (int i = 0; i < potionNames.length; i++) {
            if (potionNames[i].equals(selected)) { idx = i; break; }
        }
        if (idx < 0) return;

        BattleResult result = battleManager.useItem(potions.get(idx).item);
        appendLog(result.logText);

        if (result.playerDead) {
            player.incrementDeaths();
            player.setHp(player.getMaxHp() / 2);
            appendLog("You wake up at a safe location with half HP restored.");
            battleManager = null;
            currentQuest = null;
        }

        refreshAll();
    }

    private void openShop() {
        BuyItemDialog dialog = new BuyItemDialog(this, player);
        dialog.setVisible(true);
        refreshAll();
    }

    private void openBackpack() {
        JDialog dialog = new JDialog(this, "Inventory", true);
        InventoryPanel panel = new InventoryPanel(player);
        dialog.add(panel);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dialog.dispose());
        JPanel btnPanel = new JPanel();
        btnPanel.add(closeBtn);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        refreshAll();
    }

    private void refreshAll() {
        // Character panel
        Character c = player.getCharacter();
        nameLabel.setText("Name: " + c.getName());
        jobLabel.setText("Class: " + c.getJobName());
        levelLabel.setText("Level: " + c.getLevel());
        hpLabel.setText("HP: " + c.getHp() + " / " + c.getMaxHp());

        // XP bar
        int xpNeeded = player.getXpForNextLevel();
        xpBar.setMaximum(xpNeeded);
        xpBar.setValue(Math.min(c.getXp(), xpNeeded));
        xpBar.setString(c.getXp() + " / " + xpNeeded + " XP");
        xpLabel.setText("Next level at: " + xpNeeded + " XP");

        // Quest list
        questListModel.clear();
        for (Quest q : player.getQuestLog().getInProgress()) {
            int prog = player.getQuestLog().getProgress(q);
            questListModel.addElement("[In Progress] " + q.getName() + " (" + prog + "/" + q.getTargetCount() + ")");
        }
        for (Quest q : player.getQuestLog().getAccepted()) {
            questListModel.addElement("[Accepted] " + q.getName() + " - " + q.getTargetEnemy() + " x" + q.getTargetCount());
        }
        for (Quest q : player.getQuestLog().getCompleted()) {
            questListModel.addElement("[Completed] " + q.getName());
        }
        // Available quests
        for (Quest q : GameData.ALL_QUESTS) {
            if (player.getQuestLog().isQuestAvailable(q)) {
                questListModel.addElement("[Available] " + q.getName() + " - " + q.getTargetEnemy() + " x" + q.getTargetCount());
            }
        }

        // Status bar
        goldLabel.setText("Gold: " + player.getGold());
        statsLabel.setText("Kills: " + player.getTotalKills() +
                " | Quests Done: " + player.getQuestsCompleted() +
                " | Deaths: " + player.getDeaths());

        updateButtonStates();
    }

    private void appendLog(String text) {
        battleLogArea.append(text + "\n");
        battleLogArea.setCaretPosition(battleLogArea.getDocument().getLength());
    }

    private Quest findQuestFromDisplayIndex(int idx) {
        if (idx < 0) return null;
        String display = questListModel.getElementAt(idx);
        for (Quest q : GameData.ALL_QUESTS) {
            if (display.contains(q.getName())) {
                return q;
            }
        }
        return null;
    }

    private void updateMostFrequentDrop() {
        String topItem = "None";
        int topCount = 0;
        for (java.util.Map.Entry<String, Integer> entry : dropCounts.entrySet()) {
            if (entry.getValue() > topCount) {
                topCount = entry.getValue();
                topItem = entry.getKey();
            }
        }
        player.setMostFrequentDrop(topItem);
    }

    private void saveAndExit() {
        GameFileHandler.savePlayer(player);
        dispose();
        System.exit(0);
    }
}
