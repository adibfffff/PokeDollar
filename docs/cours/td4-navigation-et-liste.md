TD4 - Navigation et gestion d'une liste avec ses données
Objectif : Mettre en place la navigation au sein de l'application avec une seule activité et appréhender l'affichage d'une liste avec ses données.

 

0. Retour sur les permissions
Pour bien comprendre ce qui était attendu dans l'activité principale :

private fun checkAndRequestLocationPermission() {
    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        if (fineGranted || coarseGranted) {
            this.getLastKnownLocation()
        } else {
            Toast.makeText(this, "Permission de localisation refusée", Toast.LENGTH_SHORT).show()
        }
    }
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        this.getLastKnownLocation()
    } else {
        requestPermissionLauncher.launch(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        )
    }
}

private fun getLastKnownLocation() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    ) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { loc ->
                val text =
                    if (loc == null) "Impossible..." else "Lat: ${loc.latitude}, Lon: ${loc.longitude}"
                Toast.makeText(this, text, Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { err ->
                err.message?.let { Log.d("Permission", it) }
                Toast.makeText(this, err.message, Toast.LENGTH_LONG).show()
            }
    }
}
 

1. Quelques mots sur la navigation
Afin de préparer la suite des interactions au sein de l'application, nous allons créer deux écrans : "HomeScreen" et "DetailScreen", que nous placerons respectivement dans "ui.feature.home" et "ui.feature.detail". Le premier reprend exactement l'écran de l'activité principale et le deuxième sera laissé vide dans un premier temps (on y trouvera les prévisions de la journée en détail).

Nous allons maintenant intégrer la navigation au sein de l'application. Lorsque l'utilisateur cliquera sur le bouton, il sera envoyé vers le nouvel écran vide (cf. le cours).

Dans un nouveau package "ui.navigation", nous allons créer une interface "Destination" qui contiendra tous les écrans possibles de notre activité.

interface Destination : ... {
    @...
    data object Home : ...

    @...
    data class Detail(val date: String) : ...
}
Vous allez avoir besoin des fichiers suivants (à placer au bon endroit) :

plugins {
  ...
  alias(libs.plugins.kotlin.serialization)
}

dependencies {
  ...
  implementation(libs.androidx.navigation3.runtime)
  implementation(libs.androidx.navigation3.ui)
  ...
}
Si vous ne les trouvez pas, ouvrez le fichier "libs.versions.toml" puis ajoutez ces lignes :

[versions]
...
navigation3 = "1.0.0"

[libraries]
...
androidx-navigation3-runtime = { module = "androidx.navigation3:navigation3-runtime", version.ref = "navigation3" }
androidx-navigation3-ui = { module = "androidx.navigation3:navigation3-ui", version.ref = "navigation3" }

[plugins]
kotlin-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
Une fois, les différentes destinations préparées, il n'y a plus qu'à les utiliser. A vous de créer l'élément composable qui va gérer la navigation au sein de l'activité principale et de l'instancier correctement.

@...
fun NavHost() {
    val backStack = ...
    ...(
        backStack = ...,
        entryProvider = ... {
            entry<...> {
                ...()
            }
            entry<...> {
                ...()
            }
        }
    )
}
Dernier point, et c'est finalement l'objectif de cette partie, maintenant que vous avez tous les éléments en place, comment passer à l'écran de détail des prévisions au clic sur le bouton ?

 

2. Les détails !
Dans le fichier DetailScreen.kt, nous allons ajouter une barre de navigation en haut et une liste comme contenu de l'écran.

Voici un exemple de rendu :

Ecran avec un exemple de liste basique

Pour la barre de navigation, vous pouvez utiliser une "TopAppBar". Elle contiendra :

un titre (la date passée en paramètre),
une icône qui permettra de revenir en arrière,
des couleurs (si vous souhaitez surcharger le comportement par défaut).
TopAppBar(
    title = ...,
    navigationIcon = {
        IconButton(
            onClick = ...
        ) {
            Icon(...)
        }
    },
    colors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.Transparent
    )
)
Pour la liste, vous pouvez utiliser une "LazyColumn". Elle contiendra :

un premier item qui représenterait un titre de la liste,
les autres items qui se lient à une liste et dont il faut donner leur reprensation graphique pour chaque élément.
LazyColumn(...) {
    item {
        ...
    }
    items(
        items = ...,
    ) { element ->
        ...
    }
}
Voici les données surlesquelles vous pouvez vous appuyer dans un premier temps :

val hourlyForecasts = arrayListOf("Item1", "Item2", "Item3", "Item4", "Item5")
Comme les données sont de simples chaines de caractères, vous pouvez directement utiliser un "Text" comme élément de la vue.

 

