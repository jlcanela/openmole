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

package org.openmole.plugin.domain.relative

import org.openmole.core.model.data.IContext
import org.openmole.core.implementation.tools._
import java.lang.Integer

sealed class IntegerRelative(val nominal: String, val percent: String, val size: String) extends IRelative[Integer] {

  override def computeValues(context: IContext): Iterable[Integer] = {
    val nom = VariableExpansion(context, nominal).toInt
    val pe = VariableExpansion(context, percent).toInt
    val s = VariableExpansion(context, size).toInt

    val min = nom * (1 - pe / 100.)
    if (s > 1) {
      val step = 2 * nom * pe / 100. / (s - 1)
      for (i ← 0 to s) yield new Integer(BigDecimal(min + i * s).toInt)
    } else {
      List(min, nom, nom * (1 + pe / 100.)).map { e ⇒ new Integer(BigDecimal(e).toInt) }
    }
  }
}
