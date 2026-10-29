# INFO507 — Énoncé du projet (TP) et liste des TD

## Énoncé du projet (Moodle, section TP)
Objectif : concevoir et développer une application mobile de bout en bout. Thème libre (social, divertissement, pratique...). Projet original qui inclut TOUS les points suivants :
- Une liste navigable ;
- Une connexion à un service externe ;
- Un stockage des données en local ;
- Un accès à (au moins) une fonctionnalité du smartphone.

Seul ou par deux (recommandé). Composants Material Design 3 avec Compose : https://m3.material.io/components

Service externe : la création du service se limite à un simple fichier statique placé sur le serveur, disponible à http://51.68.91.213/<login>/<fichier> (ex. http://51.68.91.213/gr-0-0/cards.json). On est libre d'utiliser d'autres services externes.

Pour accéder à un service http (sans s), ajouter dans AndroidManifest.xml :
```xml
<uses-permission android:name="android.permission.INTERNET" />
<application android:usesCleartextTraffic="true" ...>
```

Livrables à chaque séance :
- Séance 1 : présenter le thème de l'application et les maquettes des interfaces.
- Séance 2 : présenter le modèle de l'application et l'implémentation du stockage des données.
- Séance 3 : présenter les données envoyées depuis le service.

## Liste des TD (app fil rouge MyWeather)
Objectif : concevoir et développer une application mobile de bout en bout qui permet de connaître la météo du moment.
- TD1 — Ergonomie & Design (sur papier)
- TD2 — Prise en main d'Android Studio : icône et première vue → td2-icone-et-premiere-vue.md
- TD3 — Activités, intentions et permissions → td3-activites-intentions-permissions.md
- TD4 — Navigation et gestion d'une liste avec ses données → td4-navigation-et-liste.md
- TD5 — Stockage des données en local (Room) → td5-stockage-room.md
- TD6 — Récupération de données via des services externes (+ DataStore) → td6-services-externes.md

## CM
« 6 - Compose-2_merged.pdf » (CM D. Wayntal + intro Compose F. N'Gbala). Partie Stockage p.154-158, Communication/Ktor p.159-163. Résumé : info507-compose-reference.md. Fiches de révision : fiches-revision-android.md.
