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

package org.openmole.core.model.execution

import java.util.logging.Level
import org.openmole.core.model.job.IJob
import org.openmole.core.model.job.IMoleJob
import org.openmole.misc.eventdispatcher.Event
import ExecutionState._

object Environment {
  case class JobSubmitted(val job: IExecutionJob) extends Event[Environment]
  case class JobStateChanged(val job: IExecutionJob, val newState: ExecutionState, oldState: ExecutionState) extends Event[Environment]
  case class ExceptionRaised(val job: IExecutionJob, val exception: Throwable, val level: Level) extends Event[Environment]
  case class MoleJobExceptionRaised(val job: IExecutionJob, val exception: Throwable, val level: Level, val moleJob: IMoleJob) extends Event[Environment]
}

trait Environment {
  def submit(job: IJob)
}
