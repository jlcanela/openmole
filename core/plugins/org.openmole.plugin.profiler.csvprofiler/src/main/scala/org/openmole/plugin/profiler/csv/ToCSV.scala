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

package org.openmole.plugin.profiler.csvprofiler

import org.openmole.core.model.job.IMoleJob
import org.openmole.core.model.tools.ITimeStamp

object ToCSV {

  def toColumns(moleJob: IMoleJob): Array[String] = {
    val timeStamps = moleJob.timeStamps
    val toWrite = new Array[String]((timeStamps.size) + 3)

    toWrite(0) = moleJob.task.name
    val (created, timeStampsStr) = toCSV(timeStamps)
    toWrite(1) = created.toString
    toWrite(2) = moleJob.id.toString
    timeStampsStr.zipWithIndex.foreach { case (timeStamp, i) ⇒ toWrite(i + 3) = timeStamp }
    toWrite
  }

  def toCSV(timeStamps: Iterable[ITimeStamp[_]]) = {
    val created = timeStamps.head.time
    (created, timeStamps.map {
      timeStamp ⇒ timeStamp.state.toString + ':' + timeStamp.hostName + ':' + (timeStamp.time - created).toString
    })
  }
}
