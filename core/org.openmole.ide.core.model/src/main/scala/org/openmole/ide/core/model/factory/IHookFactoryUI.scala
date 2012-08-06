/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openmole.ide.core.model.factory

import org.openmole.core.model.hook.ICapsuleExecutionHook
import org.openmole.ide.core.model.data.IHookDataUI

trait IHookFactoryUI extends IFactoryUI {
  override def toString: String = ""

  def buildDataUI: IHookDataUI

  def coreClass: Class[_ <: ICapsuleExecutionHook]
}