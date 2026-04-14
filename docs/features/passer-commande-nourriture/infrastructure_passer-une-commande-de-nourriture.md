# Passer une commande de nourriture : Infrastructure Module impact

**Contexte**
La couche infrastructure doit implementer les adapters de persistance necessaires pour verifier la disponibilite de stock, persister les commandes de nourriture multi-articles et decrementer les stocks associes. Une commande en echec ne doit laisser aucun effet partiel en base.

**Critères d'acceptation**
Feature: Passer une commande de nourriture

Scenario: Verifier la disponibilite de tous les articles d'une commande
Given une requete de commande contenant plusieurs articles nourriture
And une base de donnees avec les niveaux de stock par article
When l'adapter de stock est sollicite pour valider la commande
Then l'infrastructure retourne la disponibilite de chaque article demande

Scenario: Ne rien persister si un article est en rupture de stock
Given une requete de commande avec un article disponible et un article indisponible
When l'infrastructure tente d'enregistrer la commande
Then aucune commande ni ligne de commande n'est persistee
And aucun stock n'est decremente

Scenario: Persister une commande multi-articles et decrementer les stocks associes
Given une commande contenant 3 articles nourriture disponibles en stock
When l'infrastructure persiste la commande et ses lignes
Then la commande principale est persistee
And les 3 lignes de commande sont persistees
And le stock de chaque article est decremente selon la quantite commandee

**Notes**
- Utiliser une transaction pour garantir l'atomicite entre ecritures de commande et mouvements de stock.
- Ajouter des tests d'integration repository couvrant rollback et concurrence.