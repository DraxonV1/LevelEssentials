package asia.draxon.levelessentials.data;

import net.minecraft.util.math.BlockPos;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RespawnRecord {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public BlockPos position;
    public String dimension;
    public String biome;
    public String type; // "BED" or "RESPAWN_ANCHOR"
    public String timestamp;
    public boolean isValid;
    
    public RespawnRecord(BlockPos position, String dimension, String biome, String type) {
        this.position = position;
        this.dimension = dimension;
        this.biome = biome;
        this.type = type;
        this.timestamp = LocalDateTime.now().format(FORMATTER);
        this.isValid = true;
    }
    
    public String getFormattedCoords() {
        return String.format("[%d, %d, %d]", position.getX(), position.getY(), position.getZ());
    }
    
    public String getShortDimension() {
        if (dimension.contains("overworld")) return "Overworld";
        if (dimension.contains("nether")) return "Nether";
        if (dimension.contains("end")) return "The End";
        return dimension;
    }
}