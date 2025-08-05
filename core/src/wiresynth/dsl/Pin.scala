// Copyright 2025 Jelly Terra <jellyterra@proton.me>
// Use of this source code form is governed under the GNU Lesser General Public License version 2.1

package wiresynth.dsl

import scala.annotation.targetName
import scala.collection.mutable

case class Pin() extends Data {
  var part: Option[Part] = None
  var net:  Option[Net]  = None

  // Position of pin in the footprint.
  val pos = mutable.ArrayBuffer[String]()

  // Output flag.
  var isDriver = false

  def joinNet(anotherNet: Net) = {
    anotherNet.addPin(this)
    net = Some(anotherNet)
  }

  def asDriver: Pin = {
    isDriver = true
    this
  }

  @targetName("at")
  infix def @@(pinPos: Int): Pin = {
    pos += s"$pinPos"
    this
  }

  @targetName("at")
  infix def @@(pinPos: String): Pin = {
    pos += pinPos
    this
  }

  @targetName("connectPin")
  infix def <>(another: Pin): Pin = {
    net match {
      case Some(net) =>
        another.net match {
          case Some(anotherNet) =>
            net.merge(anotherNet)
          case None =>
            another.joinNet(net)
        }
      case None =>
        another.net match {
          case Some(net) =>
            joinNet(net)
          case None =>
            val net = Net()
            joinNet(net)
            another.joinNet(net)
        }
    }
    this
  }

  @targetName("connectNeutral2T")
  infix def <>(part: Neutral2T): Pin = {
    part.A <> this
    part.B
  }

  @targetName("connectPolar2TLhs")
  infix def -|+(part: Polar2T): Polar2T = {
    part.P <> this
    part
  }
}
