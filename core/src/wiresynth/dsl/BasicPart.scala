// Copyright 2025 Jelly Terra <jellyterra@proton.me>
// Use of this source code form is governed under the GNU Lesser General Public License version 2.1

package wiresynth.dsl

import scala.annotation.targetName

trait BasicPart extends Part {
  val value: Quantity
}

trait Polar2T extends BasicPart {
  val P = Pin() @@ 1
  val N = Pin() @@ 2

  @targetName("connectPinRhs")
  infix def |-(pin: Pin): Pin = {
    N <> pin
    pin
  }
}

trait Neutral2T extends BasicPart {
  val A = Pin() @@ 1
  val B = Pin() @@ 2
}
