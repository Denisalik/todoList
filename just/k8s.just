# Create a minikube cluster with default resources
create-cluster:
    minikube start -p todoList
    kubectl get nodes

# Create with more resources (recommended for deploying multiple services)
create-cluster-big:
    minikube start -p todoList --cpus 4 --memory 8192
    kubectl get nodes

k8s-status:
    kubectl get all

enter-vault:
    kubectl exec -it -n vault vault-0 -- /bin/sh

# List of required Helm repos: "name url"
helm_repos := "
hashicorp https://helm.releases.hashicorp.com
"

#check if helm is intalled and if the required helm repos are added
check-helm:
    #!/usr/bin/env sh
    set -euo pipefail

    if ! command -v helm >/dev/null 2>&1; then
            echo "❌ Helm is not installed."
            echo "   Install it from: https://helm.sh/docs/intro/install/"
            exit 1
    fi

    missing=0
    while read -r name url; do
        [ -z "$name" ] && continue

        if ! helm repo list 2>/dev/null | awk '{print $1}' | grep -qx "$name"; then
            echo "❌ Helm repo '$name' is missing"
            echo "   Add it with: helm repo add $name $url"
            missing=1
        fi
    done <<< "{{ helm_repos }}"

    if [ "$missing" -ne 0 ]; then
        echo "Add the missing repos above, then run: helm repo update"
        exit 1
    fi

k8s-install-vault:
    helm install vault hashicorp/vault --set='server.dev.enabled=true' --set='ui.enabled=true' --set='ui.serviceType=LoadBalancer' --namespace vault

k8s-apply: check-helm
    kubectl apply -f ./k8s/ -R

# Stop the cluster
stop-cluster:
    minikube stop -p todoList

# Delete the cluster
delete-cluster:
    minikube delete -p todoList

# Delete the cluster that is using more resources
delete-cluster-big:
    minikube delete -p todoList
