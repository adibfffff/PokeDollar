# TD5 — Stockage des données en local
Objectif : connaître les différentes manières de stocker des données sur Android et manipuler ces données avec Room.

## 0. Retour sur le ViewModel
Mettre à jour le package `ui.feature.detail` avec :

`DetailViewModel` (lien entre la vue et le modèle) :
```kotlin
class DetailViewModel : ...() {
    val hourlyForecast: StateFlow<List<...>> = ...
        .stateIn(
            scope = ...,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ...
        )
}
```

`HourlyForecastItem` (composable d'un item de la liste) :
```kotlin
@...
fun ...(..., modifier: ...) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(5.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = ..., color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
        Text(text = ..., color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(end = 5.dp))
        Image(painter = painterResource(...), contentDescription = ..., modifier = Modifier.size(50.dp))
    }
}
```

`DetailScreen` :
```kotlin
@Composable
fun DetailScreen(navigateBack: () -> Unit, date: String = LocalDate.now().toString(), ...) {
    val hourlyForecasts by ...
    Column( ... ) {
        TopAppBar( ... )
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            item { Text(text = ..., color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.bodyLarge) }
            items(items = ...) { element -> ... }
        }
    }
}
```

`GetHourlyForecastUseCase` :
```kotlin
object GetHourlyForecastUseCase {
    fun invoke(): Flow<List<HourlyForecast>> {
        return flowOf(listOf(HourlyForecast("0h", 18, WeatherCondition.Moon), ...))
    }
}
```

## 1. « State » vs « Flow » vs « StateFlow »
- State : donne-moi la météo actuelle ;
- Flow : donne-moi chaque nouvelle météo quand elle arrive (succession de valeurs dans le temps, ex. lecture d'une base) ;
- StateFlow : donne-moi la dernière météo à afficher (flux transformé en état adapté à la vue).

## 2. La base de données
Dans un package `local`, fichier `WeatherDatabase.kt` :
```kotlin
@Database(entities = [...], version = ..., exportSchema = false)
abstract class WeatherDatabase : ...() {
    ...
    companion object {
        private const val DB_NAME = "..."
        @Volatile
        private var INSTANCE: WeatherDatabase? = null

        fun get(context: Context): WeatherDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    Room.databaseBuilder(context, WeatherDatabase::class.java, DB_NAME).build()
                        .also { INSTANCE = it }
                }
            }
            return INSTANCE!!
        }
    }
}

data object WeatherDatabaseHolder {
    var database: WeatherDatabase? = null
    fun initialize(context: Context) { database = ... }
}
```
Note : @Database simplifie la création d'une base avec les entités et le numéro de version.

Gradle :
```toml
# versions
room = "2.8.5"
ksp = "2.3.12"
# libraries
androidx-room-runtime = { module = "androidx.room:room-runtime", version.ref = "room" }
androidx-room-ktx = { module = "androidx.room:room-ktx", version.ref = "room" }
androidx-room-compiler = { module = "androidx.room:room-compiler", version.ref = "room" }
# plugins
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
```
```kotlin
alias(libs.plugins.ksp)
implementation(libs.play.services.location)   // (géoloc MyWeather, pas utile pour Pokedollar)
implementation(libs.androidx.room.runtime)
implementation(libs.androidx.room.ktx)
ksp(libs.androidx.room.compiler)
```

## 3. Les entités
Package `local.entity`, classe `HourlyForecastEntity` :
```kotlin
@Entity(tableName = "...")
data class HourlyForecastEntity(...) {
    fun toHourlyForecast(): HourlyForecast { return ... }
    companion object {
        fun fromHourlyForecast(forecast: HourlyForecast): HourlyForecastEntity { return ... }
    }
}
```
Note : @Entity simplifie la création des tables.

## 4. La couche DAO
`HourlyForecastDao` dans `local.dao` :
```kotlin
@Dao
interface HourlyForecastDao {
    @...
    suspend fun upsert(...)
    @...
    fun getForecast(): Flow<...>
}
```
Note : @Dao simplifie l'accès aux données des entités.

## 5. Le répertoire
Package `repository`, objet `WeatherRepository` :
```kotlin
object WeatherRepository {
    suspend fun setHourlyForecast() {
        WeatherDatabaseHolder.database?.hourlyForecastDao()?.upsert(listOf( ... ))
    }
    fun getHourlyForecast(): Flow<List<HourlyForecast>> {
        return WeatherDatabaseHolder.database?.hourlyForecastDao()?.getForecast()
            ?.map { it.map { ... } } ?: flowOf(...)
    }
}
```

## 6. Mise à jour du UseCase
```kotlin
object GetHourlyForecastUseCase {
    fun invoke(): Flow<List<HourlyForecast>> { return ... }   // lit maintenant la table
}
object SyncHourlyForecastUseCase {
    suspend fun invoke(): Result<Unit> {
        return runCatching { ... }   // synchronisation (service externe au TD6)
    }
}
```

## 7. Vérification
Si chaque package a sa responsabilité, les données affichées dans la liste correspondent à celles en base. Appliquer le même principe à l'écran d'accueil (prévision du jour).
