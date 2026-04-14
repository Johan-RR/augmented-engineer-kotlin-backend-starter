# Envoyer des rappels d'hydratation aux festivaliers : Application Module impact

**Contexte**
La couche application doit orchestrer l'envoi periodique des rappels d'hydratation, en appliquant les regles du domaine et en deleguant l'emission a l'infrastructure. Elle doit aussi garantir que les messages envoyes restent amicaux et orientent vers une consommation responsable.

**Acceptance Criteria**
Feature: Envoyer des rappels d'hydratation

Scenario: [1] Executer une campagne horaire et notifier tous les festivaliers
Given il est 16:00 un jour de festival
And des festivaliers actifs sont disponibles dans le systeme
When le use case d'envoi periodique est declenche
Then la couche application prepare un rappel d'hydratation pour chaque festivalier actif
And la couche application demande l'envoi d'un message amical promouvant l'hydratation responsable

Scenario: [2] A 30 minutes, ne notifier que les profils en frequence renforcee
Given il est 16:30 un jour de festival
And un festivalier a consomme 5 boissons alcoolisees dans la derniere heure
And un autre festivalier a consomme 2 boissons alcoolisees dans la derniere heure
When le use case d'envoi periodique est declenche
Then la couche application cible uniquement le festivalier en frequence renforcee
And aucun rappel n'est prepare pour le festivalier restant en frequence horaire

Scenario: [3] Ignorer les executions hors plage 11:00-19:00
Given il est 09:30 un jour de festival
When le use case d'envoi periodique est declenche
Then la couche application ne prepare aucune notification
And la couche application retourne un resultat indiquant qu'aucun envoi n'est du

Scenario: [4] Continuer les envois en cas d'echec ponctuel de notification
Given il est 17:00 un jour de festival
And 100 festivaliers doivent etre notifies
And l'envoi echoue pour 1 festivalier pour raison technique
When le use case d'envoi periodique est declenche
Then la couche application poursuit l'envoi pour les 99 autres festivaliers
And la couche application remonte un bilan de campagne avec succes et echecs

**Notes**
- Le contrat de sortie du use case doit permettre de tracer les envois realises et les echecs techniques.
- Un mapping explicite des erreurs techniques doit eviter de reessayer indefiniment dans la meme execution.
