package adventurequest;

public interface Combatable {
    int attack(Enemy e);
    int defend();
    String useSkill();
    String getStatus();
    boolean isAlive();
    String getName();
    int getHp();
    int getMaxHp();
    void takeDamage(int dmg);
}
