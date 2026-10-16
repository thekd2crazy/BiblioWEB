# BiblioWeb — Documentation utilisateur (API)

Public cible : toute personne qui doit **utiliser** l'API BiblioWeb (développeur d'une
application cliente, testeur, intégrateur), sans avoir besoin de connaître son fonctionnement
interne.

L'application tourne par défaut sur `http://localhost:8080`. Toutes les URLs ci-dessous sont
relatives à cette adresse.

## 1. Gérer les livres

### Lister tous les livres

```bash
curl http://localhost:8080/api/livres
```

Réponse :
```json
[
  { "id": "a1b2...", "title": "1984", "author": "George Orwell", "status": "AVAILABLE" }
]
```

### Consulter un livre précis

```bash
curl http://localhost:8080/api/livres/{id}
```

### Ajouter un livre

```bash
curl -X POST http://localhost:8080/api/livres \
  -H "Content-Type: application/json" \
  -d '{"title": "1984", "author": "George Orwell"}'
```

Le livre est créé avec le statut `AVAILABLE` (disponible). L'`id` est généré automatiquement,
pas besoin (et pas possible) de le fournir.

### Modifier un livre

```bash
curl -X PUT http://localhost:8080/api/livres/{id} \
  -H "Content-Type: application/json" \
  -d '{"title": "1984", "author": "G. Orwell"}'
```

### Supprimer un livre

```bash
curl -X DELETE http://localhost:8080/api/livres/{id}
```

### Emprunter un livre

```bash
curl -X POST http://localhost:8080/api/livres/{id}/borrow
```

- Si le livre est disponible → il passe en `NOT_AVAILABLE`.
- Si le livre est déjà emprunté → erreur `409 Conflict` ("Le livre ... est deja emprunte").

### Rendre un livre

```bash
curl -X POST http://localhost:8080/api/livres/{id}/return
```

Le livre repasse en `AVAILABLE`.

## 2. Gérer les utilisateurs (membres de la bibliothèque)

### Lister tous les utilisateurs

```bash
curl http://localhost:8080/api/users
```

### Ajouter un utilisateur

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "Steve", "surname": "K.", "email": "steve@example.com"}'
```

L'email doit être unique : si un utilisateur existe déjà avec cet email, l'API renvoie une
erreur `409 Conflict`.

### Modifier / supprimer un utilisateur

Mêmes principes que pour les livres :
```bash
curl -X PUT http://localhost:8080/api/users/{id} -H "Content-Type: application/json" -d '{...}'
curl -X DELETE http://localhost:8080/api/users/{id}
```

## 3. Comprendre les erreurs

Toute erreur renvoie un code HTTP et un message JSON explicite :

| Code | Signification | Exemple de cause |
|---|---|---|
| 400 | Requête invalide | champ obligatoire manquant (ex: titre vide) |
| 404 | Ressource introuvable | `id` qui n'existe pas en base |
| 409 | Conflit | livre déjà emprunté, email déjà utilisé |

Exemple de réponse d'erreur :
```json
{
  "timestamp": "2026-10-06T10:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Livre introuvable : a1b2c3d4-..."
}
```

## 4. Démarrer l'application en local

Prérequis : Docker installé et lancé (pour la base de données PostgreSQL).

```bash
./gradlew bootRun
```

Spring Boot démarre automatiquement le conteneur PostgreSQL décrit dans `compose.yaml` — aucune
configuration manuelle de base de données n'est nécessaire.
