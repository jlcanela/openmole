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

package org.openmole.ide.misc.widget.example

import java.awt.Color
import java.awt.Dimension
import org.openmole.ide.misc.widget.MigPanel
import org.openmole.ide.misc.widget.PropertyPanel
import org.openmole.ide.misc.widget.TransparentPanel
import scala.swing.Label
import scala.swing.MainFrame
import scala.swing.SimpleSwingApplication

object PropertyPanelExample extends SimpleSwingApplication
{
  def top = new MainFrame {
    title = "Link Label Demo"
    contents = new PropertyPanel("wrap"){
      contents +=new Label("first"){foreground = Color.white}
      contents += new TransparentPanel("wrap"){contents += new Label("second")}
    }
    size = new Dimension(250,200)
  }
}