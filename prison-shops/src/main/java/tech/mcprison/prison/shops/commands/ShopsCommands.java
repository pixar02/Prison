package tech.mcprison.prison.shops.commands;

import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;

public class ShopsCommands {


    @Command(identifier = "shops create", description = "Creates a new shop.",
            onlyPlayers = false, permissions = "shops.create")
    public void createShop(CommandSender sender,
           @Arg(name = "shopName", description = "The name of the new shop.", def = " ") String mineName,
           @Arg(name = "rank", description = "The rank for this shop", def = " ") String rank){

    }





    @Command(identifier = "shops generate", description = "Generate for (all) mines a shop (without pricing).",
    onlyPlayers = false, permissions = "shops.create")
    public void generateShops(CommandSender sender,
            @Arg(name = "mine",description = "Name of the mine or all for all", def = "all") String name){

        if (name == null || name.isEmpty() || name.contains(" ")){
            sender.sendMessage( "&3Names cannot contain spaces or be empty. &b[&d" + name + "&b]" );
            return;
        }
        name = name.trim();

        if (name.equalsIgnoreCase("all")){
            //Generate all shops that don't exist (per mine)
            for (Mine mine : PrisonMines.getInstance().getMines()){

            }
        }else{
            Mine mine = PrisonMines.getInstance().getMineManager().getMine(name);
            if (mine == null){
                sender.sendMessage("&3Mine: " + name + " does not exist.");
                return;
            }

        }
    }
}
