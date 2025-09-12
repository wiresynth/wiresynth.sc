// Copyright 2025 Jelly Terra <jellyterra@proton.me>
// Use of this source code form is governed under the GNU Lesser General Public License version 2.1

package wiresynth.gen

import wiresynth.elab.Elaboration
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.charset.StandardCharsets

class BOM(e: Elaboration, path: String) {
    val bom = sb { c=>
        c.append("\"Qty\",\"Footprint\",\"Name\"\n")
        for (name, partSet) <- e.parts.groupBy { part => part.getClass().getCanonicalName() } do {
            val parts = partSet.toArray
            c.append(s"\"${parts.length}\",\"${parts(0).footprint}\",\"${name}\"\n")
        }
    }

    Files.write(Paths.get(path), bom.getBytes(StandardCharsets.UTF_8))

    println("BOM CSV generated successfully.")
}