3. Les données de la liste en détail
Dans cette partie, nous allons anticiper sur la séance suivante. Nous avons besoin de connaître le format des données que nous allons manipuler côté vue. Attention, il ne s'agit pas de sauvegarder des données, mais bien d'avoir un modèle surlequel se baser.

Nous allons avoir un nouveau package "domain" (au même niveau que "ui") qui en contiendra deux autres : "data" et "usecase".

Au sein de "data", nous trouverons le type énuméré "WeatherCondition" :

enum class WeatherCondition(val raw: String) {
    Sunny("sunny"),
    SunCloud("sun_cloud"),
    Cloud("cloud"),
    Fog("fog"),
    Rain("rain"),
    Snow("snow"),
    SnowStorm("snow_storm"),
    Moon("moon")
}
Nous trouverons également la classe de données "HourlyForecast" qui aura :

une heure (chaine de caractères),
une température (entier),
une condition météorologique (WeatherCondition).
Dans l'autre package, "usecase", on retrouvera l'objet "GetHourlyForcastUseCase" qui aura pour le moment des données statiques retournées par la fonction "invoke" :

object GetHourlyForecastUseCase {
    fun invoke(): Flow<List<HourlyForecast>> {
        return flowOf(listOf(
            HourlyForecast("0h", 18, WeatherCondition.Moon),
            HourlyForecast("1h", 17, WeatherCondition.Moon),
            HourlyForecast("2h", 17, WeatherCondition.Moon),
            HourlyForecast("3h", 17, WeatherCondition.Moon),
            HourlyForecast("4h", 16, WeatherCondition.Moon),
            HourlyForecast("5h", 16, WeatherCondition.Moon),
            HourlyForecast("6h", 15, WeatherCondition.Moon),
            HourlyForecast("7h", 16, WeatherCondition.Cloud),
            HourlyForecast("8h", 17, WeatherCondition.Cloud),
            HourlyForecast("8h", 17, WeatherCondition.Cloud),
            HourlyForecast("9h", 18, WeatherCondition.Cloud),
            HourlyForecast("10h", 18, WeatherCondition.SunCloud),
            HourlyForecast("11h", 19, WeatherCondition.SunCloud),
            HourlyForecast("12h", 20, WeatherCondition.Sunny),
            HourlyForecast("13h", 20, WeatherCondition.Sunny),
            HourlyForecast("14h", 21, WeatherCondition.Sunny),
            HourlyForecast("15h", 21, WeatherCondition.Sunny),
            HourlyForecast("16h", 20, WeatherCondition.SunCloud),
            HourlyForecast("17h", 20, WeatherCondition.Cloud),
            HourlyForecast("18h", 20, WeatherCondition.Rain),
            HourlyForecast("19h", 20, WeatherCondition.Rain),
            HourlyForecast("20h", 19, WeatherCondition.Rain),
            HourlyForecast("21h", 19, WeatherCondition.Cloud),
            HourlyForecast("22h", 18, WeatherCondition.Cloud),
            HourlyForecast("23h", 18, WeatherCondition.Cloud),
        ))
    }
}
Maintenant que notre modèle de données est défini, nous pouvons revenir à notre liste.

Cependant, il est essentiel de bien comprendre que nous avons besoin d'un pont qui va faire le lien entre nos données et notre vue.

Ce pont s'appelle le ViewModel (comme son nom l'indique...).

Dans "ui.feature.detail", nous allons créer le notre "DetailViewModel" :

class DetailViewModel : ...() {
    val hourlyForecast: StateFlow<List<...>> = ...
        .stateIn(
            scope = ...,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ...
        )
}
Dans l'écran de détail des prévisions, vous pouvez ajouter en argument le bon "ViewModel", par défaut nous utiliserons celui de base, mais on laisse la possibilité d'en prendre un autre. A vous de modifier votre interface pour prendre en compte les nouvelles données avec :

val hourlyForecasts by viewModel.hourlyForecast.collectAsStateWithLifecycle()
Si vous n'arrivez pas à récupérer "viewModel()", ajoutez à votre projet :

dependencies {
  ...
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  ...
}
Avec :

[libraries]
...
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycleRuntimeKtx" }
 

4. Le détail des éléments de la vue qui appartiennent à la liste
Nous allons créer un composable qui servira de template pour chaque élément de la liste. Nous l'appelerons "HourlyForecastItem" et il aura comme paramètre : un élément de la liste et un modifier.

Voici un exemple que vous pouvez réutiliser dans votre projet :

@...
fun ...(
    ...,
    modifier: ...
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(5.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = ...,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = ...,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(end = 5.dp)
        )

        Image(
            painter = painterResource(...),
            contentDescription = ...,
            modifier = Modifier.size(50.dp)
        )

    }
}
Vous êtes aussi libre de créer votre propre template...

Si jamais vous avez un doute, voici le résultat final :

Ecran avec la liste statique finale

