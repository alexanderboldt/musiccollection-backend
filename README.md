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
kubectl kube apply -k kustomize/base
```

Setup Keycloak:
- log in into Keycloak
- import the realm
- create a user with credentials
- assign the realm role `user`
- define a frontend-url for realm

Delete the project namespace and all objects if not needed anymore:
```bash
kubectl delete namespace musiccollection
```
