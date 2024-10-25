package com.example.noteappdemo

import android.app.Dialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun DeleteScreen(navController: NavController){
    Column (modifier = Modifier
        .fillMaxSize()
        .background(Color.DarkGray)
    ){
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray)
        , horizontalArrangement = Arrangement.End
        , verticalAlignment = Alignment.CenterVertically
        ){
            Button(onClick = {
                //navController.navigate(Screen.MainScreen.route)
                navController.popBackStack()
            }) {
                Text(text = "Done")
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
        ) {
            items(lista) { note ->
                CheckView(note = note, navController)
            }
        }
    }
}

@Composable
fun CheckView(note : Note,navController : NavController) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Button(modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(vertical = 10.dp),
            onClick = {
                lista.remove(note)
                if(lista.isEmpty()) {
                    //navController.navigate(Screen.MainScreen.route)
                    navController.popBackStack()
                }
            }
        ) {
            Text(text = note.tittle, fontSize = 25.sp)
        }
    }
}
