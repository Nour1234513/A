package com.example.noteappdemo



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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController




data class Note(
    var tittle: String,
    var text: String
)
var lista = SnapshotStateList<Note>()

@Composable

fun MainScreen(navController : NavController){
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.DarkGray)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.1f)
            .background(Color.Gray)
            .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text("Note App", fontSize = 40.sp)
        }
        ListView(lista,navController)
        NavigationBar(navController)
    }
}


@Composable
fun ListView(list: List<Note>,navController : NavController) {
    LazyColumn (modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.9f)
    ){
        items(list) { note ->
            RowView(note = note, navController)
        }
    }
}


@Composable
fun RowView(note : Note,navController : NavController) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Button(modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(vertical = 10.dp),
            onClick = { navController.navigate("EditScreen/${lista.indexOf(note)}") }
        ) {
            Text(text = note.tittle, fontSize = 25.sp)
        }
    }
}

@Composable
fun NavigationBar(navController: NavController){
    Row(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(Color.Gray)
        , horizontalArrangement = Arrangement.SpaceAround
        , verticalAlignment = Alignment.CenterVertically
    ){
        Button(onClick = {
            navController.navigate(Screen.AddNoteScreen.route)
        }) {
            Text(text = "Add Note")
        }
        Button(onClick = {
            navController.navigate(Screen.DeleteScreen.route)
        }) {
            Text(text = "Delete Note")
        }
    }
}