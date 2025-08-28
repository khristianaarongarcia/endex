@echo off
title Purpur Server Watcher
echo [Watcher] Starting Purpur with plugin change detection...
powershell -NoExit -ExecutionPolicy Bypass -File watcher.ps1
