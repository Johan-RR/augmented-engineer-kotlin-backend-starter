# Acquitter une commande et calculer un temps de preparation : Domain Module impact

**Contexte**
Le domaine doit permettre a un barman ou une barmaid d'acquitter une commande afin de la faire passer a l'etat "en preparation". Cette transition doit calculer un temps estime de preparation en appliquant strictement les regles metier pour les boissons et la nourriture, et refuser toute transition invalide.

**Critères d'acceptation**
Feature: Acquitter une commande et calculer un temps de preparation

Scenario: Acquitter une commande non prise en charge et calculer un ETA pour des boissons non alcoolisees
Given une commande au statut CREEE contenant 3 boissons non alcoolisees de types differents
When le barman acquitte la commande
Then la commande passe au statut ACQUITTEE
And le temps estime de preparation est de 3 minutes

Scenario: Calculer le temps d'une commande avec repas et boissons en utilisant la preparation en parallele
Given une commande au statut CREEE contenant 2 types de repas et 1 boisson alcool premium
When le barman acquitte la commande
Then la commande passe au statut ACQUITTEE
And le temps estime de preparation est de 23 minutes

Scenario: Refuser l'acquittement d'une commande deja acquittee
Given une commande deja au statut ACQUITTEE
When le barman demande un nouvel acquittement
Then la transition est refusee avec une erreur metier OrderAlreadyAcknowledged
And le temps estime de preparation initial reste inchange

Scenario: Calculer un temps cumule pour une commande mixte sans repas
Given une commande au statut CREEE contenant 2 types de boissons non alcoolisees
And 1 type de boisson alcool normale
And 1 type de boisson alcool premium
And 1 type de snack
When le barman acquitte la commande
Then la commande passe au statut ACQUITTEE
And le temps estime de preparation est de 9 minutes

**Notes**
- Le calcul de temps doit etre deterministic et pur au niveau domaine.
- Hypothese explicite a valider: le calcul se base sur le nombre de types differents d'articles, conformement a FEATURES_fr.md.