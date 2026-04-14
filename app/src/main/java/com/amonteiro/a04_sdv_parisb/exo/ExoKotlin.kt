package com.amonteiro.a04_sdv_parisb.exo

import android.R.attr.text
import android.R.attr.y
import android.util.Log.d
import kotlin.random.Random



fun main() {


//    Créer une variable v1 de type String et y mettre la chaine "toto"
    var v1  = "toto"
    var v2 : String? = "toto"
    var v3 : String? = null

    println(v1.uppercase())
    println(v2?.uppercase())
    println(v3?.uppercase())

    var v4 : Int? = null
//Laisser le curseur de la souris sur Random pour qu'il vous propose de l'importer
//Choisir celui de Koltin
    if(Random.nextBoolean()){
        v4 = Random.nextInt(10)
    }
    println(v4 ?: "Pas de valeur")


    var v5 =  v3 + v3 // "nullnull"
    if(v3.isNullOrBlank()) { //myIsNullOrBlank(v3)

    }

    var res = boulangerie(0,1,2)
    println(res)
    res = boulangerie(nbSand = 5) // boulangerie(0,0,5)
    println(res)

}

fun boulangerie(nbCroi:Int = 0, nbBag:Int =0, nbSand:Int =0 ): Double
    = nbCroi * PRICE_CROISSANT + nbBag * PRICE_BAGUETTE + nbSand * PRICE_SANDWITCH


fun String?.myIsNullOrBlank(): Boolean {
    return this == null || this.isNotBlank()
}

