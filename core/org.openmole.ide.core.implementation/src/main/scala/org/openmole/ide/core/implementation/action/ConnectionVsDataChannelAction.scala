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

package org.openmole.ide.core.implementation.action

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.AbstractButton
import javax.swing.ImageIcon
import org.openmole.ide.core.implementation.execution.ScenesManager
import org.openmole.ide.misc.tools.image.Images._

class ConnectionVsDataChannelAction extends ActionListener{
  override def actionPerformed(ae: ActionEvent)= {
    val button = ae.getSource.asInstanceOf[AbstractButton]
    ScenesManager.connectMode = button.isSelected
    ScenesManager.connectMode match {
      case true=> button.setIcon(new ImageIcon(CONNECT_TRANSITION_MODE))
      case false=> button.setIcon(new ImageIcon(DATA_CHANNEL_TRANSITION_MODE))
    }
    button.revalidate
  }
}