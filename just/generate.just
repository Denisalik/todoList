open-api-get:
    rm openapi.json
    curl http://localhost/v3/api-docs -o openapi.json
generate-ts:
    rm -rf ./frontend/src/generated
    docker run --rm --user "$(id -u):$(id -g)" -v "$PWD:/app" -w /app ghcr.io/orval-labs/orval:8.21.0 --config /app/config/openApi/orval.config.ts
generate: open-api-get generate-ts