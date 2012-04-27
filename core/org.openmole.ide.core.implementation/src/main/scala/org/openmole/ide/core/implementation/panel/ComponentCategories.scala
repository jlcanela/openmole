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

package org.openmole.ide.core.implementation.panel

object ComponentCategories {
  
  val ABM_TASK = new ComponentCategory("agent based model",
                                       List())
  
  val SENSITIVITY_TASK = new ComponentCategory("sensitivity",
                                               List(new ComponentCategory("Saltelli",List())))
  
  val STORAGE_TASK = new ComponentCategory("storage",
                                               List())
           
  val TASK = new ComponentCategory("New",List(ABM_TASK,SENSITIVITY_TASK,STORAGE_TASK))
  
  val PROTOTYPE = new ComponentCategory("New",List())
  
  val ENVIRONMENT = new ComponentCategory("New",List())
  
  val SAMPLING = new ComponentCategory("New",List())
}
