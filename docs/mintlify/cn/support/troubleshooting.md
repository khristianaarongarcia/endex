---
title: "故障排除"
description: "常见问题及其解决方案。"
---

本页面列出了常见问题及其解决方案。

## 安装问题

### 插件无法加载

**症状：** 插件在启动时不出现在 `/plugins` 列表中。

**解决方案：**

1. 检查控制台是否有错误消息
2. 验证 Java 版本（需要 Java 17+）
3. 确保 Vault 已安装
4. 确保有经济插件（EssentialsX、CMI 等）

```text
[错误] Could not load 'plugins/TheEndex.jar'
```

常见原因：
- JAR 文件损坏 — 重新下载
- 缺少依赖 — 安装 Vault
- Java 版本错误 — 升级到 Java 17

### Vault 未找到

**症状：** 控制台显示 "Vault not found"

**解决方案：**

1. 从 SpigotMC 下载 Vault
2. 放入 `plugins/` 文件夹
3. 重启服务器
4. 安装经济提供者（如 EssentialsX）

---

## 价格问题

### 价格不变化

**症状：** 价格保持不变，即使有交易。

**解决方案：**

1. 检查 `config.yml` 中的 `update-interval-seconds`
2. 确保 `price-sensitivity` 不为 0
3. 检查是否有活跃事件覆盖价格
4. 验证交易正在记录

```yaml
# 确保这些值正确
update-interval-seconds: 60
price-sensitivity: 0.05
```

### 价格变化太快/太慢

**症状：** 价格波动不符合预期。

**解决方案：**

调整 `config.yml` 中的敏感度：

```yaml
# 更稳定的价格
price-sensitivity: 0.02

# 更波动的价格
price-sensitivity: 0.10
```

---

## 网页仪表盘问题

### 无法访问仪表盘

**症状：** 浏览器显示 "无法连接" 或 "拒绝连接"。

**解决方案：**

1. 确保 `web.enabled: true` 在 `config.yml` 中
2. 检查端口是否正确（默认 8080）
3. 检查防火墙设置
4. 验证没有其他程序使用该端口

```bash
# Windows - 检查端口
netstat -ano | findstr :8080

# Linux - 检查端口
sudo lsof -i :8080
```

### WebSocket 断开连接

**症状：** 实时更新停止工作。

**解决方案：**

1. 检查网络连接
2. 查看浏览器控制台是否有错误
3. 如果使用反向代理，确保 WebSocket 支持已启用
4. 重新加载页面

### 玩家链接失败

**症状：** `/endex link` 代码不工作。

**解决方案：**

1. 代码有效期只有 5 分钟
2. 确保玩家有 `theendex.web.link` 权限
3. 验证网页服务器正在运行
4. 清除浏览器缓存

---

## 持仓问题

### 物品丢失

**症状：** 玩家报告持仓中的物品消失。

**解决方案：**

1. 检查 `data/` 文件夹中的备份
2. 验证服务器正常关闭（无崩溃）
3. 检查 `autosave-minutes` 设置
4. 启用 `save-on-each-update: true`

### 配送不工作

**症状：** 玩家无法领取购买的物品。

**解决方案：**

1. 确保玩家有 `theendex.delivery.use` 权限
2. 检查玩家背包是否有空间
3. 验证配送系统已启用
4. 使用 `/market delivery claim` 手动领取

---

## 性能问题

### 服务器延迟

**症状：** 安装插件后 TPS 下降。

**解决方案：**

1. 增加 `update-interval-seconds`（例如 120）
2. 减少 `history-length`
3. 使用 SQLite 存储代替 flat-file
4. 使用 Spark 或 Timings 分析

```yaml
# 性能优化配置
update-interval-seconds: 120
history-length: 3
save-on-each-update: false
autosave-minutes: 10
```

### 内存使用高

**症状：** 插件使用过多内存。

**解决方案：**

1. 减少市场中的物品数量
2. 减少 `history-length`
3. 定期重启服务器
4. 增加服务器内存分配

---

## 权限问题

### 命令不工作

**症状：** 玩家无法使用命令。

**解决方案：**

1. 验证权限节点正确
2. 检查权限插件配置
3. 使用 LuckPerms 的 `/lp user <玩家> permission check theendex.use`
4. 确保权限继承正确

```yaml
# LuckPerms 示例
theendex.use: true
theendex.invest: true
theendex.web.trade: true
```

---

## 调试

### 启用调试模式

在 `config.yml` 中：

```yaml
debug: true
```

这将输出更详细的日志到控制台。

### 收集信息

提交问题时，请提供：

1. 服务器版本（`/version`）
2. 插件版本（`/endex version`）
3. 控制台错误日志
4. `config.yml` 的相关部分
5. 重现步骤

---

## 获取帮助

如果这些解决方案不起作用：

<CardGroup cols={2}>
  <Card title="Discord" icon="discord" href="https://discord.gg/theendex">
    加入社区获取实时帮助
  </Card>
  <Card title="GitHub Issues" icon="github" href="https://github.com/KAG-Apparatus/The-Endex/issues">
    提交错误报告
  </Card>
</CardGroup>
