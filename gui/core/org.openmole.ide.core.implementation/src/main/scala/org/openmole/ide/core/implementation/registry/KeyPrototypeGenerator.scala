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

package org.openmole.ide.core.implementation.registry

import org.openmole.core.model.data._
import org.openmole.ide.core.model.dataproxy.IPrototypeDataProxyUI
import org.openmole.ide.core.implementation.dataproxy.Proxys
import org.openmole.ide.core.implementation.data.EmptyDataUIs

object KeyPrototypeGenerator {

  def apply(proxy: IPrototypeDataProxyUI): PrototypeKey =
    new PrototypeKey(proxy.dataUI.name, KeyGenerator.stripArrays(proxy.dataUI.protoType)._1.runtimeClass, proxy.dataUI.dim)

  def apply(proto: Prototype[_]): PrototypeKey = {
    val (manifest, dim) = KeyGenerator.stripArrays(proto.`type`)
    new PrototypeKey(proto.name, manifest.runtimeClass, dim)
  }

  def keyPrototypeMapping: Map[PrototypeKey, IPrototypeDataProxyUI] = (Proxys.prototypes.toList :::
    List(EmptyDataUIs.emptyPrototypeProxy)).map {
      p ⇒ KeyPrototypeGenerator(p) -> p
    }.toMap

  def isPrototype(p: Prototype[_]) = keyPrototypeMapping.keys.toList.contains(KeyPrototypeGenerator(p))

  def isPrototype(p: IPrototypeDataProxyUI) = keyPrototypeMapping.keys.toList.contains(KeyPrototypeGenerator(p))
}

case class PrototypeKey(val name: String, val protoClass: Class[_], val dim: Int)
