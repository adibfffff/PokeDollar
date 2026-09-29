#!/usr/bin/env bash
# PokeDollar - création des issues GitHub dans l'ordre de réalisation.
# Prérequis : gh installé + `gh auth login` fait. Lancer depuis le dossier du dépôt :
#   bash create_issues.sh
# Les issues sont créées dans l'ordre : leurs numéros suivent l'ordre de travail.
set -euo pipefail

REPO="adibfffff/PokeDollar"

existing=$(gh issue list -R "$REPO" --state all --limit 1 --json number --jq 'length')
if [ "$existing" != "0" ]; then
  echo "Le dépôt contient déjà des issues. Relance seulement si c'est voulu (Ctrl+C pour annuler)."; sleep 5
fi

# ---------- Labels ----------
gh label create setup -R "$REPO" --color 5319e7 --description "Mise en place du projet" --force
gh label create api   -R "$REPO" --color 0e8a16 --description "Réseau / données distantes" --force
gh label create data  -R "$REPO" --color 1d76db --description "Base locale / stockage" --force
gh label create ui    -R "$REPO" --color fbca04 --description "Interface Compose" --force
gh label create bonus -R "$REPO" --color c5def5 --description "Si le temps le permet" --force

# ---------- Jalons (phases) ----------
for m in "1 - Fondations" "2 - Recherche" "3 - Détail et collection" "4 - Accueil" "5 - Profil et paramètres" "6 - Bonus wishlist"; do
  gh api "repos/$REPO/milestones" -f title="$m" >/dev/null 2>&1 || true
done

# issue "titre" "label" "jalon" "description" "critères" "notion à maîtriser"
issue() {
  gh issue create -R "$REPO" --title "$1" --label "$2" --milestone "$3" --body "$(printf '## Description\n%s\n\n## Critères de validation\n%s\n\n## Notion à maîtriser (soutenance)\n%s\n' "$4" "$5" "$6")"
}

M1="1 - Fondations"; M2="2 - Recherche"; M3="3 - Détail et collection"
M4="4 - Accueil"; M5="5 - Profil et paramètres"; M6="6 - Bonus wishlist"

# ================= 1 - FONDATIONS =================
issue "[01] Nettoyer le template et écrire le README" setup "$M1" \
"Retirer le bouton « test » de MainActivity et remplacer le README (« test ») par une vraie présentation du projet." \
"- [ ] MainActivity ne contient plus le bouton test
- [ ] README : nom, but de l'app, liste des écrans, technos prévues" \
"Structure d'une app Compose : Activity, setContent, composable racine."

issue "[02] Définir la palette de couleurs et l'appliquer au thème" ui "$M1" \
"Remplacer les violets par défaut de Color.kt par une palette propre à l'app, et la brancher dans Theme.kt (clair et sombre)." \
"- [ ] Nouvelles couleurs dans Color.kt
- [ ] lightColorScheme et darkColorScheme mis à jour
- [ ] Aperçu (Preview) cohérent" \
"MaterialTheme, ColorScheme Material 3, rôles de couleurs (primary, surface, onPrimary…)."

issue "[03] Ajouter Navigation Compose et créer les 4 écrans vides" setup "$M1" \
"Ajouter la dépendance navigation-compose, créer HomeScreen, SearchScreen, CollectionScreen, ProfileScreen (un simple Text) et un NavHost." \
"- [ ] Dépendance ajoutée dans libs.versions.toml et build.gradle.kts
- [ ] 4 routes déclarées
- [ ] On peut afficher chaque écran via navController" \
"NavController, NavHost, routes, back stack. Catalogue de versions (libs.versions.toml)."

issue "[04] Ajouter la barre de navigation du bas" ui "$M1" \
"Ajouter une NavigationBar Material 3 dans un Scaffold avec 4 onglets et leurs icônes. Dépend de [03]." \
"- [ ] 4 onglets cliquables
- [ ] L'onglet actif est mis en évidence
- [ ] Pas d'empilement d'écrans à chaque clic (launchSingleTop / restoreState)" \
"Scaffold, NavigationBar, currentBackStackEntryAsState, gestion de l'état sélectionné."

issue "[05] Créer les modèles de données Card et Prices" api "$M1" \
"Créer les data classes Kotlin qui représentent une carte (id, nom, images, set, type, rareté) et ses prix, à partir de la doc de l'API choisie (ex. pokemontcg.io v2)." \
"- [ ] data class Card, CardImages, CardSet, Prices
- [ ] Champs optionnels marqués nullables
- [ ] Un exemple de JSON de la doc est utilisé pour vérifier les noms" \
"data class, nullabilité Kotlin, correspondance JSON ↔ objets."

issue "[06] Ajouter Retrofit et la permission INTERNET" setup "$M1" \
"Ajouter Retrofit + convertisseur (Gson ou Moshi) + coroutines, déclarer android.permission.INTERNET dans le manifest et créer l'interface de service." \
"- [ ] Dépendances ajoutées
- [ ] Permission INTERNET dans AndroidManifest
- [ ] Interface PokemonApi avec une fonction suspend" \
"Retrofit, interface de service, annotations @GET/@Query, sérialisation JSON, fonctions suspend et coroutines. Dépend de [05]." 

