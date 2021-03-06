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

package org.openmole.plugin.domain.range

import org.openmole.core.model.data._
import org.openmole.core.model.domain._
import java.math.MathContext
import java.math.RoundingMode
import java.util.logging.Logger
import org.openmole.core.implementation.tools._
import org.openmole.misc.tools.math.BigDecimalOperations._
import java.math.BigDecimal

object BigDecimalLogarithmRange {
  val scale = 128
}

sealed class BigDecimalLogarithmRange(val min: String, val max: String, val nbStep: String) extends Domain[BigDecimal] with Finite[BigDecimal] with Bounds[BigDecimal] {
  import BigDecimalLogarithmRange._

  override def computeValues(context: Context): Iterable[BigDecimal] = {
    val minValue = min(context)
    val mi = ln(minValue, scale)

    val maxValue = max(context)
    val ma = ln(maxValue, scale)

    val retScale = math.max(minValue.scale, maxValue.scale)

    val nbst = nbStep(context).intValue - 1
    val step = if (nbst > 0) ma.subtract(mi).abs.divide(new BigDecimal(nbst), BigDecimal.ROUND_HALF_UP)
    else BigDecimal.ZERO

    var cur = mi
    val mc = new MathContext(retScale, RoundingMode.HALF_UP)
    //Logger.getLogger(getClass.getName).info(" " + minValue + " " + retScale)

    for (i ← 0 to nbst) yield {
      val ret = cur
      cur = cur.add(step)
      exp(ret, scale).setScale(retScale, RoundingMode.HALF_UP).round(mc)
    }
  }

  def nbStep(context: Context): BigDecimal = new BigDecimal(VariableExpansion(context, nbStep))
  def min(context: Context): BigDecimal = new BigDecimal(VariableExpansion(context, min))
  def max(context: Context): BigDecimal = new BigDecimal(VariableExpansion(context, max))
}
