package com.example.noteappdemo




import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.AlertDialog as AlertDialog




@Composable
fun AddNoteScreen(navController: NavController){
    var Text by remember {
        mutableStateOf("")
    }
    var Title by remember {
        mutableStateOf("")
    }

    var alertText by remember {
        mutableStateOf("")
    }
    var showError by remember {
        mutableStateOf(false)
    }

Column(modifier = Modifier
    .fillMaxSize()
    .background(Color.Gray)
    .padding(5.dp)
    , verticalArrangement = Arrangement.SpaceAround
    ) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .background(Color.Gray),
        horizontalArrangement = Arrangement.Center
    ) {
        TextField(
            value = Title ,
            label = { Text("Tittle") },
            modifier = Modifier.fillMaxWidth(),
            onValueChange =
            {
                Title = it
            })
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f)
            .background(Color.Gray)
    ) {
        TextField(
            value = Text,
            label = { Text("Text") },
            modifier = Modifier.fillMaxSize(),
            onValueChange =
            {
                Text = it
            })
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
    ) {
        Button(
            onClick = {
                //navController.navigate(Screen.MainScreen.route)
                navController.popBackStack()
            }
        ) {
            Text("Cancel")
        }

        Button(
            onClick = {
                if (valdation(Title, 3, 10)) {
                    if (valdation(Text, 50, 300)) {
                        lista.add(Note(Title,Text))
                        //navController.navigate(Screen.MainScreen.route)
                        navController.popBackStack()
                    } else {
                        alertText = "Text should be between 50 to 300 Char"
                        showError = true
                    }
                } else {
                    alertText = "Tittle should be between 3 to 10 Char"
                    showError = true
                }
            },
        ) {
            Text("Add Note")
        }
    }
        if (showError) {
            AlertDialog(onDismissRequest = { showError=false}
                ,confirmButton = { Button(onClick = {showError= false }) {
                    Text("Cancel") } }
                ,title = { Text("Obs!!") }
                ,text = {Text(alertText)}
            )
        }
    }
}

fun valdation(Text:String , minLength : Int , maxLength : Int) : Boolean {
    return (Text.length>=minLength&&Text.length<=maxLength)
}