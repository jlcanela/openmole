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

package org.openmole.ide.plugin.domain.range

import org.openmole.ide.core.model.dataproxy.IPrototypeDataProxyUI
import org.openmole.plugin.domain.range.DoubleLogarithmRange
import org.openmole.core.model.domain.Domain

class DoubleLogarithmRangeDataUI(val min: String = "0.0", val max: String = "", val step: Option[String] = None) extends LogarthmicRangeDataUI[Double] {

  def availableTypes = List("Double")

  def coreObject(prototype: IPrototypeDataProxyUI): Domain[Double] = new DoubleLogarithmRange(min, max, stepString)

  def coreClass = classOf[DoubleLogarithmRangeDataUI]

  def buildPanelUI(p: IPrototypeDataProxyUI) = new LogarithmicRangePanelUI(this, p)

  override def toString = "Log Range"
}