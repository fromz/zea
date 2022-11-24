# Zea Platform

## Installation

```bash
kubectl create namespace zea
helm install -n zea zeebe-platform . -f values-dev.yaml
```

## Uninstallation and cleanup

```bash
helm uninstall -n zea zeebe-platform
kubectl get pvc
kubectl delete pvc <PVC ids here>
```