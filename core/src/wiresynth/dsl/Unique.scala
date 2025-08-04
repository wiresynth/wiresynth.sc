// Copyright 2025 Jelly Terra <jellyterra@proton.me>
// Use of this source code form is governed under the GNU Lesser General Public License version 2.1

package wiresynth.dsl

object Unique {
  var N = 0
}

trait Unique {
  val unique = {
    Unique.N += 1
    Unique.N
  }

  override def equals(obj: Any): Boolean =
    obj.asInstanceOf[Unique].unique == unique

  override def hashCode(): Int = unique
}
