package tech.mcprison.prison.spigot.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.spigot.SpigotPrison;

/**
 * @author rbluer RoyalBlueRanger
 * @author GABRYCA
 */
public abstract class SpigotGUIComponents {

    // createButton method (create a button for the GUI - item)
    protected ItemStack createButton(Material id, int amount, List<String> lore, String display) {

        ItemStack item = new ItemStack(id, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(display);
        try {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } catch (NoClassDefFoundError ignored){}
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    // createLore method (create a lore for the button)
    protected List<String> createLore( String... lores ) {
        List<String> results = new ArrayList<>();
        for ( String lore : lores ) {
            results.add( SpigotPrison.format(lore) );
        }
        return results;
    }

    // checkRanks method (check if the ranks module's enabled with success or disabled)
    protected boolean checkRanks(Player p){
        Module module = Prison.get().getModuleManager().getModule( PrisonRanks.MODULE_NAME ).orElse( null );
        if(!(module instanceof PrisonRanks)){
            p.sendMessage(SpigotPrison.format("&c[ERROR] The GUI can't open because the &3Ranks module &cisn't loaded"));
            p.closeInventory();
        }
        return module instanceof PrisonRanks;
    }


}
