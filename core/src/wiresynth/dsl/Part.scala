// Copyright 2025 Jelly Terra <jellyterra@proton.me>
// Use of this source code form is governed under the GNU Lesser General Public License version 2.1

package wiresynth.dsl

trait Part extends Component {
  override val isPart = true

  val valueLiteral = ""

  var footprint = ""

  infix def packageIn(f: String): this.type = {
    footprint = f
    this
  }
}
