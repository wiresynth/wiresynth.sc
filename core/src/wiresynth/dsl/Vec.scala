// Copyright 2025 Jelly Terra <jellyterra@proton.me>
// Use of this source code form is governed under the GNU Lesser General Public License version 2.1

package wiresynth.dsl

import scala.reflect.ClassTag

class Vec[T <: Data](val array: Array[T]) extends Data {
  def apply(index: Int): T = array(index)
}

object Vec {
  def fill[T <: Data: ClassTag](n: Int)(elem: => T): Vec[T] = {
    val vec = Vec(Array.fill(n)(elem))
    vec.array.zipWithIndex.foreach { (elem, i) =>
      vec.children += elem.withMark(s"$i")
    }
    vec.withMarkDiscarded()
  }
}
