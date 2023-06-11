package com.maurizio.di

import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CyclesDetectorTest {
    private val sorter: TopologicalSorter = mockk(relaxed = true)
    private val cyclesDetector = CyclesDetector(sorter)

    @Test
    fun `given no constructor with @Inject annotation found when called then an exception is thrown`() {
        // given
        val classes = setOf(
            ClassA::class.java.constructors[0],
            ClassB::class.java.constructors[0]
        )

        // when
        val message =
            assertThrows<DependencyNotFoundException> { cyclesDetector.detectCycles(classes) }.message

        // then
        assertThat(message)
            .isEqualTo("unable to instantiate ${ClassC::class}, no constructor with @Inject annotation found")
    }

    @Test
    fun `given a class is missing in the dependencies when called then an exception is thrown`() {
        // given
        val classes = setOf(
            ClassA::class.java.constructors[0],
            ClassC::class.java.constructors[0],
            ClassD::class.java.constructors[0],
        )

        // when
        val message =
            assertThrows<DependencyNotFoundException> { cyclesDetector.detectCycles(classes) }.message

        // then
        assertThat(message)
            .isEqualTo("unable to instantiate ${ClassB::class}, no constructor with @Inject annotation found")
    }
}