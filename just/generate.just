open-api-get:
    curl http://localhost/v3/api-docs -o openapi.json
generate-ts:
    docker run --rm --user "$(id -u):$(id -g)" -v "$PWD:/app" -w /app ghcr.io/orval-labs/orval:8.21.0 --config /app/config/openApi/orval.config.ts