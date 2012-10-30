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

package org.openmole.ide.plugin.task.stat

import java.util.Locale
import java.util.ResourceBundle
import org.openmole.ide.core.implementation.dataproxy.Proxys
import org.openmole.ide.core.implementation.dialog.StatusBar
import org.openmole.ide.core.model.dataproxy.IPrototypeDataProxyUI
import org.openmole.ide.core.model.panel.ITaskPanelUI
import org.openmole.ide.misc.widget.Help
import org.openmole.ide.misc.widget.Helper
import org.openmole.ide.misc.widget.PluginPanel
import org.openmole.ide.misc.widget.multirow.MultiTwoCombos
import org.openmole.ide.misc.widget.multirow.MultiTwoCombos._
import org.openmole.ide.misc.widget.multirow.RowWidget._
import org.openmole.ide.misc.widget.multirow.MultiWidget._
import scala.swing.Label
import scala.swing.TabbedPane

abstract class BasicStatPanelUI(statType: String,
                                sequences: List[(IPrototypeDataProxyUI, IPrototypeDataProxyUI)]) extends PluginPanel("wrap 2") with ITaskPanelUI {

  val i18n = ResourceBundle.getBundle("help", new Locale("en", "EN"))
  val arrayDoublePrototypes = Proxys.classPrototypes(classOf[Array[Double]])
  val doublePrototypes = Proxys.classPrototypes(classOf[Double])

  if (arrayDoublePrototypes.isEmpty)
    StatusBar.inform("At least 1 Array of Prototype (Double) has to be created before using a" + statType + "  Tasks")

  if (doublePrototypes.isEmpty)
    StatusBar.inform("At least 1 Prototype (Double) have to be created before using a" + statType + "  Tasks")

  val multiPrototypeCombo: Option[MultiTwoCombos[IPrototypeDataProxyUI, IPrototypeDataProxyUI]] =
    if (!arrayDoublePrototypes.isEmpty && !doublePrototypes.isEmpty) {
      Some(new MultiTwoCombos("Prototypes",
        arrayDoublePrototypes,
        doublePrototypes,
        "to " + statType,
        sequences.map { s ⇒
          new TwoCombosPanel(arrayDoublePrototypes,
            doublePrototypes,
            "to " + statType,
            new TwoCombosData(Some(s._1), Some(s._2)))
        },
        NO_EMPTY,
        ADD))
    } else None

  if (multiPrototypeCombo.isDefined)
    tabbedPane.pages += new TabbedPane.Page("Settings", new PluginPanel("") { add(multiPrototypeCombo.get.panel, "gap bottom 40") })
  else
    tabbedPane.pages += new TabbedPane.Page("Settings", new PluginPanel("") { add(new Label("At least 2 Prototypes (a Double and an array of Double have to be created first.)"), "gap bottom 40") })

  override val help = new Helper {
    multiPrototypeCombo match {
      case Some(x: MultiTwoCombos[IPrototypeDataProxyUI, IPrototypeDataProxyUI]) ⇒ add(x,
        new Help(i18n.getString("prototype")))
      case _ ⇒
    }
  }
}