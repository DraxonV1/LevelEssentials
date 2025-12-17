package asia.draxon.levelessentials.mixin;

import asia.draxon.levelessentials.LevelEssentials;
import asia.draxon.levelessentials.data.RespawnRecord;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
    
    @Inject(method = "setSpawnPoint", at = @At("HEAD"))
    private void onSetSpawnPoint(RegistryKey<World> dimension, BlockPos pos, float angle, 
                                 boolean forced, boolean sendMessage, CallbackInfo ci) {
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) (Object) this;
        
        if (pos != null) {
            String dimensionName = dimension.getValue().toString();
            String biome = serverPlayer.getWorld().getBiome(pos).getKey().get().getValue().toString();
            String type = dimensionName.contains("nether") ? "RESPAWN_ANCHOR" : "BED";
            
            RespawnRecord record = new RespawnRecord(pos, dimensionName, biome, type);
            
            LevelEssentials.getDataManager().addRespawnRecord(
                serverPlayer.getUuidAsString(), 
                record
            );
            
            LevelEssentials.LOGGER.info("Respawn point recorded for player {} at {}", 
                serverPlayer.getName().getString(), pos);
        }
    }
}