/*
 * Copyright (C) 2012 Romain Reuillon
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

package org.openmole.core.implementation.validation

import org.openmole.core.implementation.mole._
import org.openmole.core.implementation.task._
import org.openmole.core.implementation.transition._
import org.openmole.core.implementation.data._
import org.openmole.core.implementation.tools._
import DataflowProblem._
import org.openmole.core.model.data._
import org.openmole.core.model.mole._
import org.openmole.core.model.task._
import org.openmole.core.model.transition._

import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class ValidationSpec extends FlatSpec with ShouldMatchers {

  implicit val plugins = PluginSet.empty

  "Validation" should "detect a missing input error" in {
    val p = Prototype[String]("t")

    val t1 = EmptyTask("t1")
    val t2 = EmptyTask("t2")
    t2 addInput p

    val c1 = new Capsule(t1)
    val c2 = new Capsule(t2)

    val mole = c1 -- c2

    val errors = Validation.typeErrors(mole)(mole.capsules)
    errors.headOption match {
      case Some(MissingInput(_, d)) ⇒ assert(d.prototype == p)
      case _ ⇒ sys.error("Error should have been detected")
    }
  }

  "Validation" should "not detect a missing input error" in {
    val p = Prototype[String]("t")

    val t1 = EmptyTask("t1")
    val t2 = EmptyTask("t2")
    t2 addInput p
    t2 addParameter (p -> "Test")

    val c1 = new Capsule(t1)
    val c2 = new Capsule(t2)

    val mole = c1 -- c2

    Validation.typeErrors(mole)(mole.capsules).isEmpty should equal(true)
  }

  "Validation" should "detect a type error" in {
    val pInt = Prototype[Int]("t")
    val pString = Prototype[String]("t")

    val t1 = EmptyTask("t1")
    t1 addOutput pInt

    val t2 = EmptyTask("t2")
    t2 addInput pString

    val c1 = new Capsule(t1)
    val c2 = new Capsule(t2)

    val mole = c1 -- c2

    val errors = Validation.typeErrors(mole)(mole.capsules)
    errors.headOption match {
      case Some(WrongType(_, d, t)) ⇒
        assert(d.prototype == pString)
        assert(t == pInt)
      case _ ⇒ sys.error("Error should have been detected")
    }
  }

  "Validation" should "detect a topology error" in {
    val p = Prototype[String]("t")

    val t1 = EmptyTask("t1")
    val t2 = EmptyTask("t2")

    val c1 = Slot(t1)
    val c2 = new Capsule(t2)

    val mole = c1 -< c2 -- c1

    val errors = Validation.topologyErrors(mole)
    errors.isEmpty should equal(false)
  }

  "Validation" should "detect a duplicated transition" in {
    val p = Prototype[String]("t")

    val t1 = EmptyTask("t1")
    val t2 = EmptyTask("t2")

    val c1 = new Capsule(t1)
    val c2 = Slot(t2)

    val mole = (c1 -- c2) + (c1 -- c2)

    val errors = Validation.duplicatedTransitions(mole)
    errors.isEmpty should equal(false)
  }

  "Validation" should "detect a missing input error due to datachannel filtering" in {
    val p = Prototype[String]("t")

    val t1 =
      new TestTask {
        val name = "t1"
        override def outputs = DataSet(p)
        override def process(context: Context) = Context(Variable(p, "test"))
      }

    val t2 = EmptyTask("t2")
    val t3 = EmptyTask("t2")
    t3 addInput p

    val c1 = new Capsule(t1)
    val c2 = new Capsule(t2)
    val c3 = Slot(t3)

    val mole = (c1 -- c2 -- c3) + (c1 oo (c3, Filter(p)))

    val errors = Validation.typeErrors(mole)(mole.capsules)

    errors.headOption match {
      case Some(MissingInput(_, d)) ⇒ assert(d.prototype == p)
      case _ ⇒ sys.error("Error should have been detected")
    }
  }

  "Validation" should "detect a missing input in the submole" in {
    val p = Prototype[String]("t")

    val t1 = EmptyTask("t1")

    val t2 = EmptyTask("t2")
    t2 addInput p

    val c1 = Capsule(t1)
    val c2 = Capsule(t2)

    val mt = MoleTask("mt", c1 -- c2)

    val errors = Validation(new Mole(mt))

    errors.headOption match {
      case Some(MissingInput(_, d)) ⇒ assert(d.prototype == p)
      case _ ⇒ sys.error("Error should have been detected")
    }

  }

  "Validation" should "not detect a missing input" in {
    val p = Prototype[String]("t")

    val t1 =
      new TestTask {
        val name = "t1"
        override def outputs = DataSet(p)
        override def process(context: Context) = Context(Variable(p, "test"))
      }

    val c1 = new Capsule(t1)

    val t2 = EmptyTask("t2")
    t2 addInput p
    val c2 = new Capsule(t2)

    val mt = MoleTask("mt", c2)

    val mtC = new Capsule(mt)

    val mole = c1 -- mtC

    val errors = Validation(mole)
    errors.isEmpty should equal(true)
  }

  "Validation" should "detect a duplicated name error" in {
    val pInt = Prototype[Int]("t")
    val pString = Prototype[String]("t")

    val t1 = EmptyTask("t1")
    t1 addOutput pInt
    t1 addOutput pString

    val c1 = new Capsule(t1)

    val errors = Validation.duplicatedName(new Mole(c1))
    errors.headOption match {
      case Some(DuplicatedName(_, _, _, Output)) ⇒
      case _ ⇒ sys.error("Error should have been detected")
    }
  }

}
