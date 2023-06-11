package com.maurizio.di

import org.assertj.core.api.Assertions
import org.junit.Test

class TopologicalSorterTest {
    private val sorter = TopologicalSorter()

    @Test
    fun `given there is a circular dependency when called then an exception is thrown`() {
        // given
        val outgoingEdges = mapOf(
            ClassA::class.java to listOf(ClassB::class.java),
            ClassB::class.java to listOf(ClassA::class.java),
            ClassC::class.java to listOf(ClassB::class.java, ClassD::class.java),
            ClassD::class.java to listOf()
        )
        val incomingEdges: Map<Class<out Any>, Int> =
            mapOf(ClassA::class.java to 1, ClassB::class.java to 2, ClassC::class.java to 0, ClassD::class.java to 1)

        // then
        Assertions.assertThatThrownBy {
            sorter.doTopologicalSorting(
                outgoingEdges,
                incomingEdges
            )
        }.isInstanceOf(CircularDependencyException::class.java)
            .hasMessage("Circular dependency detected")
    }
}
