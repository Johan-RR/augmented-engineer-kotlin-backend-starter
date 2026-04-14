# Modifier ma commande : Application Module impact

**Contexte**
La couche application doit exposer un flux permettant a un festivalier authentifie de modifier sa commande. Si la commande n'est pas acquittee, la modification est appliquee immediatement. Si la commande est deja acquittee, l'application cree une demande de changement et notifie la partie bar sans modifier directement la commande.

**Critères d'acceptation**
Feature: Modifier ma commande

Scenario: Retourner 200 lors de la modification directe d'une commande non acquittee
Given un festivalier authentifie proprietaire d'une commande au statut CREEE
And la requete PATCH /orders/{orderId} contient des lignes de commande valides
When le client appelle l'endpoint de modification
Then la couche application retourne une reponse 200
And la reponse contient les lignes de commande mises a jour et le nouveau total de jetons

Scenario: Retourner 409 quand la modification depasse le solde de jetons
Given un festivalier authentifie proprietaire d'une commande non acquittee
And la requete de modification depasse le solde de jetons disponible
When le client appelle PATCH /orders/{orderId}
Then la couche application retourne une reponse 409
And le corps de reponse contient un code d'erreur INSUFFICIENT_TOKENS

Scenario: Retourner 202 quand la commande est deja acquittee
Given un festivalier authentifie proprietaire d'une commande au statut ACQUITTEE
And la requete de modification demande l'ajout ou le retrait d'articles
When le client appelle PATCH /orders/{orderId}
Then la couche application retourne une reponse 202
And la reponse indique qu'une demande de changement a ete transmise au barman ou a la barmaid

Scenario: Retourner 403 quand le festivalier n'est pas proprietaire de la commande
Given un festivalier authentifie qui n'est pas proprietaire de la commande cible
When le client appelle PATCH /orders/{orderId}
Then la couche application retourne une reponse 403
And aucune modification de commande n'est appliquee

**Notes**
- Le mapping des erreurs metier vers les statuts HTTP doit etre centralise pour garder des contrats API coherents.
- Prevoir des tests d'integration controleur pour les statuts 200, 202, 403 et 409.
