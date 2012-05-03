/*
 * Copyright (C) 2012 mathieu
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

package org.openmole.ide.misc.tools.check.example

import org.openmole.core.implementation.data.Prototype
import org.openmole.ide.misc.tools.check.TypeCheck

object TypeCheckExample extends App {

  println(TypeCheck.apply("12.5", new Prototype[Int]("proto1")))
  println(TypeCheck.apply("125", new Prototype[Int]("proto1")))
  println(TypeCheck.apply("125", new Prototype[String]("proto1")))
  println(TypeCheck.apply("\"125\"", new Prototype[String]("proto1")))
  println(TypeCheck.apply("12.5d", new Prototype[Double]("proto1")))

}
