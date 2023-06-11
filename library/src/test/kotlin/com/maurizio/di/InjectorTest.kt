package com.maurizio.di

import io.mockk.every
import io.mockk.mockk
import javax.inject.Inject
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class InjectorTest {
    private val finder: ClassFinder = mockk(relaxed = true)
    private val injector = Injector(finder)

    @Test
    fun `given no constructor with @Inject annotation found when called then an exception is thrown`() {
        // given
        every { finder.getClassesAnnotatedWithInject() } returns setOf(
            ClassA::class.java.constructors[0],
            ClassB::class.java.constructors[0]
        )

       // then
        assertThatThrownBy { injector.inject(this::class.java.packageName) }.isInstanceOf(DependencyNotFoundException::class.java)
            .hasMessage("unable to instantiate ${ClassC::class}, no constructor with @Inject annotation found")
    }

    @Test
    fun `given there is a circular dependency when called then an exception is thrown`() {
        // given
        every { finder.getClassesAnnotatedWithInject() } returns setOf(
            ClassA::class.java.constructors[0],
            ClassB::class.java.constructors[0],
            ClassC::class.java.constructors[0],
            ClassD::class.java.constructors[0],
        )

        // then
        assertThatThrownBy { injector.inject(this::class.java.packageName) }.isInstanceOf(CircularDependencyException::class.java)
            .hasMessage("Circular dependency detected")
    }

    @Test
    fun `given a class is missing in the dependencies when called then an exception is thrown`() {
        // given
        every { finder.getClassesAnnotatedWithInject() } returns setOf(
            ClassA::class.java.constructors[0],
            ClassC::class.java.constructors[0],
            ClassD::class.java.constructors[0],
        )

        // then
        assertThatThrownBy { injector.inject(this::class.java.packageName) }.isInstanceOf(DependencyNotFoundException::class.java)
            .hasMessage("unable to instantiate ${ClassB::class}, no constructor with @Inject annotation found")
    }

    @Test
    fun `given the dependencies are correct when called then builds the graph`() {
        // given
        every { finder.getClassesAnnotatedWithInject() } returns setOf(
            ClassC::class.java.constructors[0],
            ClassD::class.java.constructors[0],
            ClassE::class.java.constructors[0],
            ClassF::class.java.constructors[0]
        )

        // when
        injector.inject(this::class.java.packageName)

        // then
        assertThat(injector.get<ClassC>()).isNotNull
        assertThat(injector.get<ClassD>()).isNotNull
        assertThat(injector.get<ClassE>()).isNotNull
        assertThat(injector.get<ClassF>()).isNotNull
    }

    @Test
    fun `given the dependencies are correct with property when called then builds the graph`() {
        // given
        every { finder.getClassesAnnotatedWithInject() } returns setOf(
            ClassC::class.java.constructors[0],
            ClassD::class.java.constructors[0],
            ClassE::class.java.constructors[0],
            ClassF::class.java.constructors[0])

        // when
        injector.inject(this::class.java.packageName)

        // then
        assertThat(injector.get<ClassC>()).isNotNull
        assertThat(injector.get<ClassD>()).isNotNull
        assertThat(injector.get<ClassE>()).isNotNull
        assertThat(injector.get<ClassF>()).isNotNull
    }
}

class ClassA(val classB: ClassB)

class ClassB(val classA: ClassA, ClassC: ClassC)

class ClassC

class ClassD(val classC: ClassC)

class ClassE(val classD: ClassD)

class ClassF(val classE: ClassE, val classD: ClassD, classC: ClassC)
