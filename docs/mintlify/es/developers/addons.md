---
title: "Crear Complementos"
description: "Crear complementos personalizados para The Endex."
---

The Endex soporta la creación de complementos personalizados a través de un sistema modular simple.

## Estructura del Complemento

Un complemento típico incluye:

```
MiComplemento/
├── build.gradle.kts
├── src/main/kotlin/
│   └── com/example/micomplemento/
│       └── MiComplemento.kt
└── src/main/resources/
    └── addon.yml
```

## addon.yml

```yaml
name: MiComplemento
version: 1.0.0
main: com.example.micomplemento.MiComplemento
api-version: 1.5.0
description: Mi complemento personalizado
author: TuNombre
```

## Clase Principal

```kotlin
import tech.kagsystems.theendex.addons.Addon
import tech.kagsystems.theendex.addons.AddonInfo

@AddonInfo(
    name = "MiComplemento",
    version = "1.0.0",
    description = "Mi complemento personalizado"
)
class MiComplemento : Addon() {
    
    override fun onEnable() {
        logger.info("¡MiComplemento habilitado!")
    }
    
    override fun onDisable() {
        logger.info("¡MiComplemento deshabilitado!")
    }
}
```

## Usar la API

```kotlin
import tech.kagsystems.theendex.api.TheEndexAPI

class MiComplemento : Addon() {
    
    override fun onEnable() {
        val api = TheEndexAPI.getInstance()
        
        // Escuchar cambios de precio
        api.onPriceChange { material, oldPrice, newPrice ->
            logger.info("$material: $oldPrice -> $newPrice")
        }
    }
}
```

## Registrar Comandos

```kotlin
override fun onEnable() {
    registerCommand("micomplemento") { sender, args ->
        sender.sendMessage("¡Hola desde MiComplemento!")
        true
    }
}
```

## Registrar Configuración

```kotlin
override fun onEnable() {
    saveDefaultConfig()
    
    val miValor = config.getString("mi-clave", "predeterminado")
}
```

## Instalación

Coloca el JAR compilado en:

```text
plugins/TheEndex/addons/
```

Recarga The Endex:

```text
/endex reload
```

## Complementos de Ejemplo

Revisa `addons/sample-addon` en el repositorio para un ejemplo completo.

El **Complemento Crypto** (`addons/crypto`) es un ejemplo más avanzado que añade:

- Sistema de moneda virtual
- Mecánicas de minería
- GUI personalizada
- Integración WebSocket

Para una guía detallada de desarrollo de complementos, consulta `docs/ADDONS.md` en el repositorio.
