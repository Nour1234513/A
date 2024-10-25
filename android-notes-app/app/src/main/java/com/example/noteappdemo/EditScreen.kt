package com.example.noteappdemo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun EditScreen(navController: NavController, index: Int?){
    var showError by remember {
        mutableStateOf(false)
    }
    var alertText by remember {
        mutableStateOf("")
    }
    var textinput by remember {
        mutableStateOf(lista[index!!].text)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
            , horizontalArrangement = Arrangement.SpaceAround
        ){
            Text(
                text=lista[index!!].tittle
                , fontSize = 30.sp
            )
            Button(onClick = {

                    if (valdation(textinput, 50, 300)) {
                        lista[index!!].text = textinput
                        navController.popBackStack()
                    }
                    else {
                        alertText = "Text should be between 50 to 300 Char"
                        showError = true
                    }
            }) {
                Text("Back")
            }
        }
        Column(modifier = Modifier.fillMaxSize()) {
            TextField(
                value = textinput
                , label = {Text("Tittle")}
                ,modifier = Modifier.fillMaxSize()
                , onValueChange = {
                    textinput = it
                })
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