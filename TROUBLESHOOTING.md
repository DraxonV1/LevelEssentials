# LevelEssentials - Troubleshooting Guide

## Build Errors

### Error: "Could not create an instance of type net.fabricmc.loom.extension.LoomGradleExtensionImpl"

**Cause:** Incompatible Gradle version. Fabric Loom 1.12 requires Gradle 8.12+.

**Solution:**
```bash
# Update Gradle wrapper
gradle wrapper --gradle-version 8.12

# Verify the update
./gradlew --version

# Clean and rebuild
./gradlew clean build
```

**Alternative Solution:**
If you don't have Gradle installed globally:
```bash
# Download and use the wrapper directly
# On Windows:
gradlew.bat wrapper --gradle-version 8.12

# On Linux/Mac:
./gradlew wrapper --gradle-version 8.12
```

### Error: "Failed to apply plugin 'fabric-loom'"

**Cause:** Incorrect Loom version or repository configuration.

**Solution:**
1. Check `build.gradle` has correct Loom version: `1.12-SNAPSHOT`
2. Verify `settings.gradle` includes Fabric maven repository
3. Clear Gradle cache:
```bash
# Windows
rmdir /s /q %USERPROFILE%\.gradle\caches

# Linux/Mac
rm -rf ~/.gradle/caches

# Rebuild
./gradlew clean build --refresh-dependencies
```

### Error: "Minecraft version X is not supported"

**Cause:** Trying to build for unsupported Minecraft version.

**Solution:**
Ensure `gradle.properties` has:
```properties
minecraft_version=1.21.1
yarn_mappings=1.21.1+build.3
```

The built JAR works on all 1.21.1-1.21.11 versions!

### Error: "Could not resolve dependencies"

**Solution:**
```bash
# Force refresh all dependencies
./gradlew clean build --refresh-dependencies

# If still failing, check internet connection and try:
./gradlew build --stacktrace --debug
```

## Runtime Errors

### Mod Not Loading

**Symptoms:** Mod doesn't appear in Mod Menu, features don't work.

**Diagnostic Steps:**
1. Check `latest.log` in `.minecraft/logs/`
2. Look for errors related to `levelessentials`
3. Verify Fabric Loader and Fabric API versions

**Common Causes:**
- Missing Fabric API
- Wrong Fabric API version for your Minecraft version
- Corrupted JAR file

**Solution:**