# BiblioWeb — expliqué simplement

*Ce document s'adresse à toute personne qui n'est pas développeuse et veut comprendre
ce que fait le projet, sans jargon technique.*

## En une phrase

BiblioWeb est le **registre numérique d'une bibliothèque** : il garde en mémoire la liste des
livres, la liste des membres, et qui a emprunté quoi.

## Le problème que ça résout

Avant, une bibliothèque tient ça à jour sur papier ou dans un tableau Excel : liste des livres,
liste des membres, carnet des emprunts. C'est lent à consulter, facile à perdre, et impossible
à faire vérifier automatiquement (rien n'empêche de "prêter" un livre déjà prêté par erreur).

BiblioWeb remplace ce carnet par un système centralisé et toujours à jour.

## Ce que le système sait faire

- **Tenir le catalogue des livres** : ajouter un livre, corriger son titre/auteur, le retirer
  du catalogue, savoir s'il est disponible ou déjà emprunté.
- **Tenir la liste des membres** : inscrire une personne, mettre à jour ses coordonnées, la
  retirer. Chaque personne a un email unique — le système refuse d'inscrire deux fois la même
  adresse, pour éviter les doublons.
- **Gérer les emprunts** : marquer un livre comme "emprunté" et empêcher qu'il soit prêté une
  seconde fois tant qu'il n'a pas été rendu ; marquer un livre comme "rendu" pour le remettre
  disponible.

## Comment c'est construit (sans jargon)

Imaginez une bibliothèque organisée en plusieurs postes de travail, chacun avec un rôle précis :

1. **L'accueil** reçoit les demandes ("je veux ajouter un livre", "je veux emprunter tel
   ouvrage") et vérifie que la demande est complète et bien formée avant de la transmettre.
2. **Le/la bibliothécaire** applique les règles : *"non, ce livre est déjà prêté"*, *"non,
   cette adresse email est déjà utilisée par quelqu'un d'autre"*.
3. **Le registre** (la base de données) conserve réellement l'information, de façon durable —
   même si l'ordinateur redémarre, rien n'est perdu.

Chaque poste ne fait qu'une chose et fait confiance au poste suivant pour la suite — c'est ce
qui rend le système fiable et plus facile à faire évoluer (ajouter une nouvelle règle à un seul
endroit plutôt que de devoir la recopier partout).

## Ce que le système ne fait pas (encore)

- Il n'y a pas encore d'écran/d'interface visuelle pour les utilisateurs finaux : pour l'instant,
  on communique avec BiblioWeb par des requêtes techniques (ce qui le rend prêt à être branché
  derrière un futur site web ou une application mobile).
- Personne ne doit s'identifier (pas de notion de "connexion" avec mot de passe) : tout le monde
  qui a accès au système peut tout faire. Ce sera à ajouter avant une mise en production réelle.
- On ne garde pas encore d'historique des emprunts (qui a emprunté quoi, et quand) — seulement
  l'état actuel ("disponible" ou "emprunté").

## Pourquoi c'est utile

- **Fiabilité** : impossible de prêter deux fois le même livre par erreur.
- **Rapidité** : retrouver un livre ou un membre est instantané, plutôt que de chercher dans un
  classeur.
- **Évolutivité** : la base est posée pour ajouter plus tard un historique des emprunts, des
  comptes utilisateurs, une interface web, etc. sans tout reconstruire.
