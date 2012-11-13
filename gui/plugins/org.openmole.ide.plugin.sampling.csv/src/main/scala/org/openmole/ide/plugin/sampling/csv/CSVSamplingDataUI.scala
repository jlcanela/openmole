/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openmole.ide.plugin.sampling.csv

import java.io.File
import org.openmole.misc.exception.UserBadDataError
import org.openmole.ide.core.model.dataproxy._
import org.openmole.ide.core.model.data._
import org.openmole.plugin.sampling.csv.CSVSampling
import org.openmole.ide.core.implementation.data.EmptyDataUIs._
import org.openmole.ide.core.implementation.sampling.InputSampling
import org.openmole.ide.misc.tools.Counter
import org.openmole.core.model.sampling.Sampling

class CSVSamplingDataUI(var csvFilePath: String = "",
                        var prototypeMapping: List[(String, IPrototypeDataProxyUI)] = List.empty) extends ISamplingDataUI {

  def coreObject(factors: List[IFactorDataUI],
                 samplings: List[Sampling]) = try {
    val fi = new File(csvFilePath)
    val sampling = CSVSampling(fi)
    prototypeMapping.filter(!_._2.dataUI.isInstanceOf[EmptyPrototypeDataUI]).foreach {
      m ⇒ sampling addColumn (m._1, m._2.dataUI.coreObject)
    }
    sampling
  } catch {
    case e: Throwable ⇒ throw new UserBadDataError("CSV file path is not correct for the CSV Sampling")
  }

  def coreClass = classOf[CSVSampling]

  def imagePath = "img/csvSampling.png"

  override def fatImagePath = "img/csvSampling_fat.png"

  def buildPanelUI = new CSVSamplingPanelUI(this)

  def inputs = new InputSampling

  def isAcceptable(factor: IDomainDataUI[_]) = true

  def isAcceptable(sampling: ISamplingDataUI) = true

  def preview = "from " + new File(csvFilePath).getName
}
