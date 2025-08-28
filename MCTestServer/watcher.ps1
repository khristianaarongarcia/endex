# ===== CONFIG =====
$ServerJar   = "purpur.jar"
$Memory      = "2G"
$PluginsPath = "plugins"
$Cooldown    = 10  # seconds between allowed restarts
# ==================

# JVM flags (Aikar optimized)
$JavaArgs = @(
    "-Xms$Memory -Xmx$Memory",
    "-XX:+UseG1GC",
    "-XX:+ParallelRefProcEnabled",
    "-XX:MaxGCPauseMillis=100",
    "-XX:+UnlockExperimentalVMOptions",
    "-XX:+DisableExplicitGC",
    "-XX:+AlwaysPreTouch",
    "-Dusing.aikars.flags=https://mcflags.emc.gs",
    "-Daikars.new.flags=true",
    "-jar $ServerJar nogui"
) -join " "

$LastRestart = Get-Date 0
$ServerProc  = $null

function Start-Server {
    Write-Host "[Watcher] Starting server..."
    $global:ServerProc = Start-Process -PassThru -NoNewWindow -FilePath "java.exe" -ArgumentList $JavaArgs
}

function Stop-Server {
    if ($null -ne $ServerProc -and !$ServerProc.HasExited) {
        Write-Host "[Watcher] Stopping server (Ctrl+C equivalent)..."
        Stop-Process -Id $ServerProc.Id -Force
        Start-Sleep -Seconds 5   # let file locks clear
    }
}

function Restart-Server {
    $Now = Get-Date
    if (($Now - $LastRestart).TotalSeconds -lt $Cooldown) {
        Write-Host "[Watcher] Change detected but still in cooldown, ignoring..."
        return
    }
    $global:LastRestart = $Now

    Stop-Server
    Start-Server
}

# --- Setup FileSystemWatcher ---
$Watcher = New-Object System.IO.FileSystemWatcher
$Watcher.Path = (Resolve-Path $PluginsPath)
$Watcher.Filter = "*.jar"
$Watcher.IncludeSubdirectories = $false
$Watcher.EnableRaisingEvents = $true

Register-ObjectEvent $Watcher Changed -Action { Restart-Server }
Register-ObjectEvent $Watcher Created -Action { Restart-Server }
Register-ObjectEvent $Watcher Deleted -Action { Restart-Server }
Register-ObjectEvent $Watcher Renamed -Action { Restart-Server }

# --- Start server immediately on script launch ---
Start-Server

# Keep script alive
while ($true) { Start-Sleep 5 }
