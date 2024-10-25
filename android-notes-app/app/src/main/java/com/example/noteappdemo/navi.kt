package com.example.noteappdemo

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable

fun navi (){
    val naviController = rememberNavController()
    NavHost(navController = naviController, startDestination = Screen.MainScreen.route){
        composable(route= Screen.MainScreen.route
        ){
            MainScreen(navController = naviController)
        }
        composable(route = Screen.AddNoteScreen.route
        ){
            AddNoteScreen(navController = naviController)
        }
        composable(route = Screen.EditScreen.route + "/{index}" //what is this ???!!!
            , arguments = listOf(
                navArgument("index"){
                    type= NavType.IntType
                }
            )
        ){
            EditScreen(navController =naviController, it.arguments?.getInt("index"))
        }
        composable(route = Screen.DeleteScreen.route)
        {
            DeleteScreen(navController = naviController)
        }
    }
}
