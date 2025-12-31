---
title: "Addons"
description: "Estendendo a funcionalidade do The Endex"
---

# Sistema de Addons

O The Endex suporta um sistema de addons modular, permitindo adicionar recursos específicos sem inchar o plugin principal.

## Addons Oficiais

### 1. CryptoAddon
Adiciona criptomoedas virtuais (Bitcoin, Ethereum, etc.) ao mercado.
- **Recursos**: Preços baseados em API real (CoinGecko), carteiras de cripto separadas.
- **Instalação**: Coloque `Endex-Crypto.jar` em `plugins/TheEndex/addons/`.

### 2. DiscordLink
Conecta seu mercado ao seu servidor Discord.
- **Recursos**: Comandos de barra no Discord (`/price`), alertas de colapso em canais, sincronização de cargos baseada em riqueza.

## Criando um Addon

Desenvolvedores podem criar seus próprios addons estendendo a classe `EndexAddon`.

### Estrutura Básica

```java
public class MyCustomAddon extends EndexAddon {
    
    @Override
    public void onEnable() {
        getLogger().info("Meu addon foi ativado!");
    }
    
    @Override
    public void onDisable() {
        // Limpeza
    }
}
```

### Carregamento

Addons devem ser colocados na pasta `plugins/TheEndex/addons/`. Eles são carregados automaticamente na inicialização do plugin principal.

## Marketplace

(Em Breve) Um marketplace comunitário onde você pode baixar addons criados por outros usuários.
