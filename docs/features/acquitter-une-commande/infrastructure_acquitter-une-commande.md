# Acquitter une commande : Infrastructure Module impact

**Contexte**
La couche infrastructure doit persister l'etat acquittee d'une commande, stocker le temps estime de preparation et publier la notification au festivalier via l'adaptateur de communication. Elle doit garantir la coherence des ecritures et la fiabilite d'emission de notification.

**Critères d'acceptation**
Feature: Acquitter une commande

Scenario: Persister le statut acquittee et l'ETA d'une commande
Given une commande existante au statut CREEE dans la base de donnees
When l'adaptateur de persistance enregistre le resultat d'acquittement
Then le statut de la commande est ACQUITTEE en base
And le champ estimated_preparation_time_minutes est renseigne

Scenario: Emettre une notification contenant l'ETA calcule
Given une commande venant d'etre acquittee avec un ETA de 9 minutes
When l'adaptateur de notification publie l'evenement vers le canal de notification
Then le message transmis contient l'identifiant de commande
And le message transmis contient le temps estime de preparation

Scenario: Garantir l'atomicite des ecritures de commande lors de l'acquittement
Given une tentative d'acquittement de commande en cours de persistance
When une erreur technique survient pendant l'ecriture en base
Then aucune mise a jour partielle de la commande n'est conservee
And l'etat de la commande reste inchange

Scenario: Mapper une erreur technique de persistance vers une erreur applicative exploitable
Given une indisponibilite de la base de donnees au moment de l'acquittement
When l'infrastructure tente de persister le statut ACQUITTEE
Then l'adaptateur retourne une erreur technique explicite vers la couche superieure
And aucun message de notification n'est emis

**Notes**
- Utiliser une transaction pour la coherence des ecritures sur la commande.
- Si le projet utilise un outbox, verifier la publication fiable des notifications sans double emission.