issue "[07] Récupérer une liste de cartes depuis l'API" api "$M1" \
"Créer un CardRepository et un ViewModel qui appellent l'API et exposent la liste de cartes dans un StateFlow. Affichage provisoire en texte." \
"- [ ] Appel réseau réussi
- [ ] Liste exposée via StateFlow
- [ ] Erreur réseau capturée (try/catch)" \
"ViewModel, viewModelScope, StateFlow, collectAsState, architecture Repository."

issue "[08] Ajouter Coil pour afficher les images" setup "$M1" \
"Ajouter Coil (coil-compose) et afficher l'image d'une carte avec AsyncImage." \
"- [ ] Dépendance ajoutée
- [ ] Une image de carte s'affiche depuis son URL
- [ ] Placeholder pendant le chargement" \
"Chargement d'images asynchrone, cache, AsyncImage, ContentScale."

issue "[09] Ajouter Room et l'entité OwnedCard" data "$M1" \
"Ajouter Room + KSP, créer l'entité OwnedCard (cardId, nom, imageUrl, prix, quantité), le DAO et la base de données." \
"- [ ] Plugin KSP + dépendances Room
- [ ] Entity, Dao (insert, delete, getAll en Flow), Database
- [ ] Insertion/lecture testée" \
"Room : @Entity, @Dao, @Database, Flow, KSP. Pourquoi stocker localement la collection."

issue "[10] Créer le composant réutilisable CardItem" ui "$M1" \
"Composable CardItem (image, nom, prix) réutilisé dans la recherche, la collection et l'accueil. Dépend de [08]." \
"- [ ] Paramètres : Card (ou données minimales) + onClick
- [ ] Preview disponible
- [ ] Prix absent géré proprement" \
"Composables réutilisables, paramètres, state hoisting, Modifier, Preview."

# ================= 2 - RECHERCHE =================
issue "[11] Écran Recherche : afficher une liste de cartes" ui "$M2" \
"Afficher les cartes de l'API dans une LazyVerticalGrid avec CardItem. Dépend de [07] et [10]." \
"- [ ] Grille scrollable
- [ ] Chargement paginé ou limité (ex. 20 cartes)
- [ ] Données venant du ViewModel" \
"LazyColumn/LazyVerticalGrid, pagination, recomposition."

issue "[12] Ajouter la barre de recherche par nom" ui "$M2" \
"Champ de recherche qui filtre les cartes par nom via le paramètre de requête de l'API." \
"- [ ] TextField en haut de l'écran
- [ ] La recherche se lance avec un léger délai (debounce)
- [ ] Le texte est conservé dans le ViewModel" \
"State dans le ViewModel, debounce avec Flow, requête paramétrée."

issue "[13] Ajouter le filtre par extension (set)" ui "$M2" \
"Menu déroulant listant les sets (récupérés via l'API) et filtrant les résultats." \
"- [ ] Liste des sets chargée
- [ ] Sélection appliquée à la requête
- [ ] Option « Tous »" \
"ExposedDropdownMenu, combiner plusieurs états de filtre dans un seul UiState."

issue "[14] Ajouter le filtre par type" ui "$M2" \
"Filtre par type de Pokémon (Feu, Eau, Plante…)." \
"- [ ] Sélection d'un type
- [ ] Combinable avec les autres filtres" \
"Construction d'une requête à filtres multiples (syntaxe q= de l'API)."

issue "[15] Ajouter le filtre par rareté" ui "$M2" \
"Filtre par rareté (Common, Rare Holo…)." \
"- [ ] Sélection d'une rareté
- [ ] Combinable avec les autres filtres" \
"Chips de filtre (FilterChip), état de filtres immuable."

issue "[16] Ajouter le filtre par fourchette de prix" ui "$M2" \
"Slider (RangeSlider) prix min / max appliqué aux résultats. Le prix n'est pas toujours filtrable côté API : filtrer côté app si besoin." \
"- [ ] RangeSlider fonctionnel
- [ ] Résultats filtrés selon la fourchette
- [ ] Cartes sans prix gérées" \
"RangeSlider, filtrage côté client vs côté serveur, avantages et limites."

issue "[17] Gérer les états chargement / erreur / aucun résultat" ui "$M2" \
"Introduire un UiState (Loading, Success, Error, Empty) pour l'écran Recherche." \
"- [ ] Indicateur de chargement
- [ ] Message d'erreur + bouton Réessayer
- [ ] Message « aucun résultat »" \
"sealed interface, UiState, gestion d'erreurs réseau, `when` exhaustif."

# ================= 3 - DÉTAIL ET COLLECTION =================
issue "[18] Écran de détail d'une carte" ui "$M3" \
"Écran affichant l'image en grand, nom, set, rareté et prix. Navigation depuis un CardItem avec l'id de la carte en argument." \
"- [ ] Route avec argument (cardId)
- [ ] Détails chargés depuis l'API
- [ ] Bouton retour" \
"Arguments de navigation, chargement d'une ressource par id, SavedStateHandle."

