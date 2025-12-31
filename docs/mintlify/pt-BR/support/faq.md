---
title: "FAQ"
description: "Perguntas Frequentes"
---

# Perguntas Frequentes (FAQ)

### P: O The Endex substitui o EssentialsX Economy?
**R:** Não. O The Endex não é um plugin de gerenciamento de moeda (não gerencia saldos de jogadores). Ele usa o **Vault** para interagir com seu plugin de economia existente (EssentialsX, CMI, Economy, etc.). Ele atua como um "mercado de ações" em cima da sua economia.

### P: Os preços podem ficar negativos?
**R:** Não. Você pode definir um `min-price` (preço mínimo) na configuração. Por padrão, os preços nunca cairão abaixo de 0.01 (ou o que você definir).

### P: É compatível com a versão 1.8?
**R:** Não. O The Endex foi projetado para versões modernas do Minecraft (1.16+). Versões mais antigas não possuem recursos de API necessários e não são suportadas.

### P: Como faço para redefinir todos os preços?
**R:** Você pode excluir o arquivo `data.yml` (ou banco de dados) enquanto o servidor estiver desligado, ou usar um comando de administrador (se disponível na sua versão) para redefinir para os preços base.

### P: O painel web não está funcionando.
**R:** Verifique:
1. Se `enabled: true` está definido na seção `web` do `config.yml`.
2. Se a porta (padrão 8080) está aberta no seu firewall/roteador.
3. Se nenhum outro serviço está usando essa porta.
4. Verifique o console para erros de inicialização "BindException".

### P: Posso usar itens personalizados?
**R:** Sim, o The Endex tem suporte parcial para itens personalizados via NBT ou nomes específicos, dependendo da configuração. Integração com plugins como Oraxen ou ItemsAdder está planejada ou disponível via addons.

### P: Como evito que jogadores explorem o sistema?
**R:**
- Use limites de preço (`min-price`, `max-price`).
- Ative impostos sobre transações.
- Monitore os logs de transação.
- Ajuste a `volatility` para evitar mudanças muito drásticas.
