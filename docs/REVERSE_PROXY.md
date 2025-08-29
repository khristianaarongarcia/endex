# Reverse Proxy: HTTPS and TLS

Below are example configs to run The Endex web UI behind a reverse proxy with HTTPS.

## Nginx (recommended)

server {
  listen 80;
  server_name your.domain.example;
  return 301 https://$host$request_uri;
}

server {
  listen 443 ssl http2;
  server_name your.domain.example;

  ssl_certificate     /etc/letsencrypt/live/your.domain.example/fullchain.pem;
  ssl_certificate_key /etc/letsencrypt/live/your.domain.example/privkey.pem;

  location / {
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
    proxy_pass http://127.0.0.1:3434;

    # WebSocket upgrade
    proxy_http_version 1.1;
    proxy_set_header Upgrade $http_upgrade;
    proxy_set_header Connection "upgrade";
  }
}

## Caddy (simple)

your.domain.example {
  reverse_proxy 127.0.0.1:3434
}

Notes:
- Ensure `web.websocket.enabled` remains true to allow WS through the proxy.
- Consider adding Basic Auth or OIDC in the proxy for extra protection if needed.
