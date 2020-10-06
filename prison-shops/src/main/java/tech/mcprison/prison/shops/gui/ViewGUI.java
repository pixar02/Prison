package tech.mcprison.prison.shops.gui;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.gui.Action;
import tech.mcprison.prison.gui.Button;
import tech.mcprison.prison.gui.GUI;
import tech.mcprison.prison.shops.PrisonShops;
import tech.mcprison.prison.shops.data.Shop;
import tech.mcprison.prison.util.BlockType;

public class ViewGUI {

    private GUI gui;
    //private File?Doc?

    /**
     * Shows all the shops that are on the server
     */
    public ViewGUI() {
        int rows = PrisonShops.getInstance().getShopManager().getShops().size()/9;

        this.gui = Prison.get().getPlatform().createGUI("&3Shop list", rows);
        for(Shop shop : PrisonShops.getInstance().getShopManager().getShops()){
            Action action = button -> {
                //Open the specifc mine's gui
                ShopGui shopGui = new ShopGui(shop);
                shopGui.open(button.getPlayer());
            };

            Button button = new Button(BlockType.getBlock("MINECART_WITH_CHEST"), action, shop.getName(),
                    false, shop.getDescription());
            gui.addButton(PrisonShops.getInstance().getShopManager().getShops().indexOf(shop), button);
        }
    }

    public void open(){

    }
}
