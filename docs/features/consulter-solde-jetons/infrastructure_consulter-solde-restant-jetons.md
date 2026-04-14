# Consulter le solde restant de mes jetons : Infrastructure Module impact

**Contexte**
La couche infrastructure doit fournir les données nécessaires au calcul du solde de jetons de la journée courante, notamment la dotation journalière et les consommations du festivalier. Elle implémente les ports de persistance attendus par le domaine avec des requêtes fiables et cohérentes.

**Critères d'acceptation**
Feature: Consulter le solde restant de mes jetons

Scenario: Charger la dotation journalière d'un festivalier pour le jour courant
Given un festivalier existant avec une dotation configurée pour le jour courant
When l'adapter de persistance est sollicité pour récupérer la dotation
Then l'infrastructure retourne 6 jetons boisson et 9 jetons nourriture pour ce jour

Scenario: Charger les consommations de la journée courante uniquement
Given un festivalier ayant des consommations sur plusieurs jours
When l'adapter de persistance est sollicité pour récupérer les consommations
Then seules les consommations du jour courant sont retournées
And les consommations des autres jours sont exclues

Scenario: Retourner une consommation nulle en l'absence de commandes
Given un festivalier existant sans consommation sur la journée courante
When l'adapter de persistance est sollicité pour récupérer les consommations
Then l'infrastructure retourne 0 jeton boisson consommé et 0 jeton nourriture consommé

Scenario: Propager une erreur technique de lecture de manière contrôlée
Given une indisponibilité de la source de données
When l'adapter de persistance est sollicité pour récupérer la dotation ou les consommations
Then une erreur technique explicite est retournée vers la couche appelante
And aucune donnée partielle incohérente n'est renvoyée

**Notes**
- Le filtrage temporel doit être défini selon le fuseau horaire officiel du festival.
- Ajouter des tests d'intégration repository pour valider les requêtes et le mapping des données.
