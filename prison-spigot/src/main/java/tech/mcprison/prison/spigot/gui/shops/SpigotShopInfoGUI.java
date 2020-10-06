package tech.mcprison.prison.spigot.gui.shops;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import tech.mcprison.prison.shops.data.Shop;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

public class SpigotShopInfoGUI extends SpigotGUIComponents {

    private final Player player;
    private final Shop shop;
    private final String shopName;

    public SpigotShopInfoGUI(Player player, Shop shop, String shopName) {
        this.player = player;
        this.shop = shop;
        this.shopName = shopName;
    }

    public void open(){

        Inventory inv = Bukkit.createInventory(null, 9, SpigotPrison.format("&3Shops -> Info"));
    }

    private boolean guibuilder(){

        return false;
    }

    private void buttonSetup(){

    }

}
