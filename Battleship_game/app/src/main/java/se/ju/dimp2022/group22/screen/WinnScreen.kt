package se.ju.dimp2022.group22.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.ju.dimp2022.group22.R
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import se.ju.dimp2022.group22.NavigationScreen
import se.ju.dimp2022.group22.veiwModel.GameViewModel

@Composable
fun WinnScree(navController: NavController,gameViewModel: GameViewModel){
   LaunchedEffect(key1 = "", block = {
       delay(3000)
       gameViewModel.winnerText.value=""
      gameViewModel.reJoinLobby()
      navController.popBackStack(NavigationScreen.Lobby.route, inclusive = false, saveState = false)
   })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painter = painterResource(id = R.drawable.battleship__winbl),
                    contentScale = ContentScale.FillBounds,
                )
                ,horizontalAlignment = Alignment.CenterHorizontally
                ,verticalArrangement = Arrangement.Center
        ) {Box(
            modifier = Modifier
                //.fillMaxWidth(0.5f)
                //.fillMaxWidth()
                .padding(16.dp)
                .background(Color.Transparent.copy(alpha = 0.5f))
                .clip(MaterialTheme.shapes.medium)
            //.padding(16.dp)


        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f)
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ){

                Text(text = gameViewModel.winnerText.value , fontFamily = FontFamily.Monospace , fontSize = 32.sp, color = Color.Yellow )

            }
        }
    }
}