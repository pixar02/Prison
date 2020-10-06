package tech.mcprison.prison.shops.gui;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.gui.Action;
import tech.mcprison.prison.gui.Button;
import tech.mcprison.prison.gui.GUI;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.mines.data.Block;
import tech.mcprison.prison.shops.data.Shop;
import tech.mcprison.prison.util.BlockType;

import java.util.HashMap;
import java.util.Map;

public class ShopGui {

    private final Shop shop;
    private GUI gui;
    private Map<Integer, ShopGui> pages;

    /**
     * Default constructor
     *
     * @param shop shop for the gui
     */
    public ShopGui(Shop shop) {
        this.shop = shop;
        pages = new HashMap<>();
        pages.put(0, this);
        this.gui = Prison.get().getPlatform().createGUI(shop.getName(), generateRows());

        //Add the sellable blocks
        for (Block block : shop.getBlocks()){
            gui.getInventory().addItem(new ItemStack(1, block.getType(), generateLore(block)));
        }

        if (pages.size() > 1){
            Action back = button -> {
                //Show the previous page
            };

            for(int key : pages.keySet()){
                if (key != 0 && pages.get(key) == this){
                    //begin page (no previous button)
                    Button button = new Button(BlockType.getBlock("REDSTONE_BLOCK"), back,
                            "Previous page",false, shop.getDescription());
                    gui.addButton(46, button);
                }
            }

            Action next = button -> {
                //Show the next page

            };
            Button button = new Button(BlockType.getBlock("EMERALD_BLOCK"), next,
                    "Next page", false, shop.getDescription());
            gui.addButton(53, button);

        }
        //Close gui button (escape also works)
        Action action = button -> {};
        Button button = new Button(BlockType.getBlock("BARRIER"), action, "Close shop",
                true, shop.getDescription());
        gui.addButton(50, button);
    }

    /**
     * Constructor in case the the shop has more then 45 blocks
     *
     * @param shop Shop for the gui
     * @param pages all the pages for this shop
     */
    public ShopGui(Shop shop, Map<Integer, ShopGui> pages){
        this.shop = shop;
        this.pages = pages;
        this.pages.put(pages.size()+1, this);
        this.gui = Prison.get().getPlatform().createGUI(shop.getName(), generateRows());
    }

    public void open(Player... players){

    }

    /**
     * Generate the Lore for a block
     * @param block block that needs Lore
     * @return String[] contains pricing for the block
     */
    private String[] generateLore(Block block){
        String[] lore = {
                block.getType().getXMaterialName() + ": "+ shop.getPricing().get(block),
        };
        return lore;
    }

    /**
     * Calculate the amount of row required
     * Generate a new page if necessary
     *
     * @return Integer amount of rows
     */
    private Integer generateRows(){
        if (shop.getBlocks().size() >= 45){
            //We need a second page
            this.pages.put(pages.size(), new ShopGui(shop, pages));
            return 6;
        } else {
            return shop.getBlocks().size()/9;
        }
    }
}
