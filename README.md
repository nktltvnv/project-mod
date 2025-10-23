# Project Mod

Project Mod strives to provide a community-driven space to distribute and discover game modifications.

---

## ⚙️ Setup

### Minikube
Used to run a local Kubernetes cluster for testing and development.

1. Install [Minikube](https://kubernetes.io/ru/docs/tasks/tools/install-minikube/).
2. Start Minikube: ```minikube start --driver=docker```
   1. (Optional) Make Docker the default driver: ```minikube config set driver docker```. Use before the previous command.
3. Create namespace [_project-mode_](k8s/namespace.yaml)
4. Setup Minikube Dashboard: ```minikube dashboard```

### Skaffold
Automates building, deploying, and syncing the application inside the Kubernetes cluster.

1. Install [Skaffold](https://skaffold.dev/docs/install/)
2. Run Skaffold: ```skaffold dev```