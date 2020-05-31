package tech.mcprison.prison.spigot.commands;

import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tech.mcprison.prison.spigot.SpigotPrison;

public class PrestigesCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Check parameters
        if (args.length == 0) {

            if (sender.hasPermission("prison.admin")) {
                sender.sendMessage(SpigotPrison.format("&8---------------------------"));
                sender.sendMessage(SpigotPrison.format("     &3Prestiges commands:"));
                sender.sendMessage(SpigotPrison.format("&8---------------------------"));
                sender.sendMessage(SpigotPrison.format("&3/prestige  &8&l| &3Prestige, permission prison.prestige."));
                sender.sendMessage(SpigotPrison.format("&3/prestiges  &8&l| &3Get a list of prestiges commands."));
                sender.sendMessage(SpigotPrison.format("&3/prestiges template"));
                sender.sendMessage(SpigotPrison.format("&8&l| &3Enable hacky prestiges."));

            } else {
                sender.sendMessage(SpigotPrison.format("&8---------------------------"));
                sender.sendMessage(SpigotPrison.format("     &3Prestiges commands:"));
                sender.sendMessage(SpigotPrison.format("&8---------------------------"));
                sender.sendMessage(SpigotPrison.format("&3/prestige     &8&l| &3Prestige, you can execute it if you're at the last rank and there're prestiges in the server."));
            }

            return true;
        }

        String param = args[0];
        int NotNull = 0;
        while (NotNull != args.length - 1){
            args[NotNull] = args[NotNull + 1];
            NotNull++;
        }

        args = (String[]) ArrayUtils.removeElement(args, args[NotNull]);

        if (param.equalsIgnoreCase("template")){
            return PrestigesTemplateCommand.onCommand(sender);
        }

        return true;
    }
}
