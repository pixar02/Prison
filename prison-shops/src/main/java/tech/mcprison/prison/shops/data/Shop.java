package tech.mcprison.prison.shops.data;

import tech.mcprison.prison.shops.ShopException;
import tech.mcprison.prison.store.Document;

public class Shop extends ShopData{

    public Shop(){

    }

    public Shop(String name){
        setName(name);
    }

    public Shop(Document document) throws ShopException {

    }

    public Document toDocument(){
        return null;
    }
}
