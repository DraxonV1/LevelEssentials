package asia.draxon.levelessentials.hud;

import asia.draxon.levelessentials.LevelEssentials;
import asia.draxon.levelessentials.config.ModConfig;
import asia.draxon.levelessentials.data.DeathRecord;
import asia.draxon.levelessentials.data.RespawnRecord;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

public class HudRenderer implements HudRenderCallback {
    private static boolean positionEditorActive = false;
    private static int tempX = 0;
    private static int tempY = 0;
    
    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        ModConfig config = LevelEssentials.getConfig();
        
        if (!config.enableHud || client.player == null) return;
        
        String playerUuid = client.player.getUuidAsString();
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
        
        // Calculate position based on anchor
        int x = calculateX(config, screenWidth);
        int y = calculateY(config, screenHeight);
        
        if (positionEditorActive) {
            x = tempX;
            y = tempY;
            
            // Draw editor overlay
            drawContext.fill(x - 5, y - 5, x + 205, y + 55, 0x80000000);
            drawContext.drawText(client.textRenderer, 
                Text.literal("Position Editor (Arrow Keys)"), 
                x, y, 0xFFFF00, true);
            drawContext.drawText(client.textRenderer, 
                Text.literal("ENTER: Save | ESC: Cancel"), 
                x, y + 40, 0xFFFFFF, true);
        }
        
        int yOffset = 0;
        
        // Render respawn point info
        if (config.showRespawnCoords) {
            RespawnRecord lastRespawn = LevelEssentials.getDataManager().getLastRespawn(playerUuid);
            if (lastRespawn != null) {
                String respawnText = String.format("§aRespawn: §f%s §7(%s)", 
                    lastRespawn.getFormattedCoords(), lastRespawn.type);
                drawContext.drawText(client.textRenderer, 
                    Text.literal(respawnText), 
                    x, y + yOffset, config.hudColor, config.hudShadow);
                yOffset += 12;
            }
        }
        
        // Render death coordinates
        if (config.showDeathCoords) {
            DeathRecord lastDeath = LevelEssentials.getDataManager().getLastDeath(playerUuid);
            if (lastDeath != null) {
                String deathText = String.format("§cLast Death: §f%s", lastDeath.getFormattedCoords());
                drawContext.drawText(client.textRenderer, 
                    Text.literal(deathText), 
                    x, y + yOffset, config.hudColor, config.hudShadow);
                yOffset += 12;
                
                // Death compass feature
                if (config.enableDeathCompass && client.player != null) {
                    double dx = lastDeath.position.getX() - client.player.getX();
                    double dz = lastDeath.position.getZ() - client.player.getZ();
                    double distance = Math.sqrt(dx * dx + dz * dz);
                    double angle = Math.atan2(dz, dx);
                    
                    String compassText = String.format("§e↗ §7%.0fm", distance);
                    drawContext.drawText(client.textRenderer, 
                        Text.literal(compassText), 
                        x, y + yOffset, config.hudColor, config.hudShadow);
                }
            }
        }
    }
    
    public static void togglePositionEditor() {
        positionEditorActive = !positionEditorActive;
        if (positionEditorActive) {
            ModConfig config = LevelEssentials.getConfig();
            MinecraftClient client = MinecraftClient.getInstance();
            int screenWidth = client.getWindow().getScaledWidth();
            int screenHeight = client.getWindow().getScaledHeight();
            tempX = calculateX(config, screenWidth);
            tempY = calculateY(config, screenHeight);
        }
    }
    
    public static void onClientTick(MinecraftClient client) {
        if (!positionEditorActive || client.currentScreen != null) return;
        
        int moveSpeed = 5;
        
        if (isKeyPressed(GLFW.GLFW_KEY_UP)) {
            tempY = Math.max(0, tempY - moveSpeed);
        }
        if (isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
            tempY = Math.min(client.getWindow().getScaledHeight() - 50, tempY + moveSpeed);
        }
        if (isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
            tempX = Math.max(0, tempX - moveSpeed);
        }
        if (isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
            tempX = Math.min(client.getWindow().getScaledWidth() - 200, tempX + moveSpeed);
        }
        
        if (isKeyPressed(GLFW.GLFW_KEY_ENTER)) {
            savePosition();
        }
        if (isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
            positionEditorActive = false;
        }
    }
    
    private static void savePosition() {
        ModConfig config = LevelEssentials.getConfig();
        config.hudX = tempX;
        config.hudY = tempY;
        config.hudAnchor = "CUSTOM";
        config.save();
        positionEditorActive = false;
        
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(Text.literal("§aHUD position saved!"), false);
        }
    }
    
    private static boolean isKeyPressed(int keyCode) {
        long window = MinecraftClient.getInstance().getWindow().getHandle();
        return GLFW.glfwGetKey(window, keyCode) == GLFW.GLFW_PRESS;
    }
    
    private static int calculateX(ModConfig config, int screenWidth) {
        if (config.hudAnchor.equals("CUSTOM")) return config.hudX;
        
        return switch (config.hudAnchor) {
            case "TOP_LEFT", "BOTTOM_LEFT" -> config.hudX;
            case "TOP_RIGHT", "BOTTOM_RIGHT" -> screenWidth - 200 - config.hudX;
            default -> config.hudX;
        };
    }
    
    private static int calculateY(ModConfig config, int screenHeight) {
        if (config.hudAnchor.equals("CUSTOM")) return config.hudY;
        
        return switch (config.hudAnchor) {
            case "TOP_LEFT", "TOP_RIGHT" -> config.hudY;
            case "BOTTOM_LEFT", "BOTTOM_RIGHT" -> screenHeight - 50 - config.hudY;
            default -> config.hudY;
        };
    }
}