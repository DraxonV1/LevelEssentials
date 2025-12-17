# LevelEssentials

![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21.1--1.21.11-brightgreen)
![Mod Loader](https://img.shields.io/badge/Mod%20Loader-Fabric-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

> **✅ Universal 1.21.x Compatibility:** One mod JAR works on **ALL** versions from 1.21.1 to 1.21.11! No need to download different versions.

Essential utilities for tracking deaths, respawn points, and more in Minecraft!

## Features

### Core Features
- **Death Tracking**: Automatically saves death coordinates with detailed information
- **Death Screen Display**: Shows death coordinates directly on the "You Died" screen
- **Command Access**: View death history with `/lastdeaths` command
- **Respawn Point Tracking**: Saves all bed and respawn anchor locations
- **HUD Display**: Customizable on-screen display of current respawn point and last death
- **Mod Menu Integration**: Easy configuration through Mod Menu

### Additional Features
- **Death Statistics** (`/deathstats`): View detailed statistics about your deaths
  - Total deaths and XP lost
  - Top death causes
  - Deaths by dimension
  
- **Death Compass**: Visual indicator showing distance and direction to last death location

- **Interactive Commands**: Click coordinates in chat to auto-fill teleport commands

- **HUD Position Editor** (`/hudposition`): Drag and position HUD anywhere on screen
  - Use arrow keys to adjust position
  - Real-time preview
  - Save custom positions

- **Comprehensive Data Tracking**:
  - Death cause
  - Dimension and biome information
  - Timestamp
  - Experience lost
  - Respawn point type (bed/anchor)

## Commands

| Command | Description |
|---------|-------------|
| `/lastdeaths` | Show your last 10 death locations |
| `/lastdeaths <count>` | Show your last N death locations |
| `/mybeds` | List all your saved respawn points |
| `/respawnpoints` | Alias for `/mybeds` |
| `/deathstats` | View detailed death statistics |
| `/hudposition` | Open HUD position editor |

## Configuration

Access configuration through **Mod Menu** (requires Mod Menu and Cloth Config installed).

### Display Settings
- Toggle death coordinate display on death screen
- Enable/disable HUD
- Show/hide respawn coordinates

### HUD Settings
- Adjustable HUD scale (0.5x - 2.0x)
- Custom positioning
- Color customization
- Shadow effects

### Feature Settings
- Maximum death records to keep (10-200)
- Enable death statistics tracking
- Teleport command suggestions
- Death compass indicator
- Death notifications

## Installation for Players

### Requirements
- **Minecraft**: 1.21.1 - 1.21.11
- **Fabric Loader**: 0.16.0 or higher
- **Fabric API**: Latest version for your Minecraft version
- **Java**: 21 or higher

### Recommended (Optional)
- **Mod Menu**: For in-game configuration
- **Cloth Config**: For configuration GUI

### Steps
1. Install [Fabric Loader](https://fabricmc.net/use/)
2. Download [Fabric API](https://modrinth.com/mod/fabric-api)
3. Download LevelEssentials from [releases](https://github.com/DraxonV1/LevelEssentials/releases)
4. Place all mods in your `.minecraft/mods` folder
5. Launch Minecraft with Fabric profile

## Building from Source

### Requirements
- JDK 21
- Git

### Steps
```bash
# Clone the repository
git clone https://github.com/DraxonV1/LevelEssentials.git
cd LevelEssentials

# Build the mod (Windows)
gradlew.bat build

# Build the mod (Linux/Mac)
./gradlew build

# Output will be in build/libs/
```

### Development Setup
```bash
# Generate IDE files
./gradlew genSources

# For IntelliJ IDEA
./gradlew idea

# For Eclipse
./gradlew eclipse

# Run client
./gradlew runClient
```

## Data Storage

All data is stored in `.minecraft/config/levelessentials/`:
- `deaths.json` - All death records
- `respawns.json` - All respawn point records

Configuration is stored in `.minecraft/config/levelessentials.json`

## Compatibility

**✅ Fully Compatible Versions:**
- Minecraft 1.21.1
- Minecraft 1.21.2
- Minecraft 1.21.3
- Minecraft 1.21.4
- Minecraft 1.21.5
- Minecraft 1.21.6
- Minecraft 1.21.7
- Minecraft 1.21.8
- Minecraft 1.21.9
- Minecraft 1.21.10
- Minecraft 1.21.11

**How it works across versions:**
- Built against 1.21.1 mappings (most stable)
- Uses only stable APIs that haven't changed in 1.21.x
- `fabric.mod.json` explicitly allows `>=1.21.1 <=1.21.11`
- No version-specific code or deprecated APIs used

**Mod Loader**: Fabric only (not compatible with Forge/NeoForge)

**Side**: Client and Server compatible (data saved per player)

### Version-Specific Notes

The mod compiles against Minecraft 1.21.1 but runs on all 1.21.x versions because:
1. **API Stability**: Fabric API maintains backward/forward compatibility within minor versions
2. **No Breaking Changes**: Minecraft 1.21.x series has no breaking changes in the APIs we use
3. **Mixin Compatibility**: All mixins target stable methods present in all 1.21.x versions
4. **Explicit Version Range**: The `fabric.mod.json` file specifies the supported range

### Testing Recommendations

If you want to test on different versions:
1. Download the mod built for 1.21.1
2. Drop it in your mods folder for any 1.21.x version
3. It will work without recompilation

For maximum compatibility, ensure you have:
- Latest Fabric Loader for your MC version
- Matching Fabric API version for your MC version

## Compatibility

## Troubleshooting

**Build Error: "Could not create an instance of type net.fabricmc.loom.extension.LoomGradleExtensionImpl"**
This error occurs when using an incompatible Gradle version. Solution:
```bash
# Update Gradle wrapper to 8.12
gradle wrapper --gradle-version 8.12

# Or use the gradlew command
./gradlew wrapper --gradle-version 8.12
```

**Fabric Loom Version Compatibility:**
- Loom 1.12+ requires Gradle 8.12 or higher
- Loom 1.11 requires Gradle 8.10+
- Loom 1.10 requires Gradle 8.8+

**HUD not showing?**
- Check if HUD is enabled in Mod Menu config
- Try `/hudposition` to reposition

**Commands not working?**
- Ensure you're using the correct command syntax
- Check if you have permission (should work in singleplayer)

**Data not saving?**
- Check `.minecraft/config/levelessentials/` folder exists
- Ensure you have write permissions

**Dependency Issues?**
```bash
# Clean and rebuild with fresh dependencies
./gradlew clean build --refresh-dependencies
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Links

- **Website**: [levelessentials.mc.draxon.asia](https://levelessentials.mc.draxon.asia)
- **GitHub**: [DraxonV1/LevelEssentials](https://github.com/DraxonV1/LevelEssentials)
- **Issues**: [Report bugs](https://github.com/DraxonV1/LevelEssentials/issues)

## Credits

Created by DraxonV1

Special thanks to:
- Fabric development team
- Minecraft modding community
- All contributors and testers

## Support

Having issues? Found a bug? Have a feature request?
- Open an issue on [GitHub](https://github.com/DraxonV1/LevelEssentials/issues)
- Check existing issues first
- Provide Minecraft version, mod version, and crash logs if applicable