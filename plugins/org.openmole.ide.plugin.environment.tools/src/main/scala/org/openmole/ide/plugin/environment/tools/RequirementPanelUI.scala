/*
 * Copyright (C) 2011 <mathieu.Mathieu Leclaire at openmole.org>
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

package org.openmole.ide.plugin.environment.tools

import java.util.Locale
import java.util.ResourceBundle
import org.openmole.ide.misc.widget._
import scala.swing.CheckBox
import scala.swing.Label
import scala.swing.TabbedPane
import scala.swing.TextField

class RequirementPanelUI(val requirementDataUI: RequirementDataUI = new RequirementDataUI)
    extends TabbedPane.Page("Requirements", new Label) {

  val i18n = ResourceBundle.getBundle("help", new Locale("en", "EN"))

  val architectureCheckBox = new CheckBox("64 bits")

  val workerNodeMemoryLabel = new Label("Worker memory")
  val workerNodeMemoryTextField = new TextField(4)
  val maxCPUTimeLabel = new Label("Max CPU Time")
  val maxCPUTimeTextField = new TextField(4)
  val otherRequirementLabel = new Label("Other")
  val otherRequirementTextField = new TextField(16)

  this.content = new PluginPanel("wrap 2") {
    contents += (architectureCheckBox, "wrap")
    contents += (workerNodeMemoryLabel, "gap para")
    contents += workerNodeMemoryTextField
    contents += (maxCPUTimeLabel, "gap para")
    contents += maxCPUTimeTextField
    contents += (otherRequirementLabel, "gap para")
    contents += otherRequirementTextField
  }

  architectureCheckBox.selected = requirementDataUI.architecture64
  workerNodeMemoryTextField.text = requirementDataUI.workerNodeMemory
  maxCPUTimeTextField.text = requirementDataUI.maxCPUTime
  otherRequirementTextField.text = requirementDataUI.otherRequirements

  override def helpMap = Map(architectureCheckBox -> (i18n.getString("architecture"), i18n.getString("architectureEx")),
    workerNodeMemoryTextField -> (i18n.getString("workerNodeMemory"), i18n.getString("workerNodeMemoryEx")),
    maxCPUTimeTextField -> (i18n.getString("maxCPUTime"), i18n.getString("maxCPUTimeEx")),
    otherRequirementTextField -> (i18n.getString("otherRequirement"), i18n.getString("otherRequirementEx")))

  def save = new RequirementDataUI(
    architectureCheckBox.selected,
    workerNodeMemoryTextField.text,
    maxCPUTimeTextField.text,
    otherRequirementTextField.text)
}
