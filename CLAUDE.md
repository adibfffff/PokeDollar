# Pokedollar — projet INFO507 (L3 Informatique, USMB)

App Android de gestion d'une collection de cartes Pokémon TCG, en Kotlin + Jetpack Compose.
Rendu : **jeudi 5 novembre 2026**.

## Comment m'accompagner (important)
- Je suis étudiant de L3, **débutant** en Android : code simple, niveau étudiant (pas pro), commentaires courts.
- N'utiliser **que les notions du cours et des TD** (fichiers dans `docs/cours/`). Si quelque chose ne vient pas du cours, **le signaler clairement** (« hors cours »).
- Ne rien inventer : si un doute existe sur une API, une version ou une bibliothèque, le dire au lieu de deviner.
- Avancer **une notion / un fichier à la fois** ; ne pas tout modifier d'un coup sans que je comprenne.
- Pour chaque fichier : **chemin complet**, code, explication courte de chaque changement.
- Je veux m'approprier le code : expliquer avant de modifier, et me laisser coder quand je le demande.
- Répondre uniquement à la question posée quand je pose une question.

## Le cours (à respecter)
@docs/cours/info507-compose-reference.md
@docs/cours/fiches-revision-android.md
@docs/cours/enonce-projet-et-liste-td.md
@docs/cours/td2-icone-et-premiere-vue.md
@docs/cours/td3-activites-intentions-permissions.md
@docs/cours/td4-navigation-et-liste.md
@docs/cours/td5-stockage-room.md
@docs/cours/td6-services-externes.md

## Contraintes du projet (toutes remplies)
| Contrainte | Où |
|---|---|
| Liste navigable | grilles Recherche et Collection (`LazyVerticalGrid`) → Détail |
| Service externe | API TCGdex avec Ktor (`remote/`) |
| Stockage local | Room (`local/`) ; DataStore prévu (TD6 §5) |
| Fonctionnalité smartphone | partage de la collection (intent implicite `ACTION_SEND`, accueil) |

## Technique
- Package `td.info507.pokedollar`, sources dans `app/src/main/java/td/info507/pokedollar/`.
- Thème **sombre uniquement** (noir et doré), police Montserrat (`res/font`), pas de mode clair.
- Navigation 3 (`ui/navigation/Destination.kt`, `NavHost.kt`) avec barre du bas (Accueil, Collection, Recherche, Profil), icônes sans texte.
- API : TCGdex `https://api.tcgdex.net/v2/fr`, gratuite et sans clé.
  - Recherche `/cards?name=…` → tableau résumé (id, name, image ; **pas** de set ni de prix).
  - Détail `/cards/{id}` → avec `set.name` et `pricing.cardmarket.avg` (€).
  - Image : URL + `/high.webp` (ou `/low.webp`).
- Images distantes : Coil 3 (`SubcomposeAsyncImage` avec roue de chargement, `ic_card_placeholder` si absente/erreur).

## Architecture (fiche 12 : chaque couche ne parle qu'à celle du dessous)
```
Écran → ViewModel → UseCase → CardRepository ─┬─► local/  (Room)  CardEntity
                                               └─► remote/ (Ktor)  RemoteCard
```
- `domain/data/Card.kt` : id, name, image?, price?, setName?, quantity = 1.
- `local/` : `entity/CardEntity` (+ toCard/fromCard), `dao/CardDao` (getAll, getById, findById, upsert, delete), `PokeDollarDatabase` + `PokeDollarDatabaseHolder`, initialisé dans `PokeDollarApplication` (déclarée dans le manifeste).
- `remote/` : `HttpClientFactory` (expectSuccess, ContentNegotiation ignoreUnknownKeys, HttpTimeout 15 s, pas de Logging), `RemoteCard` (+ RemoteSet, RemotePricing, RemoteCardmarket, toCard), `TcgdexApi` (searchCards, getCard).
- `repository/CardRepository` : Room (getCollection, getCard, addOneCopy, removeOneCopy…) + API (searchCards, fetchCard).
- `domain/usecase/` : GetCollectionUseCase, GetCollectionCardUseCase, SearchCardsUseCase, GetCardDetailUseCase, AddCopyUseCase, RemoveCopyUseCase (écritures et API avec `runCatching` → `Result`).
- `ui/feature/` : home (HomeScreen + HomeViewModel : total, top 3, collection vide), collection, search (SearchViewModel), detail (CardDetailViewModel avec `load(cardId)`), profile.
- `ui/component/` : CardItem (image + prix + série), PrimaryButton.

## Décisions déjà prises
- Pas de photo de profil, pas de géolocalisation (TD3 notions 3-5 non appliquées).
- Détail : image, puis nom + série à gauche et prix à droite ; bouton « Ajouter » pleine largeur ; si possédée, bouton (4f) + corbeille (1f) qui retire **un** exemplaire.
- Grille 2 colonnes pour Collection et Recherche ; la recherche n'affiche pas les prix (l'API ne les donne pas).
- Animations : seulement en bonus de fin s'il reste du temps.

## Problème connu à traiter
- Les ViewModels obtenus avec `viewModel()` sont liés à l'activité : plusieurs écrans Détail ouverts successivement partagent le **même** `CardDetailViewModel` (quantités qui « sautent »). À corriger proprement, en expliquant la solution et en signalant si elle est hors cours.

## Reste à faire
- DataStore (TD6 §5) : préférences (pseudo, etc.) + écran Profil.
- Mise à jour des prix de la collection (synchronisation, pull-to-refresh éventuel).
- Cas vides / erreurs / hors-ligne (recherche sans résultat, collection vide).
- README, captures, démo de 3 min, relecture du code pour l'oral.

## Git (je l'apprends, en terminal)
- Dépôt : `github.com/adibfffff/PokeDollar`, branche principale `main`.
- **Ne jamais coder directement sur `main`** : une branche par fonctionnalité (`feature/…`), fusionnée par Pull Request (`gh pr create` / `gh pr merge --merge --delete-branch`) seulement quand elle marche.
- Commits réguliers, petits et précis, messages en français (ex. « TD6 : écran Recherche branché sur l'API »).
