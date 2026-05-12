package adventurequest;

import java.util.ArrayList;
import java.util.List;

public class Shop {
    private final List<Item> shopItems;

    public Shop() {
        shopItems = new ArrayList<>();
        // Shop sells select items
        shopItems.add(GameData.getItemByName("Wooden Sword"));
        shopItems.add(GameData.getItemByName("Iron Sword"));
        shopItems.add(GameData.getItemByName("Leather Armor"));
        shopItems.add(GameData.getItemByName("Chain Mail"));
        shopItems.add(GameData.getItemByName("Health Potion"));
        shopItems.add(GameData.getItemByName("Greater Potion"));
        shopItems.add(GameData.getItemByName("Mage Staff"));
        shopItems.add(GameData.getItemByName("Dragon Scale"));
    }

    public List<Item> getShopItems() {
        return new ArrayList<>(shopItems);
    }

    public void buyItem(Player player, Item item) throws InsufficientGoldException {
        if (player.getGold() < item.getValue()) {
            throw new InsufficientGoldException(item.getValue(), player.getGold());
        }
        player.spendGold(item.getValue());
        player.getInventory().addItem(item);
    }
}
