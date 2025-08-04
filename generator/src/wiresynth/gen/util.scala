// Copyright 2025 Jelly Terra <jellyterra@proton.me>
// Use of this source code form is governed under the GNU Lesser General Public License version 2.1

package wiresynth.gen

def sb(u: StringBuffer => Unit): String = {
  val s = StringBuffer()
  u(s)
  s.toString()
}
