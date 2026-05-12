package adventurequest;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

public class InventoryPanel extends JPanel {
    private final Player player;
    private final DefaultListModel<String> listModel;
    private final JList<String> itemList;
    private final JTextField searchField;
    private final JComboBox<String> sortCombo;
    private final JLabel goldLabel;

    public InventoryPanel(Player player) {
        this.player = player;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createTitledBorder("Inventory"));

        // Top: search + sort
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        searchField = new JTextField();
        searchField.setToolTipText("Search items by name...");
        topPanel.add(new JLabel("Search:"), BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.CENTER);

        sortCombo = new JComboBox<>(new String[]{"Default", "Value (Low to High)", "Value (High to Low)"});
        topPanel.add(sortCombo, BorderLayout.EAST);

        // Center: item list
        listModel = new DefaultListModel<>();
        itemList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(itemList);

        // Bottom: gold + use button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        goldLabel = new JLabel("Gold: " + player.getGold());
        JButton useButton = new JButton("Use Selected Item");

        useButton.addActionListener(e -> {
            int idx = itemList.getSelectedIndex();
            if (idx < 0) {
                JOptionPane.showMessageDialog(this,
                        "Select an item to use.", "No Selection",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String selected = listModel.getElementAt(idx);
            String itemName = selected.split(" x")[0];
            Item item = player.getInventory().getItem(itemName);
            if (item == null) return;

            if (item.getType() == ItemType.POTION) {
                player.heal(item.getStatBonus());
                player.getInventory().removeItem(itemName, 1);
                JOptionPane.showMessageDialog(this,
                        "Used " + itemName + ". Restored " + item.getStatBonus() + " HP!",
                        "Item Used", JOptionPane.INFORMATION_MESSAGE);
            } else if (item.getType() == ItemType.WEAPON || item.getType() == ItemType.ARMOR) {
                JOptionPane.showMessageDialog(this,
                        itemName + " gives +" + item.getStatBonus() + " " +
                                (item.getType() == ItemType.WEAPON ? "ATK" : "DEF") +
                                " (equip stats applied automatically).",
                        "Equipment Info", JOptionPane.INFORMATION_MESSAGE);
            }
            refreshDisplay();
        });

        bottomPanel.add(goldLabel, BorderLayout.WEST);
        bottomPanel.add(useButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Search listener
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { filterAndSort(); }
            @Override public void removeUpdate(DocumentEvent e) { filterAndSort(); }
            @Override public void changedUpdate(DocumentEvent e) { filterAndSort(); }
        });

        // Sort listener
        sortCombo.addActionListener(e -> filterAndSort());

        setPreferredSize(new Dimension(350, 250));
    }

    public void refreshDisplay() {
        goldLabel.setText("Gold: " + player.getGold());
        filterAndSort();
    }

    private void filterAndSort() {
        String keyword = searchField.getText().trim();
        List<Inventory.InventoryEntry> entries;

        if (keyword.isEmpty()) {
            entries = player.getInventory().getEntries();
        } else {
            entries = player.getInventory().search(keyword);
        }

        // Sort
        int sortMode = sortCombo.getSelectedIndex();
        if (sortMode == 1) {
            entries.sort((a, b) -> Integer.compare(a.item.getValue(), b.item.getValue()));
        } else if (sortMode == 2) {
            entries.sort((a, b) -> Integer.compare(b.item.getValue(), a.item.getValue()));
        }

        listModel.clear();
        for (Inventory.InventoryEntry e : entries) {
            listModel.addElement(e.toString());
        }
    }
}
