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

package org.openmole.ide.core.implementation.panel

import org.openide.util.ImageUtilities
import org.openmole.ide.misc.widget.PopupMenu
import scala.swing.Component
import scala.swing.Menu
import scala.swing.Button
import scala.swing.MenuItem
import scala.swing.event.ButtonClicked
import java.awt.Color

class PopupToolBarPresenter(t: String, basemenu: Menu) extends Button(t){
  val popup = new PopupMenu {contents += basemenu}
  
  icon = ImageUtilities.loadImageIcon("org/openide/awt/resources/arrow.png", true)
  background = new Color(204,204,204,128)
  listenTo(mouse.clicks)
  reactions += {
    case x:ButtonClicked => popup.show(this, 0, size.height)
  }
    
  def remove(c: Component) = c match {
    case x: Menu =>
    case x: MenuItem => popup.contents -= c
  }
    
  def removeAll = {
    popup.peer.removeAll
    popup.contents += basemenu
  }
  
}