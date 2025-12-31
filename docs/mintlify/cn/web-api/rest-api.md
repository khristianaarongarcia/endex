---
title: "REST API"
description: "The Endex 公开的 REST API 端点。"
---

以下端点在嵌入式网页服务器上可用。

## 基础 URL

```text
http://<您的IP>:8080/api
```

## 认证

对于受保护的端点，在请求头中提供：

```text
Authorization: Bearer <令牌>
```

令牌在 `config.yml` 的 `web.api.tokens` 数组中配置。

<Warning>
将令牌保密。如果泄露，请立即轮换。
</Warning>

## 端点

### 获取价格

**`GET /api/prices`**

返回所有材料的当前价格。

响应：

```json
{
  "DIAMOND": { "price": 800.00, "change": "+5.25%" },
  "EMERALD": { "price": 500.00, "change": "-2.10%" }
}
```

### 获取价格历史

**`GET /api/prices/:material/history`**

返回材料的价格历史。

响应：

```json
{
  "material": "DIAMOND",
  "history": [
    { "timestamp": 1609459200000, "price": 750.00 },
    { "timestamp": 1609545600000, "price": 800.00 }
  ]
}
```

### 购买/出售

**`POST /api/trade`**

请求：

```json
{
  "action": "buy",
  "material": "DIAMOND",
  "amount": 10
}
```

响应：

```json
{
  "success": true,
  "newBalance": 9200.00,
  "totalCost": 8000.00
}
```

### 获取持仓

**`GET /api/holdings`**

返回已认证玩家的当前持仓。

响应：

```json
{
  "holdings": [
    { "material": "DIAMOND", "amount": 64, "value": 51200.00 },
    { "material": "IRON_INGOT", "amount": 128, "value": 640.00 }
  ],
  "totalValue": 51840.00
}
```

### 管理员：触发事件

**`POST /api/admin/event`**

触发市场事件。需要管理员令牌。

请求：

```json
{
  "event": "ore_rush"
}
```

响应：

```json
{
  "success": true,
  "message": "事件 'ore_rush' 已激活"
}
```

### 管理员：重载

**`POST /api/admin/reload`**

重载插件配置。需要管理员令牌。

响应：

```json
{
  "success": true,
  "message": "配置已重载"
}
```

## 错误响应

所有端点在出错时返回：

```json
{
  "error": true,
  "message": "描述发生了什么"
}
```

常见的 HTTP 状态码：
- `400` — 错误的请求（缺少参数）
- `401` — 未认证
- `403` — 禁止（权限不足）
- `404` — 未找到
- `500` — 服务器错误

## 速率限制

API 端点受速率限制保护。默认：

- **公共端点：** 每分钟 60 次请求
- **已认证端点：** 每分钟 120 次请求
- **管理员端点：** 每分钟 30 次请求

响应头包含：

```text
X-RateLimit-Limit: 60
X-RateLimit-Remaining: 59
X-RateLimit-Reset: 1609459260
```

## WebSocket

除了 REST 端点外，实时更新通过 WebSocket 提供：

```text
ws://<您的IP>:8080/ws
```

消息格式：

```json
{
  "type": "PRICE_UPDATE",
  "data": {
    "material": "DIAMOND",
    "price": 810.00,
    "change": "+1.25%"
  }
}
```

## 示例

### cURL

```bash
# 获取价格
curl http://localhost:8080/api/prices

# 使用认证购买
curl -X POST http://localhost:8080/api/trade \
  -H "Authorization: Bearer your-token" \
  -H "Content-Type: application/json" \
  -d '{"action":"buy","material":"DIAMOND","amount":10}'
```

### JavaScript

```javascript
// 获取价格
const response = await fetch('http://localhost:8080/api/prices');
const prices = await response.json();
console.log(prices.DIAMOND.price);

// WebSocket 实时更新
const ws = new WebSocket('ws://localhost:8080/ws');
ws.onmessage = (event) => {
  const data = JSON.parse(event.data);
  if (data.type === 'PRICE_UPDATE') {
    console.log(`${data.data.material}: $${data.data.price}`);
  }
};
```

---

有关深入的开发者指南，请参阅仓库中的 `docs/DEVELOPERS.md` 或 [开发者 API](../developers/api) 页面。
