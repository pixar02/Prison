/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017-2020 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.spigot;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.update.spiget.SpigetUpdate;
import org.inventivetalent.update.spiget.UpdateCallback;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.PrisonCommand;
import tech.mcprison.prison.alerts.Alerts;
import tech.mcprison.prison.integration.Integration;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.LogLevel;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.shops.PrisonShops;
import tech.mcprison.prison.spigot.autofeatures.AutoManager;
import tech.mcprison.prison.spigot.autofeatures.AutoManagerFeatures;
import tech.mcprison.prison.spigot.block.OnBlockBreakEventListener;
import tech.mcprison.prison.spigot.commands.PrisonShortcutCommands;
import tech.mcprison.prison.spigot.commands.PrisonSpigotCommands;
import tech.mcprison.prison.spigot.compat.Compatibility;
import tech.mcprison.prison.spigot.compat.Spigot113;
import tech.mcprison.prison.spigot.compat.Spigot18;
import tech.mcprison.prison.spigot.compat.Spigot19;
import tech.mcprison.prison.spigot.economies.EssentialsEconomy;
import tech.mcprison.prison.spigot.economies.GemsEconomy;
import tech.mcprison.prison.spigot.economies.SaneEconomy;
import tech.mcprison.prison.spigot.economies.VaultEconomy;
import tech.mcprison.prison.spigot.gui.GUIListener;
import tech.mcprison.prison.spigot.gui.GuiConfig;
import tech.mcprison.prison.spigot.gui.ListenersPrisonManager;
import tech.mcprison.prison.spigot.permissions.LuckPermissions;
import tech.mcprison.prison.spigot.permissions.LuckPerms5;
import tech.mcprison.prison.spigot.permissions.VaultPermissions;
import tech.mcprison.prison.spigot.placeholder.MVdWPlaceholderIntegration;
import tech.mcprison.prison.spigot.placeholder.PlaceHolderAPIIntegration;
import tech.mcprison.prison.spigot.player.SlimeBlockFunEventListener;
import tech.mcprison.prison.spigot.sellall.SellAllCommands;
import tech.mcprison.prison.spigot.sellall.SellAllConfig;
import tech.mcprison.prison.spigot.spiget.BluesSpigetSemVerComparator;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * The plugin class for the Spigot implementation.
 *
 * @author Faizaan A. Datoo
 * @author GABRYCA
 */
public class SpigotPrison extends JavaPlugin {

    Field commandMap;
    Field knownCommands;
    SpigotScheduler scheduler;
    Compatibility compatibility;
    boolean debug = false;

    private File dataDirectory;
//    private boolean doAlertAboutConvert = false;
    
    private AutoManagerFeatures autoFeatures = null;
//    private FileConfiguration autoFeaturesConfig = null;

    private static SpigotPrison config;

    public static SpigotPrison getInstance(){
        return config;
    }

//  ###Tab-Complete###
//  private TreeSet<String> registeredCommands = new TreeSet<>();
       

//    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onLoad() {
    	
