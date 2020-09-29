package tech.mcprison.prison.shops.data;

import tech.mcprison.prison.mines.data.Block;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.util.Location;

import java.util.List;

public class ShopData {

    private String name;
    private Mine mine;

    private List<Location> signLocations;
    private List<Location> npcLocations;

    private List<Block> blocks;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Mine getMine() {
        return mine;
    }

    public void setMine(Mine mine) {
        this.mine = mine;
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
