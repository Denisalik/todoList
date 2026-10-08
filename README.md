# To Do List Application With JWT Authorization

## Quick Start
```sh
just start
```
### Docker
```sh
just create-rsa
just docker-build
just docker-up
#or if you don't have just
sh just/rsa.sh
docker compose build
docker compose up
```
### Kubernetes
```shell
#todo
```
### Raw
```shell
#install node and java using version managers or any package managers
nvm install v26.10.0
nvm use v26.10.0
#node -v -> v26.10.0
sdk install java 25-tem
sdk use java 25-tem
#sdk current java -> Current default java version 25-tem

just docker-db
just spring
```
### Check Scripts
```shell
just
```
## Access
| Name            | Host URL(require change)         | Nginx URL                   | Internal URL                   | Credentials     |
|-----------------|----------------------------------|-----------------------------|--------------------------------|-----------------|
| Swagger UI      | http://localhost:8080/swagger-ui | http://localhost/swagger-ui | http://backend:8080/swagger-ui | ------------    |
| Frontend(Nginx) | http://localhost:80              | http://localhost            | http://frontend:80             | ------------    |
| Backend API     | http://localhost:8080            | http://localhost/api        | http://backend:8080            | user / password |
| Auth endpoints  | http://localhost:8080/auth       | http://localhost/auth       | http://backend:8080/auth       | admin / admin   |
| PostgreSQL      | http://localhost:5432            | -----------------------     | sql:5432                       | user / mypass   |
| Prometheus      | http://localhost:9090            | -----------------------     | http://prometheus:9090         | ------------    |
| Grafana         | http://localhost:3000            | http://localhost/grafana    | http://grafana:3000            | admin / grafana |
| Loki            | http://localhost:3100/metrics    | -----------------------     | http://loki:3100/metrics       | ------------    |
### Host URL works only if you change "expose" to "ports" in the docker-compose.yml file:
expose: ["\<port\>"] --> ports: ["\<port\>:\<port\>"]

## Open-API code generation
```shell
#Run your services
just start
#curl openapi.json and generate code using docker run orval
just generate
```
# TODO
- add normal status handling for openAPI, not global exception handling
- add frontend
