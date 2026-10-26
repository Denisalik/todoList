openapi_url := "http://localhost:8080/v3/api-docs"
openapi_file := "openapi.json"

open-api-get:
    rm -f {{openapi_file}}
    curl -f {{openapi_url}} -o {{openapi_file}}

generate-ts:
    rm -rf ./frontend/src/generated
    docker run --rm --user "$(id -u):$(id -g)" -v "$PWD:/app" -w /app ghcr.io/orval-labs/orval:8.21.0 --config /app/config/openApi/orval.config.ts

generate: open-api-get generate-ts