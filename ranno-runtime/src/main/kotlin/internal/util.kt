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
package org.cufy.ranno.internal

import org.cufy.ranno.Enumerable
import java.lang.reflect.AccessibleObject
import java.lang.reflect.Field
import java.lang.reflect.Method
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.javaMethod
import java.lang.reflect.Array as java_lang_reflect_Array

/**
 * Assuming `this` class is a file class (e.g. UtilsKt)
 * find all the methods in it.
 *
 * A workaround for toplevel reflection
 */
internal val KClass<*>.jvmMethods: Sequence<Method>
    get() = java.let {
        sequenceOf(it.methods, it.declaredMethods)
            .flatMap { it.asSequence() }.distinct()
    }

/**
 * Assuming `this` class is a file class (e.g. UtilsKt)
 * find all the fields in it.
 *
 * A workaround for toplevel reflection
 */
internal val KClass<*>.jvmFields: Sequence<Field>
    get() = java.let {
        sequenceOf(it.fields, it.declaredFields)
            .flatMap { it.asSequence() }.distinct()
    }

/**
 * A type-safe function to obtain the `qualifiedName` of
 * this class or throw a descriptive error on failure.
 */
internal val KClass<*>.qualifiedNameOrThrow: String
    get() = qualifiedName ?: error("Cannot get qualified name of $this")

/**
 * For a class `A` return the array variant of it `A[]`.
 */
internal fun KClass<*>.asArrayClass(): KClass<*> {
    return java_lang_reflect_Array.newInstance(java, 0)::class
}

/**
 * Decode the jvm parameters signature [classnames] into a list of k-classes.
 *
 * Example:
 * ```
 * ILjava.lang.String;Z -> listOf(Int::class, String::class, Boolean::class)
 * ```
 */
internal fun decodeClassnames(classnames: String): List<KClass<*>>? {
    var i = 0

    fun hasNext(): Boolean {
        return i < classnames.length
    }

    fun decodeNext(): KClass<*>? {
        return when (classnames[i++]) {
            'Z' -> Boolean::class
            'B' -> Byte::class
            'C' -> Char::class
            'D' -> Double::class
            'F' -> Float::class
            'I' -> Int::class
            'J' -> Long::class
            'S' -> Short::class
            '[' -> decodeNext()?.asArrayClass()
            'L' -> {
                val terminal = classnames.indexOf(';', startIndex = i)
                val classname = classnames.substring(i, terminal)
                i = terminal + 1
                lookupClass(classname.replace("/", "."))
                    ?: return null
            }

            else -> error("Malformed parameters signature $classnames")
        }
    }

    return buildList {
        while (hasNext()) {
            add(decodeNext() ?: return null)
        }
    }
}

internal fun KFunction<*>.trySetAccessibleAlternative(): Boolean {
    if (this is ToplevelKFunction<*>)
        return method.trySetAccessibleAlternative()

    return javaMethod?.trySetAccessibleAlternative() == true
}

internal fun AccessibleObject.trySetAccessibleAlternative(): Boolean {
    @Suppress("DEPRECATION")
    if (isAccessible)
        return true

    try {
        isAccessible = true
        return true
    } catch (_: SecurityException) {
    } catch (e: Throwable) {
        if (e.javaClass.name !=
            "java.lang.reflect.InaccessibleObjectException"
        ) throw e
    }

    return false
}

/**
 * Ensure [annotation] is annotated with [Enumerable]. Or throw if not.
 */
internal fun requireEnumerable(annotation: KClass<out Annotation>) {
    require(annotation.hasAnnotation<Enumerable>()) {
        "Annotation must be annotated with @Enumerable for annotatedElements() to work"
    }
}
