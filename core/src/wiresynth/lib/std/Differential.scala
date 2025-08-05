// Copyright 2025 Jelly Terra <jellyterra@proton.me>
// Use of this source code form is governed under the GNU Lesser General Public License version 2.1

package wiresynth.lib.std

import wiresynth.dsl.*

case class Differential() extends Bundle {
  val P = Pin()
  val N = Pin()
}
