# Examiner une demande de modification de commande acquittee : Application Module impact

**Contexte**
La couche application doit exposer une operation permettant au barman ou a la barmaid d'accepter ou de refuser une demande de modification d'une commande deja acquittee. Elle orchestre la decision, mappe les erreurs metier vers des reponses API et declenche la notification du festivalier quand la demande est acceptee avec un nouvel ETA.

**Critères d'acceptation**
Feature: Examiner une demande de modification de commande acquittee

Scenario: Accepter une demande et retourner le nouvel ETA
Given une demande de changement existante en attente sur une commande ACQUITTEE
And l'utilisateur appelant possede le role BARMAN
When le client appelle POST /orders/{orderId}/change-requests/{requestId}/review avec decision ACCEPT
Then la couche application retourne une reponse 200
And le corps contient decision a ACCEPTEE
And le corps contient updatedEstimatedPreparationTimeMinutes strictement positif

Scenario: Refuser une demande et retourner la decision
Given une demande de changement existante en attente sur une commande ACQUITTEE
And l'utilisateur appelant possede le role BARMAN
When le client appelle POST /orders/{orderId}/change-requests/{requestId}/review avec decision REJECT
Then la couche application retourne une reponse 200
And le corps contient decision a REFUSEE
And le corps contient un rejectionReason renseigne

Scenario: Retourner 409 quand les preconditions metier ne permettent pas l'acceptation
Given une demande de changement existante en attente
And aucun article prepare n'est transferable vers une autre commande
And l'utilisateur appelant possede le role BARMAN
When le client appelle POST /orders/{orderId}/change-requests/{requestId}/review avec decision ACCEPT
Then la couche application retourne une reponse 409
And le corps contient un code d'erreur NO_TRANSFERABLE_PREPARED_ITEM

Scenario: Retourner 404 quand la demande de changement n'existe pas
Given un identifiant de demande de changement inexistant
And l'utilisateur appelant possede le role BARMAN
When le client appelle POST /orders/{orderId}/change-requests/{requestId}/review
Then la couche application retourne une reponse 404
And le corps contient un code d'erreur CHANGE_REQUEST_NOT_FOUND

Scenario: Notifier le festivalier uniquement lors d'une acceptation
Given une demande de changement existante en attente sur une commande ACQUITTEE
And l'utilisateur appelant possede le role BARMAN
When le client appelle POST /orders/{orderId}/change-requests/{requestId}/review avec decision ACCEPT
Then la couche application appelle le port de notification du festivalier
And le message contient le nouvel ETA calcule

**Notes**
- Garder le contrat d'API explicite sur la decision prise, la raison metier et le nouvel ETA quand applicable.
- Couvrir les cas 200, 404 et 409 via tests d'integration de controleur et tests de use case.