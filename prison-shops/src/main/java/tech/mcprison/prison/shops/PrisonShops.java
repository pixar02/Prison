package tech.mcprison.prison.shops;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.error.ErrorManager;
import tech.mcprison.prison.file.JsonFileIO;
import tech.mcprison.prison.localization.LocaleManager;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.shops.commands.ShopsCommands;
import tech.mcprison.prison.shops.data.ShopsConfig;
import tech.mcprison.prison.shops.managers.ShopManager;
import tech.mcprison.prison.store.Database;

import java.io.File;
import java.util.Optional;

public class PrisonShops extends Module {

    public static final String MODULE_NAME = "Shops";
    private static PrisonShops i = null;

    private ShopsConfig config;
    private ShopManager shopManager;
    private LocaleManager localeManager;
    private Database db;
    private ErrorManager errorManager;

    private JsonFileIO jsonFileIO;


    public PrisonShops(String version) {
        super(MODULE_NAME, version, 3);
    }

    public static PrisonShops getInstance(){
        return i;
    }

    @Override
    public String getBaseCommands() {
        return "&7/&2shops";
    }

    public void enable() {
        i = this;

        this.errorManager = new ErrorManager(this);
        this.jsonFileIO = new JsonFileIO(errorManager, getStatus());

        initDb();
        initConfig();

        this.localeManager = new LocaleManager(this, "lang/shops");


        this.shopManager = new ShopManager();
        this.shopManager.loadFromDbCollection(this);

        Prison.get().getCommandHandler().registerCommands(new ShopsCommands());
    }

    private void initDb(){
        Optional<Database> dbOptional =
                Prison.get().getPlatform().getStorage().getDatabase("shops");

        if (!dbOptional.isPresent()) {

            Prison.get().getPlatform().getStorage().createDatabase("shops");
            dbOptional = Prison.get().getPlatform().getStorage().getDatabase("shops");

            if (!dbOptional.isPresent()) {
                Output.get().logError("Could not load the shops database.");
                getStatus().toFailed("Could not load storage database.");
                return;
            }
        }

        this.db = dbOptional.get();
    }

    private void initConfig() {
        this.config = new ShopsConfig();

        File configFile = new File(getDataFolder(), "config.json");

        if (!configFile.exists()) {
            getJsonFileIO().saveJsonFile(configFile,config);
        } else {
            this.config = (ShopsConfig) getJsonFileIO().readJsonFile(configFile, config);
        }

    }

    public void disable() {
    }

    public LocaleManager getShopsMessages() {
        return this.localeManager;
    }

    public JsonFileIO getJsonFileIO() {
        return this.jsonFileIO;
    }

    public ShopManager getShopManager() {
        return this.shopManager;
    }

    public Database getDb() {
        return db;
    }
}
