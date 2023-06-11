package com.maurizio.di

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class GraphBuilderTest {
    private val graphBuilder = GraphBuilder()

    @Test
    fun `given the dependencies are correct when called then buids the graph`() {
        // given

        // when
        val graph = graphBuilder.buildGraph(
            setOf(
                ClassC::class.java.constructors[0],
                ClassD::class.java.constructors[0],
                ClassE::class.java.constructors[0],
                ClassF::class.java.constructors[0]
            )
        )

        // then
        Assertions.assertThat(graph.get(ClassC::class)).isNotNull
        Assertions.assertThat(graph.get(ClassD::class)).isNotNull
        Assertions.assertThat(graph.get(ClassE::class)).isNotNull
        Assertions.assertThat(graph.get(ClassF::class)).isNotNull
    }
}