# Examiner une demande de modification de commande acquittee : Infrastructure Module impact

**Contexte**
La couche infrastructure doit persister la decision de revue d'une demande de changement, appliquer les modifications de commande en cas d'acceptation, recalculer et stocker le nouvel ETA, puis emettre une notification fiable au festivalier. Elle doit aussi fournir les donnees necessaires pour determiner si des articles prepares sont transferables.

**Critères d'acceptation**
Feature: Examiner une demande de modification de commande acquittee

Scenario: Persister atomiquement l'acceptation et la mise a jour de commande
Given une demande de changement en attente associee a une commande ACQUITTEE
And au moins un article prepare est transferable
When l'adaptateur de persistance enregistre une decision ACCEPTEE
Then la demande de changement est sauvegardee au statut ACCEPTEE
And les lignes de commande sont mises a jour dans la meme transaction
And le champ estimated_preparation_time_minutes est mis a jour

Scenario: Persister le refus sans modifier la commande
Given une demande de changement en attente associee a une commande ACQUITTEE
When l'adaptateur de persistance enregistre une decision REFUSEE
Then la demande de changement est sauvegardee au statut REFUSEE
And les lignes de la commande restent inchangees

Scenario: Publier une notification fiable apres acceptation
Given une demande de changement venant d'etre acceptee avec un nouvel ETA
When l'adaptateur de notification publie l'evenement de mise a jour de commande
Then le message contient l'identifiant de commande
And le message contient le nouvel ETA
And le message est emis une seule fois

Scenario: Ne pas emettre de notification lors d'un refus
Given une demande de changement venant d'etre refusee
When l'infrastructure persiste la decision
Then aucun message de notification d'ETA n'est emis
And seul le statut de la demande est mis a jour

Scenario: Remonter une erreur technique exploitable en cas d'echec transactionnel
Given une erreur technique survient pendant la transaction d'acceptation
When l'infrastructure tente de persister la decision et la commande
Then aucune ecriture partielle n'est conservee
And une erreur technique explicite est remontee vers la couche superieure

**Notes**
- Utiliser une transaction unique pour garantir la coherence entre decision, commande et ETA.
- Si un outbox est utilise, garantir l'absence de double emission de notification.