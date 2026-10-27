import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

interface Destination: NavKey{
    @Serializable data object Home : Destination
    @Serializable data class CardDetail (val cardId: String) : Destination
    @Serializable data object Collection : Destination
    @Serializable data object Profile : Destination
    @Serializable data object Search : Destination

}