/*
 * Copyright (C) 2011 <mathieu.Mathieu Leclaire at openmole.org>
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

package org.openmole.ide.core.model.panel

import java.awt.Color
import javax.swing.JPanel
import org.openmole.ide.misc.widget.Helper
import scala.swing.Alignment
import scala.swing.TabbedPane

trait IPanelUI {
  def peer: JPanel

  val tabbedPane = new TabbedPane {
    tabPlacement(Alignment.Left)
    opaque = true
    background = new Color(77, 77, 77)
  }

  def help: Helper = new Helper
}