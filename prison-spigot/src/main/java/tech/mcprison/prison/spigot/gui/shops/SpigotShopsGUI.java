package tech.mcprison.prison.spigot.gui.shops;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import tech.mcprison.prison.shops.data.PrisonSortableShops;
import tech.mcprison.prison.shops.data.Shop;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.ListenersPrisonManager;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

import java.util.Set;

public class SpigotShopsGUI extends SpigotGUIComponents {

    private final Player player;

    public SpigotShopsGUI(Player player) {
        this.player = player;
    }

    public void open(){

        Set<Shop>shops = new PrisonSortableShops().getSortedSet();

        int dimension = (int) Math.ceil(shops.size() / 9D) * 9;


        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3ShopsManager -> Shops"));

        for (Shop shop : shops){

        }


        // Open the inventory
        this.player.openInventory(inv);
        ListenersPrisonManager.get().addToGUIBlocker(player);
    }
}
