package com.example.noteappdemo

sealed class Screen ( val route : String){
    object MainScreen : Screen ("MainScreen")
    object AddNoteScreen : Screen("DetailScreen")
    object EditScreen : Screen ("EditScreen")
    object  DeleteScreen : Screen("DeleteScreen")
}

