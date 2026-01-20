# README

## Initialisation et lancement des applications


### Base de données

La base de données se crée au démarrage du back-end, via le `compose.yaml` à la racine du répertoire /back.
Elle est configurée pour être recrée entièrement à chaque démarrage du back-end, pour faciliter les différents tests, autant sur la partie back-end que front-end.
Un fichier `./back/src/main/resources/sql/init.sql` est chargé à chaque initialisation pour ajouter des premières données "fixtures" pour faciliter les tests.

### Back-end

Lancer la commande suivante :
-       mvn spring-boot:run


### Front-end

Lancer la commande suivante :
-       ng serve


## Utilisation

-       http://localhost:4200 (application : web-app + API)
        http://localhost:8080 (API)


## Fonctionnement
    
### Authentification
La quasi-totalité des routes nécessite d'être authentifié.

Depuis Postman ou équivalent, après le login et la récupération du token, il faut que celui-ci soit joint comme Bearer Token, et ce pour chaque requête :
-       Token : monSuperTokenBienLongJusteIci
        Prefix : bearer 
Le token est valide durant 24 heures.

## Développement

L'API a été réalisé en Java 21 avec Spring 4 et Maven 3.
La web APP est elle en Angular 19.

## Tests

### Front-end
Lancer la commande :
-       npm run test:coverage
Le rapport de coverrage est en suite disponible dans `./front/coverage/site/index.html`.

Coverage réalisé à 95% environ.

### End-to-end
Au préalable il faut démarrer l'API / back-end et la web-app / front-end.
Lancer ensuite la commande :
-       npm run cypress:run
Puis pour avoir le coverrage lancer la commande:
-       npm run cypress:coverage
Le rapport de coverrage est en suite disponible dans `./front/cypress/reports/report.json` et également dans `./front/mochawesome-report/report.html` de manière plus visuel.

Coverage des différents cas mentionnés dans le plan de testing réalisé.

### Back-end
Au préalable il faut démarrer l'API.
Lancer ensuite la commande :
-       mvn clean test
Le rapport de coverrage est en suite disponible dans `./back/target/site/index.html`.

H2 a été utilisé pour effectuer les tests avec une base de données.

Coverage réalisé à 90% environ.
