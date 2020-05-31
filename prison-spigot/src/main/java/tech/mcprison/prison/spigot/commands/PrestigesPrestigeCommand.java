package tech.mcprison.prison.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.spigot.SpigotPrison;

public class PrestigesPrestigeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player || sender instanceof tech.mcprison.prison.internal.Player)) {
            sender.sendMessage(SpigotPrison.format("&cFor some reasons, it looks like you aren't a player"));
            return true;
        }


        if (!(PrisonRanks.getInstance().getLadderManager().getLadder("default").isPresent()) ||
                !(PrisonRanks.getInstance().getLadderManager().getLadder("default").get().getLowestRank().isPresent()) ||
                PrisonRanks.getInstance().getLadderManager().getLadder("default").get().getLowestRank().get().name == null){
            sender.sendMessage(SpigotPrison.format("&cThere aren't ranks in the default ladder"));
            return true;
        }

        if (!(PrisonRanks.getInstance().getLadderManager().getLadder("prestiges").isPresent()) ||
                !(PrisonRanks.getInstance().getLadderManager().getLadder("prestiges").get().getLowestRank().isPresent()) ||
                PrisonRanks.getInstance().getLadderManager().getLadder("prestiges").get().getLowestRank().get().name == null){
            sender.sendMessage(SpigotPrison.format("&cThere aren't prestiges in the prestige ladder"));
            return true;
        }

        if (sender.hasPermission("ranks.rankup.prestiges")){
            Bukkit.dispatchCommand(sender, "rankup prestiges");
        }

        return true;
    }
}
