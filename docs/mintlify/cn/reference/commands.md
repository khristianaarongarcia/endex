---
title: "命令"
description: "The Endex 的玩家和管理员命令。"
---

此页面列出了 The Endex 公开的常用命令。

## 玩家命令

```text
/endex
/endex market
/market buy <material> <amount>
/market sell <material> <amount>
/market price <material>
/market top
/market invest buy <amount>
/market invest list
/market invest redeem-all
```

## 管理员命令

权限通常需要 `theendex.admin`。

```text
/endex reload
/endex version
/endex track dump
/market event list
/market event <name>
/market event end <name>
/market event clear
```

## 注意事项

- 大多数命令支持材料的 Tab 补全。
- 编辑 `config.yml`、`events.yml` 或市场数据文件后，使用 `/endex reload`。

## 插件扩展

插件扩展命令通常注册在：

```text
/endex <addon> <subcommand> [args]
```

示例（加密货币扩展）：

```text
/endex crypto help
/endex crypto buy <amount>
/endex crypto sell <amount>
```
