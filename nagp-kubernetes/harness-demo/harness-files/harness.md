Create secret for Github - PAT
```
harness secret apply --token <SECRET> --secret-name "harness_gitpat"
```
Create a GiHub Connector
```
harness connector --file  harness-files/github-connector.yml apply --git-user sanu07
```
Create a Kubernetes Cluster Connector: Access to the deployment target.
```
harness connector --file harness-files/kubernetes-connector.yml apply --delegate-name helm-delegate
```
Create a Service: Represents your application
```
harness service --file harness-files/service.yml apply
```
Create an Environment: Represents the production or non-production infrastructure.
```
harness environment --file harness-files/environment.yml apply
```
Create an Infrastructure Definition: Specifies the target cluster for the infrastructure.
```
harness infrastructure  --file harness-files/infrastructure-definition.yml apply
```
Pick a deployment strategy
```
harness pipeline --file harness-files/rolling-pipeline.yml apply
```