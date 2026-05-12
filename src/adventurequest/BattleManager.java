package adventurequest;

public class BattleManager {
    private final Combatable player;
    private final Player playerObj;
    private Enemy enemy;
    private final StringBuilder log;

    public BattleManager(Player playerObj, Enemy enemy) {
        this.player = playerObj.getCharacter();
        this.playerObj = playerObj;
        this.enemy = enemy;
        this.log = new StringBuilder();
    }

    public String getEnemyName() { return enemy.getName(); }
    public int getEnemyHp() { return enemy.getHp(); }
    public int getEnemyMaxHp() { return enemy.getMaxHp(); }
    public Enemy getEnemy() { return enemy; }
    public void setEnemy(Enemy enemy) { this.enemy = enemy; }

    public BattleResult executeTurn(String action) {
        log.setLength(0);
        boolean victory = false;
        boolean playerDead = false;
        int xpGained = 0;
        int goldGained = 0;
        Item droppedItem = null;

        switch (action) {
            case "attack":
                handleAttack();
                break;
            case "skill":
                handleSkill();
                break;
            case "defend":
                handleDefend();
                break;
            default:
                log.append("Unknown action.\n");
        }

        if (!enemy.isAlive()) {
            victory = true;
            xpGained = enemy.getXpReward();
            goldGained = enemy.getGoldReward();
            droppedItem = enemy.getRandomDrop();
            log.append(String.format("\n*** %s defeated! ***\n", enemy.getName()));
            log.append(String.format("+%d XP, +%d Gold\n", xpGained, goldGained));
            if (droppedItem != null) {
                log.append(String.format("Item dropped: %s!\n", droppedItem.getName()));
            }
        }

        if (!player.isAlive()) {
            playerDead = true;
            log.append("\n*** You have been defeated! ***\n");
        }

        return new BattleResult(victory, playerDead, xpGained, goldGained, droppedItem, log.toString());
    }

    private void handleAttack() {
        // Player attacks
        int dmg = player.attack(enemy);
        log.append(String.format("You attack %s for %d damage!\n", enemy.getName(), dmg));

        // Enemy counterattacks if alive
        if (enemy.isAlive()) {
            int enemyRawDmg = enemy.getAttackPower();
            int enemyDmg = Math.max(1, enemyRawDmg - player.defend());
            player.takeDamage(enemyDmg);
            log.append(String.format("%s attacks you for %d damage!\n", enemy.getName(), enemyDmg));
        }
    }

    private void handleSkill() {
        String skillMsg = player.useSkill();
        log.append(skillMsg).append("\n");

        // Handle skill effects based on job type
        if (player instanceof Warrior) {
            int dmg = player.attack(enemy);
            log.append(String.format("You attack %s for %d damage!\n", enemy.getName(), dmg));
        } else if (player instanceof Mage) {
            int dmg = ((Mage) player).useSkillAttack(enemy);
            log.append(String.format("Fireball hits %s for %d damage!\n", enemy.getName(), dmg));
        } else if (player instanceof Archer) {
            int dmg = player.attack(enemy);
            log.append(String.format("You attack %s for %d damage!\n", enemy.getName(), dmg));
        }

        // Enemy counterattacks if alive
        if (enemy.isAlive()) {
            int enemyRawDmg = enemy.getAttackPower();
            int enemyDmg = Math.max(1, enemyRawDmg - player.defend());
            player.takeDamage(enemyDmg);
            log.append(String.format("%s attacks you for %d damage!\n", enemy.getName(), enemyDmg));
        }
    }

    private void handleDefend() {
        int defAmount = player.defend();
        log.append(String.format("You brace for impact! Defense: %d\n", defAmount));

        // Enemy attacks with reduced damage
        if (enemy.isAlive()) {
            int enemyRawDmg = enemy.getAttackPower();
            int enemyDmg = Math.max(1, enemyRawDmg - defAmount * 2);
            player.takeDamage(enemyDmg);
            log.append(String.format("%s attacks you for %d damage! (reduced by defend)\n",
                    enemy.getName(), enemyDmg));
        }
    }

    public BattleResult useItem(Item item) {
        log.setLength(0);

        if (item.getType() == ItemType.POTION) {
            playerObj.heal(item.getStatBonus());
            playerObj.getInventory().removeItem(item.getName(), 1);
            log.append(String.format("You use %s and recover %d HP!\n",
                    item.getName(), item.getStatBonus()));
        } else {
            log.append("That item cannot be used in battle.\n");
        }

        // Enemy still gets to attack
        if (enemy.isAlive()) {
            int enemyRawDmg = enemy.getAttackPower();
            int enemyDmg = Math.max(1, enemyRawDmg - player.defend());
            player.takeDamage(enemyDmg);
            log.append(String.format("%s attacks you for %d damage!\n", enemy.getName(), enemyDmg));
        }

        boolean playerDead = !player.isAlive();
        return new BattleResult(false, playerDead, 0, 0, null, log.toString());
    }
}
