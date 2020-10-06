package tech.mcprison.prison.shops.gui;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.gui.Button;
import tech.mcprison.prison.gui.GUI;
import tech.mcprison.prison.shops.PrisonShops;
import tech.mcprison.prison.shops.data.Shop;

public class ViewGUI {

    private GUI gui;
    //private File?Doc?

    /**
     * Shows all the shops that are on the server
     */
    public ViewGUI() {
        int rows = PrisonShops.getInstance().getShopManager().getShops().size()/9;

        this.gui = Prison.get().getPlatform().createGUI("&3Shop list", rows);
        for(Shop shop :PrisonShops.getInstance().getShopManager().getShops()){
            Button button = new Button();
            gui.addButton(PrisonShops.getInstance().getShopManager().getShops().indexOf(shop), button);
        }
    }

    public void open(){

    }
}
