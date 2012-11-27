package org.openmole.ide.plugin.sampling.combine

import org.openmole.ide.core.model.data.IFactorDataUI
import org.openmole.ide.core.model.dataproxy.IPrototypeDataProxyUI
import org.openmole.core.model.sampling.{ Factor, DiscreteFactor }
import org.openmole.core.model.data.Prototype
import org.openmole.misc.exception.UserBadDataError
import org.openmole.core.model.domain.{ Discrete, Domain }

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
object CombineSamplingCoreFactory {

  def apply(dataUIList: List[IFactorDataUI]) =
    dataUIList.map(f ⇒ f.prototype match {
      case Some(p: IPrototypeDataProxyUI) ⇒
        DiscreteFactor(Factor(p.dataUI.coreObject.asInstanceOf[Prototype[Any]],
          f.domain.dataUI.coreObject.asInstanceOf[Domain[Any] with Discrete[Any]]))
      case _ ⇒ throw new UserBadDataError("No Prototype is define for the domain " + f.domain.dataUI.preview)
    })

}
