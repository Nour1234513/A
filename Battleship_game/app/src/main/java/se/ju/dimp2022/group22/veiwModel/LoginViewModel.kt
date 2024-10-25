package se.ju.dimp2022.group22.veiwModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.garrit.android.multiplayer.Player
import io.garrit.android.multiplayer.ServerState
import io.garrit.android.multiplayer.SupabaseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class LoginViewModel : ViewModel() {
     val serverLoginStatus: MutableStateFlow<ServerState>  = SupabaseService.serverState
    fun joinLobby(player: Player) {
        viewModelScope.launch {
            SupabaseService.joinLobby(player)
        }
    }
}