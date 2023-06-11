package com.maurizio.di

import kotlin.reflect.KClass

class Injector(
    private val classFinder: ClassFinder = ClassFinder(),
    private val cyclesDetector: CyclesDetector = CyclesDetector(),
    private val graphBuilder: GraphBuilder = GraphBuilder()
) {
    var graph: MutableMap<KClass<out Any>, Any> = mutableMapOf()

    fun inject(packageNeme: String) {
        classFinder.init(packageNeme)
        val annotatedClasses = classFinder.getClassesAnnotatedWithInject()
        cyclesDetector.detectCycles(annotatedClasses)
        graph = graphBuilder.buildGraph(annotatedClasses)
    }

    inline fun <reified T> get(): T = graph[T::class] as T
}
