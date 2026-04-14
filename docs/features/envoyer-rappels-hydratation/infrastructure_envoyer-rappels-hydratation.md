# Envoyer des rappels d'hydratation aux festivaliers : Infrastructure Module impact

**Contexte**
La couche infrastructure doit fournir les mecanismes techniques de planification, de collecte des donnees de consommation et d'emission des notifications vers le canal choisi. Elle doit garantir des envois fiables, sans doublons sur un meme slot, et respectant les horaires definis.

**Acceptance Criteria**
Feature: Envoyer des rappels d'hydratation

Scenario: [1] Declencher automatiquement la campagne horaire entre 11:00 et 19:00
Given la planification de campagne est active
When l'horloge atteint une heure pleine comprise entre 11:00 et 19:00
Then l'infrastructure declenche une execution de campagne d'hydratation
And l'execution est tracee avec l'horodatage du slot

Scenario: [2] Declencher un slot supplementaire toutes les 30 minutes pour les profils renforces
Given la planification de campagne est active
When l'horloge atteint une demi-heure comprise entre 11:30 et 18:30
Then l'infrastructure declenche une execution destinee aux profils en frequence renforcee
And l'execution est tracee avec le type de slot THIRTY_MINUTES

Scenario: [3] Calculer le nombre de boissons alcoolisees sur une fenetre glissante d'une heure
Given un historique de consommations horodatees est disponible en persistance
When l'adaptateur lit les consommations d'un festivalier a 15:30
Then seules les consommations alcoolisees entre 14:30 et 15:30 sont comptees
And le resultat est fourni au domaine pour decider de la frequence

Scenario: [4] Eviter les doublons d'envoi sur un meme festivalier et un meme slot
Given une execution de campagne pour le slot 16:00 a deja ete enregistree pour un festivalier
When une seconde execution technique tente de publier le meme rappel pour le meme slot
Then l'infrastructure bloque la double emission
And une trace d'idempotence est enregistree

Scenario: [5] Ne pas executer de campagne hors plage horaire
Given la planification de campagne est active
When l'horloge atteint 20:00
Then aucune execution de campagne n'est declenchee
And aucun message n'est emis vers le canal de notification

**Notes**
- Le mecanisme d'idempotence peut s'appuyer sur une cle composee festivalierId plus slotHorodatage.
- La timezone du festival doit etre configurable au niveau infrastructure et alignee avec la couche domaine.