package tech.mcprison.prison.shops.gui;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.gui.GUI;
import tech.mcprison.prison.internal.Player;

public class ShopsGUI {


    private GUI gui;

    /**
     * Master GUI
     *
     * create a shop
     * view shops
     *
     */
    public ShopsGUI() {
        gui = Prison.get().getPlatform().createGUI("&3Prison ShopManager", 3);

    }


    public void show(Player... players){
        for (Player player : players){

            PrisonAPI.getPlayer(player.getName());
        }
    }


    public void open(){

    }
}
