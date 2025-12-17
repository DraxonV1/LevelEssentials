package asia.draxon.levelessentials.config;

import asia.draxon.levelessentials.LevelEssentials;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ModConfig config = LevelEssentials.getConfig();
            ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("LevelEssentials Configuration"));
            
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();
            
            // Display Category
            ConfigCategory display = builder.getOrCreateCategory(Text.literal("Display"));
            display.addEntry(entryBuilder.startBooleanToggle(
                Text.literal("Show Death Coordinates"), config.showDeathCoordsOnScreen)
                .setDefaultValue(true)
                .setSaveConsumer(val -> config.showDeathCoordsOnScreen = val)
                .build());
            
            display.addEntry(entryBuilder.startBooleanToggle(
                Text.literal("Enable HUD"), config.enableHud)
                .setDefaultValue(true)
                .setSaveConsumer(val -> config.enableHud = val)
                .build());
            
            display.addEntry(entryBuilder.startBooleanToggle(
                Text.literal("Show Respawn Coordinates"), config.showRespawnCoords)
                .setDefaultValue(true)
                .setSaveConsumer(val -> config.showRespawnCoords = val)
                .build());
            
            // HUD Category
            ConfigCategory hud = builder.getOrCreateCategory(Text.literal("HUD"));
            hud.addEntry(entryBuilder.startFloatField(
                Text.literal("HUD Scale"), config.hudScale)
                .setDefaultValue(1.0f)
                .setMin(0.5f)
                .setMax(2.0f)
                .setSaveConsumer(val -> config.hudScale = val)
                .build());
            
            hud.addEntry(entryBuilder.startBooleanToggle(
                Text.literal("HUD Shadow"), config.hudShadow)
                .setDefaultValue(true)
                .setSaveConsumer(val -> config.hudShadow = val)
                .build());
            
            // Features Category
            ConfigCategory features = builder.getOrCreateCategory(Text.literal("Features"));
            features.addEntry(entryBuilder.startIntField(
                Text.literal("Max Death Records"), config.maxDeathRecords)
                .setDefaultValue(50)
                .setMin(10)
                .setMax(200)
                .setSaveConsumer(val -> config.maxDeathRecords = val)
                .build());
            
            features.addEntry(entryBuilder.startBooleanToggle(
                Text.literal("Enable Death Statistics"), config.enableDeathStatistics)
                .setDefaultValue(true)
                .setSaveConsumer(val -> config.enableDeathStatistics = val)
                .build());
            
            features.addEntry(entryBuilder.startBooleanToggle(
                Text.literal("Enable Teleport Suggestions"), config.enableTeleportSuggestions)
                .setDefaultValue(true)
                .setSaveConsumer(val -> config.enableTeleportSuggestions = val)
                .build());
            
            features.addEntry(entryBuilder.startBooleanToggle(
                Text.literal("Death Compass (Points to Last Death)"), config.enableDeathCompass)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Adds a compass overlay pointing to your last death location"))
                .setSaveConsumer(val -> config.enableDeathCompass = val)
                .build());
            
            features.addEntry(entryBuilder.startBooleanToggle(
                Text.literal("Notify On Death"), config.notifyOnDeath)
                .setDefaultValue(true)
                .setSaveConsumer(val -> config.notifyOnDeath = val)
                .build());
            
            builder.setSavingRunnable(config::save);
            return builder.build();
        };
    }
}