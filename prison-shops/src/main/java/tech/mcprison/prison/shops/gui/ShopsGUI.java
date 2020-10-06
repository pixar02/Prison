package tech.mcprison.prison.shops.gui;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.gui.Action;
import tech.mcprison.prison.gui.Button;
import tech.mcprison.prison.gui.GUI;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.util.BlockType;

public class ShopsGUI {


    private GUI gui;

    /**
     * Master GUI
     *
     * create a shop
     * view shops (List of shops)
     * control shops (edit/ add blocks / delete)
     */
    public ShopsGUI() {
        gui = Prison.get().getPlatform().createGUI("&3Prison ShopManager", 3);
        //gui.getInventory().addItem(new ItemStack(1, BlockType.getBlock("DIMOND_BLOCK"), ""));

        Action action = button -> {

        };

        Button createB = new Button(BlockType.MINECART, action, "Create a shop", false);
        Button viewB = new Button(BlockType.BOOK, action, "View Shops", false);
        Button controlB = new Button(BlockType.BOOK_AND_QUILL, action, "Control Shops", false);

        gui.addButton(11, createB);
        gui.addButton(13, viewB);
        gui.addButton(15, controlB);
    }


    public void open(Player... players){
        for (Player player : players){
            PrisonAPI.getPlayer(player.getName());


        }
    }
}
