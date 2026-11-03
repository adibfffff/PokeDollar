# PokeDollar

Application Android pour gérer sa collection de cartes Pokémon TCG et connaître sa valeur.
Projet INFO507 (L3 Informatique, USMB).

## Fonctionnalités
- **Accueil** : valeur totale de la collection, les 3 cartes les plus chères, partage de la valeur de la collection.
- **Recherche** : recherche de cartes par nom sur l'API TCGdex, en direct pendant la frappe.
- **Collection** : grille des cartes possédées, triée de la plus chère à la moins chère, avec le nombre total de cartes.
- **Détail** : image, nom, série, prix Cardmarket ; ajouter ou retirer un exemplaire.
- **Profil** : choix d'un pseudo.

## Contraintes du projet
| Contrainte | Dans l'app |
|---|---|
| Liste navigable | Grilles Recherche et Collection, un clic ouvre le détail |
| Service externe | API TCGdex (`https://api.tcgdex.net/v2/fr`) avec Ktor |
| Stockage local | Room (collection) et DataStore (pseudo) |
| Fonctionnalité du smartphone | Partage par intention implicite `ACTION_SEND` |

## Technique
- Kotlin + Jetpack Compose (Material 3), une seule activité
- Navigation 3 avec barre de navigation en bas
- Ktor + kotlinx.serialization pour l'API, Coil pour les images
- Room et DataStore pour le stockage local
- Architecture en couches : Écran → ViewModel → UseCase → Repository → Room / API

## Structure
```
app/src/main/java/td/info507/pokedollar/
├── domain/       modèles (Card…) et UseCases
├── local/        Room (base, DAO, entité) et DataStore
├── remote/       client Ktor et API TCGdex
├── repository/   CardRepository, UserRepository
└── ui/           écrans, composants, navigation, thème
```

## Lancer le projet
1. Ouvrir le dossier dans Android Studio et laisser Gradle synchroniser.
2. Lancer sur un émulateur ou un téléphone (Android 7.0 minimum) avec accès à Internet.
