docker-up:
    docker compose up -d

docker-build:
    docker compose build

docker-down:
    docker compose down

docker-watch:
    docker compose watch

docker-logs:
    docker compose logs -f

docker-db:
    docker compose up sql -d

docker: docker-build docker-up

docker-push:
    #!/usr/bin/env sh
    set -euo pipefail

    PACKAGE_JSON="./frontend/package.json"

    VERSION_BACKEND=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

    if command -v jq >/dev/null 2>&1; then
        VERSION_FRONTEND=$(jq -r '.version' "$PACKAGE_JSON")
    elif command -v node >/dev/null 2>&1; then
        VERSION_FRONTEND=$(node -p "require('$PACKAGE_JSON').version")
    elif command -v python3 >/dev/null 2>&1; then
        VERSION_FRONTEND=$(python3 -c "import json; print(json.load(open('$PACKAGE_JSON'))['version'])")
    elif command -v python >/dev/null 2>&1; then
        VERSION_FRONTEND=$(python -c "import json; print(json.load(open('$PACKAGE_JSON'))['version'])")
    else
        echo "Error: No suitable tool found (need jq, node, or python)" >&2
        exit 1
    fi

    docker build --target prod -t denisalik/to-do-list-backend:$VERSION_BACKEND -t denisalik/to-do-list-backend:latest .
    docker build --target prod-without-loki -t denisalik/to-do-list-backend:$VERSION_BACKEND-without-loki .
    docker build --target k8s-production -t denisalik/to-do-list-frontend:$VERSION_FRONTEND -t denisalik/to-do-list-frontend:latest ./frontend
    docker login
    docker push denisalik/to-do-list-backend:$VERSION_BACKEND
    docker push denisalik/to-do-list-backend:$VERSION_BACKEND-without-loki
    docker push denisalik/to-do-list-backend:latest
    docker push denisalik/to-do-list-frontend:$VERSION_FRONTEND
    docker push denisalik/to-do-list-frontend:latest
