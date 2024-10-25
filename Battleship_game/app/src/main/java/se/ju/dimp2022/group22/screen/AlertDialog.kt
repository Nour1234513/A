package se.ju.dimp2022.group22.screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.ju.dimp2022.group22.R

@Composable
fun Conformation(onCancel :()-> Unit,onConfirm : ()-> Unit,text:String) {
    AlertDialog(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .paint(
                painter = painterResource(id = R.drawable.boat),
                contentScale = ContentScale.FillBounds,
            )
        , onDismissRequest = { /*TODO*/ }, confirmButton = {
            Button(onClick = {
                onCancel.invoke()
            }
                ,colors = ButtonDefaults.buttonColors(Color.LightGray)) {
                Text(text = "Cancel", color = Color.Black)
            }
            Spacer(Modifier.width(20.dp))
            Button(onClick = {
                onConfirm.invoke()
            }
                , colors = ButtonDefaults.buttonColors(Color.LightGray)
            ) {
                Text(text = "Confirm", color = Color.Black)
            }
        }
        ,text = { Text(text = text, fontSize = 16.sp, fontStyle = FontStyle.Italic) }
        ,textContentColor = Color.Black
        , containerColor = Color.Transparent

    )
}
