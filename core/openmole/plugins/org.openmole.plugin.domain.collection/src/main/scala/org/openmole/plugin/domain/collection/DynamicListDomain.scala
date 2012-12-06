/*
 * Copyright (C) 2010 Romain Reuillon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openmole.plugin.domain.collection

import org.openmole.core.implementation.tools.VariableExpansion
import org.openmole.core.model.data._
import scala.collection.mutable.ArrayBuffer
import org.openmole.core.model.domain._
import scala.collection.JavaConversions
import scala.collection.mutable.ListBuffer
import org.openmole.misc.tools.io.FromString

sealed class DynamicListDomain[+T](values: String*)(implicit s: FromString[T]) extends Domain[T] with Finite[T] {

  override def computeValues(context: Context): Iterable[T] =
    values.map { p ⇒ s.fromString(VariableExpansion(context, p)) }

}
