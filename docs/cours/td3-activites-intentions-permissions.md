
Sujet
Conditions d’achèvement
TD3 - Introduction aux activités, aux intentions et aux permissions
Objectif : Comprendre le concept des activités central au développement d'application mobile sous Android en manipulant les intentions et leurs permissions.

 

1. Le petit bouton
Dans le fichier MainActivity.kt, nous allons placer un bouton au centre. Nous lui donnerons "Prévisions détaillées" comme texte à l'intérieur, en respectant les bonnes pratiques vues lors de la séance précédente.

Pour ce petit exemple, n'hésitez pas à créer un écran composable et une prévisualisation temporaire.

Vous devriez avoir ce résultat :

Ecran avec un bouton

 

2. Un toast !
Dans le fichier MainActivity.kt, nous allons ajouter une action sur le bouton que nous venons de créer.

onClick = {}
Nous utiliserons un Toast qui permet d'afficher un message très court à l'utilisateur pendant un certain temps :

Toast.makeText(context, "Coucou !", Toast.LENGTH_SHORT).show()
Attention, il est nécessaire de bien préciser la méthode "show" sinon rien ne se passe...

Ecran avec un bouton et un toast

 

3. Préparons la vue
A présent, nous allons voir la gestion temporaire des photos, afin d'intégrer une photo de profil ou d'un paysage sympa, lorsque l'utilisateur clique sur l'image.

Nous allons donc ajouter à l'écran d'accueil, une icône d'utilisateur qui sera remplacée lorsque l'utilisateur aura pris sa photo (voir point suivant).

Ecran avec l'icône de l'utilisateur

Il n'est pas nécessaire de détailler la création de l'icône, vous connaissez déjà le principe ! Nous allons plutôt voir comment l'intégrer dans notre écran d'accueil.

Pour se faire, vous allez créer un composable au niveau de de cet écran, qui prendra une "ImageBitmap" en paramètre. Si cette image existe on l'affiche sinon on choisit l'icône créée précédemment.

@...
fun MyImage(...) {
    val modifier = ...
    val contentDescription = ...
    return if (... == null) {
        Image(...)
    } else {
        Image(...)
    }
}
 

4. Ça part d'une bonne intention
La différence avec une intention qui réalise directement une action, c'est qu'ici nous allons avoir besoin du résultat. C'est-à-dire, l'activité "MainActivity" va appeler l'activité de l'appareil photo du téléphone. Lorsque celle-ci aura été prise, l'utilisateur sera alors redirigé vers l'activité principale avec le contenu de la photo (null si l'utilisateur a décidé d'annuler).

Pour se faire, nous allons stocker cette photo dans la gestion de l'état de Compose avec :

var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
Ensuite, nous utiliserons la méthode suivante en précisant le contrat effectué issu de la classe "ActivityResultContracts" :

val cameraLauncher = rememberLauncherForActivityResult(
    contract = ...
) { bitmap -> imageBitmap = ... }
Puis lancer l'activité qui va permettre de prendre une photo et récupérer l'aperçu avec :

cameraLauncher.launch()
Enfin, il ne reste qu'à passer la photo à notre composable du point précédent.

 

5. Donner la permission
Afin de tester les permissions, nous allons faire en sorte que lorsque l'utilisateur ouvre l'application, celle-ci lui demande l'accès à une géolocalisation précise ou approximative.

A la racine du fichier AndroidManifest.xml, nous allons ajouter la permission pour accéder à la position de l'appareil avec :

<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
Vous allez avoir besoin des fichiers suivants (à placer au bon endroit) :

dependencies {
   ...
    implementation(libs.play.services.location)
   ...
}
Si vous ne les trouvez pas, ouvrez le fichier "libs.versions.toml" puis ajoutez ces lignes :

[versions]
...
playServiceLocation = "21.3.0"

[libraries]
...
play-services-location = { module = "com.google.android.gms:play-services-location", version.ref = "playServiceLocation" }
L'idée va être de créer un attribut à la classe "MainActivity", qui va contenir la récupération de la position.

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        ...

        fusedLocationClient = ...
    }

    private fun getLastKnownLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { loc ->
                ...
            }
            .addOnFailureListener { err ->
                err.message?.let { Log.d("Permission", it) }
            }
    }
}
Sans vérification, nous allons directement réaliser l'intention dans le code en pensant que l'utilisateur l'a permis.

Normalement, ce message apparaît dans la console lorsque vous testez :

... does not have any of [android.permission.ACCESS_FINE_LOCATION, android.permission.ACCESS_COARSE_LOCATION]
Cela signifie que la permission déclarée dans le manifeste ne suffit pas, il faut également la demander à l'utilisateur au moins une fois. Pour se faire, nous allons demander à l'utilisateur qu'il accorde sa permission pour récupérer sa position :

val requestPermissionLauncher = rememberLauncherForActivityResult(
    ...
) { isGranted -> ... }
Il ne reste plus qu'à vérifier que la permission a bien été donnée au moment de la récupération de la position :

if (ContextCompat.checkSelfPermission(context, ...) == PackageManager.PERMISSION_...) {
    ...
} else {
    ...
}
Si vous retestez en accordant la permission, voici le résultat obtenu :

Ecran de demande de position précise   Ecran de demande de position approximative

