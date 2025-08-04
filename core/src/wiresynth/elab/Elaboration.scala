// Copyright 2025 Jelly Terra <jellyterra@proton.me>
// Use of this source code form is governed under the GNU Lesser General Public License version 2.1

package wiresynth.elab

import wiresynth.dsl

import scala.collection.mutable

class Elaboration(topLevel: dsl.Component) {

  val parts = mutable.LinkedHashSet[dsl.Part]()
  val nets  = mutable.LinkedHashSet[dsl.Net]()

  topLevel.children.foreach { it =>
    it.walk(
      "",
      (data, prefix) => {
        val next = data.elabCanonicalName(prefix)
        data.canonicalName = next

        data match {
          case pin: dsl.Pin =>
            pin.net.foreach { net =>
              // Store alive only.
              if net.partPins.size > 1 then {
                nets += net

                net.partPins.foreach { pin =>
                  // Alive pin with alive part.
                  parts += pin.part.get
                }
              }
            }
          case _ =>
        }

        s"${next}_"
      }
    )
  }
}
