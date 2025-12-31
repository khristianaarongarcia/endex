---
title: "Instalación"
description: "Cómo instalar The Endex en tu servidor de Minecraft."
---

## Requisitos

| Requisito | Versión |
|-----------|---------|
| Minecraft Server | 1.16.5 – 1.21.x |
| Java | 17 o superior |
| Vault | Última |
| Plugin de Economía | EssentialsX, CMI, etc. |

## Instalación

<Steps>
  <Step title="Descargar">
    Descarga el último JAR de The Endex desde [SpigotMC](https://www.spigotmc.org/resources/the-endex.XXXXX/) o [Modrinth](https://modrinth.com/plugin/the-endex).
  </Step>
  <Step title="Instalar Vault">
    Si no lo has hecho, descarga e instala [Vault](https://www.spigotmc.org/resources/vault.34315/).
  </Step>
  <Step title="Instalar Plugin de Economía">
    The Endex requiere un proveedor de economía. Recomendamos [EssentialsX](https://essentialsx.net/).
  </Step>
  <Step title="Colocar JAR">
    Coloca `TheEndex.jar` en tu carpeta `plugins/`.
  </Step>
  <Step title="Reiniciar">
    Reinicia tu servidor.
  </Step>
  <Step title="Configurar">
    Edita `plugins/TheEndex/config.yml` según tus necesidades.
  </Step>
</Steps>

## Verificar Instalación

Ejecuta `/endex version` para verificar que el plugin esté cargado.

```text
/endex version
```

Deberías ver:
```
The Endex v1.5.7
Autor: KAG Systems
Estado: Funcionando
```

## Estructura de Archivos

Después de la instalación, encontrarás:

```text
plugins/
└── TheEndex/
    ├── config.yml      # Configuración principal
    ├── items.yml       # Configuración del mercado
    ├── events.yml      # Eventos del mercado
    ├── commands.yml    # Alias de comandos
    ├── guis/           # Configuración de GUI
    │   ├── market.yml
    │   ├── details.yml
    │   ├── holdings.yml
    │   └── deliveries.yml
    ├── data/           # Datos del mercado
    └── addons/         # Extensiones del plugin
```

## Próximos Pasos

<CardGroup cols={2}>
  <Card title="Inicio Rápido" icon="rocket" href="quick-start">
    Aprende lo básico rápidamente
  </Card>
  <Card title="Configuración" icon="gear" href="../reference/configuration">
    Opciones de configuración completas
  </Card>
</CardGroup>
