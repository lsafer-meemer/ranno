@file:JvmName("RannoKt")
@file:JvmMultifileClass

/*
 *	Copyright 2023 cufy.org
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	    http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package org.cufy.ranno

import org.cufy.ranno.internal.trySetAccessibleAlternative
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.jvm.jvmErasure

// @formatter:off

//////////////////////////////////////////////////

/**
 * An [@Enumerable][Enumerable] function/property
 * validator that ensures the annotated
 * function/property has a specific count,
 * position and types of parameters.
 *
 * @author LSafer
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class EnumerableParameters(
    /**
     * This value defines the parameters count,
     * positions and types of valid functions or
     * property.
     *
     * If a function or a property is a class
     * member, its `this` argument is considered
     * its first parameter.
     *
     * If a function or property is an extension,
     * its `this` argument is considered its
     * first parameter unless it is a class member
     * then it will be the second parameter.
     *
     * @since 1.0.0
     */
    vararg val value: KClass<*>,
)

/**
 * An [@Enumerable][Enumerable] function/property
 * validator that ensures the annotated
 * function/property has a specific return type.
 *
 * @author LSafer
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class EnumerableReturnType(
    /**
     * This value defines the allowed return types
     * of a valid function or property.
     *
     * A valid function or property might have
     * exactly this class or a class that extends
     * this class.
     *
     * @since 1.0.0
     */
    val value: KClass<*>,
)

/**
 * An [@Enumerable][Enumerable] class
 * validator that ensures the annotated
 * class extends a specific type.
 *
 * @author LSafer
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class EnumerableSuperType(
    /**
     * This value defines the type a valid class
     * should extend.
     *
     * A valid class might extend exactly this
     * class or a class that extends this class.
     *
     * @since 1.0.0
     */
    val value: KClass<*>,
)

//////////////////////////////////////////////////

