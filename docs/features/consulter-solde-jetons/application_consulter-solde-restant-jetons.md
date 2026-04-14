# Consulter le solde restant de mes jetons : Application Module impact

**Contexte**
La couche application doit exposer un point d'entrée permettant à un festivalier authentifié de consulter son solde courant de jetons boisson et nourriture. Elle orchestre l'appel au cas d'usage domaine et transforme le résultat en réponse API lisible par le client.

**Critères d'acceptation**
Feature: Consulter le solde restant de mes jetons

Scenario: Retourner le solde courant via l'endpoint de consultation
Given un festivalier authentifié avec des droits valides
And un solde courant calculable côté domaine
When le client appelle l'endpoint de consultation du solde
Then la couche application retourne une réponse 200
And la réponse contient les champs tokenBoissonRestants et tokenNourritureRestants

Scenario: Retourner 404 si le festivalier n'existe pas
Given un identifiant de festivalier inconnu du système
When le client appelle l'endpoint de consultation du solde
Then la couche application retourne une réponse 404
And le message d'erreur indique que le festivalier est introuvable

Scenario: Retourner 401 si l'utilisateur n'est pas authentifié
Given une requête sans contexte d'authentification valide
When le client appelle l'endpoint de consultation du solde
Then la couche application retourne une réponse 401
And aucun solde de jetons n'est exposé

Scenario: Retourner des valeurs entières non négatives dans le DTO de réponse
Given un solde courant fourni par le domaine
When la couche application mappe le résultat vers le DTO de réponse
Then tokenBoissonRestants est un entier supérieur ou égal à 0
And tokenNourritureRestants est un entier supérieur ou égal à 0

**Notes**
- Le mapping application doit rester sans logique métier de calcul.
- Prévoir des tests d'intégration de contrôleur pour valider les statuts HTTP et le contrat JSON.
