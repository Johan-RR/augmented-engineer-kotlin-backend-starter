# Passer une commande de nourriture : Application Module impact

**Contexte**
La couche application doit exposer un endpoint permettant a un utilisateur authentifie de soumettre une commande de nourriture multi-articles. Elle doit mapper la requete vers le cas d'usage domaine et retourner une erreur explicite si un des articles est indisponible.

**Critères d'acceptation**
Feature: Passer une commande de nourriture

Scenario: Retourner 201 lors de la creation d'une commande de nourriture multi-articles valide
Given un utilisateur authentifie
And la requete contient plusieurs articles nourriture avec des quantites positives
And tous les articles de la requete sont disponibles en stock
When le client appelle l'endpoint POST /food-orders
Then la couche application retourne une reponse 201
And la reponse contient l'identifiant de commande et les lignes de commande

Scenario: Retourner 409 quand au moins un article est en rupture de stock
Given un utilisateur authentifie
And au moins un article de la requete est indisponible en stock
When le client appelle l'endpoint POST /food-orders
Then la couche application retourne une reponse 409
And le corps de reponse contient un code d'erreur STOCK_UNAVAILABLE

Scenario: Retourner 400 quand la requete de commande est invalide
Given un utilisateur authentifie
And la requete contient une ligne sans identifiant d'article ou avec une quantite nulle
When le client appelle l'endpoint POST /food-orders
Then la couche application retourne une reponse 400
And le corps de reponse decrit les erreurs de validation

**Notes**
- Le controle de stock reste dans le domaine; l'application se charge de mapper les erreurs metier en statuts HTTP.
- Prevoir des tests de controleur pour les statuts 201, 409 et 400 avec verification du contrat JSON.