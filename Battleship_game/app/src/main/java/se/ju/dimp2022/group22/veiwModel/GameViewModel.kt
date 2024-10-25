package se.ju.dimp2022.group22.veiwModel



import android.content.res.Resources
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.garrit.android.multiplayer.ActionResult
import io.garrit.android.multiplayer.GameResult
import io.garrit.android.multiplayer.SupabaseCallback
import io.garrit.android.multiplayer.SupabaseService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue


class Cell(x:Int,y:Int){
    var xC = x
    var yC = y
    var isActive :  MutableState<Boolean> = mutableStateOf(true)
    var hadShip : MutableState<Boolean> = mutableStateOf(false)

}
class ShipCell(x:Int,y:Int){
    var xC = x
    var yC = y
    var cellGotHit= mutableStateOf(false)
    var ship:MutableState<Ship?> = mutableStateOf(null)
    var canHaveAShip = true
    fun hasShip():Boolean {
        if(ship.value!=null){
            return true
        }
        return false
    }
}
class Ship(var length: Int, positionX:Float, positionY: Float){
    var positionInX = mutableFloatStateOf( positionX)
    var positionInY = mutableFloatStateOf( positionY)
    var vertical:MutableState<Boolean> = mutableStateOf(false)
    var gotHit = 0
    var countOfPlaces =0
    fun hasBeenPlaced ():Boolean{
        if(countOfPlaces==length){
            return true
        }
        return false
    }
    fun isDisabled():Boolean{
        if(gotHit==length){
            return true
        }
        return false
    }
}

class GameViewModel: ViewModel() , SupabaseCallback {

    var shipsGameCells = mutableListOf<ShipCell>()
    var iamReady = mutableStateOf(false)
    var gameCells = mutableListOf<Cell>()
    val gameStarted = mutableStateOf(false)
    val infoText = mutableStateOf("")
    var opponentReady = mutableStateOf(false)
    var changeTurn = mutableStateOf(false)
    private var gameResult :MutableState<GameResult?> = mutableStateOf(null)
    var gameFinished =mutableStateOf(false)
    private var lastCellShot:MutableState<Cell?>  = mutableStateOf(null)
    var opponentShips = mutableIntStateOf(6)
    val cellSize = Resources.getSystem().configuration.screenWidthDp/10
    private var myTurnToShot = false
    var ships: MutableList<Ship> = mutableListOf()
    var winnerText = mutableStateOf( "")

    fun startTheGame(){
        gameStarted.value=true
        if(SupabaseService.currentGame!!.player1.id == SupabaseService.player!!.id) {
           changeTurn.value=true
            myTurnToShot=true
        }
    }

