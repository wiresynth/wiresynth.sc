// Copyright 2025 Jelly Terra <jellyterra@proton.me>
// Use of this source code form is governed under the GNU Lesser General Public License version 2.1

package wiresynth.gen

import wiresynth.elab.Elaboration

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}
import java.text.SimpleDateFormat
import java.util.Calendar

class Kicad(e: Elaboration, path: String) {
  val netlist = sb { c =>
    c.append(s"""
       |(export (version D)
       |(design
       |(date "${new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                 Calendar.getInstance.getTime
               )}")
       |(tool "WireSynth")
       |)
       |(components
       |""".stripMargin)
    e.parts.foreach { part =>
      c.append(s"""
         |(comp (ref "${part.canonicalName}")
         |(value ${part.valueLiteral})
         |(footprint ${part.footprint})
         |)
         |""".stripMargin)
    }
    c.append(s")\n(nets\n")
    e.nets.zipWithIndex.foreach { (net, i) =>
      var netName = net.pins.last.canonicalName
      net.pins.foreach { pin =>
        if pin.canonicalName.length < netName.length then {
          netName = pin.canonicalName
        }
      }
      c.append(
        s"(net (code $i) (name $netName)\n"
      )
      net.partPins.foreach { pin =>
        pin.pos.foreach { pos =>
          c.append(
            s"(node (ref ${pin.part.get.canonicalName}) (pin ${pos}))\n"
          )
        }
      }
      c.append(")\n")
    }
    c.append("))")
  }

  Files.write(
    Paths.get(path),
    netlist.getBytes(StandardCharsets.UTF_8)
  )

  println("Netlist generated successfully.")
}
