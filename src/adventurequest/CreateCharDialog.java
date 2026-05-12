package adventurequest;

import javax.swing.*;
import java.awt.*;

public class CreateCharDialog extends JDialog {
    private JTextField nameField;
    private JComboBox<String> jobCombo;
    private JTextArea previewArea;
    private boolean confirmed;
    private String playerName;
    private String selectedJob;

    private static final String[] JOBS = {"Warrior", "Mage", "Archer"};
    private static final String[] JOB_DESCRIPTIONS = {
            "Warrior: High HP and Defense. Skill: Iron Defense (50% def boost for 3 turns).\nHP +12, ATK +3, DEF +4 per level.",
            "Mage: High Attack, low Defense. Skill: Fireball (2x damage, bypasses 50% armor).\nHP +6, ATK +6, DEF +2 per level.",
            "Archer: Balanced stats. Skill: Aimed Shot (50% chance for 2.5x crit next attack).\nHP +8, ATK +4, DEF +3 per level."
    };

    public CreateCharDialog(JFrame owner) {
        super(owner, "Create Your Character", true);
        this.confirmed = false;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        // Top: name input
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(new JLabel("Character Name:"));
        nameField = new JTextField(15);
        namePanel.add(nameField);

        // Middle: job selection
        JPanel jobPanel = new JPanel(new BorderLayout());
        jobPanel.setBorder(BorderFactory.createTitledBorder("Choose Your Class"));
        jobCombo = new JComboBox<>(JOBS);
        jobCombo.addActionListener(e -> updatePreview());
        jobPanel.add(jobCombo, BorderLayout.NORTH);

        previewArea = new JTextArea(5, 30);
        previewArea.setEditable(false);
        previewArea.setLineWrap(true);
        previewArea.setWrapStyleWord(true);
        jobPanel.add(new JScrollPane(previewArea), BorderLayout.CENTER);

        // Bottom: buttons
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("Create");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(e -> {
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a character name.",
                        "Name Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            playerName = nameField.getText().trim();
            selectedJob = (String) jobCombo.getSelectedItem();
            confirmed = true;
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(namePanel, BorderLayout.NORTH);
        topPanel.add(jobPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        updatePreview();
        pack();
        setLocationRelativeTo(getOwner());
        setResizable(false);
    }

    private void updatePreview() {
        int idx = jobCombo.getSelectedIndex();
        previewArea.setText(JOB_DESCRIPTIONS[idx]);
    }

    public boolean isConfirmed() { return confirmed; }
    public String getPlayerName() { return playerName; }
    public String getSelectedJob() { return selectedJob; }

    public Player createPlayer() {
        if (!confirmed) return null;
        Character character;
        switch (selectedJob) {
            case "Warrior": character = new Warrior(playerName); break;
            case "Mage": character = new Mage(playerName); break;
            case "Archer": character = new Archer(playerName); break;
            default: return null;
        }
        return new Player(character);
    }
}
