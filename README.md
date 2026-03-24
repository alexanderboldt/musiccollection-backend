# :radio: Music Collection

This is a backend application for managing the music-library like artists and albums.

## :computer: Tech-Stack

### Development
- Kotlin
- Quarkus
- Hibernate
- Flyway
- PostgreSQL
- Keycloak
- MinIO

### Test
- RestAssured
- Mockito
- Kotest
- Docker-Testcontainer

### Buildsystem
- Gradle

### CI/CD
- GitHub Actions

## :whale: Install with Docker
Create and start the containers:
```bash
docker compose up -d
```

Setup Keycloak:
- log in into Keycloak
- create a user with credentials
- assign the realm role `user`
- define a frontend-url for realm

## :wheel: Install with Kubernetes
Install all objects with Kustomize:
```bash
kubectl apply -k kustomize/base
```

Setup Keycloak:
- log in into Keycloak
- import the realm
- create a user with credentials
- assign the realm role `user`
- define a frontend-url for realm

Set the context to the project namespace:
```bash
kubectl config set-context --current -n musiccollection
```

Delete the project namespace and all objects if not needed anymore:
```bash
kubectl delete namespace musiccollection
```

## :cloud: Install with OpenTofu (Terraform)
Make sure a connection to a cloud is available in the cli.

Navigate to the `opentofu` directory and execute with these commands:
```bash
tofu init
tofu plan
tofu apply
```

Delete all resources if not needed anymore:
```bash
tofu destroy
```

## :dog: Test with Bruno
The folder `api-collection` contains the requests to test the app with Bruno.
