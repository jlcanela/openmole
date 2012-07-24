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

package org.openmole.ide.plugin.hook.display

import org.openmole.ide.misc.widget.multirow.RowWidget._
import org.openmole.ide.misc.widget.multirow.MultiWidget._
import org.openmole.ide.core.model.dataproxy.ITaskDataProxyUI
import org.openmole.ide.core.model.panel.IHookPanelUI
import org.openmole.ide.plugin.hook.tools.MultiPrototypePanelUI

class ToStringHookPanelUI(dataUI: ToStringHookDataUI,
                          taskProxy: ITaskDataProxyUI) extends MultiPrototypePanelUI("Display prototypes",
  taskProxy,
  if (dataUI.toBeHooked.isEmpty)
    taskProxy.dataUI.prototypesOut ::: taskProxy.dataUI.implicitPrototypesOut
  else dataUI.toBeHooked)
    with IHookPanelUI {

  def saveContent = new ToStringHookDataUI(dataUI.activated,
    multiPrototypeCombo.content.map { _.comboValue.get })
}