package asia.draxon.levelessentials;

import asia.draxon.levelessentials.commands.ModCommands;
import asia.draxon.levelessentials.config.ModConfig;
import asia.draxon.levelessentials.data.DataManager;
import asia.draxon.levelessentials.hud.HudRenderer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LevelEssentials implements ModInitializer {
    public static final String MOD_ID = "levelessentials";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    private static DataManager dataManager;
    private static ModConfig config;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing LevelEssentials...");
        
        // Initialize data manager
        dataManager = new DataManager();
        
        // Initialize config
        config = ModConfig.load();
        
        // Register commands
        ModCommands.register();
        
        // Register HUD renderer
        HudRenderCallback.EVENT.register(new HudRenderer());
        
        // Register tick events for HUD position editor
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            HudRenderer.onClientTick(client);
        });
        
        LOGGER.info("LevelEssentials initialized successfully!");
    }
    
    public static DataManager getDataManager() {
        return dataManager;
    }
    
    public static ModConfig getConfig() {
        return config;
    }
}