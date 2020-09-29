package tech.mcprison.prison.shops.managers;

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.shops.PrisonShops;
import tech.mcprison.prison.shops.data.Shop;
import tech.mcprison.prison.store.Collection;
import tech.mcprison.prison.store.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

public class ShopManager {

    private List<Shop> shops;
    private TreeMap<String, Shop> shopsByName;

    private Collection coll;


    public ShopManager(){
        this.shops = new ArrayList<>();
        shopsByName = new TreeMap<>();

        this.coll =  null;
    }

    public void loadFromDbCollection(PrisonShops pShops) {
        Optional<Collection> collOptional = pShops.getDb().getCollection("shops");

        if (!collOptional.isPresent()) {
            Output.get().logError("Could not create 'shops' collection.");
            pShops.getStatus().toFailed("Could not create mines collection in storage.");
            return;
        }
        this.coll = collOptional.get();

        loadShops();

        //Output.get().logInfo( String.format("Loaded %d mines and submitted with a %d " +
                 //       "second offset timing for auto resets.",
                //getShops().size()));
    }

    private void loadShops(){
        List<Document> shopDocuments = coll.getAll();

        for (Document document : shopDocuments) {
            try {
                Shop shop = new Shop(document);
                add(shop, false);
            }catch (Exception ex){
                Output.get()
                        .logError("&cFailed to load shop " + document.getOrDefault("name", "null"), ex);
            }
        }
    }

    private boolean add(Shop shop, boolean save) {
        boolean results = false;
        if (!getShops().contains(shop)){
            if (save) {
                saveShop(shop);
            }

            results = getShops().add(shop);
            getShopsByName().put( shop.getName().toLowerCase(), shop);
        }
        return results;
    }

    public void saveShop(Shop shop) {
        coll.save(shop.toDocument());
    }

    public List<Shop> getShops() {
        return this.shops;
    }

    public TreeMap<String, Shop> getShopsByName() {
        return this.shopsByName;
    }
}