/**
 * Return all the elements annotated with [T].
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
inline fun <reified T : Annotation> elementsWith(predicate: (T) -> Boolean): List<KAnnotatedElement> {
    return elementsWith(T::class).filter { it.findAnnotations<T>().any(predicate) }
}

//////////////////////////////////////////////////

/**
 * Return all the functions annotated with [T].
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
inline fun <reified T : Annotation> functionsWith(predicate: (T) -> Boolean): List<KFunction<*>> {
    return functionsWith(T::class).filter { it.findAnnotations<T>().any(predicate) }
}

//////////////////////////////////////////////////

/**
 * Return all the properties annotated with [T].
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
inline fun <reified T : Annotation> propertiesWith(predicate: (T) -> Boolean): List<KProperty<*>> {
    return propertiesWith(T::class).filter { it.findAnnotations<T>().any(predicate) }
}

//////////////////////////////////////////////////

/**
 * Return all the classes annotated with [T].
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
inline fun <reified T : Annotation> classesWith(predicate: (T) -> Boolean): List<KClass<*>> {
    return classesWith(T::class).filter { it.findAnnotations<T>().any(predicate) }
}

//////////////////////////////////////////////////

/**
 * Call the functions annotated with [annotation] qualified name
 * and matches the given [predicate]
 * with the given [arguments].
 *
 * __Note: Suspend functions won't be run with this
 * function even if a continuation is passed as the
 * last argument. To also invoke suspending functions
 * use [runWithSuspend].__
 *
 * Only functions that can be invoked with the
 * provided arguments will be invoked.
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @param arguments the arguments.
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
fun runWith(annotation: String, vararg arguments: Any?, predicate: (KFunction<*>) -> Boolean = { true }): List<Any?> {
    @OptIn(ExperimentalRannoApi::class)
    return elementsWith(annotation)
        .asSequence()
        .filterIsInstance<KFunction<*>>()
        .filter { it.canCallWith(*arguments) && predicate(it) }
        .map { it.callWith(*arguments) }
        .toList()
}

/**
 * Call the functions annotated with [annotation]
 * and matches the given [predicate]
 * with the given [arguments].
 *
 * __Note: Suspend functions won't be run with this
 * function even if a continuation is passed as the
 * last argument. To also invoke suspending functions
 * use [runWithSuspend].__
 *
 * Only functions that can be invoked with the
 * provided arguments will be invoked.
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @param arguments the arguments.
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
fun runWith(annotation: KClass<out Annotation>, vararg arguments: Any?, predicate: (KFunction<*>) -> Boolean = { true }): List<Any?> {
    @OptIn(ExperimentalRannoApi::class)
    return elementsWith(annotation)
        .asSequence()
        .filterIsInstance<KFunction<*>>()
        .filter { it.canCallWith(*arguments) && predicate(it) }
        .map { it.callWith(*arguments) }
        .toList()
}

/**
 * Call the functions annotated with [T]
 * and matches the given [predicate]
 * with the given [arguments].
 *
 * __Note: Suspend functions won't be run with this
 * function even if a continuation is passed as the
 * last argument. To also invoke suspending functions
 * use [runWithSuspend].__
 *
 * Only functions that can be invoked with the
 * provided arguments will be invoked.
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @param arguments the arguments.
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
inline fun <reified T : Annotation> runWith(vararg arguments: Any?, noinline predicate: (T) -> Boolean = { true }): List<Any?> {
    return runWith(T::class, *arguments) { it.findAnnotations<T>().any(predicate) }
}

/**
 * Call the functions annotated with [annotation]
 * with the given [arguments].
 *
 * __Note: Suspend functions won't be run with this
 * function even if a continuation is passed as the
 * last argument. To also invoke suspending functions
 * use [runWithSuspend].__
 *
 * Only functions that can be invoked with the
 * provided arguments will be invoked.
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @param arguments the arguments.
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
fun runWith(annotation: Annotation, vararg arguments: Any?): List<Any?> {
    return runWith(annotation::class, *arguments) { annotation in it.annotations }
}

//////////////////////////////////////////////////

/**
 * Call the functions annotated with [annotation] qualified name
 * and matches the given [predicate]
 * with the given [arguments].
 *
 * Only functions that can be invoked with the
 * provided arguments will be invoked.
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @param arguments the arguments.
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
suspend fun runWithSuspend(annotation: String, vararg arguments: Any?, predicate: (KFunction<*>) -> Boolean = { true }): List<Any?> {
    @OptIn(ExperimentalRannoApi::class)
    return elementsWith(annotation)
        .asSequence()
        .filterIsInstance<KFunction<*>>()
        .filter { it.canCallWithSuspend(*arguments) && predicate(it) }
        .mapTo(mutableListOf()) { it.callWithSuspend(*arguments) }
}

/**
 * Call the functions annotated with [annotation]
 * and matches the given [predicate]
 * with the given [arguments].
 *
 * Only functions that can be invoked with the
 * provided arguments will be invoked.
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @param arguments the arguments.
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
suspend fun runWithSuspend(annotation: KClass<out Annotation>, vararg arguments: Any?, predicate: (KFunction<*>) -> Boolean = { true }): List<Any?> {
    @OptIn(ExperimentalRannoApi::class)
    return elementsWith(annotation)
        .asSequence()
        .filterIsInstance<KFunction<*>>()
        .filter { it.canCallWithSuspend(*arguments) && predicate(it) }
        .mapTo(mutableListOf()) { it.callWithSuspend(*arguments) }
}

/**
 * Call the functions annotated with [T]
 * and matches the given [predicate]
 * with the given [arguments].
 *
 * Only functions that can be invoked with the
 * provided arguments will be invoked.
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @param arguments the arguments.
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
suspend inline fun <reified T : Annotation> runWithSuspend(vararg arguments: Any?, noinline predicate: (T) -> Boolean = { true }): List<Any?> {
    return runWithSuspend(T::class, *arguments) { it.findAnnotations<T>().any(predicate) }
}

/**
 * Call the functions annotated with [annotation]
 * with the given [arguments].
 *
 * Only functions that can be invoked with the
 * provided arguments will be invoked.
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @param arguments the arguments.
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
suspend fun runWithSuspend(annotation: Annotation, vararg arguments: Any?): List<Any?> {
    return runWithSuspend(annotation::class, *arguments) { annotation in it.annotations }
}

//////////////////////////////////////////////////

/**
 * Call the functions annotated with [annotation]
 * and matches the given [predicate]
 * with [this] as the argument.
 *
 * __Note: Suspend functions won't be run with this
 * function even if a continuation is passed as the
 * last argument. To also invoke suspending functions
 * use [applyWithSuspend].__
 *
 * Only functions that can be invoked with the
 * provided arguments will be invoked.
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @param arguments additional arguments.
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
fun Any.applyWith(annotation: String, vararg arguments: Any?, predicate: (KFunction<*>) -> Boolean = { true }): List<Any?> {
    return runWith(annotation, this, *arguments, predicate = predicate)
}

/**
 * Call the functions annotated with [annotation]
 * and matches the given [predicate]
 * with [this] as the argument.
 *
 * __Note: Suspend functions won't be run with this
 * function even if a continuation is passed as the
 * last argument. To also invoke suspending functions
 * use [applyWithSuspend].__
 *
 * Only functions that can be invoked with the
 * provided arguments will be invoked.
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @param arguments additional arguments.
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
fun Any.applyWith(annotation: KClass<out Annotation>, vararg arguments: Any?, predicate: (KFunction<*>) -> Boolean = { true }): List<Any?> {
    return runWith(annotation, this, *arguments, predicate = predicate)
}

/**
 * Call all the functions annotated with [T]
 * and matches the given [predicate]
 * with [this] as the argument.
 *
 * __Note: Suspend functions won't be run with this
 * function even if a continuation is passed as the
 * last argument. To also invoke suspending functions
 * use [applyWithSuspend].__
 *
 * Only functions that can be invoked with the
 * provided arguments will be invoked.
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @param arguments additional arguments.
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
inline fun <reified T : Annotation> Any.applyWith(vararg arguments: Any?, noinline predicate: (T) -> Boolean = { true }): List<Any?> {
    return runWith<T>(this, *arguments, predicate = predicate)
}

/**
 * Call the functions annotated with [annotation]
 * with [this] as the argument.
 *
 * __Note: Suspend functions won't be run with this
 * function even if a continuation is passed as the
 * last argument. To also invoke suspending functions
 * use [applyWithSuspend].__
 *
 * Only functions that can be invoked with the
 * provided arguments will be invoked.
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @param arguments additional arguments.
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
fun Any.applyWith(annotation: Annotation, vararg arguments: Any?): List<Any?> {
    return runWith(annotation, this, *arguments)
}

//////////////////////////////////////////////////

/**
 * Call the functions annotated with [annotation]
 * and matches the given [predicate]
 * with [this] as the argument.
 *
 * Only functions that can be invoked with the
 * provided arguments will be invoked.
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @param arguments additional arguments.
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
suspend fun Any.applyWithSuspend(annotation: String, vararg arguments: Any?, predicate: (KFunction<*>) -> Boolean = { true }): List<Any?> {
    return runWithSuspend(annotation, this, *arguments, predicate = predicate)
}

/**
 * Call the functions annotated with [annotation]
 * and matches the given [predicate]
 * with [this] as the argument.
 *
 * Only functions that can be invoked with the
 * provided arguments will be invoked.
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @param arguments additional arguments.
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
suspend fun Any.applyWithSuspend(annotation: KClass<out Annotation>, vararg arguments: Any?, predicate: (KFunction<*>) -> Boolean = { true }): List<Any?> {
    return runWithSuspend(annotation, this, *arguments, predicate = predicate)
}

/**
 * Call all the functions annotated with [T]
 * and matches the given [predicate]
 * with [this] as the argument.
 *
 * Only functions that can be invoked with the
 * provided arguments will be invoked.
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @param arguments additional arguments.
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
suspend inline fun <reified T : Annotation> Any.applyWithSuspend(vararg arguments: Any?, noinline predicate: (T) -> Boolean = { true }): List<Any?> {
    return runWithSuspend<T>(this, *arguments, predicate = predicate)
}

/**
 * Call the functions annotated with [annotation]
 * with [this] as the argument.
 *
 * Only functions that can be invoked with the
 * provided arguments will be invoked.
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @param arguments additional arguments.
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
suspend fun Any.applyWithSuspend(annotation: Annotation, vararg arguments: Any?): List<Any?> {
    return runWithSuspend(annotation, this, *arguments)
}

//////////////////////////////////////////////////

/**
 * The default configuration enumeration annotation.
 *
 * For structures without custom annotations.
 *
 * Example (Ktor) :
 *
 * ```kt
 * fun Route.routes() {
 *      applyWith<EnumeratedScript>(3) {
 *          it.domain == "com.example"
 *      }
 * }
 *
 * @EnumeratedScript(domain= "com.example")
 * fun Route.__Routes() {
 *      get { /*...*/ }
 *      post { /*...*/ }
 * }
 *
 * @EnumeratedScript(domain= "com.example")
 * fun Route.__RoutesWithArgument(number: Int) {
 * }
 * ```
 *
 * @author LSafer
 * @since 1.0.0
 */
@Enumerable
@Repeatable
@EnumerableReturnType(Unit::class)
@Target(AnnotationTarget.FUNCTION)
@Deprecated(DEPRECATION_MSG)
annotation class EnumeratedScript(
    /**
     * The enumeration qualifier.
     *
     * @since 1.0.0
     */
    val name: String = "",
    /**
     * Used to reduce conflict between multiple modules.
     *
     * @since 1.0.0
     */
    val domain: String = "",
)

//////////////////////////////////////////////////

/**
 * Return true if this callable can be call with the given [arguments].
 *
 * If the callable accepts fewer arguments,
 * the extra arguments are ignored.
 *
 * @since 1.0.0
 */
@ExperimentalRannoApi
@Deprecated(DEPRECATION_MSG)
fun KFunction<*>.canCallWith(vararg arguments: Any?): Boolean {
    return !isSuspend && canCallWithSuspend(*arguments)
}

/**
 * Call this callable with the given [arguments].
 *
 * If the callable accepts fewer arguments,
 * the extra arguments are ignored.
 *
 * @return the returned value.
 * @since 1.0.0
 */
@ExperimentalRannoApi
@Deprecated(DEPRECATION_MSG)
fun KFunction<*>.callWith(vararg arguments: Any?): Any? {
    trySetAccessibleAlternative()

    return if (parameters.size == arguments.size)
        call(*arguments)
    else
        call(*arguments.take(parameters.size).toTypedArray())
}

/**
 * Return true if this callable can be call with the given [arguments].
 *
 * If the callable accepts fewer arguments,
 * the extra arguments are ignored.
 *
 * @since 1.0.0
 */
@ExperimentalRannoApi
@Deprecated(DEPRECATION_MSG)
fun KFunction<*>.canCallWithSuspend(vararg arguments: Any?): Boolean {
    if (parameters.size > arguments.size)
        return false

    for (i in parameters.indices) {
        val parameter = parameters[i]
        val argument = arguments[i]

        if (!parameter.type.jvmErasure.isInstance(argument))
            return false
    }

    return true
}

/**
 * Call this callable with the given [arguments].
 *
 * If the callable accepts fewer arguments,
 * the extra arguments are ignored.
 *
 * @return the returned value.
 * @since 1.0.0
 */
@ExperimentalRannoApi
@Deprecated(DEPRECATION_MSG)
suspend fun KFunction<*>.callWithSuspend(vararg arguments: Any?): Any? {
    trySetAccessibleAlternative()

    return if (parameters.size == arguments.size)
        callSuspend(*arguments)
    else
        callSuspend(*arguments.take(parameters.size).toTypedArray())
}

private const val DEPRECATION_MSG = "This API was unnecessarily specific thus it was deprecated." +
        "A more manual approach should be used in applications instead."
