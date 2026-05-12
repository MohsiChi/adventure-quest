package adventurequest;

public class InsufficientGoldException extends Exception {
    private final int required;
    private final int available;

    public InsufficientGoldException(int required, int available) {
        super("Insufficient gold! Required: " + required + ", Available: " + available);
        this.required = required;
        this.available = available;
    }

    public int getRequired() { return required; }
    public int getAvailable() { return available; }
    public int getShortfall() { return required - available; }
}
