/*
 * Copyright (C) 2011 Mathieu Mathieu Leclaire <mathieu.Mathieu Leclaire at openmole.org>
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
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openmole.ide.plugin.task.serialization

import org.openmole.ide.core.implementation.panel.ComponentCategories
import org.openmole.ide.core.model.factory.ITaskFactoryUI
import org.openmole.core.model.task.ITask
import org.openmole.ide.core.model.builder.IPuzzleUIMap
import org.openmole.ide.core.implementation.builder.SceneFactory
import org.openmole.plugin.task.serialization.StoreIntoCSVTask
import org.openmole.ide.core.implementation.prototype.GenericPrototypeDataUI
import org.openmole.core.model.data.Prototype

class StoreIntoCSVTaskFactoryUI extends ITaskFactoryUI {
  override def toString = "Merge prototypes in file"

  def buildDataUI = new StoreIntoCSVTaskDataUI

  def category = ComponentCategories.STORAGE_TASK

  def buildDataProxyUI(task: ITask, uiMap: IPuzzleUIMap) = {
    val t = SceneFactory.as[StoreIntoCSVTask](task)
    uiMap.task(task, x ⇒ new StoreIntoCSVTaskDataUI(t.name,
      t.columns.toList.map { e ⇒ (uiMap.prototype(e._1.asInstanceOf[Prototype[Array[Any]]]), e._2) },
      Some(uiMap.prototype(t.filePrototype))))
  }
}