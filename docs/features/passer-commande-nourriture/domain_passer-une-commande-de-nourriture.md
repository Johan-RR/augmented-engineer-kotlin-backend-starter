# Passer une commande de nourriture : Domain Module impact

**Contexte**
Le domaine doit permettre a un utilisateur de passer une commande de nourriture contenant plusieurs articles et quantites. La commande doit etre refusee de maniere atomique si au moins un article demande est indisponible en stock.

**Critères d'acceptation**
Feature: Passer une commande de nourriture

Scenario: Creer une commande multi-articles quand tous les articles sont disponibles
Given un utilisateur avec une demande contenant 2 pizzas margherita et 1 salade cesar
And un catalogue nourriture ou les articles demandes sont disponibles en stock
When le cas d'usage de commande de nourriture est execute
Then une commande de nourriture est creee avec toutes les lignes demandees
And la commande est marquee comme acceptee

Scenario: Echouer si au moins un article est en rupture de stock
Given un utilisateur avec une demande contenant 1 burger disponible et 1 dessert en rupture de stock
When le cas d'usage de commande de nourriture est execute
Then la commande est refusee avec un motif de rupture de stock
And aucune ligne de commande n'est creee

Scenario: Echouer si la quantite demandee depasse le stock disponible
Given un utilisateur avec une demande de 3 portions de frites
And le stock disponible pour les frites est de 2 portions
When le cas d'usage de commande de nourriture est execute
Then la commande est refusee avec un motif de stock insuffisant
And aucun stock n'est reserve

**Notes**
- La validation de disponibilite doit couvrir tous les articles avant toute confirmation de commande.
- La logique doit garantir l'atomicite metier: tout est accepte ou tout est refuse.