package se.ju.dimp2022.group22.screen



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import se.ju.dimp2022.group22.NavigationScreen
import se.ju.dimp2022.group22.R
import se.ju.dimp2022.group22.veiwModel.GameViewModel
import se.ju.dimp2022.group22.veiwModel.Ship
import se.ju.dimp2022.group22.veiwModel.ShipCell



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShipsScreen(
    navController: NavController,
    name: String,
    name1: String,
   gameViewModel:GameViewModel
    ) {
        var leaveShip by remember {
            mutableStateOf(false)
        }
    val screenWidthDp = gameViewModel.cellSize * 10
    if (gameViewModel.gameFinished.value) {
        navController.navigate(NavigationScreen.Winn.route)
    }
    if (gameViewModel.changeTurn.value) {
        gameViewModel.changeTurn.value = false
        navController.navigate(NavigationScreen.Attack.route)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.blurryintro),
                contentScale = ContentScale.FillBounds
            )
    ) {
        if(leaveShip){
            Conformation({leaveShip=false}
                ,{gameViewModel.leaveGame()
                    leaveShip=false
                 }
                ,"Are you sure you want to leave")
        }
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text(text = "$name VS $name1") },
                    colors = topAppBarColors(Color.Gray.copy(alpha = 0.6f)), actions = {
                        Image(
                            modifier = Modifier.size(100.dp, 250.dp),
                            painter = painterResource(id = R.drawable.ship),
                            contentDescription = " "
                        )
                        Text(
                            text = " = ${gameViewModel.ships.size}",
                            fontSize = 25.sp,
                            fontStyle = FontStyle.Italic
                        )
                    }

                )
            },
            bottomBar = {
                BottomAppBar(containerColor = Color.Gray.copy(alpha = 0.7f)) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                        ,horizontalArrangement = Arrangement.SpaceBetween
                        , verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (!gameViewModel.iamReady.value) {
                            Button(onClick = {
                                    gameViewModel.ready()
                            }) {
                                Text(text = "Ready")
                            }
                        } else if (!gameViewModel.opponentReady.value) {
                            Text(text = "Waiting for opponent to be ready")
                        } else {
                            if (!gameViewModel.gameStarted.value) {
                                gameViewModel.startTheGame()
                            }
                            Text(text = "Game Started")
                        }
                        Icon(imageVector = Icons.Default.ArrowBack,
                            contentDescription = "leave",
                            modifier = Modifier
                                .clickable(onClick = {
                                    leaveShip = true
                                })
                        )
                    }
                }
            }, content = { padding ->
                Box(modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                    contentAlignment = Alignment.Center, content = {

                        LazyVerticalGrid(columns = GridCells.Fixed(10),
                            horizontalArrangement = Arrangement.spacedBy(0.dp),
                            verticalArrangement = Arrangement.spacedBy(0.dp),
                            userScrollEnabled= false,
                            modifier = Modifier
                                .size(screenWidthDp.dp, screenWidthDp.dp),
                            content = {
                                items(gameViewModel.shipsGameCells) { shipCell ->
                                    ShipCellsBox(shipCell = shipCell, (screenWidthDp / 10))
                                }
                            }
                        )
                        for (ix in 0 until gameViewModel.ships.size) {
                            DragAbleShip(
                                gameViewModel.ships[ix],
                                gameViewModel,
                                screenWidthDp,
                            )
                        }
                    }
                )
            }
        )
    }
}
@Composable
fun ShipCellsBox(shipCell: ShipCell, size: Int){
    var color1 = 0xFF5A9BB1
    if(shipCell.hasShip()&&!shipCell.cellGotHit.value){
        color1=0xFF68F597
    }
    if(shipCell.hasShip()&&!shipCell.cellGotHit.value&&!shipCell.ship.value!!.hasBeenPlaced()){
        color1=0xFFFF0000
    }
    if(shipCell.cellGotHit.value){
        color1=0xFFFF672B
    }

    Column (
        modifier = Modifier
            .size(size.dp, size.dp)
            .aspectRatio(1f)
            .padding(1.dp)
            .border(1.dp, Color.Gray)
            .background(Color(color1))
        , horizontalAlignment = Alignment.CenterHorizontally
        , verticalArrangement = Arrangement.Center
        , content = {}
    )
}
@Composable
fun DragAbleShip(ship: Ship, gameViewModel: GameViewModel, screenWidth: Int){
    val density = LocalDensity.current.density
    val boxSize=screenWidth/10
    var image by rememberSaveable {
        mutableIntStateOf(R.drawable.ship)
    }
    var rotate by remember {
        mutableFloatStateOf(0F)
    }
    var width by remember {
        mutableFloatStateOf(0f)
    }
    var height by remember {
        mutableFloatStateOf(0f)
    }
    if(ship.vertical.value){
        height= (boxSize*ship.length-10).toFloat()
        width= (boxSize-10).toFloat()
        rotate=90f
        image=R.drawable.ship1
    }
    else if(!ship.vertical.value){
        height= (boxSize-10).toFloat()
        width= (boxSize*ship.length-10).toFloat()
        rotate=0f
        image=R.drawable.ship
    }
    Image(modifier = Modifier
        .size(width.dp, height.dp)
        .absoluteOffset(ship.positionInX.floatValue.dp, ship.positionInY.floatValue.dp)
        .pointerInput(Unit) {
            detectDragGestures(
                onDragEnd = {
                    gameViewModel.calculatePosition(boxSize, screenWidth / 2)
                }, onDrag = { change, dragAmount ->
                    if (!gameViewModel.iamReady.value) {
                        change.consume()
                        ship.positionInX.floatValue += dragAmount.x / density
                        ship.positionInY.floatValue += dragAmount.y / density
                    }
                })
        }
        .clickable(
            enabled = true, onClick = {
                if (!gameViewModel.iamReady.value) {
                    ship.vertical.value = !ship.vertical.value
                    gameViewModel.calculatePosition(boxSize, screenWidth / 2)
                }
            }
        )
        , contentScale = ContentScale.FillBounds
        ,painter = painterResource(id = image)
        ,contentDescription ="ship"
    )
}
