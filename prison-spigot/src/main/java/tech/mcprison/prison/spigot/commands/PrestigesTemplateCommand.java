package tech.mcprison.prison.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.spigot.SpigotPrison;

import static org.bukkit.Bukkit.getServer;

public class PrestigesTemplateCommand {

    public static boolean onCommand(CommandSender sender) {

        // Check the permission
        if (!(sender.hasPermission("prison.admin"))) {
            sender.sendMessage(SpigotPrison.format("&cSorry, but you don't have the permission &1[&c-Prison.admin&1]"));
            return true;
        }

        // Get the ladderManager
        LadderManager lm = PrisonRanks.getInstance().getLadderManager();

        if (!(PrisonRanks.getInstance().getLadderManager().getLadder("default").isPresent())){
            sender.sendMessage(SpigotPrison.format("&cThere aren't ranks in the default ladder"));
            return true;
        }

        if (!(lm.getLadder("default").get().getLowestRank().isPresent())){
            sender.sendMessage(SpigotPrison.format("&cSorry, but looks like there aren't ranks in the -&3default&c- ladder, or this's an error"));
            return true;
        }

        String rankName;
        int x = 0;
        try {
            while (lm.getLadder("default").get().getNext(x).isPresent()){
                x++;
            }
        } catch (Exception ex3){
            x--;
        }

        String commandAdd = null;
        String commandRemove = null;

        if (getServer().getPluginManager().getPlugin("LuckPerms") != null){
            commandAdd = "lp user {player} permission set ranks.rankup.prestiges true";
            commandRemove = "lp user {player} permission set ranks.rankup.prestiges false";
        } else if (getServer().getPluginManager().getPlugin("UltraPermissions") != null){
            commandAdd = "upc addPlayerPermission {player} ranks.rankup.prestiges";
            commandRemove = "upc removePlayerPermission {player} ranks.rankup.prestiges";
        } else if (getServer().getPluginManager().getPlugin("PermissionsEx") != null){
            commandAdd = "pex user {player} add ranks.rankup.prestiges";
            commandRemove = "pex user {player} remove ranks.rankup.prestiges";
        }

        if (commandAdd == null || commandRemove == null){
            sender.sendMessage(SpigotPrison.format("&cError: can't find a supported permissions manager."));
            return true;
        }

        // Check if there's a default ladder
        if (!(lm.getLadder("default").isPresent())){
            sender.sendMessage(SpigotPrison.format("&cSorry, but looks like there isn't the -&3default&c- ladder"));
            return true;
        }

        // Check if there's the last rank
        if (!(lm.getLadder("default").get().getByPosition(x).isPresent())){
            sender.sendMessage(SpigotPrison.format("&cSorry, but looks like there aren't ranks in the -&3default&c- ladder, or this's an error"));
            return true;
        }

        // Get the highest rank of the ladder
        rankName = lm.getLadder("default").get().getByPosition(x).get().name;
        // Make a prestiges ladder
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ranks ladder create prestiges");

        // Check if there's a prestige ladder
        if (!(lm.getLadder("prestiges").isPresent())){
            sender.sendMessage(SpigotPrison.format("&cSorry, but the -&3prestiges&c- ladder hasn't been made with success."));
            return true;
        }

        String rankNamePrestige = "+";

        // Tell the sender the last rank name of the default ladder
        sender.sendMessage(SpigotPrison.format("&aThe last rank's " + rankName));

        // Add to the prestige ladder a prestige rank
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), SpigotPrison.format("ranks create " + rankNamePrestige + " 1000 prestiges &8[&c&l+&8]&f"));

        // Add a rankup command to the last rank of the default ladder, the one to add the permission to prestige
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ranks command add " + rankName + " " + commandAdd);

        // Get the lowest rank of the default ladder
        rankName = lm.getLadder("default").get().getLowestRank().get().name;

        // Add the rankupCommand of the prestige ladder to set the player rank to the first of the default one
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ranks command add " + rankNamePrestige + " ranks set rank {player} " + rankName + " default");

        // Add to the prestige the command to remove the permission to prestige to the player
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ranks command add " + rankNamePrestige + " " + commandRemove);

        // Tell the sender the first rank name
        sender.sendMessage(SpigotPrison.format("&aThe first rank's " + rankName));

        // Add to the lowest rank the command to remove to the player the permission to prestige
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ranks command add " + rankName + " " + commandRemove);

        // Success
        sender.sendMessage(SpigotPrison.format("&aLadder -prestiges- has been created with success, &c&lDON'T RENAME IT &cor prestiges won't work, &ayou can rename the ranks inside the ladder and add new, they'll work as prestiges."));

        return true;
    }
}
