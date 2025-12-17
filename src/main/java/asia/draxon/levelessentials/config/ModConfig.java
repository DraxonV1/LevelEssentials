package asia.draxon.levelessentials.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(
        FabricLoader.getInstance().getConfigDir().toFile(), 
        "levelessentials.json"
    );
    
    // Display settings
    public boolean showDeathCoords = true;
    public boolean showRespawnCoords = true;
    public boolean showDeathCoordsOnScreen = true;
    
    // HUD settings
    public boolean enableHud = true;
    public int hudX = 10;
    public int hudY = 10;
    public String hudAnchor = "TOP_RIGHT"; // TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    public float hudScale = 1.0f;
    public int hudColor = 0xFFFFFF;
    public boolean hudShadow = true;
    
    // Feature settings
    public int maxDeathRecords = 50;
    public int maxRespawnRecords = 20;
    public boolean enableDeathStatistics = true;
    public boolean enableTeleportSuggestions = true;
    public boolean enableDeathCompass = true;
    
    // Auto-waypoint settings (NEW FEATURE)
    public boolean autoCreateWaypoints = false;
    public boolean notifyOnDeath = true;
    
    public static ModConfig load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                return GSON.fromJson(reader, ModConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        ModConfig config = new ModConfig();
        config.save();
        return config;
    }
    
    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}