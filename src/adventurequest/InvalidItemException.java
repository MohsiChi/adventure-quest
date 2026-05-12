package adventurequest;

public class InvalidItemException extends Exception {
    public InvalidItemException(String itemName) {
        super("Invalid item: " + itemName);
    }

    public InvalidItemException(String itemName, String reason) {
        super("Invalid item '" + itemName + "': " + reason);
    }
}
