# BiblioWeb — Documentation technique

Public cible : développeurs qui reprennent, maintiennent ou font évoluer le projet.

## 1. Stack technique

| Composant | Choix |
|---|---|
| Langage | Java 17 |
| Framework | Spring Boot 4.1.1 |
| Accès aux données | Spring Data JPA (Hibernate) |
| Base de données | PostgreSQL (prod/dev), H2 en mémoire (tests) |
| Build | Gradle (wrapper fourni, pas besoin d'installer Gradle) |
| Conteneurisation | Docker Compose (`compose.yaml`), démarré automatiquement par `spring-boot-docker-compose` |
| Validation | Jakarta Validation (`spring-boot-starter-validation`) |

## 2. Architecture en couches

```
HTTP request
     │
     ▼
controller/   → reçoit la requête REST, valide le corps (DTO), ne contient aucune logique métier
     │
     ▼
service/      → logique métier (règles d'emprunt, unicité de l'email, etc.), transactions
     │
     ▼
repository/   → Spring Data JPA, accès base de données (CRUD générique)
     │
     ▼
model/        → entités JPA mappées aux tables
```

Règle de dépendance : une couche ne connaît que la couche immédiatement en dessous.
Le `controller` ne manipule jamais directement un `Repository`.

## 3. Structure du projet

```
src/main/java/be/ecam/biblioweb/
├── BiblioWebApplication.java      point d'entrée Spring Boot
├── model/
│   ├── Livre.java                 entité JPA (id, title, author, status)
│   ├── User.java                  entité JPA (id, name, surname, email unique)
│   └── Status.java                enum AVAILABLE / NOT_AVAILABLE
├── repository/
│   ├── LivreRepository.java       extends JpaRepository<Livre, UUID>
│   └── UserRepository.java        extends JpaRepository<User, UUID> + findByEmail
├── service/
│   ├── LivreService.java / LivreServiceImpl.java
│   └── UserService.java / UserServiceImpl.java
├── controller/
│   ├── LivreController.java       /api/livres
│   └── UserController.java        /api/users
├── dto/
│   ├── LivreRequest.java          record validé (title, author)
│   └── UserRequest.java           record validé (name, surname, email)
└── exception/
    ├── ResourceNotFoundException.java
    ├── EmailAlreadyUsedException.java
    └── GlobalExceptionHandler.java    mappe les exceptions vers des réponses HTTP
```

Pourquoi des DTO en plus des entités ? Pour ne jamais exposer `id` ou `status` en écriture
depuis le client : seuls `title`/`author` (livre) ou `name`/`surname`/`email` (user) sont
acceptés en entrée, le reste est géré par le serveur.

## 4. Modèle de données

**Livre**
| Champ | Type | Remarques |
|---|---|---|
| id | UUID | généré automatiquement |
| title | String | |
| author | String | |
| status | enum | `AVAILABLE` par défaut à la création, `NOT_AVAILABLE` si emprunté |

**User**
| Champ | Type | Remarques |
|---|---|---|
| id | UUID | généré automatiquement |
| name | String | |
| surname | String | |
| email | String | unique en base, vérifié aussi en service (409 si doublon) |

## 5. API REST

Base path : `/api`

### Livres

| Méthode | URL | Body | Succès | Erreurs |
|---|---|---|---|---|
| GET | `/api/livres` | — | 200 + liste | — |
| GET | `/api/livres/{id}` | — | 200 + livre | 404 si inconnu |
| POST | `/api/livres` | `{"title":"...","author":"..."}` | 201 + livre créé (status=AVAILABLE) | 400 si champs manquants |
| PUT | `/api/livres/{id}` | `{"title":"...","author":"..."}` | 200 + livre modifié | 404, 400 |
| DELETE | `/api/livres/{id}` | — | 204 | 404 |
| POST | `/api/livres/{id}/borrow` | — | 200, status→NOT_AVAILABLE | 404, 409 si déjà emprunté |
| POST | `/api/livres/{id}/return` | — | 200, status→AVAILABLE | 404 |

### Utilisateurs

| Méthode | URL | Body | Succès | Erreurs |
|---|---|---|---|---|
| GET | `/api/users` | — | 200 + liste | — |
| GET | `/api/users/{id}` | — | 200 + user | 404 |
| POST | `/api/users` | `{"name":"...","surname":"...","email":"..."}` | 201 + user créé | 400, 409 si email déjà utilisé |
| PUT | `/api/users/{id}` | idem | 200 + user modifié | 404, 400 |
| DELETE | `/api/users/{id}` | — | 204 | 404 |

### Format d'erreur standard

Toutes les erreurs (gérées par `GlobalExceptionHandler`) renvoient :

```json
{
  "timestamp": "2026-10-06T10:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Livre introuvable : <uuid>"
}
```

## 6. Base de données & environnements

- **Dev/run normal** : `compose.yaml` décrit un conteneur PostgreSQL. Grâce à la dépendance
  `developmentOnly 'org.springframework.boot:spring-boot-docker-compose'`, Spring Boot démarre
  ce conteneur et configure automatiquement la datasource (pas d'URL/mot de passe à renseigner
  dans `application.yaml`).
- **Tests** (`./gradlew test`) : le support Docker Compose est désactivé pendant les tests.
  `src/test/resources/application.yaml` bascule donc sur une base H2 en mémoire
  (mode `PostgreSQL` pour limiter les différences de syntaxe SQL), avec `ddl-auto: create-drop`.
- **`ddl-auto: update`** en dev/prod : le schéma est créé/mis à jour automatiquement à partir
  des entités JPA. À remplacer par des migrations (Flyway/Liquibase) si le projet doit un jour
  gérer des données de production réelles.

## 7. Commandes utiles

```bash
./gradlew bootRun        # lance l'appli (démarre aussi Postgres via Docker Compose)
./gradlew test            # lance les tests (base H2, pas besoin de Docker)
./gradlew build            # compile + teste + package le jar
```

## 8. Limites connues / pistes d'évolution

- Pas d'authentification/autorisation (toute l'API est publique).
- Pas de pagination sur les listes (`GET /api/livres`, `GET /api/users`) : à ajouter si le
  volume de données grandit (`Pageable` de Spring Data).
- Pas d'entité `Emprunt` dédiée avec historique (dates d'emprunt/retour) : actuellement
  seul le `Status` du livre indique s'il est emprunté, sans savoir par qui ni depuis quand.
- Pas de tests unitaires sur les services/contrôleurs (seul le test de contexte Spring Boot existe).
