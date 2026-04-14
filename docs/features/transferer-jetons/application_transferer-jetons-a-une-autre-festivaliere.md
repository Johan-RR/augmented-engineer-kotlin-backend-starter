# Transferer des jetons a une autre festivaliere : Application Module impact

**Contexte**
La couche application doit exposer les points d'entree necessaires pour initier un transfert de jetons et confirmer ce transfert par la destinataire. Elle orchestre les cas d'usage domaine, applique les controles d'acces (source et destinataire) et mappe les erreurs metier en reponses API explicites.

**Critères d'acceptation**
Feature: Transferer des jetons a une autre festivaliere

Scenario: Creer un transfert en attente via l'endpoint d'initiation
Given une source authentifiee et une destinataire existante
And une demande de transfert de 2 jetons boisson et 1 jeton nourriture
When le client appelle l'endpoint d'initiation de transfert
Then la couche application retourne une reponse 201
And la reponse contient un identifiant de transfert et le statut EN_ATTENTE

Scenario: Retourner une erreur de validation quand la limite par type est depassee
Given une source authentifiee
And une demande de transfert de 5 jetons boisson
When le client appelle l'endpoint d'initiation de transfert
Then la couche application retourne une reponse 400
And le message d'erreur indique la limite maximale de 3 jetons par type

Scenario: Retourner un rejet metier quand le solde source est insuffisant
Given une source authentifiee avec un solde insuffisant
And une demande de transfert valide en forme
When le client appelle l'endpoint d'initiation de transfert
Then la couche application retourne une reponse 409
And aucun transfert confirme n'est enregistre

Scenario: Autoriser uniquement la destinataire a confirmer le transfert
Given un transfert EN_ATTENTE associe a une destinataire specifique
When une autre festivaliere authentifiee appelle l'endpoint de confirmation
Then la couche application retourne une reponse 403
And le transfert reste EN_ATTENTE

Scenario: Confirmer un transfert en attente et retourner son nouvel etat
Given la destinataire authentifiee d'un transfert EN_ATTENTE
When la destinataire appelle l'endpoint de confirmation
Then la couche application retourne une reponse 200
And la reponse contient le statut CONFIRME

**Notes**
- Definir un contrat API stable pour l'initiation et la confirmation (payload, codes d'erreur, structure des reponses).
- Ajouter des tests d'integration controleur pour valider les statuts HTTP et les autorisations.
