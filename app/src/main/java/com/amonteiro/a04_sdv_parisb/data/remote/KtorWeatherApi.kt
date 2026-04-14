package com.amonteiro.a04_sdv_parisb.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

//Suspend sera expliqué dans le chapitre des coroutines
suspend fun main() {
    val list = KtorWeatherApi.loadWeathers("Nice")

    for (w in list) {
        println("""
            Il fait ${w.main.temp}° à ${w.name} (id=${w.id}) avec un vent de ${w.wind.speed} m/s
            -Description : ${w.weather.getOrNull(0)?.description ?: "-"}
            -Icône : ${w.weather.getOrNull(0)?.icon ?: "-"}
        """.trimIndent())
    }



    KtorWeatherApi.close()
}

object KtorWeatherApi {
    private const val API_URL =
        "https://api.openweathermap.org/data/2.5/find?appid=b80967f0a6bd10d23e44848547b26550&units=metric&lang=fr&q="

    //Création et réglage du client
    private val client  = HttpClient {
        install(Logging) {
            //(import io.ktor.client.plugins.logging.Logger)
            logger = object : Logger {
                override fun log(message: String) {
                    println(message)
                }
            }
            level = LogLevel.INFO  // TRACE, HEADERS, BODY, etc.
        }
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 5000
        }
        //engine { proxy = ProxyBuilder.http("monproxy:1234") }
    }

    //GET Le JSON reçu sera parser en UserDTO
    //Crash si le JSON ne correspond pas
    suspend fun loadWeathers(cityname:String): List<WeatherEntity> {
        val response = client.get(API_URL  + cityname){
//            headers {
//                append("Authorization", "Bearer YOUR_TOKEN")
//                append("Custom-Header", "CustomValue")
//            }
        }
        if (!response.status.isSuccess()) {
            throw Exception("Erreur API: ${response.status} - ${response.bodyAsText()}")
        }

        val list =  response.body<WeatherAPIResponseDTO>().list
        //traintement, on remplace le nom de l'image par l'url complète
        //
        list.forEach {//it : WeatherEntity->
            it.weather.forEach { //it : DescriptionEntity->
                it.icon = "https://openweathermap.org/img/wn/${it.icon}@4x.png"
            }
        }


        return list
    }

    //Ferme le Client mais celui ci ne sera plus utilisable. Uniquement pour le main
    fun close() = client.close()

}

//DATA CLASS

//Possible qu'il y ait besoin de cette annotation en fonction du compilateur
@Serializable //KotlinX impose cette annotation
data class WeatherAPIResponseDTO(
    val list: List<WeatherEntity>,
)

@Serializable //KotlinX impose cette annotation
data class WeatherEntity(
    val id: Long,
    val name: String,
    val main : TempEntity,
    val wind : WindEntity,
    val weather : List<DescriptionEntity>
)

@Serializable
data class DescriptionEntity(
    val description: String,
    var icon: String
)


@Serializable
data class TempEntity(
    val temp: Double
)

@Serializable
data class WindEntity(
    var speed: Double
)

