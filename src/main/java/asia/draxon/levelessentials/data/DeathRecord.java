package asia.draxon.levelessentials.data;

import net.minecraft.util.math.BlockPos;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DeathRecord {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public BlockPos position;
    public String dimension;
    public String biome;
    public String deathCause;
    public String timestamp;
    public int experienceLost;
    
    public DeathRecord(BlockPos position, String dimension, String biome, String deathCause, int experienceLost) {
        this.position = position;
        this.dimension = dimension;
        this.biome = biome;
        this.deathCause = deathCause;
        this.timestamp = LocalDateTime.now().format(FORMATTER);
        this.experienceLost = experienceLost;
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