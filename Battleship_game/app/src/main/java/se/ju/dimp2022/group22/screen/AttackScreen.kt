@file:OptIn(ExperimentalMaterial3Api::class)

package se.ju.dimp2022.group22.screen




import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import se.ju.dimp2022.group22.NavigationScreen
import se.ju.dimp2022.group22.R
import se.ju.dimp2022.group22.veiwModel.Cell
import se.ju.dimp2022.group22.veiwModel.GameViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable


fun AttackScreen(navController: NavController, gameViewModel: GameViewModel) {
    var leaveInAttack by remember {
        mutableStateOf(false)
    }
     if (gameViewModel.changeTurn.value){
         gameViewModel.changeTurn.value=false
      navController.navigateUp()
    }
    if (gameViewModel.gameFinished.value) {
        navController.navigate(NavigationScreen.Winn.route)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.blurryintro),
                contentScale = ContentScale.FillBounds,

                )
    ) {
        if(leaveInAttack){
            Conformation({leaveInAttack=false}
                ,{gameViewModel.leaveGame()
                    leaveInAttack=false
                 }
                ,"Are you sure you want to leave the game??")
        }
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    colors = topAppBarColors(Color.Gray.copy(alpha = 0.6f)),
                    title = {
                        Text(text = "Attack" , fontStyle = FontStyle.Italic ,fontSize = 32.sp, color = Color.Black )
                    }
                    ,actions = {
                        Image(
                            modifier = Modifier.size(100.dp,250.dp),
                            painter = painterResource(id = R.drawable.ship), contentDescription =" " )
                        Text(text = " = ${gameViewModel.opponentShips.intValue}", fontSize = 25.sp, fontStyle = FontStyle.Italic)
                    }
                )
            },
            bottomBar = {
                BottomAppBar (containerColor =Color.Gray.copy(alpha = 0.7f)){
                    Row(
                        modifier = Modifier.fillMaxSize()
                            .padding(16.dp)
                        ,horizontalArrangement = Arrangement.SpaceBetween
                        , verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(gameViewModel.infoText.value)
                        Icon(imageVector = Icons.Default.ArrowBack,
                            contentDescription = "leave",
                            modifier = Modifier
                                .clickable(onClick = {
                                    leaveInAttack = true
                                })
                        )
                    }
                }
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding), verticalArrangement = Arrangement.Center
            ) {
                AttackGrid(gameViewModel)
            }
        }
    }
}
@Composable
fun AttackGrid(gameViewModel: GameViewModel){
    Box(modifier = Modifier.aspectRatio(1f)
    ){
        LazyVerticalGrid(
            GridCells.Fixed(10),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(gameViewModel.gameCells) { cell1 ->
                CellView(cell1, gameViewModel)
            }
        }
    }
}
@Composable
fun CellView(cell: Cell, gameViewModel: GameViewModel){
   var color2 = Color.Blue
    if(!cell.isActive.value){
        color2=Color.Yellow
    }
    if (cell.hadShip.value){
        color2= Color.Red
    }
    Column(
        modifier = Modifier
            .padding(1.dp)
            .aspectRatio(1f)
            .clickable(onClick = { gameViewModel.sendFire(cell) })
            .background(color2)
            .border(width = 1.dp, Color.Gray)
        , content = {}
    )
}

