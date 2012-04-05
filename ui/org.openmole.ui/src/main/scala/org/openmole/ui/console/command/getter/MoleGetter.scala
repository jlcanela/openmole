/*
 * Copyright (C) 2011 reuillon
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

package org.openmole.ui.console.command.getter

import org.openmole.core.model.mole.IMole
import org.openmole.ui.console.Console

class MoleGetter extends IGetter {
  override def get(variableName: String, obj: Object, args: Array[String]) = {
    val mole = obj.asInstanceOf[IMole]
    val i = args(0).toInt
    Console.setVariable(variableName, mole.capsules(i))
  }
}