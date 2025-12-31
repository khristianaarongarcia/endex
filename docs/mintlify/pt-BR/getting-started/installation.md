---
title: "Instalação"
description: "Como instalar o The Endex no seu servidor"
---

# Guia de Instalação

Siga estes passos para instalar o The Endex no seu servidor Minecraft.

## Pré-requisitos

Antes de começar, certifique-se de ter:

- Um servidor Minecraft rodando **Spigot**, **Paper** ou **Purpur** (versões 1.16 - 1.21+).
- **Java 17** ou superior instalado.
- Um plugin de economia compatível com **Vault** (ex: EssentialsX).
- **Vault** instalado.
- **PlaceholderAPI** (opcional, mas recomendado para placeholders).

## Passos de Instalação

<Steps>
  <Step title="Baixar o Plugin">
    Baixe a versão mais recente do `TheEndex.jar` no [SpigotMC](https://www.spigotmc.org/) ou [Modrinth](https://modrinth.com/).
  </Step>
  <Step title="Instalar o Arquivo">
    Coloque o arquivo `.jar` baixado na pasta `plugins` do seu servidor.
  </Step>
  <Step title="Reiniciar o Servidor">
    Reinicie seu servidor para carregar o plugin. Evite usar `/reload`.
  </Step>
  <Step title="Verificar Instalação">
    Verifique o console para a mensagem de inicialização do The Endex ou digite `/plugins` no jogo para ver se o The Endex aparece em verde.
  </Step>
</Steps>

## Configuração Inicial

Após a primeira execução, uma pasta `TheEndex` será criada no seu diretório `plugins`. Ela contém:

- `config.yml`: Arquivo de configuração principal.
- `messages.yml`: Arquivo de tradução de mensagens do jogo.
- `items.yml`: Configuração de itens e preços base.

Consulte a seção de [Configuração](/pt-BR/reference/configuration) para mais detalhes sobre como personalizar sua instalação.
