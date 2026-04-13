# Kotlin Coding Guidelines

Guidelines specifiques au projet Bel'Air's Buvette.

## 1. Scope, objectifs et architecture

Ces guidelines definissent les regles de code Kotlin a appliquer dans ce repository pour garantir:
- coherence entre modules
- clarte du code metier
- evolutivite du systeme
- testabilite elevee

Le projet suit une architecture modulaire avec trois modules principaux:
- domain: coeur metier, pur Kotlin, sans dependance framework
- infrastructure: adaptateurs techniques (persistance, integration, consommateur externes), depend du domain
- application: point d'entree et composition du backend de l'application, utilisant le domaine via les ports interfaces, dépend du domaine et de l'infrastructure.

Contraintes structurantes:
- JDK cible: 21
- Kotlin cible: 2.3.10
- Build: Gradle Kotlin DSL avec conventions partagees dans build-logic
- Tests: Kotlin test + execution sur JUnit Platform

Principes directeurs:
- Le metier vit dans domain et ne depend jamais d'un choix technique.
- Les details techniques restent confines dans infrastructure.
- application orchestre et assemble les composants sans contenir de logique metier complexe.
- Le code privilegie la lisibilite, la simplicite et des invariants explicites plutot que la magie.

Ces regles servent de reference pour toutes les sections suivantes (naming, modelisation, erreurs, tests, etc.).

## 2. Style Kotlin et conventions de nommage

Cette section fixe un style lisible et uniforme pour limiter la charge cognitive dans les revues de code.

### 2.1 Regles de style general

- Indentation: 4 espaces, pas de tabulations.
- Encodage: UTF-8.
- Un type principal (classe, interface, objet) par fichier.
- Preferer val a var. Toute mutabilite doit etre justifiee.
- Fonctions courtes, avec une responsabilite claire.
- Eviter les booleens "magiques" en parametres. Preferer un type explicite (enum, value object).
- Preferer les null-safety features Kotlin (?. ?: requireNotNull) plutot que des verifications null disperses.
- Lever des erreurs explicites et metier (voir section erreurs) plutot que IllegalStateException generique partout.

### 2.2 Conventions de nommage Kotlin

- Packages: lowercase, sans underscore, alignes sur le contexte metier.
- Classes, interfaces, objets, enums: PascalCase.
- Fonctions et proprietes: camelCase.
- Constantes compile-time: UPPER_SNAKE_CASE.
- Noms explicites, sans abreviations ambigues (ex: festivalGoerId au lieu de fgId).

### 2.3 Nommage par couche

- domain:
	- Entites et value objects en nom metier (TokenBalance, Order, DrinkType).
	- Services metier nommes par intention (OrderPricingService, PreparationTimeCalculator).
	- Aucun suffixe technique lie a un framework.

- application:
	- Use cases suffixes par UseCase (PlaceOrderUseCase, CancelOrderUseCase).
	- Ports entrants nommes par role metier (NotificationGateway...).
	- Command/Query objets nommes avec intention (PlaceOrderCommand, GetBalanceQuery).
    - DTO techniques explicites (OrderEntity, OrderResponseDto) sans fuite dans domain.

- infrastructure:
	- Adaptateurs techniques suffixes par leur nature (JpaOrderRepository, HttpNotificationClient).
	- DTO techniques explicites (OrderEntity, OrderResponseDto) sans fuite dans domain.

### 2.4 Lisibilite et intention

- Un nom doit expliquer "pourquoi" et "quoi", pas "comment".
- Eviter les noms vagues: data, info, manager, helper, util.
- Preferer des API explicites a des fonctions generiques sur-parametrees.
- Lorsque deux formulations sont possibles, choisir la plus proche du vocabulaire de FEATURES.md.

## 3. Modelisation du domaine et invariants

Cette section definit comment traduire les regles metier en code Kotlin robuste dans le module domain.

### 3.1 Modele metier explicite

- Le code du domaine exprime le langage metier de FEATURES.md, pas un vocabulaire technique.
- Le comportement metier vit dans les objets du domaine, pas dans des services utilitaires generiques.
- Chaque regle metier importante doit etre visible dans une methode nommee par intention.

