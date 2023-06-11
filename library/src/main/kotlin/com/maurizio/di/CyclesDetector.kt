package com.maurizio.di

import java.lang.reflect.Constructor

class CyclesDetector(private val sorter: TopologicalSorter = TopologicalSorter()) {

    fun detectCycles(constructors: Set<Constructor<out Any>>) {
        val incomingEdges = computeIncomingEdges(constructors)
        val outgoingEdges = computeOutgoingEdges(constructors)
        checkIfThereAreClassesWithoutDependencies(incomingEdges)
        sorter.doTopologicalSorting(outgoingEdges, incomingEdges)
    }

    private fun checkIfThereAreClassesWithoutDependencies(edges: Map<Class<out Any>, Int>) =
        edges.values.find { it == 0 } ?: throw CircularDependencyException("Constructors with no parameters not found")

    private fun computeOutgoingEdges(constructors: Set<Constructor<out Any>>): Map<Class<out Any>, List<Class<*>>> {
        val outgoingEdges: MutableMap<Class<out Any>, MutableList<Class<*>>> = mutableMapOf()
        constructors.forEach { constructor: Constructor<out Any> ->
            outgoingEdges.putIfAbsent(constructor.declaringClass, mutableListOf())
        }
        constructors.forEach { constructor: Constructor<out Any> ->
            constructor.parameterTypes.forEach { parameter: Class<*> ->
                if (!outgoingEdges.containsKey(parameter)) {
                    throw DependencyNotFoundException("unable to instantiate $parameter, no constructor with @Inject annotation found")
                }
                outgoingEdges[parameter]?.add(constructor.declaringClass)
            }
        }
        return outgoingEdges
    }

    private fun computeIncomingEdges(constructors: Set<Constructor<out Any>>) =
        constructors.associate { it.declaringClass to it.parameterCount }

}