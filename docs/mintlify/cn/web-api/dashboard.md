---
title: "网页仪表盘"
description: "访问和使用 The Endex 网页仪表盘。"
---

网页仪表盘是一个 React 驱动的网页 UI，实时连接到您的 Minecraft 服务器。

## 网址

启用后，可通过以下地址访问：

```text
http://<您的IP>:8080
```

默认端口为 **8080**。

## 功能

- 实时价格表
- 价格图表（使用 Recharts）
- 无需游戏内 GUI 即可进行交易
- 管理员面板（重载、事件触发）

## 玩家链接

玩家可以使用以下命令将其 Minecraft 账户链接到网页：

```text
/endex link
```

这会生成一个一次性代码，在浏览器中输入以验证其身份。

## 管理员角色

在 `config.yml` 中，您可以设置 Discord OAuth、基于令牌的认证等——有关详细信息，请参阅下一节的 REST API 和仓库中的 `docs/DEVELOPERS.md`。

## 自定义

仪表盘的整体外观可以主题化——颜色、标志等。高级用户可以将导出的默认包放入 `webui/` 并覆盖他们想要的任何内容（详情请参阅 `docs/CUSTOM_WEBUI.md`）。

## 配置

网页设置在 `config.yml` 中控制：

```yaml
web:
  enabled: true
  port: 8080
  host: "0.0.0.0"
  cors-origins:
    - "*"
  api:
    tokens: []
    token-hashes: []
```

## 安全

<Warning>
**生产环境使用**  
对于生产环境部署，请在网页服务器前面配置反向代理（Nginx、Caddy）。
</Warning>

有关安全最佳实践，请参阅 [REST API](rest-api) 文档和仓库中的 `docs/SECURITY.md`。
