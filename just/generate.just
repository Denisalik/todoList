open-api-get:
    curl http://localhost/v3/api-docs -o openapi.json
generate-ts:
    docker run --rm -v "$(PWD):/app" -w /app ghcr.io/orval-labs/orval:8.21.0 --config /app/config/openApi/orval.config.ts
generate-ts-open-api:
    docker run --rm -v ${PWD}:/local openapitools/openapi-generator-cli generate \
      -i /local/openapi.json \
      -g typescript-fetch \
      -o /local/frontend/src/generated/client \
      --additional-properties=npmName=my-api-client,supportsES6=true