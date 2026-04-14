# Modifier ma commande : Domain Module impact

**Contexte**
Le domaine doit permettre a un festivalier de modifier une commande existante en ajoutant ou retirant des articles tant que la commande n'est pas acquittee. Le recalcul du cout doit rester coherent avec les regles de jetons boisson et nourriture. Si la commande est deja acquittee, le domaine ne modifie pas directement la commande mais enregistre une demande de changement a destination du barman ou de la barmaid.

**Critères d'acceptation**
Feature: Modifier ma commande

Scenario: Modifier directement une commande non acquittee
Given un festivalier proprietaire d'une commande au statut CREEE
And une commande contenant 1 boisson alcool normale et 1 snack
When le festivalier remplace le snack par 1 repas
Then la commande est mise a jour avec les nouvelles lignes
And le cout total en jetons est recalcule selon les regles metier

Scenario: Refuser une modification qui depasse le solde disponible
Given un festivalier avec 1 jeton boisson restant
And une commande non acquittee contenant uniquement 1 boisson non alcoolisee
When le festivalier ajoute 1 boisson alcool premium a la commande
Then la modification est refusee pour solde insuffisant
And la commande initiale reste inchangee

Scenario: Creer une demande de changement quand la commande est deja acquittee
Given un festivalier proprietaire d'une commande au statut ACQUITTEE
When le festivalier demande le retrait d'un article de la commande
Then la commande initiale n'est pas modifiee immediatement
And une demande de changement est creee pour revue par le barman ou la barmaid

Scenario: Ajuster le debit de jetons apres retrait d'article
Given un festivalier avec une commande non acquittee contenant 1 repas et 1 boisson alcool normale
When le festivalier retire le repas de la commande
Then le nouveau cout de la commande est diminue de 3 jetons nourriture
And le solde de jetons du festivalier est ajuste en consequence

**Notes**
- Conserver une regle metier explicite liant le statut de commande aux operations de modification autorisees.
- Publier un evenement metier lors de la creation d'une demande de changement pour commande acquittee.