### 3.2 Entites et Value Objects

- Utiliser une entite quand l'identite et le cycle de vie sont centraux (ex: Order, FestivalGoer).
- Utiliser un value object pour representer un concept immutable et valide par construction (ex: TokenBalance, OrderId).
- Eviter la primitive obsession:
	- preferer des types metier dedies a String/Int bruts pour les identifiants et quantites critiques
	- encapsuler les regles de validation dans ces types

### 3.3 Invariants metier: fail fast

- Un objet du domaine ne doit jamais exister dans un etat invalide.
- Valider les invariants a la creation et a chaque transition d'etat.
- Refuser immediatement une transition invalide via une erreur metier explicite.
- Ne jamais deleguer une regle metier fondamentale a l'infrastructure.

Exemples d'invariants attendus pour ce projet:
- un solde de tokens ne peut jamais devenir negatif
- les tokens non depenses ne sont pas reportes au jour suivant
- une commande deja acknowledged ne suit pas le meme flux de modification/annulation qu'une commande non acknowledged
- un changement de commande doit revalider le cout total et les contraintes de preparation

### 3.4 Transitions d'etat et API du domaine

- Modeliser les transitions explicitement (ex: place, acknowledge, requestChange, acceptChange, rejectChange, cancel, markReady).
- Chaque transition:
	- verifie ses preconditions
	- applique uniquement les changements autorises
	- retourne un resultat exploitable par la couche application
- Eviter les setters publics qui permettent des modifications arbitraires de l'etat.

### 3.5 Temps, calculs et determinisme

- Utiliser java.time (Instant, LocalDate, LocalTime, Duration) pour toute logique temporelle.
- Injecter une Clock dans les use cases ou services de domaine qui dependent de l'heure courante.
- Garder les calculs deterministes et testables: pas d'acces direct au temps systeme dans les regles metier.
- Pour les tokens, utiliser des entiers et des operations explicites (pas de flottants).

## 4. Gestion des erreurs et strategie de resultats

Cette section precise comment representer les echecs metier et techniques sans brouiller les responsabilites des couches.

### 4.1 Principes generaux

- Une erreur metier est une information attendue du domaine, pas une surprise technique.
- Une exception technique est reservee aux cas non recuperables ou aux defaillances d'infrastructure.
- Ne pas utiliser Exception ou RuntimeException generiques pour porter des regles metier.
- Les messages d'erreur doivent etre actionnables et alignes sur le vocabulaire metier.

### 4.2 Erreurs metier dans domain

- Modeliser les erreurs metier avec des types explicites (sealed interface/class recommandee).
- Chaque invariant critique doit avoir un type d'erreur dedie (ex: InsufficientDrinkTokens, OrderAlreadyAcknowledged).
- Eviter les codes numeriques opaques dans le domaine.
- Le domaine ne depend pas de details de transport (HTTP status, format JSON, etc.).

### 4.3 Resultats des use cases dans application

- Les use cases retournent des resultats explicites (succes ou echec metier), pas un booleen ambigu.
- Preferer un type de retour structure:
	- sealed class UseCaseResult
	- kotlin.Result uniquement pour des cas simples et sans perte de semantique metier
- Une methode de use case doit rendre explicite ce que l'appelant peut traiter comme cas nominal et cas d'echec.

### 4.4 Frontieres techniques dans infrastructure

- Capturer les erreurs techniques au plus pres de leur source (DB, HTTP client, messaging).
- Mapper les erreurs techniques vers des erreurs applicatives/metier comprehensibles pour la couche superieure.
- Ne jamais laisser remonter une exception de librairie externe vers domain.
- Les retries/timeouts/circuit-breakers restent dans infrastructure, pas dans domain.

### 4.5 Mapping et observabilite

- Le mapping erreur -> reponse externe se fait au niveau application (ou adaptateur d'entree), jamais dans domain.
- Journaliser les erreurs techniques avec contexte (correlationId, operation, cause racine) sans exposer de donnees sensibles.
- Pour les erreurs metier attendues, journaliser au niveau adapte (souvent info/warn), pas en erreur systematique.
- Conserver des messages utilisateur clairs et des messages techniques detailles separes.

