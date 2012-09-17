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

package org.openmole.ide.core.implementation.workflow

import org.openmole.ide.core.model.workflow.IMoleScene
import org.openmole.ide.core.model.dataproxy.ITaskDataProxyUI
import javax.imageio.ImageIO
import org.openmole.ide.core.model.commons.Constants._
import org.openmole.ide.core.model.workflow._
import java.awt.Color
import java.awt.BasicStroke
import java.awt.BorderLayout
import java.awt.Graphics2D
import java.awt.Dimension
import java.awt.LinearGradientPaint
import java.awt.Point
import java.awt.Rectangle
import java.awt.RenderingHints
import org.openmole.ide.core.model.panel.PanelMode._
import scala.swing.Panel

class TaskWidget(scene: IMoleScene,
                 val capsule: ICapsuleUI) extends Panel {
  peer.setLayout(new BorderLayout)
  preferredSize = new Dimension(TASK_CONTAINER_WIDTH, TASK_CONTAINER_HEIGHT)

  override def paint(g: Graphics2D) = {
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON)

    g.setPaint(backColor)
    // g.setColor(backColor)
    g.fillRoundRect(0, 0, preferredSize.width, preferredSize.height, 5, 5)
    g.setColor(borderColor)
    g.setStroke(new BasicStroke(5))
    g.draw(new Rectangle(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1))
    g.fillRect(0, 0, preferredSize.width, TASK_TITLE_HEIGHT)

    capsule.dataUI.task match {
      case Some(x: ITaskDataProxyUI) ⇒
        g.drawImage(ImageIO.read(x.dataUI.getClass.getClassLoader.getResource(x.dataUI.fatImagePath)), 10, 30, 80, 80, peer)
      case None ⇒
    }
  }

  def backColor = {

    val start = new Point(0, 0)
    val end = new Point(0, preferredSize.height)
    val dist = Array(0.0f, preferredSize.height * 0.5f, preferredSize.height * 0.8f)
    capsule.dataUI.task match {
      case Some(x: ITaskDataProxyUI) ⇒
        scene match {
          case y: BuildMoleScene ⇒

            new LinearGradientPaint(start, end, dist,
              Array(new Color(215, 238, 244),
                new Color(228, 228, 228),
                new Color(215, 238, 244)))

          //new Color(215, 238, 244)
          case _ ⇒
            new LinearGradientPaint(start, end, dist,
              Array(new Color(215, 238, 244, 64),
                new Color(228, 228, 228),
                new Color(215, 238, 244, 64)))
          //new Color(215, 238, 244, 64)
        }
      case _ ⇒
        new Color(215, 238, 244)
    }
  }

  def borderColor: Color = {
    if (capsule.selected) new Color(222, 135, 135)
    else {
      capsule.dataUI.task match {
        case Some(x: ITaskDataProxyUI) ⇒
          scene match {
            case y: BuildMoleScene ⇒ new Color(73, 90, 105)
            case _ ⇒ new Color(44, 137, 160, 64)
          }
        case _ ⇒ new Color(73, 90, 105)
      }
    }
  }
}
