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
just create-cluster
#check vault configuration if you are using cluster first time.
#apply all k8s files
just k8s-apply
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

## Vault configuration
```shell
#check if helm is installed and vault repos are added
just check-helm

#create vault namespace
kubectl apply -f ./k8s/vault/

#install vault in cluster
just k8s-install-vault

#connect to vault pod
just enter-vault

#create policy for reading secrets
cat <<EOF > /home/vault/read-policy.hcl
path "secret/data/*" {
  capabilities = ["read"]
}
EOF
vault policy write read-policy /home/vault/read-policy.hcl
#create k8s auth for vault to communicate with k8s
vault auth enable kubernetes
vault write auth/kubernetes/config \
   token_reviewer_jwt="$(cat /var/run/secrets/kubernetes.io/serviceaccount/token)" \
   kubernetes_host=https://${KUBERNETES_PORT_443_TCP_ADDR}:443 \
   kubernetes_ca_cert=@/var/run/secrets/kubernetes.io/serviceaccount/ca.crt
#create roles for above policy to vault account with namespace
vault write auth/kubernetes/role/main-role \
   bound_service_account_names=main-service-account \
   bound_service_account_namespaces=main \
   policies=read-policy \
   ttl=1h
vault write auth/kubernetes/role/obs-role \
   bound_service_account_names=obs-service-account \
   bound_service_account_namespaces=obs \
   policies=read-policy \
   ttl=1h
#now you can add secrets:
vault kv put secret/postgres POSTGRES_PASSWORD=POSTGRES_PASSWORD POSTGRES_USER=POSTGRES_USER
vault kv put secret/ai AI_API_KEY=<token>
vault kv put secret/ai AI_BASE_URL=<url>
vault kv put secret/ai AI_MODEL=<model>
vault kv put secret/grafana GF_SECURITY_ADMIN_USER=admin GF_SECURITY_ADMIN_PASSWORD=grafana
```
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
