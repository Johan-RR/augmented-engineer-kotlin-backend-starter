# Consulter le solde restant de mes jetons : Domain Module impact

**Contexte**
Le festivalier doit pouvoir consulter à tout moment son solde de jetons boisson et nourriture pour la journée courante du festival. Le domaine doit garantir les règles de calcul du solde restant, en tenant compte des dotations quotidiennes et des consommations enregistrées, sans jamais produire de solde négatif.

**Critères d'acceptation**
Feature: Consulter le solde restant de mes jetons

Scenario: Retourner un solde correct pour un festivalier avec consommations partielles
Given un festivalier avec une dotation de 6 jetons boisson et 9 jetons nourriture pour la journée courante
And des consommations de 2 jetons boisson et 4 jetons nourriture sur cette même journée
When le cas d'usage de consultation du solde est exécuté
Then le solde retourné est de 4 jetons boisson et 5 jetons nourriture

Scenario: Retourner un solde nul quand tous les jetons de la journée sont consommés
Given un festivalier avec une dotation de 6 jetons boisson et 9 jetons nourriture pour la journée courante
And des consommations de 6 jetons boisson et 9 jetons nourriture sur cette même journée
When le cas d'usage de consultation du solde est exécuté
Then le solde retourné est de 0 jeton boisson et 0 jeton nourriture

Scenario: Ne jamais retourner de solde négatif même en présence de données incohérentes
Given un festivalier avec une dotation de 6 jetons boisson et 9 jetons nourriture pour la journée courante
And des consommations incohérentes de 8 jetons boisson et 12 jetons nourriture sur cette même journée
When le cas d'usage de consultation du solde est exécuté
Then le solde retourné est borné à 0 jeton boisson et 0 jeton nourriture

Scenario: Ignorer les jetons non dépensés des jours précédents
Given un festivalier avec un reliquat non dépensé de la veille
And une dotation complète de 6 jetons boisson et 9 jetons nourriture pour la journée courante
And aucune consommation sur la journée courante
When le cas d'usage de consultation du solde est exécuté
Then le solde retourné correspond uniquement à la dotation de la journée courante

**Notes**
- Modéliser explicitement le jour de festival dans les objets métier impliqués par le calcul du solde.
- Le calcul du solde doit rester déterministe et testable indépendamment des couches techniques.
