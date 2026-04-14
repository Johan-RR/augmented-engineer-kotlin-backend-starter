# Passer une commande de boisson : Application Module impact

**Contexte**
La couche application doit exposer un endpoint permettant a un festivalier authentifie de passer une commande de boisson contenant plusieurs articles. Elle orchestre l'appel au cas d'usage domaine, mappe la requete en commande metier et retourne une reponse explicite en cas de succes ou d'echec pour rupture de stock.

**Critères d'acceptation**
Feature: Passer une commande de boisson

Scenario: Retourner 201 lors de la creation d'une commande multi-articles valide
Given un festivalier authentifie avec un solde de jetons suffisant
And la requete contient plusieurs articles boisson
And tous les articles de la requete sont disponibles en stock
When le client appelle l'endpoint POST /drink-orders
Then la couche application retourne une reponse 201
And la reponse contient un identifiant de commande

Scenario: Retourner 409 quand un article est en rupture de stock
Given un festivalier authentifie
And au moins un article de la requete est en rupture de stock
When le client appelle l'endpoint POST /drink-orders
Then la couche application retourne une reponse 409
And le corps de reponse contient un code d'erreur STOCK_UNAVAILABLE

Scenario: Retourner le detail des lignes de commande pour une commande multi-articles
Given un festivalier authentifie avec un solde de jetons suffisant
And la requete contient 3 articles boisson disponibles en stock
When le client appelle l'endpoint POST /drink-orders
Then la couche application retourne une reponse 201
And la reponse contient les 3 lignes d'articles de la commande

**Notes**
- Le controle metier de disponibilite reste dans le domaine; l'application mappe les erreurs en statuts HTTP.
- Prevoir des tests d'integration de controleur pour les statuts 201 et 409 ainsi que le contrat JSON.