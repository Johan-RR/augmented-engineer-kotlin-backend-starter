# Acquitter une commande et notifier le festivalier : Application Module impact

**Contexte**
La couche application doit exposer une operation d'acquittement de commande declenchee par le barman ou la barmaid. Elle orchestre la transition metier, retourne le nouveau statut avec le temps estime de preparation, et declenche la notification du festivalier que sa commande est en cours de preparation.

**Critères d'acceptation**
Feature: Acquitter une commande et notifier le festivalier

Scenario: Retourner le statut acquittee et l'ETA apres acquittement
Given une commande existante au statut CREEE
And l'utilisateur appelant possede le role BARMAN
When le client appelle POST /orders/{orderId}/acknowledge
Then la couche application retourne une reponse 200
And le corps contient status a ACQUITTEE
And le corps contient estimatedPreparationTimeMinutes strictement positif

Scenario: Refuser l'acquittement d'une commande deja acquittee
Given une commande existante au statut ACQUITTEE
And l'utilisateur appelant possede le role BARMAN
When le client appelle POST /orders/{orderId}/acknowledge
Then la couche application retourne une reponse 409
And le corps contient un code d'erreur ORDER_ALREADY_ACKNOWLEDGED

Scenario: Retourner 404 quand la commande n'existe pas
Given un identifiant de commande inexistant
And l'utilisateur appelant possede le role BARMAN
When le client appelle POST /orders/{orderId}/acknowledge
Then la couche application retourne une reponse 404
And le corps contient un code d'erreur ORDER_NOT_FOUND

Scenario: Declencher une notification au festivalier apres acquittement
Given une commande existante au statut CREEE rattachee a un festivalier
And l'utilisateur appelant possede le role BARMAN
When le client appelle POST /orders/{orderId}/acknowledge
Then la couche application appelle le port de notification avec le statut EN_PREPARATION
And le message de notification contient le temps estime de preparation calcule

**Notes**
- Le mapping des erreurs metier vers HTTP doit rester dans la couche application.
- Prevoir des tests de use case et des tests d'integration de controleur pour les statuts 200, 404 et 409.