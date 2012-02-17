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

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Rectangle
import java.awt.RenderingHints
import org.netbeans.api.visual.action.WidgetAction
import org.netbeans.api.visual.action.WidgetAction._
import org.netbeans.api.visual.anchor.AnchorShapeFactory
import org.netbeans.api.visual.layout.LayoutFactory
import org.netbeans.api.visual.widget.ComponentWidget
import org.netbeans.api.visual.widget.ConnectionWidget
import org.openmole.ide.core.model.workflow.IDataChannelUI
import org.openmole.ide.core.implementation.dialog.DataChannelDialog
import org.openmole.ide.core.model.workflow.IMoleScene
import org.openmole.ide.core.model.panel.PanelMode._
import org.openmole.ide.misc.widget._
import scala.swing.Action

class DataChannelConnectionWidget(scene: IMoleScene, val dataChannelUI: IDataChannelUI) extends ConnectionWidget(scene.graphScene){
  
  setLineColor(new Color(188,188,188))
  setStroke(new BasicStroke(6, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,20.0f, List(10.0f).toArray, 0.0f))
  
  setSourceAnchorShape(AnchorShapeFactory.createImageAnchorShape(Images.IMAGE_OUTPUT_DATA_CHANNEL,false))
  setTargetAnchorShape(AnchorShapeFactory.createImageAnchorShape(Images.IMAGE_OUTPUT_DATA_CHANNEL,false))
  var labeled = false
  
  val componentWidget = new PrototypeWidget(scene,new LinkLabel(DataChannelConnectionWidget.this.dataChannelUI.prototypes.size.toString,
                                 new Action("") { def apply = edit},10))
  setConstraint(componentWidget, LayoutFactory.ConnectionWidgetLayoutAlignment.CENTER, 0.5f)
  componentWidget.setOpaque(true)
  addChild(componentWidget)
  scene.refresh
  
  def edit : Unit= {
    DataChannelDialog.display(DataChannelConnectionWidget.this)
    scene.refresh
  }
  
  class PrototypeWidget(scene: IMoleScene,val link: LinkLabel) extends ComponentWidget(scene.graphScene,link.peer) {
    link.foreground = Color.WHITE
    val dim = 30
    val pos = link.size.width / 2 + 1
    setPreferredBounds(new Rectangle(dim,dim))
    
    override def paintBackground = {
      val g = scene.graphScene.getGraphics
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                         RenderingHints.VALUE_ANTIALIAS_ON)
      g.setColor(new Color(0, 0, 0, 180))
      g.fillOval(pos,pos, dim, dim)
      link.text = DataChannelConnectionWidget.this.dataChannelUI.prototypes.size.toString
      revalidate
    }
    
    override def paintBorder = {
      val g = scene.graphScene.getGraphics
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                         RenderingHints.VALUE_ANTIALIAS_ON)
      g.setStroke(new BasicStroke(3f))
      g.setColor(new Color(200,200,200))
      g.drawOval(pos,pos, dim-2,dim-2)
      revalidate
    }
  }
  
}
