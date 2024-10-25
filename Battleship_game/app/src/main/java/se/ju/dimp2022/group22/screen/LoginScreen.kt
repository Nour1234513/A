package se.ju.dimp2022.group22.screen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf

import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.garrit.android.multiplayer.Player
import kotlinx.coroutines.delay

import se.ju.dimp2022.group22.NavigationScreen
import se.ju.dimp2022.group22.R
import se.ju.dimp2022.group22.veiwModel.LoginViewModel
import java.util.UUID





@Composable
fun LoginScreen(loginViewModel: LoginViewModel = viewModel(), navController: NavController) {
    val nav = loginViewModel.serverLoginStatus
    if(nav.collectAsState().value.toString()=="LOBBY"){
        navController.navigate(NavigationScreen.Lobby.route)
    }
    var alpha by rememberSaveable {
        mutableFloatStateOf(1f)
    }
    var name by rememberSaveable {
        mutableStateOf("")
    }
    var imageVisible by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(imageVisible) {
        delay(3000)
        alpha = 0.5f
        imageVisible = true
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.battleshipgame),
                contentScale = ContentScale.FillBounds,
                alpha = alpha
            )
                ,horizontalAlignment = Alignment.CenterHorizontally
                ,verticalArrangement = Arrangement.Center
        ){
        if (imageVisible) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                     verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = " Battle Ship " , fontSize = 20.sp , fontStyle = androidx.compose.ui.text.font.FontStyle.Italic  )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    OutlinedTextField(value = name,
                        onValueChange = {
                            name = it
                        },
                        label = {
                            Text("Enter your name" , color = Color.Black  , fontStyle = androidx.compose.ui.text.font.FontStyle.Italic  )
                        },
                        colors = TextFieldDefaults.colors(Color.Black)
                    )
                }
                Button(onClick = {
                    if(name.isNotBlank()) {
                        loginViewModel.joinLobby(Player(UUID.randomUUID().toString(), name))
                    }
                }) {
                    Text(text = " Join lobby")
                }
            }

            }
        }
    }

