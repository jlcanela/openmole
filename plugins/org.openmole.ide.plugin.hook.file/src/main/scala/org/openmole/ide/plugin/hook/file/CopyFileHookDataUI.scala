/*
 * Copyright (C) 2011 leclaire
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

package org.openmole.ide.plugin.hook.file

import java.io.File
import org.openmole.core.model.data.IPrototype
import org.openmole.core.model.mole.ICapsule
import org.openmole.core.model.mole.IMoleExecution
import org.openmole.ide.core.model.control.IExecutionManager
import org.openmole.ide.core.model.data.IHookDataUI
import org.openmole.ide.core.model.dataproxy.IPrototypeDataProxyUI
import org.openmole.ide.core.model.dataproxy.ITaskDataProxyUI
import org.openmole.plugin.hook.file.CopyFileHook

class CopyFileHookDataUI(var activated: Boolean = true,
                         val toBeHooked: List[(IPrototypeDataProxyUI, String)] = List.empty) extends IHookDataUI {

  override def coreObject(executionManager: IExecutionManager,
                          moleExecution: IMoleExecution,
                          capsule: ICapsule) = toBeHooked.map { h ⇒
    new CopyFileHook(moleExecution,
      capsule,
      executionManager.prototypeMapping(h._1).asInstanceOf[IPrototype[File]],
      h._2)
  }

  def buildPanelUI(task: ITaskDataProxyUI) = new CopyFileHookPanelUI(task, this)
}
