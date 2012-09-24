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

package org.openmole.ide.core.model.panel

import org.openmole.ide.core.model.sampling._
import org.openmole.ide.core.model.data._
import org.netbeans.api.visual.widget.Scene
import java.awt.Point

trait ISamplingCompositionPanelUI extends IPanelUI {
  def saveContent(name: String): ISamplingCompositionDataUI

  def scene: Scene

  def addFactor(factorDataUI: IFactorDataUI,
                location: Point,
                display: Boolean = true)

  def addSampling(samplingDataUI: ISamplingDataUI,
                  location: Point,
                  display: Boolean = true)

  def removeFactor(factorWidget: IFactorWidget)

  def setFinalSampling(samplingWidget: ISamplingWidget)

  def setSamplingWidget(id: Option[String], b: Boolean): Unit

  def setSamplingWidget(samplingWidget: ISamplingWidget, b: Boolean): Unit

  def removeSampling(samplingWidget: ISamplingWidget)
}
