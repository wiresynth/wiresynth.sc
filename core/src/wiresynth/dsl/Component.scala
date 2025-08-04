// Copyright 2025 Jelly Terra <jellyterra@proton.me>
// Use of this source code form is governed under the GNU Lesser General Public License version 2.1

package wiresynth.dsl

import scala.collection.mutable

// Component itself does not
trait Component extends Data {
  val isPart = false

  val pins  = mutable.LinkedHashSet[Pin]()
  val parts = mutable.LinkedHashSet[Part]()

  override def addChild(child: Data): Unit = {
    children += child
    child match {
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
}
