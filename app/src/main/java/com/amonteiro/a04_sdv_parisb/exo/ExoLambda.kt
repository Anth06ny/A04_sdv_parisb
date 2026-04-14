package com.amonteiro.a04_sdv_parisb.exo

import android.R.attr.data
import com.amonteiro.a04_sdv_parisb.data.remote.WindEntity

class MyLiveData<T>(value : T) {
    var value : T = value
        set(newValue) {
            field = newValue
            //déclencher l'observateur
            action(newValue)
        }

    var action : (T)->Unit = {}
}

fun main() {
    var data = MyLiveData(WindEntity(5.0))

    data.action = { println(it)}

    //data.value = WindEntity(6.0)
    data.value.speed++

}



fun exo1() {

    //Déclaration
    val lower: (String) -> Unit = { it: String -> println(it.uppercase()) }
    val lower2 = { it: String -> println(it.uppercase()) }
    val lower3: (String) -> Unit = { it -> println(it.uppercase()) }
    val lower4: (String) -> Unit = { println(it.uppercase()) }

    //Appel
    lower("Coucou")

    val hour: (Int) -> Int = { it / 60 }
    println(hour(125))

    val max = { a: Int, b: Int -> Math.max(a, b) }

    val reverse: (String) -> String = { it.reversed() }

    var minToMinHour: ((Int?) -> Pair<Int, Int>?)? = { if (it != null) Pair(it / 60, it % 60) else null }
    //val minToMinHour2: (Int?) -> Pair<Int, Int>? = { it?.let { Pair(it / 60, it % 60) } }

    println(minToMinHour?.invoke(125))
    println(minToMinHour?.invoke(null))
    minToMinHour = null


}