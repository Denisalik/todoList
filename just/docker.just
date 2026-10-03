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
    docker build -t denisalik/to-do-list-backend:0.0.1 -t denisalik/to-do-list-backend:latest .
    docker build -t denisalik/to-do-list-frontend:0.0.1 -t denisalik/to-do-list-frontend:latest ./frontend
    docker login
    docker push denisalik/to-do-list-backend:0.0.1
    docker push denisalik/to-do-list-backend:latest
    docker push denisalik/to-do-list-frontend:0.0.1
    docker push denisalik/to-do-list-frontend:latest
