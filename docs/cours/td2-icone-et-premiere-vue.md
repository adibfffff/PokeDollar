
Sujet
Conditions d’achèvement
TD2 - Prise en main d'Android Studio avec la création de l'icône et d'une vue
Objectif : Découvrir l'IDE Android Studio et comprendre la gestion des ressources en créant l'icône de lancement et les premières vues de l'application.

 

1. Les couleurs
A partir de ce qui a été généré, nous allons définir nos trois couleurs qui serviront plus tard dans le fichier colors.xml :

blue_100 => #FFF3FAFF
blue_400 => #FF3DA5FF
blue_600 => #FF182238
Vous êtes libre d'utiliser d'autres valeurs pour ces couleurs, elles serviront uniquement pour la partie suivante.

 

2. L'icône de l'application
Nous allons créer une "couleur" pour le dégradé du fond de l'icône de l'application.

Dans le fichier ic_launcher_gradient.xml :

<?xml version="1.0" encoding="utf-8"?>
<gradient xmlns:android="http://schemas.android.com/apk/res/android"
    android:startColor="@color/..."
    android:centerColor="..."
    android:endColor="..."
    android:angle="90"
    android:startX="30"
    android:endX="70"
    android:startY="30"
    android:endY="70"
    android:type="linear"
    />
A vous de choisir les couleurs utilisées pour le fond (de préférence déjà définies plus tôt...).

Dans le fichier ic_launcher_background, mettre à jour la couleur du fond.

<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="108dp"
    android:height="108dp"
    android:viewportHeight="108"
    android:viewportWidth="108">
    <path
        android:fillColor="..."
        android:pathData="M0,0h108v108h-108z" />
    ...
Nous sommes maintenant prêts pour remplacer l'icône de l'application : New > Image Asset.

Le résultat final peut ressembler à cela => td2_launcher.png

 

3. Les couleurs (le retour)
A présent, nous allons définir toutes les couleurs nécessaires à l'application. Cela se passe dans le fichier ui.theme.Color.kt.

Voici les couleurs qui ont été choisie mais vous êtes libres d'en choisir d'autres :

White => 0xFFFFFFFF
Black => 0xFF000000
Gray400 => 0xFFD9E2EA
Gray500 => 0xFF8B95A8
Gray700 => 0xFF6B6D70
Blue100 => 0xFFF3FAFF
Blue300 => 0xFF008FF9
Blue400 => 0xFF3DA5FF
Blue600 => 0xFF182238
Blue800 => 0xFF0D1526
 

4. La typographie [facultatif]
Vous pouvez utiliser celle de base ou vous pouvez la personnaliser. Voici celle que nous utiliserons : Nunito.

Pour cela, rendez-vous au niveau du fichier ui.theme.Type.kt et ajoutez ces quelques lignes :

private val NunitoFontFamily: FontFamily = FontFamily(
    Font(resId = ..., weight = FontWeight.Black),
    Font(resId = ..., weight = FontWeight.Bold),
    Font(resId = ..., weight = FontWeight.Medium),
)
Vous pouvez personnaliser l'écriture de certains styles comme par exemple :

titleLarge = TextStyle(
    fontFamily = NunitoFontFamily,
    fontSize = 70.sp,
    fontStyle = FontStyle.Normal,
    fontWeight = FontWeight.Black
), ...
 

5. Le thème
Dans le fichier ui.theme.Theme.kt, nous allons tout coordonner.

private val DarkColorScheme = darkColorScheme(
    background = Blue800,
    surface = Blue600,
    primary = Blue300,
    primaryContainer = Blue600,
    secondary = White,
    onSecondary = White,
    tertiary = Gray500
)

private val LightColorScheme = lightColorScheme(
    background = Gray400,
    surface = White,
    primary = Blue400,
    primaryContainer = Blue100,
    secondary = Black,
    onSecondary = White,
    tertiary = Gray700
)

@Composable
fun MyWeatherTheme(
    ...
) {
    val colorScheme = if (darkTheme) {
        ...
    } else {
        ...
    }

    MaterialTheme(
        colorScheme = ...,
        typography = ...,
        content = ...
    )
}
Encore une fois, vous pouvez utiliser un autre thème que celui proposé...

 

6. Les autres icônes
Vous pouvez récupérer ces icônes pour le contenu de l'application.

ic_sunny.png
ic_sun_cloud.png
ic_cloud.png
ic_fog.png
ic_rain.png
ic_snow.png
ic_snow_storm.png
ic_moon.png
A vous de les mettres au bon endroit. Indice : vous connaissez ce répertoire.

 

7. A première vue
Avant d'aller plus loin, nous allons créer une fonction dans l'activité principale : HomeScreen.

Pour atteindre le résultat final montré plus bas, nous allons uniquement (ou presque) manipuler des "Column" et des "Row". Ce sont des composants de la vue qui sont composés d'autres éléments de la vue. Nous aurons également besoin d'autres éléments de la vue plus simples comme : "Text", "Image", "Button"...

Faites un squelette de cette maquette qui permettra de faire ressortir toutes les "Column" et les "Row".
N'hésitez pas à vous servir du rendu afin d'éviter de lancer l'application sur chaque modification.
Gardez en tête que votre interface doit être responsive, il est donc important que vos éléments soient placés de manière relative (et non absolu).
Chaque élément de la vue possède un "Modifier", c'est lui qui vous permettra de jouer sur le placement, la taille, la forme, la marge, la couleur, etc. N'hésitez pas à vous servir de l'auto-complétion pour trouver votre bonheur !

Voici le résultat attendu de cette partie :

Maquette de la vue de l'accueil (light)   Maquette de la vue de l'accueil (dark)

 

8. Les textes statiques
"Aujourd'hui" ou "X °C" sont des textes qui seront toujours présents. Ils ne dépendent pas des données affichées. Il faut donc les gérer différemment.

Dans le fichier "strings.xml", nous allons ajouter une entrée avec les clés : "home_today" et "degree_value".

 

9. Une vue plus poussée [facultatif]
Reprenons la méthodologie proposée lors de la partie précédente pour essayer d'arriver à ce résultat.

Indication : Les poids peuvent être intéressants pour laisser la place à l'élément en fonction de ses éléments frères de manière relative. On utilise alors l'attribut ".weight(0.5f)".

Maquette de la vue de l'accueil (light)   Maquette de la vue de l'accueil (dark)

