package se.ju.dimp2022.group22


sealed class NavigationScreen (val route: String){
    object Login : NavigationScreen(route = "Login")
    object Lobby : NavigationScreen(route = "Lobby")
    object  Attack : NavigationScreen(route = "Attack")
    object Ships : NavigationScreen(route = "Ships")
    object Winn : NavigationScreen(route = "Winn")
}

