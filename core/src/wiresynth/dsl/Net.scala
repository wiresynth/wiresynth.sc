// Copyright 2025 Jelly Terra <jellyterra@proton.me>
// Use of this source code form is governed under the GNU Lesser General Public License version 2.1

package wiresynth.dsl

import scala.collection.mutable

case class Net() extends Unique {
  val pins, driverPins, partPins = mutable.Set[Pin]()

  def merge(another: Net) = {
    another.pins.map(pin => pin.joinNet(this))
  }

  def addPin(pin: Pin) = {
    pin.part.foreach { part =>
      partPins += pin

      if pin.isDriver then {
        driverPins += pin
      }
    }
    pins += pin
  }
}
