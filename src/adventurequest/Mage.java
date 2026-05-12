package adventurequest;

public class Mage extends Character {
    public Mage(String name) {
        super(name, 1, 80, 18, 8);
    }

    @Override
    public int attack(Enemy e) {
        int damage = Math.max(1, attack - e.defend());
        e.takeDamage(damage);
        return damage;
    }

    @Override
    public int defend() {
        return defense;
    }

    @Override
    public String useSkill() {
        return name + " casts Fireball! Deals massive AoE damage bypassing 50% armor.";
    }

    public int useSkillAttack(Enemy e) {
        int bypass = e.defend() / 2;
        int damage = Math.max(1, (attack * 2) - bypass);
        e.takeDamage(damage);
        return damage;
    }

    @Override
    public String getJobName() { return "Mage"; }

    @Override
    public int[] getLevelUpGains() {
        return new int[]{6, 6, 2}; // HP, Attack, Defense
    }

    @Override
    public String getStatus() {
        return "Mage: " + name + " Lv." + level + "\n" +
               "HP: " + hp + "/" + maxHp + "\n" +
               "ATK: " + attack + " | DEF: " + defense;
    }

    @Override
    public String toString() {
        return "Mage: " + name + " Lv." + level;
    }
}
