# Cours INFO507 — Référence pour Pokedollar

Source : « 6 - Compose-2_merged.pdf » (CM D. Wayntal + intro Compose F. N'Gbala). Résumé fidèle au cours : c'est **la façon de faire attendue** pour le projet.

## Contraintes du projet (obligatoires)
Application mobile de bout en bout, thème libre, originale, qui inclut **TOUS** :
1. Une **liste navigable**
2. Une **connexion à un service externe** (API)
3. Un **stockage des données en local**
4. Un **accès à au moins une fonctionnalité du smartphone** (appareil photo, GPS, notifications, fichiers, biométrie…)

Choix pédagogiques : app **native Android en Kotlin**, vues en **Jetpack Compose** (PAS XML), binôme max, test sur émulateur ou téléphone, éviter le copier/coller.

## Stack imposée / montrée en cours
| Besoin | Techno du cours |
|---|---|
| UI | Jetpack Compose + Material 3 (`MaterialTheme`) |
| Liste | `LazyColumn` + `items(..., key = {...})` |
| Navigation | **Navigation 3** : `NavKey`, `rememberNavBackStack`, `NavDisplay`, `entryProvider { entry<...> }` |
| Images distantes | **Coil** : `rememberAsyncImagePainter(model = url)` |
| Réseau | **Ktor client** (OkHttp + ContentNegotiation + kotlinx.serialization + Logging) — pas Retrofit |
| Stockage simple | **DataStore** (préférences clé/valeur : thème, id…) |
| Stockage structuré | **Room** (`@Entity data class`) ou SQLite |
| Fichiers | `openFileOutput("data.json", MODE_PRIVATE)` (JSON/CSV) |
| Textes | `res/values/strings.xml` + `stringResource(R.string.x)` (1 fichier par langue) |
| Git | dépôt versionné (menu Version Control d'Android Studio ou terminal) |

## Structure d'un projet
- `AndroidManifest.xml` : icône, nom (`@string/app_name`), thème, activités, **permissions** (`<uses-permission android:name="android.permission.INTERNET" />`).
- `kotlin+java/` : sources, `test/` (tests unitaires métier), `androidTest/`.
- `res/` : `drawable`, `mipmap` (icône launcher), `values` (colors, strings, styles/themes), `font`, `raw`.
- `Gradle Scripts` : minSdk/targetSdk/compileSdk, package, version, dépendances (`libs.versions.toml`).

## Kotlin (rappels du cours)
- `val` (immuable, recommandé) / `var` ; inférence de type ; `const val` ; pas de `static` → `companion object`.
- Null-safety : `String?`, `?.`, `if (x != null)`.
- Templates : `"Hello $mot"`.
- Fonctions `fun`, expression body `= ...`, `Unit`.
- Classes : `open`, `abstract`, visibilité `public/private/protected/internal`, constructeur primaire, `init {}`, interfaces + `override`.

## Activité & cycle de vie
- `class MainActivity : ComponentActivity()` → `onCreate` → `enableEdgeToEdge()` → `setContent { AppTheme { ... } }`.
- Cycle : onCreate → onStart → onResume → onPause → onStop → onDestroy (rotation = recréation).
- Intents : explicite `Intent(context, Other::class.java)` ; implicite `Intent(Intent.ACTION_VIEW, Uri.parse(url))` (ex. ouvrir un lien). Avec Compose une seule activité suffit.
- Permissions : déclarer dans le manifeste ; si permission « dangereuse » → la **demander à l'exécution**.

## Compose — briques de base
- `@Composable fun X()` = une fonction = une brique.
- `Text(text, color, style = TextStyle(fontSize = 12.sp, fontWeight = ...))`
- `Image(painter = painterResource(R.drawable.x), contentDescription = ...)` / `Icon(..., tint = ...)`
- Image distante : `Image(painter = rememberAsyncImagePainter(model = url), ...)` (Coil)
- Boutons : `Button` (filled), `TextButton`, `IconButton { Icon(...) }`
- Champ : `OutlinedTextField(value, onValueChange, placeholder/label, singleLine)`
- Layouts : `Column` (vertical), `Row` (horizontal), `Box` (superposition) + `Arrangement.spacedBy(10.dp)`, `Alignment.CenterVertically`
- `Modifier` : `.padding()`, `.background()`, `.size()`, `.fillMaxWidth()`, `.fillMaxSize()`, `.clip(CircleShape)`, `.clickable {}` — **l'ordre compte**.
- `Checkbox(checked, onCheckedChange)`

## État
- On ne modifie pas l'UI, on modifie l'**état** → recomposition.
- `var x by remember { mutableStateOf(0) }` (survit aux recompositions)
- `rememberSaveable { ... }` (survit à la rotation / changement de thème)
- Listes : `remember { mutableStateListOf(...) }`

## Listes « Lazy »
- `LazyColumn { item { } ; items(list, key = { it.id }) { elem -> Cell(elem) } }`
- Seuls les éléments visibles sont construits → performance.
- **Keys** : liste statique → position OK ; liste **dynamique → fournir une key** (sinon l'état des items se décale).

## Navigation (Navigation 3)
```kotlin
@Serializable data object Home : NavKey
@Serializable data class CardDetail(val cardId: String) : NavKey

val backStack = rememberNavBackStack(Home)
NavDisplay(
  backStack = backStack,
  entryProvider = entryProvider {
    entry<Home> { HomeScreen(onCardClick = { id -> backStack.add(CardDetail(id)) }) }
    entry<CardDetail> { key -> CardDetailScreen(key.cardId, navigateBack = { backStack.removeLastOrNull() }) }
  }
)
```
Naviguer = `backStack.add(X)` ; retour = `backStack.removeLastOrNull()`.

## Thème
- `Color.kt` : palette `val Red500 = Color(0xFF...)`
- Police : `FontFamily(Font(R.font.x, FontWeight.Bold), ...)` dans `res/font`
- `Typography` : titleLarge, titleMedium, bodyLarge, bodyMedium…
- `lightColorScheme(...)` / `darkColorScheme(...)` (primary, onPrimary, background, surface…)
- `AppTheme(darkTheme = isSystemInDarkTheme()) { MaterialTheme(colorScheme, typography, content) }`
- Composants réutilisables qui lisent `MaterialTheme.colorScheme.primary` / `MaterialTheme.typography.bodyLarge` (ex. `PrimaryButton`).

## Stockage
- **DataStore** : `val Context.dataStore by preferencesDataStore(name = "settings")` → petites infos (thème, pseudo…).
- **Fichiers internes** : `openFileOutput("data.json", MODE_PRIVATE).use { it.write(json.toByteArray()) }`.
- **Room** : `@Entity data class X(@PrimaryKey val id: ..., ...)` + DAO + Database.

## Communication (Ktor)
```kotlin
// dépendances
implementation(libs.ktor.client.core)
implementation(libs.ktor.client.okhttp)
implementation(libs.ktor.client.content.negotiation)
implementation(libs.ktor.serialization.kotlinx.json)
implementation(libs.ktor.client.logging)

object HttpClientFactory {
  fun create(): HttpClient = HttpClient(OkHttp) {
    expectSuccess = true
    install(ContentNegotiation) { json(Json) }
    install(Logging)
    defaultRequest { contentType(ContentType.Application.Json) }
  }
}
val client = HttpClientFactory.create()
```
+ permission INTERNET. Penser à la **synchro** : que se passe-t-il si les données changent côté client / serveur / les deux, connecté ou hors-ligne ?

## Design / bonnes pratiques (évaluables à l'oral)
- Wireframe lo-fi (squelette) → hi-fi (maquette) ; outils : Figma, papier.
- Chargement < 3 s ; affordance ; lisibilité ; simplicité ; thumb zone (navigation en bas) ; gestes (pull-to-refresh plutôt qu'un bouton refresh).
- Gamification, biométrie, notifications = pistes « Et après ? ».
