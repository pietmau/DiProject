# Dependency Injection Framework

This is a simple example of a dependency injection framework in Kotlin that supports both constructor injection and property injection. It demonstrates how to use the framework in your project.

## Usage

To use the dependency injection framework, follow these steps:

1. Create an instance of the `Injector` class in your `main` function:

```kotlin
fun main(args: Array<String>) {
    val injector = Injector()
    // ...
}
```

2. Use the `inject` method of the `Injector` class to register the desired package for component scanning. For example:

```kotlin
injector.inject("com.maurizio")
```

3. Define your classes with dependencies and annotate them with the `@Inject` annotation on the constructor or properties. Here's an example:

```kotlin
class CoffeeMaker @Inject constructor(private val pump: Pump) {
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
```

4. Finally, use the `get` method of the `Injector` class to obtain an instance of the desired class. For example:

```kotlin
val maker = injector.get<CoffeeMaker>()
maker.makeCoffee()
```

## Classes

### Injector

The `Injector` class is responsible for managing dependencies and providing instances of the requested classes. It provides the following methods:

- `inject(packageName: String)`: Scans the given package for classes with the `@Inject` annotation and registers them for dependency injection.

- `get<T>(): T`: Retrieves an instance of the requested class `T` from the injector.

### @Inject Annotation

The `@Inject` annotation is used to mark constructors and properties that have dependencies. The framework uses this annotation to identify the constructors that need to be invoked and properties that need to be injected.

## Conclusion

This dependency injection framework provides support for both constructor injection and property injection in your Kotlin project. By using the provided `Injector` class and the `@Inject` annotation on constructors and properties, you can easily wire up your classes and achieve loose coupling between them.

Feel free to explore and extend this framework according to your project's needs. Happy coding!

## License

This project is licensed under the MIT License.

## TODO

Publish the library to Maven Central.

