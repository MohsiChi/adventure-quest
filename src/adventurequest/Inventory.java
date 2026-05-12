package adventurequest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Inventory {
    private final List<InventoryEntry> entries;

    public Inventory() {
        this.entries = new ArrayList<>();
    }

    public void addItem(Item i) {
        for (InventoryEntry entry : entries) {
            if (entry.item.getName().equalsIgnoreCase(i.getName())) {
                entry.quantity++;
                return;
            }
        }
        entries.add(new InventoryEntry(i, 1));
    }

    public void addItem(String name, int quantity) throws InvalidItemException {
        if (quantity <= 0) {
            throw new InvalidItemException(name, "Quantity must be positive, got: " + quantity);
        }
        Item item = GameData.getItemByName(name);
        if (item == null) {
            throw new InvalidItemException(name, "Item not found in game data");
        }
        for (InventoryEntry entry : entries) {
            if (entry.item.getName().equalsIgnoreCase(name)) {
                entry.quantity += quantity;
                return;
            }
        }
        entries.add(new InventoryEntry(item, quantity));
    }

    public boolean removeItem(String name, int quantity) {
        for (int i = 0; i < entries.size(); i++) {
            InventoryEntry entry = entries.get(i);
            if (entry.item.getName().equalsIgnoreCase(name)) {
                if (entry.quantity >= quantity) {
                    entry.quantity -= quantity;
                    if (entry.quantity <= 0) {
                        entries.remove(i);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasItem(String name) {
        for (InventoryEntry entry : entries) {
            if (entry.item.getName().equalsIgnoreCase(name) && entry.quantity > 0) {
                return true;
            }
        }
        return false;
    }

    public Item getItem(String name) {
        for (InventoryEntry entry : entries) {
            if (entry.item.getName().equalsIgnoreCase(name) && entry.quantity > 0) {
                return entry.item;
            }
        }
        return null;
    }

    public List<InventoryEntry> getEntries() {
        return new ArrayList<>(entries);
    }

    public List<InventoryEntry> search(String keyword) {
        String lower = keyword.toLowerCase();
        return entries.stream()
                .filter(e -> e.item.getName().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    public List<InventoryEntry> sortByValue(boolean ascending) {
        List<InventoryEntry> sorted = new ArrayList<>(entries);
        if (ascending) {
            sorted.sort(Comparator.comparingInt(e -> e.item.getValue()));
        } else {
            sorted.sort((a, b) -> Integer.compare(b.item.getValue(), a.item.getValue()));
        }
        return sorted;
    }

    public void clear() {
        entries.clear();
    }

    @Override
    public String toString() {
        if (entries.isEmpty()) return "Empty";
        StringBuilder sb = new StringBuilder();
        for (InventoryEntry e : entries) {
            sb.append(e.item.getName()).append(" x").append(e.quantity).append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    public static class InventoryEntry {
        public final Item item;
        public int quantity;

        public InventoryEntry(Item item, int quantity) {
            this.item = item;
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return item.getName() + " x" + quantity + " (" + item.getValue() + " gold each)";
        }
    }
}
