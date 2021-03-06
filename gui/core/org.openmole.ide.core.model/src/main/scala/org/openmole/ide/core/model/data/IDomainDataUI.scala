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

package org.openmole.ide.core.model.data

import org.openmole.core.model.domain._
import org.openmole.ide.core.model.panel.IDomainPanelUI
import org.openmole.ide.misc.tools.util.Types._
import org.openmole.ide.misc.tools.util.Types

object IDomainDataUI {
  implicit val ordering = Ordering.by((_: IDomainDataUI).name)
}

trait IDomainDataUI extends IDataUI {

  def domainType: Manifest[_]

  def coreObject: Domain[_]

  def buildPanelUI: IDomainPanelUI

  def preview: String

  def isAcceptable(domain: IDomainDataUI): Boolean = false

  def availableTypes: List[String] = List(INT, DOUBLE, BIG_DECIMAL, BIG_INTEGER, LONG).map(Types.pretify)
}