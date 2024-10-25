package se.ju.dimp2022.group22.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.garrit.android.multiplayer.Player
import se.ju.dimp2022.group22.NavigationScreen
import se.ju.dimp2022.group22.R
import se.ju.dimp2022.group22.veiwModel.LobbyViewModel
import androidx.compose.ui.unit.sp as sp



@Composable
fun LobbyScreen(lobbyViewModel:LobbyViewModel = viewModel(), navController: NavController) {
    val users = lobbyViewModel.lobbyUsers
    val game = lobbyViewModel.findInvite()
    val nav = lobbyViewModel.serverStatus

    if(nav.collectAsState().value.toString()=="GAME"){
    navController.navigate(NavigationScreen.Ships.route+"/${lobbyViewModel.me.value?.name}/${lobbyViewModel.opponent}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.blurryintro),
                contentScale = ContentScale.FillBounds
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFA9A9A9).copy(alpha = 0.6f))
                .padding(16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Game Lobby", fontSize = 30.sp)
        }
        LazyColumn(
            modifier = Modifier
                .background(Color(0xFFD3D3D3).copy(alpha = 0.6f))
        ) {
            items(users) { player ->
                RowView(player, lobbyViewModel)
            }
        }
    }
    if (game!=null){
        Conformation(onCancel = { lobbyViewModel.declineInvite() }
            , onConfirm = { lobbyViewModel.acceptInvite()}
            , text = "${game.player1.name} invited you to a game")
    }
}

@Composable
fun RowView(player: Player, lobbyViewModel: LobbyViewModel){
    val sendInviteFunState: MutableState<Player?> = remember {
        mutableStateOf(null)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .size(50.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable(
                onClick = {
                    if (lobbyViewModel.me.value?.id != player.id) {
                        sendInviteFunState.value = player
                    }
                }
            )
        , horizontalArrangement = Arrangement.Start
        , verticalAlignment = Alignment.CenterVertically
    ){
        Text(text = player.name , fontSize = 20.sp )
    }
    if(sendInviteFunState.value!=null){
        Conformation(onCancel = { sendInviteFunState.value = null},
            onConfirm = {lobbyViewModel.sendGameInvite(player)
                        sendInviteFunState.value = null}
            ,text ="Do you want to challenge ${sendInviteFunState.value!!.name} " )
    }
}




