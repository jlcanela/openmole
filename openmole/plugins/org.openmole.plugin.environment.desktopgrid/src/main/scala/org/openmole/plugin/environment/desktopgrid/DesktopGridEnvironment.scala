/*
 * Copyright (C) 2011 Romain Reuillon
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

package org.openmole.plugin.environment.desktopgrid

import org.openmole.core.batch.authentication.Authentication
import org.openmole.core.batch.environment._
import org.openmole.core.batch.storage._
import java.util.concurrent.Executors

import org.openmole.misc.workspace.Workspace
import org.openmole.misc.sftpserver.SFTPServer
import org.openmole.misc.tools.io.FileUtil._
import java.io.File
import java.net.URI
import fr.iscpif.gridscale.storage.{ LocalStorage ⇒ GSLocalStorage }

object DesktopGridEnvironment {
  val timeStempsDirName = "timeStemps"
  val jobsDirName = "jobs"
  val resultsDirName = "results"
  val timeStempSeparator = '@'
}

class DesktopGridEnvironment(
    val port: Int,
    val login: String, password: String,
    override val runtimeMemory: Option[Int] = None) extends BatchEnvironment { env ⇒

  type SS = VolatileStorageService
  type JS = DesktopGridJobService

  val path = Workspace.newDir
  new SFTPServer(path, login, password, port)

  val url = new URI("desktop", login, "localhost", port, null, null, null)
  val id = url.toString

  @transient lazy val batchStorage = new VolatileStorageService {
    def environment = env
    val remoteStorage = new DumyStorage
    def url = env.url
    def root = path.getAbsolutePath
    def connections = Int.MaxValue
    val storage = new GSLocalStorage {}
    val authentication: Unit = Unit
  }

  @transient override lazy val allStorages = List(batchStorage)

  @transient override lazy val allJobServices = List(
    new DesktopGridJobService {
      def environment = env
      def id = env.id
    })

}
