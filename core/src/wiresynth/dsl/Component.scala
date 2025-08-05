// Copyright 2025 Jelly Terra <jellyterra@proton.me>
// Use of this source code form is governed under the GNU Lesser General Public License version 2.1

package wiresynth.dsl

import wiresynth.lib.std.Basic

import scala.collection.mutable

// Component itself does not present physical part.
// It's a medium to conduct relations and connections.
trait Component extends Data {
  val isPart = false

  val pins  = mutable.LinkedHashSet[Pin]()
  val parts = mutable.LinkedHashSet[Part]()

  // Allocate unique number for basic parts in this component.
  val partCounter = mutable.HashMap[String, Int]()

  override def addChild(child: Data): Unit = {
    children += child
    child match {
      case basicPart: BasicPart =>
        parts += basicPart
        val counter = partCounter.get(basicPart.prefix) match {
          case None =>
            partCounter.put(basicPart.prefix, 1)
            0
          case Some(counter) =>
            partCounter.put(basicPart.prefix, counter + 1)
            counter
        }

        basicPart.name = s"${basicPart.prefix}$counter"
      case part: Part =>
        parts += part
      case pin: Pin =>
        pins += pin
        if isPart then {
          pin.part = Some(asInstanceOf[Part])
        }
      case _ =>
    }
  }

  def R(v: Resistance) = {
    val p = Basic.R(v)
    addChild(p)
    p
  }

  def L(v: Inductance) = {
    val p = Basic.L(v)
    addChild(p)
    p
  }

  def C(v: Capacitance) = {
    val p = Basic.C(v)
    addChild(p)
    p
  }

  def CP(v: Capacitance) = {
    val p = Basic.C(v)
    addChild(p)
    p
  }
}
