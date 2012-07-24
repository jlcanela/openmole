/*
 * Copyright (C) 2011 <mathieu.leclaire at openmole.org>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openmole.ide.plugin.hook.file

import org.openmole.ide.misc.widget.multirow.RowWidget._
import org.openmole.ide.misc.widget.multirow.MultiWidget._
import org.openmole.ide.core.implementation.dataproxy.Proxys
import org.openmole.ide.core.model.dataproxy.ITaskDataProxyUI
import org.openmole.ide.core.model.panel.IHookPanelUI
import org.openmole.ide.misc.widget.PluginPanel
import org.openmole.ide.misc.widget.multirow.MultiComboTextField
import org.openmole.ide.misc.widget.multirow.MultiComboTextField._
import java.io.File
import scala.swing.Label

class CopyFileHookPanelUI(taskProxy: ITaskDataProxyUI,
                          dataUI: CopyFileHookDataUI) extends PluginPanel("wrap") with IHookPanelUI {

  val multiComboTextField = new MultiComboTextField("",
    comboContent,
    dataUI.toBeHooked.map { d ⇒
      new ComboTextFieldPanel(comboContent,
        new ComboTextFieldData(Some(d._1),
          d._2))
    })

  contents += new Label("<html><b>Files to be dumped</b></html>")
  contents += multiComboTextField.panel

  def comboContent = Proxys.classPrototypes(classOf[File],
    taskProxy.dataUI.prototypesOut ::: taskProxy.dataUI.implicitPrototypesOut)

  def saveContent = new CopyFileHookDataUI(dataUI.activated,
    multiComboTextField.content.map { m ⇒ (m.comboValue.get, m.textFieldValue) })
}