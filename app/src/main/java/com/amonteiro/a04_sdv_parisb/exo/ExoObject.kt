package com.amonteiro.a04_sdv_parisb.exo

import java.util.Random

fun main() {


    val randomName = RandomName()
    randomName.add("Bobby")
    randomName.add("")
randomName.addAll("h", "iohoih")
}

class RandomName {
    private val list = arrayListOf("Bob", "Toto", "John")
    private var oldValue = ""


    fun add(name: String) = if (name !in list && name.isNotBlank())
        list.add(name)
    else false

    fun next() = list.random()

    fun addAll(vararg names:String) {
        for( n in names){
            add(n)
        }
    }

    fun nextDiff() : String{
        var newValue = next()
        while(newValue == oldValue) {
            newValue = next()
        }

        oldValue = newValue
        return oldValue
    }

    fun nextDiff2() : String{
        oldValue = list.filter { it != oldValue  }.random()
        return oldValue
    }

    fun nextDiff3()  = list.filter { it != oldValue  }.random().also { oldValue = it }

    fun next2() = Pair(nextDiff(), nextDiff())


}


data class CarEntity(var marque: String, var model: String = "") {
    var color = ""

    fun print() = "$marque $model $color"
}

class HouseEntity(var color: String, width: Int, length: Int) {
    var area = width * length
}

class PrintRandomIntEntity(val max: Int) {
    private val random: Random = Random()

    init {
        println(random.nextInt(max))
        println(random.nextInt(max))
        println(random.nextInt(max))
    }
}

class ThermometerEntity(val min: Int, val max: Int, value: Int) {
    var value: Int = value.coerceIn(min, max)
        set(newValue) {
            field = if (newValue < min) min else if (newValue > max) max else newValue
        }

    companion object {
        fun getCelsiusThermometer() = ThermometerEntity(-30, 50, 0)
    }
}

