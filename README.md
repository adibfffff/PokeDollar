PokeDollar
Application Android de suivi de collection et de cotes de cartes Pokémon.
L'objectif est de permettre à un utilisateur de constituer sa collection de cartes Pokémon et de suivre leur valeur au fil du temps, à partir de données récupérées via une API externe.
Statut du projet
🚧 En développement — phase initiale. La structure du projet est en place, aucune fonctionnalité n'est encore implémentée.
Stack technique
Langage : Kotlin
UI : Vues classiques (XML)
Build : Gradle (Kotlin DSL)
Stockage : local (solution à définir — Room ou SQLite envisagés)
Données cartes/cotes : API externe à définir
Roadmap
 Choisir l'API de données Pokémon TCG (cotes, images, infos cartes)
 Mettre en place le stockage local de la collection
 Écran de recherche de cartes
 Écran de collection personnelle (ajout / suppression de cartes)
 Suivi de l'évolution des cotes dans le temps
 Icône et identité visuelle de l'app
Installation
Cloner le dépôt :
   git clone https://github.com/adibfffff/PokeDollar.git
Ouvrir le dossier dans Android Studio.
Laisser Gradle synchroniser le projet.
Lancer l'application sur un émulateur ou un appareil physique.
Structure du projet
app/
 └── src/main/
      ├── java/...        # Code Kotlin
      ├── res/            # Ressources (layouts, drawables, icônes, valeurs)
      └── AndroidManifest.xml
Contribuer
Ce projet est développé et documenté au fil de l'avancement. Les choix techniques (API, stockage) seront précisés dans ce README à mesure qu'ils seront tranchés.
Licence
Non définie pour l'instant.
