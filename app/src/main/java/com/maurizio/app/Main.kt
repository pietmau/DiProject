package com.maurizio.app

import com.maurizio.di.Injector
import javax.inject.Inject

fun main(args: Array<String>) {
    val injector = Injector()
    injector.inject("com.maurizio")
    val maker = injector.get<CoffeMaker>()
    maker.makeCoffee()
}

class CoffeMaker @Inject constructor(private val pump: Pump) {
    @Inject
    lateinit var heater: Heater

    fun makeCoffee() {
        pump.drip()
        heater.heat()
        System.out.println("Coffee!!!")
    }
}

class Pump @Inject constructor() {

    fun drip(): Unit {}

}

class Heater @Inject constructor() {

    fun heat(): Unit {}

}