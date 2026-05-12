package adventurequest;

public class Warrior extends Character {
    private boolean defenseBuffActive;
    private int defenseBuffTurns;

    public Warrior(String name) {
        super(name, 1, 120, 12, 15);
        this.defenseBuffActive = false;
        this.defenseBuffTurns = 0;
    }

    @Override
    public int attack(Enemy e) {
        int damage = Math.max(1, attack - e.defend());
        e.takeDamage(damage);
        tickBuffs();
        return damage;
    }

    @Override
    public int defend() {
        if (defenseBuffActive) {
            return (int) (defense * 1.5);
        }
        return defense;
    }

    @Override
    public String useSkill() {
        defenseBuffActive = true;
        defenseBuffTurns = 3;
        return name + " uses Iron Defense! Defense boosted by 50% for 3 turns.";
    }

    private void tickBuffs() {
        if (defenseBuffActive) {
            defenseBuffTurns--;
            if (defenseBuffTurns <= 0) {
                defenseBuffActive = false;
            }
        }
    }

    public boolean isDefenseBuffActive() { return defenseBuffActive; }

    @Override
    public String getJobName() { return "Warrior"; }

    @Override
    public int[] getLevelUpGains() {
        return new int[]{12, 3, 4}; // HP, Attack, Defense
    }

    @Override
    public String getStatus() {
        String status = "Warrior: " + name + " Lv." + level + "\n";
        status += "HP: " + hp + "/" + maxHp + "\n";
        status += "ATK: " + attack + " | DEF: " + defense;
        if (defenseBuffActive) {
            status += " [Buffed x1.5, " + defenseBuffTurns + " turns]";
        }
        return status;
    }

    @Override
    public String toString() {
        return "Warrior: " + name + " Lv." + level;
    }
}
