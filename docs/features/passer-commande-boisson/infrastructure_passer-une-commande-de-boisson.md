# Passer une commande de boisson : Infrastructure Module impact

**Contexte**
La couche infrastructure doit implementer les adapters de persistance pour verifier la disponibilite des articles boisson, enregistrer les commandes multi-articles et decrementer le stock. Elle doit garantir qu'une commande echouee pour rupture de stock ne laisse aucun effet partiel.

**Critères d'acceptation**
Feature: Passer une commande de boisson

Scenario: Verifier la disponibilite de tous les articles d'une commande
Given une requete de commande contenant plusieurs articles boisson
And une base de donnees avec les niveaux de stock par article
When l'adapter de stock est sollicite pour valider la commande
Then l'infrastructure retourne la disponibilite de chaque article demande

Scenario: Annuler toute ecriture si un article est en rupture de stock
Given une requete de commande contenant un article disponible et un article en rupture de stock
When l'adapter de persistance tente d'enregistrer la commande
Then aucune ligne de commande n'est persistee
And aucun stock n'est decremente

Scenario: Persister une commande avec plusieurs lignes et decrementer chaque stock associe
Given une commande boisson contenant 3 articles disponibles en stock
When l'infrastructure persiste la commande et ses lignes
Then la commande principale est persistee
And les 3 lignes de commande sont persistees
And le stock de chaque article est decremente selon la quantite commandee

**Notes**
- Utiliser une transaction pour garantir l'atomicite commande plus mouvement de stock.
- Ajouter des tests d'integration repository sur les cas de concurrence et de rollback.