# Dependency Injection Framework

This is a simple example of a dependency injection framework that demonstrates how to use it in your Kotlin project. The framework allows for easy management of dependencies and facilitates loose coupling between classes.

## Usage

To use the dependency injection framework, follow these steps:

1. Create an instance of `Injector` in your `main` function:

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

3. Define your classes with dependencies and annotate them with the `@Inject` annotation. Here's an example:

```kotlin
class CoffeeMaker @Inject constructor(private val pump: Pump) {

    fun makeCoffee() {
        pump.drip()
        System.out.println("Coffee!!!")
    }

}

class Pump @Inject constructor() {

    fun drip(): Unit {}

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

The `@Inject` annotation is used to mark classes or constructors that have dependencies. The framework uses this annotation to identify the classes that need to be instantiated and injected.


## Conclusion

This dependency injection framework simplifies the management of dependencies in your Kotlin project. By using the provided `Injector` class and the `@Inject` annotation, you can easily wire up your classes and achieve loose coupling between them.

Feel free to explore and extend this framework according to your project's needs. Happy coding!