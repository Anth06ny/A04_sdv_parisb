package com.amonteiro.a04_sdv_parisb.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.amonteiro.a04_sdv_parisb.data.remote.KtorWeatherApi
import com.amonteiro.a04_sdv_parisb.data.remote.WeatherEntity
import kotlinx.coroutines.flow.MutableStateFlow

suspend fun main(){
    val viewModel = MainViewModel()
    viewModel.loadWeathers("Nice")
    //Affichage de la liste (qui doit être remplie) contenue dans la donnée observable
    println("List : ${viewModel.dataList.value}" )

    //Pour que le programme s'arrête, inutile sur Android
    KtorWeatherApi.close()
}

class MainViewModel : ViewModel() {
    //MutableStateFlow est une donnée observable
    val dataList = MutableStateFlow(emptyList<WeatherEntity>())

    suspend fun loadWeathers(cityName:String){
        dataList.value =  KtorWeatherApi.loadWeathers(cityName)
    }
}