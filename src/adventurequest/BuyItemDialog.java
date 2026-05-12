package adventurequest;

import javax.swing.*;
import java.awt.*;

public class BuyItemDialog extends JDialog {
    private final Player player;
    private JList<String> itemList;
    private DefaultListModel<String> listModel;

    public BuyItemDialog(JFrame owner, Player player) {
        super(owner, "Shop", true);
        this.player = player;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        JLabel goldLabel = new JLabel("Your Gold: " + player.getGold());
        goldLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Item list
        listModel = new DefaultListModel<>();
        Shop shop = new Shop();
        for (Item item : shop.getShopItems()) {
            String display = String.format("%-20s %s - %d gold (Bonus: +%d %s)",
                    item.getName(), item.getType().getDisplayName(),
                    item.getValue(), item.getStatBonus(),
                    item.getType() == ItemType.POTION ? "HP" : item.getType() == ItemType.WEAPON ? "ATK" : "DEF");
            listModel.addElement(display);
        }
        itemList = new JList<>(listModel);
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(itemList);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton buyButton = new JButton("Buy");
        JButton closeButton = new JButton("Close");

        buyButton.addActionListener(e -> {
            int selected = itemList.getSelectedIndex();
            if (selected < 0) {
                JOptionPane.showMessageDialog(this,
                        "Please select an item to buy.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Shop s = new Shop();
            Item item = s.getShopItems().get(selected);

            try {
                s.buyItem(player, item);
                goldLabel.setText("Your Gold: " + player.getGold());
                JOptionPane.showMessageDialog(this,
                        "Purchased " + item.getName() + "!\nRemaining Gold: " + player.getGold(),
                        "Purchase Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (InsufficientGoldException ex) {
                JOptionPane.showMessageDialog(this,
                        "Not enough gold!\nRequired: " + ex.getRequired() +
                                "\nAvailable: " + ex.getAvailable() +
                                "\nShortfall: " + ex.getShortfall(),
                        "Purchase Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(buyButton);
        buttonPanel.add(closeButton);

        add(goldLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(500, 350);
        setLocationRelativeTo(getOwner());
    }
}
