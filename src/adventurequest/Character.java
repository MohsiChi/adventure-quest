package adventurequest;

public abstract class Character implements Combatable {
    protected String name;
    protected int level;
    protected int hp;
    protected int maxHp;
    protected int attack;
    protected int defense;
    protected int xp;
    protected int gold;

    public Character(String name, int level, int hp, int attack, int defense) {
        this.name = name;
        this.level = level;
        this.maxHp = hp;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.xp = 0;
        this.gold = 0;
    }

    @Override
    public void takeDamage(int dmg) {
        int actualDmg = Math.max(0, dmg);
        hp = Math.max(0, hp - actualDmg);
    }

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }

    @Override
    public boolean isAlive() {
        return hp > 0;
    }

    @Override
    public String getName() { return name; }

    @Override
    public int getHp() { return hp; }

    @Override
    public int getMaxHp() { return maxHp; }

    public int getLevel() { return level; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public int getXp() { return xp; }
    public int getGold() { return gold; }

    public void setGold(int gold) { this.gold = gold; }
    public void addGold(int amount) { this.gold += amount; }
    public boolean spendGold(int amount) { if (gold >= amount) { gold -= amount; return true; } return false; }

    public void addXp(int amount) { this.xp += amount; }
    public void setLevel(int level) { this.level = level; }
    public void setMaxHp(int maxHp) { this.maxHp = maxHp; }
    public void setHp(int hp) { this.hp = hp; }
    public void setAttack(int attack) { this.attack = attack; }
    public void setDefense(int defense) { this.defense = defense; }

    public abstract String getJobName();
    public abstract int[] getLevelUpGains();

    @Override
    public abstract String useSkill();

    @Override
    public abstract String getStatus();

    @Override
    public abstract String toString();
}
