package com.amonteiro.a04_sdv_parisb.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.amonteiro.a04_sdv_parisb.R
import com.amonteiro.a04_sdv_parisb.data.remote.WeatherEntity
import com.amonteiro.a04_sdv_parisb.presentation.ui.theme.A04_sdv_parisbTheme
import com.amonteiro.a04_sdv_parisb.presentation.viewmodel.MainViewModel

@Preview(showBackground = true, showSystemUi = true)
@Preview(
    showBackground = true, showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES or android.content.res.Configuration.UI_MODE_TYPE_NORMAL
)
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
fun SearchScreen(modifier: Modifier = Modifier, mainViewModel: MainViewModel = MainViewModel()) {

    val list = mainViewModel.dataList.collectAsStateWithLifecycle().value

    Column(modifier = modifier) {
        println("SearchScreen()")

        repeat(list.size) {
            PictureRowItem(data = list[it])
        }
    }

}

@Composable //Composable affichant 1 élément
fun PictureRowItem(modifier: Modifier = Modifier, data: WeatherEntity) {
    Row(modifier = modifier.fillMaxWidth().background(MaterialTheme.colorScheme.onSecondaryContainer)) {

        //Permission Internet nécessaire
        AsyncImage(
            model = data.weather.firstOrNull()?.icon,
            //Pour aller le chercher dans string.xml R de votre package com.nom.projet
            //contentDescription = getString(R.string.picture_of_cat),
            //En dur
            contentDescription = "une photo de chat",
            contentScale = ContentScale.FillWidth,

            //Pour toto.png. Si besoin de choisir l'import pour la classe R, c'est celle de votre package
            //Image d'échec de chargement qui sera utilisé par la preview
            error = painterResource(R.drawable.error),
            //Image d'attente.
            //placeholder = painterResource(R.drawable.toto),

            onError = { println(it) },
            modifier = Modifier
                .heightIn(max = 100.dp)
                .widthIn(max = 100.dp)
        )

        Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
            Text(text = data.name, fontSize = 20.sp, modifier = Modifier,

                color = MaterialTheme.colorScheme.onPrimary
                )
            Text(text = data.getResume().take(10) + "...",
                fontSize = 14.sp, modifier = Modifier,

                color = MaterialTheme.colorScheme.secondaryContainer)
        }
    }

}