    	/**
    	 * Old versions of prison MUST be upgraded with v3.0.x or even v3.1.1.
    	 * Upgrading from old versions of prison to v3.2.x is not supported.  
    	 * Please upgrade to an earlier release of v3.0.x then to v3.2.1.
    	
        // The meta file is used to see if the folder needs converting.
        // If the folder doesn't contain it, it's probably not a Prison 3 thing.
        File metaFile = new File(getDataFolder(), ".meta");
        if (getDataFolder().exists()) {
            if (!metaFile.exists()) {
                File old = getDataFolder();
                old.renameTo(new File(getDataFolder().getParent(), "Prison.old"));
                doAlertAboutConvert = true;
            }
        }
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
            try {
                metaFile.createNewFile();
            } catch (IOException e) {
                Output.get().logError( 
                        "Could not create .meta file, this will cause problems with the converter!");
            }
        }
    	 */
        
    }

    @Override
    public void onEnable() {
    	config = this;
        this.saveDefaultConfig();
        debug = getConfig().getBoolean("debug", false);

        initDataDir();
        initCommandMap();
        initCompatibility();
        initUpdater();
        this.scheduler = new SpigotScheduler(this);
        
        Prison.get().init(new SpigotPlatform(this), Bukkit.getVersion());
        Prison.get().getLocaleManager().setDefaultLocale(getConfig().getString("default-language", "en_US"));
        
        new GuiConfig();

        GUIListener.get().init(this);
        Bukkit.getPluginManager().registerEvents(new ListenersPrisonManager(),this);
        Bukkit.getPluginManager().registerEvents(new PrisonSpigotCommands(), this);

        Bukkit.getPluginManager().registerEvents(new AutoManager(), this);
        Bukkit.getPluginManager().registerEvents(new OnBlockBreakEventListener(), this);
        Bukkit.getPluginManager().registerEvents(new SlimeBlockFunEventListener(), this);

        getCommand("prisonmanager").setExecutor(new PrisonSpigotCommands());
        
        // Only register the command if not enabled so it will not conflict with other sellall plugins:
        if ( SellAllCommands.isEnabled() ) {
        	new SellAllConfig();
        	
        	getCommand("sellall").setExecutor(new SellAllCommands());
        }
        
        
        new SpigotListener().init();

        Prison.get().getCommandHandler().registerCommands(new PrisonShortcutCommands());
        
        initIntegrations();
        initModules();

        applyDeferredIntegrationInitializations();
        
        extractCommandsForAutoComplete();
        
        initMetrics();

//        if (doAlertAboutConvert) {
//            Alerts.getInstance().sendAlert(
//                    "&7An old installation of Prison has been detected. &3Type /prison convert to convert your old data automatically. &7If you already converted, delete the 'Prison.old' folder so that we stop nagging you.");
//        }
        
        
        Prison.get().getPlatform().getPlaceholders().printPlaceholderStats();
                
        
        
        // Finally print the version after loading the prison plugin:
        PrisonCommand cmdVersion = Prison.get().getPrisonCommands();
        
//        // Store all loaded plugins within the PrisonCommand for later inclusion:
//        for ( Plugin plugin : Bukkit.getPluginManager().getPlugins() ) {
//        	String name = plugin.getName();
//        	String version = plugin.getDescription().getVersion();
//        	String value = "&7" + name + " &3(&a" + version + "&3)";
//        	cmdVersion.getRegisteredPlugins().add( value );
//		}

		ChatDisplay cdVersion = cmdVersion.displayVersion();
		cdVersion.toLog( LogLevel.INFO );
		
		// Provides a startup test of blocks available for the version of spigot that being used:
		if ( getConfig().getBoolean("prison-block-compatibility-report") ) {
			SpigotUtil.testAllPrisonBlockTypes();
		}
		
		Output.get().logInfo( "Prison - Finished loading." );
		
    }

    @Override
    public void onDisable() {
    	if (this.scheduler != null ) {
    		this.scheduler.cancelAll();
    	}
        Prison.get().deinit();
    }

    public static FileConfiguration getGuiConfig(){
        GuiConfig messages = new GuiConfig();
        return messages.getFileGuiConfig();
    }

    public static FileConfiguration getSellAllConfig(){
        SellAllConfig configs = new SellAllConfig();
        return configs.getFileSellAllConfig();
    }
    
    public AutoManagerFeatures getAutoFeatures() {
		return autoFeatures;
	}

	public void setAutoFeatures( AutoManagerFeatures autoFeatures ) {
		this.autoFeatures = autoFeatures;
	}

    
    

    public static String format(String format){
        return ChatColor.translateAlternateColorCodes('&', format);
    }

    private void initMetrics() {
        if (!getConfig().getBoolean("send-metrics", true)) {
            return; // Don't check if they don't want it
        }
        Metrics metrics = new Metrics(this);

        // Report the modules being used
        metrics.addCustomChart(new Metrics.SimpleBarChart("modules_used", () -> {
            Map<String, Integer> valueMap = new HashMap<>();
            for (Module m : PrisonAPI.getModuleManager().getModules()) {
                valueMap.put(m.getName(), 1);
            }
            return valueMap;
        }));

        // Report the API level
        metrics.addCustomChart(
                new Metrics.SimplePie("api_level", () -> "API Level " + Prison.API_LEVEL));
        
        Optional<Module> prisonMinesOpt = Prison.get().getModuleManager().getModule( PrisonMines.MODULE_NAME );
        Optional<Module> prisonRanksOpt = Prison.get().getModuleManager().getModule( PrisonRanks.MODULE_NAME );
        
        int mineCount = prisonMinesOpt.map(module -> ((PrisonMines) module).getMineManager().getMines().size()).orElse(0);
        int rankCount = prisonRanksOpt.map(module -> ((PrisonRanks) module).getRankCount()).orElse(0);
        int ladderCount = prisonRanksOpt.map(module -> ((PrisonRanks) module).getladderCount()).orElse(0);
        
        metrics.addCustomChart(new Metrics.MultiLineChart("mines_ranks_and_ladders", new Callable<Map<String, Integer>>() {
            @Override
            public Map<String, Integer> call() throws Exception {
                Map<String, Integer> valueMap = new HashMap<>();
                valueMap.put("mines", mineCount);
                valueMap.put("ranks", rankCount);
                valueMap.put("ladders", ladderCount);
                return valueMap;
            }
        }));
    }

	private void initUpdater() {
        if (!getConfig().getBoolean("check-updates")) {
            return; // Don't check if they don't want it
        }
        
//        String currentVersion = getDescription().getVersion();

        SpigetUpdate updater = new SpigetUpdate(this, Prison.SPIGOTMC_ORG_PROJECT_ID);
//        SpigetUpdate updater = new SpigetUpdate(this, 1223);
        
        BluesSpigetSemVerComparator aRealSemVerComparator = new BluesSpigetSemVerComparator();
        updater.setVersionComparator( aRealSemVerComparator );
//        updater.setVersionComparator(VersionComparator.EQUAL);

        updater.checkForUpdate(new UpdateCallback() {
            @Override
            public void updateAvailable(String newVersion, String downloadUrl,
                                        boolean hasDirectDownload) {
                Alerts.getInstance().sendAlert(
                        "&3%s is now available. &7Go to the &lSpigot&r&7 page to download the latest release with new features and fixes :)",
                        newVersion);
            }

            @Override
            public void upToDate() {
                // Plugin is up-to-date
            }
        });

    }

    private void initDataDir() {
        dataDirectory = new File(getDataFolder(), "data_storage");
        if (!dataDirectory.exists()) {
            dataDirectory.mkdir();
        }
    }

    private void initCommandMap() {
        try {
            commandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMap.setAccessible(true);
            knownCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommands.setAccessible(true);
        } catch (NoSuchFieldException e) {
            getLogger().severe(
                    "&c&lReflection error: &7Ensure that you're using the latest version of Spigot and Prison.");
            e.printStackTrace();
        }
    }

    private void initCompatibility() {
    	
    	if ( new BluesSpigetSemVerComparator().compareMCVersionTo("1.9.0") < 0 ) {
            compatibility = new Spigot18();
        } 
    	else if ( new BluesSpigetSemVerComparator().compareMCVersionTo("1.13.0") < 0 ) {
            compatibility = new Spigot19();
        }
    	else {
    		compatibility = new Spigot113();
    	}

        getLogger().info("Using version adapter " + compatibility.getClass().getName());
    }


	private void initIntegrations() {

    	registerIntegration(new VaultEconomy());
        registerIntegration(new EssentialsEconomy());
        registerIntegration(new SaneEconomy());
        registerIntegration(new GemsEconomy());

        registerIntegration(new VaultPermissions());
        registerIntegration(new LuckPerms5());
        registerIntegration(new LuckPermissions());

        registerIntegration(new MVdWPlaceholderIntegration());
        registerIntegration(new PlaceHolderAPIIntegration());
        
//        registerIntegration(new WorldGuard6Integration());
//        registerIntegration(new WorldGuard7Integration());
    }
	
	/**
	 * <p>This "tries" to reload the placeholder integrations, which may not
	 * always work, and can fail.  It's here to try to do something, but
	 * it may not work.  At least we tried.
	 * </p>
	 * 
	 */
	public void reloadIntegrationsPlaceholders() {
		
		MVdWPlaceholderIntegration ph1 = new MVdWPlaceholderIntegration();
		PlaceHolderAPIIntegration ph2 = new PlaceHolderAPIIntegration();
		
		registerIntegration( ph1 );
		registerIntegration( ph2 );
		
		ph1.deferredInitialization();
		ph2.deferredInitialization();
	}
    
    private void registerIntegration(Integration integration) {
    	integration.setRegistered( 
    			Bukkit.getPluginManager().isPluginEnabled(integration.getProviderName()) );

    	integration.integrate();
		
    	PrisonAPI.getIntegrationManager().register(integration);
    }

    private void initModules() {
        YamlConfiguration modulesConf = loadConfig("modules.yml");

        // TODO: This business logic needs to be moved to the Module Manager:
        if (modulesConf.getBoolean("mines")) {
            Prison.get().getModuleManager()
                    .registerModule(new PrisonMines(getDescription().getVersion()));
        } else {
            Output.get().logInfo("&7Modules: &cPrison Mines are disabled and were not Loaded. ");
            Output.get().logInfo("&7  Prison Mines have been disabled in &2plugins/Prison/modules.yml&7.");
            Prison.get().getModuleManager().getDisabledModules().add( PrisonMines.MODULE_NAME );
        }

        if (modulesConf.getBoolean("ranks")) {
            Prison.get().getModuleManager()
                    .registerModule(new PrisonRanks(getDescription().getVersion()));
        } else {
        	Output.get().logInfo("&3Modules: &cPrison Ranks, Ladders, and Players are disabled and were not Loaded. ");
        	Output.get().logInfo("&7  Prison Ranks have been disabled in &2plugins/Prison/modules.yml&7.");
        	Prison.get().getModuleManager().getDisabledModules().add( PrisonRanks.MODULE_NAME );
        }
        if (modulesConf.getBoolean("shops")) {
            Prison.get().getModuleManager()
                    .registerModule(new PrisonShops(getDescription().getVersion()));
        } else {
            Output.get().logInfo("&3Modules: &cPrison Shops are disabled and were not Loaded. ");
            Output.get().logInfo("&7  Prison Shops have been disabled in &2plugins/Prison/modules.yml&7.");
            Prison.get().getModuleManager().getDisabledModules().add( PrisonShops.MODULE_NAME );
        }
    }

    private void applyDeferredIntegrationInitializations() {
    	for ( Integration deferredIntegration : PrisonAPI.getIntegrationManager().getDeferredIntegrations() ) {
    		deferredIntegration.deferredInitialization();
    	}
    }
    
    public Compatibility getCompatibility() {
    	return compatibility;
    }
    
    private File getBundledFile(String name) {
        getDataFolder().mkdirs();
        File file = new File(getDataFolder(), name);
        if (!file.exists()) {
            saveResource(name, false);
        }
        return file;
    }

    private YamlConfiguration loadConfig(String file) {
        return YamlConfiguration.loadConfiguration(getBundledFile(file));
    }

    File getDataDirectory() {
        return dataDirectory;
    }
    

    /**
     * <p>This function will register any missing "command" and will
     * set the usable onTabComplete to the one within this class, 
     * that follows this function.  
     * </p>
     * 
     */
    private void extractCommandsForAutoComplete() {
/*
 * ###Tab-Complete### (search for other occurrences of this tag)
 * 
 * The following works up to a certain point, but is disabled until 
 * a full solution can be implemented.
 * 
		List<String> commandKeys = Prison.get().getCommandHandler().getRootCommandKeys();
    	
    	registeredCommands.clear();
    	registeredCommands.addAll( commandKeys );
    	
    	// commands are already broken down to elements with roots: Keep the following
    	// just in case we need to expand with other uses:
    	for ( String cmdKey : commandKeys ) {
    		
    		Output.get().logInfo( "SpigotPrison.extractCommandsForAutoComplete: Command: %s", cmdKey );
    		
    		Optional<tech.mcprison.prison.commands.PluginCommand> registeredCommand = Prison.get().getPlatform().getCommand(cmdKey);
    		if ( !registeredCommand.isPresent() ) {
    			tech.mcprison.prison.commands.PluginCommand  rootPcommand = new tech.mcprison.prison.commands.PluginCommand(cmdKey, "--", "/" + cmdKey);
    			Prison.get().getPlatform().registerCommand(rootPcommand);
    		}
    		
    		PluginCommand pCommand = this.getCommand(cmdKey);
    		if ( pCommand != null ) {
    			pCommand.setTabCompleter(this);
    		} else {
    			Output.get().logInfo( "SpigotPrison.extractCommandsForAutoComplete: " +
				"## Error not found ## Command: %s ", cmdKey );
    		}
		}
 */
    	
	}
/*
 * ###Tab-Complete###
 * 
 * This function is disabled until tab complete can be fully implemented.
 * 
 * @see org.bukkit.plugin.java.JavaPlugin#onTabComplete(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
 *
 *  // Not being used...
	@Override
	public List<String> onTabComplete( CommandSender sender, Command command, String alias, String[] args )
	{
		List<String> results = new ArrayList<>();
		Output.get().logInfo( "SpigotPrison.onTabComplete: Command: %s :: %s", command.getLabel(), command.getName() );

		// Map<String, Map<String, Object>> cmds = getDescription().getCommands();
		
//		registeredCommands
		
		return results;
	}
 */
    
}
