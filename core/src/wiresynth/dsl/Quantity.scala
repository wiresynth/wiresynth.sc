// Copyright 2025 Jelly Terra <jellyterra@proton.me>
// Use of this source code form is governed under the GNU Lesser General Public License version 2.1

package wiresynth.dsl

import scala.math.pow

trait Quantity {
  val value: Double
  def toLiteral: String
}

object Quantity {
  case class Nil() extends Quantity {
    val value = 0
    override def toLiteral: String = "Nil"
  }
}

class Voltage(v: Double, exp: Int) extends Quantity {
  val value = v * pow(10, exp)

  override def toLiteral = s"${value}V"
}

class Capacitance(v: Double, exp: Int) extends Quantity {
  val value = v * pow(10, exp)

  override def toLiteral = s"${value}F"
}

class Inductance(v: Double, exp: Int) extends Quantity {
  val value = v * pow(10, exp)

  override def toLiteral = s"${value}H"
}

class Resistance(v: Double, exp: Int) extends Quantity {
  val value = v * pow(10, exp)

  override def toLiteral = s"${value}Ohm"
}

class Frequency(v: Double, exp: Int) extends Quantity {
  val value = v * pow(10, exp)

  override def toLiteral = s"${value}Hz"
}

implicit class DoubleCast(val v: Double) {

  // Voltage

  def mV: Voltage = Voltage(v, -3)

  def V: Voltage = Voltage(v, 1)

  // Resistance

  def mOhm: Resistance = Resistance(v, -3)

  def Ohm: Resistance = Resistance(v, 1)

  def kOhm: Resistance = Resistance(v, 3)

  def MOhm: Resistance = Resistance(v, 6)

  // Inductance

  def nH: Inductance = Inductance(v, -9)

  def uH: Inductance = Inductance(v, -6)

  def mH: Inductance = Inductance(v, -3)

  def H: Inductance = Inductance(v, 1)

  // Capacitance

  def nF: Capacitance = Capacitance(v, -9)

  def uF: Capacitance = Capacitance(v, -6)

  def mF: Capacitance = Capacitance(v, -3)

  def F: Capacitance = Capacitance(v, 1)

  // Frequency

  def Hz: Frequency = Frequency(v, 1)

  def kHz: Frequency = Frequency(v, 3)

  def MHz: Frequency = Frequency(v, 6)

  def GHz: Frequency = Frequency(v, 9)
}
