/*
 * Copyright (C) 2010 Romain Reuillon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openmole.core.batch.control

import scala.concurrent.stm._

trait UsageControl {
  def waitAToken: AccessToken
  def tryGetToken: Option[AccessToken]
  def releaseToken(token: AccessToken)
  def available: Int

  def tryWithToken[B](f: Option [AccessToken] => B) = {
    val t = tryGetToken
    try f(t)
    finally t.foreach(releaseToken)
  }

  def withToken[B](f: (AccessToken ⇒ B)): B = {
    val token = waitAToken
    try f(token)
    finally releaseToken(token)
  }
}
