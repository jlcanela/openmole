/*
 * Copyright (C) 2012 reuillon
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

package org.openmole.core.implementation.task

import org.openmole.core.implementation.data._
import org.openmole.core.model.data._
import org.openmole.core.model.task._

object RenameTask {

  def apply[T](name: String, source: Prototype[T], destination: Prototype[T])(implicit plugins: PluginSet = PluginSet.empty) =
    new TaskBuilder { builder ⇒
      addInput(source)
      addOutput(destination)

      def toTask =
        new RenameTask(name, source, destination) {
          val inputs = builder.inputs
          val outputs = builder.outputs
          val parameters = builder.parameters
        }
    }

}
sealed abstract class RenameTask[T](val name: String, val source: Prototype[T], val destination: Prototype[T])(implicit val plugins: PluginSet) extends Task {
  override def process(context: Context) = Variable(destination, context.valueOrException(source))
}