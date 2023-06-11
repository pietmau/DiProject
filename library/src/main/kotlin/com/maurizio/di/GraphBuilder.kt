package com.maurizio.di

import java.lang.reflect.Constructor
import javax.inject.Inject
import kotlin.reflect.KClass
import org.reflections.Reflections

class GraphBuilder(private val reflections: Reflections = Reflections()) {

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
        graph.values.forEach {
            it::class.java.fields.forEach { field ->
                if (field.isAnnotationPresent(Inject::class.java)) {
                    val type = field.type.kotlin
                    val value = graph[type] ?: throw RuntimeException("No implementation found for $type")
                    field.set(it, value)
                }
            }
        }
        return graph
    }
}