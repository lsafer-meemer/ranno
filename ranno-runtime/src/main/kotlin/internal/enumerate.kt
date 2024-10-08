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
import java.util.logging.Logger
import kotlin.reflect.KAnnotatedElement

private val logger = Logger.getLogger("org.cufy.ranno")

private val ANNOTATION_QN = Enumerable::class.qualifiedName!!

private val elementCache = mutableMapOf<String, List<KAnnotatedElement>>()

internal fun enumerateElementsWith(annotation: String): List<KAnnotatedElement> {
    return elementCache.getOrPut(annotation) {
        listResourceLocations("$ANNOTATION_QN/$annotation")
            .asSequence() // -> list of directories uris
            .flatMap { it.listUrisOrEmpty() } // -> list of files uris
            .distinct()
            .flatMap { it.readLinesOrEmpty() } // -> list of lines
            .mapNotNull {
                lookupElement(it) ?: run {
                    logger.warning { "Element not found at runtime: $it" }
                    null
                }
            }
            .distinct()
            .filter {
                it.annotations.any {
                    it.annotationClass.qualifiedName == annotation
                }
            }
            .toList()
    }
}
