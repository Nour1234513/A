package se.ju.dimp2022.group22.veiwModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.garrit.android.multiplayer.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch



class LobbyViewModel:ViewModel() {
    val serverStatus: MutableStateFlow<ServerState> = SupabaseService.serverState
    val lobbyUsers = SupabaseService.users
    val me = mutableStateOf(SupabaseService.player)
    var opponent = ""
    private val games = SupabaseService.games

    fun findInvite(): Game? {
        val game: Game? = games.find { it.player2.id == SupabaseService.player!!.id }
        if(game!=null){
            opponent = if(game.player2.id==me.value?.id){
                game.player1.name
            } else{
                game.player2.name
            }
            return game
        }
        return null
    }

    fun acceptInvite() {
        viewModelScope.launch {
            if (findInvite() != null)
                SupabaseService.acceptInvite(findInvite()!!)
        }
    }

    fun declineInvite() {
        viewModelScope.launch {
            if (findInvite() != null)
                SupabaseService.declineInvite(findInvite()!!)
        }
    }

    fun sendGameInvite(player: Player) {
        opponent=player.name
        viewModelScope.launch {
            SupabaseService.invite(player)
        }
    }
}