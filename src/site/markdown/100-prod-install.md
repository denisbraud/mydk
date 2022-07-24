# Processus d'installation

## Architecture générale

@project.name@ est composé : 

- d’une application web

### Livrables

- [@project.artefactId@-{Go.Ro.Co}.jar](http://nexus.todo/#browse/browse:maven-releases:com/@project.artefactId@/) : Java SpringBoot App **serveur APP** ([Installation applicative > APP](#app)).
- d'une base de données PostgreSQL

### Plateformes

| Environnement | Serveur | Type | URL |
| ------------- | ------- | ---- | --- |
| Production    | srvprod | todo | [@project.url@](@project.url@) |
| Recette       | srvrec  | todo | <http://recapp.todo> |
| Développement | srvrec  | todo | <http://devapp.todo> |

### Procédures pour l'installation

- S'il s'agit de la 1ere installation, effectuer d'abord les étapes de l'[Installation initiale](#installation_initiale)

Dans le cadre usuel d'une mise à jour :

- Suivre les étapes de l'[Installation applicative](#installation_applicative)

## installation_initiale

### serveur APP

Les logiciels suivants doivent être installés : 

- `JDK11`

Le serveur doit être autorisé à l’envoi de mail par SMTP pour le process `java.exe`.

### serveur BDD

La Base De Données est installée sur le même serveur que l'application (**serveur APP**).

Les logiciels suivants doivent être installés :

- PostgreSQL 14.2

### serveur SMTP

Le serveur SMTP utilisé en production est : **smtp.todo**

 ---
 
> Le serveur SMTP utilisé sur la machine de développement est FakeSMTP sur le port 2033.

  ---

## installation_applicative

### app

***Livrable*** : [@project.artefactId@-{Go.Ro.Co}.jar](http://nexus.todo/#browse/browse:maven-releases:com/@project.artefactId@/)

#### Procédure d'Installation

todo

#### Tests Post-Installation

La page [/management/health](/management/health) répond et a un statut 200.
