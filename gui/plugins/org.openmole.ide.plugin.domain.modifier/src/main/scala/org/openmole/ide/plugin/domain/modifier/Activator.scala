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

package org.openmole.ide.plugin.domain.modifier

import org.openmole.ide.core.implementation.registry.OSGiActivator
import org.openmole.ide.core.implementation.registry.DomainActivator
import org.openmole.ide.misc.tools.util.Types._
import org.openmole.ide.core.model.factory.IDomainFactoryUI
import org.openmole.core.model.domain.Domain

class Activator extends OSGiActivator with DomainActivator {

  override def domainFactories = List(
    new IDomainFactoryUI {
      def buildDataUI = new TakeDomainDataUI

      def fromCoreObject(d: Domain[_]) = ???
    },
    new IDomainFactoryUI { def buildDataUI = GroupDomainDataUI.empty },
    new IDomainFactoryUI { def buildDataUI = new MapDomainDataUI },
    new IDomainFactoryUI { def buildDataUI = SlidingDomainDataUI.empty },
    new IDomainFactoryUI { def buildDataUI = SortDomainDataUI.empty },
    new IDomainFactoryUI { def buildDataUI = new SortByNameDomainDataUI })
}
