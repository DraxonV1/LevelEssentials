package asia.draxon.levelessentials.mixin;

import asia.draxon.levelessentials.LevelEssentials;
import asia.draxon.levelessentials.data.DeathRecord;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    
    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onPlayerDeath(DamageSource damageSource, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        
        if (player instanceof ServerPlayerEntity serverPlayer) {
            BlockPos deathPos = player.getBlockPos();
            RegistryKey<World> dimension = player.getWorld().getRegistryKey();
            String dimensionName = dimension.getValue().toString();
            String biome = player.getWorld().getBiome(deathPos).getKey().get().getValue().toString();
            String deathCause = damageSource.getName();
            int experienceLost = serverPlayer.experienceLevel;
            
            DeathRecord record = new DeathRecord(
                deathPos, 
                dimensionName, 
                biome, 
                deathCause, 
                experienceLost
            );
            
            LevelEssentials.getDataManager().addDeathRecord(
                serverPlayer.getUuidAsString(), 
                record
            );
            
            LevelEssentials.LOGGER.info("Death recorded for player {} at {}", 
                serverPlayer.getName().getString(), deathPos);
        }
    }
}