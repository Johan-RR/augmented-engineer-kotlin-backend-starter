# Passer une commande de boisson : Domain Module impact

**Contexte**
Le domaine doit permettre a un festivalier de passer une commande de boisson contenant plusieurs articles, en appliquant les regles de cout en jetons et de disponibilite des articles. La commande doit etre refusee de maniere atomique si au moins un article est en rupture de stock.

**Critères d'acceptation**
Feature: Passer une commande de boisson

Scenario: Creer une commande multi-articles quand tous les articles sont disponibles
Given un festivalier avec 6 jetons boisson disponibles
And un catalogue contenant une limonade, une biere normale et un cocktail premium disponibles en stock
When le cas d'usage de commande de boisson est execute pour 1 limonade, 1 biere normale et 1 cocktail premium
Then une commande est creee avec le statut creee
And 3 jetons boisson sont debites du solde du festivalier

Scenario: Echouer si au moins un article est en rupture de stock
Given un festivalier avec 6 jetons boisson disponibles
And un catalogue contenant une limonade disponible et un mojito premium en rupture de stock
When le cas d'usage de commande de boisson est execute pour 1 limonade et 1 mojito premium
Then la commande est refusee avec un motif de rupture de stock
And aucun jeton boisson n'est debite du solde du festivalier

Scenario: Calculer le cout total d'une commande contenant plusieurs boissons alcoolisees
Given un festivalier avec 6 jetons boisson disponibles
And un catalogue contenant 2 bieres normales et 1 cocktail premium disponibles en stock
When le cas d'usage de commande de boisson est execute pour ces 3 articles
Then la commande est acceptee
And le cout total applique est de 4 jetons boisson

**Notes**
- La validation de stock doit etre faite avant la confirmation metier de la commande.
- Le debit des jetons et la creation de commande doivent rester coherents au niveau metier.