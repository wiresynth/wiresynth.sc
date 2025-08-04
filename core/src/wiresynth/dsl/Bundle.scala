// Copyright 2025 Jelly Terra <jellyterra@proton.me>
// Use of this source code form is governed under the GNU Lesser General Public License version 2.1

package wiresynth.dsl

import scala.annotation.targetName

trait Bundle extends Component {
  @targetName("connect")
  infix def <>(another: Bundle): Unit = {
    pins.zipWithIndex.foreach { (pin, i) =>
      another.pins.find(theOther => theOther.name == pin.name).foreach {
        anotherPin =>
          anotherPin <> pin
      }
    }
  }
}
