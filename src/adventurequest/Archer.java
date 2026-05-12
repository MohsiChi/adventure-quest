package adventurequest;

import java.util.Random;

public class Archer extends Character {
    private boolean critReady;
    private final Random random;

    public Archer(String name) {
        super(name, 1, 100, 15, 10);
        this.critReady = false;
        this.random = new Random();
    }

    @Override
    public int attack(Enemy e) {
        int rawDamage = attack;
        String mode;
        if (critReady) {
            critReady = false;
            rawDamage = (int) (attack * 2.5);
        }
        int damage = Math.max(1, rawDamage - e.defend());
        e.takeDamage(damage);
        return damage;
    }

    @Override
    public int defend() {
        return defense;
    }

    @Override
    public String useSkill() {
        if (random.nextDouble() < 0.5) {
            critReady = true;
            return name + " takes aim... Critical hit ready for next attack!";
        } else {
            return name + " focuses but fails to find an opening.";
        }
    }

    @Override
    public String getJobName() { return "Archer"; }

    @Override
    public int[] getLevelUpGains() {
        return new int[]{8, 4, 3}; // HP, Attack, Defense
    }

    @Override
    public String getStatus() {
        String status = "Archer: " + name + " Lv." + level + "\n";
        status += "HP: " + hp + "/" + maxHp + "\n";
        status += "ATK: " + attack + " | DEF: " + defense;
        if (critReady) {
            status += " [Crit Ready!]";
        }
        return status;
    }

    @Override
    public String toString() {
        return "Archer: " + name + " Lv." + level;
    }
}
