# Modifier ma commande : Infrastructure Module impact

**Contexte**
La couche infrastructure doit persister les modifications de lignes de commande, ajuster les mouvements de jetons et gerer la creation des demandes de changement pour les commandes deja acquittees. Elle doit garantir la coherence transactionnelle afin d'eviter les effets partiels.

**Critères d'acceptation**
Feature: Modifier ma commande

Scenario: Persister une modification directe de commande non acquittee de facon atomique
Given une commande non acquittee avec des lignes existantes en base
And une requete de modification valide avec ajout et retrait d'articles
When l'adapter de persistance applique la modification
Then les lignes de commande sont mises a jour en base
And les mouvements de jetons associes sont enregistres dans la meme transaction

Scenario: Annuler toute ecriture si une erreur survient pendant l'ajustement des jetons
Given une commande non acquittee et une requete de modification valide
And une erreur technique survient lors de l'enregistrement des mouvements de jetons
When l'adapter de persistance traite la modification
Then aucune mise a jour de ligne de commande n'est conservee
And la transaction est rollbackee

Scenario: Persister une demande de changement pour une commande deja acquittee
Given une commande au statut ACQUITTEE
And une requete de modification emise par son proprietaire
When l'adapter de persistance traite la demande
Then une entite de demande de changement est persistee avec le detail des deltas demandes
And un message de notification est publie a destination du barman ou de la barmaid

**Notes**
- Utiliser un verrouillage optimiste ou pessimiste pour eviter les ecrasements lors de modifications concurrentes.
- Ajouter des tests d'integration repository avec rollback et publication de notification.