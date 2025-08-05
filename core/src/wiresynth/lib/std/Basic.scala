// Copyright 2025 Jelly Terra <jellyterra@proton.me>
// Use of this source code form is governed under the GNU Lesser General Public License version 2.1

package wiresynth.lib.std

import wiresynth.dsl.*

import scala.language.postfixOps

object Basic {
  case class R(value: Resistance) extends Neutral2T {
    override val valueLiteral = value.toLiteral

    val prefix = "R"
  }

  case class L(value: Inductance) extends Neutral2T {
    override val valueLiteral = value.toLiteral

    val prefix = "L"
  }

  case class C(value: Capacitance) extends Neutral2T {
    override val valueLiteral = value.toLiteral

    val prefix = "C"
  }

  case class CP(value: Capacitance) extends Polar2T {
    override val valueLiteral = value.toLiteral

    val prefix = "CP"
  }
}
