# TD6 — Récupération de données via des services externes et customisation
Objectif : être capable de récupérer des données à partir d'un service externe.

## 1. Le service
Mettre à jour les prévisions météo via l'API https://www.weatherapi.com/. Créer l'objet `HttpClientFactory` dans le package `remote` : fonction qui crée un client, qui permettra les requêtes GET (cf. cours).
```toml
[versions]
ktor = "3.0.3"
[libraries]
ktor-client-core = { group = "io.ktor", name = "ktor-client-core", version.ref = "ktor" }
ktor-client-okhttp = { group = "io.ktor", name = "ktor-client-okhttp", version.ref = "ktor" }
ktor-client-content-negotiation = { group = "io.ktor", name = "ktor-client-content-negotiation", version.ref = "ktor" }
ktor-serialization-kotlinx-json = { group = "io.ktor", name = "ktor-serialization-kotlinx-json", version.ref = "ktor" }
ktor-client-logging = { group = "io.ktor", name = "ktor-client-logging", version.ref = "ktor" }
```

## 2. Liaison avec l'API
Reproduire le schéma des données de l'API dans `RemoteWeatherResponse` (doc : https://www.weatherapi.com/docs/#apis-forecast). Aide fournie :
```kotlin
@Serializable
data class RemoteWeatherResponse(val forecast: RemoteForecastDays)

@Serializable
data class RemoteForecastDays(@SerialName("forecastday") val forecastDay: List<RemoteForecastDay>)

@Serializable
data class RemoteForecastDay(val date: String, val day: RemoteDay, val hour: List<RemoteForecastHour>) {
    fun toDailyForecast(): DailyForecast = DailyForecast(
        date = date,
        minTemperature = day.minTemperature.roundToInt(),
        maxTemperature = day.maxTemperature.roundToInt(),
        condition = day.condition.toWeatherCondition()
    )
}

@Serializable
data class RemoteDay(
    @SerialName("mintemp_c") val minTemperature: Double,
    @SerialName("maxtemp_c") val maxTemperature: Double,
    val condition: RemoteWeatherCondition
)

@Serializable
data class RemoteForecastHour(
    val time: String,   // "yyyy-MM-dd HH:mm"
    @SerialName("temp_c") val temperature: Double,
    @SerialName("is_day") val isDay: Int,
    val condition: RemoteWeatherCondition
) {
    fun toHourlyForecast(): HourlyForecast {
        val hour = time.substringAfter(" ").substringBefore(":").toInt()
        val condition = condition.toWeatherCondition()
        return HourlyForecast(
            hour = "${hour}h",
            temperature = temperature.roundToInt(),
            condition = if (isDay == 0 && condition == WeatherCondition.Sunny) WeatherCondition.Moon else condition
        )
    }
}

@Serializable
data class RemoteWeatherCondition(val code: Int) {
    fun toWeatherCondition(): WeatherCondition = when (code) {
        1000 -> WeatherCondition.Sunny
        1003 -> WeatherCondition.SunCloud
        1006, 1009 -> WeatherCondition.Cloud
        1030, 1135, 1147 -> WeatherCondition.Fog
        /* codes pluie */ -> WeatherCondition.Rain
        /* codes neige */ -> WeatherCondition.Snow
        1114, 1117, 1279, 1282 -> WeatherCondition.SnowStorm
        else -> WeatherCondition.Sunny
    }
}
```

## 3. La requête
```kotlin
object WeatherApi {
    private const val BASE_URL = "..."
    private const val API_KEY = "..."
    private val client = ...

    suspend fun fetchHourlyForecast(): ... = client....(urlString = "$BASE_URL/...") {
        parameter("key", API_KEY)
        parameter("q", "...")
        parameter("hours", 24)
    }.body()
}
```

## 4. Mise à jour des données
Mettre à jour le modèle à l'endroit prévu par l'architecture (fichier à identifier). Prévoir la permission INTERNET.

## 5. On boucle la boucle (DataStore)
Stocker latitude/longitude avec DataStore.
```kotlin
implementation(libs.androidx.datastore)
```
```toml
datastore = "1.0.0"
androidx-datastore = { group = "androidx.datastore", name = "datastore-preferences", version.ref = "datastore" }
```
`domain.data/UserPreferences` :
```kotlin
data class UserPreferences(val location: String?)
```
`local/DataStore` :
```kotlin
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

data object DataStoreHolder {
    var dataStore: DataStore<Preferences>? = null
    fun initialize(context: Context) { dataStore = context.dataStore }
}
```
(initialiser DataStoreHolder à la création de l'application)

`repository/UserRepository` :
```kotlin
object UserRepository {
    private const val DEFAULT_LOC = "chambery"
    private val locationKey = stringPreferencesKey("location")

    fun getLocation(): Flow<UserPreferences> =
        DataStoreHolder.dataStore!!.data.map { preferences -> UserPreferences(preferences[locationKey] ?: DEFAULT_LOC) }

    suspend fun saveLocation(location: String) {
        DataStoreHolder.dataStore!!.edit { preferences -> preferences[locationKey] = location }
    }
}
```
`domain.usecase` : `GetLocationUseCase` et `SyncLocationUseCase`. Au lancement : prévisions de la position actuelle si localisation activée, sinon du lieu par défaut.
