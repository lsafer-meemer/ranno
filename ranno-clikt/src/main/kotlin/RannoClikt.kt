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
package org.cufy.ranno.clikt

import com.github.ajalt.clikt.core.CliktCommand
import org.cufy.ranno.Enumerable
import org.cufy.ranno.EnumerableSuperType
import org.cufy.ranno.classesWith
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotations

//////////////////////////////////////////////////

/**
 * Return all the commands annotated with [annotation] qualified name.
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
fun commandsWith(annotation: String, predicate: (KClass<*>) -> Boolean = { true }): List<CliktCommand> {
    return classesWith(annotation).mapMaybeCommand(predicate)
}

/**
 * Return all the commands annotated with [annotation].
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
fun commandsWith(annotation: KClass<out Annotation>, predicate: (KClass<*>) -> Boolean = { true }): List<CliktCommand> {
    return classesWith(annotation).mapMaybeCommand(predicate)
}

/**
 * Return all the commands annotated with [T].
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
inline fun <reified T : Annotation> commandsWith(noinline predicate: (T) -> Boolean = { true }): List<CliktCommand> {
    return commandsWith(T::class) { it.findAnnotations<T>().any(predicate) }
}

/**
 * Return all the commands annotated with [annotation].
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
fun commandsWith(annotation: Annotation): List<CliktCommand> {
    return classesWith(annotation).mapMaybeCommand()
}

//////////////////////////////////////////////////

/**
 * Run the command (single) annotated with [annotation] (see [CliktCommand.main])
 * and matches the given [predicate]
 * with the given [arguments].
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @param arguments the arguments.
 * @throws IllegalArgumentException if zero or more than one command was found.
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
fun runCommandWith(annotation: String, vararg arguments: String, predicate: (CliktCommand) -> Boolean = { true }) {
    commandsWith(annotation)
        .single { predicate(it) }
        .main(arguments.asList())
}

/**
 * Run the command (single) annotated with [annotation] (see [CliktCommand.main])
 * and matches the given [predicate]
 * with the given [arguments].
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @param arguments the arguments.
 * @throws IllegalArgumentException if zero or more than one command was found.
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
fun runCommandWith(annotation: KClass<out Annotation>, vararg arguments: String, predicate: (CliktCommand) -> Boolean = { true }) {
    commandsWith(annotation)
        .single { predicate(it) }
        .main(arguments.asList())
}

/**
 * Run the command (single) annotated with [T] (see [CliktCommand.main])
 * and matches the given [predicate]
 * with the given [arguments].
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @param arguments the arguments.
 * @throws IllegalArgumentException if zero or more than one command was found.
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
inline fun <reified T : Annotation> runCommandWith(vararg arguments: String, crossinline predicate: (T) -> Boolean = { true }) {
    runCommandWith(T::class, *arguments) { it::class.findAnnotations<T>().any(predicate) }
}

/**
 * Run the command (single) annotated with [annotation] (see [CliktCommand.main])
 * with the given [arguments].
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @param arguments the arguments.
 * @throws IllegalArgumentException if zero or more than one command was found.
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
fun runCommandWith(annotation: Annotation, vararg arguments: String) {
    commandsWith(annotation)
        .single()
        .main(arguments.asList())
}

//////////////////////////////////////////////////

/**
 * Run the commands annotated with [annotation] (see [CliktCommand.main])
 * and matches the given [predicate]
 * with the given [arguments].
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @param arguments the arguments.
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
fun runCommandsWith(annotation: String, vararg arguments: String, predicate: (CliktCommand) -> Boolean = { true }) {
    commandsWith(annotation)
        .filter { predicate(it) }
        .forEach { it.main(arguments.asList()) }
}

/**
 * Run the commands annotated with [annotation] (see [CliktCommand.main])
 * and matches the given [predicate]
 * with the given [arguments].
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @param arguments the arguments.
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
fun runCommandsWith(annotation: KClass<out Annotation>, vararg arguments: String, predicate: (CliktCommand) -> Boolean = { true }) {
    commandsWith(annotation)
        .filter { predicate(it) }
        .forEach { it.main(arguments.asList()) }
}

/**
 * Run the commands annotated with [T] (see [CliktCommand.main])
 * and matches the given [predicate]
 * with the given [arguments].
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @param arguments the arguments.
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
inline fun <reified T : Annotation> runCommandsWith(vararg arguments: String, crossinline predicate: (T) -> Boolean = { true }) {
    runCommandsWith(T::class, *arguments) { it::class.findAnnotations<T>().any(predicate) }
}

/**
 * Run the commands annotated with [annotation] (see [CliktCommand.main])
 * with the given [arguments].
 *
 * ___Note: Only the elements passed to ranno
 * annotation processor will be returned by this
 * function.___
 *
 * @param arguments the arguments.
 * @since 1.0.0
 */
@Deprecated(DEPRECATION_MSG)
fun runCommandsWith(annotation: Annotation, vararg arguments: String) {
    commandsWith(annotation)
        .forEach { it.main(arguments.asList()) }
}

//////////////////////////////////////////////////

/**
 * The default clikt enumeration annotation.
 *
 * For structures without custom annotations.
 *
 * @author LSafer
 * @since 1.0.0
 */
@Enumerable
@Repeatable
@EnumerableSuperType(CliktCommand::class)
@Target(AnnotationTarget.CLASS)
@Deprecated(DEPRECATION_MSG)
annotation class EnumeratedCommand(
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
    val domain: String = ""
)

//////////////////////////////////////////////////

private inline fun Iterable<KClass<*>>.mapMaybeCommand(
    crossinline predicate: (KClass<*>) -> Boolean = { true }
): List<CliktCommand> {
    return asSequence()
        .filter { predicate(it) }
        .map { it.objectInstance ?: it.createInstance() }
        .filterIsInstance<CliktCommand>()
        .toList()
}

//////////////////////////////////////////////////

private const val DEPRECATION_MSG = "This API was unnecessarily specific thus it was deprecated." +
        "A more manual approach should be used in applications instead."
