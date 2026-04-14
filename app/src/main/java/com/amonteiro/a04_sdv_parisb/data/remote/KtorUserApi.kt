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
    val user = KtorUserApi.loadUser()

    println("""
        Il s'appelle ${user.name} pour le contacter :
        Phone : ${user.coord?.phone ?: "-"}
        Mail : ${user.coord?.mail?: "-"}
    """.trimIndent())

    KtorUserApi.close()
}

object KtorUserApi {
    private const val API_URL =
        "https://www.amonteiro.fr/api/randomuser"

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
    suspend fun loadUser(): UserDTO {
        val response = client.get(API_URL){
//            headers {
//                append("Authorization", "Bearer YOUR_TOKEN")
//                append("Custom-Header", "CustomValue")
//            }
        }
        if (!response.status.isSuccess()) {
            throw Exception("Erreur API: ${response.status} - ${response.bodyAsText()}")
        }

        return response.body()
        //possibilité de typer le body
        //.body<List<MuseumDTO>>()
    }

    //Ferme le Client mais celui ci ne sera plus utilisable. Uniquement pour le main
    fun close() = client.close()

}

//DATA CLASS

//Possible qu'il y ait besoin de cette annotation en fonction du compilateur
@Serializable //KotlinX impose cette annotation
data class UserDTO(
    val age: Int,
    val name: String,
    val coord: CoordDTO? = null,
)

@Serializable //KotlinX impose cette annotation
data class CoordDTO(
    val phone: String? = null,
    val mail: String? = null,
)