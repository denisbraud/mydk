# Production - Administration

## Application - Web

L’application (SpringBootApp) s'appuie sur Java 11.

### Logs

Les logs de l'application se trouvent dans le fichier `todo`.

### Test de bon fonctionnement

| Test Connexion | Test Santé |
| --- | --- |
|Se connecter à l'aide de l'URL [/management/info](/management/info).|Vérifier que tous les composants sont UP : [/management/health](/management/health)|

### Surveillance applicative 

La supervision relève d’une surveillance applicative standard :  
Présence des processus en mémoire, consommation mémoire par les processus applicatifs, consommation des fichiers système dédiés, ... 
todo

## Base de données - PostgreSQL

La base de données est PostgreSQL 14.2.
La taille attendue de cette dernière est de 50 Mo de données.

PGAdmin 4 est est utilisé pour la gestion des données.

### Sauvegarde full

- Sauvegarde full manuelle
- Pas de Sauvegarde différentielle.
- Pas de sauvegarde externalisée.

***Restoration locale***

```
cd C:\Program Files\pgAdmin 4\v5\runtime\
pg_restore.exe --host "localhost" --port "5432" --username "@project.artefactId@" --no-password --dbname "@project.artefactId@" --clean --verbose "@project.artefactId@-dd-MM-yyyy.backup"
```

### Conservation des données / RGPD

cf. [RGPD](120-homologation.md)
