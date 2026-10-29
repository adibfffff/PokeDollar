# Fiches de révision Android (INFO507) — 13 fiches
Source : fiches de révision fournies par Adib (reprend le CM et le projet MyWeather des TD). Version condensée fidèle.

## Fil des TD MyWeather
1. Identité (icône, couleurs, typo, icônes météo) → fiche 08
2. Premier écran (accueil, textes, bouton + toast) → fiches 02-04
3. Deux écrans + navigation (détail au clic) → fiche 07
4. Téléphone (photo, géoloc + permission) → fiche 13
5. Écran détail (liste heure par heure) → fiche 06
6. Vraies données (Room, ViewModel) → fiches 09-10
7. Internet (API météo Ktor, sauvegarde position) → fiche 11

## 01 Bases de Compose
- On décrit l'écran avec des fonctions Kotlin @Composable (briques LEGO) au lieu de XML.
- Entrée : `MainActivity : ComponentActivity()` → `onCreate` → `enableEdgeToEdge()` → `setContent { MyWeatherTheme { NavHost() } }`.
- Règles : nom en majuscule, retourne Unit (émet l'UI), appelable seulement depuis un autre @Composable. @Preview pour voir sans émulateur.
- Pièges : appeler un composable dans onClick (impossible : on change un état) ; oublier @Composable.

## 02 Composants
Text (text, color, style), Image (painter, contentDescription), Icon (tint), Button, TextButton, IconButton, OutlinedTextField (value, onValueChange, label, placeholder). Foundation + Material 3.
- Textes dans res/values/strings.xml (1 fichier par langue) : `stringResource(R.string.common_temperature, valeur)` avec `%d`. Apostrophe échappée `\'`.
- Image locale `painterResource(R.drawable.x)` ; distante Coil `rememberAsyncImagePainter(model = url)`. contentDescription = null si décorative.
- Syntaxe : paramètres ( ) puis contenu { } (dernier paramètre lambda).

## 03 Column, Row, Box
- Column empile vers le bas, Row de gauche à droite, Box superpose (dernier enfant dessus).
- Arrangement = axe principal (espacement : spacedBy, Center, SpaceBetween) ; Alignment = axe secondaire.
- Column : verticalArrangement / horizontalAlignment. Row : horizontalArrangement / verticalAlignment. Box : contentAlignment.

## 04 Modifiers
Appliqués dans l'ordre (recette). size, width/height, fillMaxSize/fillMaxWidth(0.85f), padding (avant background = marge extérieure, après = intérieure), background, clip(CircleShape / RoundedCornerShape), clickable, weight(1f), statusBarsPadding().
Pièges : .background().clip() reste carré ; .padding().clickable{} → marge non cliquable.

## 05 État et recomposition
On modifie l'état, Compose recompose (réexécute la fonction).
- `var count = 0` → rien ne change ; `mutableStateOf(0)` seul → recréé à 0 ; `remember { mutableStateOf(0) }` → OK sauf rotation ; `rememberSaveable { ... }` → survit à la rotation.
- Champ : `var city by rememberSaveable { mutableStateOf("") }` + `value = city, onValueChange = { city = it }`.
- State hoisting : l'écran reçoit des lambdas (`onForecastClicked: (String) -> Unit`), il ne navigue pas lui-même.
- `by` nécessite imports getValue/setValue ; liste observable : mutableStateListOf.

## 06 Listes Lazy et keys
LazyColumn ne construit que le visible. `item { }`, `items(count = n) { index -> }`, `items(items = liste) { el -> }`. Aussi LazyRow, LazyVerticalGrid.
Keys : liste dynamique → `items(liste, key = { it.id })` (clé unique et stable) sinon l'état se décale. Pas de verticalScroll sur une LazyColumn.

## 07 Navigation 3
Back stack = tas de cartes.
```kotlin
interface Destination : NavKey {
    @Serializable data object Home : Destination
    @Serializable data class Detail(val date: String) : Destination
}
@Composable fun NavHost() {
    val backStack = rememberNavBackStack(Destination.Home)
    NavDisplay(backStack = backStack, entryProvider = entryProvider {
        entry<Destination.Home> { HomeScreen(onForecastClicked = { date -> backStack.add(Destination.Detail(date)) }) }
        entry<Destination.Detail> { key -> DetailScreen(date = key.date, navigateBack = { backStack.removeLastOrNull() }) }
    })
}
```
Pièges : oublier @Serializable ; donner le backStack aux écrans ; passer un gros objet (passer un id).

## 08 Thème
ui/theme : Color.kt (`val Blue400 = Color(0xFF3DA5FF)`), Type.kt (FontFamily + Typography titleLarge/bodyLarge...), Theme.kt (`lightColorScheme`/`darkColorScheme`, `MyWeatherTheme(darkTheme = isSystemInDarkTheme())` → MaterialTheme). Rôles : primary, background, surface, onPrimary (= texte posé sur primary). Jamais de couleur en dur. Composant réutilisable PrimaryButton (Button + RoundedCornerShape(16.dp) + ButtonDefaults.buttonColors(primary/onPrimary)).

## 09 ViewModel
Garde l'état de l'écran hors de l'UI, survit à la rotation, tâches longues dans viewModelScope, expose des StateFlow.
| | remember | rememberSaveable | ViewModel | Room |
|---|---|---|---|---|
| Recomposition | gardé | gardé | gardé | gardé |
| Rotation | perdu | gardé | gardé | gardé |
| Quitter l'écran | perdu | perdu | perdu | gardé |
| Fermer l'app | perdu | perdu | perdu | gardé |
```kotlin
class HomeViewModel : ViewModel() {
    val dailyForecast: StateFlow<DailyForecast?> =
        GetDailyForecastUseCase.invoke(LocalDate.now().toString())
            .stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5_000), initialValue = null)
    init { viewModelScope.launch { SyncDailyForecastUseCase.invoke(LocalDate.now().toString()) } }
}
@Composable fun HomeScreen(onForecastClicked: (String) -> Unit, viewModel: HomeViewModel = viewModel()) {
    val dailyForecast by viewModel.dailyForecast.collectAsStateWithLifecycle()
}
```
Flow (tunnel, pas de valeur actuelle, travail relancé par écouteur) vs StateFlow (quai, valeur actuelle toujours, initialValue obligatoire). Règle : Room et UseCases → Flow ; ViewModel → StateFlow ; écran lit le StateFlow. Piège : `HomeViewModel()` dans un composable → toujours `viewModel()`.

## 10 Room
@Entity (table, @PrimaryKey obligatoire), @Dao (requêtes, Room écrit le code), @Database (entités + DAO, une seule instance).
```kotlin
@Entity(tableName = "hourly_forecast")
data class HourlyForecastEntity(@PrimaryKey @ColumnInfo(name = "hour") val hour: String, ...)
@Dao interface HourlyForecastDao {
    @Upsert suspend fun upsert(forecasts: List<HourlyForecastEntity>)
    @Query("SELECT * FROM hourly_forecast") fun getForecast(): Flow<List<HourlyForecastEntity>>
}
class MyWeatherApplication : Application() {
    override fun onCreate() { super.onCreate(); WeatherDatabaseHolder.initialize(this) }
}
```
Base créée une fois au démarrage, Application déclarée dans le Manifest (`android:name=".MyWeatherApplication"`). Lecture continue → Flow sans suspend ; écriture → suspend. Entity modifiée → désinstaller l'app de l'émulateur.

## 11 Ktor et JSON
Requête GET : serveur + chemin + paramètres (?key=…&q=…&hours=24).
```kotlin
object HttpClientFactory {
    fun create(): HttpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        install(Logging) { level = LogLevel.ALL }
        install(HttpTimeout) { requestTimeoutMillis = 15000L }
    }
}
@Serializable data class RemoteForecastHour(val time: String, @SerialName("temp_c") val temperature: Double, ...)
object WeatherApi {
    private val client = HttpClientFactory.create()
    suspend fun fetchHourlyForecast(position: UserLocation): RemoteWeatherResponse =
        client.get("https://api.weatherapi.com/v1/forecast.json") { parameter("key", "TA_CLE"); parameter("q", "..."); parameter("hours", 24) }.body()
}
```
suspend (attente sans figer), runCatching dans le UseCase. Pièges : permission INTERNET, @Serializable, ignoreUnknownKeys, appeler l'API depuis l'écran.

## 12 Couches
Écran → ViewModel → UseCase → Repository → Room / API. Une couche = une responsabilité ; chacune ne parle qu'à celle du dessous. Room = source de vérité.
3 versions d'une donnée : RemoteForecastHour (colle au JSON), HourlyForecast (domaine, ne dépend de rien), HourlyForecastEntity (colle à Room), reliées par des mappers. Changer d'API → seul remote/ change. Pièges : écran qui appelle Repository/DAO ; Entity qui remonte jusqu'à l'écran.

## 13 Téléphone
Permission normale (INTERNET : Manifest seulement) vs dangereuse (ACCESS_FINE/COARSE_LOCATION, CAMERA : Manifest + demande à l'exécution). Schéma launcher + contrat : `registerForActivityResult` (Activity) / `rememberLauncherForActivityResult` (composable). Contrats : RequestMultiplePermissions() → Map<String, Boolean> ; TakePicturePreview() → Bitmap?. Position : `LocationServices.getFusedLocationProviderClient(this).lastLocation.addOnSuccessListener { loc -> lifecycleScope.launch { ... } }`. Prévoir le refus ; après deux refus plus de popup.

## Lexique
@Composable, recomposition, état, remember, rememberSaveable, Modifier, Arrangement, Alignment, LazyColumn, key, NavKey, back stack, MaterialTheme, ViewModel, Flow/StateFlow, suspend, Repository, UseCase, Entity/DAO, permission dangereuse, contrat, JSON, @SerialName, runCatching.
