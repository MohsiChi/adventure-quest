package adventurequest;

public enum ItemType {
    WEAPON("Weapon"),
    ARMOR("Armor"),
    POTION("Potion"),
    MISC("Misc");

    private final String displayName;

    ItemType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
