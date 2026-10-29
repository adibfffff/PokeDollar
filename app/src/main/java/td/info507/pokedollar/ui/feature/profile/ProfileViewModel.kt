package td.info507.pokedollar.ui.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import td.info507.pokedollar.domain.data.UserPreferences
import td.info507.pokedollar.domain.usecase.GetUserPreferencesUseCase
import td.info507.pokedollar.domain.usecase.SavePseudoUseCase

class ProfileViewModel : ViewModel() {

    // Le pseudo enregistré (se met à jour tout seul après un enregistrement)
    val preferences: StateFlow<UserPreferences> =
        GetUserPreferencesUseCase.invoke()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = UserPreferences(pseudo = "")
            )

    // Bouton Enregistrer : onSaved est appelée seulement si ça a marché
    fun savePseudo(pseudo: String, onSaved: () -> Unit) {
        if (pseudo.isBlank()) return
        viewModelScope.launch {
            SavePseudoUseCase.invoke(pseudo)
                .onSuccess { onSaved() }
        }
    }
}
