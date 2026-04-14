# Mutualiser nos jetons pour passer une commande de groupe : Application Module impact

**Contexte**
La couche application doit exposer un flux permettant de soumettre une commande de groupe avec la liste des participants et leurs contributions, puis de mapper le resultat metier en reponse API explicite. Elle doit retourner un succes uniquement lorsque toutes les validations metier sont satisfaites.

**Critères d'acceptation**
Feature: Mutualiser des jetons pour passer une commande de groupe

Scenario: Retourner 201 quand une commande de groupe est valide
Given une requete API contenant 3 participants et leurs contributions
And la somme des contributions couvre le cout total de la commande
When le client appelle l'endpoint de creation de commande de groupe
Then la couche application retourne une reponse 201
And la reponse contient l'identifiant de commande de groupe
And la reponse contient le recapitulatif des contributions retenues

Scenario: Retourner 422 quand la somme des contributions est insuffisante
Given une requete API contenant des contributions dont la somme est inferieure au cout total
When le client appelle l'endpoint de creation de commande de groupe
Then la couche application retourne une reponse 422
And le corps de reponse contient un code d'erreur INSUFFICIENT_GROUP_TOKENS

Scenario: Retourner 422 quand une contribution depasse le solde d'un participant
Given une requete API contenant au moins un participant avec une contribution superieure a son solde
When le client appelle l'endpoint de creation de commande de groupe
Then la couche application retourne une reponse 422
And le corps de reponse contient un code d'erreur INVALID_PARTICIPANT_CONTRIBUTION

**Notes**
- Garder les validations de regles metier dans le domaine et effectuer le mapping erreur-vers-HTTP dans l'application.
- Ajouter des tests d'integration du controleur pour les reponses 201 et 422 avec verification du contrat JSON.
