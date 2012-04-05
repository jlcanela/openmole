/*
 * Copyright (C) 2011 reuillon
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

package org.openmole.ui.console.command.viewer

import java.util.concurrent.atomic.AtomicInteger
import java.util.logging.Level
import java.util.logging.Logger
import org.apache.commons.cli.BasicParser
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.OptionBuilder
import org.apache.commons.cli.Options
import org.apache.commons.cli.ParseException
import org.openmole.core.batch.control.ServiceDescription
import org.openmole.core.batch.environment.BatchEnvironment
import org.openmole.core.model.execution.ExecutionState
import org.openmole.core.model.execution.IEnvironment
import scala.collection.mutable.HashMap

class BatchEnvironmentViewer extends IViewer {
  //val environmentViewer = new EnvironmentViewer

  override def view(obj: Object, args: Array[String]) = {
    import IViewer.Separator
    
    val options = new Options
    options.addOption("v", true, "level of verbosity")

    try {
      val parser = new BasicParser
      val commandLine = parser.parse(options, args)

      val v = if (commandLine.hasOption('v')) {
        commandLine.getOptionValue('v').toInt
      } else 0

      val accounting = new Array[AtomicInteger](ExecutionState.values.size)
      val executionJobRegistry = obj.asInstanceOf[BatchEnvironment].jobRegistry

      for (state <- ExecutionState.values) {
        accounting(state.id) = new AtomicInteger
      }

      for (executionJob <- executionJobRegistry.allExecutionJobs) {
        accounting(executionJob.state.id).incrementAndGet
      }

      for (state <- ExecutionState.values) {
        System.out.println(state.toString + ": " + accounting(state.id))
      }
      
      if (v >= 1) {
        System.out.println(Separator)
        val jobServices = new HashMap[ServiceDescription, HashMap[ExecutionState.Value, AtomicInteger]]

        for (executionJob <- executionJobRegistry.allExecutionJobs) {
          executionJob.batchJob match {
            case Some(batchJob) =>             
              val jobServiceInfo = jobServices.getOrElseUpdate(batchJob.jobServiceDescription,  new HashMap[ExecutionState.Value, AtomicInteger])
              val nb = jobServiceInfo.getOrElseUpdate(batchJob.state, new AtomicInteger)
              nb.incrementAndGet
            case None =>
          }
        }

        jobServices.foreach {
          case(description, map) =>
            System.out.print(description.toString + ":")

            ExecutionState.values.foreach {
              state => if(map.isDefinedAt(state))System.out.print(" [" + state.toString + " = " + map.get(state).get + "]")
            }
            System.out.println
        }
      }

      if (v >= 2) {
        System.out.println(Separator)

        val executionJobRegistry = obj.asInstanceOf[BatchEnvironment].jobRegistry
        for (executionJob <- executionJobRegistry.allExecutionJobs) {
          executionJob.batchJob match {
            case Some(batchJob) => System.out.println(batchJob.toString + " " + batchJob.state.toString)
            case None =>
          }
        }
      }
    } catch {
      case ex: ParseException =>
        Logger.getLogger(classOf[BatchEnvironmentViewer].getName).log(Level.SEVERE, "Wrong arguments format.")
        new HelpFormatter().printHelp(" ", options)
    }

  }
}