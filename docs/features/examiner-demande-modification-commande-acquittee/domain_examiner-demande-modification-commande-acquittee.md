# Examiner une demande de modification de commande acquittee : Domain Module impact

**Contexte**
Le domaine doit permettre au barman ou a la barmaid de reviser une demande de modification d'une commande deja acquittee. La demande peut etre acceptee ou refusee. L'acceptation n'est autorisee que si au moins un article deja prepare peut etre transfere vers une autre commande. En cas d'acceptation, le nouveau temps estime de preparation doit etre recalcule.

**Critères d'acceptation**
Feature: Examiner une demande de modification de commande acquittee

Scenario: Accepter une demande quand un article prepare est transferable
Given une commande au statut ACQUITTEE avec une demande de changement en attente
And au moins un article deja prepare de cette commande est transferable vers une autre commande
When le barman accepte la demande de changement
Then la demande passe au statut ACCEPTEE
And la commande est mise a jour selon les changements valides
And le temps estime de preparation de la commande est recalcule

Scenario: Refuser l'acceptation quand aucun article prepare n'est transferable
Given une commande au statut ACQUITTEE avec une demande de changement en attente
And aucun article deja prepare de cette commande n'est transferable vers une autre commande
When le barman tente d'accepter la demande de changement
Then la demande est refusee avec la raison NO_TRANSFERABLE_PREPARED_ITEM
And la commande reste inchangee

Scenario: Refuser explicitement une demande de changement
Given une commande au statut ACQUITTEE avec une demande de changement en attente
When le barman refuse la demande de changement avec un motif metier
Then la demande passe au statut REFUSEE
And la commande reste inchangee

Scenario: Interdire la revue d'une demande deja traitee
Given une demande de changement deja au statut ACCEPTEE
When le barman tente de la traiter une seconde fois
Then l'operation est rejetee pour etat de demande invalide
And aucune modification supplementaire n'est appliquee

**Notes**
- Conserver une invariance metier: seule une demande EN_ATTENTE associee a une commande ACQUITTEE est eligible a la revue.
- Exposer une raison metier explicite pour les refus automatiques afin de faciliter le mapping applicatif.