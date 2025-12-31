param(
    [string]$JarPath = "release\TheEndex-1.5.6-dec0759-spigot.jar",
    [string]$ServiceEntry = "META-INF/services/org.eclipse.jetty.websocket.core.Extension"
)

Add-Type -AssemblyName System.IO.Compression.FileSystem

$src = Resolve-Path $JarPath
$dst = "$($src).tmp"

$srcZ = [System.IO.Compression.ZipFile]::OpenRead($src)
$dstZ = [System.IO.Compression.ZipFile]::Open($dst, [System.IO.Compression.ZipArchiveMode]::Create)

foreach ($entry in $srcZ.Entries) {
    if ($entry.FullName -ieq $ServiceEntry) { continue }
    $dstEntry = $dstZ.CreateEntry($entry.FullName)
    $srcStream = $entry.Open()
    $dstStream = $dstEntry.Open()
    $srcStream.CopyTo($dstStream)
    $srcStream.Dispose()
    $dstStream.Dispose()
}

$srcZ.Dispose()
$dstZ.Dispose()

Move-Item -Force $dst $src
Write-Host "Removed $ServiceEntry from $JarPath"