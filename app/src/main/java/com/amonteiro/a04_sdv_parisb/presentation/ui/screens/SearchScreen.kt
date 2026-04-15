package com.amonteiro.a04_sdv_parisb.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amonteiro.a04_sdv_parisb.presentation.ui.theme.A04_sdv_parisbTheme

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true,
           uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES or android.content.res.Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun SearchScreenPreview() {
    //Il faut remplacer NomVotreAppliTheme par le thème de votre application
    //Utilisé par exemple dans MainActivity.kt sous setContent {...}
    A04_sdv_parisbTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            SearchScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun SearchScreen(modifier: Modifier = Modifier) {

    Column(modifier= modifier) {
        println("SearchScreen()")
        Text(text = "Text1",fontSize = 20.sp)
        Spacer(Modifier.size(8.dp))
        Text(text = "Text2",fontSize = 14.sp)

        repeat(5) {
            PictureRowItem(text = "coucou$it")
        }
    }

}

@Composable
fun PictureRowItem(text :String){
    Text(text = text,fontSize = 20.sp)
}