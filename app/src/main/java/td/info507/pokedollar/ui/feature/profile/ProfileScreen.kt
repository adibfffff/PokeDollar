package td.info507.pokedollar.ui.feature.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import td.info507.pokedollar.R
import td.info507.pokedollar.ui.component.PrimaryButton

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val preferences by viewModel.preferences.collectAsStateWithLifecycle()

    var newPseudo by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current
    val savedMessage = stringResource(R.string.profile_saved)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.profile_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = if (preferences.pseudo.isEmpty()) {
                stringResource(R.string.profile_no_pseudo)
            } else {
                stringResource(R.string.profile_hello, preferences.pseudo)
            },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        OutlinedTextField(
            value = newPseudo,
            onValueChange = { newPseudo = it },
            label = { Text(stringResource(R.string.profile_pseudo_label)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        PrimaryButton(
            text = stringResource(R.string.profile_save),
            onClick = {
                viewModel.savePseudo(newPseudo, onSaved = {
                    newPseudo = ""
                    Toast.makeText(context, savedMessage, Toast.LENGTH_SHORT).show()
                })
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
