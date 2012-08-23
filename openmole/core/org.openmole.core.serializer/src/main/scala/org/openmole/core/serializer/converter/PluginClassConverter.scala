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

package org.openmole.core.serializer.converter

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.converters.extended.JavaClassConverter
import org.openmole.misc.pluginmanager.PluginManager

class PluginClassConverter[A <: { def classUsed(c: Class[_]) }](serializer: A) extends JavaClassConverter(classOf[XStream].getClassLoader) {

  override def toString(obj: Object) = {
    val c = obj.asInstanceOf[Class[_]]
    if (PluginManager.isClassProvidedByAPlugin(c)) serializer.classUsed(c)
    super.toString(obj)
  }

}
