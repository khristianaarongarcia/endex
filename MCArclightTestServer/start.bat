@echo off
title Minecraft Server - Purpur

:: ============================================
:: Aikar's Optimized JVM Flags for Minecraft
:: https://docs.papermc.io/paper/aikars-flags
:: ============================================

:: Set your memory allocation (adjust based on your system)
:: Recommended: 4G-10G for most servers
set MIN_RAM=2G
set MAX_RAM=4G

:: Java executable (use "java" if Java is in PATH, or specify full path)
set JAVA=java

:: Server JAR file
set SERVER_JAR=arclight.jar

:: ============================================
:: Aikar's Flags - Optimized for Minecraft
:: ============================================

%JAVA% -Xms%MIN_RAM% -Xmx%MAX_RAM% ^
    -XX:+UseG1GC ^
    -XX:+ParallelRefProcEnabled ^
    -XX:MaxGCPauseMillis=200 ^
    -XX:+UnlockExperimentalVMOptions ^
    -XX:+DisableExplicitGC ^
    -XX:+AlwaysPreTouch ^
    -XX:G1NewSizePercent=30 ^
    -XX:G1MaxNewSizePercent=40 ^
    -XX:G1HeapRegionSize=8M ^
    -XX:G1ReservePercent=20 ^
    -XX:G1HeapWastePercent=5 ^
    -XX:G1MixedGCCountTarget=4 ^
    -XX:InitiatingHeapOccupancyPercent=15 ^
    -XX:G1MixedGCLiveThresholdPercent=90 ^
    -XX:G1RSetUpdatingPauseTimePercent=5 ^
    -XX:SurvivorRatio=32 ^
    -XX:+PerfDisableSharedMem ^
    -XX:MaxTenuringThreshold=1 ^
    -Dusing.aikars.flags=https://mcflags.emc.gs ^
    -Daikars.new.flags=true ^
    -jar %SERVER_JAR% --nogui

:: Keep window open if server crashes
pause
