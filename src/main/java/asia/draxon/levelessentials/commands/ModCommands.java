package asia.draxon.levelessentials.commands;

import asia.draxon.levelessentials.LevelEssentials;
import asia.draxon.levelessentials.data.DeathRecord;
import asia.draxon.levelessentials.data.RespawnRecord;
import asia.draxon.levelessentials.hud.HudRenderer;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            // /lastdeaths command
            dispatcher.register(CommandManager.literal("lastdeaths")
                .executes(context -> {
                    String uuid = context.getSource().getPlayer().getUuidAsString();
                    List<DeathRecord> deaths = LevelEssentials.getDataManager().getDeathRecords(uuid);
                    
                    if (deaths.isEmpty()) {
                        context.getSource().sendFeedback(() -> 
                            Text.literal("No death records found.").formatted(Formatting.YELLOW), false);
                        return 0;
                    }
                    
                    context.getSource().sendFeedback(() -> 
                        Text.literal("=== Your Last Deaths ===").formatted(Formatting.GOLD, Formatting.BOLD), false);
                    
                    for (int i = deaths.size() - 1; i >= Math.max(0, deaths.size() - 10); i--) {
                        DeathRecord death = deaths.get(i);
                        int index = i + 1;
                        
                        Text message = Text.literal(String.format("#%d ", index))
                            .formatted(Formatting.GRAY)
                            .append(Text.literal(death.getFormattedCoords())
                                .formatted(Formatting.AQUA)
                                .styled(style -> style
                                    .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, 
                                        String.format("/tp @s %d %d %d", death.position.getX(), death.position.getY(), death.position.getZ())))
                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
                                        Text.literal("Click to suggest teleport command")))))
                            .append(Text.literal(String.format(" in %s", death.getShortDimension()))
                                .formatted(Formatting.GRAY))
                            .append(Text.literal(String.format("\n   Cause: %s | Time: %s", 
                                death.deathCause, death.timestamp))
                                .formatted(Formatting.DARK_GRAY));
                        
                        context.getSource().sendFeedback(() -> message, false);
                    }
                    
                    return 1;
                })
                .then(CommandManager.argument("count", IntegerArgumentType.integer(1, 50))
                    .executes(context -> {
                        int count = IntegerArgumentType.getInteger(context, "count");
                        String uuid = context.getSource().getPlayer().getUuidAsString();
                        List<DeathRecord> deaths = LevelEssentials.getDataManager().getDeathRecords(uuid);
                        
                        if (deaths.isEmpty()) {
                            context.getSource().sendFeedback(() -> 
                                Text.literal("No death records found.").formatted(Formatting.YELLOW), false);
                            return 0;
                        }
                        
                        context.getSource().sendFeedback(() -> 
                            Text.literal(String.format("=== Your Last %d Deaths ===", Math.min(count, deaths.size())))
                                .formatted(Formatting.GOLD, Formatting.BOLD), false);
                        
                        for (int i = deaths.size() - 1; i >= Math.max(0, deaths.size() - count); i--) {
                            DeathRecord death = deaths.get(i);
                            int index = i + 1;
                            
                            Text message = Text.literal(String.format("#%d ", index))
                                .formatted(Formatting.GRAY)
                                .append(Text.literal(death.getFormattedCoords())
                                    .formatted(Formatting.AQUA))
                                .append(Text.literal(String.format(" in %s - %s", 
                                    death.getShortDimension(), death.deathCause))
                                    .formatted(Formatting.GRAY));
                            
                            context.getSource().sendFeedback(() -> message, false);
                        }
                        
                        return 1;
                    })));
            
            // /mybeds and /respawnpoints commands
            dispatcher.register(CommandManager.literal("mybeds")
                .executes(context -> executeRespawnPoints(context)));
            
            dispatcher.register(CommandManager.literal("respawnpoints")
                .executes(context -> executeRespawnPoints(context)));
            
            // /deathstats command (NEW FEATURE)
            dispatcher.register(CommandManager.literal("deathstats")
                .executes(context -> {
                    String uuid = context.getSource().getPlayer().getUuidAsString();
                    List<DeathRecord> deaths = LevelEssentials.getDataManager().getDeathRecords(uuid);
                    
                    if (deaths.isEmpty()) {
                        context.getSource().sendFeedback(() -> 
                            Text.literal("No death records found.").formatted(Formatting.YELLOW), false);
                        return 0;
                    }
                    
                    // Calculate statistics
                    Map<String, Long> causeCount = deaths.stream()
                        .collect(Collectors.groupingBy(d -> d.deathCause, Collectors.counting()));
                    
                    Map<String, Long> dimensionCount = deaths.stream()
                        .collect(Collectors.groupingBy(d -> d.getShortDimension(), Collectors.counting()));
                    
                    int totalXP = deaths.stream().mapToInt(d -> d.experienceLost).sum();
                    
                    context.getSource().sendFeedback(() -> 
                        Text.literal("=== Death Statistics ===").formatted(Formatting.GOLD, Formatting.BOLD), false);
                    
                    context.getSource().sendFeedback(() -> 
                        Text.literal(String.format("Total Deaths: %d", deaths.size()))
                            .formatted(Formatting.WHITE), false);
                    
                    context.getSource().sendFeedback(() -> 
                        Text.literal(String.format("Total XP Lost: %d levels", totalXP))
                            .formatted(Formatting.RED), false);
                    
                    context.getSource().sendFeedback(() -> 
                        Text.literal("\nTop Death Causes:").formatted(Formatting.YELLOW), false);
                    
                    causeCount.entrySet().stream()
                        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                        .limit(5)
                        .forEach(entry -> {
                            context.getSource().sendFeedback(() -> 
                                Text.literal(String.format("  %s: %d", entry.getKey(), entry.getValue()))
                                    .formatted(Formatting.GRAY), false);
                        });
                    
                    context.getSource().sendFeedback(() -> 
                        Text.literal("\nDeaths by Dimension:").formatted(Formatting.YELLOW), false);
                    
                    dimensionCount.forEach((dim, count) -> {
                        context.getSource().sendFeedback(() -> 
                            Text.literal(String.format("  %s: %d", dim, count))
                                .formatted(Formatting.GRAY), false);
                    });
                    
                    return 1;
                }));
            
            // /hudposition command to open HUD position editor
            dispatcher.register(CommandManager.literal("hudposition")
                .executes(context -> {
                    HudRenderer.togglePositionEditor();
                    context.getSource().sendFeedback(() -> 
                        Text.literal("HUD Position Editor toggled! Use arrow keys to adjust position, ENTER to save, ESC to cancel.")
                            .formatted(Formatting.GREEN), false);
                    return 1;
                }));
        });
    }
    
    private static int executeRespawnPoints(com.mojang.brigadier.context.CommandContext<net.minecraft.server.command.ServerCommandSource> context) {
        String uuid = context.getSource().getPlayer().getUuidAsString();
        List<RespawnRecord> respawns = LevelEssentials.getDataManager().getRespawnRecords(uuid);
        
        if (respawns.isEmpty()) {
            context.getSource().sendFeedback(() -> 
                Text.literal("No respawn points found.").formatted(Formatting.YELLOW), false);
            return 0;
        }
        
        context.getSource().sendFeedback(() -> 
            Text.literal("=== Your Respawn Points ===").formatted(Formatting.GOLD, Formatting.BOLD), false);
        
        for (int i = respawns.size() - 1; i >= 0; i--) {
            RespawnRecord respawn = respawns.get(i);
            int index = i + 1;
            
            String status = respawn.isValid ? "✓" : "✗";
            Formatting statusColor = respawn.isValid ? Formatting.GREEN : Formatting.RED;
            
            Text message = Text.literal(String.format("%s #%d ", status, index))
                .formatted(statusColor)
                .append(Text.literal(respawn.getFormattedCoords())
                    .formatted(Formatting.AQUA)
                    .styled(style -> style
                        .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, 
                            String.format("/tp @s %d %d %d", respawn.position.getX(), respawn.position.getY(), respawn.position.getZ())))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
                            Text.literal("Click to suggest teleport command")))))
                .append(Text.literal(String.format(" (%s) in %s", respawn.type, respawn.getShortDimension()))
                    .formatted(Formatting.GRAY))
                .append(Text.literal(String.format("\n   Biome: %s | Set: %s", 
                    respawn.biome, respawn.timestamp))
                    .formatted(Formatting.DARK_GRAY));
            
            context.getSource().sendFeedback(() -> message, false);
        }
        
        return 1;
    }
}