    init {

        ships.add(Ship(4,-(cellSize*2.5f),-cellSize*6.5f))
        ships.add( Ship(3,cellSize*2.5f , -cellSize*6.5f))
        ships.add(Ship(2,-cellSize*3.5f,cellSize*6.5f))
        ships.add(Ship(2,-cellSize*1f,cellSize*6.5f))
        ships.add(Ship(1,cellSize*1.5f,cellSize*6.5f))
        ships.add(Ship(1,cellSize*3f,cellSize*6.5f))

        SupabaseService.callbackHandler=this
        for (y in 0..9){
            for(x in 0..9){
                gameCells.add(Cell(x,y))
            }
        }
        for (y in 0..9){
            for(x in 0..9){
                shipsGameCells.add(ShipCell(x,y))
            }
        }
    }
    fun calculatePosition(cellSize: Int, halfScreenSize: Int){
        for(ix in 0 until shipsGameCells.size){
            shipsGameCells[ix].canHaveAShip = true
            shipsGameCells[ix].ship.value=null
        }
        for (clearShips in 0 until ships.size){
            ships[clearShips].countOfPlaces=0
        }
        for(position in 0 until ships.size){
            val shipX: Float =((ships[position].positionInX.floatValue+halfScreenSize)/cellSize)
            val shipY =(( ships[position].positionInY.floatValue+halfScreenSize)/cellSize)
            val shipPlace: ShipCell? = shipsGameCells.find { it.xC==shipX.toInt()&&it.yC==shipY.toInt() }
            if(shipPlace!=null){
                var from =0
                var to =0
                when (ships[position].length){
                    4->{
                        from = -2
                        to = 2
                    }
                    3-> {
                        from = -1
                        to = 2
                    }
                    2-> {
                        from = 0
                        to = 2
                    }
                    1 -> {
                        from = 0
                        to = 1
                    }
                }
                for (length in from  until to) {
                    val cell: ShipCell? = if (ships[position].vertical.value) {
                        shipsGameCells.find { it.xC == shipX.toInt() && it.yC == shipY.toInt() + length }
                    } else {
                        shipsGameCells.find { it.xC == shipX.toInt() + length && it.yC == shipY.toInt() }
                    }
                    if(cell!=null) {
                        cell.ship.value = ships[position]
                        if (cell.canHaveAShip) {
                            if (!ships[position].vertical.value) {
                                checkInH(cell,from,to,length)
                            }
                            if (ships[position].vertical.value) {
                                checkInV(cell,from,to,length)
                            }
                            cell.canHaveAShip = false
                            ships[position].countOfPlaces += 1
                        }
                    }
                }
            }
        }
    }
   private fun checkInH(cell: ShipCell,from:Int,to:Int,length: Int){
        if (length == to - 1) {
            val x = shipsGameCells.indexOf(cell) + 1
            if (x % 10 in 1..8) {
                shipsGameCells[x].canHaveAShip = false
            }
        }
        if (length == from) {
            val x = shipsGameCells.indexOf(cell) - 1
            if (x % 10 in 1..8) {
                shipsGameCells[x].canHaveAShip = false
            }
        }
        var y = (shipsGameCells.indexOf(cell) + 10)
        if (y in 0..99) {
            shipsGameCells[y.absoluteValue].canHaveAShip = false
        }
        y = shipsGameCells.indexOf(cell) - 10
        if (y in 0..99)
            shipsGameCells[y.absoluteValue].canHaveAShip = false
    }
     private fun checkInV(cell: ShipCell,from:Int,to:Int,length: Int){
        var x = shipsGameCells.indexOf(cell) +1
        if (x % 10 in 1..8) {
            shipsGameCells[x].canHaveAShip = false
        }
        x = shipsGameCells.indexOf(cell) -1
        if (x % 10 in 1..8) {
            shipsGameCells[x].canHaveAShip = false
        }
        if(length==from) {
            val y = (shipsGameCells.indexOf(cell) - 10)
            if (y in 0..99) {
                shipsGameCells[y].canHaveAShip = false
            }
        }
        if(length==to-1) {
            val y = (shipsGameCells.indexOf(cell) + 10)
            if (y in 0..99) {
                shipsGameCells[y].canHaveAShip = false
            }
        }
    }
    private fun checkIfPlayerPlacedShips():Boolean{
        for(x in 0 until ships.size){
            if(!ships[x].hasBeenPlaced()){
                return false
            }
        }
        return true
    }
    private fun sendAnswer(x:Int, y:Int){
        val findCell=shipsGameCells.find { it.xC==x&&it.yC==y }
        findCell!!.cellGotHit.value=true
        if (findCell.hasShip()){
            viewModelScope.launch {
                findCell.ship.value!!.gotHit+=1
                if(findCell.ship.value!!.isDisabled()){
                    SupabaseService.sendAnswer(ActionResult.SUNK)
                    ships.remove(findCell.ship.value)
                }
                else {
                    SupabaseService.sendAnswer(ActionResult.HIT)
                }
            }
        }
        else if(!findCell.hasShip()){
            viewModelScope.launch {
                SupabaseService.sendAnswer(ActionResult.MISS)
            }
        }
        if(ships.isEmpty()){
            gameResult.value=GameResult.LOSE
            gameFinished.value=true
            winnerText.value="Hard luck!! You lost"
            viewModelScope.launch {
                SupabaseService.gameFinish(GameResult.WIN)
            }
        }
    }
    private fun gitAnswer(action:ActionResult){
        when (action) {
            ActionResult.SUNK -> {
                myTurnToShot=true
                viewModelScope.launch {
                    opponentShips.intValue-=1
                    infoText.value = action.toString()
                    lastCellShot.value!!.hadShip.value = true
                    delay(2000)
                    infoText.value = ""
                }
            }
            ActionResult.HIT -> {
                myTurnToShot=true
                viewModelScope.launch {
                    infoText.value = action.toString()
                    lastCellShot.value!!.hadShip.value = true
                    delay(2000)
                    infoText.value = ""
                }
            }
            ActionResult.MISS -> {
                releaseTurn()
                viewModelScope.launch {
                    infoText.value = action.toString()
                    lastCellShot.value!!.hadShip.value = false
                    delay(2000)
                    infoText.value = ""
                }
            }
        }
    }
    fun leaveGame(){
        gameFinished.value=true
        winnerText.value="You Surrendered"
        viewModelScope.launch {
            SupabaseService.leaveGame()
        }
    }
    fun reJoinLobby(){
        viewModelScope.launch {
            SupabaseService.joinLobby(SupabaseService.player!!)
        }
    }
    fun ready(){
        if(checkIfPlayerPlacedShips()) {
            viewModelScope.launch {
                iamReady.value = true
                SupabaseService.playerReady()
            }
        }
    }
    private fun releaseTurn(){
        myTurnToShot=false
        viewModelScope.launch {
            SupabaseService.releaseTurn()
        }
        changeTurn.value=true
    }
    fun sendFire(cell: Cell) {
        if (cell.isActive.value&&myTurnToShot) {
            viewModelScope.launch {
                SupabaseService.sendTurn(cell.xC, cell.yC)
            }
            cell.isActive.value = false
            lastCellShot.value=cell
        }
        myTurnToShot=false
    }
    private fun winn(status: GameResult){

        if (status == GameResult.LOSE){
            winnerText.value = "You lost opponent won! "
            gameResult.value=GameResult.LOSE
            gameFinished.value=true
        }
        if (status== GameResult.WIN){
            winnerText.value = "Direct hit ! You sunk all the ships"
            gameResult.value=GameResult.WIN
            gameFinished.value=true
        }
        if(status == GameResult.SURRENDER){
            winnerText.value = "You won opponent surrendered"
            gameResult.value=GameResult.WIN
            gameFinished.value=true
        }
    }
    override suspend fun playerReadyHandler() {
        opponentReady.value=true
    }

    override suspend fun releaseTurnHandler() {
        changeTurn.value = !changeTurn.value
        myTurnToShot=true
    }

    override suspend fun actionHandler(x: Int, y: Int) {
        sendAnswer(x,y)
    }

    override suspend fun answerHandler(status: ActionResult) {
        gitAnswer(status)
    }

    override suspend fun finishHandler(status: GameResult) {
        winn(status)
    }
}