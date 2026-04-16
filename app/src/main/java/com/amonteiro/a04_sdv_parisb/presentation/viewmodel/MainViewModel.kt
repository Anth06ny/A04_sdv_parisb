package com.amonteiro.a04_sdv_parisb.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amonteiro.a04_sdv_parisb.data.remote.DescriptionEntity
import com.amonteiro.a04_sdv_parisb.data.remote.KtorWeatherApi
import com.amonteiro.a04_sdv_parisb.data.remote.TempEntity
import com.amonteiro.a04_sdv_parisb.data.remote.WeatherEntity
import com.amonteiro.a04_sdv_parisb.data.remote.WindEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

suspend fun main() {
    val viewModel = MainViewModel()
    viewModel.loadWeathers("")

    while (viewModel.runInProgress.value) {
        println("Attente...")
        delay(500)
    }

    //Affichage de la liste (qui doit être remplie) contenue dans la donnée observable
    println("List : ${viewModel.dataList.value}")
    println("errorMessage : ${viewModel.errorMessage.value}")

    //Pour que le programme s'arrête, inutile sur Android
    KtorWeatherApi.close()
}

class MainViewModel : ViewModel() {
    //MutableStateFlow est une donnée observable
    val dataList = MutableStateFlow(emptyList<WeatherEntity>())
    val runInProgress = MutableStateFlow(false)
    val errorMessage = MutableStateFlow("")

    init {//Création d'un jeu de donnée au démarrage
        println("Instanciation de MainViewModel")
        //loadFakeData()
    }

    fun loadWeathers(cityName: String) {

        runInProgress.value = true
        errorMessage.value = ""


        viewModelScope.launch(Dispatchers.IO) {
            try {
                dataList.value = KtorWeatherApi.loadWeathers(cityName)
                //dataList.value = KtorWeatherApi.loadWeathersFake(cityName)
            }
            catch (e: Exception) {
                e.printStackTrace()
                errorMessage.value = e.message ?: "Une erreur est survenue"
            }
            finally {
                runInProgress.value = false
            }
        }
    }



    fun loadFakeData(runInProgress :Boolean = false, errorMessage:String = "" ) {
        this.runInProgress.value = runInProgress
        this.errorMessage.value = errorMessage
        dataList.value = listOf(
            WeatherEntity(
                id = 1,
                name = "Paris",
                main = TempEntity(temp = 18.5),
                weather = listOf(
                    DescriptionEntity(description = "ciel dégagé", icon = "https://picsum.photos/200")
                ),
                wind = WindEntity(speed = 5.0)
            ),
            WeatherEntity(
                id = 2,
                name = "Toulouse",
                main = TempEntity(temp = 22.3),
                weather = listOf(
                    DescriptionEntity(description = "partiellement nuageux", icon = "https://picsum.photos/201")
                ),
                wind = WindEntity(speed = 3.2)
            ),
            WeatherEntity(
                id = 3,
                name = "Toulon",
                main = TempEntity(temp = 25.1),
                weather = listOf(
                    DescriptionEntity(description = "ensoleillé", icon = "https://picsum.photos/202")
                ),
                wind = WindEntity(speed = 6.7)
            ),
            WeatherEntity(
                id = 4,
                name = "Lyon",
                main = TempEntity(temp = 19.8),
                weather = listOf(
                    DescriptionEntity(description = "pluie légère", icon = "https://picsum.photos/203")
                ),
                wind = WindEntity(speed = 4.5)
            )
        ).shuffled() //shuffled() pour avoir un ordre différent à chaque appel
    }

}