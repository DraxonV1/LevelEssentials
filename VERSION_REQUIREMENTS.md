# LevelEssentials - Exact Version Requirements

## ✅ Tested & Verified Versions

### Build Environment
- **Gradle**: 8.12 (Required, minimum 8.12)
- **Java**: 21 (JDK 21)
- **Fabric Loom**: 1.12-SNAPSHOT
- **Build MC Version**: 1.21.1

### Runtime Requirements (Players)
- **Minecraft**: 1.21.1, 1.21.2, 1.21.3, 1.21.4, 1.21.5, 1.21.6, 1.21.7, 1.21.8, 1.21.9, 1.21.10, 1.21.11
- **Fabric Loader**: 0.16.9+ (Latest: 0.18.1)
- **Java**: 21

### Fabric API Versions by Minecraft Version

| Minecraft Version | Fabric API Version | Status |
|-------------------|-------------------|--------|
| 1.21.1 | 0.108.0+1.21.1 | ✅ Stable |
| 1.21.2 | 0.110.0+1.21.2 | ✅ Stable |
| 1.21.3 | 0.111.0+1.21.3 | ✅ Stable |
| 1.21.4 | 0.112.0+1.21.4 | ✅ Stable |
| 1.21.5 | 0.114.0+1.21.5 | ✅ Stable |
| 1.21.6 | 0.117.0+1.21.6 | ✅ Stable |
| 1.21.7 | 0.120.0+1.21.7 | ✅ Stable |
| 1.21.8 | 0.136.1+1.21.8 | ✅ Stable |
| 1.21.9 | 0.134.0+1.21.9 | ✅ Stable |
| 1.21.10 | 0.138.3+1.21.10 | ✅ Stable |
| 1.21.11 | 0.139.5+1.21.11 | ✅ Stable (Latest) |

## Optional Dependencies

| Mod | Version | Purpose |
|-----|---------|---------|
| Mod Menu | 11.0.3+ | Config GUI |
| Cloth Config | 15.0.140+ | Config Screen |

## Build Process

### 1. Clone Repository
```bash
git clone https://github.com/DraxonV1/LevelEssentials.git
cd LevelEssentials
```

### 2. Update Gradle Wrapper (CRITICAL)
```bash
# This is REQUIRED before building
gradle wrapper --gradle-version 8.12

# Verify
./gradlew --version
# Should show: Gradle 8.12
```

### 3. Build
```bash
./gradlew clean build
```

### 4. Output