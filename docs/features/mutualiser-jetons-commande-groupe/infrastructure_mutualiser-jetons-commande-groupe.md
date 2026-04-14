# Mutualiser nos jetons pour passer une commande de groupe : Infrastructure Module impact

**Contexte**
La couche infrastructure doit persister une commande de groupe, ses participants et leurs contributions, puis appliquer les debits de jetons associes de maniere transactionnelle. Elle doit garantir l'absence d'effets partiels en cas d'echec de validation ou de concurrence.

**Critères d'acceptation**
Feature: Mutualiser des jetons pour passer une commande de groupe

Scenario: Persister une commande de groupe et toutes les contributions associees
Given une commande de groupe valide avec 3 participants contributeurs
When l'adapter de persistance enregistre la commande
Then la commande de groupe est persistee
And les 3 contributions participantes sont persistees avec leurs montants
And les soldes des participants sont decrementes selon leurs contributions

Scenario: Effectuer un rollback complet en cas d'echec pendant l'ecriture
Given une commande de groupe valide dont la persistance echoue pendant l'enregistrement d'une contribution
When l'adapter de persistance execute la transaction
Then aucune commande de groupe n'est persistee
And aucune contribution n'est persistee
And aucun solde de participant n'est debite

Scenario: Detecter un conflit de concurrence sur le solde d'un participant
Given deux commandes de groupe concurrentes impliquant le meme participant
And un solde initial qui ne permet pas de satisfaire les deux commandes
When les deux transactions de debit sont executees
Then une seule commande est validee
And l'autre commande est rejetee avec un motif de conflit de concurrence

**Notes**
- Utiliser une transaction unique pour la persistance de commande et les debits de jetons.
- Prevoir un mecanisme de verrouillage optimiste ou pessimiste sur les soldes selon le choix technique retenu.