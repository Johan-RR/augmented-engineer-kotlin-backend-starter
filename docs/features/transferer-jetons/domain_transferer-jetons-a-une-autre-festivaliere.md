# Transferer des jetons a une autre festivaliere : Domain Module impact

**Contexte**
Le domaine doit permettre a une festivaliere source de transferer des jetons boisson et/ou nourriture a une festivaliere destinataire, dans la limite maximale autorisee, sans jamais produire de solde negatif. Le transfert est initie en statut en attente et ne devient effectif qu'apres confirmation explicite par la destinataire.

**Critères d'acceptation**
Feature: Transferer des jetons a une autre festivaliere

Scenario: Creer un transfert en attente dans la limite autorisee
Given une festivaliere source avec 6 jetons boisson et 9 jetons nourriture
And une festivaliere destinataire existante
When la source initie un transfert de 2 jetons boisson et 3 jetons nourriture
Then un transfert est cree avec le statut EN_ATTENTE
And aucun debit ni credit de jetons n'est applique tant que le transfert n'est pas confirme

Scenario: Refuser un transfert depassant la limite de trois jetons par type
Given une festivaliere source avec un solde suffisant
And une festivaliere destinataire existante
When la source initie un transfert de 4 jetons boisson et 1 jeton nourriture
Then le transfert est refuse pour depassement de la limite par type
And aucun mouvement de jetons n'est enregistre

Scenario: Refuser un transfert qui rendrait le solde source negatif
Given une festivaliere source avec 1 jeton boisson et 0 jeton nourriture
And une festivaliere destinataire existante
When la source initie un transfert de 2 jetons boisson et 0 jeton nourriture
Then le transfert est refuse pour solde insuffisant
And le solde de la source reste inchange

Scenario: Confirmer un transfert applique le debit et le credit de facon atomique
Given un transfert EN_ATTENTE de 1 jeton boisson et 2 jetons nourriture entre deux festivaliere existantes
When la destinataire confirme le transfert
Then le transfert passe au statut CONFIRME
And la source est debitee de 1 jeton boisson et 2 jetons nourriture
And la destinataire est creditee de 1 jeton boisson et 2 jetons nourriture

Scenario: Refuser la confirmation d'un transfert deja confirme
Given un transfert deja au statut CONFIRME
When une confirmation supplementaire est demandee
Then la confirmation est refusee car le transfert n'est plus en attente
And aucun mouvement de jetons supplementaire n'est applique

**Notes**
- Modeliser explicitement le cycle de vie du transfert (EN_ATTENTE, CONFIRME, REFUSE/EXPIRE si necessaire).
- Le debit source et le credit destinataire doivent etre atomiques du point de vue metier.
