package adventurequest;

public class Item {
    private String name;
    private ItemType type;
    private int value;
    private int statBonus;

    public Item(String name, ItemType type, int value, int statBonus) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.statBonus = statBonus;
    }

    public String getName() { return name; }
    public ItemType getType() { return type; }
    public int getValue() { return value; }
    public int getStatBonus() { return statBonus; }

    @Override
    public String toString() {
        return name + " (" + type.getDisplayName() + ") - " + value + " gold";
    }
}
