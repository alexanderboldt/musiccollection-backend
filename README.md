# Music Collection

This is a backend application for managing the music-library like artists and albums.

## Tech-Stack

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

## Install with Docker
Create and start the containers:
```bash
docker compose up -d
```

## Install with Kubernetes
Install all objects with Kustomize:
```bash
kubectl kube apply -k kustomize/base
```

Setup Keycloak:
- log in into Keycloak
- import the realm
- create a user with credentials
- assign the realm role `user`

Delete the project namespace and all objects if not needed anymore:
```bash
kubectl delete namespace musiccollection
```