issue "[19] Bouton « Ajouter à ma collection »" data "$M3" \
"Depuis le détail, insérer la carte dans Room (ou incrémenter sa quantité si elle existe déjà). Dépend de [09]." \
"- [ ] Carte enregistrée en base
- [ ] Doublon = quantité + 1
- [ ] Retour visuel (Snackbar)" \
"Insertion Room, stratégie de conflit (@Insert onConflict / upsert), Snackbar."

issue "[20] Écran Collection : afficher les cartes possédées" ui "$M3" \
"Lister les OwnedCard de Room dans une grille, avec la quantité affichée." \
"- [ ] Liste mise à jour automatiquement
- [ ] Message si la collection est vide" \
"Flow Room → StateFlow → UI, mise à jour réactive."

issue "[21] Gérer la quantité d'une carte (+ / −)" ui "$M3" \
"Boutons + et − sur chaque carte de la collection. À 0, la carte est retirée." \
"- [ ] Quantité modifiée en base
- [ ] Ne descend pas sous 0" \
"@Update / requêtes @Query en Room, événements UI vers le ViewModel."

issue "[22] Supprimer une carte de la collection" data "$M3" \
"Action de suppression avec confirmation (AlertDialog)." \
"- [ ] Dialogue de confirmation
- [ ] Carte supprimée de Room" \
"@Delete, AlertDialog, état de dialogue."

# ================= 4 - ACCUEIL =================
issue "[23] Calculer la valeur totale de la collection" data "$M4" \
"Requête ou fonction calculant la somme prix × quantité de toutes les cartes possédées." \
"- [ ] Résultat correct sur un jeu de test
- [ ] Mise à jour automatique quand la collection change
- [ ] Test unitaire du calcul" \
"Requête SQL SUM en Room ou map sur Flow, test unitaire JUnit."

issue "[24] Afficher la valeur totale sur l'accueil" ui "$M4" \
"Grande carte en haut de l'accueil avec le montant total formaté (devise)." \
"- [ ] Montant formaté (2 décimales, symbole)
- [ ] Valeur 0 si collection vide" \
"Formatage de nombres/devises (NumberFormat, Locale), Card Material 3."

issue "[25] Afficher les 3 cartes les plus chères sur l'accueil" ui "$M4" \
"Afficher les 3 OwnedCard au prix le plus élevé (requête triée + limit 3) avec CardItem." \
"- [ ] 3 cartes max, triées par prix décroissant
- [ ] Cas < 3 cartes géré" \
"ORDER BY … DESC LIMIT 3, réutilisation de composants, LazyRow."

# ================= 5 - PROFIL ET PARAMÈTRES =================
issue "[26] Écran Profil : pseudo et statistiques" ui "$M5" \
"Afficher un pseudo modifiable, le nombre de cartes et la valeur totale." \
"- [ ] Pseudo modifiable et conservé
- [ ] Stats issues de la base" \
"Saisie utilisateur, réutilisation des données du ViewModel."

issue "[27] Écran Paramètres : choix de la devise" ui "$M5" \
"Ajouter un écran Paramètres (accessible depuis le profil) avec le choix € / $. L'API fournit des prix Cardmarket (€) et TCGplayer ($)." \
"- [ ] Choix de la devise
- [ ] Les prix affichés changent selon le choix" \
"Sélection unique (RadioButton), choix de la source de prix."

issue "[28] Paramètres : thème clair / sombre" ui "$M5" \
"Interrupteur pour forcer le thème clair, sombre ou système." \
"- [ ] Switch fonctionnel
- [ ] Thème appliqué à toute l'app" \
"isSystemInDarkTheme, passage d'un paramètre à PokeDollarTheme."

issue "[29] Sauvegarder les paramètres avec DataStore" data "$M5" \
"Persister devise, thème et pseudo avec Preferences DataStore. Dépend de [26], [27], [28]." \
"- [ ] Valeurs conservées après fermeture de l'app
- [ ] Lecture via Flow" \
"DataStore vs SharedPreferences, Flow de préférences, écriture asynchrone."

# ================= 6 - BONUS =================
issue "[30] Wishlist : entité et DAO Room" data "$M6" \
"Créer WishlistCard (entité + DAO) dans la base Room existante (migration de version)." \
"- [ ] Entité + DAO
- [ ] Version de la base incrémentée avec migration" \
"Migrations Room, incrément de version, relations entre tables."

issue "[31] Wishlist : bouton « Ajouter aux souhaits » depuis le détail" ui "$M6" \
"Bouton cœur sur l'écran de détail pour ajouter/retirer une carte de la wishlist. Dépend de [30]." \
"- [ ] Icône pleine/vide selon l'état
- [ ] Ajout/retrait en base" \
"État dérivé d'un Flow, toggle."

issue "[32] Wishlist : écran des cartes souhaitées" ui "$M6" \
"Écran listant les cartes souhaitées avec leur prix actuel, accessible depuis le profil." \
"- [ ] Liste réactive
- [ ] Message si vide" \
"Réutilisation de CardItem, ajout d'une destination de navigation."

echo "32 issues créées."
