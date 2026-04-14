# Envoyer des rappels d'hydratation aux festivaliers : Domain Module impact

**Contexte**
Le domaine doit definir les regles metier qui determinent quand un rappel d'hydratation est du et a quelle frequence il doit etre envoye. Les rappels sont horaires pour tous les festivaliers entre 11:00 et 19:00, avec une frequence renforcee toutes les 30 minutes pour les festivaliers ayant consomme plus de 3 boissons alcoolisees sur la derniere heure.

**Acceptance Criteria**
Feature: Envoyer des rappels d'hydratation

Scenario: [1] Envoyer un rappel horaire a tous les festivaliers pendant la plage autorisee
Given il est 14:00 un jour de festival
And la plage d'envoi autorisee est de 11:00 a 19:00
When le domaine evalue les rappels a declencher pour ce slot
Then tous les festivaliers sont eligibles a un rappel d'hydratation
And la frequence appliquee est horaire

Scenario: [2] Appliquer une frequence de 30 minutes au dela de 3 boissons alcoolisees
Given il est 14:30 un jour de festival
And un festivalier a consomme 4 boissons alcoolisees dans la derniere heure
When le domaine evalue les rappels a declencher pour ce slot
Then ce festivalier est eligible a un rappel d'hydratation
And la frequence appliquee est de 30 minutes

Scenario: [3] Ne pas renforcer la frequence lorsque le seuil n'est pas depasse
Given il est 15:30 un jour de festival
And un festivalier a consomme exactement 3 boissons alcoolisees dans la derniere heure
When le domaine evalue les rappels a declencher pour ce slot
Then ce festivalier n'est pas marque en frequence renforcee
And la frequence appliquee reste horaire

Scenario: [4] Ne declencher aucun rappel en dehors de la plage horaire
Given il est 20:00 un jour de festival
And la plage d'envoi autorisee est de 11:00 a 19:00
When le domaine evalue les rappels a declencher pour ce slot
Then aucun rappel d'hydratation n'est declenche

**Notes**
- Le domaine doit expliciter la comparaison stricte "plus de 3" et non "au moins 3".
- La timezone du festival doit etre centralisee au niveau des regles metier pour eviter les ambiguities.