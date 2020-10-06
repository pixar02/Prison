package tech.mcprison.prison.shops.data;

import tech.mcprison.prison.mines.data.Block;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.util.Location;

import java.util.List;
import java.util.Map;

public class ShopData {

    private String name;
    private String description;
    private Mine mine;

    private List<Location> signLocations;
    private List<Location> npcLocations;

    private List<Block> blocks;
    private Map<Block, Integer> pricing;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Mine getMine() {
        return mine;
    }

    public void setMine(Mine mine) {
        this.mine = mine;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    /**
     * Add a block to the Shop
     *
     * @param block the block to add
     * @param price the price of that block
     */
    public void addBlock(Block block, int price){
        this.blocks.add(block);
        this.pricing.put(block, price);
    }

    public Map<Block, Integer> getPricing() {
        return pricing;
    }

    public List<Location> getSignLocations() {
        return signLocations;
    }

    public void setSignLocations(List<Location> signLocations) {
        this.signLocations = signLocations;
    }

    public List<Location> getNpcLocations() {
        return npcLocations;
    }

    public void setNpcLocations(List<Location> npcLocations) {
        this.npcLocations = npcLocations;
    }
}
