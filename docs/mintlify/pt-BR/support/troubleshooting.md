---
title: "Solução de Problemas"
description: "Resolvendo problemas comuns"
---

# Solução de Problemas

Se você encontrar problemas com o The Endex, siga estes passos de diagnóstico.

## Problemas Comuns

### O plugin não inicia (Vermelho no /plugins)
- **Causa**: Dependência faltando.
- **Solução**: Certifique-se de ter **Vault** e um plugin de economia (EssentialsX) instalados. Verifique os logs de inicialização para "Dependency not found".

### "An internal error occurred..." ao executar comandos
- **Causa**: Bug no plugin ou configuração corrompida.
- **Solução**:
  1. Verifique o console para o "Stack Trace" (bloco de texto de erro).
  2. Valide seus arquivos YAML (config, items) em um validador YAML online. Um simples erro de indentação pode quebrar tudo.
  3. Se o erro persistir, reporte no Discord ou GitHub.

### Preços não mudam
- **Causa**: Volatilidade muito baixa ou nenhuma negociação.
- **Solução**:
  - Verifique se `volatility` no `items.yml` é > 0.
  - Certifique-se de que o mercado não está "Fechado" ou "Congelado" (se esse recurso estiver ativado).
  - Faça algumas transações de teste para ver se algo se move.

### Painel Web mostra "Disconnected"
- **Causa**: Problema de WebSocket ou porta.
- **Solução**:
  - Verifique se a porta está aberta (TCP).
  - Se estiver usando um proxy (Nginx/Apache), certifique-se de que o suporte a WebSocket está configurado.
  - Verifique a URL configurada no `config.yml`.

## Obtendo Ajuda

Se você não conseguir resolver aqui:

1. **Logs**: Copie os logs relevantes do console.
2. **Config**: Tenha uma cópia dos seus arquivos de configuração pronta (remova senhas).
3. **Contato**: Junte-se ao nosso Discord de suporte ou abra uma issue no GitHub.

Por favor, forneça o máximo de detalhes possível para que possamos ajudar!
