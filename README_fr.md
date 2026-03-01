# La Buvette de Bel'Air : construire un backend pour la célèbre buvette du festival eXalt. Avec Kotlin, IA, et amour.

Version anglaise : [README.md](README.md)  
Version española : [README_es.md](README_es.md)

>[!note]
> 
> Ce projet fait partie du parcours d'apprentissage eXalt IT augmented engineer, disponible dans son [academy](https://example.com).

Bonjour et bienvenue dans le dépôt du projet La Buvette de Bel'Air !

Ce projet est votre terrain de jeu pour créer un backend robuste de gestion des boissons et snacks !

Vous allez construire le meilleur backend possible en utilisant Kotlin.

Mais plus important encore, votre nouveau meilleur ami : GitHub Copilot, votre canard en caoutchouc / stagiaire trop enthousiaste pour le pair programming !

## Structure du projet

```
belairs-buvette/
 application/      # Point d'entrée  relie le domaine et l'infrastructure
 domain/           # Logique métier et modèle de domaine
 infrastructure/   # Adaptateurs, persistance, intégrations externes
 build-logic/      # Plugins de convention Gradle
```

## Installation de la chaîne d'outils

| Outil | Version | Documentation |
|-------|---------|---------------|
| JDK | 21+ | [Adoptium Temurin](https://adoptium.net/) |
| Git | latest | [git-scm.com](https://git-scm.com/downloads) |

> Le projet utilise le Gradle wrapper (`./gradlew`), vous n'avez donc pas besoin d'installer Gradle séparément.

## Démarrage

### Prérequis

- JDK 21+
- Git

### Fork & Clone

Forkez ce dépôt sur votre propre compte Gitlab (branche main uniquement), puis clonez-le :

```bash
git clone <URL_DE_VOTRE_FORK>
cd belairs-buvette
```

### Miroir vers GitHub

Pour pouvoir utiliser correctement les fonctionnalités IA avancées avec Copilot, miroir ce dépôt sur votre compte GitHub :

```bash
git remote add github <the URL of your new GitHub repository>
git branch -M main
git push -u github main
```

### Compiler

```bash
./gradlew build
```

### Lancer les tests

```bash
./gradlew test
```

## Étapes suivantes

Commencez par suivre le reste du matériel de formation dans l'[academy](https://example.com).

Consultez le fichier [FEATURES_fr.md](./FEATURES_fr.md) pour la liste des user stories et des critères d'acceptation.

Bon codage !
