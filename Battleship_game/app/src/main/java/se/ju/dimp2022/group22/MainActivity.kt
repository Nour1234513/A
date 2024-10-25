package se.ju.dimp2022.group22
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import se.ju.dimp2022.group22.screen.AttackScreen
import se.ju.dimp2022.group22.screen.LobbyScreen
import se.ju.dimp2022.group22.screen.LoginScreen
import se.ju.dimp2022.group22.screen.ShipsScreen
import se.ju.dimp2022.group22.screen.WinnScree
import se.ju.dimp2022.group22.ui.theme.BattleShipTheme
import se.ju.dimp2022.group22.veiwModel.GameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            BattleShipTheme {
                // A surface container using the 'background' color from the theme
                var gameViewModel :GameViewModel = viewModel()
                Surface {
                 //val gameViewModel : GameViewModel = viewModel()
                    NavHost(navController = navController, startDestination = NavigationScreen.Login.route){
                        composable(route = NavigationScreen.Login.route){
                            LoginScreen(navController=navController)
                        }
                        composable(route = NavigationScreen.Lobby.route) {
                            LobbyScreen(navController = navController)
                        }
                        composable(route = NavigationScreen.Attack.route) {
                            AttackScreen(navController = navController,gameViewModel)
                        }
                        composable(route = "${NavigationScreen.Ships.route}/{name}/{name1}",
                            arguments = listOf(
                            navArgument("name"){
                                type= NavType.StringType
                            },
                                navArgument("name1"){
                                    type= NavType.StringType
                            }
                            )
                        ){
                            gameViewModel= viewModel()
                            ShipsScreen(navController = navController
                                ,it.arguments?.getString("name").toString()
                                ,it.arguments?.getString("name1").toString()
                                ,gameViewModel
                            )
                        }
                        composable(route = NavigationScreen.Winn.route) {
                            WinnScree(navController,gameViewModel)
                        }
                    }
                }
            }
        }
    }
}

