package tech.mcprison.prison.spigot.gui.rank;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.ListenersPrisonManager;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SpigotRanksGUI extends SpigotGUIComponents {

    private final Player p;
    private final Optional<RankLadder> ladder;

    public SpigotRanksGUI(Player p, Optional<RankLadder> ladder) {
        this.p = p;
        this.ladder = ladder;
    }

    public void open() {

        // Init the ItemStack
        // ItemStack itemRank;

        int dimension = 27;

        // Check if Ranks are enabled
        if (!(checkRanks(p))){
            return;
        }

        // Load config
        Configuration GuiConfig = SpigotPrison.getGuiConfig();

        // Get the dimensions and if needed increases them
        if (ladder.isPresent() && !(ladder.get().ranks.size() == 0)) {
            dimension = (int) Math.ceil(ladder.get().ranks.size() / 9D) * 9;
        } else {
            p.sendMessage(SpigotPrison.format(GuiConfig.getString("Gui.Message.NoRanksFoundAdmin")));
            return;
        }

        // If the inventory is empty
        if (dimension == 0){
            p.sendMessage(SpigotPrison.format(GuiConfig.getString("Gui.Message.EmptyGui")));
            p.closeInventory();
            return;
        }

        // If the dimension's too big, don't open the GUI
        if (dimension > 54){
            p.sendMessage(SpigotPrison.format(GuiConfig.getString("Gui.Message.TooManyRanks")));
            p.closeInventory();
            return;
        }

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3" + "Ladders -> Ranks"));

        // For every rank make a button
        for (RankLadder.PositionRank pos : ladder.get().ranks) {
            Optional<Rank> rankOptional = ladder.get().getByPosition(pos.getPosition());

            // Well... check if the rank is null probably
            if (!rankOptional.isPresent()) {
                continue; // Skip it
            }

            if (guiBuilder(GuiConfig, inv, rankOptional)) return;

        }

        // Open the inventory
        this.p.openInventory(inv);
        ListenersPrisonManager.get().addToGUIBlocker(p);
    }

    private boolean guiBuilder(Configuration guiConfig, Inventory inv, Optional<Rank> rankOptional) {
        try {
            buttonsSetup(guiConfig, inv, rankOptional);
        } catch (NullPointerException ex){
            p.sendMessage(SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private void buttonsSetup(Configuration guiConfig, Inventory inv, Optional<Rank> rankOptional) {
        ItemStack itemRank;
        // Init the lore array with default values for ladders
        List<String> ranksLore = createLore(
                guiConfig.getString("Gui.Lore.ShiftAndRightClickToDelete"),
                guiConfig.getString("Gui.Lore.ClickToManageRank"),
                "",
                guiConfig.getString("Gui.Lore.Info"));

        if (!rankOptional.isPresent()){
            p.sendMessage(SpigotPrison.format(guiConfig.getString("Gui.Message.CantGetRanksAdmin")));
            return;
        }

        // Get the specific rank
        Rank rank = rankOptional.get();

        // Add the RankID Lore
        ranksLore.add(SpigotPrison.format(guiConfig.getString("Gui.Lore.Id") + rank.id));

        // Add the RankName lore
        ranksLore.add(SpigotPrison.format(guiConfig.getString("Gui.Lore.Name") + rank.name));

        // Add the Rank Tag lore
        ranksLore.add(SpigotPrison.format(guiConfig.getString("Gui.Lore.Tag2") + ChatColor.translateAlternateColorCodes('&', rank.tag)));

        // Add the Price lore
        ranksLore.add(SpigotPrison.format(guiConfig.getString("Gui.Lore.Price3") + rank.cost));

        // Init a variable
        List<RankPlayer> players =
                PrisonRanks.getInstance().getPlayerManager().getPlayers().stream()
                        .filter(rankPlayer -> rankPlayer.getRanks().containsValue(rankOptional.get()))
                        .collect(Collectors.toList());

        // Add the number of players with this rank
        ranksLore.add(SpigotPrison.format(guiConfig.getString("Gui.Lore.PlayersWithTheRank") + players.size()));

        // RankUpCommands info lore
        ranksLore.add("");
        getCommands(ranksLore, rank);

        // Make the button with materials, amount, lore and name
        itemRank = createButton(Material.TRIPWIRE_HOOK, 1, ranksLore, SpigotPrison.format("&3" + rank.name));

        // Add the button to the inventory
        inv.addItem(itemRank);
    }

    static void getCommands(List<String> ranksLore, Rank rank) {

        Configuration GuiConfig = SpigotPrison.getGuiConfig();

        if (rank.rankUpCommands == null || rank.rankUpCommands.size() == 0) {
            ranksLore.add(SpigotPrison.format(GuiConfig.getString("Gui.Lore.ContainsTheRank") + rank.name + GuiConfig.getString("Gui.Lore.ContainsNoCommands")));
        } else {
            ranksLore.add(SpigotPrison.format(GuiConfig.getString("Gui.Lore.LadderThereAre") + rank.rankUpCommands.size() + GuiConfig.getString("Gui.Lore.LadderCommands")));
            for (String command : rank.rankUpCommands) {
                ranksLore.add(SpigotPrison.format(GuiConfig.getString("Gui.Lore.RankupCommands") + command));
            }
            ranksLore.add(SpigotPrison.format(GuiConfig.getString("Gui.Lore.ClickToManageCommands")));
        }
    }
}
