package com.maurizio.di

import org.reflections.Reflections
import org.reflections.scanners.Scanners
import java.lang.reflect.Constructor
import javax.inject.Inject

class ClassFinder {
    private var reflections: Reflections? = null

    fun init(pckg: String) {
        reflections = Reflections(pckg, *Scanners.values())
    }

    fun getClassesAnnotatedWithInject(): Set<Constructor<out Any>> =
        (reflections?.getConstructorsAnnotatedWith(Inject::class.java) ?: emptySet()) + getFieldsAnnotatedInject()

    private fun getFieldsAnnotatedInject() =
        (reflections?.getFieldsAnnotatedWith(Inject::class.java) ?: emptySet()).toList().map { it.declaringClass.constructors[0] }.toSet()
}