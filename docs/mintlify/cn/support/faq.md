---
title: "常见问题"
description: "关于 The Endex 的常见问题。"
---

## 一般问题

<AccordionGroup>

<Accordion title="什么是 The Endex？">
The Endex 是一个用于 Minecraft 的动态市场插件，提供实时价格基于供需变化、虚拟持仓、投资和网页仪表盘。
</Accordion>

<Accordion title="它支持哪些 Minecraft 版本？">
The Endex 支持 Minecraft 1.16.5 至 1.21.x（包括 Paper、Spigot 和 Purpur）。
</Accordion>

<Accordion title="它免费吗？">
The Endex 提供免费版和高级版。免费版包含核心市场功能，高级版添加高级功能如网页仪表盘、投资和 API 访问。
</Accordion>

<Accordion title="我在哪里可以获得支持？">
加入我们的 [Discord 服务器](https://discord.gg/theendex) 获取社区支持，或在 SpigotMC 页面上提交问题。
</Accordion>

</AccordionGroup>

## 技术问题

<AccordionGroup>

<Accordion title="价格多久更新一次？">
默认情况下，价格每 60 秒更新一次。您可以在 `config.yml` 中配置更新间隔。
</Accordion>

<Accordion title="如何重置所有价格？">
删除 `plugins/TheEndex/data/` 文件夹并重新加载插件。这将重置所有市场数据。
</Accordion>

<Accordion title="我可以使用 MySQL 代替 SQLite 吗？">
目前，The Endex 支持 flat-file 和 SQLite 存储。MySQL 支持计划在未来版本中添加。
</Accordion>

<Accordion title="如何备份市场数据？">
复制整个 `plugins/TheEndex/` 文件夹。数据存储在 `data/` 子目录中。
</Accordion>

</AccordionGroup>

## 经济问题

<AccordionGroup>

<Accordion title="它如何与 Vault 配合使用？">
The Endex 使用 Vault 进行货币交易。所有买卖操作都通过您配置的经济插件（如 EssentialsX）。
</Accordion>

<Accordion title="我可以设置最高/最低价格吗？">
是的！在 `items.yml` 中为每种材料配置 `min-price` 和 `max-price`。
</Accordion>

<Accordion title="价格可以变为负数吗？">
不可以。价格有最低限制（默认 0.01）以防止负数或零价格。
</Accordion>

</AccordionGroup>

## 网页仪表盘问题

<AccordionGroup>

<Accordion title="如何访问网页仪表盘？">
确保 `config.yml` 中 `web.enabled: true`，然后访问 `http://您的IP:8080`。
</Accordion>

<Accordion title="如何保护仪表盘？">
使用反向代理（Nginx、Caddy）添加 HTTPS 和认证。参见 `docs/SECURITY.md`。
</Accordion>

<Accordion title="玩家如何链接账户？">
玩家使用 `/endex link` 生成一次性代码，然后在网页仪表盘中输入。
</Accordion>

</AccordionGroup>

## 故障排除

<AccordionGroup>

<Accordion title="插件无法启动">
检查控制台错误。常见问题包括：
- 缺少 Vault
- 缺少经济插件
- Java 版本不兼容
</Accordion>

<Accordion title="价格不变化">
确保：
- `update-interval-seconds` 设置正确
- 有玩家在进行交易
- 没有事件覆盖价格
</Accordion>

<Accordion title="网页仪表盘无法访问">
检查：
- `web.enabled` 为 true
- 端口没有被防火墙阻止
- 没有其他程序使用该端口
</Accordion>

</AccordionGroup>

## 更多帮助

如果您的问题没有在这里列出，请：

1. 查看 [故障排除](troubleshooting) 页面
2. 搜索 Discord 服务器
3. 提交 GitHub issue

<Card title="Discord 支持" icon="discord" href="https://discord.gg/theendex">
  加入我们的社区获取帮助
</Card>
