# Git Flow

- [Doc GitFlow](https://www.atlassian.com/fr/git/tutorials/comparing-workflows/gitflow-workflow)
- [Commandes équivalentes](https://gist.github.com/JamesMGreene/cdd0ac49f90c987e45ac)

## Issue > Branche feature/xx + MR

La réalisation d'une tâche (Issue) se traduit par la création d'une `feature branch` et d'une `Merge Request` (MR).

***Créer une feature branche et une MR associées à l'issue***

- Aller sur la tâche (issue) sous GitLab
- Prendre en copier le no de l'issue et son titre
- Dans un fichier texte, coller et ajouter l'équivalent code de l'issue : `#nn Titre Issue` + `nn-titre-issue`

**Créer une nouvelle branche**

- Repository > Branches
- [New Branch]
- Branch Name : `feature/nn-titre-issue`
- Create from branch : `develop`

**Créer une nouvelle MR**

- Merge Requests : [New]
- Source Branch : `feature/nn-titre-issue` Target Branch : `develop`
- Description : Closes `#nn`
- Renseigner Assignee + Milestone + labels
- [Submit Merge Request]

> *Note : version gitLab obsolète*
>
> Toutes ces opérations sont normalement réalisées automatiquement par GitLab :
> 
> - Aller sur la tâche (issue) sous GitLab
> - Cliquer sur le bouton [Create a merge request]
> 
> Malheureusement avec notre version actuelle de GitLab, la branche créée **ne contient pas le préfixe "feature/"** rendant cette fonction incompatible avec notre process GitFlow.

***CHANGELOG***

Aller sur `src/site/markdown/210-CHANGELOG. md` de la branche `feature/nn-titre-issue`

- Ajouter `#nn Titre Issue` dans `210-CHANGELOG. md`
- Commit changes

## Dév. et Merge sur branche develop

La branche est prête pour le dev.

- `git flow feature checkout {feature_name}`
- ou équivalent `git checkout feature/{feature_name}`

***Fin du dev et des tests : Submit Merge Request***

- Aller sur la MR
- Supprimer le préfixe `WIP: `
- Choisir un relecteur via le champ Assignee
- Save changes

Une fois, la MR relue et acceptée par le relecteur, cette branche feature peut être mergée sur `develop`.

***Merge feature branch***

- Git : Fetch from origin

Si des modifications ont eu lieu sur `develop`

- Switch to `develop`
- Pull
- Switch to `feature/nn-titre-issue`
- Git flow > Rebase feature
- Résoudre les conflits éventuels
- Dans History, les branches `develop` et `origin/develop` doivent être au même niveau et **avant** les commits de la branche `feature/nn-titre-issue`
- Git flow > Finish feature
- Switch to `develop`
- Push to origin

La branche `feature/nn-titre-issue` est supprimée en local mais pas sur le serveur.

- Aller sur la `MR` (celle-ci doit avoir le statut `Merged`)
- [x] Remove source branch

## Release

***Version suppression -SNAPSHOT***

- pom.xml : revision suppr. "-SNAPSHOT"
- Push commit 'version Go.Ro.Co'

***GitFlow Release : Start and Finish***

- Git : Fetch from origin
- Switch to `master`
- Pull
- Switch to `develop`
- Pull
- Git flow > Start Release : nom=Go.Ro.Co (ie. branch release/Go.Ro.Co)
- Git flow > Finish Release
- Switch to `master`
- Push + Push tag
- Switch to `develop`
- git merge --no-ff master
- reword commit `Merge branch 'master' into develop` in `Merge tag 'Go.Ro.Co' into develop`

***Version +1***

Incrémenter la version

- pom.xml : revision (add -SNAPSHOT) + milestone_id + milestone_title
- */package.json : "version"
- 210-CHANGELOG.md

## Numérotation des versions

Go.Ro.Co ou MAJOR.MINOR.PATCH

- Go : Génération / Version majeure : modification du modèle et/ou de l'API (release au sens GitFlow, milestone au sens GitLab)
- Ro : Révision / Version mineure : (release au sens GitFlow, milestone au sens GitLab)
- Co : Correctif / Patch : correction non prévue, non planifiée (hotfix au sens GitFlow)

Références :

- https://www.wikiwand.com/fr/Version_d%27un_logiciel
- https://semver.org/

