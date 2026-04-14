# Transferer des jetons a une autre festivaliere : Infrastructure Module impact

**Contexte**
La couche infrastructure doit persister les transferts de jetons (etat et montants) et garantir l'application fiable des mouvements de soldes lors de la confirmation. Elle implemente les adapters de persistance requis par le domaine, avec des garanties transactionnelles et de concurrence pour eviter les doubles confirmations.

**Critères d'acceptation**
Feature: Transferer des jetons a une autre festivaliere

Scenario: Persister un transfert en attente avec ses metadonnees
Given une demande metier de creation de transfert valide
When l'adapter de persistance enregistre le transfert
Then un enregistrement est cree avec le statut EN_ATTENTE
And les identifiants source et destinataire ainsi que les montants par type sont persistés

Scenario: Appliquer la confirmation dans une transaction atomique
Given un transfert EN_ATTENTE persiste
And les soldes source et destinataire en base
When l'adapter de persistance confirme le transfert
Then le statut du transfert est mis a jour en CONFIRME
And le solde source est debite et le solde destinataire est credite dans la meme transaction

Scenario: Empêcher une double confirmation concurrente
Given un transfert EN_ATTENTE persiste
And deux confirmations concurrentes sont soumises
When l'infrastructure traite ces confirmations
Then une seule confirmation aboutit
And la seconde tentative est rejetee de facon controlee sans double debit

Scenario: Retourner un transfert introuvable de maniere explicite
Given un identifiant de transfert inexistant
When l'adapter de persistance est sollicite pour confirmation
Then une erreur explicite de transfert introuvable est retournee
And aucun mouvement de jetons n'est applique

**Notes**
- Utiliser un controle de concurrence (verrou optimiste ou condition sur statut) pour proteger la transition EN_ATTENTE -> CONFIRME.
- Ajouter des tests d'integration repository sur la transactionnalite et les cas concurrents.
