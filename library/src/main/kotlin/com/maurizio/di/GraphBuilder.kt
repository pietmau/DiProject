package com.maurizio.di

import java.lang.reflect.Constructor
import kotlin.reflect.KClass

class GraphBuilder {

    fun buildGraph(constructors: Set<Constructor<out Any>>): MutableMap<KClass<out Any>, Any> {
        val graph: MutableMap<KClass<out Any>, Any> = mutableMapOf()
        constructors.filter { it.parameterCount == 0 }.forEach {
            graph[it.declaringClass.kotlin] = it.newInstance()
        }
        constructors.filter { it.parameterCount > 0 }.forEach {
            val parameters = it.parameterTypes.map { type ->
                graph[type.kotlin] ?: throw RuntimeException("No implementation found for $type")
            }.toTypedArray()
            graph[it.declaringClass.kotlin] = it.newInstance(*parameters)
        }
        return graph
    }

}
