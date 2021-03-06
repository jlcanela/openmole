/*
 * Copyright (C) 2012 Romain Reuillon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openmole.plugin.method.sensitivity

import org.openmole.core.implementation.data._
import org.openmole.core.model.data._

import SensitivityTask._
import SaltelliSampling._
import org.openmole.core.model.task.PluginSet

object RawSensitivityTask {

  abstract class Builder(implicit plugins: PluginSet) extends SensitivityTask.Builder {
    override def outputs: DataSet = super.outputs + DataSet(for (i ← modelInputs; o ← modelOutputs) yield indice(name, i, o))
  }

}

import SensitivityTask._

trait RawSensitivityTask extends SensitivityTask {

  override def process(context: Context): Context = {
    val matrixNames = context(matrixName.toArray)

    Context.empty ++
      (for (i ← modelInputs; o ← modelOutputs) yield {
        val (a, b, c) = extractValues(context.valueOrException(o.toArray), matrixNames, i)
        Variable(
          indice(name, i, o),
          computeSensitivity(a, b, c))
      })
  }

}

