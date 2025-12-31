---
title: "API REST"
description: "Documentação da API HTTP para desenvolvedores"
---

# API REST

O The Endex expõe uma API RESTful para permitir que desenvolvedores externos interajam com os dados do mercado. Isso é útil para criar sites de estatísticas, bots do Discord ou aplicativos móveis.

## Endpoint (URL Base)

`http://<IP do Servidor>:<Porta>/api/v1`

## Endpoints

### Obter Todos os Itens

`GET /items`

Retorna uma lista de todos os itens sendo rastreados.

**Resposta:**
```json
[
  {
    "id": "DIAMOND",
    "name": "Diamante",
    "price": 105.50,
    "basePrice": 100.0,
    "trend": "UP",
    "change": 5.5
  },
  {
    "id": "GOLD_INGOT",
    "name": "Barra de Ouro",
    "price": 48.20,
    "basePrice": 50.0,
    "trend": "DOWN",
    "change": -3.6
  }
]
```

### Obter Item Específico

`GET /items/{id}`

**Exemplo:** `GET /items/DIAMOND`

**Resposta:**
```json
{
  "id": "DIAMOND",
  "name": "Diamante",
  "price": 105.50,
  "history": [
    {"time": 1620000000, "price": 100.0},
    {"time": 1620003600, "price": 102.0},
    {"time": 1620007200, "price": 105.5}
  ]
}
```

### Obter Status do Mercado

`GET /status`

Retorna o status global do mercado (eventos ativos, etc.).

**Resposta:**
```json
{
  "status": "OPEN",
  "activeEvents": [],
  "lastUpdate": 1620007200
}
```

## Autenticação

Atualmente, a API é pública e somente leitura por padrão. Versões futuras incluirão autenticação por chave de API para operações de escrita (POST/PUT).

## Limite de Taxa (Rate Limiting)

Não há limite de taxa codificado, mas por favor use o bom senso e armazene os resultados em cache se estiver fazendo muitas solicitações.
