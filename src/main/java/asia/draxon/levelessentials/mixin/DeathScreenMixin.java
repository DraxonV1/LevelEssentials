package asia.draxon.levelessentials.mixin;

import asia.draxon.levelessentials.LevelEssentials;
import asia.draxon.levelessentials.config.ModConfig;
import asia.draxon.levelessentials.data.DeathRecord;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DeathScreen.class)
public abstract class DeathScreenMixin extends Screen {
    protected DeathScreenMixin(Text title) {
        super(title);
    }
    
    @Inject(method = "render", at = @At("TAIL"))
    private void renderDeathCoords(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ModConfig config = LevelEssentials.getConfig();
        if (!config.showDeathCoordsOnScreen || this.client == null || this.client.player == null) {
            return;
        }
        
        String playerUuid = this.client.player.getUuidAsString();
        DeathRecord lastDeath = LevelEssentials.getDataManager().getLastDeath(playerUuid);
        
        if (lastDeath != null) {
            int centerX = this.width / 2;
            int y = this.height / 2 + 50;
            
            Text coordsText = Text.literal(String.format("§cYou died at: §f%s", lastDeath.getFormattedCoords()));
            context.drawCenteredTextWithShadow(this.textRenderer, coordsText, centerX, y, 0xFFFFFF);
            
            Text dimensionText = Text.literal(String.format("§7in %s - %s", 
                lastDeath.getShortDimension(), lastDeath.biome));
            context.drawCenteredTextWithShadow(this.textRenderer, dimensionText, centerX, y + 12, 0xAAAAAA);
            
            if (config.enableTeleportSuggestions) {
                Text tpText = Text.literal("§aUse /lastdeaths to see all death locations");
                context.drawCenteredTextWithShadow(this.textRenderer, tpText, centerX, y + 24, 0x00FF00);
            }
        }
    }
}