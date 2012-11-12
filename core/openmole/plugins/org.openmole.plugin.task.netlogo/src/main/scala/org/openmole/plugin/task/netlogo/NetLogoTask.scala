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

package org.openmole.plugin.task.netlogo

import java.io.File
import java.util.AbstractCollection
import org.openmole.core.implementation.data._
import org.openmole.core.implementation.tools._
import org.openmole.core.model.data._
import org.openmole.core.model.task._
import org.openmole.plugin.task.external._

object NetLogoTask {

  class Workspace(val location: Either[(File, String), File]) {
    def this(workspace: File, script: String) = this(Left(workspace, script))
    def this(script: File) = this(Right(script))
  }

}

class NetLogoTask(
    val name: String,
    val workspace: NetLogoTask.Workspace,
    val launchingCommands: Iterable[String],
    val netLogoInputs: Iterable[(Prototype[_], String)],
    val netLogoOutputs: Iterable[(String, Prototype[_])],
    val netLogoFactory: NetLogoFactory,
    val inputs: DataSet,
    val outputs: DataSet,
    val parameters: ParameterSet,
    val inputFiles: Iterable[(Prototype[File], String, Boolean)],
    val outputFiles: Iterable[(String, Prototype[File])],
    val resources: Iterable[(File, String, Boolean)])(implicit val plugins: PluginSet) extends ExternalTask {

  val scriptPath =
    workspace.location match {
      case Left((f, s)) ⇒ f.getName + "/" + s
      case Right(s) ⇒ s.getName
    }

  override def process(context: Context): Context = {

    val tmpDir = org.openmole.misc.workspace.Workspace.newDir("netLogoTask")
    val links = prepareInputFiles(context, tmpDir)

    val script = new File(tmpDir, scriptPath)
    val netLogo = netLogoFactory()
    try {
      netLogo.open(script.getAbsolutePath)

      for (inBinding ← netLogoInputs) {
        val v = context.value(inBinding._1).get
        netLogo.command("set " + inBinding._2 + " " + v.toString)
      }

      for (cmd ← launchingCommands) netLogo.command(VariableExpansion(context, cmd))

      fetchOutputFiles(context, tmpDir, links) ++ netLogoOutputs.map {
        case (name, prototype) ⇒
          val outputValue = netLogo.report(name)
          if (!prototype.`type`.runtimeClass.isArray) Variable(prototype.asInstanceOf[Prototype[Any]], outputValue)
          else {
            val netlogoCollection = outputValue.asInstanceOf[AbstractCollection[Any]]
            val array = java.lang.reflect.Array.newInstance(prototype.`type`.runtimeClass.getComponentType, netlogoCollection.size)
            val it = netlogoCollection.iterator
            for (i ← 0 until netlogoCollection.size) java.lang.reflect.Array.set(array, i, it.next)
            Variable(prototype.asInstanceOf[Prototype[Any]], array)
          }
      }
    } finally netLogo.dispose

  }

}
