// Copyright 2025 Jelly Terra <jellyterra@proton.me>
// Use of this source code form is governed under the GNU Lesser General Public License version 2.1

package wiresynth.dsl

import scala.collection.mutable

trait Data extends Unique {
  var name          = ""
  var canonicalName = ""

  val children = mutable.LinkedHashSet[Data]()

  def addChild(child: Data): Unit = {
    children += child
  }

  // For compiler plugin.
  def autoMark(newName: String): this.type = {
    if name.isEmpty then {
      name = newName
    }
    this
  }

  def withMark(newName: String): this.type = {
    name = newName
    this
  }

  def withMarkDiscarded(): this.type = {
    name = ""
    this
  }

  def walk[T](prev: T, act: (Data, T) => T): Unit = {
    val next = act(this, prev)
    children.foreach { child => child.walk(next, act) }
  }

  def elabCanonicalName(prefix: String): String = {
    if name.isEmpty then {
      return "<empty>"
    }
    s"$prefix$name"
  }
}
