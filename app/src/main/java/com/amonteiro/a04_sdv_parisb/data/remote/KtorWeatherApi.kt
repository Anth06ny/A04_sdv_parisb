package com.amonteiro.a04_sdv_parisb.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

//Suspend sera expliqué dans le chapitre des coroutines
suspend fun main() {
    val avant = System.currentTimeMillis()
    println("Avant")
    val list = KtorWeatherApi.loadWeathers("Nice")

    for (w in list) {
        println(
            """
            Il fait ${w.main.temp}° à ${w.name} (id=${w.id}) avec un vent de ${w.wind.speed} m/s
            -Description : ${w.weather.getOrNull(0)?.description ?: "-"}
            -Icône : ${w.weather.getOrNull(0)?.icon ?: "-"}
        """.trimIndent()
        )
    }


    println("Términé en ${System.currentTimeMillis() - avant}")

    KtorWeatherApi.close()
}

object KtorWeatherApi {
    private const val API_URL =
        "https://api.openweathermap.org/data/2.5/find?appid=b80967f0a6bd10d23e44848547b26550&units=metric&lang=fr&q="

    //Création et réglage du client
    private val client = HttpClient {
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
    suspend fun loadWeathers(cityname: String): List<WeatherEntity> {
        val response = client.get(API_URL + cityname) {
//            headers {
//                append("Authorization", "Bearer YOUR_TOKEN")
//                append("Custom-Header", "CustomValue")
//            }
        }
        if (!response.status.isSuccess()) {
            throw Exception("Erreur API: ${response.status} - ${response.bodyAsText()}")
        }

        val list = response.body<WeatherAPIResponseDTO>().list
        //traintement, on remplace le nom de l'image par l'url complète
        //
        list.forEach {//it : WeatherEntity->
            it.weather.forEach { //it : DescriptionEntity->
                it.icon = "https://openweathermap.org/img/wn/${it.icon}@4x.png"
            }
        }


        return list
    }

    suspend fun loadWeathersFake(cityname: String): List<WeatherEntity> {
        delay(2000)
        return listOf(
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
    val main: TempEntity,
    val wind: WindEntity,
    val weather: List<DescriptionEntity>
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

