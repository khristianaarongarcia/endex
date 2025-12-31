---
title: "Painel Web"
description: "Interface web em tempo real para sua economia"
---

# Painel Web

O The Endex inclui um servidor web embutido que fornece um painel bonito e responsivo para os jogadores acompanharem a economia fora do jogo.

## Recursos

- **Dados em Tempo Real**: Preços atualizados instantaneamente via WebSockets.
- **Gráficos Históricos**: Visualize tendências de preços nas últimas 24h, 7d ou 30d.
- **Maiores Ganhos/Perdas**: Veja rapidamente quais itens estão com melhor/pior desempenho.
- **Responsivo**: Funciona perfeitamente em dispositivos móveis e desktops.

## Configuração

Para ativar o painel, edite seu `config.yml`:

```yaml
web:
  enabled: true
  port: 8080 # Porta do servidor web
  host: "0.0.0.0" # Ouvir em todas as interfaces
  public-url: "http://seu-servidor.com:8080" # URL para links
```

### Rede

Se você estiver hospedando em casa, certifique-se de que a porta `8080` (ou a que você escolheu) esteja aberta no seu firewall e encaminhada (port forwarded).

## Acesso

Uma vez ativado e o servidor reiniciado, acesse o painel no seu navegador:

`http://<IP do Servidor>:8080`

## Personalização

Você pode personalizar a aparência do painel editando os arquivos HTML/CSS/JS dentro da pasta `plugins/TheEndex/web/`.

- `index.html`: Página principal.
- `css/style.css`: Estilos.
- `js/app.js`: Lógica frontend.

*Nota: Conhecimento básico de desenvolvimento web é recomendado para personalização avançada.*
