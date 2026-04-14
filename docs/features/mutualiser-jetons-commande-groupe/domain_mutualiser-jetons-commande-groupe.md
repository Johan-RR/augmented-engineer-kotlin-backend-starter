# Mutualiser nos jetons pour passer une commande de groupe : Domain Module impact

**Contexte**
Le domaine doit permettre a un groupe de festivaliers de passer une commande unique financee par des contributions de jetons de plusieurs participants. Les regles metier doivent garantir que la somme des contributions couvre le cout total de la commande, que chaque contribution reste valide par participant, et que le debit reste atomique.

**Critères d'acceptation**
Feature: Mutualiser des jetons pour passer une commande de groupe

Scenario: Creer une commande de groupe quand la somme des contributions couvre le cout total
Given un groupe compose de 3 festivaliers avec des soldes de jetons valides
And une commande de groupe dont le cout total est de 8 jetons boisson
And des contributions de 3, 2 et 3 jetons boisson
When le cas d'usage metier de creation de commande de groupe est execute
Then la commande de groupe est creee
And chaque festivalier est debite du montant exact de sa contribution
And aucun solde de jetons ne devient negatif

Scenario: Refuser une commande de groupe quand la somme des contributions est insuffisante
Given un groupe compose de 2 festivaliers
And une commande de groupe dont le cout total est de 6 jetons nourriture
And des contributions de 2 et 2 jetons nourriture
When le cas d'usage metier de creation de commande de groupe est execute
Then la commande de groupe est refusee avec un motif de contribution insuffisante
And aucun debit de jetons n'est applique

Scenario: Refuser une contribution individuelle superieure au solde du participant
Given un groupe compose de 2 festivaliers
And le premier festivalier dispose de 1 jeton boisson
And une contribution de 2 jetons boisson est proposee pour ce premier festivalier
When le cas d'usage metier de creation de commande de groupe est execute
Then la commande de groupe est refusee avec un motif de contribution invalide
And aucun debit de jetons n'est applique a aucun participant

**Notes**
- Modeliser explicitement la contribution par participant pour conserver la tracabilite metier.
- Garantir l'atomicite metier: soit toutes les contributions sont debitees, soit aucune.
