package asia.draxon.levelessentials.data;

import asia.draxon.levelessentials.LevelEssentials;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.math.BlockPos;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File DATA_DIR = new File(
        FabricLoader.getInstance().getConfigDir().toFile(), 
        "levelessentials"
    );
    
    private Map<String, List<DeathRecord>> playerDeaths = new HashMap<>();
    private Map<String, List<RespawnRecord>> playerRespawns = new HashMap<>();
    
    public DataManager() {
        if (!DATA_DIR.exists()) {
            DATA_DIR.mkdirs();
        }
        load();
    }
    
    public void addDeathRecord(String playerUuid, DeathRecord record) {
        playerDeaths.computeIfAbsent(playerUuid, k -> new ArrayList<>()).add(record);
        
        // Limit records
        List<DeathRecord> deaths = playerDeaths.get(playerUuid);
        int maxRecords = LevelEssentials.getConfig().maxDeathRecords;
        if (deaths.size() > maxRecords) {
            deaths.remove(0);
        }
        
        save();
    }
    
    public void addRespawnRecord(String playerUuid, RespawnRecord record) {
        playerRespawns.computeIfAbsent(playerUuid, k -> new ArrayList<>()).add(record);
        
        // Limit records
        List<RespawnRecord> respawns = playerRespawns.get(playerUuid);
        int maxRecords = LevelEssentials.getConfig().maxRespawnRecords;
        if (respawns.size() > maxRecords) {
            respawns.remove(0);
        }
        
        save();
    }
    
    public List<DeathRecord> getDeathRecords(String playerUuid) {
        return playerDeaths.getOrDefault(playerUuid, new ArrayList<>());
    }
    
    public List<RespawnRecord> getRespawnRecords(String playerUuid) {
        return playerRespawns.getOrDefault(playerUuid, new ArrayList<>());
    }
    
    public DeathRecord getLastDeath(String playerUuid) {
        List<DeathRecord> deaths = getDeathRecords(playerUuid);
        return deaths.isEmpty() ? null : deaths.get(deaths.size() - 1);
    }
    
    public RespawnRecord getLastRespawn(String playerUuid) {
        List<RespawnRecord> respawns = getRespawnRecords(playerUuid);
        return respawns.isEmpty() ? null : respawns.get(respawns.size() - 1);
    }
    
    private void save() {
        try {
            File deathsFile = new File(DATA_DIR, "deaths.json");
            try (FileWriter writer = new FileWriter(deathsFile)) {
                GSON.toJson(playerDeaths, writer);
            }
            
            File respawnsFile = new File(DATA_DIR, "respawns.json");
            try (FileWriter writer = new FileWriter(respawnsFile)) {
                GSON.toJson(playerRespawns, writer);
            }
        } catch (IOException e) {
            LevelEssentials.LOGGER.error("Failed to save data", e);
        }
    }
    
    private void load() {
        try {
            File deathsFile = new File(DATA_DIR, "deaths.json");
            if (deathsFile.exists()) {
                try (FileReader reader = new FileReader(deathsFile)) {
                    Type type = new TypeToken<Map<String, List<DeathRecord>>>(){}.getType();
                    playerDeaths = GSON.fromJson(reader, type);
                    if (playerDeaths == null) playerDeaths = new HashMap<>();
                }
            }
            
            File respawnsFile = new File(DATA_DIR, "respawns.json");
            if (respawnsFile.exists()) {
                try (FileReader reader = new FileReader(respawnsFile)) {
                    Type type = new TypeToken<Map<String, List<RespawnRecord>>>(){}.getType();
                    playerRespawns = GSON.fromJson(reader, type);
                    if (playerRespawns == null) playerRespawns = new HashMap<>();
                }
            }
        } catch (IOException e) {
            LevelEssentials.LOGGER.error("Failed to load data", e);
        }
    }
}