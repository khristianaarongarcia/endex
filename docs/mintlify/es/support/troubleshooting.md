---
title: "Solución de Problemas"
description: "Problemas comunes y sus soluciones."
---

Esta página lista problemas comunes y sus soluciones.

## Problemas de Instalación

### El Plugin No Carga

**Síntoma:** El plugin no aparece en la lista de `/plugins` al iniciar.

**Soluciones:**

1. Revisa la consola por mensajes de error
2. Verifica la versión de Java (requiere Java 17+)
3. Asegúrate de que Vault esté instalado
4. Asegúrate de que haya un plugin de economía (EssentialsX, CMI, etc.)

```text
[ERROR] Could not load 'plugins/TheEndex.jar'
```

Causas comunes:
- Archivo JAR corrupto — vuelve a descargar
- Dependencia faltante — instala Vault
- Versión de Java incorrecta — actualiza a Java 17

### Vault No Encontrado

**Síntoma:** La consola muestra "Vault not found"

**Soluciones:**

1. Descarga Vault de SpigotMC
2. Colócalo en la carpeta `plugins/`
3. Reinicia el servidor
4. Instala un proveedor de economía (como EssentialsX)

---

## Problemas de Precios

### Los Precios No Cambian

**Síntoma:** Los precios permanecen igual incluso con transacciones.

**Soluciones:**

1. Revisa `update-interval-seconds` en `config.yml`
2. Asegúrate de que `price-sensitivity` no sea 0
3. Verifica si hay eventos activos sobrescribiendo precios
4. Verifica que las transacciones se están registrando

```yaml
# Asegúrate de que estos valores estén correctos
update-interval-seconds: 60
price-sensitivity: 0.05
```

### Los Precios Cambian Muy Rápido/Lento

**Síntoma:** Las fluctuaciones de precios no coinciden con las expectativas.

**Soluciones:**

Ajusta la sensibilidad en `config.yml`:

```yaml
# Precios más estables
price-sensitivity: 0.02

# Precios más volátiles
price-sensitivity: 0.10
```

---

## Problemas del Panel Web

### No Puedo Acceder al Panel

**Síntoma:** El navegador muestra "No se puede conectar" o "Conexión rechazada".

**Soluciones:**

1. Asegúrate de que `web.enabled: true` en `config.yml`
2. Verifica que el puerto sea correcto (predeterminado 8080)
3. Revisa la configuración del firewall
4. Verifica que ningún otro programa esté usando el puerto

```bash
# Windows - verificar puerto
netstat -ano | findstr :8080

# Linux - verificar puerto
sudo lsof -i :8080
```

### WebSocket Se Desconecta

**Síntoma:** Las actualizaciones en tiempo real dejan de funcionar.

**Soluciones:**

1. Revisa la conexión de red
2. Mira la consola del navegador por errores
3. Si usas un proxy inverso, asegúrate de que el soporte de WebSocket esté habilitado
4. Recarga la página

### La Vinculación del Jugador Falla

**Síntoma:** El código de `/endex link` no funciona.

**Soluciones:**

1. Los códigos solo son válidos por 5 minutos
2. Asegúrate de que el jugador tenga el permiso `theendex.web.link`
3. Verifica que el servidor web esté funcionando
4. Limpia la caché del navegador

---

## Problemas de Tenencias

### Objetos Perdidos

**Síntoma:** Los jugadores reportan que objetos de sus tenencias desaparecen.

**Soluciones:**

1. Revisa los backups en la carpeta `data/`
2. Verifica que el servidor se apagó correctamente (sin crash)
3. Revisa la configuración de `autosave-minutes`
4. Habilita `save-on-each-update: true`

### Las Entregas No Funcionan

**Síntoma:** Los jugadores no pueden reclamar objetos comprados.

**Soluciones:**

1. Asegúrate de que el jugador tenga el permiso `theendex.delivery.use`
2. Verifica que el inventario del jugador tenga espacio
3. Verifica que el sistema de entregas esté habilitado
4. Usa `/market delivery claim` para reclamar manualmente

---

## Problemas de Rendimiento

### Lag del Servidor

**Síntoma:** TPS cae después de instalar el plugin.

**Soluciones:**

1. Aumenta `update-interval-seconds` (por ejemplo, 120)
2. Reduce `history-length`
3. Usa almacenamiento SQLite en lugar de flat-file
4. Perfila usando Spark o Timings

```yaml
# Configuración optimizada para rendimiento
update-interval-seconds: 120
history-length: 3
save-on-each-update: false
autosave-minutes: 10
```

### Alto Uso de Memoria

**Síntoma:** El plugin usa demasiada memoria.

**Soluciones:**

1. Reduce el número de objetos en el mercado
2. Reduce `history-length`
3. Reinicia el servidor periódicamente
4. Aumenta la asignación de memoria del servidor

---

## Problemas de Permisos

### Los Comandos No Funcionan

**Síntoma:** Los jugadores no pueden usar comandos.

**Soluciones:**

1. Verifica que los nodos de permiso sean correctos
2. Revisa la configuración del plugin de permisos
3. Usa `/lp user <jugador> permission check theendex.use` de LuckPerms
4. Asegúrate de que la herencia de permisos esté correcta

```yaml
# Ejemplo de LuckPerms
theendex.use: true
theendex.invest: true
theendex.web.trade: true
```

---

## Depuración

### Habilitar Modo Debug

En `config.yml`:

```yaml
debug: true
```

Esto mostrará logs más detallados en la consola.

### Recopilar Información

Al reportar un problema, proporciona:

1. Versión del servidor (`/version`)
2. Versión del plugin (`/endex version`)
3. Logs de errores de la consola
4. Secciones relevantes de `config.yml`
5. Pasos para reproducir

---

## Obtener Ayuda

Si estas soluciones no funcionan:

<CardGroup cols={2}>
  <Card title="Discord" icon="discord" href="https://discord.gg/theendex">
    Únete a la comunidad para ayuda en tiempo real
  </Card>
  <Card title="GitHub Issues" icon="github" href="https://github.com/KAG-Apparatus/The-Endex/issues">
    Reportar errores
  </Card>
</CardGroup>
