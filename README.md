# App deployment notes

## Set up Google Cloud

1. Sign up to Google Cloud
2. Execute login command:

```bash
gcloud config set account anastasiia.peleshok@gmail.com
```

1. Give permissions

```bash
 # give permissions
 gcloud projects add-iam-policy-binding spiritual-tiger-453909-c0     --member="user:anastasiia.peleshok@gmail.com"     --role="roles/artifactregistry.writer"
```

1. Install `gclould` tool. [guide](https://cloud.google.com/sdk/docs/install)

## Upload image to registry

1. Setup docker registry.  [guide](https://console.cloud.google.com/artifacts?invt=AbsLBA&inv=1&project=spiritual-tiger-453909-c0)
2. Push docker image in Artifact Registry by this [tutorial](https://cloud.google.com/artifact-registry/docs/docker/pushing-and-pulling#pushing)
    1. **Note**: Skip step with hello-app

## Cluster creation

Source:  [tutorial](https://cloud.google.com/kubernetes-engine/docs/deploy-app-cluster)

1. Start gcloud shell (icon at top right corner).
2. Create cluster `deployment-cluster`

```bash
gcloud container clusters create-auto deployment-cluster \
    --location=us-central1
```

1. Obtain credentials for cluster

```bash
gcloud container clusters get-credentials deployment-cluster \
    --location us-central1gcloud container clusters get-credentials deployment-cluster \
    --location us-central1
```

## Deployment on cluster

1. Create deployment `deployment-server` with image `us-central1-docker.pkg.dev/spiritual-tiger-453909-c0/deployment/deployment-app:latest` .

```bash
kubectl create deployment deployment-server \
    --image=us-central1-docker.pkg.dev/spiritual-tiger-453909-c0/deployment/deployment-app:latest
```

1. Expose `80` for `deployment-server` deployment.

```bash
kubectl expose deployment deployment-server \
    --type LoadBalancer \
    --port 80 \
    --target-port 6969
```

1. Set up env variable (for choosing app profile)

```bash
kubectl set env deployment/deployment-server ENV_TYPE=prod
```

1. Get pod external IP

```bash
kubectl get services
```

6. Check if app running at `https://<external_ip>:80`

## Cleaning

```bash
kubectl delete service deployment-server
```

```bash
gcloud container clusters delete deployment-cluster \
    --location us-central1
```

# Deployment using helm

1. Install gcloud and gcloud plugin locally. [Tutorial](https://cloud.google.com/sdk/docs/install-sdk#deb)
2. Connect from your local terminal to cluster that you created

```bash
gcloud container clusters get-credentials deployment-cluster \
    --location us-central1
```

1. Check if you are in the right kubectl context

```bash
kubectl config get-contexts
```

1. Create helm repository

```bash
helm create [name]
```

1. Configure values file and create additional values for different profiles if needed

ex. in values-prod.yaml

```bash
service:
  type: LoadBalancer   # Can be ClusterIP, NodePort, or LoadBalancer
  port: 80             # The external service port (clients connect to this)
  targetPort: 6969 
  
livenessProbe:
  httpGet:
    path: /
    port: http
readinessProbe:
  httpGet:
    path: /
    port: http

autoscaling:
  enabled: false
  minReplicas: 1
  maxReplicas: 100
  targetCPUUtilizationPercentage: 80  
```

1. Deploy app with command

```bash
helm install [chart.yaml name] [path to chart.yaml file] --values [path to values.yaml file]]
```

1. Connect to app:
   ip: kubectl get services
   port: from values.yaml