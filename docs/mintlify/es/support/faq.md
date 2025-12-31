---
title: "Preguntas Frecuentes"
description: "Preguntas frecuentes sobre The Endex."
---

## Preguntas Generales

<AccordionGroup>

<Accordion title="¿Qué es The Endex?">
The Endex es un plugin de mercado dinámico para Minecraft que proporciona precios en tiempo real basados en oferta y demanda, tenencias virtuales, inversiones y un panel web.
</Accordion>

<Accordion title="¿Qué versiones de Minecraft soporta?">
The Endex soporta Minecraft 1.16.5 a 1.21.x (incluyendo Paper, Spigot y Purpur).
</Accordion>

<Accordion title="¿Es gratis?">
The Endex ofrece una versión gratuita y una versión premium. La versión gratuita incluye las funciones básicas del mercado, mientras que la versión premium añade funciones avanzadas como el panel web, inversiones y acceso a la API.
</Accordion>

<Accordion title="¿Dónde puedo obtener soporte?">
Únete a nuestro [servidor de Discord](https://discord.gg/theendex) para soporte de la comunidad, o reporta problemas en la página de SpigotMC.
</Accordion>

</AccordionGroup>

## Preguntas Técnicas

<AccordionGroup>

<Accordion title="¿Con qué frecuencia se actualizan los precios?">
Por defecto, los precios se actualizan cada 60 segundos. Puedes configurar el intervalo de actualización en `config.yml`.
</Accordion>

<Accordion title="¿Cómo reinicio todos los precios?">
Elimina la carpeta `plugins/TheEndex/data/` y recarga el plugin. Esto reiniciará todos los datos del mercado.
</Accordion>

<Accordion title="¿Puedo usar MySQL en lugar de SQLite?">
Actualmente, The Endex soporta almacenamiento flat-file y SQLite. El soporte para MySQL está planeado para una versión futura.
</Accordion>

<Accordion title="¿Cómo hago backup de los datos del mercado?">
Copia toda la carpeta `plugins/TheEndex/`. Los datos se almacenan en el subdirectorio `data/`.
</Accordion>

</AccordionGroup>

## Preguntas de Economía

<AccordionGroup>

<Accordion title="¿Cómo funciona con Vault?">
The Endex usa Vault para transacciones de moneda. Todas las operaciones de compra y venta pasan por tu plugin de economía configurado (como EssentialsX).
</Accordion>

<Accordion title="¿Puedo establecer precios mínimos/máximos?">
¡Sí! Configura `min-price` y `max-price` para cada material en `items.yml`.
</Accordion>

<Accordion title="¿Pueden los precios volverse negativos?">
No. Los precios tienen un límite inferior (predeterminado 0.01) para prevenir precios negativos o cero.
</Accordion>

</AccordionGroup>

## Preguntas del Panel Web

<AccordionGroup>

<Accordion title="¿Cómo accedo al panel web?">
Asegúrate de que `web.enabled: true` en `config.yml`, luego visita `http://tu-ip:8080`.
</Accordion>

<Accordion title="¿Cómo aseguro el panel?">
Usa un proxy inverso (Nginx, Caddy) para añadir HTTPS y autenticación. Ver `docs/SECURITY.md`.
</Accordion>

<Accordion title="¿Cómo vinculan los jugadores sus cuentas?">
Los jugadores usan `/endex link` para generar un código de un solo uso, que luego ingresan en el panel web.
</Accordion>

</AccordionGroup>

## Solución de Problemas

<AccordionGroup>

<Accordion title="El plugin no inicia">
Revisa los errores en la consola. Problemas comunes incluyen:
- Vault faltante
- Plugin de economía faltante
- Versión de Java incompatible
</Accordion>

<Accordion title="Los precios no cambian">
Asegúrate de que:
- `update-interval-seconds` está configurado correctamente
- Los jugadores están haciendo transacciones
- No hay eventos sobrescribiendo los precios
</Accordion>

<Accordion title="No puedo acceder al panel web">
Verifica:
- `web.enabled` es true
- El puerto no está bloqueado por firewall
- Ningún otro programa está usando el puerto
</Accordion>

</AccordionGroup>

## Más Ayuda

Si tu pregunta no está listada aquí:

1. Revisa la página de [Solución de Problemas](troubleshooting)
2. Busca en el servidor de Discord
3. Abre un issue en GitHub

<Card title="Soporte en Discord" icon="discord" href="https://discord.gg/theendex">
  Únete a nuestra comunidad para obtener ayuda
</Card>
