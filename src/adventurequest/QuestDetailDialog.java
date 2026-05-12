package adventurequest;

import javax.swing.*;
import java.awt.*;

public class QuestDetailDialog extends JDialog {
    private boolean accepted;

    public QuestDetailDialog(JFrame owner, Quest quest) {
        super(owner, "Quest Details", true);
        this.accepted = false;
        initUI(quest);
    }

    private void initUI(Quest quest) {
        setLayout(new BorderLayout(10, 10));

        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        infoPanel.add(new JLabel("Quest: " + quest.getName()));
        infoPanel.add(new JLabel(" "));
        infoPanel.add(new JLabel("Description: " + quest.getDescription()));
        infoPanel.add(new JLabel("Target: " + quest.getTargetEnemy() + " x" + quest.getTargetCount()));
        infoPanel.add(new JLabel(" "));
        infoPanel.add(new JLabel("Rewards:"));
        infoPanel.add(new JLabel("  - " + quest.getRewardXp() + " XP"));
        infoPanel.add(new JLabel("  - " + quest.getRewardGold() + " Gold"));
        if (quest.getRewardItem() != null) {
            infoPanel.add(new JLabel("  - " + quest.getRewardItem().getName()));
        }

        add(infoPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton acceptButton = new JButton("Accept Quest");
        JButton closeButton = new JButton("Close");

        acceptButton.addActionListener(e -> {
            accepted = true;
            dispose();
        });

        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(acceptButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getOwner());
        setResizable(false);
    }

    public boolean isAccepted() { return accepted; }
}
