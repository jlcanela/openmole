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

package org.openmole.ide.plugin.sampling.csv

import org.openmole.ide.core.implementation.registry._
import org.openmole.ide.core.implementation.builder._
import org.openmole.ide.core.model.factory.ISamplingFactoryUI
import org.openmole.core.model.sampling.Sampling
import org.openmole.ide.core.model.sampling.IBuiltCompositionSampling
import org.openmole.plugin.sampling.csv.CSVSampling
import org.openmole.ide.core.implementation.sampling.SamplingProxyUI

class Activator extends OSGiActivator with SamplingActivator {

  override def samplingFactories = List(new ISamplingFactoryUI {
    def buildDataUI = new CSVSamplingDataUI

    def fromCoreObject(sampling: Sampling, bSC: IBuiltCompositionSampling) = {
      sampling match {
        case cs: CSVSampling ⇒
          val proxy = new SamplingProxyUI(new CSVSamplingDataUI(cs.file.getCanonicalPath))
          (proxy, Builder.buildConnectedSamplings(proxy, Seq(), bSC))
        case _ ⇒ (new SamplingProxyUI(buildDataUI), bSC)
      }
    }
  })
}
