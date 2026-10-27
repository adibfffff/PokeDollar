package td.info507.pokedollar.ui.feature.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import td.info507.pokedollar.R

// Écran de détail d'une carte (vide pour l'instant)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDetailScreen(cardId: String, navigateBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()){
        TopAppBar(
            title = { Text(cardId) },
            navigationIcon = {
                IconButton(onClick = navigateBack) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = stringResource(R.string.common_back)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,                        //  fond de l'écran en noir
                titleContentColor = MaterialTheme.colorScheme.primary,     // titre doré
                navigationIconContentColor = MaterialTheme.colorScheme.primary  // flèche dorée
            )
        )

        Text(text = "Détail de $cardId")
    }


}