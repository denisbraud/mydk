# Gestion de projet

---

> - ***Projet :*** **@project.name@**
> - ***URL Prod :*** [@project.url@](@project.url@)
> - ***Contact :*** [Intervenants](080-intervenants.md)
> - ***Doc :*** [@project.url@/doc/000-intro.html](@project.url@/doc/000-intro.html)
> - ***Suivi :*** [@scm.url@/boards](@scm.url@/boards)

---

## livrables

Les livrables sont stockés sur le nexus :

- @project.name@-{Go.Ro.Co}.zip (http://nexus.todo/#browse/browse:maven-releases:com/@project.artefactId@/)

Mode opératoire d'installation :

- [@project.url@/doc/100-prod-install.html#installation_applicative](@project.url@/doc/100-prod-install.html#installation_applicative)

## Suivi des tâches

Le suivi de l'avancement des tâches (Issues) s'effectue via GitLab.

- %@milestone_id@ : Prochaine version

***États***

- [Closed](@scm.url@/issues?state=closed) : Tâche terminée en développement
- [Closed](@scm.url@/issues?state=closed&label_name[]=Tested) + ~Tested : Tâche testée en Recette

***Labels***

Les labels suivants permettent de retrouver les tâches `EnAttente` bloquées :

- ~EnAttenteMOA
- ~EnAttenteINFRA

## Documentation complète

La documentation complète du projet est hébergée dans GIT au format markdown, répertoire [src/site/markdown](@scm.url@/tree/feature/doc/src/site/markdown).  
Celle-ci est maintenu en parallèle des développements par tous les intervenants ( [mode opératoire](290-doc.md) ).

- PROD : [@project.url@/doc/000-intro.html](@project.url@/doc/000-intro.html)
- dev : <http://appdev.todo/doc/000-intro.html>

## Application / Plateformes

| Plateforme | DNS                            | Machine               | IP                      |
| ---------- | ------------------------------ | --------------------- | ----------------------- |
| PROD       | [@project.url@](@project.url@) | <http://srvprod.todo> | <http://127.0.0.1>      |
| Recette    |                                | <http://srvrec.todo>  | <http://127.0.0.1>      |
| Dev        |                                | <http://srvdev.todo>  | <http://127.0.0.1>      |
